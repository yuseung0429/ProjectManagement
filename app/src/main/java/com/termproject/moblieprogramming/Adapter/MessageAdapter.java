package com.termproject.moblieprogramming.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.termproject.moblieprogramming.Data.Message;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.Tool;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder>{

    ArrayList<Message> message_list;
    public MessageAdapter(ArrayList<Message> message_list)
    {
        this.message_list = message_list;
    }

    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_message, parent, false);
        return new MessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message temp = message_list.get(position);
        holder.tv_message_name.setText(temp.getName());
        holder.tv_message_message.setText(temp.getMessage());
        holder.tv_message_update.setText(Tool.getChangetime(temp.getUpdate()));
        if(Tool.current_user.getName().equals(temp.getName()))
        {
            holder.ll_message.setGravity(Gravity.RIGHT);
            holder.tv_message_message.setBackgroundColor(Color.YELLOW);
        }
        else
        {
            holder.ll_message.setGravity(Gravity.LEFT);
            holder.tv_message_message.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return message_list.size();
    }

    static class MessageHolder extends RecyclerView.ViewHolder {
        TextView tv_message_name, tv_message_message, tv_message_update;
        LinearLayout ll_message;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            tv_message_name = (TextView)itemView.findViewById(R.id.tv_message_name);
            tv_message_message = (TextView)itemView.findViewById(R.id.tv_message_message);
            tv_message_update = (TextView)itemView.findViewById(R.id.tv_message_update);
            ll_message = (LinearLayout)itemView.findViewById(R.id.ll_message);
        }
    }
}
