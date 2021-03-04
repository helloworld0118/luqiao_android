package com.work.wb.activity;



import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.util.Utils;
import com.work.wb.util.model.ProjectRoleModel;
import com.work.wb.util.model.msg.ErrorMsgModel;
import com.work.wb.util.model.msg.UserMsgModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity {
	private EditText mcode;
	private EditText username;
	private EditText password;
	private Button submit;
	private Dialog loginDialog;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//Staff user  = (Staff) Utils.readObject(getApplicationContext(),"currentUser",Staff.class);
		//List<ProjectRoleModel> list = (List<ProjectRoleModel>) Utils.readObject(getApplicationContext(),"projectRoles", new TypeToken<List<ProjectRoleModel>>() {}.getType());
		String token = (String) Utils.readObject(getApplicationContext(),"token",String.class);
		if(null==token){
			init();
		}else{
			ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getApplicationContext(),"currentProject",ProjectRoleModel.class);
			if(null!=project){
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			}else {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, ProjectActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			}

		}
	}
	private void init(){
		mcode= this.findViewById(R.id.code);
		username=this.findViewById(R.id.username);
		password=this.findViewById(R.id.password);
		submit=this.findViewById(R.id.btn_login);
		try{
			String tempUserName=(String)Utils.readObject(getApplicationContext(),"login_username",String.class);
			String tempPassWord=(String)Utils.readObject(getApplicationContext(),"login_password",String.class);
			String tempCode=(String)Utils.readObject(getApplicationContext(),"code",String.class);
			mcode.setText(tempCode);
			username.setText(tempUserName);
			password.setText(tempPassWord);
		}catch (Exception e){

		}
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!Utils.isNetworkAvailable(getApplicationContext())){
					Toast.makeText(getApplicationContext(), "请确认网络连接", Toast.LENGTH_LONG).show();
					return;
				}
				String code=mcode.getText().toString();
				String name=username.getText().toString();
				String pwd=password.getText().toString();
                //Log.e("HelloWorldActivity","______________onCreate execute______________");
                if(null==code||"".equals(code)){
                    Toast.makeText(getApplicationContext(), "请输入企业编码", Toast.LENGTH_LONG).show();
                    return;
                }
				if(null==name||"".equals(name)){
					Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_LONG).show();
					return;
				}
				if(null==pwd||"".equals(pwd)){
					Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_LONG).show();
					return;
				}
				View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog, null);
				TextView txt=(TextView) view.findViewById(R.id.diglogtitle);
				txt.setText("正在登录");
				loginDialog = new Dialog(LoginActivity.this, R.style.selectorDialog);
				loginDialog.setContentView(view);
                loginDialog.setCanceledOnTouchOutside(false);
                loginDialog.show();
				try {
					new Thread(runnable).start();
				} catch (Exception e) {
                    Log.e("HelloWorldActivity", e.getMessage());
				}
			}
		});
	}

	Runnable runnable = new Runnable(){
		@Override
		public void run() {

			Message msg = new Message();
			Bundle data = new Bundle();
            String code=mcode.getText().toString();
            String name=username.getText().toString();
            String pwd=password.getText().toString();
			try {

                Map<String,String> params = new HashMap<>( );
                params.put("code",code );
                params.put("mobile",name );
                params.put("password",pwd );
                String result = Utils.httpPost("api/login",params,getApplicationContext());
				Log.e("YuanGuanHan", "result========"+result);
                data.putString("result",result);
			} catch (Exception e) {
				// TODO: handle exception
				ErrorMsgModel model =new ErrorMsgModel();
				model.setSuccess(false);
				model.setMsg(e.getMessage());
				data.putString("result",new Gson().toJson(model));
			}
			msg.setData(data);
			handler.sendMessage(msg);
		}
	};

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
            if(null!=result&&!"".equals(result)) {

				try{
					UserMsgModel model = new Gson().fromJson(result, new TypeToken<UserMsgModel>() {}.getType());
					Date nowDate = new Date();
					Utils.saveObject(getApplicationContext(),username.getText().toString(),"login_username");
					Utils.saveObject(getApplicationContext(),password.getText().toString(),"login_password");
					Utils.saveObject(getApplicationContext(),mcode.getText().toString(),"code");
					Utils.saveObject(getApplicationContext(),model.getMsg().getToken(),"token");
					Utils.saveObject(getApplicationContext(),model.getMsg().getStaff(),"currentUser");
					Utils.saveObject(getApplicationContext(),nowDate.getTime(),"lastLoginTime");
					List<ProjectRoleModel> list= model.getMsg().getProjectRoles();
					Log.e("HelloWorldActivity","ProjectRoleModel size===="+list.size());
					Utils.saveObject(getApplicationContext(),list,"projectRoles");


					if (loginDialog.isShowing()) {
						loginDialog.dismiss();
					}

					if(list.size()==1){
						Utils.saveObject(getApplicationContext(),list.get(0),"currentProject");
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
					}else{
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, ProjectActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
					}
				}catch (Exception e){
					ErrorMsgModel model = new Gson().fromJson(result, ErrorMsgModel.class);
					Toast.makeText(getApplicationContext(), model.getMsg(),
							Toast.LENGTH_SHORT).show();
					loginDialog.dismiss();
				}

            }else {
				String result_msg = data.getString("msg");
				Toast.makeText(getApplicationContext(), result_msg,
						Toast.LENGTH_SHORT).show();
				loginDialog.dismiss();
			}
		}
	};
}
