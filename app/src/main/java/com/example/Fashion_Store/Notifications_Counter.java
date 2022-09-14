package com.example.Fashion_Store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.Fashion_Store.Models.Notification_Item_Model;

import java.util.ArrayList;

public class Notifications_Counter extends AppCompatActivity {

    private TextView notify_count;
    ArrayList<Notification_Item_Model> notificationItemModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_counter);

        notify_count = findViewById(R.id.notify_count);

    }
}