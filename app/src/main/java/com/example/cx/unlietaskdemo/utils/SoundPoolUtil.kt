package com.xstore.tms.android.utils

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import com.xstore.tms.android.AppApplication
import com.xstore.tms.android.R

class SoundPoolUtil private constructor(val context: Context) {

    companion object {
        private var myInstance:SoundPoolUtil?=null
        fun getInstance():SoundPoolUtil{
            if(myInstance == null){
                synchronized(SoundPoolUtil::class.java){
                    if(myInstance == null){
                        myInstance = SoundPoolUtil(AppApplication.instance!!.applicationContext)
                    }
                }
            }
            return myInstance!!
        }
    }
    private val NR_OF_SIMULTANEOUS_SOUND = 7
    private val LEFT_VOLUME = 1.0f
    private val Right_VOLUME = 1.0f
    private val NO_LOOP = 0
    private val PRIORITY = 0
    private val NORMAL_PLAY_RATE = 1.0f

    private var soundPool:SoundPool?=null
    private var timeOutId:Int? = null
    private var jinbiId:Int?=null
    private var receiveId:Int? = null
    private var homeId:Int?=null

    init {
        soundPool = SoundPool(NR_OF_SIMULTANEOUS_SOUND, AudioManager.STREAM_MUSIC,0)
        timeOutId = soundPool?.load(context, R.raw.timeout,PRIORITY)
        jinbiId = soundPool?.load(context,R.raw.jinbi,PRIORITY)
        receiveId = soundPool?.load(context,R.raw.qujian,PRIORITY)
        homeId = soundPool?.load(context,R.raw.home,PRIORITY)

    }

    fun playTimeOut(){
        timeOutId.let {
            soundPool?.play(timeOutId!!,LEFT_VOLUME,Right_VOLUME,PRIORITY,NO_LOOP,NORMAL_PLAY_RATE)
        }
    }

    fun playJinbi(){
        jinbiId.let {
            soundPool?.play(jinbiId!!,LEFT_VOLUME,Right_VOLUME,PRIORITY,NO_LOOP,NORMAL_PLAY_RATE)
        }
    }

    fun playReceiveSound(){
        receiveId.let {
            soundPool?.play(receiveId!!,LEFT_VOLUME,Right_VOLUME,PRIORITY,NO_LOOP,NORMAL_PLAY_RATE)
        }
    }

    fun playHomeSound(){
        homeId.let {
            soundPool?.play(homeId!!,LEFT_VOLUME,Right_VOLUME,PRIORITY,NO_LOOP,NORMAL_PLAY_RATE)
        }
    }

    fun release(){
        soundPool?.release()
    }

}