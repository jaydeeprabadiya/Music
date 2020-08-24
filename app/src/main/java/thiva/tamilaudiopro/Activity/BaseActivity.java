package thiva.tamilaudiopro.Activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import com.google.android.material.progressindicator.ProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import thiva.tamilaudiopro.Fragment.FragmentDashBoard;
import thiva.tamilaudiopro.JSONParser.JSONParser;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.Navigation.BubbleNavigationLinearView;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.SharedPre.SharedPref;
import thiva.tamilaudiopro.Utils.NavigationUtil;
import thiva.tamilaudiopro.asyncTask.GetRating;
import thiva.tamilaudiopro.asyncTask.LoadRating;
import thiva.tamilaudiopro.asyncTask.LoadSong;
import thiva.tamilaudiopro.Listener.RatingListener;
import thiva.tamilaudiopro.Listener.SongListener;
import thiva.tamilaudiopro.item.ItemMyPlayList;
import thiva.tamilaudiopro.item.ItemName;
import thiva.tamilaudiopro.item.ItemSong;
import thiva.tamilaudiopro.item.MessageEvent;
import thiva.tamilaudiopro.Utils.DBHelper;
import thiva.tamilaudiopro.Utils.GlobalBus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import thiva.tamilaudiopro.views.BlurImage;
import thiva.tamilaudiopro.views.Roundedimageview.RoundedImageView;


