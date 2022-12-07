package com.yuseung.projectmanagement.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yuseung.projectmanagement.Adapter.UserlistAdapter;
import com.yuseung.projectmanagement.Data.User;
import com.yuseung.projectmanagement.R;
import com.yuseung.projectmanagement.Tool.ProjectTool;
import com.yuseung.projectmanagement.Tool.Tool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class ModifyActivity extends AppCompatActivity {
    public static EditText et_modify_respid;
    LinearLayout ll_modify_weight;
    EditText et_modify_title, et_modify_weight, et_modify_startdate, et_modify_deadline;
    Button bt_modify_searchid, bt_modify_startdate, bt_modify_deadline, bt_modify_enter;
    String projectid, subprojectid, moduleprojectid;
    RecyclerView rv_userlist;
    UserlistAdapter adapter;
    ValueEventListener listener;
    ArrayList<User> user_list;
    int flag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        init();
        user_list = new ArrayList<>();
        setTitle("프로젝트 수정");
        projectid = getIntent().getStringExtra("projectid");
        subprojectid = getIntent().getStringExtra("subprojectid");
        moduleprojectid = getIntent().getStringExtra("moduleprojectid");
        et_modify_respid.setEnabled(false);
        et_modify_startdate.setEnabled(false);
        et_modify_deadline.setEnabled(false);

        if(projectid!=null && subprojectid!=null && moduleprojectid!=null)
            flag = 2;
        else if (projectid!=null && subprojectid!=null)
            flag = 1;
        else
        {
            flag = 0;
            ll_modify_weight.setVisibility(View.GONE);
        }


        Calendar cal = Calendar.getInstance();
        DatePickerDialog startdate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                et_modify_startdate.setText(String.format("%d%02d%02d",y,m+1,d));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        DatePickerDialog deadline = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                et_modify_deadline.setText(String.format("%d%02d%02d",y,m+1,d));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        View dialogView = (View) View.inflate(this, R.layout.dialog_userlist_select, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setPositiveButton("확인", null);
        dialog.setTitle("Responsibility를 선택하세요");

        adapter = new UserlistAdapter(dialog.getContext(),user_list);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_list.clear();
                for(DataSnapshot i : snapshot.getChildren())
                {
                    User temp = i.getValue(User.class);
                    user_list.add(temp);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Tool.dr.child("User").addValueEventListener(listener);

        rv_userlist = (RecyclerView) dialogView.findViewById(R.id.rv_userlist);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_userlist.setLayoutManager(lm);
        rv_userlist.setAdapter(adapter);

        bt_modify_searchid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogView.getParent() != null)
                    ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                dialog.setView(dialogView);
                dialog.show();
            }
        });
        bt_modify_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startdate.show();
            }
        });
        bt_modify_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deadline.show();
            }
        });
        bt_modify_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(et_modify_title.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Title을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else if(et_modify_respid.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Responsibility를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                else if(et_modify_startdate.getText().toString().equals("") || et_modify_deadline.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Startdate 또는 Deadline을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(et_modify_startdate.getText().toString()) > Integer.parseInt(et_modify_deadline.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Startdate는 Deadline보다 빨라야합니다.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    switch (flag)
                    {
                        case 0:
                        {
                            Tool.dr.child("Project").child(projectid).child("title").setValue(et_modify_title.getText().toString());
                            Tool.dr.child("Project").child(projectid).child("respid").setValue(et_modify_respid.getText().toString());
                            Tool.dr.child("Project").child(projectid).child("startdate").setValue(et_modify_startdate.getText().toString());
                            Tool.dr.child("Project").child(projectid).child("deadline").setValue(et_modify_deadline.getText().toString());
                            Toast.makeText(getApplicationContext(), "프로젝트 수정 완료 !!", Toast.LENGTH_SHORT).show();
                            ProjectTool.updateProject(projectid);
                            finish();
                            break;
                        }
                        case 1:
                        {
                            if(et_modify_weight.getText().toString().equals(""))
                            {
                                Toast.makeText(getApplicationContext(), "Weight를 입력하세요.", Toast.LENGTH_SHORT).show();
                            }
                            else if(!Pattern.matches(Tool.regex_num,et_modify_weight.getText().toString()))
                            {
                                Toast.makeText(getApplicationContext(), "Weight가 올바른 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("title").setValue(et_modify_title.getText().toString());
                                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("respid").setValue(et_modify_respid.getText().toString());
                                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("weight").setValue(Integer.parseInt(et_modify_weight.getText().toString()));
                                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("startdate").setValue(et_modify_startdate.getText().toString());
                                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("deadline").setValue(et_modify_deadline.getText().toString());
                                Toast.makeText(getApplicationContext(), "프로젝트 수정 완료 !!", Toast.LENGTH_SHORT).show();
                                ProjectTool.updateProject(projectid);
                                finish();
                            }
                            break;
                        }
                        case 2:
                        {
                            if(et_modify_weight.getText().toString().equals(""))
                            {
                                Toast.makeText(getApplicationContext(), "Weight를 입력하세요.", Toast.LENGTH_SHORT).show();
                            }
                            else if(!Pattern.matches(Tool.regex_num,et_modify_weight.getText().toString()))
                            {
                                Toast.makeText(getApplicationContext(), "Weight가 올바른 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("moduleproject").child(moduleprojectid).child("title").setValue(et_modify_title.getText().toString());
                                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("moduleproject").child(moduleprojectid).child("respid").setValue(et_modify_respid.getText().toString());
                                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("moduleproject").child(moduleprojectid).child("weight").setValue(Integer.parseInt(et_modify_weight.getText().toString()));
                                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("moduleproject").child(moduleprojectid).child("startdate").setValue(et_modify_startdate.getText().toString());
                                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("moduleproject").child(moduleprojectid).child("deadline").setValue(et_modify_deadline.getText().toString());
                                Toast.makeText(getApplicationContext(), "프로젝트 수정 완료 !!", Toast.LENGTH_SHORT).show();
                                ProjectTool.updateProject(projectid);
                                finish();
                            }
                            break;
                        }
                    }

                }
            }
        });
    }
    void init()
    {
        et_modify_title = (EditText) findViewById(R.id.et_modify_title);
        et_modify_respid = (EditText) findViewById(R.id.et_modify_respid);
        et_modify_weight = (EditText) findViewById(R.id.et_modify_weight);
        et_modify_startdate = (EditText) findViewById(R.id.et_modify_startdate);
        et_modify_deadline = (EditText) findViewById(R.id.et_modify_deadline);

        bt_modify_searchid = (Button) findViewById(R.id.bt_modify_searchid);
        bt_modify_startdate = (Button) findViewById(R.id.bt_modify_startdate);
        bt_modify_deadline = (Button) findViewById(R.id.bt_modify_deadline);
        bt_modify_enter = (Button) findViewById(R.id.bt_modify_enter);

        ll_modify_weight = (LinearLayout) findViewById(R.id.ll_modify_weight);
    }
}
