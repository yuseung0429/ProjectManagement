package com.termproject.moblieprogramming.Adapter;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.termproject.moblieprogramming.Data.Alarm;
import com.termproject.moblieprogramming.Data.Chat;
import com.termproject.moblieprogramming.Activity.MessageActivity;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.ChatTool;
import com.termproject.moblieprogramming.Tool.FriendTool;
import com.termproject.moblieprogramming.Tool.Tool;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder>{

    ArrayList<Alarm> alarm_list;
    public AlarmAdapter(ArrayList<Alarm> alarm_list)
    {
        this.alarm_list = alarm_list;
    }
    @Override
    public AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_alarm, parent, false);
        return new AlarmHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmHolder holder, int position) {
        Alarm temp = alarm_list.get(position);
        holder.tv_alarm_projecttitle.setText(temp.getProjecttitle());
        holder.tv_alarm_message.setText(temp.getMessage());
        holder.tv_alarm_date.setText(Tool.getChangeymdbartime(temp.getDate()));
        if(temp.getSubprojecttitle() == null)
        {
            holder.ll_alarm_subproject.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.ll_alarm_subproject.setVisibility(View.VISIBLE);
            holder.tv_alarm_subprojecttitle.setText(temp.getSubprojecttitle());
        }
    }
    @Override
    public int getItemCount() {
        return alarm_list.size();
    }

    static class AlarmHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_alarm_subproject;
        TextView tv_alarm_projecttitle, tv_alarm_subprojecttitle, tv_alarm_date, tv_alarm_message;
        public AlarmHolder(@NonNull View itemView) {
            super(itemView);
            ll_alarm_subproject = (LinearLayout) itemView.findViewById(R.id.ll_alarm_subproject);

            tv_alarm_projecttitle = (TextView) itemView.findViewById(R.id.tv_alarm_projecttitle);
            tv_alarm_subprojecttitle = (TextView) itemView.findViewById(R.id.tv_alarm_subprojecttitle);
            tv_alarm_message = (TextView) itemView.findViewById(R.id.tv_alarm_message);
            tv_alarm_date = (TextView) itemView.findViewById(R.id.tv_alarm_date);
        }
    }
}
