package thiva.tamilaudiopro.Listener;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public interface SuccessListener {
    void onStart();
    void onEnd(String success, String registerSuccess, String message);
}