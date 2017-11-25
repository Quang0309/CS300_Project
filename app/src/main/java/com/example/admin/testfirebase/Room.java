package com.example.admin.testfirebase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by NKT on 11/9/2017.
 */
public class Room implements Serializable {
    private int id;
    private String fieldName, fieldAddress;
    private String date, time;
    private String players;

    public void setId(int id) {
        this.id = id;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldAddress(String fieldAddress) {
        this.fieldAddress = fieldAddress;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public Room(int id, String fieldName, String fieldAddress, String date, String time, String players) {
        this.id = id;
        this.fieldName = fieldName;
        this.fieldAddress = fieldAddress;
        this.date = date;
        this.time = time;
        this.players = players;
    }

    public Room() {
    }

    public String getId() {
        return Integer.toString(id);
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldAddress() {
        return fieldAddress;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPlayers() {
        return players;
    }
}