import static thiva.tamilaudiopro.Activity.PlayerService.exoPlayer;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    Methods methods;
    DBHelper dbHelper;
    DrawerLayout drawer;
    public ViewPager viewpager;
    ImagePagerAdapter imagePagerAdapter;
    public static SlidingUpPanelLayout mLayout;
    NavigationView navigationView;
    BubbleNavigationLinearView bubbleNavigationLinearView;
    Toolbar toolbar;
    Boolean isExpand = false;
    BottomSheetDialog dialog_desc;
    Dialog dialog_rate;
    private Handler seekHandler = new Handler();
    String deviceId;
    RelativeLayout include_sliding_panel_childtwo, rl_music_loading, rl_min_header;
    RatingBar ratingBar;
    ProgressIndicator seekbar_min;
    ProgressBar seekbar_min2;
    MaterialTextView tv_min_title;
    View view_playlist, view_download, view_round, view1_my, view1_my2, view1_my3;
    TextView tv_music_title, tv_music_artist, tv_song_count,
            tv_current_time, tv_total_time;
    ImageView iv_music_bg, iv_min_play, iv_max_fav, iv_music_shuffle, iv_music_repeat, iv_max_option,
            iv_music_previous, iv_music_next, iv_music_play, iv_music_add2playlist, iv_music_share,
            iv_music_download, iv_music_rate, imageView_heart, imageView_pager, equalizer, actionPrevious, actionNext;
    private SeekBar volumeSeekBar, seekBar_music;
    AudioManager audioManager;
    private FragmentManager fm;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode) {
            setTheme(R.style.AppTheme3);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        switch (Setting.Now_Play){
            case 0: setContentView(R.layout.activity_base);
                break;
            case 1:setContentView(R.layout.activity_base2);
                break;
            case 2: setContentView(R.layout.activity_base3);
                break;
            case 3: setContentView(R.layout.activity_base4);
                break;
            case 4: setContentView(R.layout.activity_base5);
                break;
            case 5: setContentView(R.layout.activity_base6);
                break;
            case 6: setContentView(R.layout.activity_base7);
                break;
            case 7: setContentView(R.layout.activity_base8);
                break;
            default: setContentView(R.layout.activity_base);
        }

        fm = getFragmentManager();

        Setting.context = this;
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        methods = new Methods(this);
        dbHelper = new DBHelper(this);
        sharedPref = new SharedPref(this);

        mLayout = findViewById(R.id.sliding_layout);
        toolbar = findViewById(R.id.toolbar_offline_music);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (Setting.Blor_image_Color){
            include_sliding_panel_childtwo = findViewById(R.id.include_sliding_panel_childtwo);
            view1_my = findViewById(R.id.view1_my);
            view1_my2 = findViewById(R.id.view1_my2);
            view1_my3 = findViewById(R.id.view1_my3);
        }

        imagePagerAdapter = new ImagePagerAdapter();

        RelativeLayout rl = findViewById(R.id.rl);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/circular_std_book.otf"));
        bubbleNavigationLinearView.setVisibility(View.VISIBLE);

        if (Setting.bottomnavigationmenu) {
            bubbleNavigationLinearView.setVisibility(View.VISIBLE);

            bubbleNavigationLinearView.setCurrentActiveItem(0);
            bubbleNavigationLinearView.setBadgeValue(0, "");
            bubbleNavigationLinearView.setBadgeValue(1, null);
            bubbleNavigationLinearView.setBadgeValue(2, null);
            bubbleNavigationLinearView.setBadgeValue(3, null);
            bubbleNavigationLinearView.setBadgeValue(4, null);
        } else {
            bubbleNavigationLinearView.setVisibility(View.GONE);
        }

        navigationView = findViewById(R.id.nav_view);
        viewpager = findViewById(R.id.viewPager_song);
        viewpager.setOffscreenPageLimit(5);
        rl_min_header = findViewById(R.id.rl_min_header);
        rl_music_loading = findViewById(R.id.rl_music_loading);
        ratingBar = findViewById(R.id.rb_music);
        seekBar_music = findViewById(R.id.audio_progress_control);
        seekbar_min = findViewById(R.id.seekbar_min);
        seekbar_min2= findViewById(R.id.seekbar_min2);

        iv_music_bg = findViewById(R.id.iv_music_bg);
        iv_music_play = findViewById(R.id.iv_music_play);
        iv_music_next = findViewById(R.id.iv_music_next);
        iv_music_previous = findViewById(R.id.iv_music_previous);
        iv_music_shuffle = findViewById(R.id.iv_music_shuffle);
        iv_music_repeat = findViewById(R.id.iv_music_repeat);
        iv_music_add2playlist = findViewById(R.id.iv_music_add2playlist);
        iv_music_share = findViewById(R.id.iv_music_share);
        iv_music_download = findViewById(R.id.iv_music_download);
        iv_music_rate = findViewById(R.id.iv_music_rate);
        view_download = findViewById(R.id.view_music_download);
        view_playlist = findViewById(R.id.view_music_playlist);
        view_round = findViewById(R.id.vBgLike);
        iv_min_play = findViewById(R.id.iv_min_play);
        iv_max_fav = findViewById(R.id.bottombar_img_Favorite);
        iv_max_option = findViewById(R.id.iv_max_option);
        imageView_heart = findViewById(R.id.ivLike);
        tv_current_time = findViewById(R.id.tv_music_time);
        tv_total_time = findViewById(R.id.tv_music_total_time);
        tv_song_count = findViewById(R.id.tv_music_song_count);
        tv_music_title = findViewById(R.id.tv_music_title);
        tv_music_artist = findViewById(R.id.tv_music_artist);
        tv_min_title = findViewById(R.id.tv_min_title);
        equalizer =  findViewById(R.id.equalizer);
        volumeSeekBar = findViewById(R.id.seekBar);
        actionPrevious  = findViewById(R.id.actionPrevious);
        actionNext = findViewById(R.id.actionNext);

        actionPrevious.setOnClickListener(this);
        actionNext.setOnClickListener(this);
        iv_max_fav.setOnClickListener(this);
        iv_max_option.setOnClickListener(this);
        iv_min_play.setOnClickListener(this);
        iv_music_play.setOnClickListener(this);
        iv_music_next.setOnClickListener(this);
        iv_music_previous.setOnClickListener(this);
        iv_music_shuffle.setOnClickListener(this);
        iv_music_repeat.setOnClickListener(this);
        iv_music_add2playlist.setOnClickListener(this);
        iv_music_share.setOnClickListener(this);
        iv_music_download.setOnClickListener(this);
        iv_music_rate.setOnClickListener(this);
        equalizer.setOnClickListener(this);

        if (Setting.isRepeat) {
            iv_music_repeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_one_white_24dp));
        } else {
            iv_music_repeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_white_24dp2));
        }

        if (!methods.isYoutubeAppInstalled()) {
            iv_music_rate.setVisibility(View.GONE);
        }else{
            iv_music_rate.setVisibility(View.VISIBLE);
        }

        if (Setting.isSuffle) {
            iv_music_shuffle.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.grey));
        } else {
            if (Setting.Dark_Mode) {
                iv_music_shuffle.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.md_white_1000));
            } else {
                iv_music_shuffle.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.black));
            }
        }

        ImageView iv_white_blur = findViewById(R.id.iv_music_white_blur);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (50 * methods.getScreenHeight() / 100));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        iv_white_blur.setLayoutParams(params);

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset == 0.0f) {
                    isExpand = false;
                    rl_min_header.setVisibility(View.VISIBLE);
                } else if (slideOffset > 0.0f && slideOffset < 1.0f) {
                    rl_min_header.setVisibility(View.GONE);
                    rl_min_header.setAlpha(1.0f - slideOffset);
                } else {
                    isExpand = true;
                    rl_min_header.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    try {
                        viewpager.setCurrentItem(Setting.playPos);
                    } catch (Exception e) {
                        imagePagerAdapter.notifyDataSetChanged();
                        viewpager.setCurrentItem(Setting.playPos);
                    }
                }
            }
        });

        seekBar_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                try {
                    Intent intent = new Intent(BaseActivity.this, PlayerService.class);
                    intent.setAction(PlayerService.ACTION_SEEKTO);
                    intent.putExtra("seekto", methods.getSeekFromPercentage(progress, methods.calculateTime(Setting.arrayList_play.get(Setting.playPos).getDuration())));
                    startService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeTextPager(Setting.arrayList_play.get(position));
                View view = viewpager.findViewWithTag("myview" + position);
                if (view != null) {
                    ImageView iv = view.findViewById(R.id.iv_vp_play);
                    if (Setting.playPos == position) {
                        iv.setVisibility(View.GONE);
                    } else {
                        iv.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tv_current_time.setText("00:00");

        if (Setting.pushSID.equals("0")) {
            if (Setting.arrayList_play.size() == 0) {
                Setting.arrayList_play.addAll(dbHelper.loadDataRecent(true, Setting.recentLimit));
                if (Setting.arrayList_play.size() > 0) {
                    GlobalBus.getBus().postSticky(Setting.arrayList_play.get(Setting.playPos));
                }
            }
        } else {
            new LoadSong(new SongListener() {
                @Override
                public void onStart() {
                    Setting.pushSID = "0";
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemSong> arrayList) {
                    if (success.equals("1") && !verifyStatus.equals("-1") && arrayList.size() > 0) {
                        Setting.isOnline = true;
                        Setting.arrayList_play.clear();
                        Setting.arrayList_play.addAll(arrayList);
                        Setting.playPos = 0;

                        Intent intent = new Intent(BaseActivity.this, PlayerService.class);
                        intent.setAction(PlayerService.ACTION_PLAY);
                        startService(intent);
                    } else if (verifyStatus.equals("-1")) {
                        methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                    }
                }
            }, methods.getAPIRequest(ItemName.METHOD_SINGLE_SONG, 0, deviceId, Setting.pushSID, "", "", "", "", "", "", "", "", "", "", "", "", "", null)).execute();
        }

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                try {
                    audioManager.setStreamVolume(exoPlayer.getAudioStreamType(), i, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();//search album selec
                break;
            case R.id.menu_search_home:
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_min_play:
                playPause();
                break;
            case R.id.iv_music_play:
                playPause();
                break;
            case R.id.actionNext:
                next();
                break;
            case R.id.iv_music_next:
                next();
                break;
            case R.id.iv_music_previous:
                previous();
                break;
            case R.id.actionPrevious:
                next();
                break;
            case R.id.iv_music_shuffle:
                setShuffle();
                break;
            case R.id.iv_music_repeat:
                setRepeat();
                break;
            case R.id.iv_max_option:
                openOptionPopUp();
                break;
            case R.id.bottombar_img_Favorite:
                if (Setting.arrayList_play.size() > 0) {
                    if (Setting.isOnline) {
                        methods.animateHeartButton(view);
                        view.setSelected(!view.isSelected());
                        findViewById(R.id.ivLike).setSelected(view.isSelected());
                        fav();
                    }
                } else {
                    Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_music_share:
                shareSong();
                break;
            case R.id.iv_music_add2playlist:
                if (Setting.arrayList_play.size() > 0) {
                    methods.openPlaylists(Setting.arrayList_play.get(viewpager.getCurrentItem()), Setting.isOnline);
                } else {
                    Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_music_download:
                if (JSONParser.isNetworkAvailable(BaseActivity.this)) {
                    if (Setting.arrayList_play.size() > 0) {
                        methods.download(Setting.arrayList_play.get(viewpager.getCurrentItem()));
                    } else {
                        Toast.makeText(BaseActivity.this, getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(BaseActivity.this, getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_music_rate:
                String urlString = "https://www.youtube.com/channel/UCRSvNS1DL5IzluMaRdZLFug/search?query="  + Setting.arrayList_play.get(viewpager.getCurrentItem()).getTitle();
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    startActivity(intent);
                }
                break;
            case R.id.equalizer:
                if (Setting.isPlayed){
                    NavigationUtil.Equalizer(BaseActivity.this);
                }else {
                    Toast.makeText(BaseActivity.this, "Play", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void openOptionPopUp() {
        Context wrapper = new ContextThemeWrapper(BaseActivity.this, R.style.YOURSTYLE);
        PopupMenu popup = new PopupMenu(wrapper, iv_max_option);
        popup.getMenuInflater().inflate(R.menu.popup_base_option, popup.getMenu());

        if (Setting.isLoginOn) {
            popup.getMenu().findItem(R.id.popup_base_report).setVisible(true);
        } else {
            popup.getMenu().findItem(R.id.popup_base_report).setVisible(false);
        }
        if (!methods.isYoutubeAppInstalled()) {
            popup.getMenu().findItem(R.id.popup_youtube).setVisible(false);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_base_report:
                        NavigationUtil.ReportActivity(BaseActivity.this);
                        break;
                    case R.id.popup_base_desc:
                        if (Setting.arrayList_play.size() > 0) {
                            showBottomSheetDialog();
                        }
                        break;
                    case R.id.iv_music_rate:
                        if (Setting.arrayList_play.size() > 0) {
                            openRateDialog();
                        }
                        break;

                }
                return true;
            }
        });
        popup.show();
    }

    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.layout_desc, null);

        dialog_desc = new BottomSheetDialog(this);
        dialog_desc.setContentView(view);
        dialog_desc.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog_desc.show();

        AppCompatButton button = dialog_desc.findViewById(R.id.button_detail_close);
        TextView textView = dialog_desc.findViewById(R.id.tv_desc_title);
        textView.setText(Setting.arrayList_play.get(Setting.playPos).getTitle());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_desc.dismiss();
            }
        });

        WebView webview_song_desc = dialog_desc.findViewById(R.id.webView_bottom);
        String mimeType = "text/html;charset=UTF-8";
        String encoding = "utf-8";
        String text = "<html><head>"
                + "<style> body{color: #000 !important;text-align:left}"
                + "</style></head>"
                + "<body>"
                + Setting.arrayList_play.get(Setting.playPos).getDescription()
                + "</body></html>";

        webview_song_desc.loadDataWithBaseURL("blarg://ignored", text, mimeType, encoding, "");
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    public void seekUpdation() {
        try {
            seekbar_min2.setProgress(methods.getProgressPercentage(exoPlayer.getCurrentPosition(), methods.calculateTime(Setting.arrayList_play.get(Setting.playPos).getDuration())));
            seekbar_min.setProgress(methods.getProgressPercentage(exoPlayer.getCurrentPosition(), methods.calculateTime(Setting.arrayList_play.get(Setting.playPos).getDuration())));
            seekBar_music.setProgress(methods.getProgressPercentage(exoPlayer.getCurrentPosition(), methods.calculateTime(Setting.arrayList_play.get(Setting.playPos).getDuration())));
            tv_current_time.setText(methods.milliSecondsToTimer(exoPlayer.getCurrentPosition()));
            seekBar_music.setSecondaryProgress(exoPlayer.getBufferedPercentage());
            if (exoPlayer.getPlayWhenReady() && Setting.isAppOpen) {
                seekHandler.removeCallbacks(run);
                seekHandler.postDelayed(run, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playPause() {
        if (Setting.arrayList_play.size() > 0) {
            Intent intent = new Intent(BaseActivity.this, PlayerService.class);
            if (Setting.isPlayed) {
                intent.setAction(PlayerService.ACTION_TOGGLE);
                startService(intent);
            } else {
                if (!Setting.isOnline || methods.isNetworkAvailable()) {
                    intent.setAction(PlayerService.ACTION_PLAY);
                    startService(intent);
                } else {
                    Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
        }
    }

    public void next() {
        if (Setting.arrayList_play.size() > 0) {
            if (!Setting.isOnline || methods.isNetworkAvailable()) {
                Intent intent = new Intent(BaseActivity.this, PlayerService.class);
                intent.setAction(PlayerService.ACTION_NEXT);
                startService(intent);
            } else {
                Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
        }
    }

    public void previous() {
        if (Setting.arrayList_play.size() > 0) {
            if (!Setting.isOnline || methods.isNetworkAvailable()) {
                Intent intent = new Intent(BaseActivity.this, PlayerService.class);
                intent.setAction(PlayerService.ACTION_PREVIOUS);
                startService(intent);
            } else {
                Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
        }
    }

    public void setRepeat() {
        if (Setting.isRepeat) {
            Setting.isRepeat = false;
            iv_music_repeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_white_24dp2));
        } else {
            Setting.isRepeat = true;
            iv_music_repeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_one_white_24dp));
        }
    }

    public void setShuffle() {
        if (Setting.isSuffle) {
            Setting.isSuffle = false;
            iv_music_shuffle.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.grey));
        } else {
            if (Setting.Dark_Mode) {
                Setting.isSuffle = true;
                iv_music_shuffle.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.md_white_1000));
            } else {
                Setting.isSuffle = true;
                iv_music_shuffle.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.black));
            }
        }
    }

    private void shareSong() {
        if (Setting.arrayList_play.size() > 0) {
            if (Setting.isOnline || Setting.isDownloaded) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_song));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.listening) + " - " + Setting.arrayList_play.get(viewpager.getCurrentItem()).getTitle() + "\n\nvia " + getResources().getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_song)));
            } else {
                if (checkPer()) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("audio/mp3");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Setting.arrayList_play.get(viewpager.getCurrentItem()).getUrl()));
                    share.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.listening) + " - " + Setting.arrayList_play.get(viewpager.getCurrentItem()).getTitle() + "\n\nvia " + getResources().getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(share, getResources().getString(R.string.share_song)));
                }
            }
        } else {
            Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_no_songs_selected), Toast.LENGTH_SHORT).show();
        }
    }

    public void fav() {
        try {
            if (dbHelper.checkFav(Setting.arrayList_play.get(Setting.playPos).getId())) {
                dbHelper.removeFromFav(Setting.arrayList_play.get(Setting.playPos).getId());
                Toast.makeText(BaseActivity.this, getResources().getString(R.string.removed_fav), Toast.LENGTH_SHORT).show();
                changeFav(false);
            } else {
                dbHelper.addToFav(Setting.arrayList_play.get(Setting.playPos));
                Toast.makeText(BaseActivity.this, getResources().getString(R.string.added_fav), Toast.LENGTH_SHORT).show();
                changeFav(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeFav(Boolean isFav) {
        if (isFav) {
            iv_max_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
        } else {
            iv_max_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
        }
    }

    private void openRateDialog() {
        dialog_rate = new Dialog(BaseActivity.this);
        dialog_rate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_rate.setContentView(R.layout.layout_review);

        final ImageView iv_close = dialog_rate.findViewById(R.id.iv_rate_close);
        final TextView textView = dialog_rate.findViewById(R.id.tv_rate);
        final RatingBar ratingBar = dialog_rate.findViewById(R.id.rb_add);
        final Button button = dialog_rate.findViewById(R.id.button_submit_rating);
        final Button button_later = dialog_rate.findViewById(R.id.button_later_rating);
        final EditText editText = dialog_rate.findViewById(R.id.et_report);

        ratingBar.setStepSize(Float.parseFloat("1"));

        if (Setting.arrayList_play.get(viewpager.getCurrentItem()).getUserRating().equals("") || Setting.arrayList_play.get(viewpager.getCurrentItem()).getUserRating().equals("0")) {
            new GetRating(new RatingListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onEnd(String success, String isRateSuccess, String message, int rating) {
                    if (rating > 0) {
                        ratingBar.setRating(rating);
                        textView.setText(getString(R.string.thanks_for_rating));
                    } else {
                        ratingBar.setRating(1);
                    }
                    Setting.arrayList_play.get(viewpager.getCurrentItem()).setUserRating(String.valueOf(rating));
                }
            }, methods.getAPIRequest(ItemName.METHOD_SINGLE_SONG, 0, deviceId, Setting.arrayList_play.get(viewpager.getCurrentItem()).getId(), "", "", "", "", "", "", "", "", "", "", "", "", "", null)).execute();
        } else {
            if (Integer.parseInt(Setting.arrayList_play.get(viewpager.getCurrentItem()).getUserRating()) != 0 && !Setting.arrayList_play.get(viewpager.getCurrentItem()).getUserRating().equals("")) {
                textView.setText(getString(R.string.thanks_for_rating));
                ratingBar.setRating(Integer.parseInt(Setting.arrayList_play.get(viewpager.getCurrentItem()).getUserRating()));
            } else {
                ratingBar.setRating(1);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() != 0) {
                    if (methods.isNetworkAvailable()) {
                        loadRatingApi(String.valueOf((int) ratingBar.getRating()));
                    } else {
                        Toast.makeText(BaseActivity.this, getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BaseActivity.this, getString(R.string.select_rating), Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_rate.dismiss();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_rate.dismiss();
            }
        });

        dialog_rate.setCancelable(false);
        dialog_rate.setCanceledOnTouchOutside(false);
        dialog_rate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_rate.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_rate.show();
        Window window = dialog_rate.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void loadRatingApi(final String rate) {
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));

        LoadRating loadRating = new LoadRating(new RatingListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String isRateSuccess, String message, int rating) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (success.equals("1")) {
                    if (isRateSuccess.equals("1")) {
                        Setting.arrayList_play.get(viewpager.getCurrentItem()).setAverageRating(String.valueOf(rating));
                        Setting.arrayList_play.get(viewpager.getCurrentItem()).setTotalRate(String.valueOf(Integer.parseInt(Setting.arrayList_play.get(viewpager.getCurrentItem()).getTotalRate() + 1)));
                        Setting.arrayList_play.get(viewpager.getCurrentItem()).setUserRating(String.valueOf(rate));
                        ratingBar.setRating(rating);
                    }
                    Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BaseActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
                dialog_rate.dismiss();
            }
        }, methods.getAPIRequest(ItemName.METHOD_RATINGS, 0, deviceId, Setting.arrayList_play.get(viewpager.getCurrentItem()).getId(), "", "", "", "", "", "", rate, "", "", "", "", "", "", null));

        loadRating.execute();
    }


    public void changeTextPager(ItemSong itemSong) {
        ratingBar.setRating(Integer.parseInt(itemSong.getAverageRating()));
        tv_music_artist.setText(itemSong.getArtist());
        tv_music_title.setText(itemSong.getTitle());
        tv_song_count.setText((viewpager.getCurrentItem() + 1) + "/" + Setting.arrayList_play.size());

        if (Setting.isOnline) {
            if (Setting.Lodeing_Color){
                Picasso.get()
                        .load(itemSong.getImageSmall())
                        .centerCrop()
                        .resize(100, 100)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                if (textSwatch == null) {
                                                    return;
                                                }
                                                tv_music_title.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                tv_music_artist.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));

                                                if (Setting.Blor_image_Color){
                                                    include_sliding_panel_childtwo.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my2.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my3.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
            }
        } else {
            Uri uri;
            if (Setting.isDownloaded) {
                uri = Uri.fromFile(new File(itemSong.getImageSmall()));
            } else {
                uri = methods.getAlbumArtUri(Integer.parseInt(itemSong.getImageSmall()));
            }
            if (Setting.Lodeing_Color) {
                Picasso.get()
                        .load(uri)
                        .centerCrop()
                        .resize(100, 100)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                if (textSwatch == null) {
                                                    return;
                                                }
                                                tv_music_title.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                tv_music_artist.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                if (Setting.Blor_image_Color) {
                                                    include_sliding_panel_childtwo.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my2.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my3.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                }
                                            }
                                        });
                            }
                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
            }

        }
    }

    public void changeText(final ItemSong itemSong, final String page) {
        tv_min_title.setText(itemSong.getTitle());
        ratingBar.setRating(Integer.parseInt(itemSong.getAverageRating()));
        tv_music_title.setText(itemSong.getTitle());
        tv_music_artist.setText(itemSong.getArtist());
        tv_song_count.setText(Setting.playPos + 1 + "/" + Setting.arrayList_play.size());
        tv_total_time.setText(itemSong.getDuration());

        changeFav(dbHelper.checkFav(itemSong.getId()));

        if (Setting.isOnline) {
            if (Setting.Lodeing_Color ){
                Picasso.get()
                        .load(itemSong.getImageSmall())
                        .centerCrop()
                        .resize(100, 100)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                if (textSwatch == null) {
                                                    return;
                                                }
                                                tv_music_title.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                tv_music_artist.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                if (Setting.Blor_image_Color){
                                                    include_sliding_panel_childtwo.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my2.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my3.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                }
                                            }
                                        });
                            }
                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
            }

            if (ratingBar.getVisibility() == View.GONE) {
                ratingBar.setVisibility(View.VISIBLE);
                iv_music_rate.setVisibility(View.VISIBLE);
                iv_music_add2playlist.setVisibility(View.VISIBLE);
            }

            if (Setting.isDownload) {
                iv_music_download.setVisibility(View.VISIBLE);
                view_download.setVisibility(View.VISIBLE);
            } else {
                iv_music_download.setVisibility(View.GONE);
                view_download.setVisibility(View.GONE);
            }
        } else {
            Uri uri;
            if (Setting.isDownloaded) {
                iv_music_add2playlist.setVisibility(View.GONE);
                uri = Uri.fromFile(new File(itemSong.getImageSmall()));
            } else {
                iv_music_add2playlist.setVisibility(View.VISIBLE);
                uri = methods.getAlbumArtUri(Integer.parseInt(itemSong.getImageSmall()));
            }
            if (ratingBar.getVisibility() == View.VISIBLE) {
                ratingBar.setVisibility(View.GONE);
                iv_music_rate.setVisibility(View.GONE);
                iv_music_download.setVisibility(View.GONE);
                view_download.setVisibility(View.GONE);
            }
            if (Setting.Lodeing_Color ){
                Picasso.get()
                        .load(uri)
                        .centerCrop()
                        .resize(100, 100)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                if (textSwatch == null) {
                                                    return;
                                                }
                                                tv_music_title.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                tv_music_artist.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                if (Setting.Blor_image_Color){
                                                    include_sliding_panel_childtwo.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my2.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                    view1_my3.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                }
                                            }
                                        });
                            }
                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }
        }

        if (viewpager.getAdapter() == null || Setting.isNewAdded || !Setting.addedFrom.equals(imagePagerAdapter.getIsLoadedFrom())) {
            viewpager.setAdapter(imagePagerAdapter);
            Setting.isNewAdded = false;
        }
        try {
            viewpager.setCurrentItem(Setting.playPos);
        } catch (Exception e) {
            imagePagerAdapter.notifyDataSetChanged();
            viewpager.setCurrentItem(Setting.playPos);
        }
    }



    public void changePlayPauseIcon(Boolean isPlay) {
        if (!isPlay) {
            iv_music_play.setImageDrawable(getResources().getDrawable(R.drawable.selector_play));
            iv_min_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp3));
        } else {
            iv_music_play.setImageDrawable(getResources().getDrawable(R.drawable.selector_pause));
            iv_min_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp3));
        }
        seekUpdation();
    }

    public void isBuffering(Boolean isBuffer) {
        if (isBuffer) {
            rl_music_loading.setVisibility(View.VISIBLE);
            iv_music_play.setVisibility(View.INVISIBLE);
        } else {
            rl_music_loading.setVisibility(View.INVISIBLE);
            iv_music_play.setVisibility(View.VISIBLE);
            changePlayPauseIcon(!isBuffer);
        }
        iv_music_next.setEnabled(!isBuffer);
        iv_music_previous.setEnabled(!isBuffer);
        iv_music_download.setEnabled(!isBuffer);
        iv_min_play.setEnabled(!isBuffer);
        seekBar_music.setEnabled(!isBuffer);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private String loadedPage = "";

        private ImagePagerAdapter() {
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return Setting.arrayList_play.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        String getIsLoadedFrom() {
            return loadedPage;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View imageLayout;
            switch (Setting.Now_Play){
                case 0: imageLayout = inflater.inflate(R.layout.layout_viewpager, container, false);
                    break;
                case 1:imageLayout = inflater.inflate(R.layout.layout_viewpager, container, false);
                    break;
                case 2: imageLayout = inflater.inflate(R.layout.layout_viewpager3, container, false);
                    break;
                case 3: imageLayout = inflater.inflate(R.layout.layout_viewpager2, container, false);
                    break;
                case 4: imageLayout = inflater.inflate(R.layout.layout_viewpager, container, false);
                    break;
                case 5: imageLayout = inflater.inflate(R.layout.layout_viewpager4, container, false);
                    break;
                case 6: imageLayout = inflater.inflate(R.layout.layout_viewpager4, container, false);
                    break;
                case 7: imageLayout = inflater.inflate(R.layout.layout_viewpager5, container, false);
                    break;
                default: imageLayout = inflater.inflate(R.layout.layout_viewpager, container, false);
            }
            assert imageLayout != null;
            final RoundedImageView imageView_my = imageLayout.findViewById(R.id.image);
            final ImageView imageView_play = imageLayout.findViewById(R.id.iv_vp_play);
            final ProgressBar spinner = imageLayout.findViewById(R.id.loading);
            final RelativeLayout my  = imageLayout.findViewById(R.id.background);

            switch (Setting.Now_Play){
                case 5:
                    if (Setting.isOnline) {
                        if (Setting.Blor_image){
                            final ImageView imageViewBackground = imageLayout.findViewById(R.id.bl);
                            Target target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    imageViewBackground.setImageBitmap(BlurImage.fastblur(bitmap, 1f, 55));
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    imageViewBackground.setImageResource(Integer.parseInt(Setting.arrayList_play.get(position).getImageBig()));
                                }
                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            };
                            imageViewBackground.setTag(target);
                            Picasso.get()
                                    .load(Setting.arrayList_play.get(position).getImageBig())
                                    .placeholder(R.drawable.album)
                                    .into(target);
                        }
                    }
                    break;
                case 6:
                    if (Setting.isOnline) {
                        if (Setting.Blor_image){
                            final ImageView imageViewBackground = imageLayout.findViewById(R.id.bl);
                            Target target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    imageViewBackground.setImageBitmap(BlurImage.fastblur(bitmap, 1f, 55));
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    imageViewBackground.setImageResource(Integer.parseInt(Setting.arrayList_play.get(position).getImageBig()));
                                }
                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            };
                            imageViewBackground.setTag(target);
                            Picasso.get()
                                    .load(Setting.arrayList_play.get(position).getImageBig())
                                    .placeholder(R.drawable.album)
                                    .into(target);
                        }
                    }
                    break;

            }
            loadedPage = Setting.addedFrom;

            if (Setting.playPos == position) {
                imageView_play.setVisibility(View.GONE);
            }

            if (Setting.isOnline) {
                Picasso.get()
                        .load(Setting.arrayList_play.get(position).getImageBig())
                        .placeholder(R.drawable.tamilaudio2)
                        .into(imageView_my, new Callback() {
                            @Override
                            public void onSuccess() {
                                spinner.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                spinner.setVisibility(View.GONE);
                            }
                        });

                Picasso.get()
                        .load(Setting.arrayList_play.get(position).getImageSmall())
                        .centerCrop()
                        .resize(100, 100)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                assert imageView_my != null;
                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                if (textSwatch == null) {
                                                    return;
                                                }
                                                my.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                            }
                                        });
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
            } else {
                final Uri uri;
                if (Setting.isDownloaded) {
                    uri = Uri.fromFile(new File(Setting.arrayList_play.get(position).getImageSmall()));
                } else {
                    uri = methods.getAlbumArtUri(Integer.parseInt(Setting.arrayList_play.get(position).getImageBig()));
                }
                Picasso.get()
                        .load(uri)
                        .placeholder(R.drawable.songs)
                        .into(imageView_my);

                Picasso.get()
                        .load(uri)
                        .centerCrop()
                        .resize(100, 100)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                assert imageView_my != null;
                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                if (textSwatch == null) {
                                                    return;
                                                }
                                                my.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                            }
                                        });
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                if (Setting.Blor_image){
                        if (Setting.isDownloaded) {
                            final ImageView imageViewBackground = imageLayout.findViewById(R.id.bl);
                            Target target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    imageViewBackground.setImageBitmap(BlurImage.fastblur(bitmap, 1f, 55));
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    imageViewBackground.setImageResource(Integer.parseInt(String.valueOf(uri)));
                                }
                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            };
                            imageViewBackground.setTag(target);
                            Picasso.get()
                                    .load(uri)
                                    .placeholder(R.drawable.album)
                                    .into(target);
                        }
                    }
                spinner.setVisibility(View.GONE);
            }

            imageView_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Setting.playPos = viewpager.getCurrentItem();
                    if (!Setting.isOnline || methods.isNetworkAvailable()) {
                        Intent intent = new Intent(BaseActivity.this, PlayerService.class);
                        intent.setAction(PlayerService.ACTION_PLAY);
                        startService(intent);
                        imageView_play.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (position == 0) {
                imageView_pager = imageView_my;
            }

            imageLayout.setTag("myview" + position);
            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

   

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onSongChange(ItemSong itemSong) {
        changeText(itemSong, "home");
        Setting.context = BaseActivity.this;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBufferChange(MessageEvent messageEvent) {
        if (messageEvent.message.equals("buffer")) {
            isBuffering(messageEvent.flag);
        } else {
            changePlayPauseIcon(messageEvent.flag);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onViewPagerChanged(ItemMyPlayList itemMyPlayList) {
        imagePagerAdapter.notifyDataSetChanged();
        tv_song_count.setText(Setting.playPos + 1 + "/" + Setting.arrayList_play.size());
        GlobalBus.getBus().removeStickyEvent(itemMyPlayList);
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    
    public void onStop() {
        GlobalBus.getBus().unregister(this);
        super.onStop();
    }

    public Boolean checkPer() {
        if ((ContextCompat.checkSelfPermission(BaseActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE"}, 1);
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
                }
                if (!canUseExternalStorage) {
                    Toast.makeText(BaseActivity.this, getResources().getString(R.string.err_cannot_use_features), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        seekHandler.removeCallbacks(run);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN){
            try {
                volumeSeekBar.setProgress(audioManager.getStreamVolume(exoPlayer.getAudioStreamType()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){
            try {
                volumeSeekBar.setProgress(audioManager.getStreamVolume(exoPlayer.getAudioStreamType()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}