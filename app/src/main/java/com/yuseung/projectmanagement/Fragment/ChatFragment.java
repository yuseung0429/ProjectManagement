package com.yuseung.projectmanagement.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yuseung.projectmanagement.Adapter.ChatAdapter;
import com.yuseung.projectmanagement.Data.Chat;
import com.yuseung.projectmanagement.Activity.MainActivity;
import com.yuseung.projectmanagement.R;
import com.yuseung.projectmanagement.Tool.ChatTool;
import com.yuseung.projectmanagement.Tool.Tool;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    RecyclerView rv_chat;
    ChatAdapter adapter;
    View dialogView;
    EditText et_chat_tid;
    ArrayList<String> chatid_list;
    ArrayList<Chat> chat_list;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ChatTool.chatFragmentContext = getContext();
        Tool.chatfragment = this;
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("채팅방");
        chatid_list = new ArrayList<>();
        chat_list = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        rv_chat = (RecyclerView)v.findViewById(R.id.rv_chat);

        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_chat.setLayoutManager(lm);
        adapter = new ChatAdapter(chat_list);

        Tool.dr.child("User").child(Tool.current_user.getId()).child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatid_list.clear();
                chat_list.clear();
                for(DataSnapshot i : snapshot.getChildren())
                {
                    chatid_list.add(i.getKey());
                }
                Tool.dr.child("Chat").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(String i : chatid_list)
                        {
                            DataSnapshot j = snapshot.child(i);
                            String temp_update = j.child("update").getValue(String.class);
                            String temp_last = j.child("last").getValue(String.class);
                            String temp_frontid = j.child("frontid").getValue(String.class);
                            String temp_frontname = j.child("frontname").getValue(String.class);
                            String temp_endid = j.child("endid").getValue(String.class);
                            String temp_endname = j.child("endname").getValue(String.class);
                            chat_list.add(new Chat(i,(temp_update == null) ? "" : temp_update, (temp_last == null) ? "" : temp_last, temp_frontid, temp_frontname, temp_endid, temp_endname));
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        rv_chat.setAdapter(adapter);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.it_chat_create:
            {
                dialogView = (View) View.inflate(getContext(), R.layout.dialog_chat_create, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("상대방 아이디를 입력하세요.");
                dialog.setView(dialogView);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        et_chat_tid = (EditText) dialogView.findViewById(R.id.et_chat_tid);
                        ChatTool.createChat(ChatTool.chatFragmentContext, Tool.current_user.getId(),et_chat_tid.getText().toString());
                    }
                });
                dialog.setNegativeButton("취소", null);
                dialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}