package com.example.admin.testfirebase;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FieldDetailActivity extends AppCompatActivity {
    String district, key;
    TextView textViewName, textViewAddress;
    ImageView imageView;
    ImageButton imageButton;
    String url, UserID;
    DatabaseReference mRef, mRating;
    ValueEventListener mVe, mVeRating;
    RatingBar ratingBar, ratingBarDialog;
    Button ratingButton;
    Dialog dialog;
    Firebase mRef2, mRating2;
    com.firebase.client.ValueEventListener mVe2, mVeRating2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);

        UserID = getIntent().getStringExtra("UserID");
        district = getIntent().getExtras().getString("district");
        key = getIntent().getStringExtra("key");

        initialize();
        loadData();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.google.com/maps/search/?api=1&query=";
                String add = textViewAddress.getText().toString();
                String name = textViewName.getText().toString();
                appendGoogleMapUrlForSearch(name, add);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(FieldDetailActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                dialog.setContentView(R.layout.dialog2);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                ratingBarDialog = (RatingBar) dialog.findViewById(R.id.dialog_rating_bar);
                ratingBarDialog.setRating(0);
                ratingBarDialog.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                    }
                });

                Button buttonSubmit = (Button) dialog.findViewById(R.id.dialog_button_submit);
                buttonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog progressDialog = new ProgressDialog(FieldDetailActivity.this);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                        /*
                            Lay userID ra
                         */

                        // mRating = FirebaseDatabase.getInstance().getReference().child("Rating").child("-Ky1t5nJJ3YEVUY8EzRY"); // test only
                        /*mRating=FirebaseDatabase.getInstance().getReference().child("Rating").child(key); // vao cai key cua field hien tai
                        mVeRating = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                float avgRating = 0;
                                int countUser = 0;
                                Number tmp;
                                boolean isChanged = false;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // lấy từng child trong cái field hien tai
                                    // bỏ qua cái default value và userID
                                    if ((!snapshot.getKey().toString().equals("DEFAULT")) && !snapshot.getKey().toString().equals(UserID)) {
                                        tmp = (Number) snapshot.getValue(); // snapshot.getValue return long, which cannot cast to float
                                        avgRating += tmp.floatValue();
                                        ++countUser;
                                    } else if (snapshot.getKey().toString().equals(UserID)) { // nếu có userID trong folder Rating thì update value
                                        mRating.child(UserID).setValue(ratingBarDialog.getRating()); // update value
                                        avgRating += ratingBarDialog.getRating();
                                        ++countUser;
                                        isChanged = true;
                                    }
                                }
                                // userID này chưa rate cho cái field hiện tại
                                // push userID cùng với rating vào cái field
                                if (!isChanged) {
                                    mRating.push().setValue(ratingBarDialog.getRating()); //test
                                    avgRating += ratingBarDialog.getRating();
                                    ++countUser;
                                }

                                // after that, update the average rating value of the field
                                mRef = FirebaseDatabase.getInstance().getReference().child(district).child(key); // test only
                                avgRating = avgRating / countUser;
                                mRef.child("rating").setValue(avgRating);
                                ratingBar.setRating(avgRating);

                                progressDialog.dismiss();
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        mRating.removeEventListener(mVeRating);
                                        // mRating.addListenerForSingleValueEvent(mVeRating);
                                    }
                                });

                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };*/
                        mRating2=new Firebase("https://testmap-60706.firebaseio.com/").child("Rating").child(key);
                        mVeRating2= new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                float avgRating = 0;
                                int countUser = 0;
                                Number tmp;
                                boolean isChanged = false;

                                for (com.firebase.client.DataSnapshot snapshot : dataSnapshot.getChildren()) { // lấy từng child trong cái field hien tai
                                    // bỏ qua cái default value và userID
                                    if ((!snapshot.getKey().toString().equals("DEFAULT")) && !snapshot.getKey().toString().equals(UserID)) {
                                        tmp = (Number) snapshot.getValue(); // snapshot.getValue return long, which cannot cast to float
                                        avgRating += tmp.floatValue();
                                        ++countUser;
                                    } else if (snapshot.getKey().toString().equals(UserID)) { // nếu có userID trong folder Rating thì update value
                                        mRating2.child(UserID).setValue(ratingBarDialog.getRating()); // update value
                                        avgRating += ratingBarDialog.getRating();
                                        ++countUser;
                                        isChanged = true;
                                    }
                                }
                                // userID này chưa rate cho cái field hiện tại
                                // push userID cùng với rating vào cái field
                                if (!isChanged) {
                                    mRating2.child(UserID).setValue(ratingBarDialog.getRating()); //test
                                    avgRating += ratingBarDialog.getRating();
                                    ++countUser;
                                }

                                // after that, update the average rating value of the field
                                //mRef = FirebaseDatabase.getInstance().getReference().child(district).child(key);
                                mRef2=new Firebase("https://testmap-60706.firebaseio.com/").child(district).child(key);
                                avgRating = avgRating / countUser;
                                mRef2.child("rating").setValue(avgRating);
                                ratingBar.setRating(avgRating);

                                progressDialog.dismiss();
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        mRating2.removeEventListener(mVeRating2);
                                        // mRating.addListenerForSingleValueEvent(mVeRating);
                                    }
                                });

                                dialog.dismiss();

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        };
                        mRating2.addListenerForSingleValueEvent(mVeRating2);
                    }
                });

                Button buttonCancel = (Button) dialog.findViewById(R.id.dialog_button_cancel);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        }); // end of rating button
    }

    private void loadData() {
       /* mRef = FirebaseDatabase.getInstance().getReference().child(district).child(key);
        mVe = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FieldMenu fieldMenu = dataSnapshot.getValue(FieldMenu.class);
                dataChanged(fieldMenu);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.addValueEventListener(mVe);*/
        mRef2 = new Firebase("https://testmap-60706.firebaseio.com/");
        mRef2=mRef2.child(district).child(key);
        mVe2 = new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                FieldMenu fieldMenu = dataSnapshot.getValue(FieldMenu.class);
                dataChanged(fieldMenu);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        };
        mRef2.addValueEventListener(mVe2);

    }

    private void dataChanged(FieldMenu field2) {
        Glide.with(getApplicationContext()).load(field2.getUrl()).into(imageView);
        ratingBar.setRating(field2.getRating());
        textViewName.setText(field2.getName());
        textViewAddress.setText(field2.getAddress());
    }

    @Override
    protected void onDestroy() {
        //mRef = FirebaseDatabase.getInstance().getReference().child(district).child(key);
        //mRef.removeEventListener(mVe);
        mRef2 = new Firebase("https://testmap-60706.firebaseio.com/").child(district).child(key);
        mRef2.removeEventListener(mVe2);
        super.onDestroy();
    }

    private void initialize() {
        imageView = (ImageView) findViewById(R.id.image_view_field);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar_info);
        textViewName = (TextView) findViewById(R.id.text_view_field_name_detail);
        textViewAddress = (TextView) findViewById(R.id.text_view_field_address_detail);
        imageButton = (ImageButton) findViewById(R.id.image_button_google_maps);
        ratingButton = (Button) findViewById(R.id.button_rating);
    }

    private void appendGoogleMapUrlForSearch(String s1, String s2) {
        s1.trim();
        s2.trim();

        char[] array = s1.toCharArray();
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == ' ') {
                array[i] = '+';
            }
        }
        s1 = String.valueOf(array);

        char[] array2 = s2.toCharArray();
        for (int i = 0; i < array2.length; ++i) {
            if (array2[i] == ' ') {
                array2[i] = '+';
            }
        }
        s2 = String.valueOf(array2);

        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append(s1);
        sb.append(s2);
        url = sb.toString();
    }
}
