package com.yuseung.projectmanagement.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yuseung.projectmanagement.Adapter.ModuleProjectAdapter;
import com.yuseung.projectmanagement.Data.ModuleProject;
import com.yuseung.projectmanagement.R;
import com.yuseung.projectmanagement.Tool.AlarmTool;
import com.yuseung.projectmanagement.Tool.ProjectTool;
import com.yuseung.projectmanagement.Tool.Tool;

import java.util.ArrayList;

public class ModuleProjectActivity extends AppCompatActivity {
    RecyclerView rv_moduleproject;
    String projectid;
    String subprojectid;
    String projectrespid;
    String subprojectrespid;
    ArrayList<ModuleProject> moduleproject_list;
    ModuleProjectAdapter adapter;
    ValueEventListener listener;
    EditText et_moduleproject_title,et_alarm_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moduleproject);
        ProjectTool.moduleprojectActivityContext = getApplicationContext();

        setTitle("모듈 프로젝트");

        projectid = getIntent().getStringExtra("projectid");
        subprojectid = getIntent().getStringExtra("subprojectid");
        projectrespid = getIntent().getStringExtra("projectrespid");
        subprojectrespid = getIntent().getStringExtra("subprojectrespid");
        rv_moduleproject = (RecyclerView) findViewById(R.id.rv_moduleproject);

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_moduleproject.setLayoutManager(lm);

        moduleproject_list = new ArrayList<ModuleProject>();
        adapter = new ModuleProjectAdapter(projectid,subprojectid,projectrespid, subprojectrespid, moduleproject_list);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moduleproject_list.clear();
                for(DataSnapshot i : snapshot.getChildren())
                {
                    ModuleProject temp = i.getValue(ModuleProject.class);
                    moduleproject_list.add(temp);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("moduleproject").addValueEventListener(listener);
        rv_moduleproject.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_moduleproject, menu);
        return true;
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.it_moduleproject_create:
            {
                View dialogView = (View) View.inflate(this, R.layout.dialog_moduleproject_create, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("모듈 프로젝트 정보를 입력하세요.");
                dialog.setView(dialogView);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        et_moduleproject_title = (EditText) dialogView.findViewById(R.id.et_moduleproject_title);
                        ProjectTool.createmoduleProject(projectid,subprojectid,et_moduleproject_title.getText().toString());
                        ProjectTool.updateProject(projectid);
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("취소", null);
                dialog.show();
                break;
            }
            case R.id.it_moduleproject_send:
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
                            Toast.makeText(ModuleProjectActivity.this, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            AlarmTool.sendSubProjectArarmMessage(projectid, subprojectid, et_alarm_message.getText().toString());
                            Toast.makeText(ModuleProjectActivity.this, "메세지를 보냈습니다 !!", Toast.LENGTH_SHORT).show();
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
