package com.termproject.moblieprogramming.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.termproject.moblieprogramming.Activity.MainActivity;
import com.termproject.moblieprogramming.Adapter.AlarmAdapter;
import com.termproject.moblieprogramming.Adapter.ChatAdapter;
import com.termproject.moblieprogramming.Data.Alarm;
import com.termproject.moblieprogramming.Data.Chat;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.AlarmTool;
import com.termproject.moblieprogramming.Tool.ChatTool;
import com.termproject.moblieprogramming.Tool.ProjectTool;
import com.termproject.moblieprogramming.Tool.Tool;

import java.util.ArrayList;

public class AlarmFragment extends Fragment {
    RecyclerView rv_alarm;
    AlarmAdapter adapter;
    ArrayList<Alarm> alarm_list;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AlarmTool.alarmFragmentContext = getContext();
        Tool.alarmfragment = this;
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("알림");

        alarm_list= new ArrayList<>();

        View v = inflater.inflate(R.layout.fragment_alarm, container, false);
        rv_alarm = (RecyclerView)v.findViewById(R.id.rv_alarm);

        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_alarm.setLayoutManager(lm);
        adapter = new AlarmAdapter(alarm_list);

        Tool.dr.child("User").child(Tool.current_user.getId()).child("alarm").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alarm_list.clear();
                for(DataSnapshot i : snapshot.getChildren())
                {
                    Alarm temp = i.getValue(Alarm.class);
                    alarm_list.add(temp);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        rv_alarm.setAdapter(adapter);


        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_alarm, menu);

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.it_alarm_allread:
            {
                Tool.dr.child("User").child(Tool.current_user.getId()).child("alarm").removeValue();
                adapter.notifyDataSetChanged();
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
