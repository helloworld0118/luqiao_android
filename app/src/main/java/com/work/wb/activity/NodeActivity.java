package com.work.wb.activity;





import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.activity.node.GaiBanHanFragment;
import com.work.wb.activity.node.XiangHanFragment;
import com.work.wb.activity.node.YuanGuanHanFragment;
import com.work.wb.adapter.NodeListAdapter;
import com.work.wb.util.FragmentpagerAdapter;
import com.work.wb.util.NetWorkUtils;
import com.work.wb.util.Utils;
import com.work.wb.util.model.NodeModel;
import com.work.wb.util.model.ProjectRoleModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeActivity extends AppCompatActivity {
    private String title;
    private String program;
    private TextView tvProgramShow;
    private ListView lvNodeView;
    private NodeListAdapter adapter;
    private static final String TAG = "NodeActivity";
    private List<NodeModel> data=new ArrayList<>();
    private int type;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_refresh:
                refreshData();
                break;
        }
        return true;
    }
    private void refreshData(){
        try {
            if(NetWorkUtils.isNetworkConnected(getApplicationContext())){
                Utils.cleanShardDataByKey(getApplicationContext(), type+"_date");
            }
            new Thread(runnable).start();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        title = intent.getStringExtra("title");
        program = intent.getStringExtra("program");
        toolbar.setTitle(title);
        toolbar.setSubtitle("选择桩号(2/4)");
        setSupportActionBar(toolbar);
        tvProgramShow = findViewById(R.id.tv_program_show);
        tvProgramShow.setText("当前分部工程："+program);
        lvNodeView = findViewById(R.id.listView_nodes);
        if(program.equals("圆管涵")){
            type=1;
        }else if(program.equals("盖板涵")){
            type=2;
        }else if(program.equals("箱涵")){
            type=3;
        }
        adapter = new NodeListAdapter(data,getApplicationContext());
        lvNodeView.setAdapter(adapter);
        lvNodeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                NodeModel model=(NodeModel) lvNodeView.getItemAtPosition(arg2);

                Intent intent = new Intent(NodeActivity.this,ProcedureActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("program", program);
                intent.putExtra("node", model.getNode());
                startActivity(intent);
            }
        });
        refreshData();
    }

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();

            String lastDate = (String) Utils.readObject(getApplicationContext(),type+"_date",String.class);
            if(lastDate!=null&&!"".equals(lastDate)&&Utils.getCurrentDate().equals(lastDate)){
                String result = (String) Utils.readObject(getApplicationContext(),type+"_nodes",String.class);
                data.putString("data",result);
                msg.setData(data);
                handler.sendMessage(msg);
            }else {
                Map<String,String> params = new HashMap<>();
                params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
                params.put("type",type+"");
                try {
                    String result = Utils.httpGet("api/getNodes",params,getApplicationContext());
                    if(result.length()>2){
                        data.putString("data",result);
                    }else{
                        data.putString("data",null);
                    }
                    msg.setData(data);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    Log.e(TAG, "error========"+e.getMessage());
                    msg.setData(data);
                    data.putString("data",e.getMessage());
                    handler.sendMessage(msg);
                }
            }

        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
                try{
                    data = new Gson().fromJson(result,new TypeToken<List<NodeModel>>() {}.getType());
                    adapter.setList(data);
                    adapter.notifyDataSetChanged();
                    Utils.saveObject(getApplicationContext(),Utils.getCurrentDate(),type+"_date");
                    Utils.saveObject(getApplicationContext(),result,type+"_nodes");
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "数据读取出错",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
}
