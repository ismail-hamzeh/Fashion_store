package com.example.Fashion_Store;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.Fashion_Store.Models.Notification_Item_Model;
import com.example.Fashion_Store.databinding.FragmentCheckOutPaymentScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CheckOut_Payment_Screen extends Fragment {

    private FragmentCheckOutPaymentScreenBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    int productNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCheckOutPaymentScreenBinding.inflate(getLayoutInflater(), container, false);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("buyNowOrCart",MODE_PRIVATE);
        boolean buyNow = sharedPreferences.getBoolean("buyNow",true);

        if (buyNow) {
            getOneItemData();
        } else {
            getCartData();
        }

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("deliveryShared", MODE_PRIVATE);
        boolean door_rb = sharedPreferences2.getBoolean("door method",true);

        if (door_rb){
            binding.doorRb.setChecked(true);
        } else {
            binding.pickUpRb.setChecked(true);
        }


        binding.proceedToPaymentBtn.setOnClickListener((View.OnClickListener) view -> {
            if (!binding.cashRb.isChecked() && !binding.cardRb.isChecked()){
                binding.paymentMethodTxt.setError("Required");
            } else if (!binding.doorRb.isChecked() && !binding.pickUpRb.isChecked()){
                binding.deliveryMethodTxt.setError("Required");
            } else {
                orderData();
            }
        });


        return binding.getRoot();
    }
    public void getOneItemData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("orderDetails",MODE_PRIVATE);
        String itemPrice = sharedPreferences.getString("item price","");
        productNum = sharedPreferences.getInt("product num",0);

        int total_price = Integer.parseInt(itemPrice) * productNum;
        binding.priceTxt.setText(String.valueOf(total_price));
        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("deliveryShared", MODE_PRIVATE);

        binding.priceTxt.setText(sharedPreferences1.getString("total price", ""));
    }

    public void getCartData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cartData",MODE_PRIVATE);
        productNum = Integer.parseInt(sharedPreferences.getString("cart product num",""));

        binding.priceTxt.setText(sharedPreferences.getString("total cart price",""));

    }

    public void orderData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("deliveryShared", Context.MODE_PRIVATE);

        String userID = mAuth.getCurrentUser().getUid();

        Map<String, Object> order = new HashMap<>();
        order.put("name", sharedPreferences.getString("name delivery",""));
        order.put("location", sharedPreferences.getString("address delivery",""));
        order.put("number", sharedPreferences.getString("number delivery",""));
        order.put("product num", String.valueOf(productNum));
        order.put("total price", binding.priceTxt.getText().toString());
        order.put("card", binding.cardRb.isChecked());
        order.put("cash", binding.cashRb.isChecked());
        order.put("door delivery", binding.doorRb.isChecked());
        order.put("pickUp", binding.pickUpRb.isChecked());
        String id = db.collection("Users").document(userID).collection("Orders").document().getId();
        order.put("order id", id);

        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("orderID",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putString("orderID", id).apply();

        db.collection("Users").document(userID).collection("Orders").document(id).set(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    deleteCartData();
                    setNotification();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_checkOut,
                            new CheckOut_Completed_Screen()).addToBackStack(null).commit();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_checkOut,
                            new CheckOut_Failed_Screen()).addToBackStack(null).commit();
                }


            }
        });

    }

    public void deleteCartData(){

        String userID = mAuth.getCurrentUser().getUid();
        reference.child("Cart").child(userID).removeValue();
    }

    public void setNotification(){
        String userID = mAuth.getCurrentUser().getUid();
        String id =   db.collection("Users").document(userID).collection("Notifications").document().getId();

        Notification_Item_Model notificationItemModel = new Notification_Item_Model();
        notificationItemModel.setId(id);

        Map<String,Object> notify = new HashMap<>();
        notify.put("notify", "Thank you for shopping with us!");
        notify.put("seen","");
        db.collection("Users").document(userID).collection("Notifications").document(id).set(notify);
    }
}