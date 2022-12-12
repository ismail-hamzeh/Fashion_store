package com.example.Fashion_Store;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Fashion_Store.Adapters.Notification_Item_Adapter;
import com.example.Fashion_Store.Adapters.ViewPager_Adapter;
import com.example.Fashion_Store.Models.Notification_Item_Model;
import com.example.Fashion_Store.databinding.ActivityMainBinding;
import com.example.Fashion_Store.home_category_tabs.All_tab;
import com.example.Fashion_Store.home_category_tabs.Kids_Tab;
import com.example.Fashion_Store.home_category_tabs.Men_Tab;
import com.example.Fashion_Store.home_category_tabs.Women_Tab;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    int notify_countDB ;

    public static MainActivity getInstance(){
        MainActivity mainActivity = new MainActivity();
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.navigationBottom.setOnNavigationItemSelectedListener(this);
        binding.navigationBottom.setItemIconTintList(null);

        get_category_Tabs();
        setNotificationsCountInBadge();

        binding.searchTxt.setOnClickListener((View.OnClickListener) view -> startActivity(new Intent(MainActivity.this, Search_Screen.class)));

        binding.seeMoreArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.viewPager.getCurrentItem() == 0){
                    startActivity(new Intent(MainActivity.this, Women_Screen.class));

                } else if (binding.viewPager.getCurrentItem() == 1){
                    startActivity(new Intent(MainActivity.this, Men_Screen.class));

                } else if (binding.viewPager.getCurrentItem() == 2){
                    startActivity(new Intent(MainActivity.this, Kids_Screen.class));

                } else {
                    startActivity(new Intent(MainActivity.this, All_Screen.class));
                }
            }
        });

        binding.cartImg.setOnClickListener((View.OnClickListener) view -> startActivity(new Intent(MainActivity.this,Cart_Screen.class)));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_home:
                startActivity(new Intent(this,MainActivity.class));
                break;

            case R.id.nav_order:
                startActivity(new Intent(this,Orders_Screen.class));
                break;

            case R.id.nav_profile:
                startActivity(new Intent(this,Profile_Screen.class));
                break;

            case R.id.nav_notification:
                show_notifications_dialog();
        }
        return true;
    }



    public void show_notifications_dialog(){

        Dialog dialog = new Dialog(MainActivity.this);
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

        add_seen_field();

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

                Notification_Item_Adapter notification_item_adapter = new Notification_Item_Adapter(MainActivity.this,notification_item_models);
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

    public void get_category_Tabs(){

        final ViewPager_Adapter viewPager_adapter = new ViewPager_Adapter(this.getSupportFragmentManager());

        viewPager_adapter.addFragment(Women_Tab.getInstance(),"Women");
        viewPager_adapter.addFragment(Men_Tab.getInstance(),"Men");
        viewPager_adapter.addFragment(Kids_Tab.getInstance(),"Kids");
        viewPager_adapter.addFragment(All_tab.getInstance(),"All");

        binding.viewPager.setAdapter(viewPager_adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);


    }

    public void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, int value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.activity_notifications_counter, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.notify_count);
        text.setText(String.valueOf(value));
        itemView.addView(badge);

        if (notify_countDB == 0){
            text.setVisibility(View.INVISIBLE);
        }


    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }

    }

    public void setNotificationsCountInBadge(){
        String userID = mAuth.getCurrentUser().getUid();


        db.collection("Users").document(userID).collection("Notifications").whereEqualTo("seen","").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                notify_countDB = queryDocumentSnapshots.size();
                showBadge(MainActivity.this, binding.navigationBottom, R.id.nav_notification, notify_countDB);

            }
        });

    }

    public void add_seen_field () {
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userID).collection("Notifications").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            WriteBatch batch = db.batch();

                            for (DocumentSnapshot document : task.getResult()) {
                                DocumentReference docRef = document.getReference();
                                Map<String, Object> new_map = new HashMap<>();
                                new_map.put("seen", "yes");
                                batch.update(docRef, new_map);
                            }
                            batch.commit();
                        } else {

                        }
                    }
                });
    }


}