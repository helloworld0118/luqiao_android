package com.work.wb.activity.data.labor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.work.wb.R;
import com.work.wb.adapter.DataDetailAdapter;
import com.work.wb.entity.LaborCost;
import com.work.wb.util.model.DataDetailModel;

import java.util.ArrayList;
import java.util.List;

public class LaborDataDetailActivity extends AppCompatActivity {
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
        LaborCost model = (LaborCost) getIntent().getSerializableExtra("model");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tvTitle= findViewById(R.id.toolbar_title);
        tvTitle.setText("详情");
        listViewData = findViewById(R.id.listView_data);
        List<DataDetailModel> list = new ArrayList<>();
        list.add(new DataDetailModel("表单类型","人工单"));
        list.add(new DataDetailModel("分部工程",model.getProgram()));
        list.add(new DataDetailModel("桩号",model.getNode()));
        list.add(new DataDetailModel("工序",model.getIprocedure()));
        list.add(new DataDetailModel("工头姓名",model.getForemanName()));
        list.add(new DataDetailModel("工种",model.getWorkerType()));
        list.add(new DataDetailModel("计费方式",model.getUnitPriceType()));
        list.add(new DataDetailModel("出工人数",model.getWorkerNums()+"人"));
        if(model.getUnitPriceType().indexOf("日")<0){
            list.add(new DataDetailModel("完成量",model.getIcount()+""+model.getBaseType()));
            //list.add(new DataDetailModel("单位",model.getBaseType()));
            list.add(new DataDetailModel("金额=完成量("+model.getIcount()+")*计件单价("+model.getUnitPrice()+")",model.getPrice()+"元"));
        }else{
            list.add(new DataDetailModel("金额=出工人数("+model.getWorkerNums()+")*计日单价("+model.getUnitPrice()+")",model.getPrice()+"元"));
        }


        list.add(new DataDetailModel("提交时间",model.getCreateDate()));
//        list.add(new DataDetailModel("提交人",model.getStaffName()));
        list.add(new DataDetailModel("备注",model.getRemark()));
        adapter = new DataDetailAdapter(list,getApplicationContext());
        listViewData.setAdapter(adapter);
    }
}
