package thiva.tamilaudiopro.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import thiva.tamilaudiopro.NowPlayingScreen.NowPlayingScreen;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public final class PreferenceUtil {

    public static final String NOW_PLAYING_SCREEN_ID = "now_playing_screen_id";
    public static final String CAROUSEL_EFFECT = "carousel_effect";
    public static final String CIRCULAR_ALBUM_ART = "circular_album_art";
    private static PreferenceUtil sInstance;
    private final SharedPreferences mPreferences;

    private PreferenceUtil(@NonNull final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtil getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceUtil(context.getApplicationContext());
        }
        return sInstance;
    }

    public final NowPlayingScreen getNowPlayingScreen() {
        int id = mPreferences.getInt(NOW_PLAYING_SCREEN_ID, 0);
        for (NowPlayingScreen nowPlayingScreen : NowPlayingScreen.values()) {
            if (nowPlayingScreen.id == id) {
                return nowPlayingScreen;
            }
        }
        return NowPlayingScreen.NORMAL;
    }

    @SuppressLint("CommitPrefEdits")
    public void setNowPlayingScreen(NowPlayingScreen nowPlayingScreen) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(NOW_PLAYING_SCREEN_ID, nowPlayingScreen.id);
        editor.apply();
    }

    public void resetCarouselEffect() {
        mPreferences.edit().putBoolean(CAROUSEL_EFFECT, false).apply();
    }

    public void resetCircularAlbumArt() {
        mPreferences.edit().putBoolean(CIRCULAR_ALBUM_ART, false).apply();
    }
}
