package com.work.wb.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.adapter.ProjectListAdapter;
import com.work.wb.entity.Project;
import com.work.wb.util.Utils;
import com.work.wb.util.model.NodeModel;
import com.work.wb.util.model.ProjectRoleModel;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    private ListView listViewProject;
    private ProjectListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        //toolbar_main
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");

        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText("选择项目");
        setSupportActionBar(toolbar);

        listViewProject = findViewById(R.id.listView_project);
        List<ProjectRoleModel> list = (List<ProjectRoleModel>) Utils.readObject(getApplicationContext(),"projectRoles",new TypeToken<List<ProjectRoleModel>>() {}.getType());


        adapter =new ProjectListAdapter(list,getApplicationContext());
        listViewProject.setAdapter(adapter);
        listViewProject.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                ProjectRoleModel model=(ProjectRoleModel) listViewProject.getItemAtPosition(arg2);
                Utils.saveObject(getApplicationContext(),model,"currentProject");
                Intent intent = new Intent(ProjectActivity.this,MainActivity.class);
                overridePendingTransition(R.anim.open_enter, R.anim.open_exit);
                startActivity(intent);
                finish();
            }
        });
    }
}
