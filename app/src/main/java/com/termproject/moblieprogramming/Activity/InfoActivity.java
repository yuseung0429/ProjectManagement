package com.termproject.moblieprogramming.Activity;

import static com.termproject.moblieprogramming.Tool.ProjectTool.moduleprojectActivityContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.ChatTool;
import com.termproject.moblieprogramming.Tool.FriendTool;
import com.termproject.moblieprogramming.Tool.Tool;

import org.w3c.dom.Text;

public class InfoActivity extends AppCompatActivity {
    LinearLayout ll_info_weight, ll_info_totalweight, ll_info_task;
    TextView tv_info_title, tv_info_respid, tv_info_name,tv_info_phone, tv_info_email,tv_info_weight;
    TextView tv_info_totalweight, tv_info_progress, tv_info_startdate, tv_info_deadline, tv_info_task;
    Button bt_info_addfriend, bt_info_chat,bt_info_call;
    String projectid, subprojectid, moduleprojectid, respid;
    int flag;
    String phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().hide();
        init();


        projectid = getIntent().getStringExtra("projectid");
        subprojectid = getIntent().getStringExtra("subprojectid");
        moduleprojectid = getIntent().getStringExtra("moduleprojectid");
        respid = getIntent().getStringExtra("respid");


        if(projectid!=null && subprojectid!=null && moduleprojectid!=null)
            flag = 2;
        else if (projectid!=null && subprojectid!=null)
            flag = 1;
        else
            flag = 0;
        if(Tool.current_user.getId().equals(respid))
        {
            bt_info_addfriend.setVisibility(View.GONE);
            bt_info_chat.setVisibility(View.GONE);
            bt_info_call.setVisibility(View.GONE);
        }

        switch (flag)
        {
            case 0:
            {
                Tool.dr.child("Project").child(projectid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tv_info_title.setText(snapshot.child("title").getValue(String.class));
                        tv_info_respid.setText(snapshot.child("respid").getValue(String.class));
                        tv_info_totalweight.setText(String.valueOf(snapshot.child("totalweight").getValue(Integer.class)));
                        tv_info_progress.setText(String.valueOf(snapshot.child("progress").getValue(Integer.class)));
                        tv_info_startdate.setText(Tool.getChangeymdbartime(snapshot.child("startdate").getValue(String.class)));
                        tv_info_deadline.setText(Tool.getChangeymdbartime(snapshot.child("deadline").getValue(String.class)));
                        tv_info_task.setText(String.valueOf(snapshot.child("totaltask").getValue(Integer.class)));
                        ll_info_weight.setVisibility(View.GONE);
                        ll_info_totalweight.setVisibility(View.VISIBLE);
                        ll_info_task.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                break;
            }
            case 1:
            {
                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        tv_info_title.setText(snapshot.child("title").getValue(String.class));
                        tv_info_respid.setText(snapshot.child("respid").getValue(String.class));
                        tv_info_weight.setText(String.valueOf(snapshot.child("weight").getValue(Integer.class)));
                        tv_info_totalweight.setText(String.valueOf(snapshot.child("totalweight").getValue(Integer.class)));
                        tv_info_progress.setText(String.valueOf(snapshot.child("progress").getValue(Integer.class)));
                        tv_info_startdate.setText(Tool.getChangeymdbartime(snapshot.child("startdate").getValue(String.class)));
                        tv_info_deadline.setText(Tool.getChangeymdbartime(snapshot.child("deadline").getValue(String.class)));
                        tv_info_task.setText(String.valueOf(snapshot.child("totaltask").getValue(Integer.class)));

                        ll_info_weight.setVisibility(View.VISIBLE);
                        ll_info_totalweight.setVisibility(View.VISIBLE);
                        ll_info_task.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            }
            case 2:
            {
                Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("moduleproject").child(moduleprojectid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tv_info_title.setText(snapshot.child("title").getValue(String.class));
                        tv_info_respid.setText(snapshot.child("respid").getValue(String.class));
                        tv_info_weight.setText(String.valueOf(snapshot.child("weight").getValue(Integer.class)));
                        tv_info_progress.setText(String.valueOf(snapshot.child("progress").getValue(Integer.class)));
                        tv_info_startdate.setText(Tool.getChangeymdbartime(snapshot.child("startdate").getValue(String.class)));
                        tv_info_deadline.setText(Tool.getChangeymdbartime(snapshot.child("deadline").getValue(String.class)));

                        ll_info_weight.setVisibility(View.VISIBLE);
                        ll_info_totalweight.setVisibility(View.GONE);
                        ll_info_task.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

        Tool.dr.child("User").child(respid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phone = snapshot.child("phone").getValue(String.class);
                tv_info_name.setText(snapshot.child("name").getValue(String.class));
                tv_info_phone.setText(Tool.getPhoneformat(phone));
                tv_info_email.setText(snapshot.child("email").getValue(String.class));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bt_info_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendTool.addFriend(v.getContext(),respid);
            }
        });

        bt_info_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
                (v.getContext()).startActivity(intent);
            }
        });

        bt_info_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatTool.createChat(v.getContext(),Tool.current_user.getId(), respid);
                Intent intent =new Intent(v.getContext(), MessageActivity.class);
                intent.putExtra("otherid",respid);
                (v.getContext()).startActivity(intent);
            }
        });



    }

    void init()
    {
        tv_info_title = (TextView) findViewById(R.id.tv_info_title);
        tv_info_respid = (TextView) findViewById(R.id.tv_info_respid);
        tv_info_name = (TextView) findViewById(R.id.tv_info_name);
        tv_info_phone = (TextView) findViewById(R.id.tv_info_phone);
        tv_info_email = (TextView) findViewById(R.id.tv_info_email);
        tv_info_weight = (TextView) findViewById(R.id.tv_info_weight);
        tv_info_totalweight = (TextView) findViewById(R.id.tv_info_totalweight);
        tv_info_progress = (TextView) findViewById(R.id.tv_info_progress);
        tv_info_startdate = (TextView) findViewById(R.id.tv_info_startdate);
        tv_info_deadline = (TextView) findViewById(R.id.tv_info_deadline);
        tv_info_task = (TextView) findViewById(R.id.tv_info_task);

        bt_info_addfriend = (Button) findViewById(R.id.bt_info_addfriend);
        bt_info_chat = (Button) findViewById(R.id.bt_info_chat);
        bt_info_call = (Button) findViewById(R.id.bt_info_call);

        ll_info_weight = (LinearLayout) findViewById(R.id.ll_info_weight);
        ll_info_totalweight = (LinearLayout) findViewById(R.id.ll_info_totalweight);
        ll_info_task = (LinearLayout) findViewById(R.id.ll_info_task);

    }
}
