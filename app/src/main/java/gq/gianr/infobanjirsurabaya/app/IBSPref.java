package gq.gianr.infobanjirsurabaya.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by j on 11/06/2017.
 */

public class IBSPref {
    public static long getLastNotificationTimeInMillis(Context context) {
        /* Key for accessing the time at which Sunshine last displayed a notification */
        String lastNotificationKey = "last_notification";

        /* As usual, we use the default SharedPreferences to access the user's preferences */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        long lastNotificationTime = sp.getLong(lastNotificationKey, 0);

        return lastNotificationTime;
    }

    /**
     * Returns the elapsed time in milliseconds since the last notification was shown. This is used
     * as part of our check to see if we should show another notification when the weather is
     * updated.
     *
     * @param context Used to access SharedPreferences as well as use other utility methods
     * @return Elapsed time in milliseconds since the last notification was shown
     */
    public static long getEllapsedTimeSinceLastNotification(Context context) {
        long lastNotificationTimeMillis =
                IBSPref.getLastNotificationTimeInMillis(context);
        long timeSinceLastNotification = System.currentTimeMillis() - lastNotificationTimeMillis;
        return timeSinceLastNotification;
    }

    public static void saveTemp(Context context, long temp){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        String lastNotificationKey = "temp_countdown";
        editor.putLong(lastNotificationKey, temp);
        editor.apply();
    }



    public static long getTemp(Context context){
        String lastNotificationKey = "temp_countdown";

        /* As usual, we use the default SharedPreferences to access the user's preferences */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getLong(lastNotificationKey, 60);
    }

    /**
     * Saves the time that a notification is shown. This will be used to get the ellapsed time
     * since a notification was shown.
     *
     * @param context Used to access SharedPreferences
     * @param timeOfNotification Time of last notification to save (in UNIX time)
     */
    public static void saveLastNotificationTime(Context context, long timeOfNotification) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        String lastNotificationKey = "last_notification";
        editor.putLong(lastNotificationKey, timeOfNotification);
        editor.apply();
    }
}
