package thiva.tamilaudiopro.Activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import thiva.tamilaudiopro.Listener.SuccessListener;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.asyncTask.LoadReport;
import thiva.tamilaudiopro.item.ItemName;
import thiva.tamilaudiopro.item.ItemSong;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ReportActivity extends AppCompatActivity {

    Toolbar toolbar;
    Methods methods;
    ItemSong itemSong;
    TextView textView_song, textView_duration, textView_catname, tv_avg_rate,
            tv_views, tv_download, song_title, song_cat_name, song_cat_name_text;
    ImageView imageView;
    RatingBar ratingBar;
    EditText editText_report;
    Button button_submit;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode) {
            setTheme(R.style.AppTheme3);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        if (Setting.StatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        itemSong = Setting.arrayList_play.get(Setting.playPos);
        methods = new Methods(this);

        progressDialog = new ProgressDialog(ReportActivity.this);
        progressDialog.setMessage(getString(R.string.loading));

        toolbar = this.findViewById(R.id.toolbar_about);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }
        methods.Toolbar_Color(toolbar,getWindow(),getSupportActionBar(),"");

        button_submit = findViewById(R.id.button_report_submit);
        editText_report = findViewById(R.id.et_report);
        tv_views = findViewById(R.id.tv_report_song_views);
        tv_download = findViewById(R.id.tv_report_song_downloads);
        textView_song = findViewById(R.id.tv_report_song_name);
        song_title = findViewById(R.id.song_title);
        textView_duration = findViewById(R.id.tv_report_song_duration);
        tv_avg_rate = findViewById(R.id.tv_report_song_avg_rate);
        textView_catname = findViewById(R.id.tv_report_song_cat);
        song_cat_name = findViewById(R.id.song_cat_name);
        song_cat_name_text = findViewById(R.id.song_cat_name_text);
        imageView = findViewById(R.id.iv_report_song);
        ratingBar = findViewById(R.id.rb_report_song);

        tv_views.setText(methods.format(Double.parseDouble(itemSong.getViews())));
        tv_download.setText(methods.format(Double.parseDouble(itemSong.getDownloads())));

        song_title.setText(itemSong.getTitle());
        textView_song.setText(itemSong.getTitle());
        textView_duration.setText(itemSong.getDuration());
        Picasso.get()
                .load(itemSong.getImageSmall())
                .placeholder(R.drawable.songs)
                .into(imageView);

        tv_avg_rate.setTypeface(tv_avg_rate.getTypeface(), Typeface.BOLD);
        tv_avg_rate.setText(itemSong.getAverageRating());
        ratingBar.setRating(Float.parseFloat(itemSong.getAverageRating()));

        if (itemSong.getCatName() != null) {
            song_cat_name_text.setText("Category Name");
            textView_catname.setText(itemSong.getCatName());
            song_cat_name.setText(itemSong.getCatName());
        } else {
            song_cat_name_text.setText("Artist");
            song_cat_name.setText(itemSong.getArtist());
            textView_catname.setText(itemSong.getArtist());
        }

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_report.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ReportActivity.this, getString(R.string.enter_report), Toast.LENGTH_SHORT).show();
                } else {
                    if(Setting.isLogged) {
                        loadReportSubmit();
                    } else {
                        methods.clickLogin();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadReportSubmit() {
        if (methods.isNetworkAvailable()) {
            LoadReport loadReport = new LoadReport(new SuccessListener() {
                @Override
                public void onStart() {
                    progressDialog.show();
                }

                @Override
                public void onEnd(String success, String registerSuccess, String message) {
                    progressDialog.dismiss();
                    if (success.equals("1")) {
                        if (registerSuccess.equals("1")) {
                            finish();
                            Toast.makeText(ReportActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ReportActivity.this, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
                    }
                }
            }, methods.getAPIRequest(ItemName.METHOD_REPORT, 0, "", itemSong.getId(), "", "", "", "", "", "", "", "", "", "", "",Setting.itemUser.getId(),editText_report.getText().toString(), null));
            loadReport.execute();
        } else {
            Toast.makeText(this, getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
        }
    }
}