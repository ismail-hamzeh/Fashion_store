package com.example.Fashion_Store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.Fashion_Store.Adapters.ViewPager_Adapter;
import com.example.Fashion_Store.databinding.ActivityLoginSignUpContanierBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Login_SignUp_Container extends AppCompatActivity {

    private ActivityLoginSignUpContanierBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginSignUpContanierBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(this,MainActivity.class));
        }

        get_Login_Signup_Tabs();


    }

    public void get_Login_Signup_Tabs(){

        final ViewPager_Adapter viewPager_adapter = new ViewPager_Adapter(getSupportFragmentManager());

        viewPager_adapter.addFragment(Login_Screen.getInstance(),"Login");
        viewPager_adapter.addFragment(SignUp_Screen.getInstance(),"Sign-Up");

        binding.viewPager.setAdapter(viewPager_adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

    public void get_ForgetPass_Email_Tab(){

        final ViewPager_Adapter viewPager_adapter = new ViewPager_Adapter(getSupportFragmentManager());

        viewPager_adapter.addFragment(Forget_Pass_Email_Screen.getInstance(),"Forget Password");

        binding.viewPager.setAdapter(viewPager_adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

    public void get_ForgetPass_Code_Tab(){
        final ViewPager_Adapter viewPager_adapter = new ViewPager_Adapter(getSupportFragmentManager());

        viewPager_adapter.addFragment(Forget_Pass_Code_Screen.getInstance(),"Forget Password");

        binding.viewPager.setAdapter(viewPager_adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

    public void get_ForgetPass_RePass_Tab(){

        final ViewPager_Adapter viewPager_adapter = new ViewPager_Adapter(getSupportFragmentManager());

        viewPager_adapter.addFragment(Forget_Pass_RePassword_Screen.getInstance(),"Forget Password");

        binding.viewPager.setAdapter(viewPager_adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

}