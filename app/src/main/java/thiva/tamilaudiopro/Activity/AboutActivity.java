package thiva.tamilaudiopro.Activity;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.asyncTask.LoadAbout;
import thiva.tamilaudiopro.Listener.AboutListener;
import thiva.tamilaudiopro.Utils.DBHelper;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbarAbout;
    Methods methods;
    ProgressBar progressBarAbout;
    LinearLayout  linearLayoutAbout;
    DBHelper dbHelper;
    TextView  tv_company, tv_email, tv_website, tv_contact, description;
    String website, email, desc, appname, appversion, appauthor, appcontact, privacy, developedby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode) {
            setTheme(R.style.AppTheme3);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (Setting.StatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        dbHelper = new DBHelper(this);
        methods = new Methods(this);

        toolbarAbout = this.findViewById(R.id.toolbar_about);
        toolbarAbout.setTitle(getString(R.string.about_us));
        this.setSupportActionBar(toolbarAbout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }

        methods.Toolbar_Color(toolbarAbout,getWindow(),getSupportActionBar(),"");

        progressBarAbout = findViewById(R.id.load);
        linearLayoutAbout = findViewById(R.id.view_Layout);
        linearLayoutAbout.setVisibility(View.GONE);
        tv_company = findViewById(R.id.company);
        tv_email = findViewById(R.id.email);
        tv_website = findViewById(R.id.website);
        tv_contact = findViewById(R.id.contact);
        description = findViewById(R.id.description);

        dbHelper.getAbout();

        if (methods.isNetworkAvailable()) {
            if (Setting.itemNemosofts.getPurchase_code().equals(Setting.purchase_code)){
                loadAboutData();
            }
        } else {
            if (dbHelper.getAbout()) {
                setVariables();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    public void loadAboutData() {
        LoadAbout loadAbout = new LoadAbout(AboutActivity.this, new AboutListener() {
            @Override
            public void onStart() {
                progressBarAbout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(String success, String verifyStatus, String message) {
                progressBarAbout.setVisibility(View.GONE);
                linearLayoutAbout.setVisibility(View.VISIBLE);
                if (success.equals("1")) {
                    if (!verifyStatus.equals("-1")) {
                        setVariables();
                        dbHelper.addtoAbout();
                    } else {
                        methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                    }
                }

            }
        });
        loadAbout.execute();
    }

    public void setVariables() {
        appname = Setting.itemAbout.getAppName();
        desc = Setting.itemAbout.getAppDesc();
        appversion = BuildConfig.VERSION_NAME;
        appauthor = Setting.itemAbout.getAuthor();
        appcontact = Setting.itemAbout.getContact();
        email = Setting.itemAbout.getEmail();
        website = Setting.itemAbout.getWebsite();
        privacy = Setting.itemAbout.getPrivacy();
        developedby = Setting.itemAbout.getDevelopedby();

        tv_company = findViewById(R.id.company);
        tv_email = findViewById(R.id.email);
        tv_website = findViewById(R.id.website);
        tv_contact = findViewById(R.id.contact);

        if (!desc.trim().isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                description.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_COMPACT));
            } else {
                description.setText(Html.fromHtml(desc));
            }
        }

        if (!email.trim().isEmpty()) {
            tv_email.setText(email);
        }

        if (!website.trim().isEmpty()) {
            tv_website.setText(website);
        }

        if (!appauthor.trim().isEmpty()) {
            tv_company.setText(appauthor);
        }

        if (!appcontact.trim().isEmpty()) {
            tv_contact.setText(appcontact);
        }
    }
}