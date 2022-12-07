package com.yuseung.projectmanagement.Tool;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class FriendTool {
    public static Context friendFragmentContext;
    static public void addFriend(Context context, String id)
    {
        Tool.dr.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String exist = snapshot.child(Tool.current_user.getId()).child("friend").child(id).getValue(String.class);
                if(exist == null)
                {
                    Tool.dr.child("User").child(Tool.current_user.getId()).child("friend").child(id).setValue(snapshot.child(id).child("name").getValue(String.class));
                    Toast.makeText(context, "친구 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(context, "이미 등록된 친구입니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    static public void deleteFriend(Context context, String id)
    {
        Tool.dr.child("User").child(Tool.current_user.getId()).child("friend").child(id).removeValue();
        Toast.makeText(context, "친구 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
    }

}
