package com.example.admin.testfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class RoomDetail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Person person;
    private FirebaseUser user;
    private DatabaseReference mRefUser;
    private Firebase mRefRoom;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        mRefUser = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testfirebase-27f0c.firebaseio.com/user");
        mRefRoom = new Firebase("https://lobby-3b4a3.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final Room room = (Room)getIntent().getSerializableExtra("room");
        TextView fieldName = (TextView)findViewById(R.id.field_name);
        TextView fieldAddress = (TextView)findViewById(R.id.field_address);
        TextView matchDate = (TextView)findViewById(R.id.match_date);
        TextView matchTime = (TextView)findViewById(R.id.match_time);
        ListView lvPlayers = (ListView)findViewById(R.id.list_players);
        Button btnJoin = (Button)findViewById(R.id.btn_join);
        Button btnLeave = (Button) findViewById(R.id.btn_leave);
        Button btnBack = (Button)findViewById(R.id.btn_back);
        final TextView count = (TextView)findViewById(R.id.count);


        btnJoin.setText("Join");
        // else ...

        fieldName.setText(room.getFieldName());
        fieldAddress.setText(room.getFieldAddress());
        matchDate.setText(room.getDate());
        matchTime.setText(room.getTime());
        players = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, players);
        lvPlayers.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerId = currentUser.getUid();
                boolean flag = true;
                for (String s:players) {
                    if (s.equals(playerId)) {

                        Toast.makeText(RoomDetail.this, "You are already in this room", Toast.LENGTH_SHORT).show();
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    String playerArrStr = room.getPlayers();
                    playerArrStr = playerArrStr + playerId;
                    room.setPlayers(playerArrStr);
                    mRefRoom.child(room.getId()).setValue(room);
                    players.add(playerId);
                    adapter.notifyDataSetChanged();
                    count.setText(String.valueOf(players.size())+"/10");
                }
            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playerId = currentUser.getUid();
                String playerStr = new String();
                ArrayList<String> playersArr = new ArrayList<String>();
                for (int i = 0;i < room.getPlayers().length(); i = i + 28) {

                    String tmp = room.getPlayers().substring(i,i + 28);
                    playersArr.add(tmp);
                }
                Iterator<String> it = playersArr.iterator();
                while (it.hasNext()) {

                    String s = it.next();
                    if (!s.equals(playerId)) {

                        playerStr = playerStr + s;
                    } else {

                        it.remove();
                    }
                }
                room.setPlayers(playerStr);
                mRefRoom.child(room.getId()).setValue(room);
                adapter.notifyDataSetChanged();
                count.setText(String.valueOf(players.size())+"/10");
            }
        });

        mRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int n = room.getPlayers().length();
                for (int i = 0;i < n;i = i + 28) {
                    String sid = room.getPlayers().substring(i,i+28);
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {

                        Person person = snapshot.getValue(Person.class);
                        if (person.getUID().equals(sid)) {

                            players.add(person.getName());
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        count.setText(String.valueOf(room.getPlayers().length() / 28)+"/10");
    }
}
