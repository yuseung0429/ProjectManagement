package com.termproject.moblieprogramming.Adapter;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.termproject.moblieprogramming.Data.Chat;
import com.termproject.moblieprogramming.Activity.MessageActivity;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.ChatTool;
import com.termproject.moblieprogramming.Tool.FriendTool;
import com.termproject.moblieprogramming.Tool.Tool;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{

    ArrayList<Chat> chat_list;
    public ChatAdapter(ArrayList<Chat> chat_list)
    {
        this.chat_list = chat_list;
    }
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_chat, parent, false);
        return new ChatHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        Chat temp = chat_list.get(position);
        holder.setItem(temp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent((v.getContext()).getApplicationContext(), MessageActivity.class);
                intent.putExtra("otherid", (Tool.current_user.getId().equals(temp.getFrontid())) ? temp.getEndid() : temp.getFrontid());
                (v.getContext()).startActivity(intent);
            }
        });
        Tool.sdr.child(((Tool.current_user.getId().equals(temp.getFrontid())) ? temp.getEndid() : temp.getFrontid()) + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ChatTool.chatFragmentContext).load(uri).into(holder.iv_chat_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.iv_chat_image.setImageResource(R.drawable.profiledefault);
            }
        });
    }
    @Override
    public int getItemCount() {
        return chat_list.size();
    }

    static class ChatHolder extends RecyclerView.ViewHolder {
        ImageView iv_chat_image;
        TextView tv_chat_name, tv_chat_last, tv_chat_update;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            iv_chat_image = (ImageView)itemView.findViewById(R.id.iv_chat_image);
            tv_chat_name = (TextView)itemView.findViewById(R.id.tv_chat_name);
            tv_chat_last = (TextView)itemView.findViewById(R.id.tv_chat_last);
            tv_chat_update = (TextView)itemView.findViewById(R.id.tv_chat_update);
        }
        public void setItem(Chat item)
        {
            tv_chat_name.setText((Tool.current_user.getId().equals(item.getFrontid())) ? item.getEndname() : item.getFrontname());
            tv_chat_last.setText(item.getLast());
            tv_chat_update.setText(Tool.getChangetime(item.getUpdate()));
        }
    }
}
