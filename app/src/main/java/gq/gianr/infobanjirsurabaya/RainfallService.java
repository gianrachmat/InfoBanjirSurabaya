package gq.gianr.infobanjirsurabaya;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import github.vatsal.easyweather.Helper.ForecastCallback;
import github.vatsal.easyweather.WeatherMap;
import github.vatsal.easyweather.retrofit.models.ForecastResponseModel;
import github.vatsal.easyweather.retrofit.models.List;
import gq.gianr.infobanjirsurabaya.app.IBSPref;
import gq.gianr.infobanjirsurabaya.model.Notif;

/**
 * Created by j on 11/06/2017.
 */

public class RainfallService extends Service {
    public final String APP_ID = "062e8ee34715b1ebdc5ddf46a8f645d0";
    String city = "Surabaya";
    //    boolean tigaJam = true;
    boolean tigaJam = false;
    DatabaseHandler db;
    long timeLastNotif;
    long temp;

    public RainfallService() {
    }

    public RainfallService(Context ctx) {
        super();
        Log.i("RainfallService", "I'm Here!");
    }

    CountDownTimer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timeLastNotif = IBSPref.getLastNotificationTimeInMillis(this);
        temp = IBSPref.getTemp(this);
        Log.i("RainfallService", "onStartCommand: temp="+temp);
        if (temp==0){
            temp = 60;
        }
        db = new DatabaseHandler(this);
        timer = new CountDownTimer(DateUtils.MINUTE_IN_MILLIS * temp, DateUtils.MINUTE_IN_MILLIS) {

            @Override
            public void onTick(long millisUntilFinished) {
                temp = millisUntilFinished / DateUtils.MINUTE_IN_MILLIS;
                IBSPref.saveTemp(RainfallService.this, temp);
                Log.i("RainfallService", "onTick: " + millisUntilFinished / DateUtils.MINUTE_IN_MILLIS);
            }

            @Override
            public void onFinish() {
                try {
                    IBSPref.saveTemp(RainfallService.this, 0);
                    loadWeather(city);
                } catch (Exception e) {
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent(".RestartService");
        System.out.println("RainfallService destroyed");
        timer.cancel();
        IBSPref.saveLastNotificationTime(this, System.currentTimeMillis());
        sendBroadcast(broadcastIntent);
    }

    private void loadWeather(String city) {
        WeatherMap weatherMap = new WeatherMap(this, APP_ID);

        weatherMap.getCityForecast(city, new ForecastCallback() {
            @Override
            public void success(ForecastResponseModel response) {
                System.out.println("getListRainfallService:" + response.getList().length);
                checkRainfall(response);
            }

            @Override
            public void failure(String message) {

            }
        });
    }

    void checkRainfall(ForecastResponseModel response) {
        List[] lists = response.getList();
        int i = 0;
//
//        for (int i = 0; i < Integer.parseInt(response.getCnt()); i++) {
//        if (lists[i].getRain() != null && lists[i].getRain().get3h() != null) {
//            double r = lists[i].getRain().get3h();
//            if (r >= 0d) {
//                sendNotif(lists[i]);
////                break;
//            }
//        } else {
        sendNotif(lists[i]);
//            break;
//        }
//
    }
//    }

    void sendNotif(List message) {
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
        db = new DatabaseHandler(this);
        String contentText, contentTitle;
        String tingkat = "rendah";
        Date tgl;
        SimpleDateFormat sdf;

        tgl = new Date(System.currentTimeMillis());
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
//        if (tigaJam) {
        if (message.getRain() != null && message.getRain().get3h() != null) {

            if (message.getRain().get3h() < 20d) {
                tingkat = "sedang";
            } else if (message.getRain().get3h() < 50d) {
                tingkat = "tinggi";
            }
            tgl = new Date(Long.valueOf(message.getDt()));

            contentText = "Pada jam " + sdf.format(tgl) + "+3 jam ke depan";
            contentTitle = "Curah hujan " + tingkat + ", " + message.getRain().get3h() + "mm";

            db.addNotif(new Notif(sdf.format(tgl), contentTitle, sdf.format(new Date(System.currentTimeMillis()))));
        } else {
            contentText = "Pada jam " + sdf.format(tgl) + "+3 jam ke depan";
            contentTitle = String.valueOf(message.getWeather()[0].getDescription());
            db.addNotif(new Notif(message.getDt_txt(), contentTitle, sdf.format(tgl)));
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
                ;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        IBSPref.saveLastNotificationTime(this, System.currentTimeMillis());
//        timer.start();
        stopSelf();
//        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
