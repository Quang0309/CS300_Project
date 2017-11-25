package com.example.admin.testfirebase;

import android.net.Uri;

import java.net.URI;

/**
 * Created by ADMIN on 19-Oct-17.
 */

public class Person {
    private String name;
    private String nick;
    private String phone;
    private String DOB;
    private String Address;
    private String URL="";
    private String UID="";
    public Person() {
    }

    public Person(String name,String nick, String address, String DOB, String phone,String URL, String UID) {
        this.name = name;
        this.phone = phone;
        this.DOB = DOB;
        this.nick = nick;
        Address = address;
        this.URL = URL;
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
