package com.yuseung.projectmanagement.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yuseung.projectmanagement.Data.User;
import com.yuseung.projectmanagement.R;
import com.yuseung.projectmanagement.Tool.Tool;

import java.util.regex.Pattern;


public class FindpwActivity extends AppCompatActivity {
    LinearLayout ll_findpw_search, ll_findpw_reset;
    Button bt_findpw_enter, bt_findpw_reset;
    EditText et_findpw_id, et_findpw_name, et_findpw_email, et_findpw_pw, et_findpw_pwre;
    User temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);
        getSupportActionBar().hide();
        init();
        ll_findpw_reset.setVisibility(View.GONE);
        bt_findpw_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tool.dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if((temp = snapshot.child("User").child(et_findpw_id.getText().toString()).getValue(User.class))!=null
                                && (et_findpw_name.getText().toString().equals(temp.getName()) && et_findpw_email.getText().toString().equals(temp.getEmail())))
                        {
                            ll_findpw_search.setVisibility(View.GONE);
                            ll_findpw_reset.setVisibility(View.VISIBLE);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "일치하는 정보가 없습니다", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        bt_findpw_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_findpw_pw.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(!Pattern.matches(Tool.regex_pw,et_findpw_pw.getText().toString()))
                    Toast.makeText(getApplicationContext(), "비밀번호는 특수문자 포함 8~15자리입니다.", Toast.LENGTH_SHORT).show();
                else if(et_findpw_pwre.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(!et_findpw_pw.getText().toString().equals(et_findpw_pwre.getText().toString()))
                    Toast.makeText(getApplicationContext(), "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                else if(Tool.hashConverter(et_findpw_pw.getText().toString()).equals(temp.getPassword()))
                    Toast.makeText(getApplicationContext(), "기존 비밀번호와 동일합니다.", Toast.LENGTH_SHORT).show();
                else
                {
                    temp.setPassword(Tool.hashConverter(et_findpw_pw.getText().toString()));
                    Tool.dr.child("User").child(temp.getId()).setValue(temp);
                    Toast.makeText(getApplicationContext(), "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
    void init()
    {
        ll_findpw_search = (LinearLayout) findViewById(R.id.ll_findpw_search);
        ll_findpw_reset = (LinearLayout) findViewById(R.id.ll_findpw_reset);
        bt_findpw_enter = (Button) findViewById(R.id.bt_findpw_enter);
        bt_findpw_reset = (Button) findViewById(R.id.bt_findpw_reset);
        et_findpw_id = (EditText) findViewById(R.id.et_findpw_id);
        et_findpw_name = (EditText) findViewById(R.id.et_findpw_name);
        et_findpw_email = (EditText) findViewById(R.id.et_findpw_email);
        et_findpw_pw= (EditText) findViewById(R.id.et_findpw_pw);
        et_findpw_pwre= (EditText) findViewById(R.id.et_findpw_pwre);
    }
}
