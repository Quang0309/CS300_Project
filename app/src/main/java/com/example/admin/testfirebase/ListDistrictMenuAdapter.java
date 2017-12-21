package com.example.admin.testfirebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Kim Long on 21/12/2017.
 */

public class ListDistrictMenuAdapter extends ArrayAdapter<DistrictMenu> {
    Context context;
    public ListDistrictMenuAdapter(@NonNull Context context) {
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
    }
}
