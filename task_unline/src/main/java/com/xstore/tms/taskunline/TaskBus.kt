package com.xstore.tms.taskunline

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import io.reactivex.Observable
import java.util.concurrent.PriorityBlockingQueue

class TaskBus(var mContext: Context,val dbManager:TaskDBManager) {

    private val TAG:String= "TaskBus"
    private val taskQueue= PriorityBlockingQueue<Task>()
    private var cpuCount = Runtime.getRuntime().availableProcessors()
    private var countDownTimer:CountDownTimer? = null
    private var taskRunnables:MutableList<TaskRunnable> = ArrayList()
    /**
     * 开始执行
     * 初始化数据库
     * 每隔3分钟取出数据库中未执行的任务
     */
    fun start(){
        stop()
        for (i in 0..(cpuCount+1)){
            var taskRunnable = TaskRunnable(mContext,dbManager,taskQueue)
            taskRunnables.add(taskRunnable)
            taskRunnable.start()
        }
        if(countDownTimer == null){
            countDownTimer = object:CountDownTimer(3000*60*1000,1000*60){
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG,"reaload tasks")
                    loadUnFinishTask()
                }

                override fun onFinish() {
                    countDownTimer?.start()
                }

            }
        }
        countDownTimer?.start()
    }

    /**
     * 从数据库加载未执行的任务和失败重试的任务
     */
    @SuppressLint("CheckResult")
    private fun loadUnFinishTask() {
        dbManager.loadTaskByState("state in (${Task.STATE_UNEXCUTE},${Task.STATE_RETRY})",null)
                .toObservable()
                .flatMap<Task>{Observable.fromIterable(it)}
                .map { taskQueue.add(it) }
                .subscribe({
                    Log.d(TAG,"loadAllUnfinishedTasks count:$it")
                },{
                    Log.e(TAG,"loadAllUnfinishedTasks",it)
                })
    }


    /**
     * 停止所有任务
     */
    fun stop() {
        countDownTimer?.cancel()
        taskQueue.clear()
        taskRunnables.forEach {
            it.quit()
        }
    }

}