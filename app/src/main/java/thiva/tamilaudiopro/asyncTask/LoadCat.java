package thiva.tamilaudiopro.asyncTask;

import android.os.AsyncTask;

import thiva.tamilaudiopro.Activity.BuildConfig;
import thiva.tamilaudiopro.Listener.CatListener;
import thiva.tamilaudiopro.item.ItemCat;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.JSONParser.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

public class LoadCat extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private CatListener catListener;
    private ArrayList<ItemCat> arrayList = new ArrayList<>();
    private String verifyStatus = "0", message = "";

    public LoadCat(CatListener catListener, RequestBody requestBody) {
        this.catListener = catListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        catListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);

            JSONObject mainJson = new JSONObject(json);
            JSONArray jsonArray = mainJson.getJSONArray(BuildConfig.API_KEY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                if (!obj.has(ItemName.TAG_SUCCESS)) {
                    String id = obj.getString(ItemName.TAG_CID);
                    String name = obj.getString(ItemName.TAG_CAT_NAME);
                    String image = obj.getString(ItemName.TAG_CAT_IMAGE);

                    ItemCat objItem = new ItemCat(id, name, image);
                    arrayList.add(objItem);
                } else {
                    verifyStatus = obj.getString(ItemName.TAG_SUCCESS);
                    message = obj.getString(ItemName.TAG_MSG);
                }
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        catListener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }
}