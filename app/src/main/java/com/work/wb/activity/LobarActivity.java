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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.entity.BaseType;
import com.work.wb.entity.Foreman;
import com.work.wb.entity.LaborCost;
import com.work.wb.entity.Project;
import com.work.wb.entity.Staff;
import com.work.wb.plugins.SerachSelectDialog;
import com.work.wb.util.Utils;
import com.work.wb.util.model.NodeModel;
import com.work.wb.util.model.ProjectRoleModel;
import com.work.wb.util.model.ResultModel;
import com.work.wb.util.model.WorkChargeModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

public class LobarActivity extends AppCompatActivity {

    private String node;
    private String program;
    private String procedure;
    private TextView tvNode;
    private TextView tvProgram;
    private TextView tvProcedure;
    private TextView tvForeman;
    private TextView tvWorker;
    private TextView tvBaseType;
    private TextView tvPrice;
    private EditText editTextCount;
    private EditText editTextWorkNums;
    private EditText editTextRemark;
    private TextView tvPriceTip;
    //private List<Foreman> foremans;
   // private List<String> foremanNames= new ArrayList<>();
    private Foreman foreman;
    private WorkChargeModel worker;
    //private BaseType baseType;
    private Map<String,Foreman> foremanMap = new HashMap<>();
    private Map<String,List<WorkChargeModel>> workerMap = new HashMap<>();
    private  Map<String,WorkChargeModel> baseTypeMap = new HashMap<>();
    private LinearLayout rlCountTip;
   // private List<String> baseTypes =  new ArrayList<>();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobar);
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra("title"));
        toolbar.setSubtitle("填写详细信息(4/4)");
        setSupportActionBar(toolbar);
        this.node = intent.getStringExtra("node");
        this.program = intent.getStringExtra("program");
        this.procedure = intent.getStringExtra("procedure");

        /**
         *   intent.putExtra("program",program);
         intent.putExtra("node",node);
         intent.putExtra("procedure",textView.getText().toString());
         intent.putExtra("title",title);
         */
        initViews();

        try {
            new Thread(foreman_runnable).start();
           // new Thread(baseType_runnable).start();
        } catch (Exception e) {
            Log.e("YuanGuanHan", e.getMessage());
        }

    }
    Runnable foreman_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> params = new HashMap<>();
            //Staff user  = (Staff) Utils.readObject(getApplicationContext(),"currentUser",Staff.class);
            ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
            params.put("project",project.getProject().getId()+"");
            String result= null;
            try {
                result = Utils.httpGet("api/foreman/list",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                data.putString("data",result);
                msg.setData(data);
                foreman_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
                data.putString("data",Utils.ajaxReturn(false, result));
                msg.setData(data);
                foreman_handler.sendMessage(msg);
            }
        }
    };
    Handler foreman_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
               try{
                   List<Foreman> foremans = new Gson().fromJson(result,new TypeToken<List<Foreman>>() {}.getType());
                   if(foremans!=null){
                       for (Foreman temp:foremans) {
                           foremanMap.put(temp.getName(),temp);
                       }
                   }
               }catch (Exception e){
                   Utils.tip(result,getApplicationContext());
               }

            }
        }
    };

    Runnable worker_runnable = new Runnable(){
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
                result= Utils.httpGet("api/foreman/"+foreman.getId(),params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                data.putString("data",result);

                msg.setData(data);
                worker_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
                data.putString("data",Utils.ajaxReturn(false, result));
                msg.setData(data);
                worker_handler.sendMessage(msg);
            }
        }
    };
    Handler worker_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
               try{
                   List<WorkChargeModel> workers = new Gson().fromJson(result,new TypeToken<List<WorkChargeModel>>() {}.getType());
                   if(workers!=null){
                       for (WorkChargeModel temp:workers) {
                           String key = temp.getWorkerTypeName()+" - "+temp.getBasePriceTypeName();
                           if(workerMap.containsKey(key)){
                               workerMap.get(key).add(temp);
                           }else{
                               List<WorkChargeModel> list = new ArrayList<>();
                               list.add(temp);
                               workerMap.put(key,list);
                           }

                       }
                   }
               }catch (Exception e){
                   Utils.tip(result,getApplicationContext());
               }

            }
        }
    };


