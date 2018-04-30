package com.example.admin.testfirebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kim Long on 21/12/2017.
 */

public class ListDistrictMenuAdapter extends RecyclerView.Adapter<ListDistrictHolder> {
    Context context;
    ArrayList<DistrictMenu> districtMenus;
    Activity activity;
    String userID;
    public ListDistrictMenuAdapter(Context context, ArrayList<DistrictMenu> districtMenus,Activity activity, String userID) {
        this.context = context;
        this.districtMenus = districtMenus;
        this.activity = activity;
        this.userID = userID;

    }
    /* public ListDistrictMenuAdapter(@NonNull Context context) {
        super(context,0);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(R.layout.district_row,null);
        }
        DistrictMenu districtMenu=getItem(position);
        if (districtMenu!=null) {
            ((TextView) convertView.findViewById(R.id.text_view_district_name)).setText(districtMenu.getName());

        }

        return convertView;
    }*/

    @Override
    public ListDistrictHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View convertView = layoutInflater.inflate(R.layout.district_row,parent,false);
        return new ListDistrictHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ListDistrictHolder holder, final int position) {
        holder.textView.setText(districtMenus.get(position).getName());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ListFieldActivity.class);
                String s= districtMenus.get(position).getName();
                i.putExtra("District",s);
                i.putExtra("UserID",userID);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return districtMenus.size();
    }
}
