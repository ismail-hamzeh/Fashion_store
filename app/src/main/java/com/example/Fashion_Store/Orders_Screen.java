package com.example.Fashion_Store;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Fashion_Store.Adapters.Notification_Item_Adapter;
import com.example.Fashion_Store.Adapters.Order_Item_Adapter;
import com.example.Fashion_Store.Models.Notification_Item_Model;
import com.example.Fashion_Store.Models.Order_Item_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Orders_Screen extends AppCompatActivity {

    private ImageView back_orders, notification_orders;
    private TextView notify_count_orders;
    private Button order_now;
    private LinearLayout noOrders_linear;
    private RecyclerView order_Rec;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    ArrayList<Order_Item_Model> orderItemModels;
    int notify_countDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orders_screen);

        back_orders = findViewById(R.id.back_orders);
        notification_orders = findViewById(R.id.notification_orders);
        notify_count_orders = findViewById(R.id.notify_count_orders);
        order_now = findViewById(R.id.order_now_orders);
        noOrders_linear = findViewById(R.id.noOrders_linear);
        order_Rec = findViewById(R.id.orders_Rec);
        mAuth = FirebaseAuth.getInstance();

        getOrderData();
        setNotificationsCountInBadge();
        clearAll();

        order_Rec.setHasFixedSize(true);
        order_Rec.setLayoutManager(new LinearLayoutManager(this));
        orderItemModels = new ArrayList<>();


        back_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        notification_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_notifications_dialog();
            }
        });

        order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orders_Screen.this, MainActivity.class));
            }
        });
    }
    public void getOrderData(){

        String userID = mAuth.getCurrentUser().getUid();
        collectionReference =  db.collection("Users").document(userID).collection("Orders");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    return;
                }

                clearAll();
                for (DocumentChange data : value.getDocumentChanges()){
                    Order_Item_Model order_item_model = new Order_Item_Model();

                    DocumentSnapshot snapshot = data.getDocument();

                    order_item_model.setProduct_num(snapshot.getString("product num"));
                    order_item_model.setTotal_price(snapshot.getString("total price"));
                    order_item_model.setId(snapshot.getString("order id"));

                    orderItemModels.add(order_item_model);
                }
                Order_Item_Adapter orderItemAdapter = new Order_Item_Adapter(orderItemModels,Orders_Screen.this);
                order_Rec.setAdapter(orderItemAdapter);
                orderItemAdapter.notifyDataSetChanged();

                if (orderItemAdapter.getItemCount() == 0){
                    noOrders_linear.setVisibility(View.VISIBLE);
                    order_now.setVisibility(View.VISIBLE);
                    order_Rec.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    public void show_notifications_dialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.notifications_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();

        TextView cancel_dialog = (TextView) dialog.findViewById(R.id.cancel_dialog);
        TextView no_notify = (TextView) dialog.findViewById(R.id.no_notify);
        RecyclerView recyclerView = dialog.findViewById(R.id.notification_rec);
        ArrayList<Notification_Item_Model> notification_item_models = new ArrayList<>();
        String userID = mAuth.getCurrentUser().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MainActivity.getInstance().add_seen_field();

        Query query = db.collection("Users").document(userID).collection("Notifications")
                .orderBy("notify", Query.Direction.ASCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                notification_item_models.clear();
                for (DocumentChange data : value.getDocumentChanges()){

                    DocumentSnapshot snapshot = data.getDocument();

                    Notification_Item_Model notificationItemModel = new Notification_Item_Model();
                    notificationItemModel.setNotify(snapshot.getString("notify"));
                    notification_item_models.add(notificationItemModel);

                }

                Notification_Item_Adapter notification_item_adapter = new Notification_Item_Adapter(Orders_Screen.this,notification_item_models);
                recyclerView.setAdapter(notification_item_adapter);
                notification_item_adapter.notifyDataSetChanged();


                if (notification_item_adapter.getItemCount() != 0){
                    no_notify.setVisibility(View.INVISIBLE);
                }

            }
        });

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }
    public void setNotificationsCountInBadge(){
        String userID = mAuth.getCurrentUser().getUid();


        db.collection("Users").document(userID).collection("Notifications")
                .whereEqualTo("seen","").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                notify_countDB = queryDocumentSnapshots.size();
                notify_count_orders.setText(String.valueOf(notify_countDB));

                if (notify_countDB == 0){
                    notify_count_orders.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void clearAll(){
        Order_Item_Adapter orderItemAdapter = new Order_Item_Adapter(orderItemModels,Orders_Screen.this);

        if (orderItemModels != null) {
            orderItemModels.clear();

                if (orderItemAdapter != null) {
                    orderItemAdapter.notifyDataSetChanged();
                }

        }
        orderItemModels = new ArrayList<>();
    }

//public void onLongClick(View view,final int position){
//        Order_Item_Adapter orderItemAdapter = new Order_Item_Adapter(orderItemModels,this);
//    ClipData.Item item = orderItemModels.get(
//            orderItemModels.clear();
//        orderItemModels.remove(position);
//        orderItemAdapter.notifyItemRangeChanged(position, orderItemModels.size());
//}
}