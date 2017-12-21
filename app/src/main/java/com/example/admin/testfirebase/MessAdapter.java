package com.example.admin.testfirebase;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ADMIN on 21-Dec-17.
 */

public class MessAdapter extends ArrayAdapter<Message> {
    Activity context;
    int resource;
    ArrayList<Message> objects = null;
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy (HH:mm:ss)");
    public MessAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull ArrayList<Message> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater =context.getLayoutInflater();
            convertView = inflater.inflate(resource,null);
        }
        Message message = objects.get(position);
        TextView txtUsername = convertView.findViewById(R.id.txtUsername);
        TextView txt_time = convertView.findViewById(R.id.txt_time);
        TextView txtContent = convertView.findViewById(R.id.txtContent);
        txtUsername.setText( message.getMessageUser());
        txtContent.setText(message.getMessageText());
        txt_time.setText(sdf1.format(message.getMessageTime()));
        return convertView;
    }
}
