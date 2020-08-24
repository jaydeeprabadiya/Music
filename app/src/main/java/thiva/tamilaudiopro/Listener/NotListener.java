package thiva.tamilaudiopro.Listener;

import java.util.ArrayList;

import thiva.tamilaudiopro.item.ItemNotification;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public interface NotListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemNotification> arrayList);
}
