package com.termproject.moblieprogramming.Adapter;

import static com.termproject.moblieprogramming.Tool.ProjectTool.moduleprojectActivityContext;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.termproject.moblieprogramming.Activity.MessageActivity;
import com.termproject.moblieprogramming.Activity.ModifyActivity;
import com.termproject.moblieprogramming.Activity.SubProjectActivity;
import com.termproject.moblieprogramming.Data.Message;
import com.termproject.moblieprogramming.Data.ModuleProject;
import com.termproject.moblieprogramming.Data.SubProject;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.ChatTool;
import com.termproject.moblieprogramming.Tool.FriendTool;
import com.termproject.moblieprogramming.Tool.ProjectTool;
import com.termproject.moblieprogramming.Tool.Tool;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ModuleProjectAdapter extends RecyclerView.Adapter<ModuleProjectAdapter.ModuleProjectHolder>{

    ArrayList<ModuleProject> moduleproject_list;
    String projectid;
    String subprojectid;
    String projectrespid;
    String subprojectrespid;
    SeekBar sb_moduleproject_progress;
    TextView tv_moduleproject_seekbar;
    View.OnClickListener listener;

    public ModuleProjectAdapter(String projectid, String subprojectid, String projectrespid, String subprojectrespid, ArrayList<ModuleProject> moduleproject_list) {
        this.projectid = projectid;
        this.subprojectid = subprojectid;
        this.projectrespid = projectrespid;
        this.subprojectrespid = subprojectrespid;
        this.moduleproject_list = moduleproject_list;
    }

    @Override
    public ModuleProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_moduleproject, parent, false);
        return new ModuleProjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleProjectHolder holder, int position) {
        ModuleProject temp = moduleproject_list.get(position);
        holder.tv_moduleproject_title.setText(temp.getTitle());
        holder.tv_moduleproject_startdate.setText(Tool.getChangeymdbartime(temp.getStartdate()));
        holder.tv_moduleproject_deadline.setText("Deadline\n"+Tool.getChangeymdbartime(temp.getDeadline()));
        holder.tv_moduleproject_weight.setText("Weight\n"+temp.getWeight());
        holder.tv_moduleproject_progressper.setText(temp.getProgress()+"%");
        holder.tv_moduleproject_deadlineper.setText(Tool.getPercentbycurrent(temp.getStartdate(), temp.getDeadline())+"%");
        holder.pb_moduleproject_progress.setProgress(temp.getProgress());
        holder.pb_moduleproject_deadline.setProgress(Tool.getPercentbycurrent(temp.getStartdate(), temp.getDeadline()));
        Tool.dr.child("User").child(temp.getRespid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.tv_moduleproject_name.setText(snapshot.child("name").getValue(String.class));
                holder.tv_moduleproject_phone.setText(Tool.getPhoneformat(snapshot.child("phone").getValue(String.class)));
                holder.tv_moduleproject_email.setText(snapshot.child("email").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = (View) View.inflate(v.getContext(), R.layout.dialog_moduleproject_progress, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Progress를 조절하세요.");
                dialog.setView(dialogView);
                sb_moduleproject_progress = (SeekBar) dialogView.findViewById(R.id.sb_moduleproject_progress);
                tv_moduleproject_seekbar = (TextView) dialogView.findViewById(R.id.tv_moduleproject_seekbar);
                sb_moduleproject_progress.setProgress(temp.getProgress());
                tv_moduleproject_seekbar.setText(temp.getProgress()+"%");
                sb_moduleproject_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        tv_moduleproject_seekbar.setText(i+"%");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("moduleproject").child(temp.getId()).child("progress").setValue(sb_moduleproject_progress.getProgress());
                        ProjectTool.updateProject(projectid);
                    }
                });
                dialog.setNegativeButton("취소", null);
                dialog.show();
            }
        };
        if((Tool.current_user.getId().equals(projectrespid) || Tool.current_user.getId().equals(subprojectrespid)) && !Tool.current_user.getId().equals(temp.getRespid()) )
        {
            holder.bt_moduleproject_modify.setVisibility(View.VISIBLE);
            holder.bt_moduleproject_delete.setVisibility(View.VISIBLE);
            holder.bt_moduleproject_call.setVisibility(View.VISIBLE);
            holder.bt_moduleproject_chat.setVisibility(View.VISIBLE);
            holder.bt_moduleproject_addfriend.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(listener);
        }
        else if(Tool.current_user.getId().equals(temp.getRespid()))
        {
            holder.bt_moduleproject_modify.setVisibility(View.VISIBLE);
            holder.bt_moduleproject_delete.setVisibility(View.VISIBLE);
            holder.bt_moduleproject_call.setVisibility(View.INVISIBLE);
            holder.bt_moduleproject_chat.setVisibility(View.INVISIBLE);
            holder.bt_moduleproject_addfriend.setVisibility(View.INVISIBLE);
            holder.itemView.setOnClickListener(listener);
        }
        else
        {
            holder.bt_moduleproject_modify.setVisibility(View.INVISIBLE);
            holder.bt_moduleproject_delete.setVisibility(View.INVISIBLE);
            holder.bt_moduleproject_call.setVisibility(View.VISIBLE);
            holder.bt_moduleproject_chat.setVisibility(View.VISIBLE);
            holder.bt_moduleproject_addfriend.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(null);
        }
        holder.bt_moduleproject_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatTool.createChat(moduleprojectActivityContext,Tool.current_user.getId(), temp.getRespid());
                Intent intent =new Intent((v.getContext()).getApplicationContext(), MessageActivity.class);
                intent.putExtra("otherid",temp.getRespid());
                (v.getContext()).startActivity(intent);
            }
        });

        holder.bt_moduleproject_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+holder.tv_moduleproject_phone.getText().toString()));
                (v.getContext()).startActivity(intent);
            }
        });

        holder.bt_moduleproject_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent((v.getContext()).getApplicationContext(), ModifyActivity.class);
                intent.putExtra("projectid", projectid);
                intent.putExtra("subprojectid", subprojectid);
                intent.putExtra("moduleprojectid", temp.getId());
                (v.getContext()).startActivity(intent);
            }
        });
        holder.bt_moduleproject_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectTool.deletemoduleProject(projectid,subprojectid,temp.getId());
            }
        });
        holder.bt_moduleproject_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendTool.addFriend(v.getContext(),temp.getRespid());
            }
        });
        Tool.sdr.child( temp.getRespid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProjectTool.moduleprojectActivityContext).load(uri).into(holder.iv_moduleproject_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.iv_moduleproject_image.setImageResource(R.drawable.profiledefault);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moduleproject_list.size();
    }

    static class ModuleProjectHolder extends RecyclerView.ViewHolder {
        TextView tv_moduleproject_title, tv_moduleproject_startdate, tv_moduleproject_progressper, tv_moduleproject_deadlineper;
        TextView tv_moduleproject_weight, tv_moduleproject_deadline, tv_moduleproject_name, tv_moduleproject_phone, tv_moduleproject_email;
        Button bt_moduleproject_chat, bt_moduleproject_call, bt_moduleproject_modify, bt_moduleproject_delete, bt_moduleproject_addfriend;
        ImageView iv_moduleproject_image;
        ProgressBar pb_moduleproject_progress, pb_moduleproject_deadline;

        public ModuleProjectHolder(@NonNull View itemView) {
            super(itemView);
            tv_moduleproject_title = (TextView)itemView.findViewById(R.id.tv_moduleproject_title);
            tv_moduleproject_startdate = (TextView)itemView.findViewById(R.id.tv_moduleproject_startdate);
            tv_moduleproject_progressper = (TextView)itemView.findViewById(R.id.tv_moduleproject_progressper);
            tv_moduleproject_deadlineper = (TextView)itemView.findViewById(R.id.tv_moduleproject_deadlineper);
            tv_moduleproject_weight = (TextView)itemView.findViewById(R.id.tv_moduleproject_weight);
            tv_moduleproject_deadline = (TextView)itemView.findViewById(R.id.tv_moduleproject_deadline);
            tv_moduleproject_name = (TextView)itemView.findViewById(R.id.tv_moduleproject_name);
            tv_moduleproject_phone = (TextView)itemView.findViewById(R.id.tv_moduleproject_phone);
            tv_moduleproject_email = (TextView)itemView.findViewById(R.id.tv_moduleproject_email);

            bt_moduleproject_chat = (Button)itemView.findViewById(R.id.bt_moduleproject_chat);
            bt_moduleproject_call = (Button)itemView.findViewById(R.id.bt_moduleproject_call);
            bt_moduleproject_modify = (Button)itemView.findViewById(R.id.bt_moduleproject_modify);
            bt_moduleproject_delete = (Button)itemView.findViewById(R.id.bt_moduleproject_delete);
            bt_moduleproject_addfriend = (Button)itemView.findViewById(R.id.bt_moduleproject_addfriend);

            iv_moduleproject_image = (ImageView)itemView.findViewById(R.id.iv_moduleproject_image);

            pb_moduleproject_progress = (ProgressBar)itemView.findViewById(R.id.pb_moduleproject_progress);
            pb_moduleproject_deadline = (ProgressBar)itemView.findViewById(R.id.pb_moduleproject_deadline);



        }
    }
}
