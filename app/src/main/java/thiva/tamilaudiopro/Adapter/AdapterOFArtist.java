package thiva.tamilaudiopro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.item.ItemArtist;
import thiva.tamilaudiopro.views.CircularImageView;
import thiva.tamilaudiopro.views.WidthFitSquareLayout;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class AdapterOFArtist extends RecyclerView.Adapter {

    private ArrayList<ItemArtist> arrayList;
    private ArrayList<ItemArtist> filteredArrayList;
    private NameFilter filter;
    private int columnWidth = 0;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterOFArtist(Context context, ArrayList<ItemArtist> arrayList) {
        this.arrayList = arrayList;
        this.filteredArrayList = arrayList;
        Methods methods = new Methods(context);
        columnWidth = methods.getColumnWidth(3, 20);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CircularImageView imageView;
        WidthFitSquareLayout widthFitSquareLayout;
        LinearLayout ll;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tv_cat);
            imageView = view.findViewById(R.id.iv_cat);
            widthFitSquareLayout = view.findViewById(R.id.cv_cat);
            ll = view.findViewById(R.id.ll);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
            //progressBar.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_art, parent, false);
            return new MyViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            ((MyViewHolder) holder).imageView.setLayoutParams(new FrameLayout.LayoutParams(columnWidth, columnWidth));
            ((MyViewHolder) holder).widthFitSquareLayout.setLayoutParams(new LinearLayout.LayoutParams(columnWidth, columnWidth));
            ((MyViewHolder) holder).textView.setText(arrayList.get(position).getName());
            ((MyViewHolder) holder).imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get()
                    .load(arrayList.get(position).getImage())
                    .placeholder(R.drawable.music_directors)
                    .into(((MyViewHolder) holder).imageView);
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

    public ItemArtist getItem(int pos) {
        return arrayList.get(pos);
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
                ArrayList<ItemArtist> filteredItems = new ArrayList<>();

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
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            arrayList = (ArrayList<ItemArtist>) results.values;
            notifyDataSetChanged();
        }
    }
}