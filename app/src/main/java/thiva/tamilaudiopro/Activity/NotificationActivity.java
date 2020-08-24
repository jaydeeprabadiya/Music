package thiva.tamilaudiopro.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import thiva.tamilaudiopro.Adapter.AdapterNot;
import thiva.tamilaudiopro.Listener.NotListener;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.asyncTask.LoadNot;
import thiva.tamilaudiopro.item.ItemName;
import thiva.tamilaudiopro.item.ItemNotification;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class NotificationActivity extends AppCompatActivity {

    Toolbar toolbar;
    Methods methods;
    private RecyclerView rv;
    private AdapterNot adapterNot;
    private ArrayList<ItemNotification> arrayList_not;
    private ProgressBar progressBar;
    private GridLayoutManager glm_banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode) {
            setTheme(R.style.AppTheme3);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (Setting.StatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        methods = new Methods(this);

        toolbar = this.findViewById(R.id.toolbar_not);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }
        methods.Toolbar_Color(toolbar,getWindow(),getSupportActionBar(),"");


        arrayList_not = new ArrayList<>();

        rv = findViewById(R.id.notification_re);
        glm_banner = new GridLayoutManager(this, 1);
        rv.setLayoutManager(glm_banner);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        progressBar = findViewById(R.id.progressBar_not);

        loadNot();
    }

    private void loadNot() {
        if (Setting.isLogged){
            if (methods.isNetworkAvailable()) {
                LoadNot not = new LoadNot(new NotListener() {
                    @Override
                    public void onStart() {
                        if (arrayList_not.size() == 0) {
                            arrayList_not.clear();
                        }
                    }

                    @Override
                    public void onEnd(String success, ArrayList<ItemNotification> arrayList) {
                        if (NotificationActivity.this != null) {
                            if (success.equals("1")) {
                                arrayList_not.addAll(arrayList);
                                setAdapter();
                            } else {
                                progressBar.setVisibility(View.GONE);
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, methods.getAPIRequest(ItemName.METHOD_NOTIFICATION, 0, "", "", "", "", "", "", "", "", "", "", "", "", "", Setting.itemUser.getId(), "", null));
                not.execute();
            }
        }else {
            progressBar.setVisibility(View.GONE);
        }

    }

    private void setAdapter() {
        adapterNot = new AdapterNot(NotificationActivity.this, arrayList_not);
        rv.setAdapter(adapterNot);
        progressBar.setVisibility(View.GONE);
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
}