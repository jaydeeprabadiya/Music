package thiva.tamilaudiopro.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import thiva.tamilaudiopro.Activity.BaseActivity;
import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Activity.SongByCatActivity;
import thiva.tamilaudiopro.Listener.IOnBackPressed;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.EndlessRecyclerViewScrollListener;
import thiva.tamilaudiopro.Adapter.AdapterAlbums;
import thiva.tamilaudiopro.Utils.NavigationUtil;
import thiva.tamilaudiopro.asyncTask.LoadAlbums;
import thiva.tamilaudiopro.Listener.AlbumsListener;
import thiva.tamilaudiopro.Listener.InterAdListener;
import thiva.tamilaudiopro.item.ItemAlbums;
import thiva.tamilaudiopro.Utils.RecyclerItemClickListener;
import thiva.tamilaudiopro.item.ItemName;

import static thiva.tamilaudiopro.Activity.BaseActivity.mLayout;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class FragmentAlbums2 extends Fragment{

    private Methods methods;
    private RecyclerView rv;
    private AdapterAlbums adapterAlbums;
    private ArrayList<ItemAlbums> arrayList;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private String errr_msg, name;
    private GridLayoutManager glm_banner;
    private int page = 1;
    private Boolean isOver = false, isScroll = false;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);

        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                NavigationUtil.SongByCatActivity(getActivity(), getString(R.string.albums), adapterAlbums.getItem(position).getId(),
                        adapterAlbums.getItem(position).getName(), adapterAlbums.getItem(position).getImage());

            }
        });

        LinearLayout adView = rootView.findViewById(R.id.album_adView);
        methods.showBannerAd(adView);

        arrayList = new ArrayList<>();

        progressBar = rootView.findViewById(R.id.pb_albums);
        frameLayout = rootView.findViewById(R.id.fl_empty);

        rv = rootView.findViewById(R.id.rv_albums);
        glm_banner = new GridLayoutManager(getActivity(), 2);
        glm_banner.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapterAlbums.isHeader(position) ? glm_banner.getSpanCount() : 1;
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

        loadAlbums();

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

        name = getArguments().getString("name");
        setHasOptionsMenu(true);
        return rootView;
    }

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
            }, methods.getAPIRequest(ItemName.METHOD_ALBUM_CAT_ID, page, "", "", "", "", getArguments().getString("id"), "", "", "","","","","","","","", null));
            loadAlbums.execute(String.valueOf(page));
        } else {
            errr_msg = getString(R.string.err_internet_not_conn);
            setEmpty();
        }
    }

    private void setAdapter() {
        if (!isScroll) {
            if (Setting.itemNemosofts.getPurchase_code().equals(Setting.purchase_code)){
                adapterAlbums = new AdapterAlbums(getActivity(), arrayList, true);
                rv.setAdapter(adapterAlbums);
                setEmpty();
            }
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


//    @Override
//    public void onBackPressed() {
//        getActivity().onBackPressed();
//    }
}

