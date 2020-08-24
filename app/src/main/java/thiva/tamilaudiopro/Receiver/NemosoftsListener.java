package thiva.tamilaudiopro.Receiver;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */
public interface NemosoftsListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message);
}