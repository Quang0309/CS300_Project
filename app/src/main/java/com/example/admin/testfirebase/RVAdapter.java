package com.example.admin.testfirebase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

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
        personViewHolder.matchTime.setText(rooms.get(i).getTime());
        personViewHolder.fieldImage.setImageResource(R.mipmap.ic_launcher);
        personViewHolder.matchId.setText("#"+rooms.get(i).getId());

    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }
}
