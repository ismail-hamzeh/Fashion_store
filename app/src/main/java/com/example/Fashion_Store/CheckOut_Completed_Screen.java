package com.example.Fashion_Store;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.Fashion_Store.databinding.ActivityCheckOutScreenBinding;
import com.example.Fashion_Store.databinding.FragmentCheckOutCompletedScreenBinding;

public class CheckOut_Completed_Screen extends Fragment {

    private FragmentCheckOutCompletedScreenBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCheckOutCompletedScreenBinding.inflate(getLayoutInflater(), container, false);

        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("orderID",MODE_PRIVATE);
        binding.orderIdTxt.setText(sharedPreferences1.getString("orderID",""));

        binding.homeBtn.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });

        return binding.getRoot();
    }
}