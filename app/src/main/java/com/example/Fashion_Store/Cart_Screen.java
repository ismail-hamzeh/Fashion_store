package com.example.Fashion_Store;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.Fashion_Store.Adapters.Cart_Item_Adapter;
import com.example.Fashion_Store.Models.Cart_Item_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart_Screen extends AppCompatActivity {

    private ImageView back_cart;
    private Button order_now, checkOut_cart;
    private GridView gridView;
    ArrayList<Cart_Item_Model> itemCartModels = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private LinearLayout noCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);

        back_cart = findViewById(R.id.back_cart);
        order_now = findViewById(R.id.order_now_cart);
        checkOut_cart = findViewById(R.id.checkOut_cart);
        gridView = findViewById(R.id.cart_item_grid);
        noCart = findViewById(R.id.linear_no_cart);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        getCartData();
        clearAll();

        back_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cart_Screen.this, MainActivity.class));
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("buyNowOrCart",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        checkOut_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("buyNow",false).commit();
                passMyTotalCartPrice();
                startActivity(new Intent(Cart_Screen.this, CheckOut_Screen.class));
            }
        });

    }

    public void getCartData(){

        String userID = mAuth.getCurrentUser().getUid();

        reference.child("Cart").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Cart_Item_Model item_cart_model = new Cart_Item_Model();

                    item_cart_model.setImg(String.valueOf(snapshot.child("item img").getValue()));
                    item_cart_model.setName(String.valueOf(snapshot.child("item name").getValue()));
                    item_cart_model.setPrice(String.valueOf(snapshot.child("item price").getValue()));
                    item_cart_model.setNum(Integer.parseInt(String.valueOf(snapshot.child("item num").getValue())));
                    item_cart_model.setId(String.valueOf(snapshot.child("id").getValue()));

                    itemCartModels.add(item_cart_model);
                }
                Cart_Item_Adapter cart_grid_adapter = new Cart_Item_Adapter(Cart_Screen.this,itemCartModels);
                gridView.setAdapter(cart_grid_adapter);
                cart_grid_adapter.notifyDataSetChanged();

                if (cart_grid_adapter.isEmpty()){
                    noCart.setVisibility(View.VISIBLE);
                    order_now.setVisibility(View.VISIBLE);
                    checkOut_cart.setVisibility(View.INVISIBLE);
                } else {
                    checkOut_cart.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void clearAll(){
        Cart_Item_Adapter cart_grid_adapter = new Cart_Item_Adapter(Cart_Screen.this,itemCartModels);

        if (itemCartModels != null) {
            itemCartModels.clear();

            if (cart_grid_adapter != null){
                cart_grid_adapter.notifyDataSetChanged();
            }
        }
        itemCartModels = new ArrayList<>();
    }

    public void passMyTotalCartPrice() {

        String userID = mAuth.getCurrentUser().getUid();
        final int[] globalPriceSum = {0};
        final int[] globalProductNum = {0};

        Query query = reference.child("Cart").child(userID).orderByChild("id");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int price_sum = 0;
                int num_sum = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int total_price = parseInt(String.valueOf(dataSnapshot.child("total price").getValue()));
                    int total_cart_num = parseInt(String.valueOf(dataSnapshot.child("item num").getValue()));

                    price_sum = price_sum + total_price;
                    globalPriceSum[0] = price_sum;

                    num_sum = num_sum + total_cart_num;
                    globalProductNum[0] = num_sum;
                }
                SharedPreferences sharedPreferences = getSharedPreferences("cartData",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("total cart price", String.valueOf(globalPriceSum[0]));
                editor.putString("cart product num", String.valueOf(globalProductNum[0]));
                editor.commit();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}