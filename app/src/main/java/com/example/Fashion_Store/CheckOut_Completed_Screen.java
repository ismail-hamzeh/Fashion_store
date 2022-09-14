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

public class CheckOut_Completed_Screen extends Fragment {

    private TextView order_ID;
    private Button home_completed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_out_completed_screen, container, false);

        order_ID = view.findViewById(R.id.order_id);
        home_completed = view.findViewById(R.id.home_completed);

        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("orderID",MODE_PRIVATE);
        order_ID.setText(sharedPreferences1.getString("orderID",""));

        home_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }
}