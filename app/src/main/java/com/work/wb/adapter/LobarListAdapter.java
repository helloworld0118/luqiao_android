package com.work.wb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.work.wb.R;
import com.work.wb.entity.LaborCost;

import java.util.List;

/**
 * Created by bing.wang on 2018/1/22.
 */

public class LobarListAdapter extends BaseAdapter {
    private List<LaborCost> list;
    private LayoutInflater inflater;
    public LobarListAdapter(List<LaborCost> list,Context context) {
        this.list = list;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public LaborCost getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void setList(List<LaborCost> list){
        this.list=list;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //View view=inflater.inflate(R.layout.layout_student_item,null);
        View item = inflater.inflate(R.layout.data_common_list_item,null);
        LaborCost model = list.get(i);
        TextView tv_title = item.findViewById(R.id.item_tv_data_title);
        tv_title.setText("人工单");
        TextView tv_date = item.findViewById(R.id.tv_date);
        tv_date.setText(model.getCreateDate().substring(0,16));
        TextView key_1 = item.findViewById(R.id.item_tv_data_title_key_1);
        key_1.setText("工头姓名");
        TextView key_2 = item.findViewById(R.id.item_tv_data_title_key_2);
        key_2.setText("出工人数");
        TextView key_3 = item.findViewById(R.id.item_tv_data_title_key_3);
        key_3.setText("金额");
        TextView value_1 = item.findViewById(R.id.item_tv_data_title_value_1);
        value_1.setText(model.getForemanName());
        TextView value_2 = item.findViewById(R.id.item_tv_data_title_value_2);
        value_2.setText(model.getWorkerNums()+"");
        TextView value_3 = item.findViewById(R.id.item_tv_data_title_value_3);
        value_3.setText(model.getPrice()+"");

        return item;
    }


}
