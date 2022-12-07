package com.yuseung.projectmanagement.Tool;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yuseung.projectmanagement.Data.ModuleProject;
import com.yuseung.projectmanagement.Data.Project;
import com.yuseung.projectmanagement.Data.SubProject;


public class ProjectTool {
    public static Context projectFragmentContext;
    public static Context subprojectActivityContext;
    public static Context moduleprojectActivityContext;
    static public void createProject(String title)
    {
        String time = Tool.getChangeymdtime(Tool.getCurrenttime());
        Project temp = new Project(Tool.hashConverter(title+time),title,Tool.current_user.getId(),time,time,0,0,0);
        Tool.dr.child("Project").child(temp.getId()).setValue(temp);
        Toast.makeText(projectFragmentContext, "프로젝트 생성 성공 !!", Toast.LENGTH_SHORT).show();
    }
    static public void deleteProject(String projectid)
    {
        Tool.dr.child("Project").child(projectid).removeValue();
        Toast.makeText(projectFragmentContext, "프로젝트 삭제 성공 !!", Toast.LENGTH_SHORT).show();
    }
    static public void createsubProject(String projectid, String title)
    {
        String time = Tool.getChangeymdtime(Tool.getCurrenttime());
        SubProject temp = new SubProject(Tool.hashConverter(title),title,Tool.current_user.getId(),time,time,0,0,0,0);
        Tool.dr.child("Project").child(projectid).child("subproject").child(temp.getId()).setValue(temp);
        Toast.makeText(subprojectActivityContext, "서브 프로젝트 생성 성공 !!", Toast.LENGTH_SHORT).show();
    }
    static public void deletesubProject(String projectid, String subprojectid)
    {
        Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).removeValue();
        Toast.makeText(subprojectActivityContext, "서브 프로젝트 삭제 성공 !!", Toast.LENGTH_SHORT).show();
    }
    static public void createmoduleProject(String projectid,String subprojectid,String title)
    {
        String time = Tool.getChangeymdtime(Tool.getCurrenttime());
        ModuleProject temp = new ModuleProject(Tool.hashConverter(title),title,Tool.current_user.getId(),time,time,0,0);
        Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("moduleproject").child(temp.getId()).setValue(temp);
        Toast.makeText(subprojectActivityContext, "모듈 프로젝트 생성 성공 !!", Toast.LENGTH_SHORT).show();
    }
    static public void deletemoduleProject(String projectid,String subprojectid,String moduleprojectid)
    {
        Tool.dr.child("Project").child(projectid).child("subproject").child(subprojectid).child("moduleproject").child(moduleprojectid).removeValue();
        Toast.makeText(subprojectActivityContext, "모듈 프로젝트 삭제 성공 !!", Toast.LENGTH_SHORT).show();
    }
    static public void updateProject(String projectid)
    {
        Tool.dr.child("Project").child(projectid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum_subweight = 0;
                double sum_subpro = 0;
                int sum_subcount = 0;
                for(DataSnapshot i : snapshot.child("subproject").getChildren())
                {
                    int sum_moduleweight = 0;
                    double sum_modulepro = 0;
                    int sum_modulecount = 0;
                    for(DataSnapshot j : i.child("moduleproject").getChildren())
                    {
                        int weight = j.child("weight").getValue(Integer.class);
                        int progress = j.child("progress").getValue(Integer.class);
                        double temp = weight * (progress * 0.01);
                        sum_modulepro += temp;
                        sum_moduleweight += weight;
                        sum_modulecount++;
                    }
                    int weight_sub = i.child("weight").getValue(Integer.class);
                    int progress_sub = i.child("progress").getValue(Integer.class);
                    double temp_sub = weight_sub * (progress_sub * 0.01);
                    sum_subpro += temp_sub;
                    sum_subweight += weight_sub;

                    Tool.dr.child("Project").child(projectid).child("subproject").child(i.getKey()).child("totalweight").setValue(sum_moduleweight);
                    Tool.dr.child("Project").child(projectid).child("subproject").child(i.getKey()).child("progress").setValue((int)(sum_modulepro/sum_moduleweight*100));
                    Tool.dr.child("Project").child(projectid).child("subproject").child(i.getKey()).child("totaltask").setValue(sum_modulecount);
                    sum_subcount += sum_modulecount;
                }
                Tool.dr.child("Project").child(projectid).child("totalweight").setValue(sum_subweight);
                Tool.dr.child("Project").child(projectid).child("progress").setValue((int)(sum_subpro/sum_subweight*100));
                Tool.dr.child("Project").child(projectid).child("totaltask").setValue(sum_subcount);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
