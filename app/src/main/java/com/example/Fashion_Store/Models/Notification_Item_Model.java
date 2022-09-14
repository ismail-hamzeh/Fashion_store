package com.example.Fashion_Store.Models;

public class Notification_Item_Model {

    String notify, id;

    public Notification_Item_Model(String notify, String id) {
        this.notify = notify;
        this.id= id;
    }

    public Notification_Item_Model() {

    }


    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
