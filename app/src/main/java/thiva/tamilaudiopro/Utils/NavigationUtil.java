package thiva.tamilaudiopro.Utils;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import thiva.tamilaudiopro.Activity.AboutActivity;
import thiva.tamilaudiopro.Activity.FirebaseSignUpActivity;
import thiva.tamilaudiopro.Activity.LoginActivity;
import thiva.tamilaudiopro.Activity.EqualizerActivity;
import thiva.tamilaudiopro.Activity.MainActivity;
import thiva.tamilaudiopro.Activity.NotificationActivity;
import thiva.tamilaudiopro.Activity.OfflineMusicActivity;
import thiva.tamilaudiopro.Activity.PrivacyActivity;
import thiva.tamilaudiopro.Activity.ProfileActivity;
import thiva.tamilaudiopro.Activity.ReportActivity;
import thiva.tamilaudiopro.Activity.SelectSongActivity;
import thiva.tamilaudiopro.Activity.SongByCatActivity;
import thiva.tamilaudiopro.Activity.SongByMyPlaylistActivity;
import thiva.tamilaudiopro.Activity.SongByOFFPlaylistActivity;
import thiva.tamilaudiopro.Activity.SongByOfflineActivity;
import thiva.tamilaudiopro.Activity.SongByServerPlaylistActivity;
import thiva.tamilaudiopro.Activity.SuggestionActivity;
import thiva.tamilaudiopro.Constant.Constant;
import thiva.tamilaudiopro.item.ItemMyPlayList;
import thiva.tamilaudiopro.item.ItemServerPlayList;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class NavigationUtil {

    public static void MainActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, MainActivity.class), null);
        activity.finish();
    }

    public static void OfflineMusicActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, OfflineMusicActivity.class), null);
    }

    public static void SuggestionActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, SuggestionActivity.class), null);
    }

    public static void NotificationActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, NotificationActivity.class), null);
    }

    public static void ProfileActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, ProfileActivity.class), null);
    }
    public static void ReportActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, ReportActivity.class), null);
    }

    public static void SelectSongActivity(@NonNull Activity activity, String pid, String type) {
        Intent intent = new Intent(activity, SelectSongActivity.class);
        intent.putExtra("pid",pid);
        intent.putExtra("type",type);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void SelectSongActivity_play_id(@NonNull Activity activity, String pid, String type, String play_id) {
        Intent intent = new Intent(activity, SelectSongActivity.class);
        intent.putExtra("pid",pid);
        intent.putExtra("type",type);
        intent.putExtra("play_id",play_id);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void Equalizer(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, EqualizerActivity.class), null);
    }

    public static void SongByCatActivity(@NonNull Activity activity, String type, String id, String name, String image) {
        Intent intent = new Intent(activity, SongByCatActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("img", image);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void SongByPlaylistActivity(@NonNull Activity activity, String type, String id, String name, String image) {
        Intent intent = new Intent(activity, SongByCatActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("img", image);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void SongByOFFPlaylistActivity(@NonNull Activity activity, ItemMyPlayList item) {
        Intent intent = new Intent(activity, SongByOFFPlaylistActivity.class);
        intent.putExtra("item", item);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void SongByOfflineActivity(@NonNull Activity activity, String type, String id, String name) {
        Intent intent = new Intent(activity, SongByOfflineActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void SongByServerPlaylistActivity(@NonNull Activity activity, String type, ItemServerPlayList item) {
        Intent intent = new Intent(activity, SongByServerPlaylistActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("item", item);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void SongByMyPlaylistActivity(@NonNull Activity activity, ItemMyPlayList itemMyPlayList) {
        Intent intent = new Intent(activity, SongByMyPlaylistActivity.class);
        intent.putExtra("item", itemMyPlayList);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void AboutActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, AboutActivity.class), null);
    }

    public static void PrivacyActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, PrivacyActivity.class), null);
    }

    public static void LoginActivity(@NonNull Activity activity, String from) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from", from);
        ActivityCompat.startActivity(activity,intent, null);
        activity.finish();
    }

    public static void FirebaseSignUpActivity(@NonNull Activity activity, String from) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from", from);
        ActivityCompat.startActivity(activity,intent, null);
        activity.finish();
    }

    public static void SongByCatActivitySplash(@NonNull Activity activity, String type, String id, String pushName) {
        Intent intent = new Intent(activity, SongByCatActivity.class);
        intent.putExtra("isPush", true);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        intent.putExtra("name", pushName);
        intent.putExtra("img", Constant.Admin_Panel_URL+"assets/images/Categories.png");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityCompat.startActivity(activity,intent, null);
        activity.finish();
    }
}
