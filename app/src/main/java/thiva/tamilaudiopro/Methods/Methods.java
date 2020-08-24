package thiva.tamilaudiopro.Methods;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import okhttp3.MediaType;
import thiva.tamilaudiopro.Activity.BaseActivity;
import thiva.tamilaudiopro.Activity.BuildConfig;
import thiva.tamilaudiopro.Activity.DownloadService;
import thiva.tamilaudiopro.Activity.FirebaseSignUpActivity;
import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Listener.InterAdListener;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.SharedPre.MY_API;
import thiva.tamilaudiopro.Utils.DBHelper;
import thiva.tamilaudiopro.JSONParser.JSONParser;
import thiva.tamilaudiopro.SharedPre.SharedPref;
import thiva.tamilaudiopro.Adapter.AdapterPlaylistDialog;
import thiva.tamilaudiopro.Listener.ClickListenerPlayList;
import thiva.tamilaudiopro.item.ItemMyPlayList;
import thiva.tamilaudiopro.item.ItemName;
import thiva.tamilaudiopro.item.ItemSong;
import thiva.tamilaudiopro.item.ItemUser;

import thiva.tamilaudiopro.Activity.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import javax.crypto.SecretKey;
import javax.net.ssl.HttpsURLConnection;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import thiva.tamilaudiopro.preferences.PreferenceUtil;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */
import static thiva.tamilaudiopro.Activity.BaseActivity.mLayout;
public class Methods {

    @SerializedName("song_image")
    String song_image = "song_image";
    private Context context;
    private DBHelper dbHelper;
    private SecretKey key;
    private InterAdListener interAdListener;
    private InterstitialAd interstitialAd;
    private com.facebook.ads.InterstitialAd interstitialAdFB;

    public Methods(Context context, Boolean flag) {
        this.context = context;
        Store store = new Store(context);
        if (!store.hasKey(BuildConfig.SECRETKEY)) {
            key = store.generateSymmetricKey(BuildConfig.SECRETKEY, null);
        } else {
            key = store.getSymmetricKey(BuildConfig.SECRETKEY, null);
        }
    }

    public Methods(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public Methods(Context context, InterAdListener interAdListener) {
        this.context = context;
        this.interAdListener = interAdListener;
        loadad();
    }

    private void loadad() {
        if (!Setting.getPurchases){
            if (Setting.isAdmobInterAd) {
                interstitialAd = new InterstitialAd(context);
                AdRequest adRequest;
                if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                    adRequest = new AdRequest.Builder()
                            .build();
                } else {
                    Bundle extras = new Bundle();
                    extras.putString("npa", "1");
                    adRequest = new AdRequest.Builder()
                            .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                            .build();
                }
                interstitialAd.setAdUnitId(Setting.ad_inter_id);
                interstitialAd.loadAd(adRequest);
            } else if (Setting.isFBInterAd) {
                interstitialAdFB = new com.facebook.ads.InterstitialAd(context, Setting.fb_ad_inter_id);
                interstitialAdFB.loadAd();
            }
        }
    }

