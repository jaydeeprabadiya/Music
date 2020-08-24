package thiva.tamilaudiopro.item;

import java.io.Serializable;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ItemNotification implements Serializable {

    private String nid, notification_msg, notification_title, update_by;

    public ItemNotification(String nid, String notification_msg, String notification_title, String update_by) {
        this.nid = nid;
        this.notification_msg = notification_msg;
        this.notification_title = notification_title;
        this.update_by = update_by;
    }

    public String getNid() {
        return nid;
    }

    public String getNotification_msg() {
        return notification_msg;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public String getUpdate_by() {
        return update_by;
    }
}