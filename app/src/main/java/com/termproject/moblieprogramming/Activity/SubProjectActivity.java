package com.termproject.moblieprogramming.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.termproject.moblieprogramming.Adapter.MessageAdapter;
import com.termproject.moblieprogramming.Adapter.SubProjectAdapter;
import com.termproject.moblieprogramming.Data.Message;
import com.termproject.moblieprogramming.Data.SubProject;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.AlarmTool;
import com.termproject.moblieprogramming.Tool.ProjectTool;
import com.termproject.moblieprogramming.Tool.Tool;

import java.util.ArrayList;

public class SubProjectActivity extends AppCompatActivity {
    RecyclerView rv_subproject;
    String projectid, projectrespid;
    ArrayList<SubProject> subproject_list;
    SubProjectAdapter adapter;
    ValueEventListener listener;
    EditText et_subproject_title, et_alarm_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subproject);
        ProjectTool.subprojectActivityContext = getApplicationContext();
        setTitle("서브 프로젝트");
        projectid = getIntent().getStringExtra("projectid");
        projectrespid = getIntent().getStringExtra("projectrespid");

        rv_subproject = (RecyclerView) findViewById(R.id.rv_subproject);

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_subproject.setLayoutManager(lm);

        subproject_list = new ArrayList<SubProject>();
        adapter = new SubProjectAdapter(projectid, projectrespid, subproject_list);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subproject_list.clear();
                for(DataSnapshot i : snapshot.getChildren())
                {
                    SubProject temp = i.getValue(SubProject.class);
                    subproject_list.add(temp);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Tool.dr.child("Project").child(projectid).child("subproject").addValueEventListener(listener);
        rv_subproject.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Tool.refreshfragment(Tool.projectfragment);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_subproject, menu);
        return true;
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.it_subproject_create:
            {
                View dialogView = (View) View.inflate(this, R.layout.dialog_subproject_create, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("서브 프로젝트 정보를 입력하세요.");
                dialog.setView(dialogView);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        et_subproject_title = (EditText) dialogView.findViewById(R.id.et_subproject_title);
                        ProjectTool.createsubProject(projectid,et_subproject_title.getText().toString());
                        ProjectTool.updateProject(projectid);
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("취소", null);
                dialog.show();
                break;
            }
            case R.id.it_subproject_send:
            {
                View dialogView = (View) View.inflate(this, R.layout.dialog_alarm_send, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("보낼 메세지를 입력하세요.");
                dialog.setView(dialogView);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        et_alarm_message = (EditText) dialogView.findViewById(R.id.et_alarm_message);
                        if (et_alarm_message.getText().toString().equals("")) {
                            Toast.makeText(SubProjectActivity.this, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            et_alarm_message = (EditText) dialogView.findViewById(R.id.et_alarm_message);
                            AlarmTool.sendProjectAlarmMessage(projectid, et_alarm_message.getText().toString());
                            Toast.makeText(SubProjectActivity.this, "메세지를 보냈습니다 !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setNegativeButton("취소", null);
                dialog.show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
