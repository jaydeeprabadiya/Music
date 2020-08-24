package thiva.tamilaudiopro.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import okhttp3.RequestBody;
import thiva.tamilaudiopro.Adapter.AdapterAllSongList;
import thiva.tamilaudiopro.Listener.ClickListenerPlayList;
import thiva.tamilaudiopro.Listener.InterAdListener;
import thiva.tamilaudiopro.Listener.SongListener;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.EndlessRecyclerViewScrollListener;
import thiva.tamilaudiopro.Utils.GlobalBus;
import thiva.tamilaudiopro.asyncTask.LoadSong;
import thiva.tamilaudiopro.item.ItemAlbums;
import thiva.tamilaudiopro.item.ItemMyPlayList;
import thiva.tamilaudiopro.item.ItemName;
import thiva.tamilaudiopro.item.ItemSong;
import thiva.tamilaudiopro.views.BlurImage;
import thiva.tamilaudiopro.views.Roundedimageview.RoundedImageView;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class SongByCatActivity extends BaseActivity {

    Methods methods;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar_playlist;
    RecyclerView rv;
    AdapterAllSongList adapter_song;
    ArrayList<ItemSong> arrayList;
    ProgressBar progressBar;
    String id = "", name = "", type = "",img_ = "";
    FrameLayout frameLayout;
    String errr_msg;
    SearchView searchView;
    Boolean isFromPush = false;
    int page = 1;
    String addedFrom = "";
    Boolean isOver = false, isScroll = false;
    private ImageView imageViewBackground;
    RoundedImageView img;
    private int BLUR_PRECENTAGE = 55;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_song_by_cat, contentFrameLayout);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        bubbleNavigationLinearView.setVisibility(View.GONE);

        if (Setting.StatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        isFromPush = getIntent().getBooleanExtra("isPush", false);
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        img_ = getIntent().getStringExtra("img");

        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                Setting.isOnline = true;
                if (!Setting.addedFrom.equals(addedFrom)) {
                    Setting.arrayList_play.clear();
                    Setting.arrayList_play.addAll(arrayList);
                    Setting.addedFrom = addedFrom;
                    Setting.isNewAdded = true;
                }
                Setting.playPos = position;

                Intent intent = new Intent(SongByCatActivity.this, PlayerService.class);
                intent.setAction(PlayerService.ACTION_PLAY);
                startService(intent);
            }
        });

        toolbar.setVisibility(View.GONE);

        appBarLayout = findViewById(R.id.mainappbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    if (Setting.Dark_Mode) {
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
                    } else {
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
                    }
                } else if (isShow) {
                    isShow = false;
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
                }
            }
        });
        toolbar_playlist = findViewById(R.id.toolbar_song);
        setSupportActionBar(toolbar_playlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = findViewById(R.id.collapsing_play);
        collapsingToolbarLayout.setTitle(name);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.background_Light));
        if (Setting.Dark_Mode) {
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.background_Light));
        } else {
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.background_Night));
        }

        LinearLayout song_by_cat_adView = findViewById(R.id.song_by_cat_adView);
        methods.showBannerAd(song_by_cat_adView);

        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }

        methods.Toolbar_Color(toolbar, getWindow(), getSupportActionBar(), "CatActivity");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);

        frameLayout = findViewById(R.id.fl_empty);
        progressBar = findViewById(R.id.pb_song_by_cat);
        rv = findViewById(R.id.rv_song_by_cat);
        final LinearLayoutManager llm_banner = new LinearLayoutManager(SongByCatActivity.this);
        rv.setLayoutManager(llm_banner);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        if (type.equals(getString(R.string.banner))) {
            addedFrom = "banner" + name;
            arrayList = (ArrayList<ItemSong>) getIntent().getSerializableExtra("songs");
            setAdapter();
            progressBar.setVisibility(View.GONE);
        } else {
            arrayList = new ArrayList<>();
            loadSongs();
        }

        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(llm_banner) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScroll = true;
                            loadSongs();
                        }
                    }, 0);
                } else {
                    try {
                        adapter_song.hideHeader();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        fab = findViewById(R.id.fab);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = llm_banner.findFirstVisibleItemPosition();
                if (firstVisibleItem > 6) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.smoothScrollToPosition(0);
            }
        });

        imageViewBackground = findViewById(R.id.iv_profilepic);
        img = findViewById(R.id.img);


        try {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageViewBackground.setImageBitmap(BlurImage.fastblur(bitmap, 1f, BLUR_PRECENTAGE));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    imageViewBackground.setImageResource(Integer.parseInt(img_));
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            imageViewBackground.setTag(target);
            Picasso.get()
                    .load(img_)
                    .placeholder(R.drawable.category)
                    .into(target);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (type.equals(getString(R.string.categories))) {
            Picasso.get()
                    .load(img_)
                    .placeholder(R.drawable.category)
                    .into(img);
        } else if (type.equals(getString(R.string.albums))) {
            Picasso.get()
                    .load(img_)
                    .placeholder(R.drawable.album)
                    .into(img);
        } else if (type.equals(getString(R.string.artist))) {
            Picasso.get()
                    .load(img_)
                    .placeholder(R.drawable.music_directors)
                    .into(img);
        } else if (type.equals(getString(R.string.playlist))) {
            Picasso.get()
                    .load(img_)
                    .placeholder(R.drawable.category)
                    .into(img);
        } else {
            Picasso.get()
                    .load(img_)
                    .placeholder(R.drawable.category)
                    .into(img);
        }

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

    private void loadSongs() {
        if (methods.isNetworkAvailable()) {

            RequestBody requestBody = null;
            if (Setting.itemNemosofts.getNemosofts_key().equals(Setting.nemosofts_key)){
                if (type.equals(getString(R.string.categories))) {
                    addedFrom = "cat"+name;
                    requestBody = methods.getAPIRequest(ItemName.METHOD_SONG_BY_CAT, page, "", "", "", "", id,"","","","","","","","","","", null);
                } else if (type.equals(getString(R.string.albums))) {
                    addedFrom = "albums"+name;
                    requestBody = methods.getAPIRequest(ItemName.METHOD_SONG_BY_ALBUMS, page, "", "", "", "", "", id,"","","","","","","","","", null);
                } else if (type.equals(getString(R.string.artist))) {
                    addedFrom = "artist"+ name;
                    requestBody = methods.getAPIRequest(ItemName.METHOD_SONG_BY_ARTIST, page, "", "", "", "", "", "", name.replace(" ","%20"),"","","","","","","","", null);
                } else if (type.equals(getString(R.string.playlist))) {
                    addedFrom = "serverplay"+name;
                    requestBody = methods.getAPIRequest(ItemName.METHOD_SONG_BY_PLAYLIST, page, "", "", "", "", "", "", "", id,"","","","","","","", null);
                }
            }
            LoadSong loadSong = new LoadSong(new SongListener() {
                @Override
                public void onStart() {

                    if (arrayList.size() == 0) {
                        arrayList.clear();
                        frameLayout.setVisibility(View.GONE);
                        rv.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemSong> arrayListCatBySong) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            if (arrayListCatBySong.size() == 0) {
                                isOver = true;
                                try {
                                    adapter_song.hideHeader();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                errr_msg = getString(R.string.err_no_songs_found);
                                setEmpty();
                            } else {
                                arrayList.addAll(arrayListCatBySong);
                                if(isScroll && Setting.addedFrom.equals(addedFrom)) {
                                    Setting.arrayList_play.clear();
                                    Setting.arrayList_play.addAll(arrayList);
                                    try {
                                        GlobalBus.getBus().postSticky(new ItemMyPlayList("", "", null));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (type.equals(getString(R.string.artist))) {
                                    if (page == 5){
                                        isOver = true;
                                    } else {
                                        page = page + 1;
                                        setAdapter();
                                    }
                                }else {
                                    page = page + 1;
                                    setAdapter();
                                }

                            }
                        } else {
                            methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                        }
                    } else {
                        errr_msg = getString(R.string.err_server);
                        setEmpty();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }, requestBody);

            loadSong.execute();


        } else {
            errr_msg = getString(R.string.err_internet_not_conn);
            setEmpty();
        }
    }

    private void setAdapter() {
        if (!isScroll) {
            adapter_song = new AdapterAllSongList(SongByCatActivity.this, arrayList, new ClickListenerPlayList() {
                @Override
                public void onClick(int position) {
                    methods.showInterAd(position, "");
                }

                @Override
                public void onItemZero() {

                }
            }, "online");
            rv.setAdapter(adapter_song);
            setEmpty();
        } else {
            adapter_song.notifyDataSetChanged();
        }
    }

    public void setEmpty() {
        if (type.equals(getString(R.string.banner))) {
            adapter_song.hideHeader();
        }
        if (arrayList.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = null;
            if (errr_msg.equals(getString(R.string.err_no_songs_found))) {
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            } else if (errr_msg.equals(getString(R.string.err_internet_not_conn))) {
                myView = inflater.inflate(R.layout.layout_err_internet, null);
            } else if (errr_msg.equals(getString(R.string.err_server))) {
                myView = inflater.inflate(R.layout.layout_err_server, null);
            }else {
                errr_msg = getString(R.string.err_no_songs_found);
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            }

            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(errr_msg);

            myView.findViewById(R.id.btn_empty_try).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadSongs();
                }
            });
            frameLayout.addView(myView);
        }
    }


    @Override
    public void onBackPressed() {
        if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (dialog_desc != null && dialog_desc.isShowing()) {
            dialog_desc.dismiss();
        }
        else if (isFromPush) {
            Intent intent = new Intent(SongByCatActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
        else {
            super.onBackPressed();
            //getFragmentManager().popBackStack();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEquilizerChange(ItemAlbums itemAlbums) {
        adapter_song.notifyDataSetChanged();
        GlobalBus.getBus().removeStickyEvent(itemAlbums);
    }
}