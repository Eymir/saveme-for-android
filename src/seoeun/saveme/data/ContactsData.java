package seoeun.saveme.data;

import java.util.ArrayList;
import java.util.List;

import seoeun.saveme.SaveMeLogger;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;


public final class ContactsData {
    
    private static final String KEY_CONTACTS = "contacts";
    
    private static final String SEPARATOR = ",";
    
    private ContactsData() {
    }
    
    public static List<Long> get(Context context) {
        SharedPreferences preferences = SaveMePreferences.getInstance(context);
        String value = preferences.getString(KEY_CONTACTS, null);
        SaveMeLogger.d("GET_CONTACTS: " + value);
        
        List<Long> contacts;
        if(value != null) {
            String[] strings;
            try {
                strings = value.split(SEPARATOR);
            } catch(Exception e) {
                strings = null;
                e.printStackTrace();
            }
            
            if(strings != null) {
                int count = strings.length;
                contacts = new ArrayList<Long>(count);
                for(int i = 0; i < count; i++) {
                    try {
                        contacts.add(Long.parseLong(strings[i]));
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                contacts = null;
            }
        } else {
            contacts = null;
        }
        
        return contacts;
    }
    
    public static void set(Context context, List<Long> contacts) {
        String value;
        if(contacts != null) {
            int count = contacts.size();
            if(count > 0) {
                StringBuilder buf = new StringBuilder(24);
                for(int i = 0; i < count; i++) {
                    buf.append(String.valueOf(contacts.get(i)));
                    if(i < count - 1) {
                        buf.append(SEPARATOR);
                    }
                }
                value = buf.toString();
            } else {
                value = null;
            }
        } else {
            value = null;
        }
        
        SaveMeLogger.d("SET_CONTACTS: " + value);
        SharedPreferences preferences = SaveMePreferences.getInstance(context);
        preferences.edit().putString(KEY_CONTACTS, value).commit();
    }
    
    public static final void query(ContentResolver resolver, long id, String[] result) {
        Uri uri = Uri.withAppendedPath(Phone.CONTENT_URI, String.valueOf(id));
        Cursor cursor = resolver.query(uri,
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
    }
    
}
