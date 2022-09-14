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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login_Screen extends Fragment {

  public static Login_Screen getInstance(){
      Login_Screen login_screen = new Login_Screen();
      return login_screen;
  }

  private EditText email_login, password_login;
  private TextView forge_pass;
  private Button login_btn;
  private FirebaseAuth mAuth;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_login__screen, container, false);

      email_login = view.findViewById(R.id.email_login);
      password_login = view.findViewById(R.id.password_login);
      forge_pass = view.findViewById(R.id.forget_pass);
      login_btn = view.findViewById(R.id.login_btn);
      mAuth = FirebaseAuth.getInstance();

      forge_pass.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              ((Login_SignUp_Container)getActivity()).get_ForgetPass_Email_Tab();

          }
      });

      login_btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              loginUser();
          }
      });


      return view;
  }

  public void loginUser() {

      String email = email_login.getText().toString();
      String password = password_login.getText().toString();

      if (email.isEmpty()) {
          email_login.setError("Required");
      } else if (password.isEmpty()) {
          password_login.setError("Required");
      } else {

          mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {

                  if (task.isSuccessful()){
                      startActivity(new Intent(getActivity(), MainActivity.class));
                      getActivity().finish();
                  } else {
                      Toast.makeText(getActivity(), "Error!! "+ task.getException(), Toast.LENGTH_SHORT).show();
                  }

              }
          });

      }
  }

}