package thiva.tamilaudiopro.Listener;

import thiva.tamilaudiopro.item.ItemSong;

import java.util.ArrayList;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public interface SongListener {
    void onStart();
    void onEnd(String s, String verifyStatus, String message, ArrayList<ItemSong> arrayList);
}
