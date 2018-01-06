package com.example.admin.testfirebase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

    public void filter(String search, Room r) {

    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView matchId;
        TextView fieldName;
        TextView matchTime;
        ImageView fieldImage;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            matchId = (TextView)itemView.findViewById(R.id.match_id);
            fieldName = (TextView)itemView.findViewById(R.id.field_name);
            matchTime = (TextView)itemView.findViewById(R.id.match_time);
            fieldImage = (ImageView)itemView.findViewById(R.id.field_image);
        }
    }

    List<Room> rooms;

    RVAdapter(List<Room> rooms){
        this.rooms = rooms;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                Room room = rooms.get(itemPosition);
            }
        };

    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.fieldName.setText(rooms.get(i).getFieldName());
        personViewHolder.matchTime.setText(rooms.get(i).getDate()+ " "+rooms.get(i).getTime());

        personViewHolder.matchId.setText("#"+rooms.get(i).getId());

        if (Integer.valueOf(rooms.get(i).getId()) % 13 == 0) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/vf50c2.jpg?alt=media&token=90954e66-cc22-4aa7-bd47-984e1a488474").into(personViewHolder.fieldImage);
        } else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 1) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/su-dung-co-nhan-tao-cho-san-bong-360x250.jpg?alt=media&token=1bbdfb07-f1e1-4835-a446-ec68b88e3546").into(personViewHolder.fieldImage);
        }
        else if(Integer.valueOf(rooms.get(i).getId()) % 13 == 2)
        {
            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/san-bong-co-nhan-tao-fpt-1.jpg?alt=media&token=ea417ff9-ecb3-41b0-9559-7715cb08b2fa").into(personViewHolder.fieldImage);
        }
        else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 3) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/Co-nhan-tao-san-bong-Nguyen-Gia-300x182.jpg?alt=media&token=55a0cb7c-6c8d-4dab-ab0c-fc3cfd421b9d").into(personViewHolder.fieldImage);
        }
        else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 4) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/8.jpg?alt=media&token=8eb2dd99-a15b-4240-9998-316309a6f0ee").into(personViewHolder.fieldImage);
        }
        else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 5) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/7.jpg?alt=media&token=26cce9d0-1ad0-40e0-a6d3-f790a5f5957e").into(personViewHolder.fieldImage);
        }
        else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 6) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/6.jpg?alt=media&token=d56f5025-b99c-47fd-a83d-4a2807be3303").into(personViewHolder.fieldImage);
        }
        else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 7) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/5.jpg?alt=media&token=9ad4b67e-c5e2-4b02-a3d0-4334c27876f1").into(personViewHolder.fieldImage);
        }
        else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 8) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/4.jpg?alt=media&token=3fcb291d-276b-4502-8540-76d3691f32f2").into(personViewHolder.fieldImage);
        }
        else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 9) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/3.jpg?alt=media&token=9a99aa47-1145-4078-8856-ec36ea454115").into(personViewHolder.fieldImage);
        }
        else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 10) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/2.jpg?alt=media&token=56636f86-364b-481a-974c-64c043fd58a0").into(personViewHolder.fieldImage);
        }
        else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 11) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/10.jpg?alt=media&token=caf6ab9a-4554-4330-9a02-a515289cf8eb").into(personViewHolder.fieldImage);
        }
        else if (Integer.valueOf(rooms.get(i).getId()) % 13 == 12) {

            Glide.with(personViewHolder.fieldImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/testfirebase-27f0c.appspot.com/o/1.jpg?alt=media&token=b28459a2-350d-4fdd-b081-613daeb8b07e").into(personViewHolder.fieldImage);
        }
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

}
