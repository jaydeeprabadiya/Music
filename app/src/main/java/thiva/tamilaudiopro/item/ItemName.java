
package thiva.tamilaudiopro.item;

import java.io.Serializable;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ItemName implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String METHOD_LOGIN = "user_login";
    public static final String METHOD_REGISTER = "user_register";
    public static final String METHOD_FORGOT_PASSWORD = "forgot_pass";
    public static final String METHOD_PROFILE = "user_profile";
    public static final String METHOD_PROFILE_EDIT = "user_profile_update";
    public static final String METHOD_SINGLE_SONG = "single_song";
    public static final String METHOD_LATEST = "latest";
    public static final String METHOD_SEARCH = "song_search";
    public static final String METHOD_ALL_SONGS = "all_songs";
    public static final String METHOD_SONG_BY_CAT = "cat_songs";
    public static final String METHOD_SONG_BY_ALBUMS = "album_songs";
    public static final String METHOD_SONG_BY_ARTIST = "artist_name_songs";
    public static final String METHOD_SONG_BY_PLAYLIST = "playlist_songs";
    public static final String METHOD_ALBUMS = "album_list";
    public static final String METHOD_ALBUMS_BY_ARTIST = "artist_album_list";
    public static final String METHOD_HOME = "home";
    public static final String METHOD_ARTIST = "artist_list";
    public static final String METHOD_CAT = "cat_list";
    public static final String METHOD_APP_DETAILS = "app_details";
    public static final String METHOD_RATINGS = "song_rating";
    public static final String METHOD_SERVER_PLAYLIST = "playlist";
    public static final String METHOD_REPORT = "song_report";
    public static final String METHOD_SUGGESTION = "song_suggest";
    public static final String METHOD_NOTIFICATION = "user_notification";
    public static final String METHOD_MY_SEARCH = "song_search_my";

    public static final String TAG_ROOT = "nemosofts";

    public static final String METHOD_ALBUM_CAT_ID = "cat_id_by_album";
    public static final String METHOD_ALBUM_HOME = "album_list_home";
    public static final String METHOD_Playlist_HOME = "play_list_home";
    public static final String METHOD_SEARCH_MY = "search_text_my";

    public static final String METHOD_DOWNLOAD_COUNT = "song_download";

    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MSG = "msg";

    public static final String TAG_ID = "id";
    public static final String TAG_CAT_ID = "cat_id";
    public static final String TAG_CAT_NAME = "category_name";
    public static final String TAG_CAT_IMAGE = "category_image";
    public static final String TAG_MP3_URL = "mp3_url";
    public static final String TAG_DURATION = "mp3_duration";
    public static final String TAG_SONG_NAME = "mp3_title";
    public static final String TAG_DESC = "mp3_description";
    public static final String TAG_THUMB_B = "mp3_thumbnail_b";
    public static final String TAG_THUMB_S = "mp3_thumbnail_s";
    public static final String TAG_ARTIST = "mp3_artist";
    public static final String TAG_TOTAL_RATE = "total_rate";
    public static final String TAG_AVG_RATE = "rate_avg";
    public static final String TAG_USER_RATE = "user_rate";
    public static final String TAG_VIEWS = "total_views";
    public static final String TAG_DOWNLOADS = "total_download";

    public static final String TAG_CID = "cid";
    public static final String TAG_AID = "aid";
    public static final String TAG_PID = "pid";
    public static final String TAG_BID = "bid";

    public static final String TAG_BANNER_TITLE = "banner_title";
    public static final String TAG_BANNER_DESC = "banner_sort_info";
    public static final String TAG_BANNER_IMAGE = "banner_image";
    public static final String TAG_BANNER_TOTAL = "total_songs";

    public static final String TAG_ARTIST_NAME = "artist_name";
    public static final String TAG_ARTIST_IMAGE = "artist_image";
    public static final String TAG_ARTIST_THUMB = "artist_image_thumb";

    public static final String TAG_ALBUM_NAME = "album_name";
    public static final String TAG_ALBUM_IMAGE = "album_image";
    public static final String TAG_ALBUM_THUMB = "album_image_thumb";

    public static final String TAG_PLAYLIST_NAME = "playlist_name";
    public static final String TAG_PLAYLIST_IMAGE = "playlist_image";
    public static final String TAG_PLAYLIST_THUMB = "playlist_image_thumb";
}
