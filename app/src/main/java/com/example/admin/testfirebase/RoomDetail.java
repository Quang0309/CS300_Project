package com.example.admin.testfirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Iterator;

public class RoomDetail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
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
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String test = currentUser.getDisplayName();
        final Room room = (Room) getIntent().getSerializableExtra("room");
        TextView fieldName = (TextView) findViewById(R.id.field_name);
        TextView fieldAddress = (TextView) findViewById(R.id.field_address);
        TextView matchDate = (TextView) findViewById(R.id.match_date);
        TextView matchTime = (TextView) findViewById(R.id.match_time);
        ListView lvPlayers = (ListView) findViewById(R.id.list_players);
        final Button btnJoin = (Button) findViewById(R.id.btn_join);
        final Button btnLeave = (Button) findViewById(R.id.btn_leave);
        final Button btnBack = (Button) findViewById(R.id.btn_back);
        final TextView count = (TextView) findViewById(R.id.count);
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
                count.setText(String.valueOf(players.size()) + "/10" + " players");
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
                R.layout.roomdetailitem, players);
        lvPlayers.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Button btn = (Button) findViewById(R.id.btn_back);
        btn.performClick();
    }
}
