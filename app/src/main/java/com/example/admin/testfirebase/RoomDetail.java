package com.example.admin.testfirebase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Iterator;

import static com.example.admin.testfirebase.R.id.tabHost;

public class RoomDetail extends AppCompatActivity {
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
        TextView fieldName = (TextView) findViewById(R.id.field_name);
        TextView fieldAddress = (TextView) findViewById(R.id.field_address);
        TextView matchDate = (TextView) findViewById(R.id.match_date);
        TextView matchTime = (TextView) findViewById(R.id.match_time);
        ListView lvPlayers = (ListView) findViewById(R.id.list_players);
        final Button btnJoin = (Button) findViewById(R.id.btn_join);
        final Button btnLeave = (Button) findViewById(R.id.btn_leave);
        final Button btnBack = (Button) findViewById(R.id.btn_back);
        final TextView count = (TextView) findViewById(R.id.count);
        init();
        players = new ArrayList<>();
        final ArrayList<String> arrPlayers = new ArrayList<>();     // array id cua may thang trong room
        for (int i = 0; i < room.getPlayers().length(); i = i + 28) {

            String tmp = room.getPlayers().substring(i, i + 28);
            arrPlayers.add(tmp);
        }
        mRefUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Person p = dataSnapshot.getValue(Person.class);
                for (String strPlayer : arrPlayers) {

                    if (strPlayer.equals(p.getUID())) {

                        players.add(p.getName());                   // array ten cua may thang trong room
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                count.setText(String.valueOf(players.size()) + "/10");
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

                            arrPlayers.add(currentUser.getUid());
                            players.add(currentUser.getDisplayName());
                            adapter.notifyDataSetChanged();
                            count.setText(String.valueOf(players.size()) + "/10");
                        }
                    }
                });
                btnLeave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int i = 0;
                        boolean out = true;
                        for (String player : arrPlayers) {

                            if (player.equals(currentUser.getUid())) {

                                out = false;
                                arrPlayers.remove(i);
                                Iterator<String> it = players.iterator();
                                while (it.hasNext()) {

                                    String name = it.next();
                                    if (name.equals(currentUser.getDisplayName())) {

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

                            Toast.makeText(RoomDetail.this, "You already leaved this room!", Toast.LENGTH_SHORT).show();
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
                android.R.layout.simple_list_item_1, players);
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
                    com.example.admin.testfirebase.Message mess = new com.example.admin.testfirebase.Message(input,name);
                    messRef.push().setValue(mess);
                    Adapter.add(mess);
                    txtMess.setText("");

                }
            }
        });
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
        tab2.setIndicator("",getResources().getDrawable(R.drawable.ic_message_black_24dp));
        tabHost.addTab(tab2);

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
                data.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    com.example.admin.testfirebase.Message mess = snapshot.getValue(com.example.admin.testfirebase.Message.class);
                    data.add(mess);
                }
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
        Button btn = (Button) findViewById(R.id.btn_back);
        btn.performClick();
    }
}
