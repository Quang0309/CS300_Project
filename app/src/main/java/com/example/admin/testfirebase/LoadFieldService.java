package com.example.admin.testfirebase;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by ADMIN on 29-Apr-18.
 */

public class LoadFieldService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private Firebase mRef2;
    static String Tag = "LoadFieldService";
    public LoadFieldService() {
        super(Tag);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mRef2=new Firebase("https://testmap-60706.firebaseio.com/");
        mRef2.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
    });}
}
