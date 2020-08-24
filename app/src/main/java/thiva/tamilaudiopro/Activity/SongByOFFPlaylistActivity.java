package thiva.tamilaudiopro.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import thiva.tamilaudiopro.Adapter.AdapterOFSongList;
import thiva.tamilaudiopro.Listener.ClickListenerPlayList;
import thiva.tamilaudiopro.Listener.InterAdListener;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.GlobalBus;
import thiva.tamilaudiopro.item.ItemAlbums;
import thiva.tamilaudiopro.item.ItemMyPlayList;
import thiva.tamilaudiopro.item.ItemSong;
import thiva.tamilaudiopro.views.Roundedimageview.RoundedImageView;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class SongByOFFPlaylistActivity extends BaseActivity {

    Methods methods;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar_playlist;
    RecyclerView rv;
    ItemMyPlayList itemMyPlayList;
    AdapterOFSongList adapter_song;
    ArrayList<ItemSong> arrayList;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    RoundedImageView iv_playlist2;
    ImageView  image_edit, image_add_play, image_add_que, image_delete;
    TextView tv_no_song, text_edit, text_add_play, text_add_qu, text_delete;
    Boolean isLoaded = false;
    String addedFrom = "offplay";
    SearchView searchView;
    LinearLayout ll_edit, ll_delete, ll_add_2_playlist, ll_addQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_song_by_offline_playlist, contentFrameLayout);

        if (Setting.StatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        bubbleNavigationLinearView.setVisibility(View.GONE);

        itemMyPlayList = (ItemMyPlayList) getIntent().getSerializableExtra("item");
        addedFrom = addedFrom + itemMyPlayList.getName();

        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                Setting.isOnline = false;
                if(!Setting.addedFrom.equals(addedFrom)) {
                    Setting.arrayList_play.clear();
                    Setting.arrayList_play.addAll(arrayList);
                    Setting.addedFrom = addedFrom;
                    Setting.isNewAdded = true;
                }
                Setting.playPos = position;

                Intent intent = new Intent(SongByOFFPlaylistActivity.this, PlayerService.class);
                intent.setAction(PlayerService.ACTION_PLAY);
                startService(intent);
            }
        });

        toolbar.setVisibility(View.GONE);

        appBarLayout = findViewById(R.id.mainappbar);
        toolbar_playlist = findViewById(R.id.toolbar_playlist);
        setSupportActionBar(toolbar_playlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = findViewById(R.id.collapsing_play);
        collapsingToolbarLayout.setTitle(itemMyPlayList.getName());

        text_edit = findViewById(R.id.text_edit);
        text_add_play = findViewById(R.id.text_add_play);
        text_add_qu = findViewById(R.id.text_add_qu);
        text_delete = findViewById(R.id.text_delete);

        image_edit = findViewById(R.id.image_edit);
        image_add_play = findViewById(R.id.image_add_play);
        image_add_que = findViewById(R.id.image_add_que);
        image_delete = findViewById(R.id.image_delete);

        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }
        methods.Toolbar_Color(toolbar_playlist,getWindow(),getSupportActionBar(),"");

        if (Setting.Dark_Mode) {
            collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Background_Night_S));
        } else {
            collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Background_Light_S));
        }

        if (Setting.ToolBar_Color) {
            switch (Setting.get_color_my){
                case 0:
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Toolbar_1));
                    break;
                case 1:
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Toolbar_2));
                    break;
                case 2:
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Toolbar_3));
                    break;
                case 3:
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Toolbar_4));
                    break;
                case 4:
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Toolbar_5));
                    break;
                case 5:
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Toolbar_6));
                    break;
                case 6:
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Toolbar_7));
                    break;
                case 7:
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Toolbar_8));
                    break;
                default:
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.Toolbar_8));
            }
        }

        if (Setting.ToolBar_Color) {
            text_edit.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.white)));
            text_add_play.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.white)));
            text_add_qu.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.white)));
            text_delete.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.white)));
        }

        if (Setting.ToolBar_Color) {
            image_edit.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.white)));
            image_add_play.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.white)));
            image_add_que.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.white)));
            image_delete.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(SongByOFFPlaylistActivity.this, R.color.white)));
        }

        arrayList = new ArrayList<>();

        frameLayout = findViewById(R.id.fl_empty);
        progressBar = findViewById(R.id.pb_song_by_playlist);
        progressBar.setVisibility(View.GONE);

        ll_edit = findViewById(R.id.ll_edit);
        ll_delete = findViewById(R.id.ll_delete);
        ll_add_2_playlist = findViewById(R.id.ll_add_2_offplay);
        ll_addQueue = findViewById(R.id.ll_addQueue);

        rv = findViewById(R.id.rv_song_by_playlist);
        LinearLayoutManager llm_banner = new LinearLayoutManager(this);
        rv.setLayoutManager(llm_banner);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        iv_playlist2 = findViewById(R.id.iv_collapse_playlist2);
        tv_no_song = findViewById(R.id.tv_playlist_no_song);

        Picasso.get()
                .load(methods.getAlbumArtUri(Integer.parseInt(itemMyPlayList.getArrayListUrl().get(3))))
                .placeholder(R.drawable.songs)
                .into(iv_playlist2);

        AppBarLayout appBarLayout = findViewById(R.id.mainappbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                tv_no_song.setAlpha(1 - Math.abs((float) verticalOffset / appBarLayout.getTotalScrollRange()));
                iv_playlist2.setAlpha(1 - Math.abs((float) verticalOffset / appBarLayout.getTotalScrollRange()));
            }
        });

        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog();
            }
        });

        ll_add_2_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongByOFFPlaylistActivity.this, Add2OfflinePlaylistActivity.class);
                intent.putExtra("pid", itemMyPlayList.getId());
                startActivity(intent);
            }
        });

        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongByOFFPlaylistActivity.this, SelectSongActivity.class);
                intent.putExtra("type", getString(R.string.edit));
                intent.putExtra("pid", itemMyPlayList.getId());
                intent.putExtra("array", arrayList);
                startActivity(intent);
            }
        });

        ll_addQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size() > 0) {
                    if (Setting.isOnline || Setting.isDownloaded) {
                        showQueueAlert();
                    } else {
                        Setting.arrayList_play.addAll(arrayList);
                        GlobalBus.getBus().postSticky(new ItemMyPlayList("", "", null));
                        Toast.makeText(SongByOFFPlaylistActivity.this, getString(R.string.queue_updated), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SongByOFFPlaylistActivity.this, getString(R.string.no_songs_to_add_queue), Toast.LENGTH_SHORT).show();
                }
            }
        });

        new LoadOfflineSongs().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setExpanded(false);
            }
        });

        return super.onCreateOptionsMenu(menu);
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

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (adapter_song != null) {
                if (!searchView.isIconified()) {
                    adapter_song.getFilter().filter(s);
                    try {
                        adapter_song.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
    };

    class LoadOfflineSongs extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            arrayList.clear();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                arrayList = dbHelper.loadDataPlaylist(itemMyPlayList.getId(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            setAdapter();
        }
    }

    private void setAdapter() {
        adapter_song = new AdapterOFSongList(SongByOFFPlaylistActivity.this, arrayList, new ClickListenerPlayList() {
            @Override
            public void onClick(int position) {
                methods.showInterAd(position, "");
            }

            @Override
            public void onItemZero() {
                setEmpty();
            }
        }, getString(R.string.playlist));
        rv.setAdapter(adapter_song);
        setEmpty();
    }

    private void openDeleteDialog() {
        AlertDialog.Builder dialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new AlertDialog.Builder(SongByOFFPlaylistActivity.this, R.style.ThemeDialog);
        } else {
            dialog = new AlertDialog.Builder(SongByOFFPlaylistActivity.this);
        }
        dialog.setTitle(getString(R.string.delete));
        dialog.setMessage(getString(R.string.sure_delete_playlist));
        dialog.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper.removePlayList(itemMyPlayList.getId(), false);
                finish();
            }
        });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void showQueueAlert() {
        AlertDialog.Builder dialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new AlertDialog.Builder(SongByOFFPlaylistActivity.this, R.style.ThemeDialog);
        } else {
            dialog = new AlertDialog.Builder(SongByOFFPlaylistActivity.this);
        }
        dialog.setTitle(getString(R.string.add_to_queue));
        dialog.setMessage(getString(R.string.off_add_qeue_alert));
        dialog.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Setting.arrayList_play.clear();
                Setting.arrayList_play.addAll(arrayList);

                Toast.makeText(SongByOFFPlaylistActivity.this, getString(R.string.queue_updated), Toast.LENGTH_SHORT).show();
                GlobalBus.getBus().postSticky(new ItemMyPlayList("", "", null));
                methods.showInterAd(0, "");
            }
        });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    public void setEmpty() {
        tv_no_song.setText(arrayList.size() + " " + getString(R.string.songs));
        if (arrayList.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = inflater.inflate(R.layout.layout_err_nodata, null);

            myView.findViewById(R.id.btn_empty_try).setVisibility(View.GONE);
            frameLayout.addView(myView);
        }
    }

    @Override
    protected void onResume() {
        if (isLoaded) {

            new LoadOfflineSongs().execute();

            tv_no_song.setText(arrayList.size() + " " + getString(R.string.songs));

            Picasso.get()
                    .load(methods.getAlbumArtUri(Integer.parseInt(itemMyPlayList.getArrayListUrl().get(3))))
                    .placeholder(R.drawable.songs)
                    .into(iv_playlist2, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            if (arrayList.size() > 0) {
                                Picasso.get()
                                        .load(arrayList.get(arrayList.size() - 1).getImageBig())
                                        .into(iv_playlist2);
                            } else {
                                iv_playlist2.setImageResource(R.drawable.songs);
                            }
                        }
                    });
        } else {
            isLoaded = true;
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (dialog_desc != null && dialog_desc.isShowing()) {
            dialog_desc.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEquilizerChange(ItemAlbums itemAlbums) {
        adapter_song.notifyDataSetChanged();
        GlobalBus.getBus().removeStickyEvent(itemAlbums);
    }
}