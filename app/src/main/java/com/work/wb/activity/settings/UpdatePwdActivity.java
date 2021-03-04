package com.work.wb.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.work.wb.R;
import com.work.wb.activity.LobarActivity;
import com.work.wb.activity.LoginActivity;
import com.work.wb.adapter.DataDetailAdapter;
import com.work.wb.entity.Role;
import com.work.wb.entity.Staff;
import com.work.wb.util.MD5Util;
import com.work.wb.util.Utils;
import com.work.wb.util.model.DataDetailModel;
import com.work.wb.util.model.ProjectRoleModel;
import com.work.wb.util.model.ResultModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdatePwdActivity extends AppCompatActivity {
    private TextView tvTitle;
    private EditText editOldPwd;
    private EditText editNewPwd;
    private EditText editConfPwd;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                updatePwdInfo();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tvTitle= findViewById(R.id.toolbar_title);

        tvTitle.setText("修改密码");
        editOldPwd = findViewById(R.id.editText_oldPwd);
        editNewPwd = findViewById(R.id.editText_newPwd);
        editConfPwd = findViewById(R.id.editText_confPwd);
    }
    private void updatePwdInfo(){
        Staff user  = (Staff) Utils.readObject(getApplicationContext(),"currentUser",Staff.class);

        if(editOldPwd.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "请输入原密码",
                    Toast.LENGTH_SHORT).show();
            return;
        }else if(editNewPwd.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "请输入新密码",
                    Toast.LENGTH_SHORT).show();
            return;
        }else if(editConfPwd.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "请确认新密码",
                    Toast.LENGTH_SHORT).show();
            return;
        }else if(!user.getPassword().equals(MD5Util.encrypt(editOldPwd.getText().toString().trim()))){
            Log.e("UpdatePwd",editOldPwd.getText().toString().trim());
            Log.e("UpdatePwd",user.getPassword()+"======"+MD5Util.encrypt(editOldPwd.getText().toString().trim()));
            Toast.makeText(getApplicationContext(), "原密码输入错误",
                    Toast.LENGTH_SHORT).show();
            return;
        }else if(editNewPwd.getText().toString().length()<6 ||editNewPwd.getText().toString().length()>35){
            Toast.makeText(getApplicationContext(), "新密码长度请在6~35位之间",
                    Toast.LENGTH_SHORT).show();
            return;
        }else if(!editNewPwd.getText().toString().equals(editConfPwd.getText().toString())){
            Toast.makeText(getApplicationContext(), "新密码两次输入不一致",
                    Toast.LENGTH_SHORT).show();
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
            params.put("oldPassword",editOldPwd.getText().toString());
            params.put("newPassword",editNewPwd.getText().toString());
            try {
                String result = Utils.httpPost("api/update/"+user.getId(),params,getApplicationContext());
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
            if(null!=result){
                ResultModel model = new Gson().fromJson(result,ResultModel.class);
                if(model!=null&&model.isSuccess()){
                    Toast.makeText(getApplicationContext(), "修改成功,请重新登录",
                            Toast.LENGTH_SHORT).show();
                    Utils.cleanShardDataByKey(getApplicationContext(),"currentUser");
                    Utils.cleanShardDataByKey(getApplicationContext(),"token");
                    Intent intent =  new Intent(UpdatePwdActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "修改出错",
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "修改出错",
                        Toast.LENGTH_SHORT).show();
            }

        }
    };
}
