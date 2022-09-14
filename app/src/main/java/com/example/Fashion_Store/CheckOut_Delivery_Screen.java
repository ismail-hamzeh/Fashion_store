package com.example.Fashion_Store;

import static android.content.Context.MODE_PRIVATE;

import static java.lang.Integer.parseInt;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CheckOut_Delivery_Screen extends Fragment {

    private EditText name, location, number;
    private TextView change_txt, delivery_method_txt, price_delivery;
    private RadioGroup rg_delivery;
    private RadioButton door_radio, pickUp_radio, rb;
    private Button payment_btn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference reference;
    boolean change = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_out__delivery__screen, container, false);

        name = view.findViewById(R.id.name_delivery);
        location = view.findViewById(R.id.location_delivery);
        number = view.findViewById(R.id.number_delivery);
        change_txt = view.findViewById(R.id.address_change_txt);
        delivery_method_txt = view.findViewById(R.id.delivery_method_txt);
        price_delivery = view.findViewById(R.id.price_delivery);
        rg_delivery = view.findViewById(R.id.rg_delivery);
        door_radio = view.findViewById(R.id.door_radio);
        pickUp_radio = view.findViewById(R.id.pickUp_radio);
        payment_btn = view.findViewById(R.id.payment_delivery);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("buyNowOrCart",MODE_PRIVATE);
        boolean buyNow = sharedPreferences.getBoolean("buyNow",true);

        if (buyNow) {
            getOneItemData();
        } else {
            getCartData();
        }
        getAddressDetails();


        change_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (change == false) {
                    name.setEnabled(true);
                    location.setEnabled(true);
                    number.setEnabled(true);
                    change = true;
                } else {
                    name.setEnabled(false);
                    location.setEnabled(false);
                    number.setEnabled(false);
                    change = false;
                }
            }
        });

        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()){
                    name.setError("Required");
                } else if (location.getText().toString().isEmpty()){
                    location.setError("Required");
                } else if (number.getText().toString().isEmpty()){
                    number.setError("Required");
                } else if (!door_radio.isChecked() && !pickUp_radio.isChecked()){
                    delivery_method_txt.setError("Please choose method");
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_checkOut,
                            new CheckOut_Payment_Screen()).addToBackStack(null).commit();
                    passDeliveryData();
                }
            }
        });



        return view;
    }

    public void getAddressDetails(){

        String userID = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                name.setText(value.getString("name"));
                location.setText(value.getString("address"));
                number.setText(value.getString("number"));
            }
        });

    }

    public void passDeliveryData(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("deliveryShared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name delivery", name.getText().toString());
        editor.putString("address delivery", location.getText().toString());
        editor.putString("number delivery", number.getText().toString());
        editor.putString("total price", price_delivery.getText().toString());
        editor.putBoolean("door method",door_radio.isChecked());
        editor.apply();
    }

    public void getOneItemData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("orderDetails",MODE_PRIVATE);

        String total_price = sharedPreferences.getString("total price","");
        price_delivery.setText(String.valueOf(total_price));
    }

    public void getCartData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cartData",MODE_PRIVATE);

        price_delivery.setText(sharedPreferences.getString("total cart price",""));

    }
}