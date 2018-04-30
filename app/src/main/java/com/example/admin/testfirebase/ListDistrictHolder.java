package com.example.admin.testfirebase;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ADMIN on 29-Apr-18.
 */

public class ListDistrictHolder extends RecyclerView.ViewHolder {
    TextView textView;
    public ListDistrictHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.text_view_district_name);

    }
}
