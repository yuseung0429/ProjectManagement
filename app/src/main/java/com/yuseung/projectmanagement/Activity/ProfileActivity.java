package com.yuseung.projectmanagement.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.yuseung.projectmanagement.R;
import com.yuseung.projectmanagement.Tool.ChatTool;
import com.yuseung.projectmanagement.Tool.FriendTool;
import com.yuseung.projectmanagement.Tool.Tool;

import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {
    ImageView iv_profile_image;
    TextView tv_profile_name, tv_profile_email, tv_profile_phone;
    Button bt_profile_call, bt_profile_chat, bt_profile_removefriend;
    String otherid, phone;
    Context context;
    View.OnClickListener listener;
    private static final int GALLERY_CODE = 10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        init();
        context = getApplicationContext();
        otherid = getIntent().getStringExtra("otherid");
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);

            }
        };
        Tool.sdr.child(otherid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(iv_profile_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iv_profile_image.setImageResource(R.drawable.profiledefault);
            }
        });
        if(Tool.current_user.getId().equals(otherid))
        {
            iv_profile_image.setOnClickListener(listener);

            tv_profile_name.setText(Tool.current_user.getName());
            tv_profile_phone.setText(Tool.getPhoneformat(Tool.current_user.getPhone()));
            tv_profile_email.setText(Tool.current_user.getEmail());

            bt_profile_call.setVisibility(View.INVISIBLE);
            bt_profile_chat.setVisibility(View.INVISIBLE);
            bt_profile_removefriend.setVisibility(View.INVISIBLE);

        }
        else
        {
            iv_profile_image.setOnClickListener(null);

            bt_profile_call.setVisibility(View.VISIBLE);
            bt_profile_chat.setVisibility(View.VISIBLE);
            bt_profile_removefriend.setVisibility(View.VISIBLE);

            Tool.dr.child("User").child(otherid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    tv_profile_name.setText(snapshot.child("name").getValue(String.class));
                    phone = snapshot.child("phone").getValue(String.class);
                    tv_profile_phone.setText(Tool.getPhoneformat(phone));
                    tv_profile_email.setText(snapshot.child("email").getValue(String.class));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        bt_profile_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
                (v.getContext()).startActivity(intent);
            }
        });
        bt_profile_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatTool.createChat(v.getContext(),Tool.current_user.getId(), otherid);
                Intent intent =new Intent((v.getContext()).getApplicationContext(), MessageActivity.class);
                intent.putExtra("otherid",otherid);
                (v.getContext()).startActivity(intent);
            }
        });
        bt_profile_removefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                FriendTool.deleteFriend(view.getContext(), otherid);

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            if(data != null)
            {
                Uri file = data.getData();
                UploadTask uploadTask = Tool.sdr.child(otherid+".jpg").putFile(file);
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    iv_profile_image.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(context, "프로필 사진 업로드 성공 !!!" ,Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context, "프로필 사진 업로드 실패 !!!" ,Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }
    void init()
    {
        iv_profile_image = (ImageView) findViewById(R.id.iv_profile_image);

        tv_profile_name = (TextView) findViewById(R.id.tv_profile_name);
        tv_profile_email = (TextView) findViewById(R.id.tv_profile_email);
        tv_profile_phone = (TextView) findViewById(R.id.tv_profile_phone);

        bt_profile_call = (Button) findViewById(R.id.bt_profile_call);
        bt_profile_chat = (Button) findViewById(R.id.bt_profile_chat);
        bt_profile_removefriend = (Button) findViewById(R.id.bt_profile_removefriend);

    }
}
