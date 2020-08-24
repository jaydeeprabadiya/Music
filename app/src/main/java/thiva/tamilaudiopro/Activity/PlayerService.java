package thiva.tamilaudiopro.Activity;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.app.NotificationCompat.MediaStyle;
import androidx.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.DBHelper;
import thiva.tamilaudiopro.Utils.GlobalBus;
import thiva.tamilaudiopro.Utils.HardButtonReceiver;
import thiva.tamilaudiopro.JSONParser.JSONParser;
import thiva.tamilaudiopro.Utils.MediaButtonIntentReceiver;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.Utils.StreamDataSource;
import thiva.tamilaudiopro.item.ItemAlbums;
import thiva.tamilaudiopro.item.ItemName;
import thiva.tamilaudiopro.item.MessageEvent;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class PlayerService extends IntentService implements Player.EventListener, HardButtonReceiver.HardButtonListener {

    public static final String ACTION_TOGGLE = "action.ACTION_TOGGLE";
    public static final String ACTION_PLAY = "action.ACTION_PLAY";
    public static final String ACTION_NEXT = "action.ACTION_NEXT";
    public static final String ACTION_PREVIOUS = "action.ACTION_PREVIOUS";
    public static final String ACTION_STOP = "action.ACTION_STOP";
    public static final String ACTION_SEEKTO = "action.ACTION_SEEKTO";

    @SuppressLint("StaticFieldLeak")
    static SimpleExoPlayer exoPlayer = null;
    @SuppressLint("StaticFieldLeak")
    static private Context context;
    @SuppressLint("StaticFieldLeak")
    static PlayerService playerService;

    NotificationManager mNotificationManager;
    NotificationCompat.Builder notification;
    RemoteViews bigViews, smallViews;
    DefaultBandwidthMeter bandwidthMeter;
    DataSource.Factory dataSourceFactory;
    ExtractorsFactory extractorsFactory;

    Methods methods;
    DBHelper dbHelper;
    Boolean isNewSong = false;
    Bitmap bitmap;
    ComponentName componentName;
    AudioManager mAudioManager;
    PowerManager.WakeLock mWakeLock;
    private HardButtonReceiver mButtonReceiver;
    LoadSong loadSong;

    public PlayerService() {
        super(null);
    }

    static public PlayerService getInstance() {
        if (playerService == null) {
            playerService = new PlayerService();
        }
        return playerService;
    }

    public static Boolean getIsPlayling() {
        return exoPlayer != null && exoPlayer.getPlayWhenReady();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        methods = new Methods(getApplicationContext());
        dbHelper = new DBHelper(getApplicationContext());

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        componentName = new ComponentName(getPackageName(), MediaButtonIntentReceiver.class.getName());
        mAudioManager.registerMediaButtonEventReceiver(componentName);

        bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                Util.getUserAgent(getApplicationContext(), "onlinemp3"), bandwidthMeter);
        extractorsFactory = new DefaultExtractorsFactory();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
        exoPlayer.addListener(this);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.setReferenceCounted(false);


        IntentFilter iF = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
         /**
         * Assign the intent filter a high priority so this receiver
         * gets called first on a button press which can then determine
         * whether to pass it down to other apps or not (i.e. the Music app)
         *
         * Initially I found the SYSTEM_HIGH_PRIORITY didn't work, lastfm seemed
         * to get first request of the button press, then the music player and
         * this example didn't get access to it.
         *
         * So while +1 works, its extremely easy to break (i.e. another developer uses
         * SYSTEM_HIGH_PRIORITY + 1 then you may not get access to the button event).
         *
         * Also the Document says to use a value between SYSTEM_LOW_PRIORITY and
         * SYSTEM_HIGH_PRIORITY (i.e. -1000 & 1000)
         */
        iF.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY + 1);
        // register the receiver
        registerReceiver(mButtonReceiver, iF);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        try {
            String action = intent.getAction();
            switch (action) {

                case ACTION_PLAY:
                    startNewSong();
                    break;
                case ACTION_TOGGLE:
                    togglePlay();
                    break;
                case ACTION_SEEKTO:
                    seekTo(intent.getExtras().getLong("seekto"));
                    break;
                case ACTION_STOP:
                    stop(intent);
                    break;
                case ACTION_PREVIOUS:
                    if (!Setting.isOnline || methods.isNetworkAvailable()) {
                        previous();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ACTION_NEXT:
                    if (!Setting.isOnline || methods.isNetworkAvailable()) {
                        next();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    private void startNewSong() {
        loadSong = new LoadSong();
        loadSong.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadSong extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            isNewSong = true;
            setBuffer(true);
            GlobalBus.getBus().postSticky(Setting.arrayList_play.get(Setting.playPos));
        }

        protected Boolean doInBackground(final String... args) {
            try {
                final String finalUrl  = Setting.arrayList_play.get(Setting.playPos).getUrl();
                ExtractorMediaSource sampleSource;
                if (Setting.isOnline || !Setting.isDownloaded) {
                    dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                            Util.getUserAgent(getApplicationContext(), "onlinemp3"), bandwidthMeter);
                    sampleSource = new ExtractorMediaSource(Uri.parse(finalUrl),
                            dataSourceFactory, extractorsFactory, null, null);
                } else {
                    dataSourceFactory = new DataSource.Factory() {
                        @Override
                        public DataSource createDataSource() {
                            return new StreamDataSource(getApplicationContext(), new File(finalUrl).getAbsolutePath());
                        }
                    };
                    sampleSource = new ExtractorMediaSource(Uri.parse(finalUrl),
                            dataSourceFactory, extractorsFactory, null, null);
                }
                exoPlayer.prepare(sampleSource);
                exoPlayer.setPlayWhenReady(true);

                if (!Setting.isDownloaded) {
                    dbHelper.addToRecent(Setting.arrayList_play.get(Setting.playPos), Setting.isOnline);
                }
                return true;
            } catch (Exception e1) {
                e1.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (context != null) {
                super.onPostExecute(aBoolean);
                if (!aBoolean) {
                    try {
                        isNewSong = false;
                        setBuffer(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void togglePlay() {
        if (exoPlayer.getPlayWhenReady()) {
            exoPlayer.setPlayWhenReady(false);
        } else {
            exoPlayer.setPlayWhenReady(true);
        }
        changePlayPause(exoPlayer.getPlayWhenReady());
        updateNotiPlay(exoPlayer.getPlayWhenReady());
    }

    private void previous() {
        setBuffer(true);
        if (Setting.isSuffle) {
            Random rand = new Random();
            Setting.playPos = rand.nextInt((Setting.arrayList_play.size() - 1) + 1);
        } else {
            if (Setting.playPos > 0) {
                Setting.playPos = Setting.playPos - 1;
            } else {
                Setting.playPos = Setting.arrayList_play.size() - 1;
            }
        }
        startNewSong();
    }

    private void next() {
        setBuffer(true);
        if (Setting.isSuffle) {
            Random rand = new Random();
            Setting.playPos = rand.nextInt((Setting.arrayList_play.size() - 1) + 1);
        } else {
            if (Setting.playPos < (Setting.arrayList_play.size() - 1)) {
                Setting.playPos = Setting.playPos + 1;
            } else {
                Setting.playPos = 0;
            }
        }
        startNewSong();
    }

    private void seekTo(long seek) {
        exoPlayer.seekTo((int) seek);
    }

    private void onCompletion() {
        if (Setting.isRepeat) {
            exoPlayer.seekTo(0);
        } else {
            if (Setting.isSuffle) {
                Random rand = new Random();
                Setting.playPos = rand.nextInt((Setting.arrayList_play.size() - 1) + 1);
            } else {
                next();
            }
        }
        startNewSong();
    }

    private void changePlayPause(Boolean flag) {
        try {
            changeEquilizer();
            GlobalBus.getBus().postSticky(new MessageEvent(flag, "playicon"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBuffer(Boolean isBuffer) {
        try {
            if (!isBuffer) {
                changeEquilizer();
            }
            GlobalBus.getBus().postSticky(new MessageEvent(isBuffer, "buffer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeEquilizer() {
        try {
            GlobalBus.getBus().postSticky(new ItemAlbums("", "", "", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void stop(Intent intent) {
        try {
            Setting.isPlayed = false;
            exoPlayer.setPlayWhenReady(false);
            changePlayPause(false);
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
            try {
                mAudioManager.abandonAudioFocus(focusChangeListener);
                mAudioManager.unregisterMediaButtonEventReceiver(componentName);
                unregisterReceiver(onHeadPhoneDetect);
            } catch (Exception e) {
                e.printStackTrace();
            }
            stopService(intent);
            stopForeground(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNoti() {
        bigViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        smallViews = new RemoteViews(getPackageName(), R.layout.layout_noti_small);

        Intent notificationIntent = new Intent(this, SplashActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.putExtra("isnoti", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent previousIntent = new Intent(this, PlayerService.class);
        previousIntent.setAction(ACTION_PREVIOUS);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, PlayerService.class);
        playIntent.setAction(ACTION_TOGGLE);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, PlayerService.class);
        nextIntent.setAction(ACTION_NEXT);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, PlayerService.class);
        closeIntent.setAction(ACTION_STOP);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        String NOTIFICATION_CHANNEL_ID = "onlinemp3_ch_1";
        notification = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name))
                .setPriority(Notification.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker(Setting.arrayList_play.get(Setting.playPos).getTitle())
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setOnlyAlertOnce(true);

        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(mChannel);

            MediaSessionCompat mMediaSession;
            mMediaSession = new MediaSessionCompat(getApplicationContext(), getString(R.string.app_name));
            mMediaSession.setFlags(
                    MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

            notification.setStyle(new MediaStyle()
                    .setMediaSession(mMediaSession.getSessionToken())
                    .setShowCancelButton(true)
                    .setShowActionsInCompactView(0, 1, 2)
                    .setCancelButtonIntent(
                            MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_STOP)))
                    .addAction(new NotificationCompat.Action(
                            R.drawable.ic_skip_previous_white_24dp, "Previous",
                            ppreviousIntent))
                    .addAction(new NotificationCompat.Action(
                            R.drawable.ic_pause_white_24dp, "Pause",
                            pplayIntent))
                    .addAction(new NotificationCompat.Action(
                            R.drawable.ic_skip_next_white_24dp, "Next",
                            pnextIntent))
                    .addAction(new NotificationCompat.Action(
                            R.drawable.ic_close_white_24dp, "Close",
                            pcloseIntent));

            notification.setContentTitle(Setting.arrayList_play.get(Setting.playPos).getTitle());
            notification.setContentText(Setting.arrayList_play.get(Setting.playPos).getArtist());
        } else {
            bigViews.setOnClickPendingIntent(R.id.imageView_noti_play, pplayIntent);
            bigViews.setOnClickPendingIntent(R.id.imageView_noti_next, pnextIntent);
            bigViews.setOnClickPendingIntent(R.id.imageView_noti_prev, ppreviousIntent);
            bigViews.setOnClickPendingIntent(R.id.imageView_noti_close, pcloseIntent);
            smallViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
            bigViews.setImageViewResource(R.id.imageView_noti_play, android.R.drawable.ic_media_pause);
            bigViews.setTextViewText(R.id.textView_noti_name, Setting.arrayList_play.get(Setting.playPos).getTitle());
            smallViews.setTextViewText(R.id.status_bar_track_name, Setting.arrayList_play.get(Setting.playPos).getTitle());
            bigViews.setTextViewText(R.id.textView_noti_artist, Setting.arrayList_play.get(Setting.playPos).getArtist());
            smallViews.setTextViewText(R.id.status_bar_artist_name, Setting.arrayList_play.get(Setting.playPos).getArtist());

            notification.setCustomContentView(smallViews).setCustomBigContentView(bigViews);
        }
        startForeground(101, notification.build());
        updateNotiImage();
    }

    @SuppressLint("StaticFieldLeak")
    private void updateNotiImage() {
        new AsyncTask<String, String, String>() {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    @SuppressLint("HardwareIds")
                    String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    JSONParser.okhttpPost(Setting.SERVER_URL, methods.getAPIRequest(ItemName.METHOD_SINGLE_SONG,0,deviceId, Setting.arrayList_play.get(Setting.playPos).getId(),"","","","","","","","","","","","","", null));
                    getBitmapFromURL(Setting.arrayList_play.get(Setting.playPos).getImageSmall());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notification.setLargeIcon(bitmap);
                    } else {
                        bigViews.setImageViewBitmap(R.id.imageView_noti, bitmap);
                        smallViews.setImageViewBitmap(R.id.status_bar_album_art, bitmap);
                    }
                    mNotificationManager.notify(101, notification.build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void updateNoti() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setContentTitle(Setting.arrayList_play.get(Setting.playPos).getTitle());
            notification.setContentText(Setting.arrayList_play.get(Setting.playPos).getArtist());
        } else {
            bigViews.setTextViewText(R.id.textView_noti_name, Setting.arrayList_play.get(Setting.playPos).getTitle());
            bigViews.setTextViewText(R.id.textView_noti_artist, Setting.arrayList_play.get(Setting.playPos).getArtist());
            smallViews.setTextViewText(R.id.status_bar_artist_name, Setting.arrayList_play.get(Setting.playPos).getArtist());
            smallViews.setTextViewText(R.id.status_bar_track_name, Setting.arrayList_play.get(Setting.playPos).getTitle());
        }
        updateNotiImage();
        updateNotiPlay(exoPlayer.getPlayWhenReady());
    }

    @SuppressLint("RestrictedApi")
    private void updateNotiPlay(Boolean isPlay) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification.mActions.remove(1);
                Intent playIntent = new Intent(this, PlayerService.class);
                playIntent.setAction(ACTION_TOGGLE);
                PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (isPlay) {
                    notification.mActions.add(1, new NotificationCompat.Action(
                            R.drawable.ic_pause_white_24dp, "Pause",
                            ppreviousIntent));
                } else {
                    notification.mActions.add(1, new NotificationCompat.Action(
                            R.drawable.ic_play_arrow_white_24dp, "Play",
                            ppreviousIntent));
                }
            } else {
                if (isPlay) {
                    bigViews.setImageViewResource(R.id.imageView_noti_play, android.R.drawable.ic_media_pause);
                } else {
                    bigViews.setImageViewResource(R.id.imageView_noti_play, android.R.drawable.ic_media_play);
                }
            }
            mNotificationManager.notify(101, notification.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
            onCompletion();
        }
        if (playbackState == Player.STATE_READY && playWhenReady) {
            if (isNewSong) {
                isNewSong = false;
                Setting.isPlayed = true;
                setBuffer(false);
                try {
                    GlobalBus.getBus().postSticky(Setting.arrayList_play.get(Setting.playPos));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (notification == null) {
                    createNoti();
                } else {
                    updateNoti();
                }
            } else {
                updateNotiPlay(exoPlayer.getPlayWhenReady());
            }

            Log.e("aaaa",methods.milliSecondsToTimer(exoPlayer.getDuration()));
        }

        if(playWhenReady) {
            if(!mWakeLock.isHeld()) {
                mWakeLock.acquire(60000);
            }
        } else {
            if(mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        }
    }

    private void getBitmapFromURL(String src) {
        try {
            if (Setting.isOnline) {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } else {
                try {
                    if (Setting.isDownloaded) {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(new File(src)));
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), methods.getAlbumArtUri(Integer.parseInt(src)));
                    }
                } catch (Exception e) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tamilaudio2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        try {
            exoPlayer.setPlayWhenReady(false);
            setBuffer(false);
            changePlayPause(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    BroadcastReceiver onHeadPhoneDetect = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (exoPlayer.getPlayWhenReady()) {
                    togglePlay();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    };

    private AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) :
                    // Lower the volume while ducking.
                    exoPlayer.setVolume(0.2f);
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) :
                    if (Setting.isPlayed) {
                        Setting.isNot = true;
                        play_songs();
                    }
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS) :
                    if (Setting.isPlayed) {
                        Setting.isNot = true;
                        play_songs();
                    }
                    break;
                case (AudioManager.AUDIOFOCUS_GAIN) :
                    exoPlayer.setVolume(1f);
                    if (Setting.isNot) {
                        Setting.isNot = false;
                        play_songs();
                    }

                    break;
                default: break;
            }
        }
    };

    private void play_songs() {
        try {
            if (exoPlayer.getPlayWhenReady()) {
                togglePlay();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    public void onDestroy() {
        try {
            if(mWakeLock.isHeld()) {
                mWakeLock.release();
            }
            exoPlayer.stop();
            exoPlayer.release();
            try {
                mAudioManager.abandonAudioFocus(focusChangeListener);
                unregisterReceiver(onHeadPhoneDetect);
                mAudioManager.unregisterMediaButtonEventReceiver(componentName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onPrevButtonPress() {
        try {
            if (!Setting.isOnline || methods.isNetworkAvailable()) {
                previous();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNextButtonPress() {
        try {
            if (!Setting.isOnline || methods.isNetworkAvailable()) {
                next();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayPauseButtonPress() {
        try {
            togglePlay();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}