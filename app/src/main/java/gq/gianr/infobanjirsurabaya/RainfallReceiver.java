package gq.gianr.infobanjirsurabaya;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by j on 11/06/2017.
 */

public class RainfallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(RainfallReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        System.out.println("RainfallService Stops!");
        boolean b = isMyServiceRunning(context, RainfallService.class);
        if (!b)context.startService(new Intent(context, RainfallService.class));
    }

    private boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }
}
