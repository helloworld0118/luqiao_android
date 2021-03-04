package com.work.wb.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
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
import com.work.wb.entity.Staff;
import com.work.wb.entity.Supplier;
import com.work.wb.plugins.SerachSelectDialog;
import com.work.wb.util.Utils;
import com.work.wb.util.model.MaterialModel;
import com.work.wb.util.model.MechanicModel;
import com.work.wb.util.model.ProjectRoleModel;
import com.work.wb.util.model.ResultModel;
import com.work.wb.util.model.msg.ErrorMsgModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialActivity extends AppCompatActivity {
    private String node;
    private String program;
    private String procedure;
    private TextView tvNode;
    private TextView tvProgram;
    private TextView tvProcedure;
    private TextView tvPlateNumber;
    private TextView tvCapacity;
    private TextView tvSupplier;
    private TextView tvMaterial;


    private EditText editTextCount;
    private TextView tvBaseType;

    private TextView tvMaterialPrice;
    private TextView tvFreightPrice;

    private EditText editTextDistance;
    private EditText editTextRemark;


    private Supplier supplier;
    private Map<String,Supplier> supplierMap = new HashMap<>();

    private  Map<String,List<MechanicModel>> mechanicMap = new HashMap<>();

    private MaterialModel material;
    private  Map<String,List<MaterialModel>> materialMap = new HashMap<>();

    private MechanicModel currentMechainc;
    private  Map<String,MechanicModel> currentMechaincMap = new HashMap<>();
    private  Map<String,MaterialModel> baseTypeMap = new HashMap<>();

    private TextView tvMaterialTip;
    private TextView tvFreightTip;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra("title"));
        toolbar.setSubtitle("填写详细信息(4/4)");
        setSupportActionBar(toolbar);
        this.node = intent.getStringExtra("node");
        this.program = intent.getStringExtra("program");
        this.procedure = intent.getStringExtra("procedure");

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
                saveMaterialInfo();

                break;
        }
        return true;
    }
    private  void initViews(){
        tvNode = this.findViewById(R.id.tv_node);
        tvNode.setText(this.node);
        tvProgram = this.findViewById(R.id.tv_program);
        tvProgram.setText(this.program);
        tvProcedure = this.findViewById(R.id.tv_procedure);
        tvProcedure.setText(this.procedure);

        tvBaseType = this.findViewById(R.id.tv_baseType);
        editTextCount = this.findViewById(R.id.editText_count);
        tvCapacity = this.findViewById(R.id.tv_capacity);

        tvPlateNumber = this.findViewById(R.id.tv_plateNumber);
        tvSupplier = this.findViewById(R.id.tv_supplier);
        tvMaterial = this.findViewById(R.id.tv_material);
        editTextCount = this.findViewById(R.id.editText_count);
        tvBaseType = this.findViewById(R.id.tv_baseType);

        tvMaterialPrice = this.findViewById(R.id.tv_material_price);
        tvFreightPrice = this.findViewById(R.id.tv_freight_price);
        editTextDistance = this.findViewById(R.id.editText_distance);
        editTextRemark = this.findViewById(R.id.editText_remark);

        tvMaterialTip = this.findViewById(R.id.tv_material_tip);
        tvFreightTip = this.findViewById(R.id.tv_freight_tip);
        String  baseType = (String) Utils.readObject(getApplicationContext(),"m_baseType",String.class);
        if(null!=baseType&&!"".equals(baseType)){
            tvBaseType.setText(baseType);
            tvSupplier.setText((String)Utils.readObject(getApplicationContext(),"m_supplier_txt",String.class));
            tvMaterial.setText((String)Utils.readObject(getApplicationContext(),"m_material_txt",String.class));
            supplier = (Supplier) Utils.readObject(getApplicationContext(),"m_supplier",Supplier.class);
            material = (MaterialModel) Utils.readObject(getApplicationContext(),"m_material",MaterialModel.class);
            editTextCount.setText((String)Utils.readObject(getApplicationContext(),"m_count",String.class));
            editTextDistance.setText((String)Utils.readObject(getApplicationContext(),"m_km",String.class));
        }
        editTextCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshMaterialPrice();
            }
        });

        editTextDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshFreightPrice();
            }
        });

    }

    private void saveMaterialInfo(){
        if(tvPlateNumber.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择拉料车",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(tvSupplier.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择供应商",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(tvMaterial.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择材料",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(editTextCount.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请输入数量",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(tvBaseType.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择单位",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(editTextDistance.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请输入运输距离",
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
    private void refreshMaterialPrice(){
        if(null!=material&&!"".equals(tvBaseType.getText().toString())&&!"".equals(editTextCount.getText().toString())){
            tvMaterialPrice.setText((material.getPrice()*Integer.parseInt(editTextCount.getText().toString()))+"");
        }else {
            tvMaterialPrice.setText("");
        }
    }
    private void refreshFreightPrice(){
        if(null!=currentMechainc&&!"".equals(tvBaseType.getText().toString())&&!"".equals(editTextDistance.getText().toString())){
            tvFreightPrice.setText((currentMechainc.getPrice()*Integer.parseInt(editTextDistance.getText().toString()))+"");
        }else{
            tvFreightPrice.setText("");
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
                result = Utils.httpGet("api/skip/list",params,getApplicationContext());
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


    public void selectSupplier(View view){
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(MaterialActivity.this);
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
                    tvMaterial.setText(R.string.tip_choice);
                    tvBaseType.setText(R.string.tip_choice);
                    material=null;
                    currentMechainc=null;
                    materialMap.clear();
                    supplier = supplierMap.get(info);
                    tvBaseType.setText(R.string.tip_choice);
                    baseTypeMap.clear();

                    try {
                        new Thread(material_runnable).start();
                    } catch (Exception e) {
                        Log.e("YuanGuanHan", e.getMessage());
                    }
                    refreshMaterialPrice();
                    refreshFreightPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,MaterialActivity.this);
    }
    public void  selectSkip(View view){
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(MaterialActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(mechanicMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择拉料车");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    tvPlateNumber.setText(info);
                    tvCapacity.setText(mechanicMap.get(info).get(0).getCapacity());
                    List<MechanicModel> list = mechanicMap.get(info);
                    currentMechaincMap.clear();


                    for(MechanicModel model:list){
                        //Log.e("MaterialActivity", "insert====="+model.getBaseTypeName()+"-"+model.getBasePriceTypeName().split("/")[0]);
                        currentMechaincMap.put(model.getBaseTypeName()+"-"+model.getBasePriceTypeName().split("/")[0],model);
                    }
                    if(null!=material){
                        currentMechainc = currentMechaincMap.get(material.getMtName()+"-"+material.getBaseTypeName());
                    }
                    refreshMaterialPrice();
                    refreshFreightPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,MaterialActivity.this);
    }

    public void  selectMaterial(View view){
        if(tvSupplier.getText().toString().trim().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请先选择供应商",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(MaterialActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(materialMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择材料");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    //tvPlateNumber.setText(info.split("-")[0].trim());
                   // tvCapacity.setText(mechanicMap.get(info).get(0).getCapacity());
                    //List<MechanicModel> list = mechanicMap.get(info);
                    tvMaterial.setText(info);
                    tvBaseType.setText(R.string.tip_choice);
                    baseTypeMap.clear();
                    List<MaterialModel> list = materialMap.get(info);
                    for(MaterialModel model:list){
                        baseTypeMap.put(model.getBaseTypeName(),model);
                    }
                    if(list.size()==1){
                        material = list.get(0);
                        tvBaseType.setText(material.getBaseTypeName());
                        currentMechainc = currentMechaincMap.get(info.split("-")[0].trim()+"-"+material.getBaseTypeName());
                        if(null==currentMechainc){
                            String tipHtml="<font color='red'>无当前拉料车与材料+单位的记录<br>无法计算运输金额</font>";
                            Toast.makeText(getApplicationContext(), Html.fromHtml(tipHtml),Toast.LENGTH_LONG).show();
                        }
                    }
                    refreshMaterialPrice();
                    refreshFreightPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,MaterialActivity.this);
    }
    public void selectBaseType(View view){
        if(tvMaterial.getText().toString().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请先选择材料",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(MaterialActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(baseTypeMap.keySet());
        alert.setListData(names);

        alert.setTitle("请选择单位");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    //tvPlateNumber.setText(info.split("-")[0].trim());
                    // tvCapacity.setText(mechanicMap.get(info).get(0).getCapacity());
                    //List<MechanicModel> list = mechanicMap.get(info);
                    tvBaseType.setText(info);
                    material = baseTypeMap.get(info);
                    //Log.e("MaterialActivity", "select====="+tvMaterial.getText().toString()+"-"+info);
                    currentMechainc = currentMechaincMap.get(tvMaterial.getText().toString().split("-")[0].trim()+"-"+info);
                    if(null==currentMechainc){
                        String tipHtml="<font color='red'>无当前拉料车与材料+单位的记录<br>无法计算运输金额</font>";
                        Toast.makeText(getApplicationContext(), Html.fromHtml(tipHtml),Toast.LENGTH_LONG).show();
                    }
                    refreshMaterialPrice();
                    refreshFreightPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,MaterialActivity.this);
    }

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
                            String key = temp.getPlateNumber();
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
            try {
                String result = Utils.httpGet("api/supplier/list",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                data.putString("data",result);
                msg.setData(data);
                supplier_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
                data.putString("data",Utils.ajaxReturn(false, e.getMessage()));
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



    Runnable material_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> params = new HashMap<>();
            ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
            params.put("project",project.getProject().getId()+"");
            try {
                String result = Utils.httpGet("api/"+supplier.getId()+"/materials",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                data.putString("data",result);
                msg.setData(data);
                material_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
                data.putString("data",Utils.ajaxReturn(false, e.getMessage()));
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
                            //supplierMap.put(temp.getName(),temp);
                            String key = temp.getMtName()+" - "+temp.getMaterialSpecName();
                            if(materialMap.containsKey(key)){
                                materialMap.get(key).add(temp);
                            }else{
                                List<MaterialModel> templist = new ArrayList<>();
                                templist.add(temp);
                                materialMap.put(key,templist);
                            }
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
            if(null!=currentMechainc){
                params.put("mechainc",currentMechainc.getId()+"");
                params.put("unitFreightPrice",currentMechainc.getPrice()+"");
                params.put("plateNumber",currentMechainc.getPlateNumber());
            }else{
                for(String key :currentMechaincMap.keySet()){
                    params.put("mechainc",currentMechaincMap.get(key).getId()+"");
                    params.put("unitFreightPrice",currentMechaincMap.get(key).getPrice()+"");
                    params.put("plateNumber",currentMechaincMap.get(key).getPlateNumber());
                    break;
                }
            }
            params.put("supplier",supplier.getId()+"");
            params.put("supplierName",supplier.getName());

            params.put("material",material.getMid()+"");
            params.put("icount",editTextCount.getText().toString());
            params.put("baseType",tvBaseType.getText().toString());
            params.put("unitPrice",material.getPrice()+"");
            params.put("materialSpec",tvMaterial.getText().toString().split("-")[1].trim());
            params.put("distance",editTextDistance.getText().toString());
            params.put("materialPrice",tvMaterialPrice.getText().toString());
            params.put("materialName",material.getMtName());

            if(tvFreightPrice.getText().toString().equals("")){
                params.put("freightPrice","0");
            }else{
                params.put("freightPrice",tvFreightPrice.getText().toString());
            }

            params.put("createDate",Utils.getCurrentTime());
            params.put("date", Utils.getCurrentDate());
            params.put("staff",user.getId()+"");
            params.put("staffName",user.getName());
            params.put("remark",editTextRemark.getText().toString());


            try {
                String result = Utils.httpPost("api/addMaterialReceived",params,getApplicationContext());
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
                    Utils.saveObject(getApplicationContext(),tvBaseType.getText().toString(),"m_baseType");
                    Utils.saveObject(getApplicationContext(),tvSupplier.getText().toString(),"m_supplier_txt");
                    Utils.saveObject(getApplicationContext(),editTextCount.getText().toString(),"m_count");
                    Utils.saveObject(getApplicationContext(),editTextDistance.getText().toString(),"m_km");
                    Utils.saveObject(getApplicationContext(),supplier,"m_supplier");
                    Utils.saveObject(getApplicationContext(),tvMaterial.getText().toString(),"m_material_txt");
                    Utils.saveObject(getApplicationContext(),material,"m_material");


                    Utils.cleanShardDataByKey(getApplicationContext(),"material_today_time");
                    finish();
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
