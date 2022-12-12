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
import android.widget.Toast;

import com.example.Fashion_Store.Models.Notification_Item_Model;
import com.example.Fashion_Store.databinding.FragmentSignUpScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUp_Screen extends Fragment {

    public static SignUp_Screen getInstance() {
        SignUp_Screen signUp_screen = new SignUp_Screen();
        return signUp_screen;
    }

    private FragmentSignUpScreenBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignUpScreenBinding.inflate(getLayoutInflater(), container, false);

        mAuth = FirebaseAuth.getInstance();


        binding.signupBtn.setOnClickListener((View.OnClickListener) view -> signupUser());


        return binding.getRoot();
    }

    private void signupUser() {

        String name = binding.nameEdt.getText().toString();
        String email = binding.emailEdt.getText().toString();
        String password = binding.passwordEdt.getText().toString();
        String rePassword = binding.rePasswordEdt.getText().toString();

        if (name.isEmpty()) {
            binding.nameEdt.setError("Required");
        } else if (email.isEmpty()) {
            binding.emailEdt.setError("Required");
        } else if (password.isEmpty()) {
            binding.passwordEdt.setError("Required");
        } else if (password.length() < 8) {
            binding.passwordEdt.setError("At least 8 character");
        } else if (rePassword.isEmpty()) {
            binding.rePasswordEdt.setError("Required");
        } else if (!password.equals(rePassword)) {
            binding.rePasswordEdt.setError("Password doesn't match");
        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        setNotification();

                        String userID = mAuth.getCurrentUser().getUid();

                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name);
                        user.put("email", email);
                        user.put("password", password);
                        user.put("profile pic", "");
                        user.put("number", "");
                        user.put("address", "");
                        db.collection("Users").document(userID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Error!! " + task.getException(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    public void setNotification() {
        String userID = mAuth.getCurrentUser().getUid();
        String id = db.collection("Users").document(userID).collection("Notifications").document().getId();

        Notification_Item_Model notificationItemModel = new Notification_Item_Model();
        notificationItemModel.setId(id);

        Map<String, Object> notify = new HashMap<>();
        notify.put("notify", "Welcome!, We hope you find what you're looking for and that you enjoy your stay.");
        db.collection("Users").document(userID).collection("Notifications").document(id).set(notify);
    }
}