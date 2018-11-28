package com.xstore.tms.android.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Vibrator
import android.util.Log
import com.xstore.tms.android.AppApplication
import com.xstore.tms.android.core.event.EventDispatchManager
import java.util.concurrent.ConcurrentHashMap

/**
 * 业务逻辑梳理：
 * 刷新带配送列表 添加到待提醒列表 time>=5
        time >5   直接添加
        time = 5
            已响列表有  -> 不添加已响
            已响列表没有-> 添加已响 tipcount+1      （为了防止再同一分钟内多次刷新）

    每隔一分钟检测待提醒列表遍历把time-1
        time=0  -> 从待提醒列表删除，从已响列表删除
        time=5  ->
            已响列表有  -> 不添加已响
            已响列表没有-> 添加已响 tipcount+1

    判断tipcount>0,是则提醒，并tipcount=0

    妥投 orderNo 从待提醒列表删除，已响列表也删
    time=5 & tipcount>0 -> tipcount-1

 */
class TimeOutUtil private constructor():EventDispatchManager.SubscriberListener{

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instances:TimeOutUtil?= null
        fun getInstance():TimeOutUtil{
            if(instances == null){
                synchronized(TimeOutUtil::class.java){
                    if(instances == null){
                        instances = TimeOutUtil()
                    }
                }
            }
            return instances!!
        }

    }
    private val TAG= "TimeOutUtil"
    private var mContext:Context?= null
    private var map: ConcurrentHashMap<String, Int> = ConcurrentHashMap() //线程安全
    private var soundedSet:MutableSet<String> = HashSet()//已响列表
    private var soundPoolUtil:SoundPoolUtil?=null
    private var vibrator:Vibrator?=null
    private var downTime= 3  //取件单和宅配订单每隔3分钟检测一次
    private var tipCount= 0
    private var homeMsgCount = 0
    private var receiveMsgCount = 0

    init {
        EventDispatchManager.getInstance().register(this)
        mContext = AppApplication.instance?.applicationContext
        mContext.let {
            soundPoolUtil = SoundPoolUtil.getInstance()
            vibrator = mContext?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

    }

    fun addItem(id:String,time:Int){
        if(time-1>=5){
            map.put(id,time-1)
            if(time-1 == 5){
                if(!soundedSet.contains(id)){
                    soundedSet.add(id)
                    tipCount++
                }
            }
            Log.i("alarm","add $id : ${map[id]} ")
        }
    }

    override fun onEventMain(event: EventDispatchManager.Event?) {
        when(event?.eventType){
            EventDispatchManager.EventType.EVENT_HINT_HOME_DELIVERY -> homeMsgCount++
            EventDispatchManager.EventType.EVENT_HINT_RECEVING -> receiveMsgCount++
            EventDispatchManager.EventType.EVENT_HINT_ALARM -> {
                Log.i(TAG, "downtime = $downTime  tipcount=$tipCount homeMsgCount= $homeMsgCount  receiveMsgCount=$receiveMsgCount")
                downCount()
                Log.i("alarm", "tipcount=$tipCount")
                if(tipCount>0){
                    tipCount=0
                    soundPoolUtil?.playTimeOut()
                    vibrator?.vibrate(1000)

                }

                if(downTime > 0){
                    downTime--
                }else{
                    downTime = 2
                    if(homeMsgCount>0){
                        homeMsgCount=0
                        vibrator?.vibrate(1000)
                        soundPoolUtil?.playHomeSound()
                    }
                    if(receiveMsgCount > 0){
                        receiveMsgCount = 0
                        vibrator?.vibrate(1000)
                        soundPoolUtil?.playReceiveSound()
                    }
                }
            }
        }
    }

    private fun downCount() {
        map.mapKeys {
            if(it.value == 0){
                map.remove(it.key)
                soundedSet.remove(it.key)
            }else {
                var count= it.value-1
                if(count == 5){
                    if(!soundedSet.contains(it.key)){
                        soundedSet.add(it.key)
                        tipCount++
                    }
                }
                Log.i("alarm","${it.key}:${it.value-1}")
                map[it.key]= count
            }
        }
    }

    fun exit(){
        EventDispatchManager.getInstance().unRegister(this)
    }

    /**
     * 清除待响项的同时，将待响项加入已响列表，防止列表刷新再次提醒
     */
    fun remove(needRemoveItemDoNo: String) {
        if(map.containsKey(needRemoveItemDoNo)){
            if(map[needRemoveItemDoNo] == 5 && tipCount>0){
                tipCount--
            }
            map.remove(needRemoveItemDoNo)
        }
        soundedSet.add(needRemoveItemDoNo)
    }

    fun clear() {
        map.clear()
        tipCount = 0
    }

    fun clearHomeCount() {
        homeMsgCount = 0
    }

    fun clearReceiveCount(){
        receiveMsgCount = 0
    }

    fun addReceiveCount() {
        receiveMsgCount++
    }

    fun addHomeCount() {
        homeMsgCount++
    }

    fun clearAll() {
        map.clear()
        tipCount = 0
        receiveMsgCount = 0
        homeMsgCount = 0
    }

    fun addSoundedSet(id:String){
        soundedSet.add(id)
    }

    fun removeSounded(dono: String) {
        if(soundedSet.contains(dono)){
            soundedSet.remove(dono)
        }
    }
}