package com.example.Fashion_Store.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.Fashion_Store.R;
import com.example.Fashion_Store.Models.ViewPager_Category_Model;

import java.util.ArrayList;

public class ViewPager_Category_Adapter extends PagerAdapter {

    private ArrayList<ViewPager_Category_Model> imageList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public ViewPager_Category_Adapter(ArrayList<ViewPager_Category_Model> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;

    }


    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.viewpager_category_items,container,false);

        ImageView imageView = view.findViewById(R.id.image_category_item);

        Glide.with(view.getContext()).load(imageList.get(position).getImage_category()).into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}
