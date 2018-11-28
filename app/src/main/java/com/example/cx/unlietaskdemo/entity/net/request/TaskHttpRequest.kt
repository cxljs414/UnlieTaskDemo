package com.xstore.tms.android.entity.net.request


import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.JSONObject.toJavaObject

import com.leinyo.httpclient.exception.HttpErrorEnum
import com.leinyo.httpclient.exception.HttpException
import com.leinyo.httpclient.retrofit.HttpClient

import java.util.HashMap

import okhttp3.ResponseBody
import org.json.JSONException
import retrofit2.Call

class TaskHttpRequest {
    private var url: String? = null
    private var headers: MutableMap<String, String>? = HashMap()
    private var params: MutableMap<String, String>? = null
    private var jsonObject: Any? = null

    fun relateUrl(url: String): TaskHttpRequest {
        this.url = url
        return this
    }

    fun jsonObject(jsonObject: Any): TaskHttpRequest {
        this.jsonObject = jsonObject
        return this
    }

    fun headers(headers: MutableMap<String, String>): TaskHttpRequest {
        this.headers = headers
        return this
    }

    fun params(params: MutableMap<String, String>): TaskHttpRequest {
        this.params = params
        return this
    }

    fun content(content: String?): TaskHttpRequest {
        try {
            val json = JSON.parseObject(content)
            if(json.containsKey("url")){
                url= json["url"].toString()
            }

            if (json.containsKey("head")) {
                val jso = JSONObject.parseObject(json.getString("head"))
                jso.keys.forEach {
                    headers?.put(it,jso[it].toString())
                }
            }
            if (json.containsKey("param")) {
                val jso = JSONObject.parseObject(json.getString("param"))
                jso.keys.forEach {
                    params?.put(it,jso[it].toString())
                }
            }
            if (json.containsKey("object")) {
                val jso = json.getString("object")
                jsonObject = JSON.parseObject(jso,Any::class.java)
            }
        } catch (e: Exception) {
            throw JSONException("json parse error")
        }
        return this
    }

    fun create(): Call<ResponseBody> {
        if (TextUtils.isEmpty(url)) {
            throw HttpErrorEnum.API_URL_NOT_NULL.toException()
        }

        if (headers != null && !headers!!.isEmpty()) {
            checkMap(headers!!)
        }

        if (params != null && !params!!.isEmpty()) {
            checkMap(params!!)
        }

        var call: Call<ResponseBody>? = null
        call = if (jsonObject != null && params == null) {
            HttpClient.getInstance().apiService.post(headers, url, jsonObject)
        } else if (params != null) {
            HttpClient.getInstance().apiService.post(headers, url, params)
        } else {
            HttpClient.getInstance().apiService.post(headers, url)
        }

        if (call == null) {
            throw HttpException(HttpErrorEnum.API_REQUEST_PARAM_ERR.value)
        }
        return call
    }

    fun buildContent():String{
        var headerStr = ""
        if (headers?.isNotEmpty()!!) {
            headerStr = JSON.toJSONString(headers)
        }
        var paramStr = ""
        if (params != null && params?.isNotEmpty()!!) {
            paramStr = JSON.toJSONString(params)
        }
        var objectStr = ""
        if (jsonObject != null) {
            objectStr = JSON.toJSONString(jsonObject)
        }
        val json = JSONObject()
        if(headerStr.isNotEmpty()){
            json["head"] = headerStr
        }
        if(paramStr.isNotEmpty()){
            json["param"] = paramStr
        }
        if(objectStr.isNotEmpty()){
            json["object"] = objectStr
        }
        json["url"] = url
        return json.toJSONString()
    }

    private fun checkMap(map: MutableMap<String, String>) {
        val iterator = map.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (TextUtils.isEmpty(entry.key)) {
                iterator.remove()
            }
        }
    }

}
