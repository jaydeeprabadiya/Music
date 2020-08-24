package thiva.tamilaudiopro.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import thiva.tamilaudiopro.Fragment.FragmentHome;
import thiva.tamilaudiopro.Listener.IOnBackPressed;
import thiva.tamilaudiopro.Listener.InterAdListener;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.NavigationUtil;
import thiva.tamilaudiopro.asyncTask.LoadAbout;
import thiva.tamilaudiopro.Fragment.FragmentAlbums;
import thiva.tamilaudiopro.Fragment.FragmentArtist;
import thiva.tamilaudiopro.Fragment.FragmentCategories;
import thiva.tamilaudiopro.Fragment.FragmentDashBoard;
import thiva.tamilaudiopro.Fragment.FragmentDownloads;
import thiva.tamilaudiopro.Fragment.FragmentFav;
import thiva.tamilaudiopro.Fragment.FragmentMyPlaylist;
import thiva.tamilaudiopro.Fragment.FragmentServerPlaylist;
import thiva.tamilaudiopro.Listener.AboutListener;
import thiva.tamilaudiopro.Navigation.BubbleNavigationLinearView;
import thiva.tamilaudiopro.Navigation.listener.BubbleNavigationChangeListener;


import java.util.EventListener;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, EventListener, BubbleNavigationChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    Methods methods;
    FragmentManager fm;
    String selectedFragment = "";
    MenuItem menu_login, menu_prof, menu_cat, menu_dire, menu_album, menu_my_pla, menu_suggest;
    private Menu menu;
    private static final String MERCHANT_ID=null;
    IInAppBillingService mService;
    private BillingProcessor bp;
    private static final String LOG_TAG = "iabv3";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);
        getLayoutInflater().inflate(R.layout.cnt_tr, contentFrameLayout);

        if (Setting.StatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        initBuy();

        Menu menu = navigationView.getMenu();
        menu_login = menu.findItem(R.id.nav_login);
        menu_prof = menu.findItem(R.id.nav_profile);
        menu_suggest = menu.findItem(R.id.nav_suggest);

        menu_cat = menu.findItem(R.id.nav_cat);
        menu_dire = menu.findItem(R.id.nav_dire);
        menu_album = menu.findItem(R.id.nav_album);
        menu_my_pla = menu.findItem(R.id.nav_my_pla);

        if (Setting.bottomnavigationmenu) {
            menu_cat.setVisible(false);
            menu_dire.setVisible(false);
            menu_album.setVisible(false);
            menu_my_pla.setVisible(false);
        } else {
            menu_cat.setVisible(true);
            menu_dire.setVisible(true);
            menu_album.setVisible(true);
            menu_my_pla.setVisible(true);
        }

        changeLoginName();

        Setting.isAppOpen = true;
        methods = new Methods(this);
        methods.DataSave();
        fm = getSupportFragmentManager();

        navigationView.setNavigationItemSelectedListener(this);
        bubbleNavigationLinearView.setNavigationChangeListener(this);

        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu2);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu1);
        }
        methods.Toolbar_Color(toolbar,getWindow(),getSupportActionBar(),"Home");


        if (methods.isNetworkAvailable()) {
            loadAboutData();
        } else {
            dbHelper.getAbout();
            sharedPref.setPurchase();
            sharedPref.getAds();
        }

        if (checkPer()) {
        }
        loadDashboardFrag();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                FragmentDashBoard f_home = new FragmentDashBoard();
                loadFrag(f_home, getString(R.string.dashboard), fm);
                home();
                break;
            case R.id.nav_cat:
                FragmentCategories cat = new FragmentCategories();
                loadFrag(cat, getString(R.string.categories), fm);
                home();
                break;
            case R.id.nav_dire:
                FragmentArtist f_art = new FragmentArtist();
                loadFrag(f_art, getString(R.string.artist), fm);
                home();
                break;
            case R.id.nav_album:
                FragmentAlbums f_album = new FragmentAlbums();
                loadFrag(f_album, getString(R.string.albums), fm);
                home();
                break;
            case R.id.nav_my_pla:
                FragmentMyPlaylist f_myplay = new FragmentMyPlaylist();
                loadFrag(f_myplay, getString(R.string.myplaylist), fm);
                home();
                break;
            case R.id.nav_playlist:
                FragmentServerPlaylist f_server_playlist = new FragmentServerPlaylist();
                loadFrag(f_server_playlist, getString(R.string.playlist), fm);
                home();
                break;
            case R.id.nav_music_library:
                NavigationUtil.OfflineMusicActivity(this);
                break;
            case R.id.nav_downloads:
                if (checkPer()) {
                    FragmentDownloads f_download = new FragmentDownloads();
                    loadFrag(f_download, getString(R.string.downloads), fm);
                }
                home();
                break;
            case R.id.nav_favourite:
                FragmentFav f_fav = new FragmentFav();
                loadFrag(f_fav, getString(R.string.favourite), fm);
                home();
                break;
            case R.id.nav_settings:
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                finish();
                break;
            case R.id.nav_suggest:
                NavigationUtil.SuggestionActivity(this);
                break;
            case R.id.nav_not:
                NavigationUtil.NotificationActivity(this);
                break;
            case R.id.nav_profile:
                NavigationUtil.ProfileActivity(this);
                break;
            case R.id.nav_login:
                methods.clickLogin();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void home(){
        Setting.Home = false;
        bubbleNavigationLinearView.setBadgeValue(0, "");
        bubbleNavigationLinearView.setBadgeValue(1, null); //invisible badge
        bubbleNavigationLinearView.setBadgeValue(2, null);
        bubbleNavigationLinearView.setBadgeValue(3, null);
        bubbleNavigationLinearView.setBadgeValue(4, null); //empty badge
    }

    @Override
    public void onNavigationChanged(View view, int position) {
        switch (position) {
            case 0:
                FragmentDashBoard f_home = new FragmentDashBoard();
                loadFrag(f_home, getString(R.string.dashboard), fm);

                Setting.Home = false;
                bubbleNavigationLinearView.setCurrentActiveItem(0);
                bubbleNavigationLinearView.setBadgeValue(0, "");
                bubbleNavigationLinearView.setBadgeValue(1, null); //invisible badge
                bubbleNavigationLinearView.setBadgeValue(2, null);
                bubbleNavigationLinearView.setBadgeValue(3, null);
                bubbleNavigationLinearView.setBadgeValue(4, null); //empty badge
                break;
            case 1:
                FragmentCategories cat = new FragmentCategories();
                loadFrag(cat, getString(R.string.categories), fm);

                Setting.Home = true;
                bubbleNavigationLinearView.setCurrentActiveItem(1);
                bubbleNavigationLinearView.setBadgeValue(0, null);
                bubbleNavigationLinearView.setBadgeValue(1, ""); //invisible badge
                bubbleNavigationLinearView.setBadgeValue(2, null);
                bubbleNavigationLinearView.setBadgeValue(3, null);
                bubbleNavigationLinearView.setBadgeValue(4, null); //empty badge
                break;
            case 2:
                FragmentArtist f_art = new FragmentArtist();
                loadFrag(f_art, getString(R.string.artist), fm);

                Setting.Home = true;
                bubbleNavigationLinearView.setCurrentActiveItem(2);
                bubbleNavigationLinearView.setBadgeValue(0, null);
                bubbleNavigationLinearView.setBadgeValue(1, null); //invisible badge
                bubbleNavigationLinearView.setBadgeValue(2, "");
                bubbleNavigationLinearView.setBadgeValue(3, null);
                bubbleNavigationLinearView.setBadgeValue(4, null); //empty badge

                break;
            case 3:
                FragmentAlbums f_album = new FragmentAlbums();
                loadFrag(f_album, getString(R.string.albums), fm);

                Setting.Home = true;
                bubbleNavigationLinearView.setCurrentActiveItem(3);
                bubbleNavigationLinearView.setBadgeValue(0, null);
                bubbleNavigationLinearView.setBadgeValue(1, null); //invisible badge
                bubbleNavigationLinearView.setBadgeValue(2, null);
                bubbleNavigationLinearView.setBadgeValue(3, "");
                bubbleNavigationLinearView.setBadgeValue(4, null); //empty badge
                break;
            case 4:
                FragmentMyPlaylist f_myplay = new FragmentMyPlaylist();
                loadFrag(f_myplay, getString(R.string.myplaylist), fm);

                Setting.Home = true;
                bubbleNavigationLinearView.setCurrentActiveItem(4);
                bubbleNavigationLinearView.setBadgeValue(0, null);
                bubbleNavigationLinearView.setBadgeValue(1, null); //invisible badge
                bubbleNavigationLinearView.setBadgeValue(2, null);
                bubbleNavigationLinearView.setBadgeValue(3, null);
                bubbleNavigationLinearView.setBadgeValue(4, ""); //empty badge
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    private void loadDashboardFrag() {
        FragmentDashBoard f1 = new FragmentDashBoard();
        loadFrag(f1, getResources().getString(R.string.dashboard), fm);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        selectedFragment = name;
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
           // fm.popBackStackImmediate();
            fm.popBackStack();
        }

        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (!name.equals(getString(R.string.dashboard))) {
            ft.hide(fm.getFragments().get(fm.getBackStackEntryCount()));
            ft.add(R.id.fragment, f1, name);
            ft.addToBackStack(name);
        } else {
            ft.replace(R.id.fragment, f1, name);
            //ft.addToBackStack(name);
        }
        ft.commit();

        getSupportActionBar().setTitle(name);

//        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
//            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//        }
    }

    private void exitDialog() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Setting.Dark_Mode){
                alert = new AlertDialog.Builder(MainActivity.this, R.style.ThemeDialog2);
            }else {
                alert = new AlertDialog.Builder(MainActivity.this, R.style.ThemeDialog);
            }
        } else {
            alert = new AlertDialog.Builder(MainActivity.this);
        }
        alert.setTitle(getString(R.string.exit));
        alert.setMessage(getString(R.string.sure_exit));
        alert.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    public void loadAboutData() {
        LoadAbout loadAbout = new LoadAbout(MainActivity.this, new AboutListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onEnd(String success, String verifyStatus, String message) {
                if (!verifyStatus.equals("-1")) {
                    dbHelper.addtoAbout();
                    sharedPref.setPurchase();
                    sharedPref.setAds();
                } else {
                    methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                }
            }
        });
        loadAbout.execute();
    }

    private void changeLoginName() {
        if (menu_login != null) {
            if (Setting.isLoginOn) {
                if (Setting.isLogged) {
                    menu_suggest.setVisible(true);
                    menu_prof.setVisible(true);
                    menu_login.setTitle(getResources().getString(R.string.logout));
                    menu_login.setIcon(getResources().getDrawable(R.drawable.ic_logout));
                } else {
                    menu_suggest.setVisible(false);
                    menu_prof.setVisible(false);
                    menu_login.setTitle(getResources().getString(R.string.login));
                    menu_login.setIcon(getResources().getDrawable(R.drawable.ic_login));
                }
            } else {
                menu_login.setVisible(false);
                menu_prof.setVisible(false);
                menu_suggest.setVisible(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        Setting.isAppOpen = false;
        if (PlayerService.exoPlayer != null && !PlayerService.exoPlayer.getPlayWhenReady()) {
            Intent intent = new Intent(getApplicationContext(), PlayerService.class);
            intent.setAction(PlayerService.ACTION_STOP);
            startService(intent);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (dialog_desc != null && dialog_desc.isShowing()) {
            dialog_desc.dismiss();
        } else if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }else if (Setting.Home){
            FragmentDashBoard f_home = new FragmentDashBoard();
            loadFrag(f_home, getString(R.string.dashboard), fm);
            Setting.Home = false;
            bubbleNavigationLinearView.setBadgeValue(0, "");
            bubbleNavigationLinearView.setCurrentActiveItem(0);
            bubbleNavigationLinearView.setBadgeValue(1, null); //invisible badge
            bubbleNavigationLinearView.setBadgeValue(2, null);
            bubbleNavigationLinearView.setBadgeValue(3, null);
            bubbleNavigationLinearView.setBadgeValue(4, null); //empty badge
        }//fm.getBackStackEntryCount() != 0
        else if (getFragmentManager().getBackStackEntryCount()!=0) {
            String title = fm.getFragments().get(fm.getBackStackEntryCount()).getTag();
            if (title.equals(getString(R.string.dashboard)) || title.equals(getString(R.string.home)) || title.equals(getString(R.string.categories)) || title.equals(getString(R.string.latest))) {
                navigationView.setCheckedItem(R.id.nav_home);
            }
            getSupportActionBar().setTitle(title);
            super.onBackPressed();

        }
        else {
            exitDialog();
        }
    }

    public Boolean checkPer() {
        if ((ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean canUseExternalStorage = false;

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                }

                if (!canUseExternalStorage) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.err_cannot_use_features), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        changeLoginName();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        if (Setting.in_app){
            if (Setting.getPurchases){
                menu.clear();
            }
        }else{
            menu.clear();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_pro :
                showDialog_pay();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                //  showToast("onProductPurchased: " + productId);
                Intent intent= new Intent(MainActivity.this,SplashActivity.class);
                startActivity(intent);
                finish();
                updateTextViews();
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                // showToast("onBillingError: " + Integer.toString(errorCode));
            }
            @Override
            public void onBillingInitialized() {
                //  showToast("onBillingInitialized");
                updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
                // showToast("onPurchaseHistoryRestored");
                for(String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                updateTextViews();
            }
        });
        bp.loadOwnedPurchasesFromGoogle();
    }
    private void updateTextViews() {
        bp.loadOwnedPurchasesFromGoogle();

    }

    public Bundle getPurchases(){
        if (!bp.isInitialized()) {
            //  Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            return null;
        }
        try{
            // Toast.makeText(this, "good", Toast.LENGTH_SHORT).show();
            return  mService.getPurchases(Constants.GOOGLE_API_VERSION, getApplicationContext().getPackageName(), Constants.PRODUCT_TYPE_SUBSCRIPTION, null);
        }catch (Exception e) {
            //  Toast.makeText(this, "ex", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }


    public void showDialog_pay(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.dialog_subscribe, null);

        final BottomSheetDialog dialog_setas = new BottomSheetDialog(this);
        dialog_setas.setContentView(view);
        dialog_setas.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

        final TextView text_view_go_pro= (TextView) dialog_setas.findViewById(R.id.text_view_go_pro);

        text_view_go_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.subscribe(MainActivity.this, Setting.SUBSCRIPTION_ID);
            }
        });

        dialog_setas.show();
    }
}