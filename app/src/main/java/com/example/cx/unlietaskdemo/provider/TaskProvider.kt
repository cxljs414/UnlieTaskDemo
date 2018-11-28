package com.xstore.tms.android.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.xstore.tms.taskunline.TaskDBOpenHelper

class TaskProvider : ContentProvider() {
    private val PROVIDER_NAME = "com.xstore.tms.Task"
    private val DATABASE_NAME = "taskunline"
    private val TABLENAME = "task"
    private var db: SQLiteDatabase? = null
    private val MATCHER: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        MATCHER.addURI(PROVIDER_NAME, "task", 1)
    }

    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = TaskDBOpenHelper(context!!, DATABASE_NAME)
        db = dbHelper.writableDatabase
        return db != null

    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        if(MATCHER.match(uri) == 1){
            return db?.query(TABLENAME,projection,selection,selectionArgs,sortOrder,null,null)
        }
        return null
    }


    override fun getType(uri: Uri): String? {
        return ""
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri? {
        if(MATCHER.match(uri) == 1){
            var result= db?.insert(TABLENAME,null,values)
            if(result!= null){
                return ContentUris.withAppendedId(uri,result)
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if(MATCHER.match(uri) == 1){
            var result= db?.delete(TABLENAME,selection,selectionArgs)
            if(result != null){
                return result
            }
        }
        return -1
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        if(MATCHER.match(uri) == 1){
            var result= db?.update(TABLENAME,values,selection,selectionArgs)
            if(result != null){
                return result
            }
        }
        return -1
    }

}
