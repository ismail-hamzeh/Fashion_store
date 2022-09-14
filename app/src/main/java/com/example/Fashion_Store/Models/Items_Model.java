package com.example.Fashion_Store.Models;

public class Items_Model {

    int item_num;
    String item_img, item_name, item_price, page_title;

    public Items_Model(int item_num, String item_img, String item_name, String item_price, String page_title) {
        this.item_num = item_num;
        this.item_img = item_img;
        this.item_name = item_name;
        this.item_price = item_price;
        this.page_title = page_title;
    }

    public Items_Model() {

    }


    public int getItem_num() {
        return item_num;
    }

    public void setItem_num(int item_num) {
        this.item_num = item_num;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }
}
