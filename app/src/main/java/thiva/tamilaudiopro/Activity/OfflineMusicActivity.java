package thiva.tamilaudiopro.Activity;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import thiva.tamilaudiopro.Fragment.FragmentOFAlbums;
import thiva.tamilaudiopro.Fragment.FragmentOFArtist;
import thiva.tamilaudiopro.Fragment.FragmentOFPlaylist;
import thiva.tamilaudiopro.Fragment.FragmentOFSongs;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class OfflineMusicActivity extends BaseActivity {

    Methods methods;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager ViewPagerOffline;
    private TabLayout tabLayoutOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_offline_music, contentFrameLayout);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        bubbleNavigationLinearView.setVisibility(View.GONE);

        toolbar.setVisibility(View.GONE);
        Toolbar toolbar_off = findViewById(R.id.toolbar_offline);
        toolbar_off.setTitle(getString(R.string.music_library));
        setSupportActionBar(toolbar_off);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Setting.StatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        methods = new Methods(this);
        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }
        methods.Toolbar_Color(toolbar_off,getWindow(),getSupportActionBar(),"");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPagerOffline = findViewById(R.id.container);
        ViewPagerOffline.setOffscreenPageLimit(5);

        tabLayoutOffline = findViewById(R.id.tabs);
        if (!Setting.Dark_Mode) {
            if (Setting.ToolBar_Color) {
                tabLayoutOffline.setTabTextColors(ColorStateList.valueOf(ContextCompat.getColor(OfflineMusicActivity.this, R.color.white)));
                tabLayoutOffline.setSelectedTabIndicatorColor(ContextCompat.getColor(OfflineMusicActivity.this, R.color.white));
            }
        }

        if (Setting.ToolBar_Color) {
            switch (Setting.get_color_my){
                case 0:
                    tabLayoutOffline.setBackgroundColor(ContextCompat.getColor(OfflineMusicActivity.this, R.color.Toolbar_1));
                    break;
                case 1:
                    tabLayoutOffline.setBackgroundColor(ContextCompat.getColor(OfflineMusicActivity.this, R.color.Toolbar_2));
                    break;
                case 2:
                    tabLayoutOffline.setBackgroundColor(ContextCompat.getColor(OfflineMusicActivity.this, R.color.Toolbar_3));
                    break;
                case 3:
                    tabLayoutOffline.setBackgroundColor(ContextCompat.getColor(OfflineMusicActivity.this, R.color.Toolbar_4));
                    break;
                case 4:
                    tabLayoutOffline.setBackgroundColor(ContextCompat.getColor(OfflineMusicActivity.this, R.color.Toolbar_5));
                    break;
                case 5:
                    tabLayoutOffline.setBackgroundColor(ContextCompat.getColor(OfflineMusicActivity.this, R.color.Toolbar_6));
                    break;
                case 6:
                    tabLayoutOffline.setBackgroundColor(ContextCompat.getColor(OfflineMusicActivity.this, R.color.Toolbar_7));
                    break;
                case 7:
                    tabLayoutOffline.setBackgroundColor(ContextCompat.getColor(OfflineMusicActivity.this, R.color.Toolbar_8));
                    break;
                default:
                    tabLayoutOffline.setBackgroundColor(ContextCompat.getColor(OfflineMusicActivity.this, R.color.Toolbar_8));
            }
        }

        if (checkPer()) {
            initTabs();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTabs() {
        ViewPagerOffline.setAdapter(mSectionsPagerAdapter);
        ViewPagerOffline.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutOffline));
        tabLayoutOffline.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(ViewPagerOffline));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FragmentOFSongs.newInstance(position);
                case 1:
                    return FragmentOFArtist.newInstance(position);
                case 2:
                    return FragmentOFAlbums.newInstance(position);
                case 3:
                    return FragmentOFPlaylist.newInstance(position);
                default:
                    return FragmentOFAlbums.newInstance(position);
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public void onBackPressed() {
        if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    public Boolean checkPer() {

        if ((ContextCompat.checkSelfPermission(OfflineMusicActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                    initTabs();
                }

                if (!canUseExternalStorage) {
                    Toast.makeText(OfflineMusicActivity.this, getResources().getString(R.string.err_cannot_use_features), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
