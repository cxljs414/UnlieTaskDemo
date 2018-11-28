package com.xstore.tms.android.entity

import com.xstore.tms.android.entity.net.request.TaskHttpRequest
import com.xstore.tms.android.utils.LogUtils
import com.xstore.tms.taskunline.Task
import org.json.JSONException


class RealTask(id:String?,type:String?,content:String?) : Task() {
    private final val TAG:String = "RealTask"
    init {
        if(id?.isNotEmpty()!!){
            setId(id)
        }
        if(type?.isNotBlank()!!){
            setType(type)
        }
        if(content?.isNotEmpty()!!){
            setContent(content)
        }
    }
    override fun excute(): Task {
        LogUtils.i(TAG,"excute task ${getId()}")
        var content= getContent()
        if(content.isNullOrEmpty())throw IllegalArgumentException("not found content")
        val taskRequest= TaskHttpRequest()
        try {
            taskRequest.content(content)
            LogUtils.i(TAG,"json parse content success")
        }catch (e: JSONException){
            LogUtils.i(TAG,"json parse content error")
            throw IllegalArgumentException("json parse content error")
        }
        val call = taskRequest.create()
        val response = call.execute()
        LogUtils.i(TAG,"response=${response.body().toString()}")
        if (!response.isSuccessful) {
            LogUtils.i(TAG,"response=${response.body().toString()}")
            throw RuntimeException()
        }
        System.out.println("excute result: $content")
        return this
    }
}