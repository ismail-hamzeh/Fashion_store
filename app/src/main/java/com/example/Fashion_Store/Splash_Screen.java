package com.example.Fashion_Store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.Fashion_Store.databinding.ActivitySplashScreenBinding;

public class Splash_Screen extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startedBtn.setOnClickListener((View.OnClickListener) view -> {
            startActivity(new Intent(Splash_Screen.this, Login_SignUp_Container.class));
            finish();

        });
    }
}