package thiva.tamilaudiopro.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.Adapter.AdapterArtist;
import thiva.tamilaudiopro.asyncTask.LoadArtist;
import thiva.tamilaudiopro.Listener.ArtistListener;
import thiva.tamilaudiopro.Listener.InterAdListener;
import thiva.tamilaudiopro.item.ItemArtist;
import thiva.tamilaudiopro.Activity.MainActivity;
import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.EndlessRecyclerViewScrollListener;
import thiva.tamilaudiopro.Utils.RecyclerItemClickListener;
import thiva.tamilaudiopro.item.ItemName;

import java.util.ArrayList;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class FragmentSearchArtist extends Fragment {

    private Methods methods;
    private RecyclerView rv;
    private AdapterArtist adapter;
    private ArrayList<ItemArtist> arrayList_artist;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private String errr_msg;
    private int page = 1;
    private Boolean isOver = false, isScroll = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                FragmentAlbumsByArtist f_alb = new FragmentAlbumsByArtist();
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", arrayList_artist.get(position));
                f_alb.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.hide(getFragmentManager().getFragments().get(getFragmentManager().getBackStackEntryCount()));
                ft.add(R.id.fragment, f_alb, getString(R.string.albums));
                //ft.addToBackStack(getString(R.string.albums));
                ft.commit();
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.albums));
            }
        });

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.search_artist));

        arrayList_artist = new ArrayList<>();

        frameLayout = rootView.findViewById(R.id.fl_empty);
        progressBar = rootView.findViewById(R.id.pb_cat);
        rv = rootView.findViewById(R.id.rv_cat);
        GridLayoutManager llm_banner = new GridLayoutManager(getActivity(),3);
        rv.setLayoutManager(llm_banner);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(llm_banner) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScroll = true;
                            loadArtist();
                        }
                    }, 0);
                } else {
                    try {
                        adapter.hideHeader();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                methods.showInterAd(position, "");
            }
        }));

        loadArtist();

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
            if (methods.isNetworkAvailable()) {
                page = 1;
                isScroll = false;
                Setting.search_item = s.replace(" ", "%20");
                arrayList_artist.clear();
                if (adapter != null) {
                    try {
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                loadArtist();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    private void loadArtist() {
        if (methods.isNetworkAvailable()) {
            LoadArtist loadArtist = new LoadArtist(new ArtistListener() {
                @Override
                public void onStart() {
                    if (arrayList_artist.size() == 0) {
                        arrayList_artist.clear();
                        frameLayout.setVisibility(View.GONE);
                        rv.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemArtist> arrayListArtist) {
                    if (getActivity() != null) {
                        if (success.equals("1")) {
                            if (!verifyStatus.equals("-1")) {
                                if (arrayListArtist.size() == 0) {
                                    isOver = true;
                                    try {
                                        adapter.hideHeader();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    errr_msg = getString(R.string.err_no_songs_found);
                                    setEmpty();
                                } else {
                                    page = page + 1;
                                    arrayList_artist.addAll(arrayListArtist);
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
            }, methods.getAPIRequest(ItemName.METHOD_SEARCH, page, "", "", Setting.search_item, "artist", "", "", "", "", "", "", "", "", "", "", "", null));
            loadArtist.execute();

        } else {
            errr_msg = getString(R.string.err_internet_not_conn);
            setEmpty();
        }
    }

    private void setAdapter() {
        if (!isScroll) {
            adapter = new AdapterArtist(getActivity(), arrayList_artist);
            rv.setAdapter(adapter);
            setEmpty();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void setEmpty() {
        if (arrayList_artist.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = null;
            if (errr_msg.equals(getString(R.string.err_no_artist_found))) {
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            } else if (errr_msg.equals(getString(R.string.err_internet_not_conn))) {
                myView = inflater.inflate(R.layout.layout_err_internet, null);
            } else if (errr_msg.equals(getString(R.string.err_server))) {
                myView = inflater.inflate(R.layout.layout_err_server, null);
            }else {
                errr_msg = getString(R.string.err_no_artist_found);
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            }

            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(errr_msg);

            myView.findViewById(R.id.btn_empty_try).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadArtist();
                }
            });

            frameLayout.addView(myView);
        }
    }
}