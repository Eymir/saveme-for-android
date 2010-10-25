package seoeun.saveme;

import seoeun.saveme.data.MessagesData;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    
    private void refresh() {
        String message = MessagesData.get(this);
        ((EditText) findViewById(R.id.message)).setText(message);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Editable message = ((EditText) findViewById(R.id.message)).getText();
        MessagesData.set(this, message.toString());
    }
    
}