    public void showInterAd(final int pos, final String type) {
        if (Setting.getPurchases){
            interAdListener.onClick(pos, type);
            if(mLayout != null && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){

                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        }else {
            if (Setting.isAdmobInterAd) {
                Setting.adCount = Setting.adCount + 1;
                if (Setting.adCount % Setting.adShow == 0) {
                    interstitialAd.setAdListener(new com.google.android.gms.ads.AdListener() {
                        @Override
                        public void onAdClosed() {
                            interAdListener.onClick(pos, type);
                            if(mLayout != null && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){

                                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                            }
                            super.onAdClosed();
                        }
                    });
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    } else {
                        interAdListener.onClick(pos, type);
                        if(mLayout != null && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){

                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        }
                    }
                    loadad();
                } else {
                    interAdListener.onClick(pos, type);
                    if(mLayout != null && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){

                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                }
            } else if (Setting.isFBInterAd) {
                Setting.adCount = Setting.adCount + 1;
                if (Setting.adCount % Setting.adShowFB == 0) {
                    interstitialAdFB.setAdListener(new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                            interAdListener.onClick(pos, type);
//                            if(mLayout != null && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
//
//                                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                            }
                        }

                        @Override
                        public void onError(com.facebook.ads.Ad ad, AdError adError) {

                        }

                        @Override
                        public void onAdLoaded(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onAdClicked(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(com.facebook.ads.Ad ad) {

                        }
                    });
                    if (interstitialAdFB.isAdLoaded()) {
                        interstitialAdFB.show();
                    } else {
                        interAdListener.onClick(pos, type);
//                        if(mLayout != null && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
//
//                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                        }
                    }
                    loadad();
                } else {
                    interAdListener.onClick(pos, type);
//                    if(mLayout != null && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
//
//                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                    }
                }
            } else {
                interAdListener.onClick(pos, type);
//                if(mLayout != null  && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
//                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                }
            }
        }
    }

    private void showPersonalizedAds(LinearLayout linearLayout) {
        if (Setting.isAdmobBannerAd) {
            AdView adView = new AdView(context);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("0336997DCA346E1612B610471A578EEB").build();
            adView.setAdUnitId(Setting.ad_banner_id);
            adView.setAdSize(AdSize.BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        } else if (Setting.isFBBannerAd) {
            com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, Setting.fb_ad_banner_id, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            adView.loadAd();
            linearLayout.addView(adView);
        }
    }

    private void showNonPersonalizedAds(LinearLayout linearLayout) {
        if (Setting.isAdmobBannerAd) {
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            AdView adView = new AdView(context);
            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
            adView.setAdUnitId(Setting.ad_banner_id);
            adView.setAdSize(AdSize.BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        } else if (Setting.isFBBannerAd) {
            com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, Setting.fb_ad_banner_id, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            adView.loadAd();
            linearLayout.addView(adView);
        }
    }

    public void showBannerAd(LinearLayout linearLayout) {
        if (!Setting.getPurchases){
            if (isNetworkAvailable() && linearLayout != null) {
                if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
                    showNonPersonalizedAds(linearLayout);
                } else {
                    showPersonalizedAds(linearLayout);
                }
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point point = new Point();
        point.x = display.getWidth();
        point.y = display.getHeight();
        columnWidth = point.x;
        return columnWidth;
    }

    public int getScreenHeight() {
        int height;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point point = new Point();
        point.x = display.getWidth();
        point.y = display.getHeight();
        height = point.y;
        return height;
    }

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    public void animateHeartButton(final View v) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(v, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(v, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String hourString = "";
        String secondsString = "";
        String minutesString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        long temp_milli = (long) calculateTime(Setting.arrayList_play.get(Setting.playPos).getDuration());
        int temp_hour = (int) (temp_milli / (1000 * 60 * 60));
        if (temp_hour != 0) {
            hourString = hours + ":";
        }
        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        // Prepending 0 to minutes if it is one digit
        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }

        finalTimerString = hourString + minutesString + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public String milliSecondsToTimerDownload(long milliseconds) {
        String finalTimerString = "";
        String hourString = "";
        String secondsString = "";
        String minutesString = "";
        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours != 0) {
            hourString = hours + ":";
        }
        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        // Prepending 0 to minutes if it is one digit
        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }
        finalTimerString = hourString + minutesString + ":" + secondsString;
        // return timer string
        return finalTimerString;
    }

    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;
        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;
        // return percentage
        return percentage.intValue();
    }

    public long getSeekFromPercentage(int percentage, long totalDuration) {
        long currentSeconds = 0;
        long totalSeconds = (int) (totalDuration / 1000);
        // calculating percentage
        currentSeconds = (percentage * totalSeconds) / 100;
        // return percentage
        return currentSeconds * 1000;
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = totalDuration / 1000;
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public int calculateTime(String duration) {
        int time = 0, min, sec, hr = 0;
        try {
            StringTokenizer st = new StringTokenizer(duration, ".");
            if (st.countTokens() == 3) {
                hr = Integer.parseInt(st.nextToken());
            }
            min = Integer.parseInt(st.nextToken());
            sec = Integer.parseInt(st.nextToken());
        } catch (Exception e) {
            StringTokenizer st = new StringTokenizer(duration, ":");
            if (st.countTokens() == 3) {
                hr = Integer.parseInt(st.nextToken());
            }
            min = Integer.parseInt(st.nextToken());
            sec = Integer.parseInt(st.nextToken());
        }
        time = ((hr * 3600) + (min * 60) + sec) * 1000;
        return time;
    }

    public void setStatusColor2(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(context.getResources().getColor(R.color.colorAccent_Light));
        }
    }

    public String format(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

    public int getColumnWidth(int column, int grid_padding) {
        Resources r = context.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, grid_padding, r.getDisplayMetrics());
        return (int) ((getScreenWidth() - ((column + 1) * padding)) / column);
    }

    public GradientDrawable getGradientDrawable(int first, int second) {
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(15);
        gd.setColors(new int[]{first, second});
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gd.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
        gd.mutate();
        return gd;
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            InputStream input;
            if(Setting.SERVER_URL.contains("https://")) {
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            } else {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            }
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isYoutubeAppInstalled() {
        Intent mIntent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
        return mIntent != null;
    }

    public void openPlaylists(final ItemSong itemSong, final Boolean isOnline) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_playlist);

        final ArrayList<ItemMyPlayList> arrayList_playlist = dbHelper.loadPlayList(isOnline);

        final ImageView iv_close = dialog.findViewById(R.id.iv_playlist_close);
        final Button button_close = dialog.findViewById(R.id.button_close_dialog);
        final TextView textView = dialog.findViewById(R.id.tv_empty_dialog_pl);
        final RecyclerView recyclerView = dialog.findViewById(R.id.rv_dialog_playlist);
        final LinearLayout ll_add_playlist = dialog.findViewById(R.id.ll_add_playlist);
        final AppCompatButton btn_add_new = dialog.findViewById(R.id.button_add);
        final AppCompatButton btn_add = dialog.findViewById(R.id.button_dialog_addplaylist);
        final EditText et_Add = dialog.findViewById(R.id.et_playlist_name);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        final AdapterPlaylistDialog adapterPlaylist = new AdapterPlaylistDialog(arrayList_playlist, new ClickListenerPlayList() {
            @Override
            public void onClick(int position) {
                dbHelper.addToPlayList(itemSong, arrayList_playlist.get(position).getId(), isOnline);
                Toast.makeText(context, context.getString(R.string.song_add_to_playlist) + arrayList_playlist.get(position).getName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onItemZero() {
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        recyclerView.setAdapter(adapterPlaylist);
        if (arrayList_playlist.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_add_playlist.setVisibility(View.VISIBLE);
                btn_add.setVisibility(View.GONE);
            }
        });

        btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_Add.getText().toString().trim().isEmpty()) {
                    arrayList_playlist.clear();
                    arrayList_playlist.addAll(dbHelper.addPlayList(et_Add.getText().toString(), isOnline));
                    adapterPlaylist.notifyDataSetChanged();
                    textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    Toast.makeText(context, context.getString(R.string.playlist_added), Toast.LENGTH_SHORT).show();

                    et_Add.setText("");
                } else {
                    Toast.makeText(context, context.getString(R.string.enter_playlist_name), Toast.LENGTH_SHORT).show();
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    public void getListOfflineSongs() {
        long aa = System.currentTimeMillis();
        Setting.arrayListOfflineSongs.clear();
        ContentResolver contentResolver = context.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, MediaStore.Audio.Media.TITLE + " ASC");

        if (songCursor != null && songCursor.moveToFirst()) {
            do {
                String id = String.valueOf(songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                long duration_long = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String title = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String url = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String image = "null";

                long albumId = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                image = String.valueOf(albumId);

                String duration = milliSecondsToTimerDownload(duration_long);

                String desc = context.getString(R.string.title) + " - " + title + "</br>" + context.getString(R.string.artist) + " - " + artist;

                Setting.arrayListOfflineSongs.add(new ItemSong(id, "", "", artist, url, image, image, title, duration, desc, "0", "0", "0", "0"));
            } while (songCursor.moveToNext());
        }
        long bb = System.currentTimeMillis();
    }

    public Uri getAlbumArtUri(int album_id) {
        Uri songCover = Uri.parse("content://media/external/audio/albumart");
        Uri uriSongCover = ContentUris.withAppendedId(songCover, album_id);
        if (uriSongCover == null) {
            return null;
        }
        return uriSongCover;
    }

    public void shareSong(ItemSong itemSong, Boolean isOnline) {
        if (isOnline) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.share_song));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, context.getResources().getString(R.string.listening) + " - " + itemSong.getTitle() + "\n\nvia " + context.getResources().getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + context.getPackageName());
            context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_song)));
        } else {
            try {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/mp3");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(itemSong.getUrl()));
                share.putExtra(android.content.Intent.EXTRA_TEXT, context.getResources().getString(R.string.listening) + " - " + itemSong.getTitle() + "\n\nvia " + context.getResources().getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + context.getPackageName());
                context.startActivity(Intent.createChooser(share, context.getResources().getString(R.string.share_song)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String encrypt(String value) {
        try {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.encrypt(value, key);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String decrypt(String value) {
        try {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.decrypt(value, key);
        } catch (Exception e) {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.encrypt("null", key);
        }
    }

    public void clickLogin() {
        if (Setting.isLogged) {
            logout((Activity) context);
            Toast.makeText(context, context.getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("from", "app");
            context.startActivity(intent);
        }
    }

    public void changeAutoLogin(Boolean isAutoLogin) {
        SharedPref sharePref = new SharedPref(context);
        sharePref.setIsAutoLogin(isAutoLogin);
    }

    public void logout(Activity activity) {
        changeAutoLogin(false);
        Setting.isLogged = false;
        Setting.itemUser = new ItemUser("", "", "", "");
        Intent intent1 = new Intent(context, LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.putExtra("from", "");
        context.startActivity(intent1);
        activity.finish();
    }

    public void DataSave(){
        File Database = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getString(R.string.app_name) + "/Database/SQL");
        if (!Database.exists()) {
            Database.mkdirs();
        }

        File Json = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getString(R.string.app_name) + "/Json");
        if (!Json.exists()) {
            Json.mkdirs();
        }

        File Update = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getString(R.string.app_name) + "/Update/App");
        if (!Update.exists()) {
            Update.mkdirs();
        }
    }

    public void download(final ItemSong itemSong) {
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getString(R.string.app_name) + "/temp");
        if (!root.exists()) {
            root.mkdirs();
        }

        Random random = new Random();
        String a = String.valueOf(System.currentTimeMillis());
        String name = String.valueOf(random.nextInt((999999 - 100000) + 100000)) + a.substring(a.length() - 6, a.length() - 1);

        File file = new File(root, name + ".mp3");

        if (!dbHelper.checkDownload(itemSong.getId())) {

            String url = itemSong.getUrl();

            if (!DownloadService.getInstance().isDownloading()) {
                Intent serviceIntent = new Intent(context, DownloadService.class);
                serviceIntent.setAction(DownloadService.ACTION_START);
                serviceIntent.putExtra("downloadUrl", url);
                serviceIntent.putExtra("file_path", root.toString());
                serviceIntent.putExtra("file_name", file.getName());
                serviceIntent.putExtra("item", itemSong);
                context.startService(serviceIntent);
            } else {
                Intent serviceIntent = new Intent(context, DownloadService.class);
                serviceIntent.setAction(DownloadService.ACTION_ADD);
                serviceIntent.putExtra("downloadUrl", url);
                serviceIntent.putExtra("file_path", root.toString());
                serviceIntent.putExtra("file_name", file.getName());
                serviceIntent.putExtra("item", itemSong);
                context.startService(serviceIntent);
            }

            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... strings) {

                    String json = JSONParser.okhttpPost(Setting.SERVER_URL, getAPIRequest(ItemName.METHOD_DOWNLOAD_COUNT, 0, "", itemSong.getId(), "", "", "", "", "", "","","","","","","","", null));
                    return null;
                }
            }.execute();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.already_download), Toast.LENGTH_SHORT).show();
        }
    }

    public void getVerifyDialog(String title, String message) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.ThemeDialog);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
    public RequestBody getAPIRequest(String method, int page, String deviceID, String songID, String searchText, String searchType, String catID, String albumID, String artistName, String playListID, String rate, String email, String password, String name, String phone, String userID, String reportMessage, File file) {
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new MY_API());
        jsObj.addProperty("method_name", method);
        jsObj.addProperty("package_name", context.getPackageName());

        switch (method) {
            case BuildConfig.API_KEY:
                jsObj.addProperty("key_id", deviceID);
                break;
            case ItemName.METHOD_LATEST:
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_SINGLE_SONG:
                jsObj.addProperty("song_id", songID);
                jsObj.addProperty("device_id", deviceID);
                break;
            case ItemName.METHOD_MY_SEARCH:
                jsObj.addProperty("search_text", searchText);
                break;
            case ItemName.METHOD_SEARCH:
                jsObj.addProperty("page", String.valueOf(page));
                jsObj.addProperty("search_text", searchText);
                jsObj.addProperty("search_type", searchType);
                break;
            case ItemName.METHOD_ALL_SONGS:
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_ALBUM_CAT_ID:
                jsObj.addProperty("cat_id", catID);
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_SEARCH_MY:
                jsObj.addProperty("search_text", searchText);
                break;
            case ItemName.METHOD_SONG_BY_CAT:
                jsObj.addProperty("cat_id", catID);
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_SONG_BY_ALBUMS:
                jsObj.addProperty("album_id", albumID);
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_SONG_BY_ARTIST:
                jsObj.addProperty("artist_name", artistName);
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_SONG_BY_PLAYLIST:
                jsObj.addProperty("playlist_id", playListID);
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_ALBUMS:
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_ARTIST:
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_CAT:
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_SERVER_PLAYLIST:
                jsObj.addProperty("page", String.valueOf(page));
                break;
            case ItemName.METHOD_RATINGS:
                jsObj.addProperty("post_id", songID);
                jsObj.addProperty("device_id", deviceID);
                jsObj.addProperty("rate", rate);
                break;
            case ItemName.METHOD_LOGIN:
                jsObj.addProperty("email", email);
                jsObj.addProperty("password", password);
                break;
            case ItemName.METHOD_REGISTER:
                jsObj.addProperty("name", name);
                jsObj.addProperty("email", email);
                jsObj.addProperty("password", password);
                jsObj.addProperty("phone", phone);
                break;
            case ItemName.METHOD_FORGOT_PASSWORD:
                jsObj.addProperty("user_email", email);
                break;
            case ItemName.METHOD_PROFILE:
                jsObj.addProperty("user_id", userID);
                break;
            case ItemName.METHOD_PROFILE_EDIT:
                jsObj.addProperty("user_id", userID);
                jsObj.addProperty("name", name);
                jsObj.addProperty("email", email);
                jsObj.addProperty("password", password);
                jsObj.addProperty("phone", phone);
                break;
            case ItemName.METHOD_ALBUMS_BY_ARTIST:
                jsObj.addProperty("artist_id", songID);
                jsObj.addProperty("page", page);
                break;
            case ItemName.METHOD_DOWNLOAD_COUNT:
                jsObj.addProperty("song_id", songID);
                break;
            case ItemName.METHOD_REPORT:
                jsObj.addProperty("user_id", userID);
                jsObj.addProperty("song_id", songID);
                jsObj.addProperty("report", reportMessage);
                break;
            case ItemName.METHOD_SUGGESTION:
                jsObj.addProperty("user_id", userID);
                jsObj.addProperty("song_title", name);
                jsObj.addProperty("message", reportMessage);
                break;
            case ItemName.METHOD_NOTIFICATION:
                jsObj.addProperty("user_id", userID);
                break;
        }

        if (method.equals(ItemName.METHOD_SUGGESTION)) {
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            return new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(song_image, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file))
                    .addFormDataPart("data", MY_API.toBase64(jsObj.toString()))
                    .build();
        } else {
            return new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("data", MY_API.toBase64(jsObj.toString()))
                    .build();
        }
    }

    public void Pro_Version_Dialog() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view2 = inflater.inflate(R.layout.dialog_pro_version, null);
        final BottomSheetDialog dialog_setas = new BottomSheetDialog(context);
        dialog_setas.setContentView(view2);
        dialog_setas.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        final TextView text_view_go_pro= (TextView) dialog_setas.findViewById(R.id.text_view_go_pro);
        text_view_go_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_setas.cancel();
            }
        });
        dialog_setas.show();
    }

    public void getColroImage(ImageView color_image) {
        switch (Setting.get_color_my){
            case 0: color_image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Toolbar_1)));
                break;
            case 1: color_image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Toolbar_2)));
                break;
            case 2: color_image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Toolbar_3)));
                break;
            case 3: color_image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Toolbar_4)));
                break;
            case 4: color_image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Toolbar_5)));
                break;
            case 5: color_image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Toolbar_6)));
                break;
            case 6: color_image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Toolbar_7)));
                break;
            case 7: color_image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Toolbar_8)));
                break;
            default: color_image.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Toolbar_8)));
        }
    }

    public void Toolbar_Color(Toolbar toolbar, Window window, ActionBar actionBar, String Type) {
        if (Setting.ToolBar_Color) {
            if (Type.equals("Home")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_menu2);
                    toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));
                }
            }else if (Type.equals("CatActivity")){

            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
                    toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));
                }
            }
            switch (Setting.get_color_my){
                case 0: toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.Toolbar_1));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(context.getResources().getColor(R.color.Toolbar_1));
                    }
                    break;
                case 1: toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.Toolbar_2));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(context.getResources().getColor(R.color.Toolbar_2));
                    }
                    break;
                case 2: toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.Toolbar_3));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(context.getResources().getColor(R.color.Toolbar_3));
                    }
                    break;
                case 3: toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.Toolbar_4));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(context.getResources().getColor(R.color.Toolbar_4));
                    }
                    break;
                case 4: toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.Toolbar_5));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(context.getResources().getColor(R.color.Toolbar_5));
                    }
                    break;
                case 5: toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.Toolbar_6));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(context.getResources().getColor(R.color.Toolbar_6));
                    }
                    break;
                case 6: toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.Toolbar_7));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(context.getResources().getColor(R.color.Toolbar_7));
                    }
                    break;
                case 7: toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.Toolbar_8));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(context.getResources().getColor(R.color.Toolbar_8));
                    }
                    break;
                default: toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.Toolbar_8));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(context.getResources().getColor(R.color.Toolbar_8));
                    }
            }
        }
    }


    public String getPathImage(Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String filePath = "";
                String wholeID = DocumentsContract.getDocumentId(uri);

                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                return filePath;
            } else {

                if (uri == null) {
                    return null;
                }
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String retunn = cursor.getString(column_index);
                    cursor.close();
                    return retunn;
                }
                return uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (uri == null) {
                return null;
            }
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String returnn = cursor.getString(column_index);
                cursor.close();
                return returnn;
            }
            return uri.getPath();
        }
    }



}