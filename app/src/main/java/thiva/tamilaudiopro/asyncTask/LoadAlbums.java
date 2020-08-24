package thiva.tamilaudiopro.asyncTask;

import android.os.AsyncTask;

import thiva.tamilaudiopro.Activity.BuildConfig;
import thiva.tamilaudiopro.Listener.AlbumsListener;
import thiva.tamilaudiopro.item.ItemAlbums;
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

public class LoadAlbums extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private AlbumsListener albumsListener;
    private ArrayList<ItemAlbums> arrayList = new ArrayList<>();
    private String verifyStatus = "0", message = "";

    public LoadAlbums(AlbumsListener albumsListener, RequestBody requestBody) {
        this.albumsListener = albumsListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        albumsListener.onStart();
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
                    String id = objJson.getString(ItemName.TAG_AID);
                    String name = objJson.getString(ItemName.TAG_ALBUM_NAME);
                    String image = objJson.getString(ItemName.TAG_ALBUM_IMAGE);
                    String thumb = objJson.getString(ItemName.TAG_ALBUM_THUMB);

                    ItemAlbums objItem = new ItemAlbums(id, name, image, thumb);
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
        albumsListener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }
}