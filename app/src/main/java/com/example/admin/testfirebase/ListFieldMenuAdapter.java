package com.example.admin.testfirebase;

/**
 * Created by Kim Long on 21/12/2017.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class ListFieldMenuAdapter extends ArrayAdapter<FieldMenu> {
    Context context;
  /*  ImageView imageView;
    RatingBar ratingBar;
    TextView textViewName, textViewAddress;
    FloatingActionButton mapButton,ratingButton,fab;
    boolean isFABopen = false;*/
    public ListFieldMenuAdapter(@NonNull Context context) {
        super(context, 0);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null) {
            LayoutInflater layoutInflater=LayoutInflater.from(this.context);
            convertView=layoutInflater.inflate(R.layout.field_row,parent,false);
        }
        FieldMenu fieldMenu=getItem(position);
        /*imageView =  convertView.findViewById(R.id.image_view_field);
        ratingBar =  convertView.findViewById(R.id.rating_bar_info);
        textViewName =  convertView.findViewById(R.id.text_view_field_name_detail);
        textViewAddress =  convertView.findViewById(R.id.text_view_field_address_detail);
        mapButton =  convertView.findViewById(R.id.image_button_google_maps);
        ratingButton =  convertView.findViewById(R.id.button_rating);
        fab =  convertView.findViewById(R.id.coreFab);
        fab.setSize(0);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFABopen){
                    activateFAB();
                }
                else{
                    deactivateFAB();
                }
            }
        });*/
        if (fieldMenu!=null){
            ((TextView)convertView.findViewById(R.id.text_view_field_name)).setText(fieldMenu.getName());
            /*Glide.with(context).load(fieldMenu.getUrl()).into(imageView);
            ratingBar.setRating(fieldMenu.getRating());
            textViewName.setText(fieldMenu.getName());
            textViewAddress.setText(fieldMenu.getAddress());
            textViewName.setTypeface(textViewName.getTypeface(), Typeface.BOLD);*/

        }

        return convertView;
    }

}
