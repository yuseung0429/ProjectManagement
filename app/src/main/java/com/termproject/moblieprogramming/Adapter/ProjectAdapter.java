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
import com.termproject.moblieprogramming.Activity.MessageActivity;
import com.termproject.moblieprogramming.Activity.ModifyActivity;
import com.termproject.moblieprogramming.Activity.SubProjectActivity;
import com.termproject.moblieprogramming.Data.Message;
import com.termproject.moblieprogramming.Data.Project;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.ProjectTool;
import com.termproject.moblieprogramming.Tool.Tool;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder>{

    ArrayList<Project> project_list;
    public ProjectAdapter(ArrayList<Project> project_list)
    {
        this.project_list = project_list;
    }

    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_project, parent, false);
        return new ProjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {
        Project temp = project_list.get(position);
        holder.tv_project_title.setText(temp.getTitle());
        holder.tv_project_startdate.setText(Tool.getChangeymdbartime(temp.getStartdate()));
        holder.tv_project_totaltask.setText("total task\n" + temp.getTotaltask());
        holder.tv_project_progressper.setText(temp.getProgress() + "%");
        holder.pb_project_deadline.setProgress(Tool.getPercentbycurrent(temp.getStartdate(), temp.getDeadline()));
        holder.tv_project_deadlineper.setText(Tool.getPercentbycurrent(temp.getStartdate(), temp.getDeadline())+"%");
        holder.pb_project_progress.setProgress(temp.getProgress());
        holder.bt_project_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectTool.deleteProject(temp.getId());
            }
        });
        holder.bt_project_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent((v.getContext()).getApplicationContext(), InfoActivity.class);
                intent.putExtra("projectid", temp.getId());
                intent.putExtra("respid", temp.getRespid());
                (v.getContext()).startActivity(intent);
            }
        });
        holder.bt_project_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent((v.getContext()).getApplicationContext(), ModifyActivity.class);
                intent.putExtra("projectid", temp.getId());
                (v.getContext()).startActivity(intent);
            }
        });
        if(Tool.current_user.getId().equals(temp.getRespid()))
        {
            holder.bt_project_modify.setVisibility(View.VISIBLE);
            holder.bt_project_delete.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.bt_project_modify.setVisibility(View.INVISIBLE);
            holder.bt_project_delete.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent((v.getContext()).getApplicationContext(), SubProjectActivity.class);
                intent.putExtra("projectid", temp.getId());
                intent.putExtra("projectrespid", temp.getRespid());
                (v.getContext()).startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return project_list.size();
    }

    static class ProjectHolder extends RecyclerView.ViewHolder {
        TextView tv_project_title, tv_project_startdate, tv_project_progressper, tv_project_totaltask, tv_project_deadlineper;
        Button bt_project_info, bt_project_modify, bt_project_delete;
        ProgressBar pb_project_progress, pb_project_deadline;
        public ProjectHolder(@NonNull View itemView) {
            super(itemView);
            tv_project_title = (TextView)itemView.findViewById(R.id.tv_project_title);
            tv_project_startdate = (TextView)itemView.findViewById(R.id.tv_project_startdate);
            tv_project_progressper = (TextView)itemView.findViewById(R.id.tv_project_progressper);
            tv_project_deadlineper = (TextView)itemView.findViewById(R.id.tv_project_deadlineper);
            tv_project_totaltask = (TextView)itemView.findViewById(R.id.tv_project_totaltask);

            bt_project_info = (Button)itemView.findViewById(R.id.bt_project_info);
            bt_project_modify = (Button)itemView.findViewById(R.id.bt_project_modify);
            bt_project_delete = (Button)itemView.findViewById(R.id.bt_project_delete);

            pb_project_progress = (ProgressBar)itemView.findViewById(R.id.pb_project_progress);
            pb_project_deadline = (ProgressBar)itemView.findViewById(R.id.pb_project_deadline);
        }
    }
}
