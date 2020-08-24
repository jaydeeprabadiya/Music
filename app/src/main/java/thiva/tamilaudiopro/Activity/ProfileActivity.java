package thiva.tamilaudiopro.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.asyncTask.LoadProfile;
import thiva.tamilaudiopro.Listener.SuccessListener;
import thiva.tamilaudiopro.item.ItemName;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ProfileActivity extends AppCompatActivity {

    Methods methods;
    Toolbar toolbar;
    TextView textView_name, textView_email, textView_mobile, textView_notlog;
    LinearLayout ll_mobile;
    View view_phone;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode) {
            setTheme(R.style.AppTheme3);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        if (Setting.StatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        methods = new Methods(this);
        methods.setStatusColor2(getWindow());


        toolbar = findViewById(R.id.toolbar_pro);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        AppCompatButton button_update = findViewById(R.id.prof_edit);

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_sugg = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                startActivity(intent_sugg);
            }
        });

        textView_name = findViewById(R.id.tv_prof_fname);
        textView_email = findViewById(R.id.tv_prof_email);
        textView_mobile = findViewById(R.id.tv_prof_mobile);
        textView_notlog = findViewById(R.id.textView_notlog);

        ll_mobile = findViewById(R.id.ll_prof_phone);

        view_phone = findViewById(R.id.view_prof_phone);

        LinearLayout ll_adView = findViewById(R.id.ll_adView);
        methods.showBannerAd(ll_adView);

        if (Setting.itemUser != null && !Setting.itemUser.getId().equals("")) {
            loadUserProfile();
        } else {
            setEmpty(true, getString(R.string.not_log));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);

        if (Setting.itemUser != null && !Setting.itemUser.getId().equals("")) {
            menu.findItem(R.id.item_profile_edit).setVisible(true);
        } else {
            menu.findItem(R.id.item_profile_edit).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_profile_edit:
                if (Setting.itemUser != null && !Setting.itemUser.getId().equals("")) {
                    Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProfileActivity.this, getString(R.string.not_log), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUserProfile() {
        if (methods.isNetworkAvailable()) {
            LoadProfile loadProfile = new LoadProfile(new SuccessListener() {
                @Override
                public void onStart() {
                    progressDialog.show();
                }

                @Override
                public void onEnd(String success, String registerSuccess, String message) {
                    progressDialog.dismiss();
                    if (success.equals("1")) {
                        if (registerSuccess.equals("1")) {
                            setVariables();
                        } else {
                            setEmpty(false, getString(R.string.invalid_user));
                            methods.logout(ProfileActivity.this);
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
                    }
                }
            }, methods.getAPIRequest(ItemName.METHOD_PROFILE,0,"","","","","","","","","","","","","", Setting.itemUser.getId(),"", null));
            loadProfile.execute();
        } else {
            Toast.makeText(ProfileActivity.this, getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
        }
    }

    public void setVariables() {
        textView_name.setText(Setting.itemUser.getName());
        textView_mobile.setText(Setting.itemUser.getMobile());
        textView_email.setText(Setting.itemUser.getEmail());

        if (!Setting.itemUser.getMobile().trim().isEmpty()) {
            ll_mobile.setVisibility(View.VISIBLE);
            view_phone.setVisibility(View.VISIBLE);
        }

        textView_notlog.setVisibility(View.GONE);
    }

    public void setEmpty(Boolean flag, String message) {
        if (flag) {
            textView_notlog.setText(message);
            textView_notlog.setVisibility(View.VISIBLE);
        } else {
            textView_notlog.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        if (Setting.isUpdate) {
            Setting.isUpdate = false;
            setVariables();
        }
        super.onResume();
    }
}