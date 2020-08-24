package thiva.tamilaudiopro.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import thiva.tamilaudiopro.Activity.MainActivity;
import thiva.tamilaudiopro.Listener.IOnBackPressed;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.Adapter.AdapterCat;
import thiva.tamilaudiopro.asyncTask.LoadCat;
import thiva.tamilaudiopro.Listener.CatListener;
import thiva.tamilaudiopro.Listener.InterAdListener;
import thiva.tamilaudiopro.item.ItemCat;
import thiva.tamilaudiopro.Activity.R;
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

public class FragmentCategories extends Fragment{
    private Methods methods;
    private RecyclerView rv;
    private AdapterCat adapterCat;
    private ArrayList<ItemCat> arrayList;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private GridLayoutManager glm_banner;
    private Boolean isLoading = false;
    private String errr_msg;
    private SearchView searchView;
    private int page = 1;
    private Boolean isOver = false, isScroll = false;
    private FragmentManager fm;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        fm = getFragmentManager();

        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                FragmentManager fm = getFragmentManager();
                FragmentAlbums2 f1 = new FragmentAlbums2();




                if (!getResources().getString(R.string.albums).equals(getString(R.string.search))) {
                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                }

                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                String name = getResources().getString(R.string.albums);

                Bundle bundl = new Bundle();
                bundl.putString("type", getString(R.string.albums));
                bundl.putString("id", arrayList.get(position).getId());
                bundl.putString("name", arrayList.get(position).getName());
                bundl.putString("img", arrayList.get(position).getImage());
                f1.setArguments(bundl);


                ft.hide(getFragmentManager().getFragments().get(getFragmentManager().getBackStackEntryCount()));


               // ft.hide(fm.getFragments().get(fm.getBackStackEntryCount()));
                ft.replace(R.id.fragment, f1, name);
                //ft.addToBackStack(null);
                ft.commit();

//                if (name.equals(getString(R.string.search))) {
//
//                } else {
//                    //ft.replace(R.id.fragment, f1,name);
//                    //ft.addToBackStack(null);
//                    ft.commit();
//                }


                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.albums));

            }
        });

        LinearLayout adView = rootView.findViewById(R.id.cat_adView);
        methods.showBannerAd(adView);

        arrayList = new ArrayList<>();

        progressBar = rootView.findViewById(R.id.pb_cat);
        frameLayout = rootView.findViewById(R.id.fl_empty);

        rv = rootView.findViewById(R.id.rv_cat);
        glm_banner = new GridLayoutManager(getActivity(), 3);
        glm_banner.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapterCat.isHeader(position) ? glm_banner.getSpanCount() : 1;
            }
        });

        rv.setLayoutManager(glm_banner);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

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
                            loadCategories();
                        }
                    }, 0);
                } else {
                    try {
                        adapterCat.hideHeader();
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

        loadCategories();

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
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (adapterCat != null) {
                if (!searchView.isIconified()) {
                    adapterCat.getFilter().filter(s);
                    try {
                        adapterCat.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            return true;
        }
    };

    private void loadCategories() {
        if (methods.isNetworkAvailable()) {
            LoadCat loadCat = new LoadCat(new CatListener() {
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
                public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemCat> arrayListCat) {
                    if (getActivity() != null) {
                        if (success.equals("1")) {
                            if (!verifyStatus.equals("-1")) {
                                if (arrayListCat.size() == 0) {
                                    isOver = true;
                                    try {
                                        adapterCat.hideHeader();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    errr_msg = getString(R.string.err_no_songs_found);
                                    setEmpty();
                                } else {
                                    page = page + 1;
                                    arrayList.addAll(arrayListCat);
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
                        isLoading = false;
                    }
                }
            }, methods.getAPIRequest(ItemName.METHOD_CAT, page, "", "", "", "", "", "", "", "","","","","","","","", null));
            loadCat.execute(String.valueOf(page));
        } else {
            errr_msg = getString(R.string.err_internet_not_conn);
            setEmpty();
        }
    }

    private void setAdapter() {
        if (!isScroll) {
            adapterCat = new AdapterCat(getActivity(), arrayList);
            rv.setAdapter(adapterCat);
            setEmpty();
        } else {
            adapterCat.notifyDataSetChanged();
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
            if (errr_msg.equals(getString(R.string.err_no_cat_found))) {
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            } else if (errr_msg.equals(getString(R.string.err_internet_not_conn))) {
                myView = inflater.inflate(R.layout.layout_err_internet, null);
            } else if (errr_msg.equals(getString(R.string.err_server))) {
                myView = inflater.inflate(R.layout.layout_err_server, null);
            }else {
                errr_msg = getString(R.string.err_no_cat_found);
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            }

            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(errr_msg);

            myView.findViewById(R.id.btn_empty_try).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadCategories();
                }
            });


            frameLayout.addView(myView);
        }
    }

//    @Override
//    public boolean onBackPressed() {
//        return false;
//    }
}