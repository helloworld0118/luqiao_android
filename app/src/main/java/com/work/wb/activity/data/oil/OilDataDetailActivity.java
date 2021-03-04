package com.work.wb.activity.data.oil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.work.wb.R;
import com.work.wb.adapter.DataDetailAdapter;
import com.work.wb.entity.MechanicsPrice;
import com.work.wb.entity.Oil;
import com.work.wb.util.model.DataDetailModel;

import java.util.ArrayList;
import java.util.List;

public class OilDataDetailActivity extends AppCompatActivity {
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
        Oil model = (Oil) getIntent().getSerializableExtra("model");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tvTitle= findViewById(R.id.toolbar_title);
        tvTitle.setText("详情");
        listViewData = findViewById(R.id.listView_data);
        List<DataDetailModel> list = new ArrayList<>();
        list.add(new DataDetailModel("表单类型","加油单"));
        list.add(new DataDetailModel("车牌号",model.getPlateNumber()));
        list.add(new DataDetailModel("车辆名称",model.getMechanicName()));
        list.add(new DataDetailModel("司机姓名",model.getDriverName()));
        list.add(new DataDetailModel("加油类型",model.getOilType()));
        list.add(new DataDetailModel("数量",model.getIcount()+"升"));
        list.add(new DataDetailModel("金额",model.getPrice()+"元"));
        list.add(new DataDetailModel("提交时间",model.getCreateDate()));
//        list.add(new DataDetailModel("提交人",model.getStaffName()));
        list.add(new DataDetailModel("备注",model.getRemark()));
        adapter = new DataDetailAdapter(list,getApplicationContext());
        listViewData.setAdapter(adapter);
    }
}
