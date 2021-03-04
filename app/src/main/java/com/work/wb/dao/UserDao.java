package com.work.wb.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
	
	public SQLiteDatabase mSQLite;
	
	public UserDao(DatabaseHelper helper) {
		mSQLite = helper.getWritableDatabase();
	}
	
	public boolean insertInfo(String name,String date,String sdpath,String organization,String remark){
		/*
		ContentValues values = new ContentValues();
		values.put("user_name", name);
		values.put("organization", organization);
		values.put("login_time", date);
		values.put("sdpath", sdpath);
		values.put("remark", remark);
		long result = mSQLite.insert("userinfo", null, values);
		if (result == -1) {
			return false;
		}*/
		return true;
	}
	
//	public UserInfo getUserInfo() {
//
//		Cursor cursor = mSQLite.query("userinfo", null, null, null, null, null, null);
//		UserInfo model = null;
//		if (cursor.moveToNext()) {
//			model=new UserInfo();
//			model.setId(cursor.getInt(cursor.getColumnIndex("user_id")));
//			model.setName(cursor.getString(cursor.getColumnIndex("user_name")));
//			model.setTime(cursor.getString(cursor.getColumnIndex("login_time")));
//			model.setSdpath(cursor.getString(cursor.getColumnIndex("sdpath")));
//			model.setOrganization(cursor.getString(cursor.getColumnIndex("organization")));
//			model.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
//		}
//		return model;
//	}
	
	public boolean updateInfoById(int id,String columnName,String columnValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
		long result = mSQLite.update("userinfo", values, "user_name=?", new String[]{id+""});
		return true;
	}
	
	public boolean updateInfoById(int id,String columnName,int columnValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
		long result = mSQLite.update("userinfo", values, "user_name=?", new String[]{id+""});
		return true;
	}
	
	public boolean deleteInfo() {
		mSQLite.delete("userinfo", null, null);
		return true;
	}

}
