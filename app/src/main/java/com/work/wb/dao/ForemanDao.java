package com.work.wb.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bing.wang on 2018/1/21.
 */

public class ForemanDao {
    public SQLiteDatabase mSQLite;

    public ForemanDao(DatabaseHelper helper) {
        mSQLite = helper.getWritableDatabase();
    }
    public boolean insertInfo(){
        return true;
    }
}
