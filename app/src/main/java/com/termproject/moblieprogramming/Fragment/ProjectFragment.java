package com.termproject.moblieprogramming.Fragment;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.termproject.moblieprogramming.Activity.MainActivity;
import com.termproject.moblieprogramming.Adapter.ChatAdapter;
import com.termproject.moblieprogramming.Adapter.ProjectAdapter;
import com.termproject.moblieprogramming.Data.Chat;
import com.termproject.moblieprogramming.Data.Project;
import com.termproject.moblieprogramming.R;
import com.termproject.moblieprogramming.Tool.ChatTool;
import com.termproject.moblieprogramming.Tool.ProjectTool;
import com.termproject.moblieprogramming.Tool.Tool;

import java.util.ArrayList;

public class ProjectFragment extends Fragment {
    View dialogView;
    EditText et_project_title;
    RecyclerView rv_project;
    ArrayList<String> projectid_list;
    ArrayList<Project> project_list;
    ValueEventListener listener;
    ProjectAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ProjectTool.projectFragmentContext = getContext();
        Tool.projectfragment = this;
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("프로젝트");

        View v = inflater.inflate(R.layout.fragment_project, container, false);
        rv_project = (RecyclerView)v.findViewById(R.id.rv_project);

        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_project.setLayoutManager(lm);
        projectid_list = new ArrayList<>();
        project_list = new ArrayList<>();

        adapter = new ProjectAdapter(project_list);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                project_list.clear();
                for(DataSnapshot i : snapshot.getChildren())
                {
                    Project temp = i.getValue(Project.class);
                    project_list.add(temp);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Tool.dr.child("Project").addValueEventListener(listener);
        rv_project.setAdapter(adapter);
        return v;
    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_project, menu);

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.it_project_create:
            {
                dialogView = (View) View.inflate(getContext(), R.layout.dialog_project_create, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("프로젝트 이름을 입력하세요");
                dialog.setView(dialogView);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        et_project_title = (EditText) dialogView.findViewById(R.id.et_project_title);
                        ProjectTool.createProject(et_project_title.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("취소", null);
                dialog.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
