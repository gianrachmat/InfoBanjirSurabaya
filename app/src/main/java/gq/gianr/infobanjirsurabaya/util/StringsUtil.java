package gq.gianr.infobanjirsurabaya.util;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by j on 17/03/2017.
 */

public class StringsUtil {
    private static Context context;

    public static void initialize(Context c) {
        context = c;
    }

    public static String getString(@StringRes int stringId) {
        if (context == null)
            throw new IllegalArgumentException("You should call initialize(Context context) method first");
        return context.getString(stringId);
    }
}
