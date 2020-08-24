package thiva.tamilaudiopro.Utils;

import thiva.tamilaudiopro.Constant.Constant;

public class ApiResources {
   // public static String CURRENCY; // must be valid currency code
    //public static String EXCHSNGE_RATE;
    //public static String PAYPAL_CLIENT_ID;
   // public static String RAZORPAY_EXCHANGE_RATE;
    public static String USER_PHONE;


    String URL = Constant.Admin_Panel_URL;


    String searchUrl = URL+"search";

   String getAllReply = URL+"all_replay";
    //String termsURL = Constant.TERMS_URL;

//    public String getTermsURL() {
//        return termsURL;
//    }

    public String getGetAllReply() {
        return getAllReply;
    }
     public String getSearchUrl() {
        return searchUrl;
    }

}
