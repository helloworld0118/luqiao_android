package com.work.wb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.entity.Mechanics;
import com.work.wb.entity.Staff;
import com.work.wb.entity.Supplier;
import com.work.wb.plugins.SerachSelectDialog;
import com.work.wb.util.Utils;
import com.work.wb.util.model.MaterialModel;
import com.work.wb.util.model.MechanicModel;
import com.work.wb.util.model.ProjectRoleModel;
import com.work.wb.util.model.ResultModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

public class OilActivity extends AppCompatActivity {

    private TextView tvPlateNumber;
    private TextView tvOilType;
    private EditText editTextCount;
    private EditText editTextRemark;
    private TextView tvPrice;
    private TextView tvSupplier;
    private Mechanics mechanic;
    private Map<String,Mechanics> mechanicMap = new HashMap<>();

    private Supplier supplier;
    private Map<String,Supplier> supplierMap = new HashMap<>();

    private MaterialModel material;
    private  Map<String,List<MaterialModel>> materialMap = new HashMap<>();

    private MenuItem menuItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void initViews(){
        this.tvPlateNumber = this.findViewById(R.id.tv_plateNumber);
        this.tvOilType = this.findViewById(R.id.tv_oil_type);
        this.editTextCount = this.findViewById(R.id.editText_count);
        this.editTextRemark = this.findViewById(R.id.editText_remark);
        this.tvPrice = this.findViewById(R.id.tv_price);
        this.tvSupplier = this.findViewById(R.id.tv_supplier);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("填写加油单");
        setSupportActionBar(toolbar);
        initViews();
        try {
            new Thread(mechanic_runnable).start();
        } catch (Exception e) {
            Log.e("YuanGuanHan", e.getMessage());
        }
        try {
            new Thread(supplier_runnable).start();
        } catch (Exception e) {
            Log.e("YuanGuanHan", e.getMessage());
        }
    }

