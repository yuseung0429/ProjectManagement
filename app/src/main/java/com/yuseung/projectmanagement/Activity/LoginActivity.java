package com.yuseung.projectmanagement.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yuseung.projectmanagement.Data.User;
import com.yuseung.projectmanagement.R;
import com.yuseung.projectmanagement.Tool.Tool;


public class LoginActivity extends AppCompatActivity {
    EditText et_login_id, et_login_pw;
    Button bt_login_login, bt_login_signup,bt_login_findid, bt_login_findpw;
    User temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        init();

        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tool.dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if((temp = snapshot.child("User").child(et_login_id.getText().toString()).getValue(User.class)) != null
                                && (temp.getPassword().equals(Tool.hashConverter(et_login_pw.getText().toString()))))
                        {
                            Toast.makeText(getApplicationContext(), "환영합니다!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Tool.current_user  = temp;
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        bt_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        bt_login_findid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindidActivity.class);
                startActivity(intent);
            }
        });

        bt_login_findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindpwActivity.class);
                startActivity(intent);
           }
        });
    }
    void init()
    {
        et_login_id = (EditText) findViewById(R.id.et_login_id);
        et_login_pw = (EditText) findViewById(R.id.et_login_pw);
        bt_login_login = (Button) findViewById(R.id.bt_login_login);
        bt_login_signup = (Button) findViewById(R.id.bt_login_signup);
        bt_login_findid = (Button) findViewById(R.id.bt_login_findid);
        bt_login_findpw = (Button) findViewById(R.id.bt_login_findpw);
    }
}
