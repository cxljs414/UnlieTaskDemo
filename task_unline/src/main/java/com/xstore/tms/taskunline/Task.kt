package com.xstore.tms.taskunline

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.util.*

abstract class Task:Comparable<Task>{

    companion object {
        val STATE_UNEXCUTE = 0 //未执行
        val STATE_RETRY = 1 //执行失败需重试
        val STATE_FAILURE = 2 //执行失败，重试次数已用完

        fun gson():Gson{
            var gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(Task::class.java,AbstractTaskAdapter())
            return gsonBuilder.create()
        }

        fun toJson(task: Task): String {
            val jobs = arrayOf(task)
            return gson().toJson(jobs)
        }

        fun fromJson(json: String): Task {
            return gson().fromJson(json, Array<Task>::class.java)[0]
        }
    }

    private val MAX_RETRY_TIME = 10
    private var id= UUID.randomUUID().toString()
    //优先级，每重试一次-1，越小优先级越低
    private var priority:Int = MAX_RETRY_TIME
    //还剩重试次数，每重试一次-1，到0时重试次数用完
    private var retryTime:Int = MAX_RETRY_TIME
    //任务状态
    private var state:Int = STATE_UNEXCUTE
    //任务内容
    private var content:String ?= null
    //任务类型
    private var type:String?=null




    abstract fun excute(): Task

    override fun compareTo(other: Task): Int {
        return getPriority() - other.getPriority()
    }

    fun getPriority():Int{
        return priority
    }

    fun getId():String{
        return id
    }

    fun decrementRetryTime() {
        retryTime--
    }

    fun decrementPriority() {
        priority--
    }


    fun getRetryTime(): Int {
        return retryTime
    }

    fun setState(state: Int) {
        this.state = state
    }

    fun getState(): Int {
        return state
    }

    fun getContent(): String? {
        return content
    }

    fun setContent(content: String) {
        this.content = content
    }

    fun setId(id:String){
        this.id = id
    }

    fun setType(type:String){
        this.type = type
    }

    fun getType():String?{
        return this.type
    }

}