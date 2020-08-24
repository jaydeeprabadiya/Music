package thiva.tamilaudiopro.asyncTask;

import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import thiva.tamilaudiopro.Activity.BuildConfig;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.JSONParser.JSONParser;
import thiva.tamilaudiopro.Listener.SongListener;
import thiva.tamilaudiopro.item.ItemName;
import thiva.tamilaudiopro.item.ItemSong;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class LoadSong extends AsyncTask<String, String, String> {

    private SongListener songListener;
    private RequestBody requestBody;
    private ArrayList<ItemSong> arrayList = new ArrayList<>();
    private String verifyStatus = "0", message = "";

    public LoadSong(SongListener songListener, RequestBody requestBody) {
        this.songListener = songListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        songListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);

            JSONObject mainJson = new JSONObject(json);
            JSONArray jsonArray = mainJson.getJSONArray(BuildConfig.API_KEY);

            if (jsonArray.length() > 0 && jsonArray.getJSONObject(0).has("songs_list")) {
                jsonArray = jsonArray.getJSONObject(0).getJSONArray("songs_list");
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);

                if (!objJson.has(ItemName.TAG_SUCCESS)) {
                    String id = objJson.getString(ItemName.TAG_ID);
                    String cid = objJson.getString(ItemName.TAG_CAT_ID);
                    String cname = objJson.getString(ItemName.TAG_CAT_NAME);
                    String artist = objJson.getString(ItemName.TAG_ARTIST);
                    String name = objJson.getString(ItemName.TAG_SONG_NAME);
                    String url = objJson.getString(ItemName.TAG_MP3_URL);
                    String desc = objJson.getString(ItemName.TAG_DESC);
                    String duration = objJson.getString(ItemName.TAG_DURATION);
                    String thumb = objJson.getString(ItemName.TAG_THUMB_B).replace(" ", "%20");
                    String thumb_small = objJson.getString(ItemName.TAG_THUMB_S).replace(" ", "%20");
                    String total_rate = objJson.getString(ItemName.TAG_TOTAL_RATE);
                    String avg_rate = objJson.getString(ItemName.TAG_AVG_RATE);
                    String views = objJson.getString(ItemName.TAG_VIEWS);
                    String downloads = objJson.getString(ItemName.TAG_DOWNLOADS);

                    ItemSong objItem = new ItemSong(id, cid, cname, artist, url, thumb, thumb_small, name, duration, desc, total_rate, avg_rate, views, downloads);
                    arrayList.add(objItem);
                } else {
                    verifyStatus = objJson.getString(ItemName.TAG_SUCCESS);
                    message = objJson.getString(ItemName.TAG_MSG);
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
        songListener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }
}