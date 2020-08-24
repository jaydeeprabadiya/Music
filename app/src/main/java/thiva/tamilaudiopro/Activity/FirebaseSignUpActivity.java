package thiva.tamilaudiopro.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import thiva.tamilaudiopro.Constant.Constant;
import thiva.tamilaudiopro.Listener.FirebaseAuthApi;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.SharedPre.SharedPref;
import thiva.tamilaudiopro.Utils.ApiResources;
import thiva.tamilaudiopro.Utils.DBHelper;
import thiva.tamilaudiopro.Utils.RetrofitClient;
import thiva.tamilaudiopro.Utils.RtlUtils;
import thiva.tamilaudiopro.item.ItemUser;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.firebase.ui.auth.AuthUI.*;

public class FirebaseSignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    FirebaseAuth firebaseAuth;
    private static int RC_PHONE_SIGN_IN = 123;
    private static int RC_FACEBOOK_SIGN_IN = 124;
    private static int RC_GOOGLE_SIGN_IN = 125;
    private ProgressBar progressBar;
    private View backgroundView;
    private Button googleAuth, phoneAuth, emailAuth, facebookAuth;
    private DBHelper databaseHelper;
    SharedPref sharedPref;

    private static final int AUDIO_PERMISSION_REQUEST_CODE = 102;

    public static final String[] WRITE_EXTERNAL_STORAGE_PERMS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RtlUtils.setScreenDirection(this);
        SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
        boolean isDark = sharedPreferences.getBoolean("dark", false);

        if (isDark) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_sign_up);

        databaseHelper = new DBHelper(FirebaseSignUpActivity.this);
        sharedPref = new SharedPref(this);

        //---analytics-----------
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "sign_up_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        backgroundView = findViewById(R.id.background_view);
        googleAuth = findViewById(R.id.google_auth);
        phoneAuth = findViewById(R.id.phoneSignInBtn);
        emailAuth = findViewById(R.id.emailSignInBtn);
        facebookAuth = findViewById(R.id.facebookSignInBtn);
        if (Constant.ENABLE_FACEBOOK_LOGIN) {
            facebookAuth.setVisibility(View.VISIBLE);
        }
        if (Constant.ENABLE_GOOGLE_LOGIN) {
            googleAuth.setVisibility(View.VISIBLE);
        }
        if (Constant.ENABLE_PHONE_LOGIN) {
            phoneAuth.setVisibility(View.VISIBLE);
        }



            backgroundView.setBackgroundColor(getResources().getColor(R.color.nav_head_bg));


        progressBar = findViewById(R.id.phone_auth_progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();
        initialize();
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case AUDIO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
        }
    }

