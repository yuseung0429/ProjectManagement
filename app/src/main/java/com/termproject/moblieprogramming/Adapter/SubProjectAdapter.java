package com.termproject.moblieprogramming.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.termproject.moblieprogramming.Activity.InfoActivity;
import com.termproject.moblieprogramming.Activity.ModifyActivity;
import com.termproject.moblieprogramming.Activity.ModuleProjectActivity;
import com.termproject.moblieprogramming.Activity.SubProjectActivity;
import com.termproject.moblieprogramming.Data.Message;
import com.termproject.moblieprogramming.Data.SubProject;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.ProjectTool;
import com.termproject.moblieprogramming.Tool.Tool;

import java.util.ArrayList;

public class SubProjectAdapter extends RecyclerView.Adapter<SubProjectAdapter.SubProjectHolder>{

    ArrayList<SubProject> subproject_list;
    String projectid;
    String projectrespid;
    public SubProjectAdapter(String projectid, String projectrespid,ArrayList<SubProject> subproject_list) {
        this.projectid = projectid;
        this.projectrespid = projectrespid;
        this.subproject_list = subproject_list;
    }

    @Override
    public SubProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_subproject, parent, false);
        return new SubProjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubProjectHolder holder, int position) {
        SubProject temp = subproject_list.get(position);
        holder.tv_subproject_title.setText(temp.getTitle());
        holder.tv_subproject_startdate.setText(Tool.getChangeymdbartime(temp.getStartdate()));
        holder.tv_subproject_progressper.setText(temp.getProgress()+"%");
        holder.tv_subproject_deadlineper.setText(Tool.getPercentbycurrent(temp.getStartdate(), temp.getDeadline())+"%");
        holder.tv_subproject_totaltask.setText("Total task\n"+temp.getTotaltask());
        holder.tv_subproject_weight.setText("Weight\n"+temp.getWeight());
        holder.tv_subproject_deadline.setText("Deadline\n"+Tool.getChangeymdbartime(temp.getDeadline()));

        holder.pb_subproject_progress.setProgress(temp.getProgress());
        holder.pb_subproject_deadline.setProgress(Tool.getPercentbycurrent(temp.getStartdate(), temp.getDeadline()));

        if(Tool.current_user.getId().equals(temp.getRespid()) || Tool.current_user.getId().equals(projectrespid))
        {
            holder.bt_subproject_modify.setVisibility(View.VISIBLE);
            holder.bt_subproject_delete.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.bt_subproject_modify.setVisibility(View.INVISIBLE);
            holder.bt_subproject_delete.setVisibility(View.INVISIBLE);
        }

        holder.bt_subproject_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent((v.getContext()).getApplicationContext(), InfoActivity.class);
                intent.putExtra("projectid", projectid);
                intent.putExtra("subprojectid", temp.getId());
                intent.putExtra("respid", temp.getRespid());
                (v.getContext()).startActivity(intent);
            }
        });
        holder.bt_subproject_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent((v.getContext()).getApplicationContext(), ModifyActivity.class);
                intent.putExtra("projectid", projectid);
                intent.putExtra("subprojectid", temp.getId());
                (v.getContext()).startActivity(intent);

            }
        });
        holder.bt_subproject_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectTool.deletesubProject(projectid, temp.getId());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent((v.getContext()).getApplicationContext(), ModuleProjectActivity.class);
                intent.putExtra("projectid", projectid);
                intent.putExtra("subprojectid", temp.getId());
                intent.putExtra("projectrespid", projectrespid);
                intent.putExtra("subprojectrespid", temp.getRespid());
                (v.getContext()).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subproject_list.size();
    }

    static class SubProjectHolder extends RecyclerView.ViewHolder {
        TextView tv_subproject_title, tv_subproject_startdate, tv_subproject_progressper, tv_subproject_deadlineper;
        TextView tv_subproject_totaltask, tv_subproject_weight, tv_subproject_deadline;
        ProgressBar pb_subproject_progress, pb_subproject_deadline;
        Button bt_subproject_info, bt_subproject_modify, bt_subproject_delete;

        public SubProjectHolder(@NonNull View itemView) {
            super(itemView);
            tv_subproject_title = (TextView)itemView.findViewById(R.id.tv_subproject_title);
            tv_subproject_startdate = (TextView)itemView.findViewById(R.id.tv_subproject_startdate);
            tv_subproject_progressper = (TextView)itemView.findViewById(R.id.tv_subproject_progressper);
            tv_subproject_deadlineper = (TextView)itemView.findViewById(R.id.tv_subproject_deadlineper);
            tv_subproject_totaltask = (TextView)itemView.findViewById(R.id.tv_subproject_totaltask);
            tv_subproject_weight = (TextView)itemView.findViewById(R.id.tv_subproject_weight);
            tv_subproject_deadline = (TextView)itemView.findViewById(R.id.tv_subproject_deadline);

            pb_subproject_progress = (ProgressBar)itemView.findViewById(R.id.pb_subproject_progress);
            pb_subproject_deadline = (ProgressBar)itemView.findViewById(R.id.pb_subproject_deadline);

            bt_subproject_info = (Button)itemView.findViewById(R.id.bt_subproject_info);
            bt_subproject_modify = (Button)itemView.findViewById(R.id.bt_subproject_modify);
            bt_subproject_delete = (Button)itemView.findViewById(R.id.bt_subproject_delete);

        }
    }
}
