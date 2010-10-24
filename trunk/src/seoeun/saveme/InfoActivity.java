package seoeun.saveme;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        
        // get information
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
            descriptionRes = R.string.app_description;
            version = "unknown";
        }
        
        // get views
        ImageView vIcon = (ImageView) findViewById(R.id.about_icon);
        TextView vName = (TextView) findViewById(R.id.about_name);
        TextView vVersion = (TextView) findViewById(R.id.about_version);
        TextView vDescription = (TextView) findViewById(R.id.about_description);
        TextView vLink = (TextView) findViewById(R.id.about_link);
        TextView vCopyright = (TextView) findViewById(R.id.about_copyright);
        TextView vContact = (TextView) findViewById(R.id.about_contact);
        
        // set values
        vIcon.setImageResource(R.drawable.app_icon);
        vName.setText(R.string.app_name);
        vVersion.setText(version);
        vDescription.setText(descriptionRes);
        vLink.setText(R.string.app_link);
        vCopyright.setText(R.string.app_copyright);
        vContact.setText(R.string.app_contact);
        
        // linkify
        Linkify.addLinks(vLink, Linkify.ALL);
        Linkify.addLinks(vCopyright, Linkify.ALL);
        Linkify.addLinks(vContact, Linkify.ALL);
    }

}
