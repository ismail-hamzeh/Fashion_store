package com.example.Fashion_Store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Fashion_Store.Adapters.ViewPager_Category_Adapter;
import com.example.Fashion_Store.Models.ViewPager_Category_Model;
import com.example.Fashion_Store.databinding.ActivityItemDetailsScreenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Item_Details_Screen extends AppCompatActivity {

    private ActivityItemDetailsScreenBinding binding;
    private Button buyNow_btn, addToCart_btn;
    private ViewPager img_viewPager;
    private LinearLayout indicator_linear;
    TextView [] dots;
    ViewPager_Category_Adapter viewPager_category_adapter;
    ArrayList<ViewPager_Category_Model> items_model = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buyNow_btn = findViewById(R.id.buyNow_details);
        addToCart_btn = findViewById(R.id.addToCart_btn);
        img_viewPager = findViewById(R.id.img_viewPager_details);
        indicator_linear = findViewById(R.id.indicator_linear);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();


        getItemData();
        getImgViewPagerData();
        setUpIndicator(0);

        img_viewPager.addOnPageChangeListener(viewListener);
        binding.pageTitleTxt.setText(getIntent().getExtras().getString("page title"));

        binding.backImg.setOnClickListener(view -> onBackPressed());

        SharedPreferences sharedPreferences = getSharedPreferences("buyNowOrCart",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        buyNow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(binding.itemNumTxt.getText().toString()) > 0){
                    editor.putBoolean("buyNow",true).commit();
                    orderData();
                    startActivity(new Intent(Item_Details_Screen.this, CheckOut_Screen.class));
                } else {
                    Toast.makeText(Item_Details_Screen.this, "Please Add Item", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.cartImg.setOnClickListener((View.OnClickListener) view -> startActivity(new Intent(Item_Details_Screen.this,Cart_Screen.class)));

        final int[] num = {getIntent().getExtras().getInt("item num")};

        binding.itemPlusImg.setOnClickListener((View.OnClickListener) view -> binding.itemNumTxt.setText(String.valueOf(num[0]++)));

        binding.itemMinusImg.setOnClickListener((View.OnClickListener) view -> {
            if (num[0] > 0) {
                binding.itemNumTxt.setText(String.valueOf(--num[0]));
            }
        });

        addToCart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(binding.itemNumTxt.getText().toString()) > 0) {
                    addItemToCart();
                } else {
                    Toast.makeText(Item_Details_Screen.this, "Please Add Item", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public void getItemData(){
        binding.itemNameTxt.setText(getIntent().getExtras().getString("item name"));
        binding.itemPriceTxt.setText(getIntent().getExtras().getString("item price"));
        binding.itemNumTxt.setText(String.valueOf(getIntent().getExtras().getInt("item num")));
    }

    public void getImgViewPagerData(){

        String img = getIntent().getExtras().getString("item img");

        items_model.add(new ViewPager_Category_Model(img));
        items_model.add(new ViewPager_Category_Model(img));

        items_model = new ArrayList<>(items_model);
        viewPager_category_adapter = new ViewPager_Category_Adapter(items_model,this);
        img_viewPager.setAdapter(viewPager_category_adapter);

    }

    public void setUpIndicator ( int position){

        dots = new TextView[2];
        indicator_linear.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setGravity(View.TEXT_ALIGNMENT_CENTER);
            dots[i].setTextColor(getResources().getColor(R.color.gray_1, getApplicationContext().getTheme()));
            indicator_linear.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.pink, getApplicationContext().getTheme()));

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpIndicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void addItemToCart(){

        String userID = mAuth.getCurrentUser().getUid();

        String imageCart =  getIntent().getExtras().getString("item img");
        String nameCart = getIntent().getExtras().getString("item name");
        String priceCart = getIntent().getExtras().getString("item price");
        String numCart = binding.itemNumTxt.getText().toString();

        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("item img", imageCart);
        cartItem.put("item name", nameCart);
        cartItem.put("item price", priceCart);
        cartItem.put("item num", numCart);
        cartItem.put("total price", String.valueOf(Integer.parseInt(priceCart) * Integer.parseInt(numCart)));
        String id = reference.push().getKey();
        cartItem.put("id", id);

        reference.child("Cart").child(userID).child(id).setValue(cartItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Item_Details_Screen.this,"Added Successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void orderData(){
        SharedPreferences sharedPreferences = getSharedPreferences("orderDetails",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("item price", binding.itemPriceTxt.getText().toString());
        editor.putInt("product num", Integer.parseInt(String.valueOf(binding.itemNumTxt.getText())));

        int total_price = Integer.parseInt(binding.itemPriceTxt.getText().toString()) * Integer.parseInt(String.valueOf(binding.itemNumTxt.getText()));

        editor.putString("total price", String.valueOf(total_price));
        editor.commit();
    }


}