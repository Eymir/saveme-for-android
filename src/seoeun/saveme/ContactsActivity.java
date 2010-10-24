package seoeun.saveme;

import java.util.ArrayList;
import java.util.List;

import seoeun.saveme.data.ContactsData;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ContactsActivity extends Activity {

    private static final int CODE_PICK_PHONE_NUMBER = 1;

    private List<Long> contacts;

    private ContactListAdapter adapter = new ContactListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        ListView list = (ListView) findViewById(R.id.contacts_list);
        list.setAdapter(adapter);
        registerForContextMenu(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    
    private void refresh() {
        List<Long> contacts = ContactsData.get(this);
        if (contacts != null && !contacts.isEmpty()) {
            findViewById(R.id.contacts_hint).setVisibility(View.GONE);
        } else {
            findViewById(R.id.contacts_hint).setVisibility(View.VISIBLE);
        }
        this.contacts = contacts;
        adapter.refresh();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contacts_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                pickPhoneNumber();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        ListView list = (ListView) findViewById(R.id.contacts_list);
        if(v.equals(list)) {
            getMenuInflater().inflate(R.menu.contacts_context, menu);
        }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete:
                removePhoneNumber(((AdapterContextMenuInfo) item.getMenuInfo()).id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void pickPhoneNumber() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(Phone.CONTENT_ITEM_TYPE);

        try {
            startActivityForResult(intent, CODE_PICK_PHONE_NUMBER);
        } catch (Exception e) {
            // TODO handle the exception!
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_PICK_PHONE_NUMBER:
                if (resultCode == RESULT_OK) {
                    String segment = data.getData().getLastPathSegment();
                    try {
                        long id = Long.parseLong(segment);
                        addPhoneNumber(id);
                    } catch(NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void addPhoneNumber(long id) {
        if(contacts == null) {
            contacts = new ArrayList<Long>();
        }
        contacts.add(id);
        ContactsData.set(this, contacts);
        refresh();
    }
    
    private void removePhoneNumber(long id) {
        if(contacts != null) {
            contacts.remove(id);
        }
        ContactsData.set(this, contacts);
        refresh();
    }

    private class ContactListAdapter extends BaseAdapter {

        private String[] temp = new String[2];

        ContactListAdapter() {
        }

        void refresh() {
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return (contacts != null ? contacts.size() : 0);
        }

        @Override
        public Object getItem(int position) {
            return getItemId(position);
        }

        @Override
        public long getItemId(int position) {
            return (contacts != null ? contacts.get(position) : -1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            Context context = parent.getContext();
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.contacts_item,
                        parent, false);
            } else {
                view = convertView;
            }

            ContactsData.query(context.getContentResolver(),
                    getItemId(position), temp);
            ((TextView) view.findViewById(R.id.title)).setText(temp[0]);
            ((TextView) view.findViewById(R.id.summary)).setText(temp[1]);

            return view;
        }

    }
}
