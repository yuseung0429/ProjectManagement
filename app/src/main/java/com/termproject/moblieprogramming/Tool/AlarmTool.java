package com.termproject.moblieprogramming.Tool;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.termproject.moblieprogramming.Data.Alarm;

import java.util.HashSet;

public class AlarmTool {
    public static Context alarmFragmentContext;
    public static void sendProjectAlarmMessage(String projectid, String message)
    {
        Tool.dr.child("Project").child(projectid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashSet<String> idset = new HashSet<>();
                String projecttitle = snapshot.child("title").getValue(String.class);
                for(DataSnapshot i : snapshot.child("subproject").getChildren())
                {
                    idset.add(snapshot.child("respid").getValue(String.class));
                    for(DataSnapshot j : i.child("moduleproject").getChildren())
                    {
                        String id = j.child("respid").getValue(String.class);
                        idset.add(id);
                    }
                }
                idset.remove(Tool.current_user.getId());
                for(String id : idset)
                {
                    Alarm temp = new Alarm(projecttitle,null, message, Tool.getCurrenttime());
                    Tool.dr.child("User").child(id).child("alarm").push().setValue(temp);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    public static void sendSubProjectArarmMessage(String projectid, String subproject, String message)
    {
        Tool.dr.child("Project").child(projectid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashSet<String> idset = new HashSet<>();
                String projecttitle = snapshot.child("title").getValue(String.class);
                String subprojecttitle = snapshot.child("subproject").child(subproject).child("title").getValue(String.class);
                idset.add(snapshot.child("subproject").child(subproject).child("respid").getValue(String.class));
                for(DataSnapshot i : snapshot.child("subproject").child(subproject).child("moduleproject").getChildren())
                {
                    String id = i.child("respid").getValue(String.class);
                    idset.add(id);
                }
                idset.remove(Tool.current_user.getId());
                for(String id : idset)
                {
                    Alarm temp = new Alarm(projecttitle,subprojecttitle, message, Tool.getCurrenttime());
                    Tool.dr.child("User").child(id).child("alarm").push().setValue(temp);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
