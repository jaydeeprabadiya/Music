package thiva.tamilaudiopro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import thiva.tamilaudiopro.Activity.R;
import thiva.tamilaudiopro.item.ItemNotification;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class AdapterNot extends RecyclerView.Adapter<AdapterNot.MyViewHolder> {

    private Context context;
    private ArrayList<ItemNotification> arrayList;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_title, notification;
        RelativeLayout start_a, end_a;

        MyViewHolder(View view) {
            super(view);
            text_title = view.findViewById(R.id.text_title);
            notification = view.findViewById(R.id.notification);
            start_a = view.findViewById(R.id.start_a);
            end_a = view.findViewById(R.id.end_a);
        }
    }

    public AdapterNot(Context context, ArrayList<ItemNotification> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comments, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.text_title.setText(arrayList.get(position).getNotification_title());
        holder.notification.setText(arrayList.get(position).getNotification_msg());

        if (arrayList.get(position).getUpdate_by().equals("admin")){
            holder.start_a.setVisibility(View.VISIBLE);
            holder.end_a.setVisibility(View.INVISIBLE);
            holder.text_title.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.notification.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }else {
            holder.start_a.setVisibility(View.INVISIBLE);
            holder.end_a.setVisibility(View.VISIBLE);
            holder.text_title.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.notification.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
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
        return arrayList.get(pos).getNid();
    }

}