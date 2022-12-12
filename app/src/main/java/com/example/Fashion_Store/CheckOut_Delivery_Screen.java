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

import com.example.Fashion_Store.databinding.FragmentCheckOutDeliveryScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CheckOut_Delivery_Screen extends Fragment {

    private FragmentCheckOutDeliveryScreenBinding binding;
//    private EditText name, location, number;
//    private TextView change_txt, delivery_method_txt, price_delivery;
//    private RadioButton door_radio, pickUp_radio;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean change = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCheckOutDeliveryScreenBinding.inflate(getLayoutInflater(), container, false);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("buyNowOrCart",MODE_PRIVATE);
        boolean buyNow = sharedPreferences.getBoolean("buyNow",true);

        if (buyNow) {
            getOneItemData();
        } else {
            getCartData();
        }
        getAddressDetails();


        binding.addressChangeTxt.setOnClickListener(view1 -> {
            if (change == false) {
                binding.nameEdt.setEnabled(true);
                binding.locationEdt.setEnabled(true);
                binding.numberEdt.setEnabled(true);
                change = true;
            } else {
                binding.nameEdt.setEnabled(false);
                binding.locationEdt.setEnabled(false);
                binding.numberEdt.setEnabled(false);
                change = false;
            }
        });

        binding.proceedToPaymentBtn.setOnClickListener((View.OnClickListener) view -> {
            if (binding.nameEdt.getText().toString().isEmpty()){
                binding.nameEdt.setError("Required");
            } else if (binding.locationEdt.getText().toString().isEmpty()){
                binding.locationEdt.setError("Required");
            } else if (binding.numberEdt.getText().toString().isEmpty()){
                binding.numberEdt.setError("Required");
            } else if (!binding.doorRb.isChecked() && !binding.pickUpRb.isChecked()){
                binding.deliveryMethodTxt.setError("Please choose method");
            } else {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_checkOut,
                        new CheckOut_Payment_Screen()).addToBackStack(null).commit();
                passDeliveryData();
            }
        });



        return binding.getRoot();
    }

    public void getAddressDetails(){

        String userID = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                binding.nameEdt.setText(value.getString("name"));
                binding.locationEdt.setText(value.getString("address"));
                binding.numberEdt.setText(value.getString("number"));
            }
        });

    }

    public void passDeliveryData(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("deliveryShared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name delivery", binding.nameEdt.getText().toString());
        editor.putString("address delivery", binding.locationEdt.getText().toString());
        editor.putString("number delivery", binding.numberEdt.getText().toString());
        editor.putString("total price", binding.priceTxt.getText().toString());
        editor.putBoolean("door method",binding.doorRb.isChecked());
        editor.apply();
    }

    public void getOneItemData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("orderDetails",MODE_PRIVATE);

        String total_price = sharedPreferences.getString("total price","");
        binding.priceTxt.setText(String.valueOf(total_price));
    }

    public void getCartData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cartData",MODE_PRIVATE);

        binding.priceTxt.setText(sharedPreferences.getString("total cart price",""));

    }
}