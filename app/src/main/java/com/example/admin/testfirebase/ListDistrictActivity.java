package com.example.admin.testfirebase;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListDistrictActivity extends AppCompatActivity {
    RecyclerView listViewDistrict;
    ListDistrictMenuAdapter districtMenuAdapter;
    ArrayList<DistrictMenu> districtMenuArrayList;


    public String UserID;
    private Firebase mRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_district);

        UserID=getIntent().getStringExtra("UserID");
        districtMenuArrayList=new ArrayList<>();
        //z` initializeData();

        loadData();
    }
    private void loadData() {
        mRef2=new Firebase("https://testmap-60706.firebaseio.com/");
        mRef2.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
              //  districtMenuArrayList.clear();
                for (com.firebase.client.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String district = snapshot.getKey();
                    if (district != null && !district.equals("Rating")) {
                        districtMenuArrayList.add(new DistrictMenu(district));
                    }
                }
                //districtMenuAdapter.clear();
                districtMenuAdapter = new ListDistrictMenuAdapter(ListDistrictActivity.this,districtMenuArrayList,ListDistrictActivity.this,UserID);
                initialize();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // mRef=FirebaseDatabase.getInstance().getReference();
       /* mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                districtMenuArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String district = snapshot.getKey().toString();
                    if (district != null && !district.equals("Rating")) {
                        districtMenuArrayList.add(new DistrictMenu(district));
                    }
                }
                districtMenuAdapter.clear();
                districtMenuAdapter.addAll(districtMenuArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

       // districtMenuAdapter.addAll(districtMenuArrayList);
    }

    private void initialize() {
        listViewDistrict = (RecyclerView) findViewById(R.id.list_view_district);
        listViewDistrict.setLayoutManager(new GridLayoutManager(this,2));
        listViewDistrict.setAdapter(districtMenuAdapter);

        /*districtMenuAdapter= new ListDistrictMenuAdapter(this);
        districtMenuArrayList=new ArrayList<>();

        listViewDistrict.setAdapter(districtMenuAdapter);
        listViewDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListDistrictActivity.this,ListFieldActivity.class);
                String s= ((TextView)view.findViewById(R.id.text_view_district_name)).getText().toString();
                i.putExtra("District",s);
                i.putExtra("UserID",UserID);
                startActivity(i);
            }
        });*/
    }

}
