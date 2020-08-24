package thiva.tamilaudiopro.item;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class MessageEvent {
    public String message;
    public Boolean flag;

    public MessageEvent(Boolean flag, String message) {
        this.message = message;
        this.flag = flag;
    }
}