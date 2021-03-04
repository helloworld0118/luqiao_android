package com.work.wb.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.work.wb.R;
import com.work.wb.util.model.NodeModel;

import java.util.List;

/**
 * Created by bing.wang on 2018/1/22.
 */

public class NodeListAdapter extends BaseAdapter {
    private List<NodeModel> list;
    private LayoutInflater inflater;
    public NodeListAdapter(List<NodeModel> list,Context context) {
        this.list = list;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NodeModel getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void setList(List<NodeModel> list){
        this.list=list;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //View view=inflater.inflate(R.layout.layout_student_item,null);
        View item = inflater.inflate(R.layout.node_item,null);
        NodeModel model = list.get(i);
        TextView tv_node = item.findViewById(R.id.tv_node);
        TextView tv_date = item.findViewById(R.id.tv_date);
        tv_node.setText(model.getNode());
        tv_date.setText(model.getDate());
        return item;
    }
}
