package com.work.wb.activity.data.oil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.activity.data.mechainc.MechaincDataDetailActivity;
import com.work.wb.adapter.MechaincListAdapter;
import com.work.wb.adapter.OilListAdapter;
import com.work.wb.entity.MechanicsPrice;
import com.work.wb.entity.Oil;
import com.work.wb.entity.Role;
import com.work.wb.entity.Staff;
import com.work.wb.util.NetWorkUtils;
import com.work.wb.util.Utils;
import com.work.wb.util.model.ProjectRoleModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OilAllDayDataActivity extends AppCompatActivity {

    private TextView tvTitle;
    private List<Oil> list=new ArrayList<>();
    private ListView listViewData;
    private OilListAdapter adapter;
    private TextView tvAllSize;
    private TextView tvAllPrice;
    private TextView tvDataTitleName;
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
        setContentView(R.layout.activity_all_day_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle= findViewById(R.id.toolbar_title);
        listViewData = findViewById(R.id.listView_data);
        adapter = new OilListAdapter(list,getApplicationContext());
        listViewData.setAdapter(adapter);
        tvTitle.setText("历史数据");
        tvDataTitleName = findViewById(R.id.tv_data_title_name);
        tvDataTitleName.setText("加油单");
        tvAllSize = findViewById(R.id.tv_all_size);
        tvAllPrice = findViewById(R.id.tv_all_price);
        setSupportActionBar(toolbar);
        try {

            new Thread(allday_runnable).start();
        } catch (Exception e) {
            Log.e("YuanGuanHan", e.getMessage());
        }
        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Oil model=(Oil) listViewData.getItemAtPosition(arg2);

                Intent intent = new Intent(OilAllDayDataActivity.this,OilDataDetailActivity.class);
                intent.putExtra("model",model);
                startActivity(intent);
            }
        });
    }

    Runnable allday_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            String lastDate = (String) Utils.readObject(getApplicationContext(),"oil_all_date",String.class);
            if((lastDate!=null&&!"".equals(lastDate)&&Utils.getCurrentDate().equals(lastDate))||!NetWorkUtils.isNetworkConnected(getApplicationContext())){
                String result = (String) Utils.readObject(getApplicationContext(),"oil_all_data",String.class);
                data.putString("data",result);
                msg.setData(data);
                allday_handler.sendMessage(msg);
            }else{
                if(!NetWorkUtils.isNetworkConnected(getApplicationContext())){
                    return;
                }
                Map<String,String> params = new HashMap<>();
                Staff user  = (Staff) Utils.readObject(getApplicationContext(),"currentUser",Staff.class);
                ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
                params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
                params.put("project",project.getProject().getId()+"");
                StringBuffer roles = new StringBuffer();
                for(Role role:project.getRoles()){
                    roles.append(role.getId()+",");
                }
                roles.deleteCharAt(roles.length()-1);
                params.put("roles",roles.toString());
                params.put("staff",user.getId()+"");
                try {
                    String result = Utils.httpGet("api/oil/list",params,getApplicationContext());
                    Log.e("result=========",result);
                    data.putString("data",result);
                    msg.setData(data);
                    allday_handler.sendMessage(msg);
                } catch (IOException e) {
                    Log.e("YuanGuanHan", "error========"+e.getMessage());
                }
            }
        }
    };

    Handler allday_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
                try{
                    list = new Gson().fromJson(result,new TypeToken<List<Oil>>() {}.getType());
                    int allPrice=0;
                    for (Oil entity:list) {
                        allPrice+=entity.getPrice();
                    }
                    tvAllSize.setText(list.size()+"");
                    tvAllPrice.setText(allPrice+"");
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                    Utils.saveObject(getApplicationContext(),Utils.getCurrentDate(),"oil_all_date");
                    Utils.saveObject(getApplicationContext(),result,"oil_all_data");
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "数据读取出错",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
