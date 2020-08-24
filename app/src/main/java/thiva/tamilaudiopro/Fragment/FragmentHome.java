package thiva.tamilaudiopro.Fragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import thiva.tamilaudiopro.Activity.BaseActivity;
import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Activity.MainActivity;
import thiva.tamilaudiopro.Activity.PlayerService;
import thiva.tamilaudiopro.Listener.ServerPlaylistListener;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.Control;
import thiva.tamilaudiopro.Receiver.MyReceiver;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.Adapter.AdapterAlbums_Home;
import thiva.tamilaudiopro.Adapter.AdapterArtist_Home;
import thiva.tamilaudiopro.Adapter.AdapterPlaylist_Home;
import thiva.tamilaudiopro.Adapter.AdapterRecent;
import thiva.tamilaudiopro.Adapter.AdapterRecent_my;
import thiva.tamilaudiopro.Adapter.AdapterSongList_Home;
import thiva.tamilaudiopro.Adapter.HomePagerAdapter;
import thiva.tamilaudiopro.Utils.GlobalBus;
import thiva.tamilaudiopro.Utils.NavigationUtil;
import thiva.tamilaudiopro.asyncTask.LoadAlbums;
import thiva.tamilaudiopro.asyncTask.LoadHome;
import thiva.tamilaudiopro.Listener.AlbumsListener;
import thiva.tamilaudiopro.Listener.ClickListenerPlayList;
import thiva.tamilaudiopro.Listener.HomeListener;
import thiva.tamilaudiopro.Listener.InterAdListener;
import thiva.tamilaudiopro.Listener.RecyclerClickListener;
import thiva.tamilaudiopro.asyncTask.LoadServerPlaylist;
import thiva.tamilaudiopro.item.ItemAlbums;
import thiva.tamilaudiopro.item.ItemArtist;
import thiva.tamilaudiopro.item.ItemHomeBanner;
import thiva.tamilaudiopro.item.ItemName;
import thiva.tamilaudiopro.item.ItemServerPlayList;
import thiva.tamilaudiopro.item.ItemSong;
import thiva.tamilaudiopro.Utils.DBHelper;
import thiva.tamilaudiopro.JSONParser.JSONParser;
import thiva.tamilaudiopro.Utils.RecyclerItemClickListener;
import thiva.tamilaudiopro.views.Number_Picker.NumberPicker;

import static android.content.Context.ALARM_SERVICE;
import static thiva.tamilaudiopro.Activity.BaseActivity.mLayout;
import static thiva.tamilaudiopro.SharedPre.Setting.isOnline;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class FragmentHome extends Fragment {

    LinearLayoutManager linearLayoutManager;
    DBHelper dbHelper;
    String errr_msg;
    private Methods methods;
    private String addedFrom = "all";
    private EnchantedViewPager enchantedViewPager;
    private HomePagerAdapter homePagerAdapter;
    private ArrayList<ItemHomeBanner> arrayList_banner;
    AdapterRecent_my adapterRecent;
    ProgressBar progressBar_home, progressbar_album;
    RecyclerView recyclerView, rv_songs, recyclerView_most, recyclerView_albums_new, recyclerView_albums,
            recyclerView_albums_old, recyclerView_artist;
    private ArrayList<ItemSong> arrayList_trend_songs, arrayList, arrayList_recent;
    private AdapterRecent adapterTrending;
    private RecyclerView.Adapter adapter;
    ArrayList<ItemAlbums> arrayList_albums_new, arrayList_albums;
    ArrayList<ItemServerPlayList> arrayList_albums_old;
    AdapterAlbums_Home adapterAlbum_new, adapterAlbum;
    ArrayList<ItemArtist> arrayList_artist;
    AdapterArtist_Home adapterArtistLatest;
    AdapterPlaylist_Home adapterAlbum_old;
    LinearLayout most, artist, album, favorite,search, ll_home;
    private TextView tv_songs_all, tv_albums_all, tv_artist_all,tv_playlist_all, textView_timer, textView_stoptimer;

    private LinearLayout recent_albums_container, home_ViewPager, top_artist_container, abs, ll_trending,
            top_album_new, top_album_container, top_album_container2;

    private FragmentManager fm;
    LinearLayout adView1, adView2;
    private FrameLayout frameLayout;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        dbHelper = new DBHelper(getActivity());

        fm = getFragmentManager();
        methods = new Methods(getActivity());
        Setting.Home = false;

        ll_home = rootView.findViewById(R.id.ll_home);
        textView_timer = rootView.findViewById(R.id.textView_timer);
        textView_stoptimer = rootView.findViewById(R.id.textView_stoptimer);

        tv_songs_all = rootView.findViewById(R.id.tv_home_songs_all);
        tv_songs_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTrending flatest = new FragmentTrending();
                loadFrag(flatest, getString(R.string.trending_songs));
                Setting.Home = true;
            }
        });

        tv_artist_all = rootView.findViewById(R.id.tv_home_artist_all);
        tv_artist_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent=new Intent(getActivity(), ArtistActivity.class);
