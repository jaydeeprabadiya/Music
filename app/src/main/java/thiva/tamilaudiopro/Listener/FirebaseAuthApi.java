package thiva.tamilaudiopro.Listener;

import android.net.Uri;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import thiva.tamilaudiopro.item.ItemUser;

public interface FirebaseAuthApi {
    @FormUrlEncoded
    @POST("firebase_auth")
    Call<ItemUser> getPhoneAuthStatus(@Header("API-KEY") String apiKey,
                                      @Field("uid") String uid,
                                      @Field("phone") String phoneNo);

    @FormUrlEncoded
    @POST("firebase_auth")
    Call<ItemUser> getGoogleAuthStatus(@Header("API-KEY") String apiKey,
                                   @Field("uid") String uid,
                                   @Field("email") String email,
                                   @Field("name") String name,
                                   @Field("image_url") Uri image);


    @FormUrlEncoded
    @POST("firebase_auth")
    Call<ItemUser> getFacebookAuthStatus(@Header("API-KEY") String apiKey,
                                     @Field("uid") String uid,
                                     @Field("name") String name,
                                     @Field("email") String email,
                                     @Field("image_url") Uri photoUrl);

    Call<ItemUser> getFacebookAuthStatus(String admin_panel_url, String uid, String username, String email);

    Call<ItemUser> getGoogleAuthStatus(String admin_panel_url, String uid, String email, String username);
}
