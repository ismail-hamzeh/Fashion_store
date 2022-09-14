package com.example.Fashion_Store.Models;

public class Cart_Item_Model {

    String id, img, name , price;
    int num;


    public Cart_Item_Model(String id, String img, String name, String price, int num) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.img = img;
        this.num = num;
    }

    public Cart_Item_Model() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
