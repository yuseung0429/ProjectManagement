package com.yuseung.projectmanagement.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.regex.Pattern;


public class SignupActivity extends AppCompatActivity {
    Button bt_signup_enter, bt_signup_doublecheck;
    EditText et_signup_id, et_signup_name, et_signup_pw, et_signup_pwre, et_signup_phone, et_signup_email;
    boolean doublecheak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        init();
        bt_signup_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_signup_id.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(!Pattern.matches(Tool.regex_id,et_signup_id.getText().toString()))
                    Toast.makeText(getApplicationContext(), "아이디는 6~12자리 영문자입니다.", Toast.LENGTH_SHORT).show();
                else if(doublecheak == false)
                    Toast.makeText(getApplicationContext(), "중복 확인 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
                else if(et_signup_name.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(!Pattern.matches(Tool.regex_name,et_signup_name.getText().toString()))
                    Toast.makeText(getApplicationContext(), "이름을 확인하세요.", Toast.LENGTH_SHORT).show();
                else if(et_signup_pw.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(!Pattern.matches(Tool.regex_pw,et_signup_pw.getText().toString()))
                    Toast.makeText(getApplicationContext(), "비밀번호는 특수문자 포함 8~15자리입니다.", Toast.LENGTH_SHORT).show();
                else if(et_signup_pwre.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(!et_signup_pw.getText().toString().equals(et_signup_pwre.getText().toString()))
                    Toast.makeText(getApplicationContext(), "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                else if(et_signup_phone.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(!Pattern.matches(Tool.regex_phone,et_signup_phone.getText().toString()))
                    Toast.makeText(getApplicationContext(), "올바른 전화번호가 아닙니다.", Toast.LENGTH_SHORT).show();
                else if(et_signup_email.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(!Pattern.matches(Tool.regex_email,et_signup_email.getText().toString()))
                    Toast.makeText(getApplicationContext(), "올바른 이메일이 아닙니다.", Toast.LENGTH_SHORT).show();
                else if(et_signup_email.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                else
                {
                    Tool.dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("UserList").child(Tool.hashConverter(et_signup_name.getText().toString()+et_signup_email.getText().toString())).getValue()== null) {
                                User temp = new User(et_signup_id.getText().toString(), et_signup_pw.getText().toString(),
                                        et_signup_name.getText().toString(), et_signup_phone.getText().toString(),
                                        et_signup_email.getText().toString());
                                Tool.dr.child("User").child(et_signup_id.getText().toString()).setValue(temp);
                                Tool.dr.child("UserList").child(Tool.hashConverter(temp.getName()+temp.getEmail())).setValue(temp.getId());
                                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다!!!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "이미 존재하는 사용자입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
        bt_signup_doublecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Pattern.matches(Tool.regex_id,et_signup_id.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "아이디는 6~12자리 영문자입니다.", Toast.LENGTH_SHORT).show();
                    doublecheak = false;
                }
                else
                {
                    Tool.dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("User").child(et_signup_id.getText().toString()).getValue() == null) {
                                doublecheak = true;
                                Toast.makeText(getApplicationContext(), "사용 가능한 ID 입니다.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                doublecheak = false;
                                Toast.makeText(getApplicationContext(), "이미 존재하는 ID 입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
        et_signup_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                doublecheak = false;
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    void init()
    {
        bt_signup_enter = (Button)findViewById(R.id.bt_signup_enter);
        bt_signup_doublecheck = (Button)findViewById(R.id.bt_signup_doublecheck);
        et_signup_id = (EditText) findViewById(R.id.et_signup_id);
        et_signup_name = (EditText) findViewById(R.id.et_signup_name);
        et_signup_pw = (EditText) findViewById(R.id.et_signup_pw);
        et_signup_pwre = (EditText) findViewById(R.id.et_signup_pwre);
        et_signup_phone = (EditText) findViewById(R.id.et_signup_phone);
        et_signup_email = (EditText) findViewById(R.id.et_signup_email);
        doublecheak = false;
    }

}