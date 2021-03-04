package com.work.wb.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.entity.BaseType;
import com.work.wb.entity.Mechanics;
import com.work.wb.entity.Staff;
import com.work.wb.plugins.SerachSelectDialog;
import com.work.wb.util.Utils;
import com.work.wb.util.model.MechanicModel;
import com.work.wb.util.model.ProjectRoleModel;
import com.work.wb.util.model.ResultModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MechanicActivity extends AppCompatActivity {
    private String node;
    private String program;
    private String procedure;
    private TextView tvNode;
    private TextView tvProgram;
    private TextView tvProcedure;
    private TextView tvMechanic;
    private TextView tvMechanicName;
    private TextView tvBaseType;
    private TextView tvPrice;
    private EditText editTextCount;
    private EditText editTextRemark;
    private MechanicModel mechanic;
    private  Map<String,List<MechanicModel>> mechanicMap = new HashMap<>();
    private  Map<String,MechanicModel> baseTypeMap = new HashMap<>();
    private RelativeLayout rlCountTip;
    private TextView tvPriceTip;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic);
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra("title"));
        toolbar.setSubtitle("填写详细信息(4/4)");
        setSupportActionBar(toolbar);
        this.node = intent.getStringExtra("node");
        this.program = intent.getStringExtra("program");
        this.procedure = intent.getStringExtra("procedure");
        initViews();

        try {
            new Thread(mechanic_runnable).start();
            //new Thread(baseType_runnable).start();
        } catch (Exception e) {
            Log.e("YuanGuanHan", e.getMessage());
        }
    }

    public void initViews(){
        tvNode = this.findViewById(R.id.tv_node);
        tvNode.setText(this.node);
        tvProgram = this.findViewById(R.id.tv_program);
        tvProgram.setText(this.program);
        tvProcedure = this.findViewById(R.id.tv_procedure);
        tvProcedure.setText(this.procedure);
        tvMechanic = this.findViewById(R.id.tv_mechanic);
        tvMechanicName = this.findViewById(R.id.tv_mechanicName);
        tvBaseType = this.findViewById(R.id.tv_baseType);
        tvPrice = this.findViewById(R.id.tv_price);
        editTextCount = this.findViewById(R.id.editText_count);
        editTextRemark = this.findViewById(R.id.editText_remark);
        rlCountTip = this.findViewById(R.id.rl_count_tip);
        tvPriceTip = this.findViewById(R.id.tv_price_tip);
        editTextCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshPrice();
            }
        });
    }
    private MenuItem menuItem;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                menuItem=item;
                menuItem.setEnabled(false);
                saveMechanicInfo();

                break;
        }
        return true;
    }

    public void selectMechanic(View view){

        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(MechanicActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(mechanicMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择机械");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    //mechanic=mechanicMap.get(info);
                    tvMechanic.setText(info);
                    List<MechanicModel> list = mechanicMap.get(info);
                    tvMechanicName.setText(list.get(0).getmName());
                    baseTypeMap.clear();
                    mechanic = null;
                    tvBaseType.setText(R.string.tip_choice);
                    if(info.indexOf("日")<0){
                        //rlCountTip.setVisibility(View.VISIBLE);
                        tvPriceTip.setText("金额(元)=完成量*机械计件单价");
                    }else{
                        tvPriceTip.setText("金额(元)=机械计日单价");
                        //rlCountTip.setVisibility(View.GONE);
                    }
                    for (MechanicModel model:list ) {
                        baseTypeMap.put(model.getBaseTypeName(),model);
                    }
                    if(list.size()==1){
                        mechanic=list.get(0);
                        tvBaseType.setText(mechanic.getBaseTypeName());
                    }
                    refreshPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,MechanicActivity.this);
    }
    private void refreshPrice(){
        if(null!=mechanic&& mechanic.getBasePriceTypeName().indexOf("日")<0&&!"".equals(editTextCount.getText().toString())){
            int price = mechanic.getPrice()* Integer.parseInt(editTextCount.getText().toString());
            tvPrice.setText(price+"");
        }else if(null!=mechanic&& mechanic.getBasePriceTypeName().indexOf("日")>=0){
            tvPrice.setText(mechanic.getPrice()+"");
        }else {
            tvPrice.setText("");
        }
    }
    public void selectBaseType(View view){
        if(baseTypeMap.keySet().size()==0){
            Toast.makeText(getApplicationContext(), "请先选择机械",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(MechanicActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(baseTypeMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择单位");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    tvBaseType.setText(info);
                    mechanic = baseTypeMap.get(info);
                    //baseType = baseTypeMap.get(info);
                    refreshPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9,0.9,MechanicActivity.this);
    }
    private void saveMechanicInfo(){
        if(tvMechanic.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择机械",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        //}else if(tvMechanic.getText().toString().indexOf("件")>=0&&editTextCount.getText().toString().trim().equals("")){
        }else if(editTextCount.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请输入完成量",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(tvBaseType.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择单位",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }
        try {
            new Thread(save_runnable).start();
        } catch (Exception e) {
            Log.e("YuanGuanHan", e.getMessage());
        }


    }
    Runnable mechanic_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> params = new HashMap<>();
            //Staff user  = (Staff) Utils.readObject(getApplicationContext(),"currentUser",Staff.class);
            ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
            params.put("project",project.getProject().getId()+"");
            String result=null;
            try {
                result = Utils.httpGet("api/mechanics/list",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                if(result.length()>2){
                    data.putString("data",result);
                }else{
                    data.putString("data",null);
                }
                msg.setData(data);
                mechanic_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
                data.putString("data",Utils.ajaxReturn(false, result));
                msg.setData(data);
                mechanic_handler.sendMessage(msg);
            }
        }
    };
    Handler mechanic_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
                try{
                    List<MechanicModel> list = new Gson().fromJson(result,new TypeToken<List<MechanicModel>>() {}.getType());
                    if(list!=null){
                        for (MechanicModel temp:list) {
                            String key = temp.getPlateNumber()+" - "+ temp.getBasePriceTypeName();
                            if(mechanicMap.containsKey(key)){
                                mechanicMap.get(key).add(temp);
                            }else{
                                List<MechanicModel> tempList = new ArrayList<>();
                                tempList.add(temp);
                                mechanicMap.put(key,tempList);
                            }
                            // mechanicMap.put(temp.getPlateNumber()+" - "+ temp.getBasePriceTypeName(),temp);
                        }
                    }
                }catch (Exception e){
                    Utils.tip(result,getApplicationContext());
                }

            }
        }
    };
    Runnable save_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> params = new HashMap<>();

            Staff user  = (Staff) Utils.readObject(getApplicationContext(),"currentUser",Staff.class);
            ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
            params.put("node",node);
            params.put("iprocedure",procedure);
            params.put("project",project.getProject().getId()+"");
            params.put("program",program);
            params.put("mechanic",mechanic.getId()+"");
            params.put("mechanicName",mechanic.getmName());
            params.put("driverName",mechanic.getDriverName());
            params.put("plateNumber",mechanic.getPlateNumber());
            params.put("icount",editTextCount.getText().toString());
            params.put("baseType",tvBaseType.getText().toString());
            params.put("price",tvPrice.getText().toString());
            params.put("unitPrice",mechanic.getPrice()+"");
            params.put("unitPriceType",mechanic.getBasePriceTypeName());
            params.put("createDate",Utils.getCurrentTime());
            params.put("date", Utils.getCurrentDate());
            params.put("staff",user.getId()+"");
            params.put("staffName",user.getName());
            params.put("remark",editTextRemark.getText().toString());


            try {
                String result = Utils.httpPost("api/addMechanicsPrice",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                if(result.length()>2){
                    data.putString("data",result);
                }else{
                    data.putString("data",null);
                }
                msg.setData(data);
                save_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
            }
        }
    };
    Handler save_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            menuItem.setEnabled(true);
            if(null!=result){
                ResultModel model = new Gson().fromJson(result,ResultModel.class);
                if(model!=null&&model.isSuccess()){
                    Toast.makeText(getApplicationContext(), "保存成功",
                            Toast.LENGTH_SHORT).show();
                    Utils.cleanShardDataByKey(getApplicationContext(),"mechanic_today_time");
                    MechanicActivity.this.finish();
                }else{
                    Toast.makeText(getApplicationContext(), "保存出错",
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "保存出错",
                        Toast.LENGTH_SHORT).show();
            }

        }
    };
}
