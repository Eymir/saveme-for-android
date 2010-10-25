package seoeun.saveme.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public final class MessagesData {

    private static final String KEY_MESSAGES = "messages";
    
    private MessagesData() {
    }
    
    public static String get(Context context) {
        SharedPreferences preferences = SaveMePreferences.getInstance(context);
        String value = preferences.getString(KEY_MESSAGES, null);
        Log.d(MessagesData.class.getSimpleName(), "GET: " + value);
        return value;
    }
    
    public static final void set(Context context, String value) {
        Log.d(MessagesData.class.getSimpleName(), "SET: " + value);
        SharedPreferences preferences = SaveMePreferences.getInstance(context);
        preferences.edit().putString(KEY_MESSAGES, value).commit();
    }
    
}
