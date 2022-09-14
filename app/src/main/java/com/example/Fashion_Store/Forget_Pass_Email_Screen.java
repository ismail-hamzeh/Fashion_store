package com.example.Fashion_Store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class Forget_Pass_Email_Screen extends Fragment {

   public static Forget_Pass_Email_Screen getInstance(){
       Forget_Pass_Email_Screen forget_pass_email = new Forget_Pass_Email_Screen();
       return forget_pass_email;
   }

   private EditText email_forget_pass;
   private Button send_code_btn;
   private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forget__pass__email_screen, container, false);

        email_forget_pass = view.findViewById(R.id.email_forget_pass);
        send_code_btn = view.findViewById(R.id.send_code_btn);
        mAuth = FirebaseAuth.getInstance();

        send_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_forget_pass.getText().toString();

                if (email.isEmpty()){
                    email_forget_pass.setError("Please enter your email");
                } else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), "done, please check your email", Toast.LENGTH_SHORT).show();
                                ((Login_SignUp_Container) getActivity()).get_ForgetPass_Code_Tab();

                            } else {
                                Toast.makeText(getActivity(),"Email is not exist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return view;
    }
}