package com.work.wb.activity.data.material;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.work.wb.R;
import com.work.wb.adapter.DataDetailAdapter;
import com.work.wb.entity.MaterialReceived;
import com.work.wb.entity.MechanicsPrice;
import com.work.wb.util.model.DataDetailModel;

import java.util.ArrayList;
import java.util.List;

public class MaterialDataDetailActivity extends AppCompatActivity {
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
        MaterialReceived model = (MaterialReceived) getIntent().getSerializableExtra("model");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tvTitle= findViewById(R.id.toolbar_title);
        tvTitle.setText("详情");
        listViewData = findViewById(R.id.listView_data);
        List<DataDetailModel> list = new ArrayList<>();
        list.add(new DataDetailModel("表单类型","机械单"));
        list.add(new DataDetailModel("分部工程",model.getProgram()));
        list.add(new DataDetailModel("桩号",model.getNode()));
        list.add(new DataDetailModel("工序",model.getIprocedure()));
        list.add(new DataDetailModel("拉料车",model.getPlateNumber()));
        list.add(new DataDetailModel("供应商",model.getSupplierName()));
        list.add(new DataDetailModel("材料",model.getMaterialName()));
        list.add(new DataDetailModel("规格",model.getMaterialSpec()));
        list.add(new DataDetailModel("数量",model.getIcount()+""+model.getBaseType()));
        list.add(new DataDetailModel("运输金额\n数量("+model.getIcount()+")*距离("+model.getDistance()+")*运输单价("+model.getUnitFreightPrice()+")",model.getFreightPrice()+"元"));
        list.add(new DataDetailModel("材料金额\n数量("+model.getIcount()+")*材料单价("+model.getUnitPrice()+")",model.getMaterialPrice()+"元"));
        list.add(new DataDetailModel("提交时间",model.getCreateDate()));
//        list.add(new DataDetailModel("提交人",model.getStaffName()));
        list.add(new DataDetailModel("备注",model.getRemark()));
        adapter = new DataDetailAdapter(list,getApplicationContext());
        listViewData.setAdapter(adapter);
    }
}
