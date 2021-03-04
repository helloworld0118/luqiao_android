package com.work.wb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.work.wb.R;
import com.work.wb.util.model.DataDetailModel;
import com.work.wb.util.model.NodeModel;

import java.util.List;

/**
 * Created by bing.wang on 2018/1/22.
 */

public class DataDetailAdapter extends BaseAdapter {
    private List<DataDetailModel> list;
    private LayoutInflater inflater;
    private Context context;
    public DataDetailAdapter(List<DataDetailModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DataDetailModel getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void setList(List<DataDetailModel> list){
        this.list=list;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View item = inflater.inflate(R.layout.data_detail_item,null);
        DataDetailModel model = list.get(i);
        TextView tv_key = item.findViewById(R.id.tv_key);
        TextView tv_value = item.findViewById(R.id.tv_value);
        if(model.getKey().indexOf("运输金额")>=0||model.getKey().indexOf("材料金额")>=0){
            RelativeLayout rl_detail = item.findViewById(R.id.rl_detail);
            final float scale =context.getResources().getDisplayMetrics().density;
            int px= (int) (70 * scale + 0.5f);
            rl_detail.getLayoutParams().height=px;
        }
        tv_key.setText(model.getKey());
        tv_value.setText(model.getValue());
        return item;
    }
}
