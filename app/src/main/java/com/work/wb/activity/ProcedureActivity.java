package com.work.wb.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.work.wb.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcedureActivity extends AppCompatActivity {
    private TextView tv_node_show;
    private GridView gv_procedure;
    private String title;
    private String node;
    private String program;
    private SimpleAdapter sim_adapter;
    private List<Map<String, Object>> procedure_list=new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.title = intent.getStringExtra("title");
        this.program = intent.getStringExtra("program");
        toolbar.setTitle(title);
        node = intent.getStringExtra("node");
        toolbar.setSubtitle("选择工序(3/4)");
        setSupportActionBar(toolbar);
        initData();
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(getApplicationContext(), procedure_list, R.layout.grid_item, from, to);
        tv_node_show = findViewById(R.id.node_show);
        tv_node_show.setText("当前桩号："+program+" -> "+node);
        gv_procedure = findViewById(R.id.gridView_procedure);
        gv_procedure.setAdapter(sim_adapter);
        gv_procedure.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {

                TextView textView = v.findViewById(R.id.text);
                Intent intent=null;
                if(title.indexOf("人工")>0){
                    intent=new Intent(ProcedureActivity.this,LobarActivity.class);
                    //intent.putExtra("title","填写人工计费单");
                }else if(title.indexOf("机械")>0){
                    intent=new Intent(ProcedureActivity.this,MechanicActivity.class);
                    //intent.putExtra("title","填写机械计费单");
                }else if(title.indexOf("材料")>0){
                    intent=new Intent(ProcedureActivity.this,MaterialActivity.class);
                    //intent.putExtra("title","填写材料单");
                }
                intent.putExtra("program",program);
                intent.putExtra("node",node);
                intent.putExtra("procedure",textView.getText().toString());
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    }
    public void initData(){
        Map<String, Object> map = new HashMap<String, Object>();
        procedure_list.clear();
        Log.e("ProduceActity",program);
        if(program.equals("圆管涵")){
            map.put("image", R.mipmap.jikeng);
            map.put("text", "基坑");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.jikenghuatian144);
            map.put("text", "基底换填");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.jichun144);
            map.put("text", "基础");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.maoshi_title  );
            map.put("text", "帽石");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.guanshen144);
            map.put("text", "管身");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.baziiqiang);
            map.put("text", "八字墙");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.shensuofeng144);
            map.put("text", "伸缩缝");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.taibei144);
            map.put("text", "台背");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.puqie144);
            map.put("text", "铺砌");
            procedure_list.add(map);
        }else{
            map.put("image", R.mipmap.jikeng);
            map.put("text", "基坑");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.jikenghuatian144);
            map.put("text", "基底换填");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.jichun144);
            map.put("text", "基础");
            procedure_list.add(map);

            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.gaibanhan144  );
            map.put("text", "盖板");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.maoshi_title  );
            map.put("text", "帽石");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.qiangshen);
            map.put("text", "墙身");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.baziiqiang);
            map.put("text", "八字墙");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.zhichengliang144);
            map.put("text", "支撑梁");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.shensuofeng144);
            map.put("text", "伸缩缝");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.taibei144);
            map.put("text", "台背");
            procedure_list.add(map);
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.puqie144);
            map.put("text", "铺砌");
            procedure_list.add(map);

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
