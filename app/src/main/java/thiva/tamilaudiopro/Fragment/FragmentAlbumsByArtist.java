package thiva.tamilaudiopro.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import thiva.tamilaudiopro.Listener.IOnBackPressed;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.Adapter.AdapterAlbums;
import thiva.tamilaudiopro.Utils.NavigationUtil;
import thiva.tamilaudiopro.asyncTask.LoadAlbums;
import thiva.tamilaudiopro.Listener.AlbumsListener;
import thiva.tamilaudiopro.Listener.InterAdListener;
import thiva.tamilaudiopro.item.ItemAlbums;
import thiva.tamilaudiopro.item.ItemArtist;
import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Activity.SongByCatActivity;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.EndlessRecyclerViewScrollListener;
import thiva.tamilaudiopro.Utils.RecyclerItemClickListener;
import thiva.tamilaudiopro.item.ItemName;

import java.util.ArrayList;

import static thiva.tamilaudiopro.Activity.BaseActivity.mLayout;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class FragmentAlbumsByArtist extends Fragment implements IOnBackPressed {

    private Methods methods;
    private RecyclerView rv;
    private AdapterAlbums adapterAlbums;
    private ArrayList<ItemAlbums> arrayList;
    private ItemArtist itemArtist;
    private ProgressBar progressBar;
    private TextView tv_artist;
    private LinearLayout ll_all_songs;
    private FrameLayout frameLayout;
    private String errr_msg;
    private GridLayoutManager glm_banner;
    private int page = 1;
    private Boolean isOver = false, isScroll = false;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums_by_art, container, false);

        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                switch (type) {
                    case "":
                        NavigationUtil.SongByCatActivity(getActivity(), getString(R.string.albums), adapterAlbums.getItem(position).getId(),
                                adapterAlbums.getItem(position).getName(), adapterAlbums.getItem(position).getImage());
                        break;
                    case "all":
                        NavigationUtil.SongByCatActivity(getActivity(), getString(R.string.artist), itemArtist.getId(),
                                itemArtist.getName(), itemArtist.getImage());
                        break;
                }
            }
        });

        arrayList = new ArrayList<>();
        itemArtist = (ItemArtist) getArguments().getSerializable("item");

        progressBar = rootView.findViewById(R.id.pb_albums);
        frameLayout = rootView.findViewById(R.id.fl_empty);

        tv_artist = rootView.findViewById(R.id.tv_artist);
        ll_all_songs = rootView.findViewById(R.id.ll_artist_all_songs);
        rv = rootView.findViewById(R.id.rv_albums);
        glm_banner = new GridLayoutManager(getActivity(), 2);
        glm_banner.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapterAlbums.isHeader(position) ? glm_banner.getSpanCount() : 1;
            }
        });

        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(glm_banner);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        tv_artist.setText(itemArtist.getName());
        ll_all_songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methods.showInterAd(0, "all");
            }
        });

        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                methods.showInterAd(position, "");
            }
        }));

        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(glm_banner) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScroll = true;
                            loadAlbums();
                        }
                    }, 0);
                } else {
                    try {
                        adapterAlbums.hideHeader();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        fab = rootView.findViewById(R.id.fab);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = glm_banner.findFirstVisibleItemPosition();

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

        loadAlbums();

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            Setting.search_item = s.replace(" ", "%20");
            FragmentSearchAlbums fsearch = new FragmentSearchAlbums();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //ft.hide(getFragmentManager().getFragments().get(getFragmentManager().getBackStackEntryCount()));
            ft.add(R.id.fragment, fsearch, getString(R.string.search_albums));

            //FragmentArtist frat = new FragmentArtist();
            //ft.replace(R.id.fragment, frat, getString(R.string.artist));
            //ft.addToBackStack(null);
            ft.commit();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    private void loadAlbums() {
        if (methods.isNetworkAvailable()) {
            LoadAlbums loadAlbums = new LoadAlbums(new AlbumsListener() {
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
                public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemAlbums> arrayListAlbums) {
                    if (getActivity() != null) {
                        if (success.equals("1")) {
                            if (!verifyStatus.equals("-1")) {
                                if (arrayListAlbums.size() == 0) {
                                    isOver = true;
                                    try {
                                        adapterAlbums.hideHeader();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    errr_msg = getString(R.string.err_no_songs_found);
                                    setEmpty();
                                } else {
                                    page = page + 1;
                                    arrayList.addAll(arrayListAlbums);
                                    setAdapter();
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
                }
            }, methods.getAPIRequest(ItemName.METHOD_ALBUMS_BY_ARTIST, page, "", itemArtist.getId(), "", "", "", "", "", "","","","","","","","", null));
            loadAlbums.execute(String.valueOf(page));
        } else {
            errr_msg = getString(R.string.err_internet_not_conn);
            setEmpty();
        }
    }

    private void setAdapter() {
        if (!isScroll) {
            adapterAlbums = new AdapterAlbums(getActivity(), arrayList, true);
            rv.setAdapter(adapterAlbums);
            setEmpty();
        } else {
            adapterAlbums.notifyDataSetChanged();
        }
    }

    public void setEmpty() {
        if (arrayList.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = null;
            if (errr_msg.equals(getString(R.string.err_no_albums_found))) {
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            } else if (errr_msg.equals(getString(R.string.err_internet_not_conn))) {
                myView = inflater.inflate(R.layout.layout_err_internet, null);
            } else if (errr_msg.equals(getString(R.string.err_server))) {
                myView = inflater.inflate(R.layout.layout_err_server, null);
            }else {
                errr_msg = getString(R.string.err_no_albums_found);
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            }

            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(errr_msg);

            myView.findViewById(R.id.btn_empty_try).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadAlbums();
                }
            });


            frameLayout.addView(myView);
        }
    }

    @Override
    public void onBackPressed() {
       getActivity().onBackPressed();
    }
}
