package com.example.Fashion_Store.Models;

public class Order_Item_Model {

    String id, product_num, total_price;

    public Order_Item_Model(String id, String product_num, String total_price) {
        this.id = id;
        this.product_num = product_num;
        this.total_price = total_price;
    }

    public Order_Item_Model() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
