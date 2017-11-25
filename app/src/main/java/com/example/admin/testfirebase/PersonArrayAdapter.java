package com.example.admin.testfirebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 19-Oct-17.
 */

public class PersonArrayAdapter extends ArrayAdapter<Person> {
    Activity context;
    int resource;
    List<Person> objects;
    public PersonArrayAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Person> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(resource, null);
        }
        Person per = objects.get(position);
        TextView txtGetName = convertView.findViewById(R.id.txtGetName);
        TextView txtGetMail = convertView.findViewById(R.id.txtGetMail);
        TextView txtGetDOB = convertView.findViewById(R.id.txtGetDOB);

        txtGetName.setText("Name: "+per.getName());

        txtGetDOB.setText( "DOB: "+per.getDOB());
        return convertView;
    }
}
