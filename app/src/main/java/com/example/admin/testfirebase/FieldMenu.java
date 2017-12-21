package com.example.admin.testfirebase;

/**
 * Created by Kim Long on 21/12/2017.
 */

public class FieldMenu {
    String name;
    String address;
    String url;
    float rating;
    public FieldMenu() {}

    public FieldMenu(String name, String address, String url,float rating) {
        this.name = name;
        this.address = address;
        this.url=url;
        this.rating=rating;
    }
    public FieldMenu(FieldMenu f) {
        this.name=f.getName();
        this.address=f.getAddress();
        this.url=f.getUrl();
        this.rating=f.getRating();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
