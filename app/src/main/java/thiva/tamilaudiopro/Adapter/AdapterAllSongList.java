package thiva.tamilaudiopro.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import thiva.tamilaudiopro.Activity.PlayerService;
import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Listener.ClickListenerPlayList;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.DBHelper;
import thiva.tamilaudiopro.Utils.GlobalBus;
import thiva.tamilaudiopro.item.ItemMyPlayList;
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

public class AdapterAllSongList extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<ItemSong> arrayList;
    private ArrayList<ItemSong> filteredArrayList;
    private ClickListenerPlayList recyclerClickListener;
    private NameFilter filter;
    private String type;
    private Methods methods;
    private DBHelper dbHelper;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterAllSongList(Context context, ArrayList<ItemSong> arrayList, ClickListenerPlayList recyclerClickListener, String type) {
        this.arrayList = arrayList;
        this.filteredArrayList = arrayList;
        this.context = context;
        this.type = type;
        this.recyclerClickListener = recyclerClickListener;
        methods = new Methods(context);
        dbHelper = new DBHelper(context);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_song, textView_duration, textView_catname, tv_avg_rate, tv_views, tv_download;
        EqualizerView equalizer;
        ImageView imageView, imageView_option, iv_downlaod_icon, imageView_download;
        RelativeLayout rl, linearLayout;
        RatingBar ratingBar;

        MyViewHolder(View view) {
            super(view);

            rl = view.findViewById(R.id.ll_songlist);
            linearLayout = view.findViewById(R.id.thiva);
            tv_views = view.findViewById(R.id.tv_songlist_views);
            tv_download = view.findViewById(R.id.tv_songlist_downloads);
            textView_song = view.findViewById(R.id.tv_songlist_name);
            textView_duration = view.findViewById(R.id.tv_songlist_duration);
            tv_avg_rate = view.findViewById(R.id.tv_songlist_avg_rate);
            equalizer = view.findViewById(R.id.equalizer_view);
            textView_catname = view.findViewById(R.id.tv_songlist_cat);
            imageView = view.findViewById(R.id.iv_songlist);
            imageView_option = view.findViewById(R.id.iv_songlist_option);
            ratingBar = view.findViewById(R.id.rb_songlist);
            iv_downlaod_icon = view.findViewById(R.id.iv_downlaod_icon);
            imageView_download = view.findViewById(R.id.imageView_download);

            if(!Setting.isDownload) {
                tv_download.setVisibility(View.GONE);
                iv_downlaod_icon.setVisibility(View.GONE);
            }
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_songlist, parent, false);
            return new MyViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
           ((MyViewHolder) holder).tv_views.setText(methods.format(Double.parseDouble(arrayList.get(position).getViews())));
            ((MyViewHolder) holder).tv_download.setText(methods.format(Double.parseDouble(arrayList.get(position).getDownloads())));
            ((MyViewHolder) holder).textView_song.setText(arrayList.get(position).getTitle());
            ((MyViewHolder) holder).textView_duration.setText(arrayList.get(position).getDuration());

            Picasso.get()
                    .load(arrayList.get(position).getImageSmall())
                    .placeholder(R.drawable.songs)
                    .into(((MyViewHolder) holder).imageView);

            try {
                Picasso.get()
                        .load(arrayList.get(position).getImageSmall())
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                assert((MyViewHolder) holder).imageView != null;
                                ((MyViewHolder) holder).imageView.setImageBitmap(bitmap);
                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                if (textSwatch == null) {
                                                    return;
                                                }
                                                ((MyViewHolder) holder).textView_duration.setBackgroundColor(textSwatch.getRgb());
                                                ((MyViewHolder) holder).textView_duration.setTextColor(textSwatch.getBodyTextColor());
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

            ((MyViewHolder) holder).tv_avg_rate.setTypeface(((MyViewHolder) holder).tv_avg_rate.getTypeface(), Typeface.BOLD);
            ((MyViewHolder) holder).tv_avg_rate.setText(arrayList.get(position).getAverageRating());
            ((MyViewHolder) holder).ratingBar.setRating(Float.parseFloat(arrayList.get(position).getAverageRating()));

            if (PlayerService.getIsPlayling() && Setting.arrayList_play.get(Setting.playPos).getId().equals(arrayList.get(position).getId())) {
                ((MyViewHolder) holder).equalizer.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).equalizer.animateBars();

                if (Setting.Dark_Mode) {
                    ((MyViewHolder) holder).linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.tt_dr));
                    ((MyViewHolder) holder).textView_song.setTextColor(ContextCompat.getColor(context, R.color.tt_title_dr));
                } else {
                    ((MyViewHolder) holder).linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.tt));
                    ((MyViewHolder) holder).textView_song.setTextColor(ContextCompat.getColor(context, R.color.tt_title));
                }

            } else {
                ((MyViewHolder) holder).equalizer.setVisibility(View.GONE);
                ((MyViewHolder) holder).equalizer.stopBars();
                if (Setting.Dark_Mode) {
                    ((MyViewHolder) holder).linearLayout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                    ((MyViewHolder) holder).textView_song.setTextColor(ContextCompat.getColor(context, R.color.tt_title_normal_dr));
                } else {
                    ((MyViewHolder) holder).linearLayout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                    ((MyViewHolder) holder).textView_song.setTextColor(ContextCompat.getColor(context, R.color.tt_title_normal));
                }
            }

            if (arrayList.get(position).getCatName() != null) {
                ((MyViewHolder) holder).textView_catname.setText(arrayList.get(position).getCatName());
            } else {
                ((MyViewHolder) holder).textView_catname.setText(arrayList.get(position).getArtist());
            }

            ((MyViewHolder) holder).rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(arrayList.size() > holder.getAdapterPosition()) {
                        recyclerClickListener.onClick(getPosition(arrayList.get(holder.getAdapterPosition()).getId()));
                    }
                }
            });

            ((MyViewHolder) holder).imageView_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openOptionPopUp(((MyViewHolder) holder).imageView_option, holder.getAdapterPosition());
                }
            });

            ((MyViewHolder) holder).imageView_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Setting.isDownload){
                        methods.download(arrayList.get(position));
                        if (Setting.Dark_Mode) {
                            ((MyViewHolder) holder).imageView_download.setImageResource(R.drawable.download_on_2);
                        } else {
                            ((MyViewHolder) holder).imageView_download.setImageResource(R.drawable.download_on);
                        }
                    }
                }
            });

            if (!dbHelper.checkDownload(arrayList.get(position).getId())) {
                if (Setting.Dark_Mode) {
                    ((MyViewHolder) holder).imageView_download.setImageResource(R.drawable.download_off_2);
                } else {
                    ((MyViewHolder) holder).imageView_download.setImageResource(R.drawable.download_off);
                }
            } else {
                if (Setting.Dark_Mode) {
                    ((MyViewHolder) holder).imageView_download.setImageResource(R.drawable.download_on_2);
                } else {
                    ((MyViewHolder) holder).imageView_download.setImageResource(R.drawable.download_on);
                }
            }
        } else {
            if (getItemCount() >= 1) {
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
        return arrayList.size()+1;
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

    private int getPosition(String id) {
        int count=0;
        for(int i=0;i<filteredArrayList.size();i++) {
            if(id.equals(filteredArrayList.get(i).getId())) {
                count = i;
                break;
            }
        }
        return count;
    }

    private void openOptionPopUp(ImageView imageView, final int pos) {
        Context wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE);
        PopupMenu popup = new PopupMenu(wrapper, imageView);
        popup.getMenuInflater().inflate(R.menu.popup_song, popup.getMenu());
        if (type.equals("playlist")) {
            popup.getMenu().findItem(R.id.popup_add_song).setTitle(context.getString(R.string.remove));
        }
        if (!Setting.isOnline) {
            popup.getMenu().findItem(R.id.popup_add_queue).setVisible(false);
        }
        if(!methods.isYoutubeAppInstalled()) {
            popup.getMenu().findItem(R.id.popup_youtube).setVisible(false);
        }
        if(!Setting.isDownload) {
            popup.getMenu().findItem(R.id.popup_download).setVisible(false);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_add_song:
                        switch (type) {
                            case "playlist":
                                dbHelper.removeFromPlayList(arrayList.get(pos).getId(), true);
                                arrayList.remove(pos);
                                notifyItemRemoved(pos);
                                Toast.makeText(context, context.getString(R.string.remove_from_playlist), Toast.LENGTH_SHORT).show();
                                if (arrayList.size() == 0) {
                                    recyclerClickListener.onItemZero();
                                }
                                break;
                            default:
                                methods.openPlaylists(arrayList.get(pos), true);
                                break;
                        }
                        break;
                    case R.id.popup_add_queue:
                        Setting.arrayList_play.add(arrayList.get(pos));
                        GlobalBus.getBus().postSticky(new ItemMyPlayList("","",null));
                        Toast.makeText(context, context.getString(R.string.add_to_queue), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.popup_youtube:
                        String urlString = "https://www.youtube.com/channel/UCRSvNS1DL5IzluMaRdZLFug/search?query="  + Setting.arrayList_play.get(pos).getTitle();
                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.android.chrome");
                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            // Chrome browser presumably not installed so allow user to choose instead
                            intent.setPackage(null);
                            context.startActivity(intent);
                        }
                        break;
                    case R.id.popup_share:
                        methods.shareSong(arrayList.get(pos), true);
                        break;
                    case R.id.popup_download:
                        methods.download(arrayList.get(pos));
                        break;
                }
                return true;
            }
        });
        popup.show();
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
                    String nameList = filteredArrayList.get(i).getTitle();
                    if (nameList.toLowerCase().contains(constraint))
                        filteredItems.add(filteredArrayList.get(i));
                }
                result.count = filteredItems.size()+1;
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = filteredArrayList;
                    result.count = filteredArrayList.size()+1;
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