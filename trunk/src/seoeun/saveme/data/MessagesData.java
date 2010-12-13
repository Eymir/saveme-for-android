package seoeun.saveme.data;

import seoeun.saveme.SaveMeLogger;
import android.content.Context;
import android.content.SharedPreferences;

public final class MessagesData {

    private static final String KEY_MESSAGES = "messages";
    
    private MessagesData() {
    }
    
    public static String get(Context context) {
        SharedPreferences preferences = SaveMePreferences.getInstance(context);
        String value = preferences.getString(KEY_MESSAGES, null);
        SaveMeLogger.d("GET_MESSAGES: " + value);
        return value;
    }
    
    public static final void set(Context context, String value) {
    	SaveMeLogger.d("SET_MESSAGES: " + value);
        SharedPreferences preferences = SaveMePreferences.getInstance(context);
        preferences.edit().putString(KEY_MESSAGES, value).commit();
    }
    
}
