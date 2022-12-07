package com.yuseung.projectmanagement.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yuseung.projectmanagement.R;
import com.yuseung.projectmanagement.Tool.Tool;


public class FindidActivity extends AppCompatActivity {
    LinearLayout ll_findid_search, ll_findid_result;
    Button bt_findid_enter, bt_findid_gologin;
    EditText et_findid_name, et_findid_email;
    TextView tv_findid_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findid);
        getSupportActionBar().hide();
        init();
        ll_findid_result.setVisibility(View.GONE);
        bt_findid_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tool.dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String temp = Tool.hashConverter(et_findid_name.getText().toString() + et_findid_email.getText().toString());
                        if((temp = snapshot.child("UserList").child(temp).getValue(String.class))==null)
                        {
                            Toast.makeText(getApplicationContext(), "일치하는 정보가 없습니다", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            tv_findid_result.setText(temp);
                            ll_findid_search.setVisibility(View.GONE);
                            ll_findid_result.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        bt_findid_gologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    void init()
    {
        ll_findid_search = (LinearLayout) findViewById(R.id.ll_findid_search);
        ll_findid_result = (LinearLayout) findViewById(R.id.ll_findid_result);
        bt_findid_enter = (Button) findViewById(R.id.bt_findid_enter);
        bt_findid_gologin = (Button) findViewById(R.id.bt_findid_gologin);
        et_findid_name = (EditText) findViewById(R.id.et_findid_name);
        et_findid_email = (EditText) findViewById(R.id.et_findid_email);
        tv_findid_result = (TextView) findViewById(R.id.tv_findid_result);
    }
}
