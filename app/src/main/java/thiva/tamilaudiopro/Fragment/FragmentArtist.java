package thiva.tamilaudiopro.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import thiva.tamilaudiopro.Listener.IOnBackPressed;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

//import net.skoumal.fragmentback.BackFragment;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class FragmentArtist extends Fragment  {

    private Methods methods;
    private RecyclerView rv;
    private AdapterArtist adapterArtist;
    private ArrayList<ItemArtist> arrayList;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private GridLayoutManager glm_banner;
    private SearchView searchView;
    private String errr_msg;
    private int page = 1;
    private Boolean isOver = false, isScroll = false;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                FragmentAlbumsByArtist f_alb = new FragmentAlbumsByArtist();
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", arrayList.get(position));
                f_alb.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.hide(getFragmentManager().getFragments().get(getFragmentManager().getBackStackEntryCount()));
                ft.replace(R.id.fragment, f_alb, getString(R.string.albums));
                ft.addToBackStack(getString(R.string.artist));
                ft.commit();
                //getFragmentManager().popBackStack();


                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.albums));
            }
        });

        LinearLayout adView = rootView.findViewById(R.id.cat_adView);
        methods.showBannerAd(adView);

        arrayList = new ArrayList<>();

        progressBar = rootView.findViewById(R.id.pb_cat);
        frameLayout = rootView.findViewById(R.id.fl_empty);

        rv = rootView.findViewById(R.id.rv_cat);
        glm_banner = new GridLayoutManager(getActivity(),3);
        glm_banner.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapterArtist.isHeader(position) ? glm_banner.getSpanCount() : 1;
            }
        });

        rv.setLayoutManager(glm_banner);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                methods.showInterAd(position,"");
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
                            loadArtists();
                        }
                    }, 0);
                } else {
                    try {
                        adapterArtist.hideHeader();
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

        loadArtists();

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
//            Setting.search_item = s.replace(" ", "%20");
//            FragmentSearchArtist fsearch = new FragmentSearchArtist();
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft.hide(getFragmentManager().getFragments().get(getFragmentManager().getBackStackEntryCount()));
//            ft.add(R.id.fragment, fsearch, getString(R.string.search_artist));
//            ft.addToBackStack(getString(R.string.search_artist));
//            ft.commit();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (adapterArtist != null) {
                if (!searchView.isIconified()) {
                    adapterArtist.getFilter().filter(s);
                    try {
                        adapterArtist.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            return true;
        }
    };

    private void loadArtists() {
        if (methods.isNetworkAvailable()) {
            LoadArtist loadArtist = new LoadArtist(new ArtistListener() {
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
                public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemArtist> arrayListArtist) {
                    if (getActivity() != null) {
                        if (success.equals("1")) {
                            if (!verifyStatus.equals("-1")) {
                                if (arrayListArtist.size() == 0) {
                                    isOver = true;
                                    try {
                                        adapterArtist.hideHeader();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    errr_msg = getString(R.string.err_no_songs_found);
                                    setEmpty();
                                } else {
                                    page = page + 1;
                                    arrayList.addAll(arrayListArtist);
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
            }, methods.getAPIRequest(ItemName.METHOD_ARTIST, page, "", "", "", "", "", "", "", "","","","","","","","", null));
            loadArtist.execute(String.valueOf(page));
        } else {
            errr_msg = getString(R.string.err_internet_not_conn);
            setEmpty();
        }
    }

    private void setAdapter() {
        if (!isScroll) {
            adapterArtist = new AdapterArtist(getActivity(), arrayList);
            rv.setAdapter(adapterArtist);
            setEmpty();
        } else {
            adapterArtist.notifyDataSetChanged();
        }
    }



    public void setEmpty() {
        if(arrayList.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = null;
            if(errr_msg.equals(getString(R.string.err_no_artist_found))) {
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            } else if(errr_msg.equals(getString(R.string.err_internet_not_conn))) {
                myView = inflater.inflate(R.layout.layout_err_internet, null);
            } else if(errr_msg.equals(getString(R.string.err_server))){
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
                    loadArtists();
                }
            });

            frameLayout.addView(myView);
        }
    }

//    @Override
//    public boolean onBackPressed() {
//        return true;
//    }
//@Override
//public void onBackPressed() {
//    //on Back Pressed
//   //getFragmentManager().popBackStack();
//}
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }
//
//    @Override
//    public boolean onBackPressed() {
//        return true;
//    }
//
//    @Override
//    public int getBackPriority() {
//        return 100;
//    }
}