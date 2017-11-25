package com.example.admin.testfirebase;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by NKT on 11/17/2017.
 */
public class TestFireBase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
