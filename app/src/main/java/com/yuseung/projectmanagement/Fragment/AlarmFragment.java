package com.yuseung.projectmanagement.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yuseung.projectmanagement.Activity.MainActivity;
import com.yuseung.projectmanagement.Adapter.AlarmAdapter;
import com.yuseung.projectmanagement.Data.Alarm;
import com.yuseung.projectmanagement.R;
import com.yuseung.projectmanagement.Tool.AlarmTool;
import com.yuseung.projectmanagement.Tool.Tool;

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
