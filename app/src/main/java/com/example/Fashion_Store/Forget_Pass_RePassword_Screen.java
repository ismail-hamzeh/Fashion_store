package com.example.Fashion_Store;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Forget_Pass_RePassword_Screen extends Fragment {

    public static Forget_Pass_RePassword_Screen getInstance(){
        Forget_Pass_RePassword_Screen forget_pass_rePasswordScreen = new Forget_Pass_RePassword_Screen();
        return forget_pass_rePasswordScreen;
    }

    private Button change_pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forget__pass__re_password_screen, container, false);

        change_pass = view.findViewById(R.id.change_pass_btn);

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Login_SignUp_Container)getActivity()).get_Login_Signup_Tabs();
            }
        });


        return view;
    }
}