package com.example.admin.testfirebase;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    ListView listViewDistrict;
    ListDistrictMenuAdapter districtMenuAdapter;
    ArrayList<DistrictMenu> districtMenuArrayList;
    private DatabaseReference mRef,mRefRating;
    private DatabaseReference mDistrictRef;
    private FieldMenu field;
    String fieldID;
    public String UserID;
    private Firebase mRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_district);

        UserID=getIntent().getStringExtra("UserID");

        //z` initializeData();
        initialize();
        loadData();
    }
    private void loadData() {
        mRef2=new Firebase("https://testmap-60706.firebaseio.com/");
        mRef2.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                districtMenuArrayList.clear();
                for (com.firebase.client.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String district = snapshot.getKey().toString();
                    if (district != null && !district.equals("Rating")) {
                        districtMenuArrayList.add(new DistrictMenu(district));
                    }
                }
                districtMenuAdapter.clear();
                districtMenuAdapter.addAll(districtMenuArrayList);
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

        districtMenuAdapter.addAll(districtMenuArrayList);
    }

    private void initialize() {
        listViewDistrict = (ListView)findViewById(R.id.list_view_district);
        districtMenuAdapter= new ListDistrictMenuAdapter(this);
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
        });
    }
    private void initializeData() {
        // Rating
        mRefRating = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Rating");

        // Quận 1
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 1");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân Tao Đàn", "1 Huyền Trân Công Chúa p.Bến Thành Q.1",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,4.7f);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);




        // Quận 2
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 2");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá Thảo Điền","28 Thảo Điền, Quận 2",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng Arsenal","8 Nguyễn Ư Dĩ, phường Thảo Điền, quận 2, TP Hồ Chí Minh",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng Khu Du Lịch Rạch Dừa","29 Tống Hữu Định Phường Thảo Điền, quận 2, TP Hồ Chí Minh",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận 4
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 4");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân Bóng ABC","266 Bến Vân Đồn, Quận 4, TP. HCM",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("CLB Bóng Đá Khánh Hội","9 Đường Số 48, Phường 3, Quận 4, Hồ Chí Minh",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận 5
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 5");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá Lam Sơn","320/1 Trần Bình Trọng, P.4, Q.5, TP. HCM",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận 6
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 6");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân cỏ nhân tạo Huỳnh Đức","298/57 Tân Hòa Đông, Q6",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân cỏ nhân tạo Phú Lâm","170 Kinh Dương Vương, Phường 13, Quận 6",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá mini Thành Long","241A, Tân Hòa Đông, Q. 6, Tp. Hồ Chí Minh",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận 7
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 7");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng Hoàng kim & Hoàng Duy","615 Huỳnh Tấn Phát, Phường Tân Thuận Đông, Quận 7",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân Mai Văn Vỉnh","143 Mai Văn Vỉnh, Quận 7",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân Lâm Văn Bền","169 Lâm Văn Bền, Quận 7",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân Phú Mỹ","38 Nguyễn Thị Thập, Bình Thuận, Quận 7, TP HCM",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận 8
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 8");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân Bóng Đá Hoàng Vy","Đường 332, Phạm Hùng, Q8",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá Cao Lỗ","130 Cao Lỗ, Q8",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá mini 275","275 Cao Lỗ, Phường 4, Quận 8",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);


        // Quận 9
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 9");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá cỏ nhân tạo trung tâm TDTT Quận 9","343 Lê Văn Việt, P.Tăng Nhơn Phú A, Quận 9",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá mini cỏ nhân tạo Hoàng Thịnh","Lô E, Khu Phố 2, P.Tăng Nhơn Phú, Q.9",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá mini cỏ nhân tạo Lâm Thịnh","Đường số 2, P.Tăng Nhơn Phú B, Q.9",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá mini cỏ nhân tạo Đại Châu ","Số 99, đường Man Thiện, Q.9",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá mini cỏ nhân tạo Phù Đổng ","255 Đỗ Xuân Hợp, Q.9, TP.HCM",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận 10
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 10");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân Tiểu Ngư","780/14E hẻm 780 Sư Vạn Hạnh, p.12, q.10",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá Thống Nhất","138 Đào Duy Từ, P.6, Q.10",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá Kỳ Hòa","824/28Q Sư Vạn Hạnh (nối dài) P12 Q.10",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận 11
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 11");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá Phú Thọ", "2 Lê Đại Hành q.11",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân cỏ nhân tạo Thành Phát - CLB thể thao Phú Thọ", "Số 4 Lê Đại Hành, Q.11",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận 12
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận 12");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân cỏ nhân tạo Đại Nguyên 2", "38 Đông Hưng Thuận 11, Đông Hưng Thuận, Q.12",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận Bình Thạnh
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận Bình Thạnh");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá Chu Văn An", "Đường số 4, Chu Văn An, phường 26, quận Bình Thạnh",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận Tân Bình
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận Tân Bình");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá Cộng Hòa", "18D Cộng Hòa, P.4, Q.Tân Bình",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận Tân Phú
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận Tân Phú");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bóng đá Hòa Thạnh", "318 Trịnh Đình Trọng, Hòa Thạnh, Tân Phú",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận Bình Tân
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận Bình Tân");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân cỏ nhân tạo Tân Quý", "754/11 Đường Tân Kỳ Tân Quý, KP5, P.Bình Hưng Hòa, Q.Bình Tân",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận Gò Vấp
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận Gò Vấp");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân cỏ nhân tạo Phương Đô", "37 Nguyễn Văn Lượng P.10 – Q.Gò vấp",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân cỏ nhân tạo Phương Nam 2", "44/5 Phạm Văn Chiêu, phường 9, quận Gò Vấp",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân Cây Trâm", "48/5 Phạm Văn Chiêu P4 Q.GÒ VẤP",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân cỏ nhân tạo Bình An", "44/8 Tổ 37, Phạm Văn Chiêu, P.9, Q.Gò Vấp",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân cỏ nhân tạo Thạch Đà", "59 Phạm Văn Chiêu, P.14, Q.Gò Vấp",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân cỏ nhân tạo Đại Nam", "Số 20, Cây Trâm, Q.Gò Vấp",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận Phú Nhuận
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận Phú Nhuận");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bòng đá Phú Nhuận", "3 Hoàng Minh Giám, P9 Q.Phú Nhuận",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

        // Quận Thủ Đức
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testmap-60706.firebaseio.com/Quận Thủ Đức");

        fieldID=mRef.push().getKey();
        field=new FieldMenu("Sân bòng đá Sa Kê", "Số 12 đường 42, P.Linh Đông, Q.Thủ Đức",
                "https://firebasestorage.googleapis.com/v0/b/testmap-60706.appspot.com/o/field.jpeg?alt=media&token=4daed1e5-ede0-4096-aa7e-b9579bd25414"
                ,0);
        mRef.child(fieldID).setValue(field);
        mRefRating.child(fieldID).child("DEFAULT").setValue(0);

    }
}
