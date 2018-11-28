package com.xstore.tms.android.utils

import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.MonitorInfo
import com.xstore.tms.android.repository.MonitorRepository
import org.json.JSONObject

object MonitorUtil {
    private val repository = MonitorRepository()

    fun addMonitor(flag:Int,doNo:String,content:String,pushVersion:Long ?= null){
        var entity = MonitorInfo()
        entity.carrierPin = LoginedCarrier.carrierPin
        var brandJson= JSONObject()
        brandJson.put("deviceBrand",DeviceUtil.getDeviceBrand())
        brandJson.put("model",DeviceUtil.getSystemModel())
        brandJson.put("mac",DeviceUtil.getMacAddress())
        entity.deviceInfo = brandJson.toString()
        entity.storeId = LoginedCarrier.storeId!!.toLong()
        entity.flag = flag
        entity.doNo = doNo
        entity.content = content
        entity.pushVersion = pushVersion
        repository.uploadMonitorInfo(entity)
    }

    fun addMonitor(flag:Int,doNo:String,content:String){
        addMonitor(flag,doNo,content,null)
    }

    fun addMonitor(flag:Int,content:String,pushVersion:Long){
        addMonitor(flag,"",content,pushVersion)
    }
}