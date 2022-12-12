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

import com.example.Fashion_Store.databinding.FragmentForgetPassEmailScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class Forget_Pass_Email_Screen extends Fragment {

   public static Forget_Pass_Email_Screen getInstance(){
       Forget_Pass_Email_Screen forget_pass_email = new Forget_Pass_Email_Screen();
       return forget_pass_email;
   }

   private FragmentForgetPassEmailScreenBinding binding;
   private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentForgetPassEmailScreenBinding.inflate(getLayoutInflater(), container, false);

        mAuth = FirebaseAuth.getInstance();

        binding.sendCodeBtn.setOnClickListener((View.OnClickListener) view1 -> {

            String email = binding.emailEdt.getText().toString();

            if (email.isEmpty()){
                binding.emailEdt.setError("Please enter your email");
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
        });

        return binding.getRoot();
    }
}