//    Runnable baseType_runnable = new Runnable(){
//        @Override
//        public void run() {
//            Message msg = new Message();
//            Bundle data = new Bundle();
//            Map<String,String> params = new HashMap<>();
//            //Staff user  = (Staff) Utils.readObject(getApplicationContext(),"currentUser",Staff.class);
//           // ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
//            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
//            //params.put("project",project.getProject().getId()+"");
//            try {
//                String result = Utils.httpGet("api/basetype/5",params,getApplicationContext());
//                Log.e("YuanGuanHan", "result========"+result);
//                if(result.length()>2){
//                    data.putString("data",result);
//                }else{
//                    data.putString("data",null);
//                }
//                msg.setData(data);
//                baseType_handler.sendMessage(msg);
//            } catch (IOException e) {
//                Log.e("YuanGuanHan", "error========"+e.getMessage());
//            }
//        }
//    };
//    Handler baseType_handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Bundle bundle = msg.getData();
//            String result = bundle.getString("data");
//            if(null!=result){
//                List<BaseType> list = new Gson().fromJson(result,new TypeToken<List<BaseType>>() {}.getType());
//                if(list!=null){
//                    for (BaseType temp:list) {
//                        baseTypeMap.put(temp.getName(),temp);
//                    }
//                }
//
//            }
//        }
//    };
    public void initViews(){
        tvNode = this.findViewById(R.id.tv_node);
        tvNode.setText(this.node);
        tvProgram = this.findViewById(R.id.tv_program);
        tvProgram.setText(this.program);
        tvProcedure = this.findViewById(R.id.tv_procedure);
        tvProcedure.setText(this.procedure);
        tvForeman = this.findViewById(R.id.tv_foreman);
        tvWorker = this.findViewById(R.id.tv_worker);
        tvBaseType = this.findViewById(R.id.tv_baseType);
        tvPrice = this.findViewById(R.id.tv_price);
        tvPriceTip = this.findViewById(R.id.tv_price_tip);
        editTextCount = this.findViewById(R.id.editText_count);
        editTextWorkNums  = this.findViewById(R.id.editText_workNums);
        editTextRemark = this.findViewById(R.id.editText_remark);
        rlCountTip = this.findViewById(R.id.rl_count_tip);
        editTextWorkNums.addTextChangedListener(new TextWatcher() {
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
    private void refreshPrice(){
        if(null!=worker&& worker.getBasePriceTypeName().indexOf("日")<0&&!"".equals(editTextCount.getText().toString())){
            int price = worker.getPrice() * Integer.parseInt(editTextCount.getText().toString());
            tvPrice.setText(price+"");
        }else if(null!=worker&& worker.getBasePriceTypeName().indexOf("日")>=0&&!"".equals(editTextWorkNums.getText().toString())){
            int price = worker.getPrice() * Integer.parseInt(editTextWorkNums.getText().toString());
            tvPrice.setText(price+"");
        }else {
            tvPrice.setText("");
        }
    }
    public void selectForeman(View view){

        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(LobarActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(foremanMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择工头");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    tvForeman.setText(info);
                    tvWorker.setText("请选择");
                    workerMap.clear();
                    worker = null;
                    foreman = foremanMap.get(info);
                    try {
                        new Thread(worker_runnable).start();
                    } catch (Exception e) {
                        Log.e("YuanGuanHan", e.getMessage());
                    }
                    refreshPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9,0.9,LobarActivity.this);
    }


    public void selectWorkers(View view){
        if(null==foreman){
            Toast.makeText(getApplicationContext(), "请先选择工头",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(LobarActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(workerMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择工种");
        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    tvWorker.setText(info);
                    baseTypeMap.clear();
                    tvBaseType.setText(R.string.tip_choice);
                    if(info.indexOf("日")<0){
                        //rlCountTip.setVisibility(View.VISIBLE);
                        //editTextWorkNums.setHint("可选");
                        tvPriceTip.setText("金额(元)=完成量*工种计件单价");
                    }else{
                        tvPriceTip.setText("金额(元)=出工人数*工种计日单价");
                        //rlCountTip.setVisibility(View.GONE);
                        //editTextWorkNums.setHint("必选");
                    }
                    List<WorkChargeModel> list = workerMap.get(info);

                    for (WorkChargeModel model:list) {
                        baseTypeMap.put(model.getBaseTypeName(),model);
                    }
                    if(list.size()==1){
                        worker = list.get(0);
                        tvBaseType.setText(worker.getBaseTypeName());
                    }
                    refreshPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9,0.9,LobarActivity.this);
    }
    public void selectBaseType(View view){
        if(null==foreman){
            Toast.makeText(getApplicationContext(), "请先选择工头",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(baseTypeMap.keySet().size()==0){
            Toast.makeText(getApplicationContext(), "请先选择工种",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(LobarActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(baseTypeMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择单位");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    tvBaseType.setText(info);
                    worker = baseTypeMap.get(info);
                    refreshPrice();
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9,0.9,LobarActivity.this);
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
                saveLobarInfo();

                break;
        }
        return true;
    }
    private void saveLobarInfo(){
         if(tvForeman.getText().equals("请选择")){
             Toast.makeText(getApplicationContext(), "请选择工头",
                     Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
         }else if(tvWorker.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择工种",
                    Toast.LENGTH_SHORT).show();
             menuItem.setEnabled(true);
            return;
        // }else if(editTextWorkNums.getText().toString().trim().equals("")&& !editTextWorkNums.getHint().toString().equals("可选")){
         }else if(editTextWorkNums.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请输入出工人数",
                    Toast.LENGTH_SHORT).show();
             menuItem.setEnabled(true);
            return;
         //}else if(tvWorker.getText().toString().indexOf("件")>=0&&editTextCount.getText().toString().trim().equals("")){
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
            params.put("foreman",foreman.getId()+"");
            params.put("foremanName",foreman.getName());
            params.put("workerType",worker.getBaseTypeName());
            params.put("worker",worker.getId()+"");
            params.put("icount",editTextCount.getText().toString());
            params.put("baseType",tvBaseType.getText().toString());
            params.put("price",tvPrice.getText().toString());
            params.put("unitPrice",worker.getPrice()+"");
            params.put("unitPriceType",worker.getBasePriceTypeName());
            if(editTextWorkNums.getText().toString().equals("")){
                params.put("workerNums",0+"");
            }else{
                params.put("workerNums",editTextWorkNums.getText().toString());
            }

            params.put("createDate",Utils.getCurrentTime());
            params.put("date", Utils.getCurrentDate());
            params.put("staff",user.getId()+"");
            params.put("staffName",user.getName());
            params.put("remark",editTextRemark.getText().toString());


            try {
                String result = Utils.httpPost("api/addLabor",params,getApplicationContext());
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
                    Utils.cleanShardDataByKey(getApplicationContext(),"labor_today_time");
                    LobarActivity.this.finish();
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
