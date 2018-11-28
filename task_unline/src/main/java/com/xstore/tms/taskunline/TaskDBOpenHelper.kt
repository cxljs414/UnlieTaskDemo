package com.xstore.tms.taskunline

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class TaskDBOpenHelper(context: Context,dbName:String) :
        SQLiteOpenHelper(context,dbName,null,1){

    override fun onCreate(db: SQLiteDatabase?) {
        var sql= "CREATE table task " +
                "(" +
                "id text primary key ," +
                "content text," +  //任务内容
                "state integer," + //状态
                "retryTime integer," + //剩余重试次数
                "priority integer," + //优先级
                "type text," + //优先级
                "serialize text)"
        db?.execSQL(sql)
        Log.i("TaskDBOpenHelper","onCreate:$sql")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}