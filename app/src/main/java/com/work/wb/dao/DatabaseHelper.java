package com.work.wb.dao;

import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("DatabaseHelper", "create");
		StringBuffer createNodes = new StringBuffer();
		createNodes.append("create table if not exists node(");
		createNodes.append("nd_node varchar(20),");
		createNodes.append("nd_program varchar(20),");
		createNodes.append("nd_date varchar(20)");
		createNodes.append(")");
		db.execSQL(createNodes.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 Log.e("DownloadDBVerson======",newVersion+"=========="+oldVersion);
		 /**
		if (newVersion == 2) {
			try{
				db.execSQL("create table if not exists downlist("
						+ "down_id integer primary key,"
						+ "down_title varchar(50),"
						+ "down_objId varchar(50),"
						+ "down_remark varchar(100) )");
				db.execSQL("insert into  downlist (down_id,down_title,down_objId,down_remark) "
						+"select download_id,download_name,objId,remark from downloadinfos where download_statues=3");
			}catch(Exception e){
				 Log.e("DownLOadDatabaseHepler", e.getMessage());
			}
			
		}
		if(newVersion==3){
			try{
				db.execSQL("drop table solution");
				db.execSQL("create table if not exists solution("
						+ "id bigint(20),"
						+ "title varchar(50),"
						+ "content varchar(1000),"
						+ "imgurl varchar(50),"
						+ "parentId varchar(50),"
						+ "remark varchar(200) )");
			}catch(Exception e){
				 Log.e("DownLOadDatabaseHepler", e.getMessage());
			}
		}
		if(newVersion==4){
			try{
				//db.execSQL("drop table solution");
				db.execSQL("create table if not exists msg("
	    				+ "id integer primary key,"
	    				+ "title varchar(50),"
	    				+ "content varchar(4000),"
	    				+ "create_time varchar(100),"
	    				+ "look_users varchar(500),"
	    				+ "look_num integer,"
	    				+ "author varchar(50),"
	    				+ "type integer,"
	    				+ "attach_id integer,"
	    				+ "comment varchar(500),"
	    				+ "state varchar(50) )");
				
				
			}catch(Exception e){
				 Log.e("DownLOadDatabaseHepler", e.getMessage());
			}
		}**/
	}


}
