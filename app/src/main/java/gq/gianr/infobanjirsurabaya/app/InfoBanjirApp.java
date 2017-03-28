package gq.gianr.infobanjirsurabaya.app;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import gq.gianr.infobanjirsurabaya.util.StringsUtil;

/**
 * Created by j on 17/03/2017.
 */

public class InfoBanjirApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        StringsUtil.initialize(this);
    }
}
