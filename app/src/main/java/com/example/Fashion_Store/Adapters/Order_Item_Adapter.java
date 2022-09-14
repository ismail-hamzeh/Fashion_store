package com.example.Fashion_Store.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Fashion_Store.Models.Order_Item_Model;
import com.example.Fashion_Store.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class Order_Item_Adapter extends RecyclerView.Adapter<Order_Item_Adapter.viewHolder> {

    private ArrayList<Order_Item_Model> orderItemModels = new ArrayList<>();
    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Order_Item_Adapter(ArrayList<Order_Item_Model> orderItemModels, android.content.Context context) {
        this.orderItemModels = orderItemModels;
        this.context = context;
        notifyDataSetChanged();
    }
    public Order_Item_Adapter() {

    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_items,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.product_num.setText(orderItemModels.get(position).getProduct_num());
        holder.total_price.setText(orderItemModels.get(position).getTotal_price());


        holder.delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth = FirebaseAuth.getInstance();
                String userID = mAuth.getCurrentUser().getUid();
                String orderID = orderItemModels.get(holder.getAdapterPosition()).getId();

                removeAt(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

                db.collection("Users").document(userID)
                        .collection("Orders").document(orderID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                    DocumentReference reference = value.getReference();
                                    reference.delete();

                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItemModels.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView product_num, total_price, delete_order;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            product_num = itemView.findViewById(R.id.product_num);
            total_price = itemView.findViewById(R.id.order_price);
            delete_order = itemView.findViewById(R.id.delete_order);

        }
    }
    public void removeAt(int position) {
        orderItemModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, orderItemModels.size());
    }

}
