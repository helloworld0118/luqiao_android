package com.work.wb.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.work.wb.R;
import com.work.wb.entity.BasePriceType;
import com.work.wb.entity.BaseType;
import com.work.wb.entity.Department;
import com.work.wb.entity.Staff;
import com.work.wb.plugins.SerachSelectDialog;
import com.work.wb.util.Utils;
import com.work.wb.util.model.ProjectRoleModel;
import com.work.wb.util.model.ResultModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

public class ExpenseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTextPresenter;
    private EditText editTextPrice;

    private TextView tvBillType;
    private TextView tvPriceType;
    private TextView tvDepartment;
    private EditText editTextRemark;
    private TextView tvDate;
    private Map<String,Integer> departmentMap = new HashMap<>();
    private Map<String,Integer> billTypeMap = new HashMap<>();
    private Map<String,Integer> priceTypeMap = new HashMap<>();

    private int billType;
    private int priceType;
    private int department;
    private String date;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initViews(){
        this.editTextPresenter=this.findViewById(R.id.editText_presenter);
        this.editTextPrice=this.findViewById(R.id.editText_price);

        this.tvBillType=this.findViewById(R.id.tv_bill_type);
        this.tvPriceType=this.findViewById(R.id.tv_price_type);
        this.tvDepartment=this.findViewById(R.id.tv_department);
        this.editTextRemark=this.findViewById(R.id.editText_remark);
        this.tvDate = this.findViewById(R.id.tv_date);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("填写报销单");
        setSupportActionBar(toolbar);
        initViews();
        try {
            new Thread(baseType_runnable).start();
        } catch (Exception e) {
            Log.e("YuanGuanHan", e.getMessage());
        }
        try {
            new Thread(priceType_runnable).start();
        } catch (Exception e) {
            Log.e("YuanGuanHan", e.getMessage());
        }
        try {
            new Thread(department_runnable).start();
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
                saveExpenseInfo();

                break;
        }
        return true;
    }
    private void saveExpenseInfo(){
        if(editTextPresenter.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请输入报销人",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(editTextPrice.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请输入报销金额",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(tvDate.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择报销日期",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(tvBillType.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择票据类型",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(tvPriceType.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择费用类型",
                    Toast.LENGTH_SHORT).show();
            menuItem.setEnabled(true);
            return;
        }else if(tvDepartment.getText().equals("请选择")){
            Toast.makeText(getApplicationContext(), "请选择部门",
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
            params.put("project", project.getProject().getId()+"");
            params.put("person", editTextPresenter.getText().toString());
            params.put("priceType", priceType+"");
            params.put("billType", billType+"");
            params.put("department", department+"");
            params.put("price", editTextPrice.getText().toString()+"");
            params.put("createDate",Utils.getCurrentTime());
            params.put("date", date);
            params.put("presenterName",user.getName());
            params.put("presenter",user.getId()+"");
            params.put("remark",editTextRemark.getText().toString());


            try {
                String result = Utils.httpPost("api/expense/add",params,getApplicationContext());
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
                    Utils.cleanShardDataByKey(getApplicationContext(),"expense_today_time");
                    ExpenseActivity.this.finish();
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

    public void selectDepartment(View view){
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(ExpenseActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(departmentMap.keySet());
        alert.setListData(names);
        alert.setTitle("请选择部门");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    department=departmentMap.get(info);
                    tvDepartment.setText(info);
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,ExpenseActivity.this);
    }
    public void selectBillType(View view){
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(ExpenseActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(billTypeMap.keySet());
        alert.setListData(names);
        alert.setTitle("请票据类型");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    billType= billTypeMap.get(info);
                    tvBillType.setText(info);
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,ExpenseActivity.this);
    }
    public void selectPriceType(View view){
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(ExpenseActivity.this);
        List<String> names = new ArrayList<String>();
        names.addAll(priceTypeMap.keySet());
        alert.setListData(names);
        alert.setTitle("请费用类型");

        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                if(null!=info&&!"".equals(info)){
                    priceType = priceTypeMap.get(info);
                    tvPriceType.setText(info);
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        mDialog.setDialogWindowAttr(0.9,0.9,ExpenseActivity.this);
    }

    public void datePicker(View view){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ExpenseActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setAccentColor("#303F9F");
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        //String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        String month= (monthOfYear+1)<10?"0"+(monthOfYear+1):(monthOfYear+1)+"";
        String day = dayOfMonth<10?"0"+dayOfMonth:dayOfMonth+"";
        date = year+"-"+month+"-"+day;
        tvDate.setText(date);

    }


    Runnable baseType_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> params = new HashMap<>();
            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
            String result=null;
            try {
                result = Utils.httpGet("api/basetype/4",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                data.putString("data",result);
                msg.setData(data);
                baseType_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
                data.putString("data",Utils.ajaxReturn(false, result));
                msg.setData(data);
                baseType_handler.sendMessage(msg);
            }
        }
    };
    Handler baseType_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
               try{
                   List<BaseType> list = new Gson().fromJson(result,new TypeToken<List<BaseType>>() {}.getType());
                   if(list!=null){
                       for (BaseType temp:list) {
                           billTypeMap.put(temp.getName(),temp.getId());
                       }
                   }

               }catch (Exception e){
                   Utils.tip(result,getApplicationContext());

               }
            }
        }
    };


    Runnable priceType_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> params = new HashMap<>();
            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
            String result =null;
            try {
                result = Utils.httpGet("api/priceType/4",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                data.putString("data",result);
                msg.setData(data);
                priceType_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
                data.putString("data",Utils.ajaxReturn(false, result));
                msg.setData(data);
                priceType_handler.sendMessage(msg);
            }
        }
    };
    Handler priceType_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
                try{
                    List<BasePriceType> list = new Gson().fromJson(result,new TypeToken<List<BasePriceType>>() {}.getType());
                    if(list!=null){
                        for (BasePriceType temp:list) {
                            priceTypeMap.put(temp.getName(),temp.getId());
                        }
                    }
                }catch (Exception e){
                    Utils.tip(result,getApplicationContext());
                }

            }
        }
    };

    Runnable department_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> params = new HashMap<>();
            params.put("code",(String) Utils.readObject(getApplicationContext(),"code",String.class));
            String result = null;
            try {
                result = Utils.httpGet("api/department/list",params,getApplicationContext());
                Log.e("YuanGuanHan", "result========"+result);
                data.putString("data",result);
                msg.setData(data);
                department_handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("YuanGuanHan", "error========"+e.getMessage());
                data.putString("data",Utils.ajaxReturn(false, result));
                msg.setData(data);
                department_handler.sendMessage(msg);
            }
        }
    };
    Handler department_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
               try{
                   List<Department> list = new Gson().fromJson(result,new TypeToken<List<Department>>() {}.getType());
                   if(list!=null){
                       for (Department temp:list) {
                           departmentMap.put(temp.getName(),temp.getId());
                       }
                   }
               }catch (Exception e){
                   Utils.tip(result,getApplicationContext());
               }

            }
        }
    };
}
