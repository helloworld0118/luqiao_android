package com.work.wb.activity.data.expense;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.work.wb.R;
import com.work.wb.adapter.DataDetailAdapter;
import com.work.wb.entity.MaterialReceived;
import com.work.wb.util.model.DataDetailModel;
import com.work.wb.util.model.ExpenseModel;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDataDetailActivity extends AppCompatActivity {
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
        ExpenseModel model = (ExpenseModel) getIntent().getSerializableExtra("model");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tvTitle= findViewById(R.id.toolbar_title);
        tvTitle.setText("详情");
        listViewData = findViewById(R.id.listView_data);
        List<DataDetailModel> list = new ArrayList<>();
        list.add(new DataDetailModel("表单类型","报销单"));
        list.add(new DataDetailModel("报销人",model.getPerson()));
        list.add(new DataDetailModel("票据类型",model.getBillType()));
        list.add(new DataDetailModel("费用类型",model.getPriceType()));
        list.add(new DataDetailModel("报销金额",model.getPrice()+"元"));
        list.add(new DataDetailModel("提交时间",model.getCreateDate()));
//        list.add(new DataDetailModel("提交人",model.getStaffName()));
        list.add(new DataDetailModel("备注",model.getRemark()));
        adapter = new DataDetailAdapter(list,getApplicationContext());
        listViewData.setAdapter(adapter);
    }
}
