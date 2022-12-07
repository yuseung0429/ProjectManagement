package com.termproject.moblieprogramming.Tool;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ChatTool {
    public static Context chatFragmentContext;
    static public void createChat(Context context, String user1, String user2) {
        int flag = user1.compareTo(user2);
        String front;
        String end;
        if (flag == 0)
        {
            Toast.makeText(context, "채팅방 생성 실패 !!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (flag < 0) {
            front = user1;
            end = user2;
        } else {
            front = user2;
            end = user1;
        }
        String chatid = Tool.hashConverter(front, end);
        Tool.dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean is_front = snapshot.child("User").child(front).hasChildren();
                boolean is_end = snapshot.child("User").child(end).hasChildren();
                if (is_front && is_end) {
                    String front_name = snapshot.child("User").child(front).child("name").getValue(String.class);
                    String end_name = snapshot.child("User").child(end).child("name").getValue(String.class);
                    Tool.dr.child("Chat").child(chatid).child("frontid").setValue(front);
                    Tool.dr.child("Chat").child(chatid).child("frontname").setValue(front_name);
                    Tool.dr.child("Chat").child(chatid).child("endid").setValue(end);
                    Tool.dr.child("Chat").child(chatid).child("endname").setValue(end_name);
                    Tool.dr.child("Chat").child(chatid).child("update").setValue(Tool.getCurrenttime());
                    Tool.dr.child("User").child(front).child("chat").child(chatid).setValue(Tool.getCurrenttime());
                    Tool.dr.child("User").child(end).child("chat").child(chatid).setValue(Tool.getCurrenttime());
                    Toast.makeText(context, "채팅방 생성 성공 !!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(context, "채팅방 생성 실패 !!", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
