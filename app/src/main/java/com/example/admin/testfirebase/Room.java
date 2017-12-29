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
    private double avgAge;

    public void setId(int id) {
        this.id = id;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public double getAvgAge() {
        return avgAge;
    }

    public void setAvgAge(double avgAge) {
        this.avgAge = avgAge;
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

    private int getIntDate() {
        int count = 0;
        int start = 0;
        int end = 0;

        while (true){
            char c = date.charAt(end);
            if (c == '-')
                ++count;
            if (count == 1)
                break;
            ++end;
        }

        String sDate = date.substring(start, end);
        int iDate = Integer.parseInt(sDate);

        return iDate;
    }

    private int getIntMonth() {
        int count = 0;
        int start = 0;
        int end = 0;
        while (true){
            char c = date.charAt(end);
            if (c == '-')
                ++count;
            if (c == '-' && count == 1)
                start = end + 1;
            if (count == 2)
                break;
            ++end;
        }

        String sMonth = date.substring(start, end);
        int iMonth = Integer.parseInt(sMonth);

        return iMonth;
    }

    private int getIntYear() {
        int count = 0;
        int start = 0;
        int end = 0;


        while (end < date.length()){
            char c = date.charAt(end);
            if (c == '-')
                ++count;
            if (c == '-' && count == 2)
                start = end + 1;
            ++end;
        }

        String sYear = date.substring(start, end);
        int iYear = Integer.parseInt(sYear);

        return iYear;
    }

    private int getIntHour() {
        int count = 0;
        int start = 0;
        int end = 0;

        while (true){
            char c = date.charAt(end);
            if (c == ':')
                ++count;
            if (count == 1)
                break;
            ++end;
        }

        String sHour = date.substring(start, end);
        int iHour = Integer.parseInt(sHour);

        return iHour;
    }

    private int getIntMinute() {
        int count = 0;
        int start = 0;
        int end = 0;
        while (true){
            char c = date.charAt(end);
            if (c == ':')
                ++count;
            if (c == ':' && count == 1)
                start = end + 1;
            if (count == 2)
                break;
            ++end;
        }

        String sMinute = date.substring(start, end);
        int iMinute = Integer.parseInt(sMinute);

        return iMinute;
    }

}
