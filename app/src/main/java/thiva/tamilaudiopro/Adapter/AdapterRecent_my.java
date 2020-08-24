package thiva.tamilaudiopro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thiva.tamilaudiopro.Activity.PlayerService;
import thiva.tamilaudiopro.Activity.R;
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

public class AdapterRecent_my extends RecyclerView.Adapter<AdapterRecent_my.MyViewHolder> {

    private Context context;
    private ArrayList<ItemSong> arrayList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView2;
        ImageView imageView, play_songs;
        EqualizerView equalizer;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView_latest_list);
            textView2 = view.findViewById(R.id.text);
            imageView = view.findViewById(R.id.imageView_latest_list);
            equalizer = view.findViewById(R.id.equalizer_view2);
            play_songs = view.findViewById(R.id.play_songs);
        }
    }

    public AdapterRecent_my(Context context, ArrayList<ItemSong> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pager_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView.setText(arrayList.get(position).getTitle());
        holder.textView2.setText(arrayList.get(position).getArtist());
        Picasso.get()
                .load(arrayList.get(position).getImageBig())
                .into(holder.imageView);

        if (PlayerService.getIsPlayling() && Setting.arrayList_play.get(Setting.playPos).getId().equals(arrayList.get(position).getId())) {
            holder.equalizer.setVisibility(View.VISIBLE);
            holder.equalizer.animateBars();
            holder.play_songs.setImageResource(R.drawable.ic_pause_white_24dp);
        } else {
            holder.equalizer.setVisibility(View.GONE);
            holder.equalizer.stopBars();
            holder.play_songs.setImageResource(R.drawable.ic_play_arrow_white_24dp);
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
}