package com.example.Fashion_Store.Adapters;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.Fashion_Store.Models.Cart_Item_Model;
import com.example.Fashion_Store.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart_Item_Adapter extends BaseAdapter {

    private Context context;
    ArrayList<Cart_Item_Model> itemCartModels = new ArrayList<>();
    private LayoutInflater inflater;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    public Cart_Item_Adapter(Context context, ArrayList<Cart_Item_Model> itemCartModels) {
        this.context = context;
        this.itemCartModels = itemCartModels;
    }

    @Override
    public int getCount() {
        return itemCartModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null) {
            view = inflater.inflate(R.layout.cart_items, null);
        }

        ImageView item_plus = view.findViewById(R.id.cart_item_plus);
        ImageView item_minus = view.findViewById(R.id.cart_item_minus);
        ImageView item_img = view.findViewById(R.id.cart_item_img);
        TextView item_name = view.findViewById(R.id.cart_item_name);
        TextView item_price = view.findViewById(R.id.cart_item_price);
        TextView item_num = view.findViewById(R.id.cart_item_num);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        Glide.with(view.getContext()).load(itemCartModels.get(i).getImg()).into(item_img);
        item_name.setText(itemCartModels.get(i).getName());
        item_price.setText(itemCartModels.get(i).getPrice());
        item_num.setText(String.valueOf(itemCartModels.get(i).getNum()));

        String userID = mAuth.getCurrentUser().getUid();
        String pushID = itemCartModels.get(i).getId();
        final int[] num = {itemCartModels.get(i).getNum()};

        item_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num[0]++;

                item_num.setText(String.valueOf(num[0]));;

                int pr = parseInt(itemCartModels.get(i).getPrice());

                Map<String,Object> plus_item = new HashMap<>();
                plus_item.put("item num", String.valueOf(num[0]));
                plus_item.put("total price", String.valueOf(pr * num[0]));
                reference.child("Cart").child(userID).child(pushID).updateChildren(plus_item);
            }
        });

        item_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                num[0]--;

                item_num.setText(String.valueOf(num[0]));

                int pr = parseInt(itemCartModels.get(i).getPrice());

                Map<String,Object> plus_item = new HashMap<>();
                plus_item.put("item num", String.valueOf(num[0]));
                plus_item.put("total price", String.valueOf(pr * num[0]));
                reference.child("Cart").child(userID).child(pushID).updateChildren(plus_item);

                if (num[0] == 0) {

                    itemCartModels.remove(i);

                    reference.child("Cart").child(userID).orderByChild("id").equalTo(pushID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                data.getRef().removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        return view;
    }

//    public void getMyTotalQuantityAndPrice() {
//        String userID = mAuth.getCurrentUser().getUid();
//        final int[] globalsum = {0};
//
//        Query query = reference.child("Cart").child(userID).orderByChild("id");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                int price_sum = 0;
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    int total_price = parseInt(String.valueOf(dataSnapshot.child("total price").getValue()));
//                    price_sum = price_sum + total_price;
//
//                    globalsum[0] = price_sum;
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

}