//      button_skip.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            openMainActivity();
//        }
//    });
    private void openMainActivity() {
        Intent intent = new Intent(FirebaseSignUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void signInWithPhone(View view) {
        phoneSignIn();
    }

    private void initialize() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(WRITE_EXTERNAL_STORAGE_PERMS, AUDIO_PERMISSION_REQUEST_CODE);
        } else {

        }
    }

    private void phoneSignIn() {
        progressBar.setVisibility(View.VISIBLE);
        if (firebaseAuth.getCurrentUser() != null) {
            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().isEmpty()) {
                final String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                //already signed in
                if (!phoneNumber.isEmpty())
                    sendDataToServer();
            }

        } else {
            progressBar.setVisibility(View.GONE);
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().build());
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_PHONE_SIGN_IN);
        }

    }

    private void facebookSignIn() {
        progressBar.setVisibility(View.VISIBLE);
        if (firebaseAuth.getCurrentUser() != null) {
            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().isEmpty()) {
                //already signed in
                //send data to server
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //sendFacebookDataToServer(user.getDisplayName(), user.getEmail());

            }

        } else {
            progressBar.setVisibility(View.GONE);
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.FacebookBuilder()
                            //.setPermissions(Arrays.asList("email", "default"))
                            .build());
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_FACEBOOK_SIGN_IN);
        }
    }

    private void sendDataToServer() {
        progressBar.setVisibility(View.VISIBLE);
        String phoneNo = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        FirebaseAuthApi api = retrofit.create(FirebaseAuthApi.class);
        Call<ItemUser> call = api.getPhoneAuthStatus(Constant.Admin_Panel_URL, uid, phoneNo);
        call.enqueue(new Callback<ItemUser>() {
            @Override
            public void onResponse(Call<ItemUser> call, Response<ItemUser> response) {
                if (response.code() == 200) {


                        ItemUser user = response.body();
                        DBHelper db = new DBHelper(FirebaseSignUpActivity.this);
                        db.deleteUserData();
                        db.insertUserData(user);
                        ApiResources.USER_PHONE = user.getMobile();

                        SharedPreferences.Editor preferences = getSharedPreferences(DBHelper.USER_LOGIN_STATUS, MODE_PRIVATE).edit();
                        preferences.putBoolean(DBHelper.USER_LOGIN_STATUS, true);
                        preferences.apply();
                        preferences.commit();


                }
            }

            @Override
            public void onFailure(Call<ItemUser> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                phoneSignIn();
            }
        });


    }

    private void sendFacebookDataToServer(String username, String email) {
        progressBar.setVisibility(View.VISIBLE);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        FirebaseAuthApi api = retrofit.create(FirebaseAuthApi.class);
        Call<ItemUser> call = api.getFacebookAuthStatus(Constant.Admin_Panel_URL, uid, username, email);
        call.enqueue(new Callback<ItemUser>() {
            @Override
            public void onResponse(Call<ItemUser> call, Response<ItemUser> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equals("success")) {

                        ItemUser user = response.body();
                        DBHelper db = new DBHelper(FirebaseSignUpActivity.this);
                       // db.deleteUserData();
                       // db.insertUserData(user);
                       // ApiResources.USER_PHONE = user.getMobile();

                        SharedPreferences.Editor preferences = getSharedPreferences(DBHelper.USER_LOGIN_STATUS, MODE_PRIVATE).edit();
                        preferences.putBoolean(DBHelper.USER_LOGIN_STATUS, true);
                        preferences.apply();
                        preferences.commit();


                    }

                }
            }

            @Override
            public void onFailure(Call<ItemUser> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                facebookSignIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHONE_SIGN_IN) {

            final IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (!user.getPhoneNumber().isEmpty()) {
                    sendDataToServer();
                } else {
                    //empty
                    phoneSignIn();
                }
            } else {
                // sign in failed
                if (response == null) {
                    //Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Error !!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        } else if (requestCode == RC_FACEBOOK_SIGN_IN) {
            final IdpResponse response = com.firebase.ui.auth.IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (!user.getUid().isEmpty()) {
                    String username = user.getDisplayName();
                   // String photoUrl = String.valueOf(user.getPhotoUrl());
                    String email = user.getEmail();

                    sendFacebookDataToServer(username, email);

                } else {
                    //empty
                    facebookSignIn();
                }
            } else {
                // sign in failed
                if (response == null) {
                    //Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Error !!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else if (requestCode == RC_GOOGLE_SIGN_IN) {
            final IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (!user.getUid().isEmpty()) {
                    sendGoogleDataToServer();

                } else {
                    //empty
                    googleSignIn();
                }
            } else {
                // sign in failed
                if (response == null) {
                    //Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Error !!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    public void signInWithEmail(View view) {
        startActivity(new Intent(FirebaseSignUpActivity.this, LoginActivity.class));
    }

    public void signInWithFacebook(View view) {
        facebookSignIn();
    }


    public void signInWithGoogle(View view) {
        googleSignIn();
    }

    private void googleSignIn() {
        progressBar.setVisibility(View.VISIBLE);
        if (firebaseAuth.getCurrentUser() != null) {
            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().isEmpty()) {
                sendGoogleDataToServer();
            }

        } else {
            progressBar.setVisibility(View.GONE);
            // Choose authentication providers
            List<IdpConfig> providers = Arrays.asList(
                    new IdpConfig.GoogleBuilder().build());
            // Create and launch sign-in intent
            startActivityForResult(
                    getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_GOOGLE_SIGN_IN);
        }
    }

    private void sendGoogleDataToServer() {
        progressBar.setVisibility(View.VISIBLE);
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
       // Uri image = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        FirebaseAuthApi api = retrofit.create(FirebaseAuthApi.class);
        Call<ItemUser> call = api.getGoogleAuthStatus(Constant.Admin_Panel_URL, uid, email, username);
        call.enqueue(new Callback<ItemUser>() {
            @Override
            public void onResponse(Call<ItemUser> call, Response<ItemUser> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equals("success")) {
                        ItemUser user = response.body();
                        DBHelper db = new DBHelper(FirebaseSignUpActivity.this);
                        db.deleteUserData();
                        db.insertUserData(user);
                       // ApiResources.USER_PHONE = user.getMobile();

                        SharedPreferences.Editor preferences = getSharedPreferences(DBHelper.USER_LOGIN_STATUS, MODE_PRIVATE).edit();
                        preferences.putBoolean(DBHelper.USER_LOGIN_STATUS, true);
                        preferences.apply();
                        preferences.commit();

                        //save user login time, expire time
                       // updateSubscriptionStatus(user.getUserId());
                    }

                }
            }

            @Override
            public void onFailure(Call<ItemUser> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                googleSignIn();
            }
        });

    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }
}
