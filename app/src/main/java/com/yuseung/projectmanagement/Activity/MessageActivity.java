package com.yuseung.projectmanagement.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yuseung.projectmanagement.Adapter.MessageAdapter;
import com.yuseung.projectmanagement.Data.Message;
import com.yuseung.projectmanagement.R;
import com.yuseung.projectmanagement.Tool.Tool;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    EditText et_message_message;
    Button bt_message_send;
    RecyclerView rv_message;
    String otherid;
    String chatid;
    ArrayList<Message> message_list;
    MessageAdapter adapter;
    ValueEventListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();
        setTitle("채팅방");
        otherid = getIntent().getStringExtra("otherid");
        chatid = Tool.hashConverter(otherid, Tool.current_user.getId());

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_message.setLayoutManager(lm);

        message_list = new ArrayList<Message>();
        adapter = new MessageAdapter(message_list);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                message_list.clear();
                for(DataSnapshot i : snapshot.getChildren())
                {
                    Message message = i.getValue(Message.class);
                    message_list.add(message);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Tool.dr.child("Chat").child(chatid).child("message").addValueEventListener(listener);


        rv_message.setAdapter(adapter);
        bt_message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_message_message.getText().toString();
                if(!msg.equals(""))
                {
                    String time = Tool.getCurrenttime();
                    Message message = new Message(Tool.current_user.getName(), msg ,time);
                    Tool.dr.child("Chat").child(chatid).child("message").push().setValue(message);
                    Tool.dr.child("Chat").child(chatid).child("update").setValue(time);
                    Tool.dr.child("Chat").child(chatid).child("last").setValue(msg);
                    et_message_message.setText("");
                    rv_message.smoothScrollToPosition(message_list.size()+1);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    void init()
    {
        et_message_message = (EditText) findViewById(R.id.et_message_message);
        bt_message_send = (Button) findViewById(R.id.bt_message_send);
        rv_message = (RecyclerView) findViewById(R.id.rv_message);
    }
}
