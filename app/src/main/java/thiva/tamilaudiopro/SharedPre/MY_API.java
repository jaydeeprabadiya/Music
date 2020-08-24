package thiva.tamilaudiopro.SharedPre;

import android.app.Activity;
import android.util.Base64;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import thiva.tamilaudiopro.Activity.BuildConfig;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class MY_API {
    @Expose
    @SerializedName("sign")
    private String sign;
    @Expose
    @SerializedName("salt")
    private String salt;

    @SerializedName("package_name")
    private String package_name;

    public MY_API() {
        String apiKey = BuildConfig.API_KEY;
        salt = "" + getRandomSalt();
        sign = md5(apiKey + salt);
    }

    public MY_API(Activity activity) {
        package_name = activity.getApplication().getPackageName();
    }

    private int getRandomSalt() {
        Random random = new Random();
        return random.nextInt(900);
    }

    public static String md5(String input) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(String.format("%02x", messageDigest[i]));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toBase64(String input) {
        byte[] encodeValue = Base64.encode(input.getBytes(), Base64.DEFAULT);
        return new String(encodeValue);
    }
}