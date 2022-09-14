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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CheckOut_Payment_Screen extends Fragment {

    private RadioGroup rg_delivery;
    private RadioButton card_radio, cash_radio, door_radio, pickUp_radio;
    private TextView price_payment,payment_method_txt, delivery_method_txt;
    private Button payment_btn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    int productNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_out_payment__screen, container, false);

        rg_delivery = view.findViewById(R.id.rg_delivery_payment);
        card_radio = view.findViewById(R.id.card_radio);
        cash_radio = view.findViewById(R.id.cash_radio);
        door_radio = view.findViewById(R.id.door_radio_payment);
        pickUp_radio = view.findViewById(R.id.pickUp_radio_payment);
        price_payment = view.findViewById(R.id.price_payment);
        payment_method_txt = view.findViewById(R.id.payment_method_txt);
        delivery_method_txt = view.findViewById(R.id.delivery_method_txt2);
        payment_btn = view.findViewById(R.id.payment_payment);
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
            door_radio.setChecked(true);
        } else {
            pickUp_radio.setChecked(true
            );
        }


        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cash_radio.isChecked() && !card_radio.isChecked()){
                    payment_method_txt.setError("Required");
                } else if (!door_radio.isChecked() && !pickUp_radio.isChecked()){
                    delivery_method_txt.setError("Required");
                } else {
                    orderData();
                }
            }
        });


        return view;
    }
    public void getOneItemData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("orderDetails",MODE_PRIVATE);
        String itemPrice = sharedPreferences.getString("item price","");
        productNum = sharedPreferences.getInt("product num",0);

        int total_price = Integer.parseInt(itemPrice) * productNum;
        price_payment.setText(String.valueOf(total_price));
        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("deliveryShared", MODE_PRIVATE);

        price_payment.setText(sharedPreferences1.getString("total price", ""));
    }

    public void getCartData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cartData",MODE_PRIVATE);
        productNum = Integer.parseInt(sharedPreferences.getString("cart product num",""));

        price_payment.setText(sharedPreferences.getString("total cart price",""));

    }

    public void orderData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("deliveryShared", Context.MODE_PRIVATE);

        String userID = mAuth.getCurrentUser().getUid();

        Map<String, Object> order = new HashMap<>();
        order.put("name", sharedPreferences.getString("name delivery",""));
        order.put("location", sharedPreferences.getString("address delivery",""));
        order.put("number", sharedPreferences.getString("number delivery",""));
        order.put("product num", String.valueOf(productNum));
        order.put("total price", price_payment.getText().toString());
        order.put("card", card_radio.isChecked());
        order.put("cash", cash_radio.isChecked());
        order.put("door delivery", door_radio.isChecked());
        order.put("pickUp", pickUp_radio.isChecked());
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