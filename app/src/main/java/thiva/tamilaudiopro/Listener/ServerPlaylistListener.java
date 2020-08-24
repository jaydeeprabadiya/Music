package thiva.tamilaudiopro.Listener;

import thiva.tamilaudiopro.item.ItemServerPlayList;

import java.util.ArrayList;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public interface ServerPlaylistListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemServerPlayList> arrayList);
}