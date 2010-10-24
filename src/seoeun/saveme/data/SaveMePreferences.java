package seoeun.saveme.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveMePreferences {
    
    private static final String FILE_NAME = "saveme.preferences";

    public static SharedPreferences getInstance(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }
}
