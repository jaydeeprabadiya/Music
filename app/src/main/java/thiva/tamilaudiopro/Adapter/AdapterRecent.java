package thiva.tamilaudiopro.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thiva.tamilaudiopro.Activity.PlayerService;
import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.Listener.ClickListenerPlayList;
import thiva.tamilaudiopro.Methods.Methods;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.GlobalBus;
import thiva.tamilaudiopro.item.ItemMyPlayList;
import thiva.tamilaudiopro.item.ItemSong;
import thiva.tamilaudiopro.views.Mini_Equalizer.EqualizerView;
import thiva.tamilaudiopro.views.Roundedimageview.RoundedImageView;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class AdapterRecent extends RecyclerView.Adapter<AdapterRecent.MyViewHolder> {

    private Methods methods;
    private Context context;
    private ArrayList<ItemSong> arrayList;
    private ClickListenerPlayList clickListenerPlayList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView iv_song;
        ImageView iv_more, re_play;
        TextView tv_title, tv_cat;
        EqualizerView equalizer;

        MyViewHolder(View view) {
            super(view);
            iv_song = view.findViewById(R.id.iv_recent);
            iv_more = view.findViewById(R.id.iv_recent_more);
            tv_title = view.findViewById(R.id.tv_recent_song);
            tv_cat = view.findViewById(R.id.tv_recent_cat);
            equalizer = view.findViewById(R.id.equalizer_view);
            re_play = view.findViewById(R.id.re_play);
        }
    }

    public AdapterRecent(Context context, ArrayList<ItemSong> arrayList, ClickListenerPlayList clickListenerPlayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.clickListenerPlayList = clickListenerPlayList;
        methods = new Methods(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recent, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_cat.setText(arrayList.get(position).getCatName());
        Picasso.get()
                .load(arrayList.get(position).getImageSmall())
                .placeholder(R.drawable.songs)
                .into(holder.iv_song);

        if (PlayerService.getIsPlayling() && Setting.arrayList_play.get(Setting.playPos).getId().equals(arrayList.get(position).getId())) {
            holder.equalizer.setVisibility(View.VISIBLE);
            holder.re_play.setVisibility(View.GONE);
            holder.equalizer.animateBars();
        } else {
            holder.equalizer.setVisibility(View.GONE);
            holder.re_play.setVisibility(View.VISIBLE);
            holder.equalizer.stopBars();
        }

        holder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionPopUp(holder.iv_more, holder.getAdapterPosition());
            }
        });

        holder.iv_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerPlayList.onClick(holder.getAdapterPosition());
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

    private void openOptionPopUp(ImageView imageView, final int pos) {
        Context wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE);
        PopupMenu popup = new PopupMenu(wrapper, imageView);
        popup.getMenuInflater().inflate(R.menu.popup_song, popup.getMenu());

        if (!Setting.isOnline) {
            popup.getMenu().findItem(R.id.popup_add_queue).setVisible(false);
        }
        if(!Setting.isDownload) {
            popup.getMenu().findItem(R.id.popup_download).setVisible(false);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_add_song:
                        methods.openPlaylists(arrayList.get(pos), true);
                        break;
                    case R.id.popup_add_queue:
                        Setting.arrayList_play.add(arrayList.get(pos));
                        GlobalBus.getBus().postSticky(new ItemMyPlayList("", "", null));
                        Toast.makeText(context, context.getString(R.string.add_to_queue), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.popup_youtube:
                        String urlString = "https://www.youtube.com/channel/UCRSvNS1DL5IzluMaRdZLFug/search?query="  + Setting.arrayList_play.get(pos).getTitle();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
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
}