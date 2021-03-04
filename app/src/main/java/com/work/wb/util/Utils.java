package com.work.wb.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.work.wb.R;

import com.work.wb.activity.MainActivity;
import com.work.wb.util.model.msg.ErrorMsgModel;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utils {
	private static SimpleDateFormat timeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	private static String HOST="http://182.92.185.140:8080/luqiao/";
	//private static String HOST="http://www.nmgdhjt.com/luqiao/";

	private final static String FILENAME = "data";



     public static String getTempPath(){
    	 String temppath=Environment.getExternalStorageDirectory().getPath()+"/Android/data/.cache/";
    	 return temppath;
     }

	 public static String getCurrentTime(){
		 return timeFormat.format(new Date());
	 }
	 public static String getCurrentDate(){
		return dateFormat.format(new Date());
	 }

	public static Long getCompareDate(String time){
		return System.currentTimeMillis()-Long.parseLong(time);
	}
	 public static boolean isUpdateInfo(String updateinfo,boolean now, Context context){
		 SharedPreferences sharedPreferences = context.getSharedPreferences("share",context.MODE_PRIVATE);  
		 boolean isFirstRun = sharedPreferences.getBoolean(updateinfo, now);  
		 return isFirstRun;
	 }
	 public static void updateUptInfo(String updateinfo,boolean now, Context context){
		 SharedPreferences sharedPreferences = context.getSharedPreferences("share",context.MODE_PRIVATE);
		 Editor editor = sharedPreferences.edit();  
		 editor.putBoolean(updateinfo, now);  
		 editor.commit();  
	 }
	 public static void updateAppVersion(String version, Context context){
		 SharedPreferences sharedPreferences = context.getSharedPreferences("share",context.MODE_PRIVATE);  
		 Editor editor = sharedPreferences.edit();  
		 editor.putString("AppVersion", version); 
		 editor.commit();  
	 }
	 public static String getAppVersionLocal(Context context){
		 SharedPreferences sharedPreferences = context.getSharedPreferences("share",context.MODE_PRIVATE);  
		 return sharedPreferences.getString("AppVersion", "1.4.2");
	 }



	/** 
    * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
    */ 
	public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  

	private static final String[][] MIME_MapTable={ 
            {".3gp",    "video/3gpp"}, 
            {".apk",    "application/vnd.android.package-archive"}, 
            {".asf",    "video/x-ms-asf"}, 
            {".avi",    "video/x-msvideo"}, 
            {".bin",    "application/octet-stream"}, 
            {".bmp",    "image/bmp"}, 
            {".c",  "text/plain"}, 
            {".class",  "application/octet-stream"}, 
            {".conf",   "text/plain"}, 
            {".cpp",    "text/plain"}, 
            {".doc",    "application/msword"}, 
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, 
            {".xls",    "application/vnd.ms-excel"},  
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, 
            {".exe",    "application/octet-stream"}, 
            {".gif",    "image/gif"}, 
            {".gtar",   "application/x-gtar"}, 
            {".gz", "application/x-gzip"}, 
            {".h",  "text/plain"}, 
            {".htm",    "text/html"}, 
            {".html",   "text/html"}, 
            {".jar",    "application/java-archive"}, 
            {".java",   "text/plain"}, 
            {".jpeg",   "image/jpeg"}, 
            {".jpg",    "image/jpeg"}, 
            {".js", "application/x-javascript"}, 
            {".log",    "text/plain"}, 
            {".m3u",    "audio/x-mpegurl"}, 
            {".m4a",    "audio/mp4a-latm"}, 
            {".m4b",    "audio/mp4a-latm"}, 
            {".m4p",    "audio/mp4a-latm"}, 
            {".m4u",    "video/vnd.mpegurl"}, 
            {".m4v",    "video/x-m4v"},  
            {".mov",    "video/quicktime"}, 
            {".mp2",    "audio/x-mpeg"}, 
            {".mp3",    "audio/x-mpeg"}, 
            {".mp4",    "video/mp4"}, 
            {".mpc",    "application/vnd.mpohun.certificate"},        
            {".mpe",    "video/mpeg"},   
            {".mpeg",   "video/mpeg"},   
            {".mpg",    "video/mpeg"},   
            {".mpg4",   "video/mp4"},    
            {".mpga",   "audio/mpeg"}, 
            {".msg",    "application/vnd.ms-outlook"}, 
            {".ogg",    "audio/ogg"}, 
            {".pdf",    "application/pdf"}, 
            {".png",    "image/png"}, 
            {".pps",    "application/vnd.ms-powerpoint"}, 
            {".ppt",    "application/vnd.ms-powerpoint"}, 
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, 
            {".prop",   "text/plain"}, 
            {".rc", "text/plain"}, 
            {".rmvb",   "audio/x-pn-realaudio"}, 
            {".rtf",    "application/rtf"}, 
            {".sh", "text/plain"}, 
            {".tar",    "application/x-tar"},    
            {".tgz",    "application/x-compressed"},  
            {".txt",    "text/plain"}, 
            {".wav",    "audio/x-wav"}, 
            {".wma",    "audio/x-ms-wma"}, 
            {".wmv",    "audio/x-ms-wmv"}, 
            {".wps",    "application/vnd.ms-works"}, 
            {".xml",    "text/plain"}, 
            {".z",  "application/x-compress"}, 
            {".zip",    "application/x-zip-compressed"}, 
            {"",        "*/*"}   
        }; 
 
	private static String getMIMEType(File file) { 
	     
	    String type="*/*"; 
	    String fName = file.getName(); 
	    int dotIndex = fName.lastIndexOf("."); 
	    if(dotIndex < 0){ 
	        return type; 
	    } 
	    String end=fName.substring(dotIndex,fName.length()).toLowerCase(); 
	    if(end=="")return type; 
	    for(int i=0;i<MIME_MapTable.length;i++){ 
	        if(end.equals(MIME_MapTable[i][0])) 
	            type = MIME_MapTable[i][1]; 
	    }        
	    return type; 
	} 

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (connectivity != null) {  
            NetworkInfo info = connectivity.getActiveNetworkInfo();  
            if (info != null && info.isConnected())   
            {  
                // 当前网络是连接的  
                if (info.getState() == NetworkInfo.State.CONNECTED)   
                {  
                    // 当前所连接的网络可用  
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
	public static String getCurrentVersionInfo(Context context){  
        try {  
             PackageManager pm = context.getPackageManager();  
             PackageInfo info =pm.getPackageInfo(context.getPackageName(), 0);  
             return  info.versionName;
        } catch (Exception e) {  
            e.printStackTrace();  
            return "版本号未知";  
        }  
    }

	public static final MediaType JSON
			= MediaType.parse("application/json; charset=utf-8");


	public static String ajaxReturn(boolean success, String msg) {
		return "{\"success\":" + success + ",\"msg\":" + msg + "}";
	}
	public static void tip(String result,Context context){
		try{
			ErrorMsgModel model = new Gson().fromJson(result, ErrorMsgModel.class);
			Toast.makeText(context, model.getMsg(),
					Toast.LENGTH_SHORT).show();
		}catch (Exception ingonre){
			Toast.makeText(context, "网络好像不给力",
					Toast.LENGTH_SHORT).show();
		}
	}

	public static String httpPost(String url, Map<String,String> params,Context context) throws IOException {
		if(!NetWorkUtils.isNetworkConnected(context)){
			return ajaxReturn(false,"当前没有网络");
		}
		OkHttpClient client = new OkHttpClient();
		FormBody.Builder build = new FormBody.Builder();
		for (String key:params.keySet()) {
			build.add(key,params.get(key));
		}
		Request.Builder requestBuilder = new Request.Builder();
		try{
			String token = (String) readObject(context,"token",String.class);
			String code = (String) readObject(context,"code",String.class);
			String SALT = MD5Util.encrypt(code+token);
			String sign = SignHelper.getSignature(params,token,SALT);
			requestBuilder.addHeader("token",token);
			build.add("sign",sign);
		}catch (Exception e){

		}
		RequestBody body =build.build();
		Log.e("Utils", "Post:"+HOST+url);

		requestBuilder.url(HOST+url)
				.post(body);
		Request request =requestBuilder.build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	public static String httpGet(String url,Map<String,String> params,Context context) throws IOException {
		if(!NetWorkUtils.isNetworkConnected(context)){
			return ajaxReturn(false,"当前没有网络");
		}
		OkHttpClient client = new OkHttpClient();

		StringBuilder baseString = new StringBuilder();
		Map<String, String> sortedParams = new TreeMap<>(params);
		Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
		for (Map.Entry<String, String> param : entrys) {
			if(param.getValue()!=null && !"".equals(param.getKey().trim()) && !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue().trim())) {
				baseString.append(param.getKey().trim()).append("=").append(param.getValue().trim()).append("&");
			}
		}
		if(baseString.length() > 0 ) {
			baseString.deleteCharAt(baseString.length() - 1);
		}

		Request.Builder requestBuilder= new Request.Builder();

		String token = (String) readObject(context,"token",String.class);
		String code = (String) readObject(context,"code",String.class);
		String SALT = MD5Util.encrypt(code+token);
		String sign = SignHelper.getSignature(params,token,SALT);
		requestBuilder.addHeader("token",token);
		Log.e("Utils","URL==="+HOST+url+"?"+baseString.toString()+"&sign="+sign);
		requestBuilder.url(HOST+url+"?"+baseString.toString()+"&sign="+sign);
		Request request = requestBuilder.build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public static void cleanShardData(Context context){
		SharedPreferences sharedata = context.getSharedPreferences(FILENAME, 0);
		SharedPreferences.Editor editor = sharedata.edit();
		editor.clear();
		editor.commit();
	}
	public static void cleanShardDataByKey(Context context,String key){
		SharedPreferences sharedata = context.getSharedPreferences(FILENAME, 0);
		SharedPreferences.Editor editor = sharedata.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * desc:保存对象
	 * @param context
	 * @param KEY
	 * @param obj 要保存的对象，只能保存实现了serializable的对象
	 * modified:
	 */
	public static void saveObject(Context context,Object obj,String KEY){
		try {

			SharedPreferences.Editor sharedata = context.getSharedPreferences(FILENAME, 0).edit();
			sharedata.putString(KEY, new Gson().toJson(obj));
			sharedata.commit();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Utils","error"+e.getMessage());
		}
	}

	public static Object readObject(Context context,String KEY, Type type){
		try {
			SharedPreferences sharedata = context.getSharedPreferences(FILENAME, 0);
			if (sharedata.contains(KEY)) {
				String string = sharedata.getString(KEY, "");
				if(TextUtils.isEmpty(string)){
					return null;
				}else{
					return  new Gson().fromJson(string,type);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//所有异常返回null
		return null;

	}

}
