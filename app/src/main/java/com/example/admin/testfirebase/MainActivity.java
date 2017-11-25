package com.example.admin.testfirebase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /*DatabaseReference mRef;
    Button btnSend;
    EditText txtName, txtMail,txtDOB;

    ListView lvHistory;
    ArrayList<Person> list;
    PersonArrayAdapter adapter;
    StorageReference sRef;
    ImageView imgPer;
    FirebaseUser user;
    static int code = 1;*/
    TabHost tabHost;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button btnCreateRoom;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int roomId;
    private String name;
    private String address;
    private String date;
    private String time;
    private String userId;
    private String playerArr;
    private RecyclerView rv;
    private List<Room> rooms;
    private Firebase mRef;
    private FirebaseAuth mAuth;
    static FirebaseUser currentUser;
    private RVAdapter adapter;

    //ListView navigation_listview;
    //ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_bar);
        btnCreateRoom = (Button) findViewById(R.id.btn_create);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.btnInfo) {
                    infor_onclick();
                } else if (item.getItemId() == R.id.btnLogout)
                    logout_onclick();
                return true;
            }
        });
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("", getResources().getDrawable(R.drawable.picture1));
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("", getResources().getDrawable(R.drawable.picture2));
        tabHost.addTab(tab2);

        mRef = new Firebase("https://lobby-3b4a3.firebaseio.com/");
        populateListRoom();
        loadUser();

        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_create_room);
                dialog.setTitle("Title...");


                final EditText etFieldName = (EditText) dialog.findViewById(R.id.et_field_name);
                final EditText etAddress = (EditText) dialog.findViewById(R.id.et_field_address);
                final EditText etDate = (EditText) dialog.findViewById(R.id.et_date);
                final EditText etTime = (EditText) dialog.findViewById(R.id.et_time);
                Button btnCreateDialog = (Button) dialog.findViewById(R.id.btn_create_dialog);
                Button btnCancelDialog = (Button) dialog.findViewById(R.id.btn_cancel_dialog);

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

                                        etDate.setText(dayOfMonth + "-" + monthOfYear + "-" + year);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                etTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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
                                        String shour = Integer.toString(hourOfDay);
                                        String smin = Integer.toString(minute);
                                        String res = ("00" + shour).substring(shour.length()) + ":" + ("00" + smin).substring(smin.length());
                                        etTime.setText(res);
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

                        name = etFieldName.getText().toString();
                        address = etAddress.getText().toString();
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

    private void loadUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();
    }

    //init();

    //loadHistory();


       /* imgPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, code);
            }
        });*/


    public void infor_onclick() {
        Toast.makeText(getApplicationContext(), "vo dc ham intent", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(intent);
    }

    public void logout_onclick() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "vo dc ham intent", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent1);
        finish();
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
