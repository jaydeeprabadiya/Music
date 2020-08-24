package thiva.tamilaudiopro.asyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import thiva.tamilaudiopro.Activity.BuildConfig;
import thiva.tamilaudiopro.JSONParser.JSONParser;
import thiva.tamilaudiopro.Listener.NotListener;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.item.ItemNotification;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class LoadNot extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private NotListener notListener;
    private ArrayList<ItemNotification> arrayList = new ArrayList<>();

    public LoadNot(NotListener notListener, RequestBody requestBody) {
        this.notListener = notListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        notListener.onStart();
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

                String id = obj.getString("nid");
                String notification_msg = obj.getString("notification_msg");
                String notification_title = obj.getString("notification_title");
                String update_by = obj.getString("update_by");

                ItemNotification objItem = new ItemNotification(id, notification_msg, notification_title, update_by);
                arrayList.add(objItem);
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        notListener.onEnd(s,arrayList);
        super.onPostExecute(s);
    }
}