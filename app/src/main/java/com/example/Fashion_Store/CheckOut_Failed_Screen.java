package com.example.Fashion_Store;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.Fashion_Store.databinding.FragmentCheckOutFailedScreenBinding;


public class CheckOut_Failed_Screen extends Fragment {

    private FragmentCheckOutFailedScreenBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCheckOutFailedScreenBinding.inflate(getLayoutInflater(), container, false);

        binding.homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });



        return binding.getRoot();
    }
}