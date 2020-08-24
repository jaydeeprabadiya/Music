package thiva.tamilaudiopro.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.item.ItemAlbums;
import thiva.tamilaudiopro.item.ItemServerPlayList;
import thiva.tamilaudiopro.views.Roundedimageview.RoundedImageView;

public class AdapterPlaylist_Home extends RecyclerView.Adapter<AdapterPlaylist_Home.MyViewHolder> {
    private Context context;
    private ArrayList<ItemServerPlayList> arrayList;
    private ArrayList<ItemServerPlayList> filteredArrayList;
    private AdapterPlaylist_Home.NameFilter filter;
    private Methods methods;
    private int columnWidth = 0;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView bg;
        RoundedImageView imageView;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView_artist_name);
            imageView = view.findViewById(R.id.imageView_artist);
            bg = view.findViewById(R.id.bg);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    public AdapterPlaylist_Home(Context context, ArrayList<ItemServerPlayList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.filteredArrayList = arrayList;
        methods = new Methods(context);
    }

    @NonNull
    @Override
    public AdapterPlaylist_Home.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album_home_card, parent, false);

        return new AdapterPlaylist_Home.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterPlaylist_Home.MyViewHolder holder, final int position) {
        holder.textView.setText(arrayList.get(position).getName());
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.get()
                .load(arrayList.get(position).getThumb())
                .placeholder(R.drawable.album)
                .into(holder.imageView);

        if (Setting.album_color){
            new AdapterPlaylist_Home.LoadColor(holder.bg).execute(arrayList.get(position).getImage());
        }
    }

    public class LoadColor extends AsyncTask<String, String, String> {

        Bitmap bitmap;
        View view;

        LoadColor(View view) {
            this.view = view;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                bitmap = methods.getBitmapFromURL(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@NonNull Palette palette) {
                        int defaultValue = 0x000000;
                        int vibrant = palette.getVibrantColor(defaultValue);
                        view.setBackground(methods.getGradientDrawable(vibrant, Color.parseColor("#00000000")));
                    }
                });
                super.onPostExecute(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public String getID(int pos) {
        return arrayList.get(pos).getId();
    }

    public void hideHeader() {
        AdapterPlaylist_Home.ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }

    public boolean isHeader(int position) {
        return position == arrayList.size();
    }

    public Filter getFilter() {
        if (filter == null) {
            filter = new AdapterPlaylist_Home.NameFilter();
        }
        return filter;
    }

    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<ItemServerPlayList> filteredItems = new ArrayList<>();

                for (int i = 0, l = filteredArrayList.size(); i < l; i++) {
                    String nameList = filteredArrayList.get(i).getName();
                    if (nameList.toLowerCase().contains(constraint))
                        filteredItems.add(filteredArrayList.get(i));
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = filteredArrayList;
                    result.count = filteredArrayList.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            arrayList = (ArrayList<ItemServerPlayList>) results.values;
            notifyDataSetChanged();
        }
    }
}
