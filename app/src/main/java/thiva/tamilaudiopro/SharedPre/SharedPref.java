package thiva.tamilaudiopro.SharedPre;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.Receiver.ItemNemosofts;
import thiva.tamilaudiopro.item.ItemUser;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class SharedPref {
    private Methods methods;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String TAG_UID = "uid" ,TAG_USERNAME = "name", TAG_EMAIL = "email", TAG_MOBILE = "mobile", TAG_REMEMBER = "rem",
            TAG_PASSWORD = "pass", SHARED_PREF_AUTOLOGIN = "autologin";

    @SuppressLint("CommitPrefEdits")
    public SharedPref(Context context) {
        methods = new Methods(context, false);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("setting_mp3", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setIsFirst(Boolean flag) {
        editor.putBoolean("firstopen_new", flag);
        editor.apply();
    }

    public Boolean getIsFirst() {
        return sharedPreferences.getBoolean("firstopen_new", true);
    }

    public void setIsFirstPurchaseCode(Boolean flag) {
        editor.putBoolean("firstopenpurchasecode", flag);
        editor.apply();
    }

    public Boolean getIsFirstPurchaseCode() {
        return sharedPreferences.getBoolean("firstopenpurchasecode", true);
    }

    public void setPurchaseCode(ItemNemosofts itemAbout) {
        editor.putString("purchase_code", itemAbout.getPurchase_code());
        editor.putString("product_name", itemAbout.getProduct_name());
        editor.putString("purchase_date", itemAbout.getPurchase_date());
        editor.putString("buyer_name", itemAbout.getBuyer_name());
        editor.putString("license_type", itemAbout.getLicense_type());
        editor.putString("nemosofts_key", itemAbout.getNemosofts_key());
        editor.putString("package_name", itemAbout.getPackage_name());
        editor.apply();
    }

    public void getPurchaseCode() {
        Setting.itemNemosofts = new ItemNemosofts(
                sharedPreferences.getString("purchase_code",""),
                sharedPreferences.getString("product_name",""),
                sharedPreferences.getString("purchase_date",""),
                sharedPreferences.getString("buyer_name",""),
                sharedPreferences.getString("license_type",""),
                sharedPreferences.getString("nemosofts_key",""),
                sharedPreferences.getString("package_name","")
        );
    }

    public void setLoginDetails(ItemUser itemUser, Boolean isRemember, String password) {
        editor.putBoolean(TAG_REMEMBER, isRemember);
        editor.putString(TAG_UID, methods.encrypt(itemUser.getId()));
        editor.putString(TAG_USERNAME, methods.encrypt(itemUser.getName()));
        editor.putString(TAG_MOBILE, methods.encrypt(itemUser.getMobile()));
        editor.putString(TAG_EMAIL, methods.encrypt(itemUser.getEmail()));
        editor.putBoolean(TAG_REMEMBER, isRemember);
        editor.putString(TAG_PASSWORD, methods.encrypt(password));
        editor.apply();
    }

    public void setRemeber(Boolean isRemember) {
        editor.putBoolean(TAG_REMEMBER, isRemember);
        editor.putString(TAG_PASSWORD, "");
        editor.apply();
    }

    public void getUserDetails() {
        Setting.itemUser = new ItemUser(methods.decrypt(sharedPreferences.getString(TAG_UID,"")), methods.decrypt(sharedPreferences.getString(TAG_USERNAME,"")), methods.decrypt(sharedPreferences.getString(TAG_EMAIL,"")), methods.decrypt(sharedPreferences.getString(TAG_MOBILE,"")));
    }

    public String getEmail() {
        return methods.decrypt(sharedPreferences.getString(TAG_EMAIL,""));
    }

    public String getPassword() {
        return methods.decrypt(sharedPreferences.getString(TAG_PASSWORD,""));
    }

    public Boolean isRemember() {
        return sharedPreferences.getBoolean(TAG_REMEMBER, false);
    }

    public Boolean getIsNotification() {
        return sharedPreferences.getBoolean("noti", true);
    }

    public void setIsNotification(Boolean isNotification) {
        editor.putBoolean("noti", isNotification);
        editor.apply();
    }

    public Boolean getIsAutoLogin() {
        return sharedPreferences.getBoolean(SHARED_PREF_AUTOLOGIN, false);
    }

    public void setIsAutoLogin(Boolean isAutoLogin) {
        editor.putBoolean(SHARED_PREF_AUTOLOGIN, isAutoLogin);
        editor.apply();
    }

    public Boolean getIsRemember() {
        return sharedPreferences.getBoolean(TAG_REMEMBER, false);
    }

    public Boolean getNightMode() {
        return sharedPreferences.getBoolean("night_mode", false);
    }

    public void setNightMode(Boolean state) {
        editor.putBoolean("night_mode", state);
        editor.apply();
    }

    public Boolean getStatusBar() {
        return sharedPreferences.getBoolean("statusBar", false);
    }

    public void setStatusBar(Boolean state) {
        editor.putBoolean("statusBar", state);
        editor.apply();
    }

    public Boolean getSongsColor() {
        return sharedPreferences.getBoolean("songscolor", true);
    }

    public void setSongsColor(Boolean state) {
        editor.putBoolean("songscolor", state);
        editor.apply();
    }

    public Boolean getAlbumColor() {
        return sharedPreferences.getBoolean("albumcolor", false);
    }

    public void setAlbumColor(Boolean state) {
        editor.putBoolean("albumcolor", state);
        editor.apply();
    }

    public Boolean getBottomNavigationMenu() {
        return sharedPreferences.getBoolean("bottomnavigationmenu", true);
    }

    public void setBottomNavigationMenu(Boolean state) {
        editor.putBoolean("bottomnavigationmenu", state);
        editor.apply();
    }

    public Boolean getToolbar_Color() {
        return sharedPreferences.getBoolean("toolbar_color", false);
    }

    public void setToolbar_Color(Boolean state) {
        editor.putBoolean("toolbar_color", state);
        editor.apply();
    }

    public int getAlbum_grid() {
        return sharedPreferences.getInt("album_grid", 2);
    }

    public void setAlbum_grid(int state) {
        editor.putInt("album_grid", state);
        editor.apply();
    }

    public int getColor_my() {
        return sharedPreferences.getInt("color_my", 2);
    }

    public void setColor_my(int state) {
        editor.putInt("color_my", state);
        editor.apply();
    }

    public void setPurchase() {
        editor.putBoolean("in_app", Setting.in_app);
        editor.putString("subscription_id", methods.encrypt(Setting.SUBSCRIPTION_ID));
        editor.putString("merchant_key", methods.encrypt(Setting.MERCHANT_KEY));
        editor.putInt("sub_dur", Setting.SUBSCRIPTION_DURATION);
        editor.apply();
    }

    public void getPurchase() {
       Setting.in_app = sharedPreferences.getBoolean("in_app", true);
       Setting.SUBSCRIPTION_ID = methods.decrypt(sharedPreferences.getString("subscription_id","SUBSCRIPTION_ID"));
       Setting.MERCHANT_KEY = methods.decrypt(sharedPreferences.getString("merchant_key","MERCHANT_KEY"));
       Setting.SUBSCRIPTION_DURATION = sharedPreferences.getInt("sub_dur",30);
    }

    public void setAds() {
        editor.putBoolean("isAdmobBannerAd", Setting.isAdmobBannerAd);
        editor.putBoolean("isAdmobInterAd", Setting.isAdmobInterAd);
        editor.putString("ad_publisher_id", methods.encrypt(Setting.ad_publisher_id));
        editor.putString("ad_banner_id", methods.encrypt(Setting.ad_banner_id));
        editor.putString("ad_inter_id", methods.encrypt(Setting.ad_inter_id));
        editor.putInt("adShow", Setting.adShow);

        editor.putBoolean("isFBBannerAd", Setting.isFBBannerAd);
        editor.putBoolean("isFBInterAd", Setting.isFBInterAd);
        editor.putString("fb_ad_banner_id", methods.encrypt(Setting.fb_ad_banner_id));
        editor.putString("fb_ad_inter_id", methods.encrypt(Setting.fb_ad_inter_id));
        editor.putInt("adShowFB", Setting.adShowFB);

        editor.apply();
    }

    public void getAds() {
        Setting.isAdmobBannerAd =sharedPreferences.getBoolean("isAdmobBannerAd", false);
        Setting.isAdmobInterAd = sharedPreferences.getBoolean("isAdmobInterAd", false);
        Setting.ad_publisher_id = methods.decrypt(sharedPreferences.getString("ad_publisher_id",""));
        Setting.ad_banner_id = methods.decrypt(sharedPreferences.getString("ad_banner_id",""));
        Setting.ad_inter_id = methods.decrypt(sharedPreferences.getString("ad_inter_id",""));
        Setting.adShow = sharedPreferences.getInt("adShow",10);

        Setting.isFBBannerAd = sharedPreferences.getBoolean("isFBBannerAd", false);
        Setting.isFBInterAd = sharedPreferences.getBoolean("isFBInterAd", false);
        Setting.fb_ad_banner_id = methods.decrypt(sharedPreferences.getString("fb_ad_banner_id",""));
        Setting.fb_ad_inter_id = methods.decrypt(sharedPreferences.getString("fb_ad_inter_id",""));
        Setting.adShowFB = sharedPreferences.getInt("adShowFB",10);
    }
}
