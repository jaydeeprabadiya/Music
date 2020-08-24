package thiva.tamilaudiopro.Activity;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.StrictMode;

import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;

import com.facebook.soloader.SoLoader;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;


import thiva.tamilaudiopro.Utils.DBHelper;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class MyApplication extends Application {

    public static boolean isProVersion() {
        return true;
    }
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        SoLoader.init(this, false);

        OneSignal.startInit(getApplicationContext())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        try {
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            dbHelper.onCreate(dbHelper.getWritableDatabase());
            dbHelper.getAbout();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MobileAds.initialize(getApplicationContext(), getApplicationContext().getString(R.string.admob_app_id));
        FirebaseAnalytics.getInstance(getApplicationContext());

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
        }
    }

    public static synchronized MyApplication getInstance() {
        MyApplication coreApp;
        synchronized (MyApplication.class) {
            coreApp = instance;
        }
        return coreApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}