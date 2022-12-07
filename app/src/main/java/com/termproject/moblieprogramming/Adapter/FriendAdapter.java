package com.termproject.moblieprogramming.Adapter;

import android.content.Intent;
import android.net.Uri;
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
import com.termproject.moblieprogramming.Activity.MessageActivity;
import com.termproject.moblieprogramming.Activity.ProfileActivity;
import com.termproject.moblieprogramming.Data.Chat;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.FriendTool;
import com.termproject.moblieprogramming.Tool.ProjectTool;
import com.termproject.moblieprogramming.Tool.Tool;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder>{

    ArrayList<String> friend_list;
    ArrayList<String> name_list;
    public FriendAdapter(ArrayList<String> friend_list, ArrayList<String> name_list)
    {
        this.friend_list = friend_list;
        this.name_list = name_list;
    }
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_friend, parent, false);
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        String temp = friend_list.get(position);
        String temp_name = name_list.get(position);
        holder.tv_friend_name.setText(temp_name);
        Tool.sdr.child(temp+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(FriendTool.friendFragmentContext).load(uri).into(holder.iv_friend_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.iv_friend_image.setImageResource(R.drawable.profiledefault);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent((v.getContext()).getApplicationContext(), ProfileActivity.class);
                intent.putExtra("otherid", temp);
                (v.getContext()).startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return friend_list.size();
    }

    static class FriendHolder extends RecyclerView.ViewHolder {
        ImageView iv_friend_image;
        TextView tv_friend_name;
        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            iv_friend_image = (ImageView)itemView.findViewById(R.id.iv_friend_image);
            tv_friend_name = (TextView)itemView.findViewById(R.id.tv_friend_name);
        }
    }
}
