package com.example.Fashion_Store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.Fashion_Store.databinding.ActivityCheckOutScreenBinding;

public class CheckOut_Screen extends AppCompatActivity {

    private ActivityCheckOutScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckOutScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_checkOut,
                    new CheckOut_Delivery_Screen()).commit();
        }

        binding.backImg.setOnClickListener((View.OnClickListener) view -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                onBackPressed();
            }
        });

    }
}