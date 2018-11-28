package com.xstore.tms.taskunline

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import java.lang.RuntimeException
import java.util.concurrent.Callable

class TaskDBManager private constructor(var context: Context) {

    companion object {
        private var instance: TaskDBManager? = null

        fun getIntance(): TaskDBManager {
            return instance!!
        }

        fun getInstance(context: Context):TaskDBManager{
            if (instance == null) {
                synchronized(TaskDBManager::class.java) {
                    if (instance == null) {
                        instance = TaskDBManager(context)
                    }
                }
            }
            return instance!!
        }

        fun init(context: Context){
            if (instance == null) {
                synchronized(TaskDBManager::class.java) {
                    if (instance == null) {
                        instance = TaskDBManager(context)
                    }
                }
            }
        }
    }

    private val TAG:String = "TaskDBManager"
    private val uri= Uri.parse("content://com.xstore.tms.Task/task")
    private val contentResolver = context.contentResolver

    fun loadTaskByState(selection:String?,states: Array<out String>?):Single<List<Task>>{
        return Single.using(
                Callable<Cursor> {
                    contentResolver.query(
                            uri,
                            arrayOf("id","content","state","retryTime","priority","type","serialize"),
                            selection,
                            states,
                            null)
                },
                Function <Cursor,SingleSource<List<Task>>>{
                    var tasks:MutableList<Task> = ArrayList()
                    while (it.moveToNext()){
                        try {
                            val task= Task.fromJson(it.getString(it.getColumnIndex("serialize")))
                            tasks.add(task)
                            Log.i(TAG,"load a task -> ${task.getId()}")
                        }catch (e:Exception){
                            Log.e("loadAllTask","fromJson ${e.message}",e)
                        }
                    }
                    return@Function Single.just(tasks)
                },
                Consumer <Cursor>{
                    it.close()
                })
    }

    fun loadTaskIds(selection:String?,selectionArgs: Array<out String>?):Single<List<String>>{
        return Single.using(
                Callable<Cursor> {
                    contentResolver.query(
                            uri,
                            arrayOf("id","content","state","retryTime","type","priority"),
                            selection,
                            selectionArgs,
                            null)
                },
                Function <Cursor,SingleSource<List<String>>>{
                    var ids:MutableList<String> = ArrayList()
                    while (it.moveToNext()){
                        try {
                            ids.add(it.getString(it.getColumnIndex("id")))
                        }catch (e:Exception){
                            Log.e("loadAllTask","fromJson ${e.message}",e)
                        }
                    }
                    return@Function Single.just(ids)
                },
                Consumer <Cursor>{
                    it.close()
                })
    }

    fun loadExcuteTaskIds():Single<List<String>>{
        return loadTaskIds("state in (${Task.STATE_UNEXCUTE},${Task.STATE_RETRY})",null)
    }

    fun loadFailureTaskIds():Single<List<String>>{
        return loadTaskIds("state in (${Task.STATE_FAILURE})",null)
    }

    fun queryTaskById(id:String):Single<List<Task>>{
        return Single.using(
                Callable<Cursor> {
                    contentResolver.query(uri,
                            arrayOf("id","content","state","retryTime","priority","type","serialize"),
                            "id=?",
                            arrayOf(id),
                            null)
                },
                Function <Cursor,SingleSource<List<Task>>>{
                    var tasks:MutableList<Task> = ArrayList()
                    if (it.moveToNext()){
                        try {
                            tasks.add(Task.fromJson(it.getString(it.getColumnIndex("serialize"))))
                            Log.i(TAG,"load a task -> ${tasks[0].getId()}")
                        }catch (e:Exception){
                            Log.e("loadAllTask","fromJson ${e.message}",e)
                        }
                    }
                    return@Function Single.just(tasks)
                },
                Consumer <Cursor>{
                    it.close()
                })
    }


    fun deleteTask(id: String) {
        var result= contentResolver.delete(uri,"id=?", arrayOf("$id"))
        Log.d(TAG,"delete task:$id result:$result")
    }

    fun update(task: Task) {
        var value= ContentValues()
        value.put("id",task.getId())
        value.put("priority",task.getPriority())
        value.put("retryTime",task.getRetryTime())
        value.put("state",task.getState())
        value.put("content",task.getContent())
        value.put("type",task.getType())
        value.put("serialize",Task.toJson(task))
        val result= contentResolver.update(uri,value,"id=?", arrayOf(task.getId()))
        Log.d(TAG,"update task:${task.getId()} result:$result")
    }

    @SuppressLint("CheckResult", "Recycle")
    fun insert(task: Task) {
        //先查询是否已经有了id，有就update
        Single.using(
                Callable<Cursor> {
                    contentResolver.query(uri,
                            arrayOf("id"),
                            "id=?",
                            arrayOf(task.getId()),
                            null)
                },
                Function <Cursor,SingleSource<Boolean>>{
                    return@Function Single.just(it.count>0)
                },
                Consumer <Cursor>{
                    it.close()
                })
                .subscribe(Consumer {
                    val value= ContentValues()
                    value.put("id",task.getId())
                    value.put("priority",task.getPriority())
                    value.put("retryTime",task.getRetryTime())
                    value.put("state",task.getState())
                    value.put("content",task.getContent())
                    value.put("type",task.getType())
                    value.put("serialize",Task.toJson(task))
                    if(it){
                        contentResolver.update(uri,value,"id=?", arrayOf(task.getId()))
                    }else{
                        contentResolver.insert(uri,value)
                        Log.d(TAG,"insert task:${task.getId()}")
                    }
                })
    }

}