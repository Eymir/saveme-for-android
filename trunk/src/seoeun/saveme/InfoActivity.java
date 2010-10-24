package seoeun.saveme;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        
        int descriptionRes;
        String version;
        
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch(NameNotFoundException e) {
            info = null;
        }
        
        if(info != null) {
            descriptionRes = info.applicationInfo.descriptionRes;
            version = info.versionName;
        } else {
            descriptionRes = 0;
            version = null;
        }
        
        ((ImageView) findViewById(R.id.about_icon)).setImageResource(R.drawable.app_icon);
        ((TextView) findViewById(R.id.about_name)).setText(R.string.app_name);
        if(version != null) {
            ((TextView) findViewById(R.id.about_version)).setText(version);
        }
        if(descriptionRes > 0) {
            ((TextView) findViewById(R.id.about_description)).setText(descriptionRes);
        }
        
        ((TextView) findViewById(R.id.about_link)).setText(R.string.app_link);
        ((TextView) findViewById(R.id.about_copyright)).setText(R.string.app_copyright);
        ((TextView) findViewById(R.id.about_contact)).setText(R.string.app_contact);
    }

}