    Runnable supplier_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> params = new HashMap<>();
            //Staff user  = (Staff) Utils.readObject(getApplicationContext(),"currentUser",Staff.class);
            ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
            params.put("project",project.getProject().getId()+"");
            String result =null;
            try {
                result= Utils.httpGet("api/oil/supplier",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                data.putString("data",result);
                msg.setData(data);
                supplier_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
                data.putString("data",Utils.ajaxReturn(false, result));
                msg.setData(data);
                supplier_handler.sendMessage(msg);
            }
        }
    };
    Handler supplier_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
                try{
                    List<Supplier> list = new Gson().fromJson(result,new TypeToken<List<Supplier>>() {}.getType());
                    if(list!=null){
                        for (Supplier temp:list) {
                            supplierMap.put(temp.getName(),temp);
                        }
                    }
                }catch (Exception e){
                    Utils.tip(result,getApplicationContext());
                }
            }
        }
    };

    public void selectOilType(View view){
        if(tvSupplier.getText().toString().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请先选择供应商",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(OilActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(materialMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择加油类型");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    material=null;
                    List<MaterialModel> list =materialMap.get(info);
                    for (MaterialModel model:list){
                        if(model.getBaseTypeName().equals("升")){
                            material=model;
                        }
                    }
                    if(null==material){
                        material=materialMap.get(info).get(0);
                        material.setPrice(0);
                    }
                    tvOilType.setText(info);
                    refreshPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,OilActivity.this);
    }
    public void selectSupplier(View view){
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(OilActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(supplierMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择供应商");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    //tvPlateNumber.setText(info.split("-")[0].trim());
                    //List<MechanicModel> list = mechanicMap.get(info);
                    tvSupplier.setText(info);
                    tvOilType.setText(R.string.tip_choice);
                    material=null;
                    materialMap.clear();
                    supplier = supplierMap.get(info);
                    try {
                        new Thread(material_runnable).start();
                    } catch (Exception e) {
                        Log.e("YuanGuanHan", e.getMessage());
                    }
                    refreshPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,OilActivity.this);
    }
    private void refreshPrice(){
        if(null!=material&&!editTextCount.getText().toString().trim().equals("")){
            int price = material.getPrice() * Integer.parseInt(editTextCount.getText().toString());
            tvPrice.setText(price+"");
        }else {
            tvPrice.setText("");
        }

    }
    public void selectMechanic(View view){
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(OilActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(mechanicMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择车辆");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    mechanic=mechanicMap.get(info);
                    tvPlateNumber.setText(info);
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,OilActivity.this);
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
            String result = null;
            try {
                result = Utils.httpGet("api/mechanics/all",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                data.putString("data",result);
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
                    List<Mechanics> list = new Gson().fromJson(result,new TypeToken<List<Mechanics>>() {}.getType());
                    if(list!=null){
                        for (Mechanics temp:list) {
                            if(temp.getmType()==0) {
                                mechanicMap.put(temp.getPlateNumber() + " - 小车", temp);
                            }else if(temp.getmType()==1){
                                mechanicMap.put(temp.getPlateNumber() + " - "+temp.getmName(), temp);
                            }else if(temp.getmType()==2){
                                mechanicMap.put(temp.getPlateNumber() + " - 拉料车", temp);
                            }

                        }
                    }
                }catch (Exception e){
                    Utils.tip(result,getApplicationContext());
                }
            }
        }
    };
    Runnable material_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> params = new HashMap<>();
            ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
            params.put("project",project.getProject().getId()+"");
            String result=null;
            try {
                result = Utils.httpGet("api/"+supplier.getId()+"/oils",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                data.putString("data",result);
                msg.setData(data);
                material_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
                data.putString("data",Utils.ajaxReturn(false, result));
                msg.setData(data);
                material_handler.sendMessage(msg);
            }
        }
    };
    Handler material_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
               try{
                   List<MaterialModel> list = new Gson().fromJson(result,new TypeToken<List<MaterialModel>>() {}.getType());
                   if(list!=null){
                       for (MaterialModel temp:list) {
                           String key = temp.getMtName()+"-"+temp.getMaterialSpecName();
                          // materialMap.put(key,temp);
                           if(materialMap.containsKey(key)){
                               materialMap.get(key).add(temp);
                           }else{
                               List<MaterialModel> model = new ArrayList<>();
                               model.add(temp);
                               materialMap.put(key,model);
                           }
                       }
                   }
               }catch (Exception e){
                   Utils.tip(result,getApplicationContext());
               }

            }
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                menuItem=item;
                menuItem.setEnabled(false);
                saveOilInfo();

                break;
        }
        return true;
    }
    private void saveOilInfo(){
        if(tvPlateNumber.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择车牌号",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(tvSupplier.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择供应商",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(tvOilType.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择加油类型",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(editTextCount.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请输入加油数量",
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

    Runnable save_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> params = new HashMap<>();

            Staff user  = (Staff) Utils.readObject(getApplicationContext(),"currentUser",Staff.class);
            ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));

            params.put("project",project.getProject().getId()+"");

            params.put("supplier",supplier.getId()+"");
            params.put("supplierName",supplier.getName());
            params.put("plateNumber",mechanic.getPlateNumber());
            params.put("mechanicName",tvPlateNumber.getText().toString().split("-")[1].trim());
            params.put("mechanic",mechanic.getId()+"");
            params.put("driverName",mechanic.getDriverName());
            params.put("icount",editTextCount.getText().toString());

            params.put("unitPrice",material.getPrice()+"");


            params.put("price",tvPrice.getText().toString());
            params.put("materialName",material.getMtName());
            params.put("oilType",tvOilType.getText().toString());


            params.put("createDate",Utils.getCurrentTime());
            params.put("date", Utils.getCurrentDate());
            params.put("staff",user.getId()+"");
            params.put("staffName",user.getName());
            params.put("remark",editTextRemark.getText().toString());

            Log.e("YuanGuanHan", "icount========"+editTextCount.getText().toString());
            try {
                String result = Utils.httpPost("api/oil/add",params,getApplicationContext());
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
                    Utils.cleanShardDataByKey(getApplicationContext(),"oil_today_time");
                    OilActivity.this.finish();
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
