package thiva.tamilaudiopro.Activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.Receiver.LoadNemosofts;
import thiva.tamilaudiopro.Receiver.NemosoftsListener;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.NavigationUtil;
import thiva.tamilaudiopro.asyncTask.LoadAbout;
import thiva.tamilaudiopro.asyncTask.LoadLogin;
import thiva.tamilaudiopro.Listener.AboutListener;
import thiva.tamilaudiopro.Listener.LoginListener;
import thiva.tamilaudiopro.item.ItemName;
import thiva.tamilaudiopro.item.ItemUser;
import thiva.tamilaudiopro.Utils.DBHelper;
import thiva.tamilaudiopro.SharedPre.SharedPref;
import thiva.tamilaudiopro.preferences.PreferenceUtil;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;
    SharedPref sharedPref;
    Methods methods;
    DBHelper dbHelper;

    IInAppBillingService mService;
    private static final String LOG_TAG = "iabv3";
    private static final String MERCHANT_ID=null;
    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.getIsFirstPurchaseCode()) {
        } else {
            sharedPref.getPurchaseCode();
            sharedPref.getPurchase();
            sharedPref.getAds();
            initBuy();
        }
        Setting.Now_Play = PreferenceUtil.getInstance(this).getNowPlayingScreen().ordinal();
        Setting.Album = sharedPref.getAlbum_grid();
        Setting.get_color_my = sharedPref.getColor_my();

        switch (PreferenceUtil.getInstance(this).getNowPlayingScreen().ordinal()){
            case 0: Setting.Lodeing_Color = false;
                Setting.Blor_image = false;
                Setting.Blor_image_Color = false;
                break;
            case 1: Setting.Lodeing_Color = true;
                Setting.Blor_image = false;
                Setting.Blor_image_Color = false;
                break;
            case 2: Setting.Lodeing_Color = false;
                Setting.Blor_image = false;
                Setting.Blor_image_Color = false;
                break;
            case 3: Setting.Lodeing_Color = false;
                Setting.Blor_image = false;
                Setting.Blor_image_Color = false;
                break;
            case 4: Setting.Lodeing_Color = false;
                Setting.Blor_image = false;
                Setting.Blor_image_Color = false;
                break;
            case 5: Setting.Lodeing_Color = true;
                Setting.Blor_image = true;
                Setting.Blor_image_Color = false;
                break;
            case 6: Setting.Lodeing_Color = true;
                Setting.Blor_image = true;
                Setting.Blor_image_Color = true;
                break;
            case 7: Setting.Lodeing_Color = true;
                Setting.Blor_image = false;
                Setting.Blor_image_Color = true;
                break;
            default: Setting.Lodeing_Color = false;
                Setting.Blor_image = false;
                Setting.Blor_image_Color = false;
        }

        if (sharedPref.getToolbar_Color()) {
            Setting.ToolBar_Color = true;
        } else {
            Setting.ToolBar_Color = false;
        }

        if (sharedPref.getNightMode()) {
            Setting.Dark_Mode = true;
        } else {
            Setting.Dark_Mode = false;
        }

        if (sharedPref.getStatusBar()) {
            Setting.StatusBar = true;
        } else {
            Setting.StatusBar = false;
        }

        if (sharedPref.getSongsColor()) {
            Setting.songs_color = true;
        } else {
            Setting.songs_color = false;
        }

        if (sharedPref.getAlbumColor()) {
            Setting.album_color = true;
        } else {
            Setting.album_color = false;
        }
        if (sharedPref.getBottomNavigationMenu()) {
            Setting.bottomnavigationmenu = true;
        } else {
            Setting.bottomnavigationmenu = false;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        methods = new Methods(this);
        dbHelper = new DBHelper(this);
        methods.setStatusColor2(getWindow());

        TextView tv = (TextView) findViewById(R.id.appname);
        tv.setText(getApplicationContext().getString(R.string.app_name)+" ");
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/genghiskhan.otf");
        tv.setTypeface(face);

        TextView tv2 = (TextView) findViewById(R.id.appname2);
        tv2.setText(getApplicationContext().getString(R.string.play_your)+" ");
        Typeface face2 = Typeface.createFromAsset(getAssets(),
                "fonts/Katrine.ttf");
        tv2.setTypeface(face2);


        if (sharedPref.getIsFirst()) {
            loadAboutData();
        } else {
            try {
                Setting.isFromPush = getIntent().getExtras().getBoolean("ispushnoti", false);
            } catch (Exception e) {
                Setting.isFromPush = false;
            }
            try {
                Setting.isFromNoti = getIntent().getExtras().getBoolean("isnoti", false);
            } catch (Exception e) {
                Setting.isFromNoti = false;
            }
            if (!sharedPref.getIsAutoLogin()) {
                if(Setting.isPlayed){
                    loadSettings();
                }else {
                    openMainActivity();
                }
            } else {
                if (methods.isNetworkAvailable()) {
                    loadLogin();
                } else {
                    openMainActivity();
                }
            }
        }
    }


    private void loadLogin() {
        if (methods.isNetworkAvailable()) {
            LoadLogin loadLogin = new LoadLogin(new LoginListener() {
                @Override
                public void onStart() {
                }
                @Override
                public void onEnd(String success, String loginSuccess, String message, String user_id, String user_name) {

                    if (success.equals("1")) {
                        if (loginSuccess.equals("1")) {
                            Setting.itemUser = new ItemUser(user_id, user_name, sharedPref.getEmail(), "");
                            Setting.isLogged = true;
                            openMainActivity();
                        } else {
                            openMainActivity();
                        }
                    } else {
                        openMainActivity();
                    }
                }
            }, methods.getAPIRequest(ItemName.METHOD_LOGIN, 0, "", "", "", "", "", "", "", "", "", sharedPref.getEmail(), sharedPref.getPassword(), "", "", "", "", null));
            loadLogin.execute();
        } else {
            Toast.makeText(SplashActivity.this, getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
        }
    }

    public void loadAboutData() {
        if (methods.isNetworkAvailable()) {
            Toast.makeText(SplashActivity.this, "load About ", Toast.LENGTH_SHORT).show();
            LoadAbout loadAbout = new LoadAbout(SplashActivity.this, new AboutListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            dbHelper.addtoAbout();
                            sharedPref.setPurchase();
                            sharedPref.setAds();
                            Loadnemosofts();
                        } else {
                            errorDialog(getString(R.string.error_unauth_access), message);
                        }
                    } else {
                        errorDialog(getString(R.string.server_error), getString(R.string.err_server));
                    }
                }
            });
            loadAbout.execute();
        } else {
            errorDialog(getString(R.string.err_internet_not_conn), getString(R.string.error_connect_net_tryagain));
        }
    }

    public void Loadnemosofts() {
        Toast.makeText(SplashActivity.this, "load Settings", Toast.LENGTH_SHORT).show();
        LoadNemosofts loadNemosofts = new LoadNemosofts(SplashActivity.this, new NemosoftsListener() {
            @Override
            public void onStart() {
            }
            @Override
            public void onEnd(String success, String verifyStatus, String message) {
                if (success.equals("1")) {
                    if (!verifyStatus.equals("-1")) {
                        if (BuildConfig.APPLICATION_ID.equals(Setting.itemNemosofts.getPackage_name())) {
                            if (Setting.nemosofts_key.equals(Setting.itemNemosofts.getNemosofts_key())) {
                                sharedPref.setIsFirstPurchaseCode(false);
                                sharedPref.setPurchaseCode(Setting.itemNemosofts);
                                openLoginActivity();
                            }else {
                                errorDialog(getString(R.string.error_nemosofts_key), getString(R.string.create_nemosofts_key));
                            }
                        } else {
                            errorDialog(getString(R.string.error_package_name), getString(R.string.create_nemosofts_key));
                        }
                    } else {
                        errorDialog(getString(R.string.error_nemosofts_key), message);
                    }
                } else {
                    errorDialog(getString(R.string.err_internet_not_conn), getString(R.string.error_connect_net_tryagain));
                }
            }
        });
        loadNemosofts.execute();
    }

    private void errorDialog(String title, String message) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this, R.style.ThemeDialog);
        alertDialog.setTitle(Setting.nemosofts_key);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        if (title.equals(getString(R.string.err_internet_not_conn)) || title.equals(getString(R.string.server_error))) {
            alertDialog.setNegativeButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadAboutData();
                }
            });
        }

        alertDialog.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void openLoginActivity() {
        if (Setting.isLoginOn && sharedPref.getIsFirst()) {
            sharedPref.setIsFirst(false);
            NavigationUtil.LoginActivity(this,"");
        } else {
            NavigationUtil.MainActivity(this);
        }
    }

    private void openMainActivity() {
        if (sharedPref.getIsFirstPurchaseCode()) {
            loadAboutData2();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadSettings();
                }
            },SPLASH_TIME_OUT);
        }
    }

    public void loadAboutData2() {
        if (methods.isNetworkAvailable()) {
            Toast.makeText(SplashActivity.this, "load About", Toast.LENGTH_SHORT).show();
            LoadAbout loadAbout = new LoadAbout(SplashActivity.this, new AboutListener() {
                @Override
                public void onStart() {
                }
                @Override
                public void onEnd(String success, String verifyStatus, String message) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            dbHelper.addtoAbout();
                            sharedPref.setPurchase();
                            loadNemosofts2();
                        } else {
                            errorDialog(getString(R.string.error_unauth_access), message);
                        }
                    } else {
                        errorDialog(getString(R.string.server_error), getString(R.string.err_server));
                    }
                }
            });
            loadAbout.execute();
        } else {
            errorDialog(getString(R.string.err_internet_not_conn), getString(R.string.error_connect_net_tryagain));
        }
    }

    private void loadNemosofts2() {
        Toast.makeText(SplashActivity.this, "load Settings ", Toast.LENGTH_SHORT).show();
        LoadNemosofts loadNemosofts = new LoadNemosofts(SplashActivity.this, new NemosoftsListener() {
            @Override
            public void onStart() {
            }
            @Override
            public void onEnd(String success, String verifyStatus, String message) {
                if (success.equals("1")) {
                    if (!verifyStatus.equals("-1")) {
                        if (BuildConfig.APPLICATION_ID.equals(Setting.itemNemosofts.getPackage_name())) {
                            if (Setting.nemosofts_key.equals(Setting.itemNemosofts.getNemosofts_key())) {
                                sharedPref.setIsFirstPurchaseCode(false);
                                sharedPref.setPurchaseCode(Setting.itemNemosofts);
                                loadSettings();
                            }else {
                                errorDialog(getString(R.string.error_nemosofts_key), getString(R.string.create_nemosofts_key));
                            }
                        } else {
                            errorDialog(getString(R.string.error_package_name), getString(R.string.create_nemosofts_key));
                        }
                    } else {
                        errorDialog(getString(R.string.error_nemosofts_key), message);
                    }
                } else {
                    errorDialog(getString(R.string.err_internet_not_conn), getString(R.string.error_connect_net_tryagain));
                }
            }
        });
        loadNemosofts.execute();
    }

    private void loadSettings() {
        if (Setting.isFromPush && !Setting.pushCID.equals("0")) {
            NavigationUtil.SongByCatActivitySplash(SplashActivity.this,getString(R.string.categories), Setting.pushCID, Setting.pushCName);
        } else if (Setting.isFromPush && !Setting.pushArtID.equals("0")) {
            NavigationUtil.SongByCatActivitySplash(SplashActivity.this,getString(R.string.artist), Setting.pushArtID, Setting.pushArtNAME);
        } else if (Setting.isFromPush && !Setting.pushAlbID.equals("0")) {
            NavigationUtil.SongByCatActivitySplash(SplashActivity.this,getString(R.string.albums), Setting.pushAlbID, Setting.pushAlbNAME);
        } else {
            NavigationUtil.MainActivity(SplashActivity.this);
        }
    }

    private void initBuy() {
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        if(!BillingProcessor.isIabServiceAvailable(this)) {
            //  showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }

        bp = new BillingProcessor(this, Setting.MERCHANT_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                updateTextViews();
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
            }
            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;
                updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
                for(String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                updateTextViews();
            }
        });
        bp.loadOwnedPurchasesFromGoogle();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(mServiceConn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextViews() {
        bp.loadOwnedPurchasesFromGoogle();
        if(isSubscribe(Setting.SUBSCRIPTION_ID)){
           Setting.getPurchases = true;
        }else{
            Setting.getPurchases = false;
        }
    }

    public Bundle getPurchases(){
        if (!bp.isInitialized()) {
            return null;
        }
        try{
            return  mService.getPurchases(Constants.GOOGLE_API_VERSION, getApplicationContext().getPackageName(), Constants.PRODUCT_TYPE_SUBSCRIPTION, null);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean isSubscribe(String SUBSCRIPTION_ID_CHECK){

        if (!bp.isSubscribed(Setting.SUBSCRIPTION_ID))
            return false;

        Bundle b =  getPurchases();
        if (b==null)
            return  false;
        if( b.getInt("RESPONSE_CODE") == 0){
            ArrayList<String> ownedSkus =
                    b.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList<String>  purchaseDataList =
                    b.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            ArrayList<String>  signatureList =
                    b.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
            String continuationToken =
                    b.getString("INAPP_CONTINUATION_TOKEN");

            if(purchaseDataList == null){
                return  false;
            }
            if(purchaseDataList.size()==0){
                return  false;
            }
            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
                String sku_1 = ownedSkus.get(i);
                //Long tsLong = System.currentTimeMillis()/1000;

                try {
                    JSONObject rowOne = new JSONObject(purchaseData);
                    String  productId =  rowOne.getString("productId") ;

                    if (productId.equals(SUBSCRIPTION_ID_CHECK)){

                        Boolean  autoRenewing =  rowOne.getBoolean("autoRenewing");
                        if (autoRenewing){
                            Long tsLong = System.currentTimeMillis()/1000;
                            Long  purchaseTime =  rowOne.getLong("purchaseTime")/1000;
                            return  true;
                        }else{
                            // Toast.makeText(this, "is not autoRenewing ", Toast.LENGTH_SHORT).show();
                            Long tsLong = System.currentTimeMillis()/1000;
                            Long  purchaseTime =  rowOne.getLong("purchaseTime")/1000;
                            if (tsLong > (purchaseTime + (Setting.SUBSCRIPTION_DURATION*86400)) ){
                                //   Toast.makeText(this, "is Expired ", Toast.LENGTH_SHORT).show();
                                return  false;
                            }else{
                                return  true;
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            return false;
        }
        return  false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

}