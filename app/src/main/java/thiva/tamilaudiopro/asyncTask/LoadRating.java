package thiva.tamilaudiopro.asyncTask;

import android.os.AsyncTask;

import thiva.tamilaudiopro.Activity.BuildConfig;
import thiva.tamilaudiopro.Listener.RatingListener;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.JSONParser.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.RequestBody;
import thiva.tamilaudiopro.item.ItemName;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class LoadRating extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private String msg = "", isRateSuccess = "0", rate = "0";
    private RatingListener ratingListener;

    public LoadRating(RatingListener ratingListener, RequestBody requestBody) {
        this.ratingListener = ratingListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        ratingListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);

        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(BuildConfig.API_KEY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                isRateSuccess = c.getString(ItemName.TAG_SUCCESS);
                msg = c.getString(ItemName.TAG_MSG);
                if (c.has("rate_avg")) {
                    rate = c.getString("rate_avg");
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
        ratingListener.onEnd(String.valueOf(s), isRateSuccess, msg, Integer.parseInt(rate));
        super.onPostExecute(s);
    }
}