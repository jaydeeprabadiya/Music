package thiva.tamilaudiopro.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import thiva.tamilaudiopro.Activity.PlayerService;
import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Listener.RecyclerClickListener;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.item.ItemSong;
import thiva.tamilaudiopro.views.Mini_Equalizer.EqualizerView;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class AdapterSongList_Home extends RecyclerView.Adapter<AdapterSongList_Home.MyViewHolder> {

    private Context context;
    private ArrayList<ItemSong> arrayList;
    private ArrayList<ItemSong> filteredArrayList;
    private RecyclerClickListener recyclerClickListener;
    private NameFilter filter;
    private String type;
    private Methods jsonUtils;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_song, textView_duration, textView_catname,  textView_views, textView_downloads;
        EqualizerView equalizer;
        ImageView imageView, imageView2, bg, home_playe;
        LinearLayout linearLayout, ll_counts;

        MyViewHolder(View view) {
            super(view);
            linearLayout = view.findViewById(R.id.ll_songlist);
            ll_counts = view.findViewById(R.id.ll_counts);
            textView_song = view.findViewById(R.id.textView_songname);
            textView_duration = view.findViewById(R.id.textView_songduration);
            equalizer = view.findViewById(R.id.equalizer_view);
            textView_catname = view.findViewById(R.id.textView_catname);
            imageView = view.findViewById(R.id.imageView_songlist);
            textView_views = view.findViewById(R.id.textView_views);
            textView_downloads = view.findViewById(R.id.textView_downloads);
            home_playe = view.findViewById(R.id.home_playe);
            imageView2 = view.findViewById(R.id.imageView_songlist2);
            bg = view.findViewById(R.id.bg);
        }
    }

    public AdapterSongList_Home(Context context, ArrayList<ItemSong> arrayList, RecyclerClickListener recyclerClickListener, String type) {
        this.arrayList = arrayList;
        this.filteredArrayList = arrayList;
        this.context = context;
        this.type = type;
        this.recyclerClickListener = recyclerClickListener;
        jsonUtils = new Methods(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_songlist_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.textView_song.setText(arrayList.get(position).getTitle());
        holder.textView_duration.setText(arrayList.get(position).getDuration());

        if (!type.equals("offline")) {
            holder.textView_views.setText(jsonUtils.format(Double.parseDouble(arrayList.get(position).getViews())));
            holder.textView_downloads.setText(jsonUtils.format(Double.parseDouble(arrayList.get(position).getDownloads())));
        } else {
            holder.ll_counts.setVisibility(View.GONE);
        }

        if (!type.equals("offline") || type.equals("fav")) {

            Picasso.get()
                    .load(arrayList.get(position).getImageBig())
                    .placeholder(R.drawable.home_songs)
                    .into(holder.imageView2);

            if (Setting.songs_color){
                try {
                    Picasso.get()
                            .load(arrayList.get(position).getImageSmall())
                            .centerCrop()
                            .resize(280, 175)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    assert holder.imageView != null;
                                    holder.imageView.setImageBitmap(bitmap);
                                    Palette.from(bitmap)
                                            .generate(new Palette.PaletteAsyncListener() {
                                                @Override
                                                public void onGenerated(Palette palette) {
                                                    Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                    if (textSwatch == null) {
                                                        return;
                                                    }
                                                    holder.textView_song.setTextColor(textSwatch.getTitleTextColor());
                                                    holder.textView_catname.setTextColor(textSwatch.getTitleTextColor());
                                                    holder.bg.setBackgroundTintList(ColorStateList.valueOf(textSwatch.getRgb()));
                                                }
                                            });
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }
                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            holder.imageView.setImageBitmap(arrayList.get(position).getBitmap());
        }

        if (PlayerService.getIsPlayling() && Setting.arrayList_play.get(Setting.playPos).getId().equals(arrayList.get(position).getId())) {
            holder.equalizer.animateBars();
            holder.equalizer.setVisibility(View.VISIBLE);
            holder.home_playe.setImageResource(R.drawable.ic_pause_white_24dp);
        } else {
            holder.equalizer.stopBars();
            holder.equalizer.setVisibility(View.GONE);
            holder.home_playe.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }

        if (arrayList.get(position).getCatName() != null) {
            holder.textView_catname.setText(arrayList.get(position).getCatName());
        } else {
            holder.textView_catname.setText(arrayList.get(position).getArtist());
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerClickListener.onClick(holder.getAdapterPosition());
            }
        });

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
                ArrayList<ItemSong> filteredItems = new ArrayList<>();

                for (int i = 0, l = filteredArrayList.size(); i < l; i++) {
                    String nameList = filteredArrayList.get(i).getTempName();
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
            arrayList = (ArrayList<ItemSong>) results.values;
            notifyDataSetChanged();
        }
    }
}