//                startActivity(intent);

                FragmentArtist f_art = new FragmentArtist();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.hide(getFragmentManager().getFragments().get(getFragmentManager().getBackStackEntryCount()));
                ft.add(R.id.fragment, f_art, getString(R.string.artist));
                ft.addToBackStack(getString(R.string.artist));
                ft.commit();
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.artist));
            }
        });

        tv_albums_all = rootView.findViewById(R.id.tv_home_albums_all);
        tv_albums_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAlbums f_albums = new FragmentAlbums();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.hide(getFragmentManager().getFragments().get(getFragmentManager().getBackStackEntryCount()));
                ft.add(R.id.fragment, f_albums, getString(R.string.albums));
                ft.addToBackStack(getString(R.string.albums));
                ft.commit();
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.albums));
            }
        });

        tv_playlist_all = rootView.findViewById(R.id.tv_home_playlist_all);
        tv_playlist_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentServerPlaylist f_playlist = new FragmentServerPlaylist();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.hide(getFragmentManager().getFragments().get(getFragmentManager().getBackStackEntryCount()));
                ft.add(R.id.fragment, f_playlist, getString(R.string.playlist));
                ft.addToBackStack(getString(R.string.playlist));
                ft.commit();
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.playlist));
            }
        });

        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                if (type.equals(getString(R.string.songs))) {
                    Setting.isOnline = true;
                    addedFrom = "trend";
                    if (!Setting.addedFrom.equals(addedFrom)) {
                        Setting.arrayList_play.clear();
                        Setting.arrayList_play.addAll(arrayList_trend_songs);
                        Setting.addedFrom = addedFrom;
                        Setting.isNewAdded = true;
                    }
                    Setting.playPos = position;
                    Intent intent = new Intent(getActivity(), PlayerService.class);
                    intent.setAction(PlayerService.ACTION_PLAY);
                    if(mLayout != null  && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                    getActivity().startService(intent);

                } if (type.equals(getString(R.string.home))) {
                    isOnline = true;
                    addedFrom = "home";
                    if (!Setting.addedFrom.equals(addedFrom)) {
                        Setting.arrayList_play.clear();
                        Setting.arrayList_play.addAll(arrayList_recent);
                        Setting.addedFrom = addedFrom;
                        Setting.isNewAdded = true;
                    }
                    Setting.playPos = position;
                    Intent intent = new Intent(getActivity(), PlayerService.class);
                    intent.setAction(PlayerService.ACTION_PLAY);
                    if(mLayout != null  && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                    getActivity().startService(intent);

                }if (type.equals(getString(R.string.recent))) {
                    addedFrom = "recent";
                    isOnline = true;
                    if (!Setting.addedFrom.equals(addedFrom)) {
                        Setting.arrayList_play.clear();
                        Setting.arrayList_play.addAll(arrayList);
                        Setting.addedFrom = addedFrom;
                        Setting.isNewAdded = true;
                    }
                    Setting.playPos = position;
                    ((BaseActivity) Setting.context).changeTextPager(arrayList.get(position));
                    Intent intent = new Intent(getActivity(), PlayerService.class);
                    intent.setAction(PlayerService.ACTION_PLAY);
                    if(mLayout != null  && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                    getActivity().startService(intent);

                }
                if(mLayout != null  && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                   mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
               }
            }
        });

        frameLayout = rootView.findViewById(R.id.fl_empty);

        adView1 = rootView.findViewById(R.id.adView_1);
        adView2 = rootView.findViewById(R.id.adView_2);

        progressbar_album = rootView.findViewById(R.id.progressbar_album);
        progressBar_home = rootView.findViewById(R.id.progressBar_home);
        top_album_container2 = rootView.findViewById(R.id.top_album_container2);
        top_album_container = rootView.findViewById(R.id.top_album_container);
        top_album_new = rootView.findViewById(R.id.top_album_new);
        ll_trending = rootView.findViewById(R.id.ll_trending);
        abs = rootView.findViewById(R.id.abs);
        top_artist_container = rootView.findViewById(R.id.top_artist_container);
        home_ViewPager = rootView.findViewById(R.id.home_ViewPager);
        recent_albums_container = rootView.findViewById(R.id.recent_albums_container);

        progressbar_album.setVisibility(View.GONE);
        progressBar_home.setVisibility(View.GONE);
        top_album_container2.setVisibility(View.GONE);
        top_album_container.setVisibility(View.GONE);
        top_album_new.setVisibility(View.GONE);
        ll_trending.setVisibility(View.GONE);
        abs.setVisibility(View.GONE);
        top_artist_container.setVisibility(View.GONE);
        home_ViewPager.setVisibility(View.GONE);
        recent_albums_container.setVisibility(View.GONE);

        arrayList_banner = new ArrayList<>();
        enchantedViewPager = rootView.findViewById(R.id.viewPager_home);
        enchantedViewPager.useAlpha();
        enchantedViewPager.useScale();


        rv_songs = rootView.findViewById(R.id.rv_home_songs);
        LinearLayoutManager llm_songs = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_songs.setLayoutManager(llm_songs);
        rv_songs.setItemAnimator(new DefaultItemAnimator());
        rv_songs.setHasFixedSize(true);
        rv_songs.setAdapter(adapterRecent);
        arrayList_trend_songs = new ArrayList<>();

        recyclerView_most = rootView.findViewById(R.id.recyclerView_home_today);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_most.setLayoutManager(linearLayoutManager);
        recyclerView_most.setItemAnimator(new DefaultItemAnimator());
        recyclerView_most.setHasFixedSize(true);
        recyclerView_most.setAdapter(adapter);
        arrayList = new ArrayList<>();

        recyclerView_albums_new = rootView.findViewById(R.id.recyclerView_home_album_new);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_albums_new.setLayoutManager(linearLayoutManager);
        recyclerView_albums_new.setItemAnimator(new DefaultItemAnimator());
        recyclerView_albums_new.setHasFixedSize(true);
        recyclerView_albums_new.setAdapter(adapterAlbum_new);
        arrayList_albums_new = new ArrayList<>();

        recyclerView_albums = rootView.findViewById(R.id.recyclerView_home_album);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_albums.setLayoutManager(linearLayoutManager);
        recyclerView_albums.setItemAnimator(new DefaultItemAnimator());
        recyclerView_albums.setHasFixedSize(true);
        recyclerView_albums.setAdapter(adapterAlbum);
        arrayList_albums = new ArrayList<>();

        recyclerView_albums_old = rootView.findViewById(R.id.recyclerView_home_album2);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_albums_old.setLayoutManager(linearLayoutManager);
        recyclerView_albums_old.setItemAnimator(new DefaultItemAnimator());
        recyclerView_albums_old.setHasFixedSize(true);
        recyclerView_albums_old.setAdapter(adapterAlbum_old);
        arrayList_albums_old = new ArrayList<>();

        recyclerView_artist = rootView.findViewById(R.id.recyclerView_home_artist);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_artist.setLayoutManager(linearLayoutManager);
        recyclerView_artist.setItemAnimator(new DefaultItemAnimator());
        recyclerView_artist.setHasFixedSize(true);
        recyclerView_artist.setAdapter(adapterArtistLatest);
        arrayList_artist = new ArrayList<>();

        recyclerView = rootView.findViewById(R.id.recyclerView_home_recent);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterRecent);
        arrayList_recent = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (JSONParser.isNetworkAvailable(getActivity())) {
                    methods.showInterAd(position, getString(R.string.home));
                    if(mLayout != null  && mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
                }
            }
        }));

        recyclerView_albums_new.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (JSONParser.isNetworkAvailable(getActivity())) {
                    NavigationUtil.SongByCatActivity(getActivity(), getString(R.string.albums), arrayList_albums_new.get(position).getId(),
                            arrayList_albums_new.get(position).getName(), arrayList_albums_new.get(position).getImage());
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
                }
            }
        }));

        recyclerView_albums.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (JSONParser.isNetworkAvailable(getActivity())) {
                    NavigationUtil.SongByCatActivity(getActivity(), getString(R.string.albums), arrayList_albums.get(position).getId(),
                            arrayList_albums.get(position).getName(), arrayList_albums.get(position).getImage());
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
                }
            }
        }));

        recyclerView_albums_old.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (JSONParser.isNetworkAvailable(getActivity())) {
                    NavigationUtil.SongByCatActivity(getActivity(), getString(R.string.playlist), arrayList_albums_old.get(position).getId(),
                            arrayList_albums_old.get(position).getName(), arrayList_albums_old.get(position).getImage());
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
                }
            }
        }));

        recyclerView_artist.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (JSONParser.isNetworkAvailable(getActivity())) {
                    NavigationUtil.SongByCatActivity(getActivity(), getString(R.string.artist), arrayList_artist.get(position).getId(),
                            arrayList_artist.get(position).getName(), arrayList_artist.get(position).getImage());
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
                }
            }
        }));

        if (JSONParser.isNetworkAvailable(getActivity())) {
            if (Setting.arrayList_Home.size() == 0) {
                if(Setting.nemosofts_key.equals(Setting.itemNemosofts.getNemosofts_key())){
                    Setting.ad_arrayList = false;
                    loadHome();
                }
            }else {
                Setting.ad_arrayList = true;
                arrayList.addAll(Setting.arrayList_Home);
                if (arrayList.size() == 0) {
                    recyclerView_most.setVisibility(View.GONE);
                }else {
                    adapter = new AdapterSongList_Home(getActivity(), (ArrayList<ItemSong>) arrayList, new RecyclerClickListener() {
                        @Override
                        public void onClick(int position) {
                            if (JSONParser.isNetworkAvailable(getActivity())) {
                                methods.showInterAd(position, getString(R.string.recent));
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, "online");
                    recyclerView_most.setAdapter(adapter);
                    abs.setVisibility(View.VISIBLE);
                }

                arrayList_albums_new.addAll(Setting.arrayList_Home_album_1);
                if (arrayList_albums_new.size() == 0) {
                    top_album_new.setVisibility(View.GONE);
                }else {
                    top_album_new.setVisibility(View.VISIBLE);
                    adapterAlbum_new = new AdapterAlbums_Home(getActivity(), arrayList_albums_new);
                    recyclerView_albums_new.setAdapter(adapterAlbum_new);
                }

                arrayList_artist.addAll(Setting.arrayList_Art);
                if (arrayList_artist.size() == 0) {
                    top_artist_container.setVisibility(View.GONE);
                }else {
                    top_artist_container.setVisibility(View.VISIBLE);
                    adapterArtistLatest = new AdapterArtist_Home(getActivity(), arrayList_artist);
                    recyclerView_artist.setAdapter(adapterArtistLatest);
                }

                arrayList_trend_songs.addAll(Setting.arrayList_Trending);
                if (arrayList_trend_songs.size() == 0) {
                    ll_trending.setVisibility(View.GONE);
                }else {
                    ll_trending.setVisibility(View.VISIBLE);
                    adapterTrending = new AdapterRecent(getActivity(), arrayList_trend_songs, new ClickListenerPlayList() {
                        @Override
                        public void onClick(int position) {
                            if (methods.isNetworkAvailable()) {
                                methods.showInterAd(position, getString(R.string.songs));
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onItemZero() {
                        }
                    });
                    rv_songs.setAdapter(adapterTrending);
                }

                arrayList_banner.addAll(Setting.arrayList_Home_Pas);
                if (arrayList_banner.size() == 0) {
                    home_ViewPager.setVisibility(View.GONE);
                }else {
                    home_ViewPager.setVisibility(View.VISIBLE);
                    homePagerAdapter = new HomePagerAdapter(getActivity(), arrayList_banner);
                    enchantedViewPager.setAdapter(homePagerAdapter);
                    if (homePagerAdapter.getCount() > 2) {
                        enchantedViewPager.setCurrentItem(1);
                    }
                }

                arrayList_albums.addAll(Setting.arrayList_Home_album_2);
                if (arrayList_albums.size() == 0) {
                    top_album_container.setVisibility(View.GONE);
                }else {
                    top_album_container.setVisibility(View.VISIBLE);
                    adapterAlbum = new AdapterAlbums_Home(getActivity(), arrayList_albums);
                    recyclerView_albums.setAdapter(adapterAlbum);
                }

                arrayList_albums_old.addAll(Setting.arrayList_Home_album_3);
                if (arrayList_albums_old.size() == 0) {
                    top_album_container2.setVisibility(View.GONE);
                }else {
                    top_album_container2.setVisibility(View.VISIBLE);
                    adapterAlbum_old = new AdapterPlaylist_Home(getActivity(), arrayList_albums_old);
                    recyclerView_albums_old.setAdapter(adapterAlbum_old);
                }
                loadRecent();
            }
        } else {
            setEmpty();
        }

        search = rootView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSongBySearch sh = new FragmentSongBySearch();
                loadFrag(sh, getResources().getString(R.string.search), fm);
                Setting.Home = true;

            }
        });

        most = rootView.findViewById(R.id.most);
        most.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentFav f_fav = new FragmentFav();
                loadFrag(f_fav, getString(R.string.favourite), fm);
                Setting.Home = true;
            }
        });

        artist = rootView.findViewById(R.id.artist);
        artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Setting.isPlayed) {
                    if (!Control.timer) {
                        timer();
                    } else {
                        openCancelTimerDialog();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please start audio first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        album = rootView.findViewById(R.id.album);
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentLatest flatest = new FragmentLatest();
                loadFrag(flatest, getString(R.string.latest));
                Setting.Home = true;
            }
        });

        favorite = rootView.findViewById(R.id.favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRecentSongs frecent = new FragmentRecentSongs();
                loadFrag(frecent, getString(R.string.recently_played));
                Setting.Home = true;
            }
        });

        return rootView;
    }

    private void ShowBannerAd() {
        methods.showBannerAd(adView1);
        methods.showBannerAd(adView2);
    }

    private void loadHome() {
        if (methods.isNetworkAvailable()) {
            LoadHome loadHome = new LoadHome(new HomeListener() {
                @Override
                public void onStart() {
                    progressBar_home.setVisibility(View.VISIBLE);
                }

                @Override
                public void onEnd(String success, ArrayList<ItemHomeBanner> arrayListBanner, ArrayList<ItemAlbums> arrayListAlbums,
                                  ArrayList<ItemArtist> arrayListArtist, ArrayList<ItemSong> arrayListSongs, ArrayList<ItemSong> arrayListlatest) {

                    if (getActivity() != null) {
                        if (success.equals("1")) {
                            progressBar_home.setVisibility(View.GONE);
                            if (Setting.itemNemosofts.getNemosofts_key().equals(Setting.nemosofts_key)){
                                arrayList_banner.addAll(arrayListBanner);
                                arrayList_trend_songs.addAll(arrayListSongs);
                                arrayList_albums_new.addAll(arrayListAlbums);
                                arrayList_artist.addAll(arrayListArtist);
                                arrayList.addAll(arrayListlatest);
                            }
                            if (arrayList.size() == 0) {
                                recyclerView_most.setVisibility(View.GONE);
                            }else {
                                adapter = new AdapterSongList_Home(getActivity(), arrayList, new RecyclerClickListener() {
                                    @Override
                                    public void onClick(int position) {
                                        if (JSONParser.isNetworkAvailable(getActivity())) {
                                            methods.showInterAd(position, getString(R.string.recent));
                                        } else {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, "online");
                                recyclerView_most.setAdapter(adapter);
                                Setting.arrayList_Home.clear();
                                Setting.arrayList_Home.addAll(arrayList);
                                abs.setVisibility(View.VISIBLE);
                            }

                            if (arrayList_artist.size() == 0) {
                                top_artist_container.setVisibility(View.GONE);
                            }else {
                                top_artist_container.setVisibility(View.VISIBLE);
                                adapterArtistLatest = new AdapterArtist_Home(getActivity(), arrayList_artist);
                                recyclerView_artist.setAdapter(adapterArtistLatest);
                                Setting.arrayList_Art.clear();
                                Setting.arrayList_Art.addAll(arrayList_artist);
                            }

                            if (Setting.arrayList_play.size() == 0 && arrayListSongs.size() > 0) {
                                Setting.arrayList_play.addAll(arrayListSongs);
                                ((BaseActivity) getActivity()).changeText(Setting.arrayList_play.get(0), "home");
                            }

                            if (arrayList_banner.size() == 0) {
                                home_ViewPager.setVisibility(View.GONE);
                            }else {
                                home_ViewPager.setVisibility(View.VISIBLE);
                                homePagerAdapter = new HomePagerAdapter(getActivity(), arrayList_banner);
                                enchantedViewPager.setAdapter(homePagerAdapter);
                                Setting.arrayList_Home_Pas.clear();
                                Setting.arrayList_Home_Pas.addAll(arrayList_banner);
                                if (homePagerAdapter.getCount() > 2) {
                                    enchantedViewPager.setCurrentItem(1);
                                }
                            }

                            if (arrayList_trend_songs.size() == 0) {
                                ll_trending.setVisibility(View.GONE);
                            }else {
                                ll_trending.setVisibility(View.VISIBLE);
                                adapterTrending = new AdapterRecent(getActivity(), arrayList_trend_songs, new ClickListenerPlayList() {
                                    @Override
                                    public void onClick(int position) {
                                        if (methods.isNetworkAvailable()) {
                                            methods.showInterAd(position, getString(R.string.songs));
                                        } else {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onItemZero() {
                                    }
                                });
                                Setting.arrayList_Trending.clear();
                                Setting.arrayList_Trending.addAll(arrayList_trend_songs);
                                rv_songs.setAdapter(adapterTrending);
                            }

                            if (arrayList_albums_new.size() == 0) {
                                top_album_new.setVisibility(View.GONE);
                            }else {
                                top_album_new.setVisibility(View.VISIBLE);
                                adapterAlbum_new = new AdapterAlbums_Home(getActivity(), arrayList_albums_new);
                                recyclerView_albums_new.setAdapter(adapterAlbum_new);
                                Setting.arrayList_Home_album_1.clear();
                                Setting.arrayList_Home_album_1.addAll(arrayList_albums_new);
                            }

                            if (arrayList_albums_old.size() == 0) {
                                top_album_container2.setVisibility(View.GONE);
                            }else {
                                top_album_container2.setVisibility(View.VISIBLE);
                                adapterAlbum_old = new AdapterPlaylist_Home(getActivity(), arrayList_albums_old);
                                recyclerView_albums_old.setAdapter(adapterAlbum_old);
                                Setting.arrayList_Home_album_3.clear();
                                Setting.arrayList_Home_album_3.addAll(arrayList_albums_old);
                            }

                            LoadAlbum();
                            LoadAlbum2();
                            loadRecent();
                            errr_msg = getString(R.string.err_no_artist_found);
                        } else {
                            errr_msg = getString(R.string.err_server);
                            progressBar_home.setVisibility(View.GONE);
                        }
                    }
                }
            }, methods.getAPIRequest(ItemName.METHOD_HOME, 0,"","","","","","","","","","","","","","","", null));
            loadHome.execute();
        } else {
            errr_msg = getString(R.string.err_internet_not_conn);

        }
    }

    private void openCancelTimerDialog() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Setting.Dark_Mode){
                alert = new AlertDialog.Builder(getActivity(), R.style.ThemeDialog2);
            }else {
                alert = new AlertDialog.Builder(getActivity(), R.style.ThemeDialog);
            }
        } else {
            alert = new AlertDialog.Builder(getActivity());
        }

        alert.setTitle(getString(R.string.cancel_timer));
        alert.setMessage(getString(R.string.sure_cancel_timer));
        alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Control.timer = false;
                textView_stoptimer.setVisibility(View.VISIBLE);
                textView_timer.setVisibility(View.GONE);
                Control.alarmManager.cancel(Control.pendingIntent);
            }
        });
        alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.show();
    }

    public void timer() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        final NumberPicker hours_picker = (NumberPicker) dialog.findViewById(R.id.hours_picker);
        final NumberPicker minute_picker = (NumberPicker) dialog.findViewById(R.id.minute_picker);
        Button button = (Button) dialog.findViewById(R.id.button_dialog);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hours = String.valueOf(hours_picker.getValue());
                String minute = String.valueOf(minute_picker.getValue());

                String totalTime = hours + ":" + minute;
                int total_timer = (int) Control.convert_long(totalTime);
                Control.timer = true;

                Intent intent = new Intent(getActivity(), MyReceiver.class);
                Control.pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Control.alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Control.alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + total_timer, Control.pendingIntent);
                } else {
                    Control.alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + total_timer, Control.pendingIntent);
                }
                updateTimer(System.currentTimeMillis(), total_timer);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateTimer(final long currentTime, final int time) {
        long timeleft = (currentTime + time) - System.currentTimeMillis();
        if (timeleft > 0) {
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeleft),
                    TimeUnit.MILLISECONDS.toMinutes(timeleft) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(timeleft) % TimeUnit.MINUTES.toSeconds(1));
            textView_stoptimer.setVisibility(View.GONE);
            textView_timer.setVisibility(View.VISIBLE);
            textView_timer.setText(hms);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Control.timer) {
                        updateTimer(currentTime, time);
                    } else {
                        textView_stoptimer.setVisibility(View.VISIBLE);
                        textView_timer.setVisibility(View.GONE);

                    }
                }
            }, 1000);
        } else {
            textView_stoptimer.setVisibility(View.VISIBLE);
            textView_timer.setVisibility(View.GONE);
        }
    }


    private void LoadAlbum() {
        LoadAlbums loadAlbums = new LoadAlbums(new AlbumsListener() {
            @Override
            public void onStart() {
                if (arrayList_albums.size() == 0) {
                    arrayList_albums.clear();
                    top_album_container.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemAlbums> arrayListAlbums) {
                if (getActivity() != null) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            if (arrayListAlbums.size() == 0) {
                                errr_msg = getString(R.string.err_no_albums_found);
                                top_album_container.setVisibility(View.GONE);
                            } else {
                                arrayList_albums.addAll(arrayListAlbums);
                                adapterAlbum = new AdapterAlbums_Home(getActivity(), arrayList_albums);
                                recyclerView_albums.setAdapter(adapterAlbum);
                                Setting.arrayList_Home_album_2.clear();
                                Setting.arrayList_Home_album_2.addAll(arrayList_albums);
                            }

                        } else {
                            methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                        }
                    } else {
                        errr_msg = getString(R.string.err_server);
                    }
                }
            }
        }, methods.getAPIRequest(ItemName.METHOD_ALBUM_HOME, 0, "", "", "", "", "", "", "", "","","","","","","","", null));
        loadAlbums.execute();
    }

    private void LoadAlbum2() {
        LoadServerPlaylist loadServerPlaylist = new LoadServerPlaylist(new ServerPlaylistListener() {
            @Override
            public void onStart() {
                if (arrayList_albums_old.size() == 0) {
                    arrayList_albums_old.clear();
                    top_album_container2.setVisibility(View.VISIBLE);
                    progressbar_album.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemServerPlayList> arrayListAlbums) {
                if (getActivity() != null) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            if (arrayListAlbums.size() == 0) {
                                errr_msg = getString(R.string.err_no_albums_found);
                                top_album_container2.setVisibility(View.GONE);
                                progressbar_album.setVisibility(View.GONE);
                            } else {
                                progressbar_album.setVisibility(View.GONE);
                                arrayList_albums_old.addAll(arrayListAlbums);
                                adapterAlbum_old = new AdapterPlaylist_Home(getActivity(), arrayList_albums_old);
                                recyclerView_albums_old.setAdapter(adapterAlbum_old);
                                Setting.arrayList_Home_album_3.clear();
                                Setting.arrayList_Home_album_3.addAll(arrayList_albums_old);
                            }
                        } else {
                            methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                        }
                    } else {
                        errr_msg = getString(R.string.err_server);
                    }
                }
            }
        }, methods.getAPIRequest(ItemName.METHOD_Playlist_HOME, 0, "", "", "", "", "", "", "", "","","","","","","","", null));
        loadServerPlaylist.execute();
    }

    private void loadRecent() {
        arrayList_recent.addAll(dbHelper.loadDataRecent(true, "15"));
        if (arrayList_recent.size() == 0) {
            recent_albums_container.setVisibility(View.GONE);
        } else {
            recent_albums_container.setVisibility(View.VISIBLE);
            arrayList_recent = dbHelper.loadDataRecent(true, Setting.recentLimit);
            adapterRecent = new AdapterRecent_my(getActivity(), arrayList_recent);
            recyclerView.setAdapter(adapterRecent);
        }
        ShowBannerAd();
    }

    public void setEmpty() {
        ll_home.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);

        frameLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.layout_err_internet, null);
        TextView textView = myView.findViewById(R.id.tv_empty_msg);
        textView.setText(getString(R.string.err_internet_not_conn));
        myView.findViewById(R.id.btn_empty_try).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JSONParser.isNetworkAvailable(getActivity())) {
                    frameLayout.setVisibility(View.GONE);
                    ll_home.setVisibility(View.VISIBLE);
                    if (Setting.arrayList_Home.size() == 0) {
                        if(Setting.nemosofts_key.equals(Setting.itemNemosofts.getNemosofts_key())){
                            Setting.ad_arrayList = false;
                            loadHome();
                        }
                    }
                }

            }
        });

        frameLayout.addView(myView);
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        if (!name.equals(getString(R.string.search))) {
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (name.equals(getString(R.string.search))) {
            ft.hide(fm.getFragments().get(fm.getBackStackEntryCount()));
            ft.add(R.id.fragment, f1, name);
           // ft.addToBackStack(name);
        } else {
            ft.replace(R.id.fragment, f1, name);
            //ft.addToBackStack(null);
        }
        ft.commit();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(name);
    }

    public void loadFrag(Fragment f1, String name) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (name.equals(getString(R.string.search))) {
            ft.hide(fm.getFragments().get(fm.getBackStackEntryCount()));
            ft.add(R.id.fragment_dash, f1, name);
            //ft.addToBackStack(name);
        } else {
            ft.replace(R.id.fragment_dash, f1, name);
        }
        ft.commit();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(name);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEquilizerChange(ItemAlbums itemAlbums) {
        adapterRecent.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        adapterTrending.notifyDataSetChanged();
        GlobalBus.getBus().removeStickyEvent(itemAlbums);
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        GlobalBus.getBus().unregister(this);
        super.onStop();
    }

}