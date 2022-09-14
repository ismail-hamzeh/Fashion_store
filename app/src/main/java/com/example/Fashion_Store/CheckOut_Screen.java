package com.example.Fashion_Store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CheckOut_Screen extends AppCompatActivity {

    private ImageView back_checkOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_screen);

        back_checkOut = findViewById(R.id.back_checkOut);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_checkOut,
                    new CheckOut_Delivery_Screen()).commit();
        }

        back_checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    onBackPressed();
                }
            }
        });

    }
}