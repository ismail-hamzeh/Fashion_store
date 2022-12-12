package com.example.Fashion_Store;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.Fashion_Store.databinding.FragmentForgetPassRePasswordScreenBinding;


public class Forget_Pass_RePassword_Screen extends Fragment {

    public static Forget_Pass_RePassword_Screen getInstance(){
        Forget_Pass_RePassword_Screen forget_pass_rePasswordScreen = new Forget_Pass_RePassword_Screen();
        return forget_pass_rePasswordScreen;
    }

    private FragmentForgetPassRePasswordScreenBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentForgetPassRePasswordScreenBinding.inflate(getLayoutInflater(), container, false);

        binding.changePassBtn.setOnClickListener((View.OnClickListener) view -> ((Login_SignUp_Container)getActivity()).get_Login_Signup_Tabs());


        return binding.getRoot();
    }
}