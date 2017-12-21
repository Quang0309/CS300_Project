package com.example.admin.testfirebase;

/**
 * Created by Kim Long on 21/12/2017.
 */
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ListFieldMenuAdapter extends ArrayAdapter<FieldMenu> {
    Context context;
    public ListFieldMenuAdapter(@NonNull Context context) {
        super(context, 0);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null) {
            LayoutInflater layoutInflater=LayoutInflater.from(this.context);
            convertView=layoutInflater.inflate(R.layout.field_row,null);
        }
        FieldMenu fieldMenu=getItem(position);
        if (fieldMenu!=null){
            ((TextView)convertView.findViewById(R.id.text_view_field_name)).setText(fieldMenu.getName());
        }
        return convertView;
    }
}
