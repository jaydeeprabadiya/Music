package thiva.tamilaudiopro.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import thiva.tamilaudiopro.Activity.BuildConfig;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.Listener.AboutListener;
import thiva.tamilaudiopro.item.ItemAbout;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.JSONParser.JSONParser;
import thiva.tamilaudiopro.item.ItemName;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class LoadAbout extends AsyncTask<String, String, String> {

    private Methods methods;
    private AboutListener aboutListener;
    private String message = "", verifyStatus = "0";

    public LoadAbout(Context context, AboutListener aboutListener) {
        this.aboutListener = aboutListener;
        methods = new Methods(context);
    }

    @Override
    protected void onPreExecute() {
        aboutListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Setting.SERVER_URL, methods.getAPIRequest(ItemName.METHOD_APP_DETAILS, 0, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", null));
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(BuildConfig.API_KEY)) {
                JSONArray jsonArray = jsonObject.getJSONArray(BuildConfig.API_KEY);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    if (!c.has(ItemName.TAG_SUCCESS)) {
                        String appname = c.getString("app_name");
                        String applogo = c.getString("app_logo");
                        String desc = c.getString("app_description");
                        String appversion = c.getString("app_version");
                        String appauthor = c.getString("app_author");
                        String appcontact = c.getString("app_contact");
                        String email = c.getString("app_email");
                        String website = c.getString("app_website");
                        String privacy = c.getString("app_privacy_policy");
                        String developedby = c.getString("app_developed_by");

                        Setting.ad_banner_id = c.getString("banner_ad_id");
                        Setting.ad_inter_id = c.getString("interstital_ad_id");
                        Setting.isAdmobBannerAd = Boolean.parseBoolean(c.getString("banner_ad"));
                        Setting.isAdmobInterAd = Boolean.parseBoolean(c.getString("interstital_ad"));
                        Setting.ad_publisher_id = c.getString("publisher_id");
                        if(!c.getString("interstital_ad_click").equals("")) {
                            Setting.adShow = Integer.parseInt(c.getString("interstital_ad_click"));
                        }

                        Setting.fb_ad_banner_id = c.getString("facebook_banner_ad_id");
                        Setting.fb_ad_inter_id = c.getString("facebook_interstital_ad_id");
                        Setting.isFBBannerAd = Boolean.parseBoolean(c.getString("facebook_banner_ad"));
                        Setting.isFBInterAd = Boolean.parseBoolean(c.getString("facebook_interstital_ad"));
                        if(!c.getString("facebook_interstital_ad_click").equals("")) {
                            Setting.adShowFB = Integer.parseInt(c.getString("facebook_interstital_ad_click"));
                        }

                        Setting.purchase_code = c.getString("purchase_code");
                        Setting.nemosofts_key = c.getString("nemosofts_key");

                        Setting.in_app = Boolean.parseBoolean(c.getString("in_app"));
                        Setting.SUBSCRIPTION_ID = c.getString("subscription_id");
                        Setting.MERCHANT_KEY =  c.getString("merchant_key");
                        if(!c.getString("subscription_days").equals("")) {
                            Setting.SUBSCRIPTION_DURATION = Integer.parseInt(c.getString("subscription_days"));
                        }

                        Setting.itemAbout = new ItemAbout(appname, applogo, desc, appversion, appauthor, appcontact, email, website, privacy, developedby);
                    } else {
                        verifyStatus = c.getString(ItemName.TAG_SUCCESS);
                        message = c.getString(ItemName.TAG_MSG);
                    }
                }
            }
            return "1";
        } catch (Exception ee) {
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        aboutListener.onEnd(s, verifyStatus, message);
        super.onPostExecute(s);
    }
}