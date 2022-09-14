package com.example.Fashion_Store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.Fashion_Store.Adapters.Items_Grid_Adapter;
import com.example.Fashion_Store.Models.Items_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Men_Screen extends AppCompatActivity {

    private ImageView back_men, cart_men;
    private GridView gridView;
    private Items_Grid_Adapter items_grid_adapter;
    ArrayList<Items_Model> items_models = new ArrayList<>();
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_men_screen);

        back_men = findViewById(R.id.back_men);
        cart_men = findViewById(R.id.cart_men);
        gridView = findViewById(R.id.grid_men);
        reference = FirebaseDatabase.getInstance().getReference();

        getItemsFromDB();

        back_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cart_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Men_Screen.this, Cart_Screen.class));
            }
        });


    }
    public void getItemsFromDB(){

        reference.child("men").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()){

                    Items_Model items_model = new Items_Model();

                    items_model.setItem_img(String.valueOf(data.child("img").getValue()));
                    items_model.setItem_name(String.valueOf(data.child("name").getValue()));
                    items_model.setItem_num(Integer.parseInt(String.valueOf(data.child("number").getValue())));
                    items_model.setItem_price(String.valueOf(data.child("price").getValue()));
                    items_model.setPage_title(String.valueOf(data.child("title").getValue()));

                    items_models.add(items_model);

                }
                items_models = new ArrayList<>(items_models);
                items_grid_adapter = new Items_Grid_Adapter(Men_Screen.this,items_models);
                gridView.setAdapter(items_grid_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}