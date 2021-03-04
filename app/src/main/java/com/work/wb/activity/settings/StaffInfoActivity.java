package com.work.wb.activity.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.work.wb.R;
import com.work.wb.adapter.DataDetailAdapter;
import com.work.wb.entity.Role;
import com.work.wb.entity.Staff;
import com.work.wb.util.Utils;
import com.work.wb.util.model.DataDetailModel;
import com.work.wb.util.model.ExpenseModel;
import com.work.wb.util.model.ProjectRoleModel;

import java.util.ArrayList;
import java.util.List;

public class StaffInfoActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ListView listViewData;
    private DataDetailAdapter adapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_data_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tvTitle= findViewById(R.id.toolbar_title);
        Staff user  = (Staff) Utils.readObject(getApplicationContext(),"currentUser",Staff.class);
        tvTitle.setText("个人信息");
        listViewData = findViewById(R.id.listView_data);
        List<DataDetailModel> list = new ArrayList<>();
        list.add(new DataDetailModel("姓名",user.getName()));
        list.add(new DataDetailModel("性别",user.getSex()==0?"男":"女"));
        list.add(new DataDetailModel("手机",user.getMobile()));
        list.add(new DataDetailModel("身份证号",user.getIdcard()));

        ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
        StringBuffer roles = new StringBuffer();
        for(Role role:project.getRoles()){
            roles.append(role.getName()+",");
        }
        if(roles.length()>0) roles.deleteCharAt(roles.length()-1);
        list.add(new DataDetailModel("角色",roles.toString()));

        adapter = new DataDetailAdapter(list,getApplicationContext());
        listViewData.setAdapter(adapter);
    }
}
