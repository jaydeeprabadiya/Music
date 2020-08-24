package thiva.tamilaudiopro.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import thiva.tamilaudiopro.Adapter.AdapterMyPlaylist;
import thiva.tamilaudiopro.Listener.ClickListenerPlayList;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.DBHelper;
import thiva.tamilaudiopro.Utils.NavigationUtil;
import thiva.tamilaudiopro.item.ItemMyPlayList;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class Add2OfflinePlaylistActivity extends AppCompatActivity {

    Toolbar toolbar;
    Methods methods;
    DBHelper dbHelper;
    RecyclerView rv;
    AdapterMyPlaylist adapterMyPlaylist;
    ArrayList<ItemMyPlayList> arrayListMyPlaylist;
    FrameLayout frameLayout;
    LinearLayout ll_local, ll_recent;
    String pid = "";
    Boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode) {
            setTheme(R.style.AppTheme3);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_2_off_playlist);

        if (Setting.StatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        pid = getIntent().getStringExtra("pid");

        dbHelper = new DBHelper(this);
        methods = new Methods(this);

        toolbar = this.findViewById(R.id.toolbar_add_2_offplay);
        toolbar.setTitle(getString(R.string.add_songs));
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }
        methods.Toolbar_Color(toolbar,getWindow(),getSupportActionBar(),"");

        arrayListMyPlaylist = new ArrayList<>();
        arrayListMyPlaylist.addAll(dbHelper.loadPlayList(false));

        frameLayout = findViewById(R.id.fl_empty);
        ll_local = findViewById(R.id.ll_local);
        ll_recent = findViewById(R.id.ll_recent);

        rv = findViewById(R.id.rv_add_2_offplay);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(gridLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        ll_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.SelectSongActivity(Add2OfflinePlaylistActivity.this, pid, getString(R.string.songs));
            }
        });

        ll_recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.SelectSongActivity(Add2OfflinePlaylistActivity.this, pid, getString(R.string.recent));
            }
        });

        adapterMyPlaylist = new AdapterMyPlaylist(this, arrayListMyPlaylist, new ClickListenerPlayList() {
            @Override
            public void onClick(int position) {
                NavigationUtil.SelectSongActivity_play_id(Add2OfflinePlaylistActivity.this, pid, getString(R.string.recent), arrayListMyPlaylist.get(position).getId());
            }

            @Override
            public void onItemZero() {
            }
        }, false);

        rv.setAdapter(adapterMyPlaylist);
        setEmpty();
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

    private void setEmpty() {
        if (arrayListMyPlaylist.size() > 0) {
            frameLayout.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        } else {
            frameLayout.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);

            frameLayout.removeAllViews();
            LayoutInflater infltr = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = infltr.inflate(R.layout.layout_err_nodata, null);

            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(getString(R.string.err_no_playlist_found));

            myView.findViewById(R.id.btn_empty_try).setVisibility(View.GONE);
            frameLayout.addView(myView);
        }
    }

    @Override
    protected void onResume() {
        if(isLoaded) {
            arrayListMyPlaylist.clear();
            arrayListMyPlaylist.addAll(dbHelper.loadPlayList(false));
            adapterMyPlaylist.notifyDataSetChanged();
        }else {
            isLoaded = true;
        }
        super.onResume();
    }
}