package thiva.tamilaudiopro.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.nemosofts.library.SwitchButton.SwitchButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thiva.tamilaudiopro.Color_Pik.ColorPicker;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.SharedPre.SharedPref;
import thiva.tamilaudiopro.NowPlayingScreen.NowPlayingScreen;
import thiva.tamilaudiopro.preferences.PreferenceUtil;
import thiva.tamilaudiopro.preferences.ViewUtil;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class UiActivity extends AppCompatActivity {

    Methods methods;
    Toolbar toolbar;
    Boolean isNoti = true;
    SharedPref sharedPref;
    SwitchButton switch_noti;
    SwitchButton  Switch_StatusBar,switch_songs_color, album_color, switch_dark,
            bottomnavigationmenu, switch_as;
    LinearLayout switch_album, album_grid, coler_pik;
    Context context2;
    ImageView color_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.getNightMode()) {
            setTheme(R.style.AppTheme3);
            Setting.Dark_Mode = true;
        } else {
            setTheme(R.style.AppTheme);
            Setting.Dark_Mode = false;
        }
        if (sharedPref.getToolbar_Color()) {
            Setting.ToolBar_Color = true;
        } else {
            Setting.ToolBar_Color = false;
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
        setContentView(R.layout.activity_ui);

        if (Setting.StatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        color_image = findViewById(R.id.color);
        color_image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Toolbar_1)));

        methods = new Methods(this);

        isNoti = sharedPref.getIsNotification();

        toolbar = this.findViewById(R.id.toolbar_setting);
        toolbar.setTitle("Ui");
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }
        methods.Toolbar_Color(toolbar,getWindow(),getSupportActionBar(),"");
        methods.getColroImage(color_image);


        if (Setting.ToolBar_Color2){
            Setting.ToolBar_Color2 = false;
            Apps_recreate();
        }
        if (Setting.ToolBar_Color3){
            Setting.ToolBar_Color3 = false;
            Apps_recreate();
        }

        switch_album();
        Switch_StatusBar();
        switch_songs_color();
        album_color();
        bottomnavigationmenu();
        Dark_mode();
        noti();
        album_grid();
        switch_as();
        coler_pik  = findViewById(R.id.coler_pik);
        coler_pik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_coler_pik();
            }
        });
    }


    private void Dialog_coler_pik() {
        final ColorPicker colorPicker = new ColorPicker(UiActivity.this);
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#82B926");
        colors.add("#a276eb");
        colors.add("#6a3ab2");
        colors.add("#666666");
        colors.add("#000000");
        colors.add("#3C8D2F");
        colors.add("#FA9F00");
        colors.add("#FF0000");

                colorPicker
                .setDefaultColorButton(Color.parseColor("#f84c44"))
                .setColors(colors)
                .setColumns(4)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        sharedPref.setColor_my(position);
                        Setting.ToolBar_Color3 = true;
                        Apps_recreate();
                    }

                    @Override
                    public void onCancel() {
                        Apps_recreate();
                    }
                }).show();
    }

    private void album_grid() {
        album_grid = findViewById(R.id.album_grid);
        album_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_album_grid();
            }
        });
    }

    private void Dialog_album_grid() {
        final String[] listItems = {"Normal", "Card", "Style", "Round Image"};
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Setting.Dark_Mode){
                builder = new AlertDialog.Builder(UiActivity.this, R.style.ThemeDialog2);
            }else {
                builder = new AlertDialog.Builder(UiActivity.this, R.style.ThemeDialog);
            }
        } else {
            builder = new AlertDialog.Builder(UiActivity.this);
        }

        builder.setTitle("Album grid");

        int checkedItem = sharedPref.getAlbum_grid();
        builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPref.setAlbum_grid(which);
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Apps_recreate();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void switch_as() {
        switch_as = findViewById(R.id.switch_as);
        if (sharedPref.getToolbar_Color()) {
            switch_as.setChecked(true);
        } else {
            switch_as.setChecked(false);
        }
        switch_as.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                sharedPref.setToolbar_Color(isChecked);
                Setting.ToolBar_Color2 = true;
                Apps_recreate();
            }
        });
    }

    private void Dialog() {
        final Dialog dialog_rate;
        final int[] viewPagerPosition = new int[1];
        dialog_rate = new Dialog(UiActivity.this);
        dialog_rate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_rate.setContentView(R.layout.preference_dialog_now_playing_screen);

        final ViewPager viewPager = dialog_rate.findViewById(R.id.now_playing_screen_view_pager);
        viewPager.setAdapter(new NowPlayingScreenAdapter(this));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPagerPosition[0] = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setPageMargin((int) ViewUtil.convertDpToPixel(32, getResources()));
        viewPager.setCurrentItem(PreferenceUtil.getInstance(this).getNowPlayingScreen().ordinal());

        final Button button_later = dialog_rate.findViewById(R.id.button_set);
        final Button button_ca = dialog_rate.findViewById(R.id.button_cn);

        button_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Setting.in_app){
                    if (Setting.getPurchases){
                        NowPlayingScreen nowPlayingScreen = NowPlayingScreen.values()[viewPagerPosition[0]];
                        if (isNowPlayingThemes(nowPlayingScreen)) {
                            String result = getString(nowPlayingScreen.titleRes) + " theme is Pro version feature.";
                            Toast.makeText(context2, result, Toast.LENGTH_SHORT).show();
                        } else {
                            PreferenceUtil.getInstance(UiActivity.this).setNowPlayingScreen(nowPlayingScreen);
                        }
                        dialog_rate.dismiss();
                        Apps_recreate();
                    }else {
                        methods.Pro_Version_Dialog();
                    }
                }else {
                    NowPlayingScreen nowPlayingScreen = NowPlayingScreen.values()[viewPagerPosition[0]];
                    if (isNowPlayingThemes(nowPlayingScreen)) {
                        String result = getString(nowPlayingScreen.titleRes) + " theme is Pro version feature.";
                        Toast.makeText(context2, result, Toast.LENGTH_SHORT).show();
                    } else {
                        PreferenceUtil.getInstance(UiActivity.this).setNowPlayingScreen(nowPlayingScreen);
                    }
                    dialog_rate.dismiss();
                    Apps_recreate();
                }

            }
        });

        button_ca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_rate.dismiss();
            }
        });

        dialog_rate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_rate.show();
        Window window = dialog_rate.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private boolean isNowPlayingThemes(NowPlayingScreen nowPlayingScreen) {

        if (nowPlayingScreen.equals(NowPlayingScreen.BLUR)) {
            PreferenceUtil.getInstance(context2).resetCarouselEffect();
            PreferenceUtil.getInstance(context2).resetCircularAlbumArt();
        }

        return (nowPlayingScreen.equals(NowPlayingScreen.FULL) ||
                nowPlayingScreen.equals(NowPlayingScreen.CARD) ||
                nowPlayingScreen.equals(NowPlayingScreen.PLAIN) ||
                nowPlayingScreen.equals(NowPlayingScreen.BLUR) ||
                nowPlayingScreen.equals(NowPlayingScreen.COLOR) ||
                nowPlayingScreen.equals(NowPlayingScreen.TINY))
                && !MyApplication.isProVersion();
    }

    public static class NowPlayingScreenAdapter extends PagerAdapter {

        private Context context;

        public NowPlayingScreenAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup collection, int position) {
            NowPlayingScreen nowPlayingScreen = NowPlayingScreen.values()[position];

            LayoutInflater inflater = LayoutInflater.from(context);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.preference_now_playing_screen_item, collection, false);
            collection.addView(layout);

            ImageView image = layout.findViewById(R.id.image);
            TextView title = layout.findViewById(R.id.title);
            Picasso.get()
                    .load(nowPlayingScreen.drawableResId)
                    .into(image);
            title.setText(nowPlayingScreen.titleRes);

            return layout;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return NowPlayingScreen.values().length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return context.getString(NowPlayingScreen.values()[position].titleRes);
        }
    }

    private void noti() {
        switch_noti = findViewById(R.id.switch_noti);
        if (isNoti) {
            switch_noti.setChecked(true);
        } else {
            switch_noti.setChecked(false);
        }
        switch_noti.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                sharedPref.setIsNotification(isChecked);
            }
        });
    }

    private void Dark_mode() {
        switch_dark = findViewById(R.id.switch_dark);
        if (sharedPref.getNightMode()) {
            switch_dark.setChecked(true);
        } else {
            switch_dark.setChecked(false);
        }
        switch_dark.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                sharedPref.setNightMode(isChecked);
                Apps_recreate();
            }
        });
    }

    private void album_color() {
        album_color = findViewById(R.id.switch_album_color);
        if (sharedPref.getAlbumColor()) {
            album_color.setChecked(true);
        } else {
            album_color.setChecked(false);
        }
        album_color.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                sharedPref.setAlbumColor(isChecked);
                Apps_recreate();
            }
        });

    }

    private void switch_songs_color() {
        switch_songs_color = findViewById(R.id.switch_songs_color);
        if (sharedPref.getSongsColor()) {
            switch_songs_color.setChecked(true);
        } else {
            switch_songs_color.setChecked(false);
        }
        switch_songs_color.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                sharedPref.setSongsColor(isChecked);
                Apps_recreate();
            }
        });
    }

    private void Switch_StatusBar() {
        Switch_StatusBar = findViewById(R.id.switch_apps_full);
        if (sharedPref.getStatusBar()) {
            Switch_StatusBar.setChecked(true);
        } else {
            Switch_StatusBar.setChecked(false);
        }
        Switch_StatusBar.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                sharedPref.setStatusBar(isChecked);
                Apps_recreate();
            }
        });
    }

    private void switch_album() {
        switch_album = findViewById(R.id.switch_album);
        switch_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
        });
    }

    private void bottomnavigationmenu() {
        bottomnavigationmenu = findViewById(R.id.bottomnavigationmenu);
        if (sharedPref.getBottomNavigationMenu()) {
            bottomnavigationmenu.setChecked(true);
        } else {
            bottomnavigationmenu.setChecked(false);
        }
        bottomnavigationmenu.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                sharedPref.setBottomNavigationMenu(isChecked);
                Apps_recreate();
            }
        });
    }

    private void Apps_recreate() {
        recreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(new Intent(UiActivity.this, SettingActivity.class));
                finish();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);
        startActivity(new Intent(UiActivity.this, SettingActivity.class));
        finish();
    }
}