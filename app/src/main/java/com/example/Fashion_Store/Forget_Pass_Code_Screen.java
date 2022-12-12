package com.example.Fashion_Store;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.Fashion_Store.databinding.FragmentForgetPassCodeScreenBinding;


public class Forget_Pass_Code_Screen extends Fragment {

    public static Forget_Pass_Code_Screen getInstance(){
        Forget_Pass_Code_Screen forget_pass_code_screen = new Forget_Pass_Code_Screen();
        return forget_pass_code_screen;
    }

    private FragmentForgetPassCodeScreenBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentForgetPassCodeScreenBinding.inflate(getLayoutInflater(), container, false);

        binding.checkCodeBtn.setOnClickListener((View.OnClickListener) view1 -> ((Login_SignUp_Container)getActivity()).get_ForgetPass_RePass_Tab());



        return binding.getRoot();
    }
}