package com.example.admin.testfirebase;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ListFieldActivity extends AppCompatActivity {
    ListView listViewField;
    ListFieldMenuAdapter fieldMenuAdapter;
    static ArrayList<FieldMenu> fieldMenuArrayList;
    static ArrayList<String> keyArrayList;
    String district,UserID;
    DatabaseReference mRef;
    Firebase mRef2;
    StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_field);

        district = getIntent().getExtras().getString("District");
        UserID=getIntent().getStringExtra("UserID");


        initialize();
        loadData(district);
    }
    private void loadData(String district) {
       /* mRef= FirebaseDatabase.getInstance().getReference().child(district);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fieldMenuArrayList.clear();
                keyArrayList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    FieldMenu field=snapshot.getValue(FieldMenu.class);
                    if (field!=null) {
                        fieldMenuArrayList.add(field);
                        keyArrayList.add(snapshot.getKey());
                    }
                }
                fieldMenuAdapter.clear();
                fieldMenuAdapter.addAll(fieldMenuArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        mRef2= new Firebase("https://testmap-60706.firebaseio.com/");
        mRef2=mRef2.child(district);
        mRef2.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                fieldMenuArrayList.clear();
                keyArrayList.clear();
                for (com.firebase.client.DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    FieldMenu field=snapshot.getValue(FieldMenu.class);
                    if (field!=null) {
                        fieldMenuArrayList.add(field);
                        keyArrayList.add(snapshot.getKey());
                    }
                }
                fieldMenuAdapter.clear();
                fieldMenuAdapter.addAll(fieldMenuArrayList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void initialize() {
        keyArrayList=new ArrayList<>();
        listViewField = (ListView) findViewById(R.id.list_view_field);
        fieldMenuAdapter = new ListFieldMenuAdapter(this);
        fieldMenuArrayList = new ArrayList<>();

        listViewField.setAdapter(fieldMenuAdapter);
        listViewField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListFieldActivity.this, FieldDetailActivity.class);
                intent.putExtra("district",district);
                intent.putExtra("key",keyArrayList.get(position));
                intent.putExtra("UserID",UserID);
                startActivity(intent);
            }
        });
    }
    static public FieldMenu getField ( String key) {
        for (int i=0;i<keyArrayList.size();++i) {
            if (keyArrayList.get(i).toString().equals(key)) {
                return fieldMenuArrayList.get(i);
            }
        }
        return null;
    }
}
