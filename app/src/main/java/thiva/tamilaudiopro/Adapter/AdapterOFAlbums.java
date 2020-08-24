package thiva.tamilaudiopro.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.item.ItemAlbums;
import thiva.tamilaudiopro.views.Roundedimageview.RoundedImageView;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class AdapterOFAlbums extends RecyclerView.Adapter {

    private ArrayList<ItemAlbums> arrayList;
    private ArrayList<ItemAlbums> filteredArrayList;
    private NameFilter filter;
    private int columnWidth = 0;
    private Boolean isOnline;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterOFAlbums(Context context, ArrayList<ItemAlbums> arrayList, Boolean isOnline) {
        this.arrayList = arrayList;
        this.isOnline = isOnline;
        this.filteredArrayList = arrayList;
        Methods methods = new Methods(context);
        columnWidth = methods.getColumnWidth(2, 5);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_album, textView_artist;
        RoundedImageView imageView;

        MyViewHolder(View view) {
            super(view);
            textView_artist = view.findViewById(R.id.tv_album_artist);
            textView_album = view.findViewById(R.id.tv_album_name);
            imageView = view.findViewById(R.id.iv_albums);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView;
            switch (Setting.Album){
                case 0: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_albums_normal, parent, false);
                    break;
                case 1:itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_albums_card, parent, false);
                    break;
                case 2: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_albums, parent, false);
                    break;
                case 3: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_albums_image, parent, false);
                    break;
                default: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_albums, parent, false);
            }

            return new MyViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {

            ((MyViewHolder) holder).imageView.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth));
            ((MyViewHolder) holder).textView_album.setText(arrayList.get(position).getName());
            ((MyViewHolder) holder).imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get()
                    .load(arrayList.get(position).getImage())
                    .placeholder(R.drawable.album)
                    .into(((MyViewHolder) holder).imageView);

            ((MyViewHolder) holder).textView_album.setTypeface(((MyViewHolder) holder).textView_album.getTypeface(), Typeface.BOLD);
            if (!isOnline) {
                ((MyViewHolder) holder).textView_artist.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).textView_artist.setText(arrayList.get(holder.getAdapterPosition()).getArtist());
            }
        } else {
            if (getItemCount() == 1) {
                ProgressViewHolder.progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    public ItemAlbums getItem(int pos) {
        return arrayList.get(pos);
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    public boolean isHeader(int position) {
        return position == arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_PROG : VIEW_ITEM;
    }

    public void hideHeader() {
        try {
            ProgressViewHolder.progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Filter getFilter() {
        if (filter == null) {
            filter = new NameFilter();
        }
        return filter;
    }

    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<ItemAlbums> filteredItems = new ArrayList<>();

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

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            arrayList = (ArrayList<ItemAlbums>) results.values;
            notifyDataSetChanged();
        }
    }
}