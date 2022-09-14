package com.example.Fashion_Store.home_category_tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Fashion_Store.R;
import com.example.Fashion_Store.Adapters.ViewPager_Category_Adapter;
import com.example.Fashion_Store.Models.ViewPager_Category_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class All_tab extends Fragment {

   public static All_tab getInstance(){
       All_tab all_tab = new All_tab();
       return all_tab;
   }

    private ViewPager viewPager;
    private ViewPager_Category_Adapter viewPager_category_adapter;
    ArrayList<ViewPager_Category_Model> imageList = new ArrayList<>();
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_tab, container, false);


        viewPager = view.findViewById(R.id.viewPager_all);
        reference = FirebaseDatabase.getInstance().getReference();

        clearAll();
        getDataDB();

        return view;
    }

    public void getDataDB(){
        reference.child("all").limitToFirst(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ViewPager_Category_Model viewPagerCategoryModel = new ViewPager_Category_Model();
                    viewPagerCategoryModel.setImage_category(String.valueOf(dataSnapshot.child("img").getValue()));

                    imageList.add(viewPagerCategoryModel);
                }
                imageList = new ArrayList<>(imageList);
                viewPager_category_adapter = new ViewPager_Category_Adapter(imageList,getContext());
                viewPager.setAdapter(viewPager_category_adapter);
                viewPager.setPadding(0,0,600,0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void clearAll(){
        if (imageList != null) {
            imageList.clear();

            if (viewPager_category_adapter != null){
                viewPager_category_adapter.notifyDataSetChanged();
            }
        }
        imageList = new ArrayList<>();
    }
}