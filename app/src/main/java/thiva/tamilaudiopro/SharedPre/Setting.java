package thiva.tamilaudiopro.SharedPre;

import android.annotation.SuppressLint;
import android.content.Context;

import thiva.tamilaudiopro.Constant.Constant;
import thiva.tamilaudiopro.Receiver.ItemNemosofts;
import thiva.tamilaudiopro.item.ItemAbout;
import thiva.tamilaudiopro.item.ItemAlbums;
import thiva.tamilaudiopro.item.ItemArtist;
import thiva.tamilaudiopro.item.ItemHomeBanner;
import thiva.tamilaudiopro.item.ItemServerPlayList;
import thiva.tamilaudiopro.item.ItemSong;
import thiva.tamilaudiopro.item.ItemUser;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String purchase_code = "";
    public static String nemosofts_key = "";
    public static ItemNemosofts itemNemosofts;

    public static final String Api = "speed_api.php";
    public static String SERVER_URL = Constant.Admin_Panel_URL + Api;

    public static ItemAbout itemAbout;
    public static ItemUser itemUser;
    public static Boolean isLogged = false;
    public static int playPos = 0;
    public static Boolean isNewAdded = true;
    public static String addedFrom = "";
    public static ArrayList<ItemSong> arrayList_play = new ArrayList<>();
    public static ArrayList<ItemSong> arrayListOfflineSongs = new ArrayList<>();
    public static ArrayList<ItemAlbums> arrayListOfflineAlbums = new ArrayList<>();
    public static ArrayList<ItemArtist> arrayListOfflineArtist = new ArrayList<>();
    public static Boolean isRepeat = false, isSuffle = false,
            isPlayed = false, isFromNoti = false, isFromPush = false, isAppOpen = false, isOnline = true,
            isDownloaded = false, isUpdate = false, Home = false, isNot = false, isDownload = true;
    public static Boolean getPurchases = false;

    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static String recentLimit = "30";
    public static String  pushSID = "0", pushCID = "0", pushCName = "", pushArtID = "0", pushArtNAME = "", pushAlbID = "0", pushAlbNAME = "", search_item = "";
    public static Boolean   isAdmobBannerAd = true, isAdmobInterAd = true, isFBBannerAd = true, isFBInterAd = true;
    public static String ad_publisher_id = "";
    public static String ad_banner_id = "", ad_inter_id = "", fb_ad_banner_id = "", fb_ad_inter_id = "";
    public static int adShow = 10;
    public static int adShowFB = 10;
    public static int adCount = 0;

    public static Boolean isLoginOn = true;

    public static ArrayList<ItemSong> arrayList_Home = new ArrayList<>();
    public static ArrayList<ItemHomeBanner> arrayList_Home_Pas = new ArrayList<>();
    public static ArrayList<ItemSong> arrayList_Trending = new ArrayList<>();
    public static ArrayList<ItemAlbums> arrayList_Home_album_1 = new ArrayList<>();
    public static ArrayList<ItemAlbums> arrayList_Home_album_2 = new ArrayList<>();
    public static ArrayList<ItemServerPlayList> arrayList_Home_album_3 = new ArrayList<>();
    public static ArrayList<ItemArtist> arrayList_Art = new ArrayList<>();

    public static boolean ad_arrayList = false;
    public static boolean Lodeing_Color = false;
    public static int Now_Play = 0;
    public static int Album = 0;
    public static int get_color_my = 0;
    public static boolean Blor_image = false;
    public static boolean Blor_image_Color = false;
    public static boolean bottomnavigationmenu = true;
    public static boolean songs_color = true;
    public static boolean album_color = false;
    public static boolean Dark_Mode = false;
    public static boolean StatusBar = false;
    public static boolean ToolBar_Color = false;
    public static boolean ToolBar_Color2 = false;
    public static boolean ToolBar_Color3 = false;

    public static Boolean in_app = true;
    public static String SUBSCRIPTION_ID = "SUBSCRIPTION_ID";
    public static String MERCHANT_KEY = "MERCHANT_KEY" ; // PUT YOUR MERCHANT KEY HERE;
    public static int SUBSCRIPTION_DURATION = 30; // PUT SUBSCRIPTION DURATION DAYS HERE;
}