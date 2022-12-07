package com.termproject.moblieprogramming.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
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
import com.termproject.moblieprogramming.Activity.ModifyActivity;
import com.termproject.moblieprogramming.Data.Message;
import com.termproject.moblieprogramming.Data.User;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.ProjectTool;
import com.termproject.moblieprogramming.Tool.Tool;

import java.util.ArrayList;

public class UserlistAdapter extends RecyclerView.Adapter<UserlistAdapter.UserlistHolder>{
    Context context;
    ArrayList<User> user_list;
    public UserlistAdapter(Context context, ArrayList<User> user_list)
    {
        this.context = context;
        this.user_list = user_list;
    }

    @Override
    public UserlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_userlist, parent, false);
        return new UserlistHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserlistHolder holder, int position) {
        User temp = user_list.get(position);
        holder.tv_userlist_id.setText(temp.getId());
        holder.tv_userlist_name.setText(temp.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyActivity.et_modify_respid.setText(temp.getId());
            }
        });
        Tool.sdr.child( temp.getId() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProjectTool.moduleprojectActivityContext).load(uri).into(holder.iv_userlist_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.iv_userlist_image.setImageResource(R.drawable.profiledefault);
            }
        });
    }

    @Override
    public int getItemCount() {
        return user_list.size();
    }

    static class UserlistHolder extends RecyclerView.ViewHolder {
        TextView tv_userlist_id, tv_userlist_name;
        ImageView iv_userlist_image;
        public UserlistHolder(@NonNull View itemView) {
            super(itemView);
            tv_userlist_id = (TextView)itemView.findViewById(R.id.tv_userlist_id);
            tv_userlist_name = (TextView)itemView.findViewById(R.id.tv_userlist_name);
            iv_userlist_image = (ImageView)itemView.findViewById(R.id.iv_userlist_image);
        }
    }
}

