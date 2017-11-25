package com.example.admin.testfirebase;

import android.*;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InfoActivity extends AppCompatActivity {
    EditText txtName,txtAddress,txtPhone,txtNick;
    TextView txtDOB;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    DatabaseReference mRef;
    FloatingActionButton btnSend;
    FirebaseUser user;
    ProgressDialog dialog;
    ImageView imgAvatar;
    static int code = 1;
    StorageReference sRef;
    private FirebaseAuth mAuth;
    String URL="";
    Person currentPerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) // xin phep quyen truy cap thu vien anh
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testfirebase-27f0c.firebaseio.com/user"); // ket noi vs database
        init();
        check_user(); // check coi info nguoi dung da co tren database chua, neu co thi hien ra
        txtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date_Picker();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(URL.equals("")&&currentPerson!=null)
                {
                    URL = currentPerson.getURL();
                }

                DatabaseReference child = mRef.child(user.getUid()); // tao 1 nhanh con dat ten la userID
                Person man = new Person(txtName.getText().toString(),txtNick.getText().toString(),txtAddress.getText().toString(),txtDOB.getText().toString(),txtPhone.getText().toString(),URL,user.getUid());
                child.setValue(man); // up thong tin user len nhanh con do

                finish();
            }
        });


        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, code);

            }
        });
    }

    private void check_user() {
        final FirebaseUser currentUser = mAuth.getCurrentUser(); // lay user trong authencation

        if (currentUser!=null) {
            dialog = ProgressDialog.show(InfoActivity.this,"","Loading",true,false);
        mRef.addValueEventListener(new ValueEventListener() {    // lay data user xuong
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) { // xet tung thang user trong database
                    Person person = snapshot.getValue(Person.class); // bo thang user do vao object person
                    if (person.getUID().equals( currentUser.getUid()))  // neu thg user.getUID = cai userid lay trong authencation thi chinh la no
                    {
                        if(!person.getURL().equals(""))
                            Glide.with(getApplicationContext()).load(person.getURL()).into(imgAvatar); // lay anh tu storage
                        else

                            imgAvatar.setImageResource(R.drawable.ic_avatar);

                        txtDOB.setText(person.getDOB());
                        txtPhone.setText(person.getPhone());
                        txtName.setText(person.getName());
                        txtAddress.setText(person.getAddress());
                        txtNick.setText(person.getNick());
                        currentPerson = person;

                        break;
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        }
        dialog.dismiss();
    }

    private void Date_Picker() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                txtDOB.setText(sdf1.format(calendar.getTime()));
            }
        };
        DatePickerDialog picker = new DatePickerDialog(this,callback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    private void init() {
        txtName = (EditText) findViewById(R.id.txtName);
        txtDOB = (TextView) findViewById(R.id.txtDOB);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        btnSend = (FloatingActionButton) findViewById(R.id.btnSend);
        user = FirebaseAuth.getInstance().getCurrentUser();

        txtNick = (EditText) findViewById(R.id.txtNick);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        sRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == code && resultCode == RESULT_OK && null != data) {
            dialog = ProgressDialog.show(InfoActivity.this,"","Uploading image",true,false);
            Uri uri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = InfoActivity.this.getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);
            ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ExifInterface.ORIENTATION_NORMAL;
            if (exif != null)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                    break;
            }
            imgAvatar.setImageBitmap(loadedBitmap);

            StorageReference filepath = sRef.child( user.getUid()).child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(InfoActivity.this,"Success",Toast.LENGTH_LONG).show();
                    URL = taskSnapshot.getDownloadUrl().toString();
                    dialog.dismiss();
                }
            });
        }


        }
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }



}


    /**
     * Get the rotation of the last image added.
     * @param context
     * @param selectedImage
     * @return
     */


