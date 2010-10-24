package seoeun.saveme;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

public class ContactsActivity extends Activity {

    private static final int CODE_PICK_PHONE_NUMBER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Add")
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        pickPhoneNumber();
                        return true;
                    }
                });
        return true;
    }

    private void pickPhoneNumber() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent
                .setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

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
                    addPhoneNumber(data.getData());
                }
                break;
        }
    }

    private void addPhoneNumber(Uri contactUri) {
        /*
         * We should always run database queries on a background thread. The
         * database may be locked by some process for a long time. If we locked
         * up the UI thread while waiting for the query to come back, we might
         * get an "Application Not Responding" dialog.
         */
        AsyncTask<Uri, Void, String[]> task = new AsyncTask<Uri, Void, String[]>() {

            @Override
            protected String[] doInBackground(Uri... uris) {
                String[] result = new String[2];

                Cursor cursor = getContentResolver().query(uris[0],
                        new String[] { Contacts.DISPLAY_NAME, Phone.NUMBER },
                        null, null, null);
                try {
                    if (cursor.moveToFirst()) {
                        result[0] = cursor.getString(0);
                        result[1] = cursor.getString(1);
                    }
                } finally {
                    cursor.close();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String[] result) {
                Toast.makeText(ContactsActivity.this,
                        result[0] + "=" + result[1], Toast.LENGTH_LONG).show();
            }
        };

        task.execute(contactUri);
    }

}
