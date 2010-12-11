package seoeun.saveme;

import java.util.List;

import seoeun.saveme.data.ContactsData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SaveMeActivity extends Activity {
    
    private Dialog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saveme);
        
        final SaveMeApplication app = (SaveMeApplication) getApplication();
        
        View button = findViewById(R.id.btn_saveme);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Long> contacts = ContactsData.get(SaveMeActivity.this);
                if(contacts != null && !contacts.isEmpty()) {
                    app.trigger();
                } else {
                    Toast.makeText(SaveMeActivity.this, R.string.msg_no_contacts_outside, Toast.LENGTH_LONG).show();
                }
            }
        });
        
        // callback
        app.setListener(new SaveMeApplication.Listener() {
            @Override
            public void onTick(final long remain) {
                SaveMeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDialog();
                        if(dialog != null && dialog.isShowing()) {
                            long seconds = (long) Math.ceil(remain / 1000.0f);
                            String text = String.valueOf(seconds);
                            ((TextView) dialog.findViewById(R.id.number)).setText(text);
                        }
                    }
                });
            }
            @Override
            public void onFire() {
                SaveMeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideDialog();
                        Toast.makeText(SaveMeActivity.this, R.string.msg_message_sent, Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onCancel() {
                SaveMeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideDialog();
                    }
                });
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        final SaveMeApplication app = (SaveMeApplication) getApplication();
        app.setListener(null);
        hideDialog();
        super.onDestroy();
    }

    private void showDialog() {
        Dialog d = dialog;
        if(d == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.countdown, null);
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle(R.string.send_message);
            b.setView(v);
            b.setCancelable(false);
            b.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((SaveMeApplication) getApplication()).cancel();
                }
            });
            d = b.show();
            this.dialog = d;
        }
    }
    
    private void hideDialog() {
        Dialog d = dialog;
        if(d != null) {
            if(d.isShowing()) {
                d.dismiss();
            }
            this.dialog = null;
        }
    }

}
