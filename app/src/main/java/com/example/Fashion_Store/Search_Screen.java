package com.example.Fashion_Store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.Fashion_Store.Adapters.Items_Grid_Adapter;
import com.example.Fashion_Store.Models.Items_Model;
import com.example.Fashion_Store.databinding.ActivitySearchScreenBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Search_Screen extends AppCompatActivity{

    private ActivitySearchScreenBinding binding;
    ArrayList<Items_Model> items_models = new ArrayList<>() ;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference();

        binding.searchView.setIconified(false);

        binding.backImg.setOnClickListener(view -> onBackPressed());


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                search(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                search(s);

                return false;
            }
        });


//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final Items_Model data_position = items_models.get(i);
//
//                Intent intent = new Intent(Search_Screen.this, Item_Details_Screen.class);
//                intent.putExtra("item img", data_position.getItem_img());
//                intent.putExtra("item name", data_position.getItem_name());
//                intent.putExtra("item price", data_position.getItem_price());
//                intent.putExtra("item num", data_position.getItem_num());
//                intent.putExtra("page title", data_position.getPage_title());
//                view.getContext().startActivity(intent);
//            }
//        });

    }
    private void search(String s) {

        String query = s.toUpperCase(
                Locale.ROOT);

        Query searchQuery = reference.child("all").orderByChild("name").startAt(query).endAt(query + "\uf8ff");
        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()){
                    items_models.clear();
                    for (DataSnapshot data : snapshot.getChildren()){
                        Items_Model items_model = new Items_Model();
                        items_model.setItem_img(String.valueOf(data.child("img").getValue()));
                        items_model.setItem_name(String.valueOf(data.child("name").getValue()));
                        items_model.setItem_num(Integer.parseInt(String.valueOf(data.child("number").getValue())));
                        items_model.setItem_price(String.valueOf(data.child("price").getValue()));
                        items_model.setPage_title(String.valueOf(data.child("title").getValue()));

                        if (!s.equals(data.child("name").getValue())){
                            binding.noItemSearchLinear.setVisibility(View.VISIBLE);
                        }

                        items_models.add(items_model);
                        Collections.shuffle(items_models);
                    }
                    Items_Grid_Adapter grid_adapter = new Items_Grid_Adapter(Search_Screen.this,items_models);
                    binding.gridView.setAdapter(grid_adapter);

                    if (s.isEmpty()){
                        binding.noItemSearchLinear.setVisibility(View.VISIBLE);
                        binding.gridView.setVisibility(View.GONE);
                        binding.resultNumTxt.setText("0");
                        grid_adapter.notifyDataSetChanged();
                    } else {
                        binding.gridView.setVisibility(View.VISIBLE);
                        binding.numResultLinear.setVisibility(View.VISIBLE);
                        binding.noItemSearchLinear.setVisibility(View.INVISIBLE);
                        binding.resultNumTxt.setText(String.valueOf(grid_adapter.getCount()));
                        grid_adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}