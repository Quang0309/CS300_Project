package com.example.admin.testfirebase;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import static com.example.admin.testfirebase.R.id.field_name;

import static com.example.admin.testfirebase.R.id.tabHost;

public class RoomDetail extends AppCompatActivity {
    Context mContext;
    AlarmManager mAlarmManager;
    Calendar mCalendar;
    PendingIntent alarmIntent;
    Intent myIntent;
    boolean isAlarmOn = false;
    boolean isAlarmOff = true;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRefUser;
    private DatabaseReference mRefMessage;
    private Firebase mRefRoom;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> players;

    FloatingActionButton btnFloat;
    DatabaseReference messRef;
    EditText txtMess;
    String name;
    ListView listview;
    MessAdapter Adapter;
    ArrayList<Message> data;
    TabHost tabHost;
    String id;

    ImageView imgField;
    double avgAge = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost);

        mRefMessage = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testfirebase-27f0c.firebaseio.com/message");
        mRefUser = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testfirebase-27f0c.firebaseio.com/user");
        mRefRoom = new Firebase("https://lobby-3b4a3.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String test = currentUser.getDisplayName();
        final Room room = (Room) getIntent().getSerializableExtra("room");


        id = room.getId();

        final TextView fieldName = (TextView) findViewById(R.id.field_name);
        TextView fieldAddress = (TextView) findViewById(R.id.field_address);
        final TextView matchDate = (TextView) findViewById(R.id.match_date);
        final TextView matchTime = (TextView) findViewById(R.id.match_time);
        ListView lvPlayers = (ListView) findViewById(R.id.list_players);
        final Button btnJoin = (Button) findViewById(R.id.btn_join);
        final Button btnLeave = (Button) findViewById(R.id.btn_leave);
        final ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        final TextView count = (TextView) findViewById(R.id.count);

        this.mContext = this;
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mCalendar = Calendar.getInstance();
        myIntent = new Intent(this.mContext, AlarmReceiver.class);

        init();
        setImage();
        players = new ArrayList<>();
        final ArrayList<String> arrPlayers = new ArrayList<>();     // array id cua may thang trong room
        for (int i = 0; i < room.getPlayers().length(); i = i + 28) {

            String tmp = room.getPlayers().substring(i, i + 28);
            arrPlayers.add(tmp);
        }

        mRefUser.addChildEventListener(new ChildEventListener() {
            double ageTemp = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Person p = dataSnapshot.getValue(Person.class);

                for (String strPlayer : arrPlayers) {
                    if (strPlayer.equals(p.getUID())) {

                        for (String player : arrPlayers) {
                            if (player.equals(currentUser.getUid()) && isAlarmOn == false) {
                                String date = matchDate.getText().toString();
                                String time = matchTime.getText().toString();
                                time = time + ":00";
                                setAlarmOn(date, time);
                                isAlarmOn = true;
                                isAlarmOff = false;
                            }
                        }

                        Calendar calendar = Calendar.getInstance();
                        int curyear = calendar.get(Calendar.YEAR);
                        int year = Integer.parseInt(p.getDOB().substring(6,10));
                        int age = curyear - year;

                        double temp = (double) age;
                        ageTemp = ageTemp + temp;

                        String information;
                        information = p.getName() + "\n" + p.getNick() + " - age "+age+"\nPhone "+p.getPhone();
                        players.add(information);                   // array ten cua may thang trong room
                        break;
                    }
                }
                adapter.notifyDataSetChanged();

                if (players.size() >= 9)
                    notificationPopUp("ROOM NOTIFICATION", "10 people has join the room");
                count.setText(String.valueOf(players.size()) + "/10" + " players");

                if (players.size() == 0)
                    avgAge = 0.0;
                else
                    avgAge = ageTemp/(players.size());

                //3 Buttons
                btnJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean notIn = true;
                        for (String player : arrPlayers) {

                            if (player.equals(currentUser.getUid())) {
                                notIn = false;
                                Toast.makeText(RoomDetail.this, "you're already in this room", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if (notIn) {
                            String date = matchDate.getText().toString();
                            String time = matchTime.getText().toString();
                            time = time + ":00";
                            setAlarmOn(date, time);
                            isAlarmOn = true;
                            isAlarmOff = false;

                            arrPlayers.add(currentUser.getUid());
                            players.add(currentUser.getDisplayName());
                            adapter.notifyDataSetChanged();
                            count.setText(String.valueOf(players.size   ()) + "/10");
                        }
                    }
                });
                btnLeave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isAlarmOff == false) {
                            setAlarmOff();
                            isAlarmOff = true;
                        }
                        int i = 0;
                        boolean out = true;
                        for (String player : arrPlayers) {

                            if (player.equals(currentUser.getUid())) {

                                out = false;
                                arrPlayers.remove(i);
                                Iterator<String> it = players.iterator();
                                while (it.hasNext()) {

                                    String name = it.next();
                                    if (name.contains(currentUser.getDisplayName())) {

                                        it.remove();
                                        break;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                count.setText(String.valueOf(players.size()) + "/10");
                                break;
                            }
                            i = i + 1;
                        }
                        if (out) {

                            Toast.makeText(RoomDetail.this, "You are not in this room!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strPlayer = new String();
                        for (String player : arrPlayers) {

                            strPlayer = strPlayer + player;
                        }
                        room.setPlayers(strPlayer);
                        mRefRoom.child(room.getId()).setValue(room);

                        finish();
                    }
                });
                room.setAvgAge(avgAge);
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
        });

        fieldName.setText(room.getFieldName());
        fieldAddress.setText(room.getFieldAddress());
        matchDate.setText(room.getDate());
        matchTime.setText(room.getTime());

        adapter = new ArrayAdapter<String>(this,
                R.layout.roomdetailitem, players);
        lvPlayers.setAdapter(adapter);

        displayChatMessage();

        btnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = txtMess.getText().toString();
                if( input.equals(""))
                    return;
                else
                {
                    Message mess = new Message(input,name);
                    messRef.push().setValue(mess);
                    Adapter.add(mess);
                    txtMess.setText("");
                }
            }
        });
    }

    private void setImage() {

        if (Integer.valueOf(id) % 13 == 0) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/vf50c2.jpg?alt=media&token=90954e66-cc22-4aa7-bd47-984e1a488474").into(imgField);
        } else if (Integer.valueOf(id) % 13 == 1) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/su-dung-co-nhan-tao-cho-san-bong-360x250.jpg?alt=media&token=1bbdfb07-f1e1-4835-a446-ec68b88e3546").into(imgField);
        }
        else if(Integer.valueOf(id) % 13 == 2)
        {
            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/san-bong-co-nhan-tao-fpt-1.jpg?alt=media&token=ea417ff9-ecb3-41b0-9559-7715cb08b2fa").into(imgField);
        }
        else if (Integer.valueOf(id) % 13 == 3) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/Co-nhan-tao-san-bong-Nguyen-Gia-300x182.jpg?alt=media&token=55a0cb7c-6c8d-4dab-ab0c-fc3cfd421b9d").into(imgField);
        }
        else if (Integer.valueOf(id) % 13 == 4) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/8.jpg?alt=media&token=8eb2dd99-a15b-4240-9998-316309a6f0ee").into(imgField);
        }
        else if (Integer.valueOf(id) % 13 == 5) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/7.jpg?alt=media&token=26cce9d0-1ad0-40e0-a6d3-f790a5f5957e").into(imgField);
        }
        else if (Integer.valueOf(id) % 13 == 6) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/6.jpg?alt=media&token=d56f5025-b99c-47fd-a83d-4a2807be3303").into(imgField);
        }
        else if (Integer.valueOf(id) % 13 == 7) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/5.jpg?alt=media&token=9ad4b67e-c5e2-4b02-a3d0-4334c27876f1").into(imgField);
        }
        else if (Integer.valueOf(id) % 13 == 8) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/4.jpg?alt=media&token=3fcb291d-276b-4502-8540-76d3691f32f2").into(imgField);
        }
        else if (Integer.valueOf(id) % 13 == 9) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/3.jpg?alt=media&token=9a99aa47-1145-4078-8856-ec36ea454115").into(imgField);
        }
        else if (Integer.valueOf(id) % 13 == 10) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/2.jpg?alt=media&token=56636f86-364b-481a-974c-64c043fd58a0").into(imgField);
        }
        else if (Integer.valueOf(id) % 13 == 11) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/10.jpg?alt=media&token=caf6ab9a-4554-4330-9a02-a515289cf8eb").into(imgField);
        }
        else if (Integer.valueOf(id) % 13 == 12) {

            Glide.with(RoomDetail.this).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/1.jpg?alt=media&token=b28459a2-350d-4fdd-b081-613daeb8b07e").into(imgField);
        }
    }

    private void init() {
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("",getResources().getDrawable(R.drawable.picture1));
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("", getResources().getDrawable(R.drawable.ic_message_black_24dp));
        tabHost.addTab(tab2);

        imgField = (ImageView) findViewById(R.id.imgField);
        btnFloat = (FloatingActionButton) findViewById(R.id.btnFloat);
        listview = (ListView) findViewById(R.id.listMess);
        txtMess = (EditText) findViewById(R.id.txtMessage);
        messRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testfirebase-27f0c.firebaseio.com/message/"+ id);
        data = new ArrayList<>();
        Adapter = new MessAdapter(RoomDetail.this,R.layout.layout_message,data);
        name = currentUser.getDisplayName();
        listview.setAdapter(Adapter);
    }

    private void displayChatMessage() {
        messRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Room roomTemp = (Room) getIntent().getSerializableExtra("room");
                Message messTemp = new Message("","");
                data.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    com.example.admin.testfirebase.Message mess = snapshot.getValue(com.example.admin.testfirebase.Message.class);
                    data.add(mess);
                    messTemp = mess;
                }

                if (messTemp.getMessageUser() != currentUser.getDisplayName())
                    if(messTemp.getMessageUser() != "" && messTemp.getMessageText() != "")
                        notificationPopUp("Room: " + roomTemp.getFieldName(),messTemp.getMessageUser()+ ": " + messTemp.getMessageText());

                Adapter.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ImageButton btn = (ImageButton) findViewById(R.id.btn_back);
        btn.callOnClick();
    }

    private void alarmToast(String s) {
        Toast.makeText(RoomDetail.this, s, Toast.LENGTH_SHORT).show();
    }

    private int getDate(String s) {
        int count = 0;
        int start = 0;
        int end = 0;

        while (true){
            char c = s.charAt(end);
            if (c == '-')
                ++count;
            if (count == 1)
                break;
            ++end;
        }

        String sDate = s.substring(start, end);
        int iDate = Integer.parseInt(sDate);

        return iDate;
    }

    private int getMonth(String s) {
        int count = 0;
        int start = 0;
        int end = 0;
        while (true){
            char c = s.charAt(end);
            if (c == '-')
                ++count;
            if (c == '-' && count == 1)
                start = end + 1;
            if (count == 2)
                break;
            ++end;
        }

        String sMonth = s.substring(start, end);
        int iMonth = Integer.parseInt(sMonth);

        return iMonth;
    }

    private int getYear(String s) {
        int count = 0;
        int start = 0;
        int end = 0;


        while (end < s.length()){
            char c = s.charAt(end);
            if (c == '-')
                ++count;
            if (c == '-' && count == 2)
                start = end + 1;
            ++end;
        }

        String sYear = s.substring(start, end);
        int iYear = Integer.parseInt(sYear);

        return iYear;
    }

    private int getHour(String s) {
        int count = 0;
        int start = 0;
        int end = 0;

        while (true){
            char c = s.charAt(end);
            if (c == ':')
                ++count;
            if (count == 1)
                break;
            ++end;
        }

        String sHour = s.substring(start, end);
        int iHour = Integer.parseInt(sHour);

        return iHour;
    }

    private int getMinute(String s) {
        int count = 0;
        int start = 0;
        int end = 0;
        while (true){
            char c = s.charAt(end);
            if (c == ':')
                ++count;
            if (c == ':' && count == 1)
                start = end + 1;
            if (count == 2)
                break;
            ++end;
        }

        String sMinute = s.substring(start, end);
        int iMinute = Integer.parseInt(sMinute);

        return iMinute;
    }

    private int getSecond(String s) {
        int count = 0;
        int start = 0;
        int end = 0;

        while (end < s.length()){
            char c = s.charAt(end);
            if (c == ':')
                ++count;
            if (c == ':' && count == 2)
                start = end + 1;
            ++end;
        }

        String sSecond = s.substring(start, end);
        int iSecond = Integer.parseInt(sSecond);

        return iSecond;
    }

    private void setAlarmOn(String sDate, String sTime) {
        alarmToast("Alarm is on");

        int date = getDate(sDate);
        int month = getMonth(sDate);
        int year = getYear(sDate);
        int hour = getHour(sTime);
        int minute = getMinute(sTime);
        int second = getSecond(sTime);

        mCalendar.set(mCalendar.DAY_OF_MONTH, date);
        mCalendar.set(mCalendar.MONTH, month - 1);
        mCalendar.set(mCalendar.YEAR, year);
        mCalendar.set(mCalendar.HOUR_OF_DAY, hour - 1);
        mCalendar.set(mCalendar.MINUTE, minute);
        mCalendar.set(mCalendar.SECOND, second);

        Boolean temp = false;
        myIntent.putExtra("extra", "on");
        alarmIntent = PendingIntent.getBroadcast(RoomDetail.this, 0, myIntent, 0);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 1000*60*1, alarmIntent);

        Calendar c = Calendar.getInstance();
        int curHour = c.get(Calendar.HOUR_OF_DAY);
        int curMinute = c.get(Calendar.MINUTE);
        int tempH;
        int tempM;
        if (curHour != hour) {
            tempH = hour - curHour - 1;
            tempM = minute - curMinute;
        }
        else {
            tempH = 0;
            tempM = 0;
        }
        final Handler mHandler = new Handler();
        int time = 1000*(30 + tempH*60*60 + tempM*60);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setAlarmOff();
            }
        }, time);

    }

    private void notificationPopUp(String title, String text) {
        Context context = this;

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.ic_notif);
        notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_notif));
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(title);
        notification.setContentText(text);

        final int notifyId = 2;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, notification.build());
    }

    private void setAlarmOff() {
        alarmToast("Alarm is off");
        mAlarmManager.cancel(alarmIntent);
        myIntent.putExtra("extra", "off");
        sendBroadcast(myIntent);
    }
}



