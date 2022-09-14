package com.example.Fashion_Store.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.Fashion_Store.Item_Details_Screen;
import com.example.Fashion_Store.Models.Items_Model;
import com.example.Fashion_Store.R;

import java.util.ArrayList;

public class Items_Grid_Adapter extends BaseAdapter {

    private Context context;
    ArrayList<Items_Model> items_models = new ArrayList<>();
    ArrayList<Items_Model> itemListFiltered;
    private LayoutInflater inflater;

    public Items_Grid_Adapter(Context context, ArrayList<Items_Model> items_models) {
        this.context = context;
        this.items_models = items_models;
        this.itemListFiltered = items_models;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items_models.size();
    }

    @Override
    public Object getItem(int i) {
        return items_models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setItems(ArrayList<Items_Model> items_models) {
        this.items_models = items_models;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null){
            view = inflater.inflate(R.layout.fashion_items,null);
        }


        ImageView item_img = view.findViewById(R.id.item_img);
        TextView item_name = view.findViewById(R.id.item_name);
        TextView item_price = view.findViewById(R.id.item_price);
        TextView item_num = view.findViewById(R.id.item_num);

        Glide.with(view.getContext()).load(items_models.get(i).getItem_img()).into(item_img);
        item_name.setText(items_models.get(i).getItem_name());
        item_price.setText(items_models.get(i).getItem_price());
        item_num.setText(String.valueOf(items_models.get(i).getItem_num()));

        final Items_Model data_position = items_models.get(i);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Item_Details_Screen.class);
                intent.putExtra("item img", data_position.getItem_img());
                intent.putExtra("item name", data_position.getItem_name());
                intent.putExtra("item price", data_position.getItem_price());
                intent.putExtra("item num", data_position.getItem_num());
                intent.putExtra("page title", data_position.getPage_title());
                view.getContext().startActivity(intent);

            }
        });




        return view;
    }

}
