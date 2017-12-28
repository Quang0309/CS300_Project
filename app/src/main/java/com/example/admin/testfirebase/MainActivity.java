package com.example.admin.testfirebase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button btnCreateRoom;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int roomId;
    private String name;
    private String username;
    private String address;
    private String date;
    private String time;
    private String userId;
    private String playerArr;
    private RecyclerView rv;
    private List<Room> rooms;
    private Firebase mRef,mRef2,mRefField;
    private DatabaseReference uRef;
    private DatabaseReference mRefMessage;
    private FirebaseAuth mAuth;
    static FirebaseUser currentUser;
    private RVAdapter adapter;
    ActionBarDrawerToggle mToggle;
    CircleImageView circle_avatar;
    ArrayList<String> arrayList,arrayListFieldName,getArrayListFielAddress;
    ArrayAdapter<String> arrayAdapter,arrayAdapter2;
    ListFieldMenuAdapter fieldAdapter;
    String district;
    com.firebase.client.ValueEventListener mVe2,mVeField;
    int position_of_field_in_array;


    String URL = "";
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        btnCreateRoom = (Button) findViewById(R.id.btn_create);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.btnInfo) {
                    infor_onclick();
                } else if (item.getItemId() == R.id.btnLogout)
                    logout_onclick();
                else if (item.getItemId() == R.id.btnUploadField)
                {
                    uploadField_onclick();
                }
                else if (item.getItemId() == R.id.btnListField)
                {
                    listField_onclick();
                }
                return true;
            }
        });

        mRefMessage = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testfirebase-27f0c.firebaseio.com/message");
        mRef = new Firebase("https://lobby-3b4a3.firebaseio.com/");

        createSidebar();
        loadAvatar();
        populateListRoom();
        loadUser();

        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_create_room);
                dialog.setTitle("Title...");


                final Spinner spinnerDistrict = (Spinner) dialog.findViewById(R.id.spinner_district);
                final Spinner spinnerField = (Spinner) dialog.findViewById(R.id.spinner_field);
                final EditText etDate = (EditText) dialog.findViewById(R.id.et_date);
                final EditText etTime = (EditText) dialog.findViewById(R.id.et_time);
                Button btnCreateDialog = (Button) dialog.findViewById(R.id.btn_create_dialog);
                Button btnCancelDialog = (Button) dialog.findViewById(R.id.btn_cancel_dialog);

                mRef2=new Firebase("https://testmap-60706.firebaseio.com/");
                mVe2=new  com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                        arrayList = new ArrayList<>();
                        for (com.firebase.client.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            arrayList.add(snapshot.getKey().toString());
                        }
                        arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item, arrayList);
                        spinnerDistrict.setAdapter(arrayAdapter);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                };
                mRef2.addValueEventListener(mVe2);
                spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
                        district = parent.getItemAtPosition(position).toString();
                        mRefField=new Firebase("https://testmap-60706.firebaseio.com/").child(district);
                        mVeField=new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                arrayListFieldName=new ArrayList<>();
                                getArrayListFielAddress=new ArrayList<>();
                                for (com.firebase.client.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    FieldMenu field=snapshot.getValue(FieldMenu.class);
                                    if (field!=null) {
                                        arrayListFieldName.add(field.getName().toString());
                                        getArrayListFielAddress.add(field.getAddress().toString());
                                    }
                                }
                                //fieldAdapter.clear();
                                //fieldAdapter.addAll(arrayListField);
                                //spinnerField.setAdapter(fieldAdapter);
                                arrayAdapter2=new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item, arrayListFieldName);
                                spinnerField.setAdapter(arrayAdapter2);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        };
                        mRefField.addValueEventListener(mVeField);
                        mRefField.removeEventListener(mVeField);
                        spinnerField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, final int position, long id) {
                               position_of_field_in_array = position;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                etDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        monthOfYear = monthOfYear + 1;
                                        if (dateConstraint(dayOfMonth, monthOfYear, year))
                                            etDate.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
                                        else
                                            makeToast("Your schedule is not valid please input again");

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                etTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeToast("Remember you match must be at least 1 hour after the current time");
                        // Get Current Time
                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        if (timeConstraint(hourOfDay, minute)) {
                                            String shour = Integer.toString(hourOfDay);
                                            String smin = Integer.toString(minute);
                                            String res = ("00" + shour).substring(shour.length()) + ":" + ("00" + smin).substring(smin.length());
                                            etTime.setText(res);
                                        }
                                        else
                                           makeToast("Your time is not valid please input again");
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();

                    }
                });

                btnCancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnCreateDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        name = arrayListFieldName.get(position_of_field_in_array);
                        address =getArrayListFielAddress.get(position_of_field_in_array);
                        date = etDate.getText().toString();
                        time = etTime.getText().toString();
                        playerArr = userId;
                        roomId = 1;
                        final boolean[] flag = {true};

                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {

                                    Room r = snapshot.getValue(Room.class);
                                    if(r.getId().equals(String.valueOf(roomId))) {

                                        roomId = roomId + 1;
                                    }
                                }
                                if (flag[0]) {
                                    Room r = new Room(roomId, name, address, date, time, playerArr);
                                    mRef.child(r.getId()).setValue(r);
                                    mRefMessage.child(r.getId()).setValue(0);
                                    flag[0] = false;
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "room created!", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        });

        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                RecyclerView.ViewHolder viewHolder
                        = recyclerView.findViewHolderForAdapterPosition(position);
                TextView textViewId
                        = (TextView) viewHolder.itemView.findViewById(R.id.match_id);
                String selectedId = textViewId.getText().toString().substring(1);;
                for (Room i:rooms) {
                    if (i.getId().equals(selectedId)) {
                        Intent detailIntent = new Intent(MainActivity.this, RoomDetail.class);
                        detailIntent.putExtra("room",i);
                        startActivity(detailIntent);
                        break;
                    }
                }
            }
        });
    }

    private void init()
    {
        mAuth = FirebaseAuth.getInstance();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_bar);
        uRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testfirebase-27f0c.firebaseio.com/user");
        fieldAdapter=new ListFieldMenuAdapter(this);
    }
    private void createSidebar() {
        mToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        drawerLayout.setDrawerListener(this);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    private void loadAvatar() {
        dialog = ProgressDialog.show(MainActivity.this,"","Loading",true,false);
        final FirebaseUser currentUser = mAuth.getCurrentUser(); // lay user trong authencation
        if(currentUser!=null)
        {

            uRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    for(com.google.firebase.database.DataSnapshot snapshot:dataSnapshot.getChildren()) { // xet tung thang user trong database
                        Person person = snapshot.getValue(Person.class); // bo thang user do vao object person
                        if (person.getUID().equals(currentUser.getUid()))  // neu thg user.getUID = cai userid lay trong authencation thi chinh la no
                        {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(person.getName()).build();
                            currentUser.updateProfile(profileUpdates);
                            username = person.getName();

                            if (!person.getURL().equals(""))
                            {
                                URL = person.getURL();

                            }
                            break;
                        }
                    }
                    onRetrieveDataSuccuss(username);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                dialog.dismiss();
            }
        }, 4000);

    }

    private void onRetrieveDataSuccuss(String username) {
        if(username==null)
        {
            Intent i = new Intent(MainActivity.this,InfoActivity.class);
            startActivity(i);
        }
    }

    private void loadUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();
    }

    public void infor_onclick() {

        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(intent);
    }

    public void logout_onclick() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent1);
        finish();
    }

    private void uploadField_onclick() {
        Intent intent=new Intent(MainActivity.this,UploadFieldActivity.class);
        intent.putExtra("UserID",currentUser.getUid());
        startActivity(intent);
    }

    public void listField_onclick() {
        Intent intent = new Intent(MainActivity.this,ListDistrictActivity.class);
        intent.putExtra("UserID",currentUser.getUid());
        startActivity(intent);
    }

    private void populateListRoom() {
        rooms = new ArrayList<>();
        adapter = new RVAdapter(rooms);
        rv.setAdapter(adapter);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Room r = dataSnapshot.getValue(Room.class);
                rooms.add(r);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Room newRoom = dataSnapshot.getValue(Room.class);
                int i = 0;
                for (Room r:rooms) {

                    if (r.getId().equals(newRoom.getId())) {

                        rooms.set(i,newRoom);
                        break;
                    }
                    i = i + 1;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        circle_avatar = (CircleImageView) findViewById(R.id.circle_avatar);

        if (URL.equals(""))
            circle_avatar.setImageResource(R.drawable.ic_avatar);
        else
            Glide.with(MainActivity.this).load(URL).into(circle_avatar);
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }


    //Adding constraints for app
    boolean dateConstraint(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        if (year >= c.get(Calendar.YEAR))
            if(month >= c.get(Calendar.MONTH))
                if (day >= c.get(Calendar.DAY_OF_MONTH))
                    return true;
                else
                    return false;
            else
                return false;
        else
            return false;
    }

    boolean timeConstraint(int hour, int minute) {
        Date d = Calendar.getInstance().getTime();
        if (hour > d.getHours())
            if(minute >= d.getMinutes())
                return true;
            else
                if (hour - d.getHours() > 1)
                    return true;
                else
                    return false;
        else
            return false;
    }
    //Additional functions
    void makeToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
    }

    /* @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }*/
    /*private void loadHistory() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                list.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Person person = snapshot.getValue(Person.class);
                    if (person !=null)
                        list.add(person);
                    else
                        Toast.makeText(MainActivity.this, "NULLLLLL", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "onDataChange", Toast.LENGTH_SHORT).show();
                onValueReceived();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        /*
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person per = dataSnapshot.getValue(Person.class);
                list.add(per);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    //}

    /*private void onValueReceived() {
        
    }

    private void init() {
        btnSend = (Button) findViewById(R.id.btnSend);
        txtName = (EditText) findViewById(R.id.txtName);
        txtMail = (EditText) findViewById(R.id.txtMail);
        txtDOB = (EditText) findViewById(R.id.txtDOB);
        lvHistory = (ListView) findViewById(R.id.lvHistory);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("",getResources().getDrawable(R.drawable.picture1));
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("",getResources().getDrawable(R.drawable.picture2));
        tabHost.addTab(tab2);
        list = new ArrayList<>();
        adapter = new PersonArrayAdapter(this,R.layout.layou3,list);
        lvHistory.setAdapter(adapter);
        sRef = FirebaseStorage.getInstance().getReference();
        imgPer = (ImageView) findViewById(R.id.imgPer);
        user = FirebaseAuth.getInstance().getCurrentUser();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == code && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            *//*String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();*//*
            StorageReference filepath = sRef.child( user.getUid()).child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }
            });
        }
    }*/
}
