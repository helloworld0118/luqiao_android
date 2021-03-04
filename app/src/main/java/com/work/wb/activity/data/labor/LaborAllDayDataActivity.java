package com.work.wb.activity.data.labor;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.work.wb.adapter.LobarListAdapter;
import com.work.wb.entity.LaborCost;
import com.work.wb.entity.Role;
import com.work.wb.entity.Staff;
import com.work.wb.util.NetWorkUtils;
import com.work.wb.util.Utils;
import com.work.wb.util.model.ExpenseModel;
import com.work.wb.util.model.ProjectRoleModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaborAllDayDataActivity extends AppCompatActivity {

    private TextView tvTitle;
    private List<LaborCost> list=new ArrayList<>();
    private ListView listViewData;
    private LobarListAdapter adapter;
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
        adapter = new LobarListAdapter(list,getApplicationContext());
        listViewData.setAdapter(adapter);
        tvTitle.setText("历史数据");
        tvDataTitleName = findViewById(R.id.tv_data_title_name);
        tvDataTitleName.setText("人工单");
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
                LaborCost model=(LaborCost) listViewData.getItemAtPosition(arg2);

                Intent intent = new Intent(LaborAllDayDataActivity.this,LaborDataDetailActivity.class);
                intent.putExtra("model",model);
                startActivity(intent);
                //SolutionDetailFragment fragment=SolutionDetailFragment.getInstance(bundle);
                //CUtils.replaceCurrentTab(fragment, "SolutionDetailFragment",getActivity());
            }
        });
    }

    Runnable allday_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            String lastDate = (String) Utils.readObject(getApplicationContext(),"labor_all_date",String.class);
            if((lastDate!=null&&!"".equals(lastDate)&&Utils.getCurrentDate().equals(lastDate))||!NetWorkUtils.isNetworkConnected(getApplicationContext())){
                String result = (String) Utils.readObject(getApplicationContext(),"labor_all_data",String.class);
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
                   String result = Utils.httpGet("api/labor/list",params,getApplicationContext());
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
                    list = new Gson().fromJson(result,new TypeToken<List<LaborCost>>() {}.getType());
                    int allPrice=0;
                    for (LaborCost entity:list) {
                        allPrice+=entity.getPrice();
                    }
                    tvAllSize.setText(list.size()+"");
                    tvAllPrice.setText(allPrice+"");
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                    Utils.saveObject(getApplicationContext(),Utils.getCurrentDate(),"labor_all_date");
                    Utils.saveObject(getApplicationContext(),result,"labor_all_data");
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "数据读取出错",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
