package thiva.tamilaudiopro.asyncTask;

import android.os.AsyncTask;

import thiva.tamilaudiopro.Activity.BuildConfig;
import thiva.tamilaudiopro.Listener.ArtistListener;
import thiva.tamilaudiopro.item.ItemArtist;
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

public class LoadArtist extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private ArtistListener artistListener;
    private ArrayList<ItemArtist> arrayList = new ArrayList<>();
    private String verifyStatus = "0", message = "";

    public LoadArtist(ArtistListener artistListener, RequestBody requestBody) {
        this.artistListener = artistListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        artistListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);
            JSONObject mainJson = new JSONObject(json);
            JSONArray jsonArray = mainJson.getJSONArray(BuildConfig.API_KEY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);

                if (!objJson.has(ItemName.TAG_SUCCESS)) {
                    String id = objJson.getString(ItemName.TAG_ID);
                    String name = objJson.getString(ItemName.TAG_ARTIST_NAME);
                    String image = objJson.getString(ItemName.TAG_ARTIST_IMAGE);
                    String thumb = objJson.getString(ItemName.TAG_ARTIST_THUMB);

                    ItemArtist objItem = new ItemArtist(id, name, image, thumb);
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
        artistListener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }
}