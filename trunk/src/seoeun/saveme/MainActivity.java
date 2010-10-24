package seoeun.saveme;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost(); // The activity TabHost
        TabHost.TabSpec spec; // Resusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, SaveMeActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("saveme").setIndicator(
                res.getText(R.string.saveme),
                res.getDrawable(R.drawable.ic_tab_saveme)).setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, ContactsActivity.class);
        spec = tabHost.newTabSpec("contacts").setIndicator(
                res.getText(R.string.contacts),
                res.getDrawable(R.drawable.ic_tab_contacts)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SettingsActivity.class);
        spec = tabHost.newTabSpec("settings").setIndicator(
                res.getText(R.string.settings),
                res.getDrawable(R.drawable.ic_tab_settings)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, InfoActivity.class);
        spec = tabHost.newTabSpec("info").setIndicator(
                res.getText(R.string.info),
                res.getDrawable(R.drawable.ic_tab_info)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

}