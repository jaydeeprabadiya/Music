package thiva.tamilaudiopro.asyncTask;

import android.os.AsyncTask;

import thiva.tamilaudiopro.Activity.BuildConfig;
import thiva.tamilaudiopro.Listener.ServerPlaylistListener;
import thiva.tamilaudiopro.item.ItemName;
import thiva.tamilaudiopro.item.ItemServerPlayList;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.JSONParser.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class LoadServerPlaylist extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private ServerPlaylistListener serverPlaylistListener;
    private ArrayList<ItemServerPlayList> arrayList = new ArrayList<>();
    private String verifyStatus = "0", message = "";

    public LoadServerPlaylist(ServerPlaylistListener serverPlaylistListener, RequestBody requestBody) {
        this.serverPlaylistListener = serverPlaylistListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        serverPlaylistListener.onStart();
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
                    String id = objJson.getString(ItemName.TAG_PID);
                    String name = objJson.getString(ItemName.TAG_PLAYLIST_NAME);
                    String image = objJson.getString(ItemName.TAG_PLAYLIST_IMAGE);
                    String thumb = objJson.getString(ItemName.TAG_PLAYLIST_THUMB);

                    ItemServerPlayList objItem = new ItemServerPlayList(id, name, image, thumb);
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
        serverPlaylistListener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }
}