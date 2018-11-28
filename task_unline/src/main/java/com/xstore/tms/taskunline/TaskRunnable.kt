package com.xstore.tms.taskunline

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Process
import android.util.Log
import io.reactivex.Observable
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TimeUnit

class TaskRunnable(var context: Context,
                   var dbManager: TaskDBManager,
                   var taskQueue:PriorityBlockingQueue<Task>) : Thread(){
    private val TAG= "TaskRunnable"
    private var quit:Boolean = false
    private var currentTask:Task ?= null
    @SuppressLint("CheckResult")
    override fun run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
        Log.d(TAG,"Start running")
        while (true){
            try {
                if(quit)return
                currentTask = taskQueue.take()
                Log.d(TAG,"Take a task:${currentTask?.getId()}")
            }catch (e:InterruptedException){
                Log.e(TAG,"Take a task:${currentTask?.getId()} error",e)
            }

            if (quit)return
            if(currentTask == null)continue
            currentTask?.decrementRetryTime()
            currentTask?.decrementPriority()
            //checkAndWaitingNetwork()
            if (quit)return
            try {
                val sleep = 10-currentTask?.getRetryTime()!!
                Log.d(TAG + "-" + id + "-" + Thread.currentThread().id, Thread.currentThread().toString() + "->worker sleep time: " + sleep + " seconds.")
                Thread.sleep(sleep * TimeUnit.SECONDS.toMillis(1))
            } catch (e: InterruptedException) {
                //ignored
            }

            Observable.just(currentTask)
                    .map {
                        it.excute()
                    }
                    .subscribe({
                        Log.i(TAG,"excute success ${currentTask?.getId()}")
                        dbManager.deleteTask(currentTask?.getId()!!)
                        val intent= Intent("xstore_unlinetask_success")
                        intent.putExtra("type","${currentTask?.getType()}")
                        intent.putExtra("dono","${currentTask?.getId()}")
                        context.sendBroadcast(intent)
                    },{
                        Log.i(TAG,"excute failure ${currentTask?.getId()} retryTime=${currentTask?.getRetryTime()} state=${currentTask?.getState()}")
                        var needNotify = false
                        if(currentTask?.getRetryTime() == 0){
                            currentTask?.setState(Task.STATE_FAILURE)
                            needNotify = true
                        }else{
                            currentTask?.setState(Task.STATE_RETRY)
                        }
                        val updateResult= dbManager.update(currentTask!!)
                        if(needNotify){
                            var intent= Intent("xstore_unlinetask_failure")
                            intent.putExtra("type","${currentTask?.getType()}")
                            intent.putExtra("dono","${currentTask?.getId()}")
                            context.sendBroadcast(intent)
                        }
                        Log.i(TAG,"update task ${currentTask?.getId()} result= $updateResult")
                    })
        }

    }

    private fun checkAndWaitingNetwork() {
        while (!isNetworkConnected(context)){
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(15))
            }catch (e:InterruptedException){
                if (quit)return
            }
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
        var cm= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo= cm.activeNetworkInfo
        return networkInfo!= null && networkInfo.isConnected
    }

    fun quit() {
        quit = true
        interrupt()
    }
}