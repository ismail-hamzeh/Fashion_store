package com.example.Fashion_Store;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Fashion_Store.databinding.FragmentLoginScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login_Screen extends Fragment {

    public static Login_Screen getInstance() {
        Login_Screen login_screen = new Login_Screen();
        return login_screen;
    }

    private FragmentLoginScreenBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginScreenBinding.inflate(getLayoutInflater(), container, false);

        mAuth = FirebaseAuth.getInstance();

        binding.forgetPassTxt.setOnClickListener(view -> ((Login_SignUp_Container) getActivity()).get_ForgetPass_Email_Tab());

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


        return binding.getRoot();
    }

    public void loginUser() {

        String email = binding.emailEdt.getText().toString();
        String password = binding.passwordEdt.getText().toString();

        if (email.isEmpty()) {
            binding.emailEdt.setError("Required");
        } else if (password.isEmpty()) {
            binding.passwordEdt.setError("Required");
        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Error!! " + task.getException(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

}