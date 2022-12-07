package com.yuseung.projectmanagement.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yuseung.projectmanagement.Activity.MainActivity;
import com.yuseung.projectmanagement.Activity.ProfileActivity;
import com.yuseung.projectmanagement.Adapter.FriendAdapter;
import com.yuseung.projectmanagement.R;
import com.yuseung.projectmanagement.Tool.FriendTool;
import com.yuseung.projectmanagement.Tool.Tool;

import java.util.ArrayList;

public class FriendFragment extends Fragment {
    RecyclerView rv_friend;
    FriendAdapter adapter;
    ArrayList<String> friend_list;
    ArrayList<String> name_list;
    LinearLayout ll_friend_myinfo;
    ImageView iv_friend_myimage;
    TextView tv_friend_myname;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        FriendTool.friendFragmentContext = getContext();
        Tool.friendfragment = this;
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("친구");

        friend_list = new ArrayList<>();
        name_list = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_friend, container, false);
        rv_friend = (RecyclerView)v.findViewById(R.id.rv_friend);
        iv_friend_myimage = (ImageView)v.findViewById(R.id.iv_friend_myimage);
        tv_friend_myname = (TextView)v.findViewById(R.id.tv_friend_myname);
        ll_friend_myinfo = (LinearLayout)v.findViewById(R.id.ll_friend_myinfo) ;

        Tool.sdr.child(Tool.current_user.getId()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(FriendTool.friendFragmentContext).load(uri).into(iv_friend_myimage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iv_friend_myimage.setImageResource(R.drawable.profiledefault);
            }
        });;
        tv_friend_myname.setText(Tool.current_user.getName());

        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_friend.setLayoutManager(lm);
        adapter = new FriendAdapter(friend_list, name_list);
        Tool.dr.child("User").child(Tool.current_user.getId()).child("friend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friend_list.clear();
                name_list.clear();
                for(DataSnapshot i : snapshot.getChildren())
                {
                    friend_list.add(i.getKey());
                    name_list.add(i.getValue(String.class));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rv_friend.setAdapter(adapter);
        ll_friend_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent((v.getContext()).getApplicationContext(), ProfileActivity.class);
                intent.putExtra("otherid", Tool.current_user.getId());
                (v.getContext()).startActivity(intent);
            }
        });
        return v;
    }


}
