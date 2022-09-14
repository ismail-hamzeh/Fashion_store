package com.example.Fashion_Store.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Fashion_Store.Models.Notification_Item_Model;
import com.example.Fashion_Store.R;

import java.util.ArrayList;

public class Notification_Item_Adapter extends RecyclerView.Adapter<Notification_Item_Adapter.viewHolder> {

    private Context context;
    ArrayList<Notification_Item_Model> notificationItemModels = new ArrayList<>();

    public Notification_Item_Adapter(Context context, ArrayList<Notification_Item_Model> notificationItemModels) {
        this.context = context;
        this.notificationItemModels = notificationItemModels;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.notify_txt.setText(notificationItemModels.get(position).getNotify());
    }

    @Override
    public int getItemCount() {
        return notificationItemModels.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView notify_txt;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            notify_txt = itemView.findViewById(R.id.notify_txt);
        }
    }

}
