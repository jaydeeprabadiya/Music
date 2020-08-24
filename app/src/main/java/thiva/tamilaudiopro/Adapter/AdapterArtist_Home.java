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

import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.item.ItemArtist;
import thiva.tamilaudiopro.views.CircularImageView;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class AdapterArtist_Home extends RecyclerView.Adapter<AdapterArtist_Home.MyViewHolder> {

    private Context context;
    private ArrayList<ItemArtist> arrayList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        CircularImageView imageView_artist;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView_latest_list);
            imageView_artist = view.findViewById(R.id.imageView_latest_list);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    public AdapterArtist_Home(Context context, ArrayList<ItemArtist> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.imageView.setVisibility(View.GONE);
        holder.textView.setText(arrayList.get(position).getName());
        Picasso.get()
                .load(arrayList.get(position).getThumb())
                .placeholder(R.drawable.music_directors)
                .into(holder.imageView_artist);

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