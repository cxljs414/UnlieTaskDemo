package com.xstore.tms.android.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.os.Vibrator
import android.util.Log
import com.xstore.tms.taskunline.TaskBus
import com.xstore.tms.taskunline.TaskDBManager

class AlarmService : Service() {
    private val TAG:String = "AlarmService"
    private var timerThread:Thread?= null
    private var quit:Boolean = false
    private var pm:PowerManager?= null
    private var wakeLock:PowerManager.WakeLock?=null
    private var taskBus: TaskBus?= null
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG,"onCreate")
        timerThread = Thread{
            kotlin.run {
                while (!quit){
                    Log.i(TAG,"发送一个心跳包")
                    try {
                        Thread.sleep(1000*60)
                    } catch (e:InterruptedException) {
                        break
                    }
                    sendBroadcast(Intent("com.xstore.tms.android.clarmmsg"))
                }
            }
        }
        timerThread?.start()

        taskBus = TaskBus(this, TaskDBManager.getInstance(this))
        taskBus?.start()

        pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        //保持cpu一直运行，不管屏幕是否黑屏
        wakeLock = pm?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, AlarmService::class.java.canonicalName)
        wakeLock?.acquire(1000*60*30)
    }

    override fun onBind(intent: Intent?): IBinder {
        return AlarmBinder()
    }

    internal inner class AlarmBinder : Binder(){
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"onDestroy")
        timerThread?.interrupt()
        quit = true
    }
}