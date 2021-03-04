package com.work.wb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.work.wb.R;
import com.work.wb.entity.Project;
import com.work.wb.util.model.NodeModel;
import com.work.wb.util.model.ProjectRoleModel;

import java.util.List;

/**
 * Created by bing.wang on 2018/1/22.
 */

public class ProjectListAdapter extends BaseAdapter {
    private List<ProjectRoleModel> list;
    private LayoutInflater inflater;
    public ProjectListAdapter(List<ProjectRoleModel> list, Context context) {
        this.list = list;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ProjectRoleModel getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void setList(List<ProjectRoleModel> list){
        this.list=list;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //View view=inflater.inflate(R.layout.layout_student_item,null);
        View item = inflater.inflate(R.layout.project_item,null);
        ProjectRoleModel model = list.get(i);
        TextView tv_project = item.findViewById(R.id.tv_project);

        tv_project.setText(model.getProject().getName());
        return item;
    }
}
