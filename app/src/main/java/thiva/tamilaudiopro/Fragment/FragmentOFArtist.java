package thiva.tamilaudiopro.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;

import thiva.tamilaudiopro.Adapter.AdapterOFArtist;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.Listener.InterAdListener;
import thiva.tamilaudiopro.Utils.KeyboardUtils;
import thiva.tamilaudiopro.Utils.NavigationUtil;
import thiva.tamilaudiopro.item.ItemArtist;
import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Activity.SongByOfflineActivity;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.RecyclerItemClickListener;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class FragmentOFArtist extends Fragment {

    private Methods methods;
    private RecyclerView rv;
    private AdapterOFArtist adapterArtist;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private String errr_msg = "";
    private SearchView searchView;
    private Boolean isLoaded = false;

    public static FragmentOFArtist newInstance(int sectionNumber) {
        return new FragmentOFArtist();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                NavigationUtil.SongByOfflineActivity(getActivity(), getString(R.string.artist), adapterArtist.getItem(position).getId(),
                        adapterArtist.getItem(position).getName());
                hideKeyboard();
            }
        });
        errr_msg = getString(R.string.err_no_artist_found);

        progressBar = rootView.findViewById(R.id.pb_cat);
        frameLayout = rootView.findViewById(R.id.fl_empty);

        rv = rootView.findViewById(R.id.rv_cat);
        GridLayoutManager glm_banner = new GridLayoutManager(getActivity(), 3);
        rv.setLayoutManager(glm_banner);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                hideKeyboard();
                methods.showInterAd(position, "");
            }
        }));

        setHasOptionsMenu(true);
        return rootView;
    }

    private void hideKeyboard() {
        KeyboardUtils.hideKeyboardsearchview(searchView);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
        super.onCreateOptionsMenu(menu, inflater);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
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



    class LoadOfflineArtist extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            frameLayout.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (Setting.arrayListOfflineArtist.size() == 0) {
                getListOfArtist();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (getActivity() != null) {
                setAdapter();
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void getListOfArtist() {
        try {
            String where = null;

            final Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
            final String id = MediaStore.Audio.Artists._ID;
            final String artist_name = MediaStore.Audio.Artists.ARTIST;

            final String[] columns = {id, artist_name};
            Cursor cursor = getActivity().getContentResolver().query(uri, columns, where, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String aid = String.valueOf(cursor.getLong(cursor.getColumnIndex(id)));
                    String name = cursor.getString(cursor.getColumnIndex(artist_name));
                    String image = "null";
                    Setting.arrayListOfflineArtist.add(new ItemArtist(aid, name, image, image));

                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        if (adapterArtist == null) {
            adapterArtist = new AdapterOFArtist(getActivity(), Setting.arrayListOfflineArtist);
            rv.setAdapter(adapterArtist);
            isLoaded = true;
        }
        setEmpty();
    }

    public void setEmpty() {
        if (Setting.arrayListOfflineArtist.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = inflater.inflate(R.layout.layout_err_nodata, null);

            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(errr_msg);
            myView.findViewById(R.id.btn_empty_try).setVisibility(View.GONE);

            frameLayout.addView(myView);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && rv != null) {
            if (!isLoaded) {
                new LoadOfflineArtist().execute();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}