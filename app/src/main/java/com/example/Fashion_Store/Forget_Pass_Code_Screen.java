package com.example.Fashion_Store;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Forget_Pass_Code_Screen extends Fragment {

    public static Forget_Pass_Code_Screen getInstance(){
        Forget_Pass_Code_Screen forget_pass_code_screen = new Forget_Pass_Code_Screen();
        return forget_pass_code_screen;
    }

    private Button check_code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forget__pass__code__screen, container, false);


        check_code = view.findViewById(R.id.check_code_btn);

        check_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Login_SignUp_Container)getActivity()).get_ForgetPass_RePass_Tab();
            }
        });



        return view;
    }
}