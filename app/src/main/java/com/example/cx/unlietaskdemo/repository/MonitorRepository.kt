package com.xstore.tms.android.repository

import com.leinyo.httpclient.exception.HttpRequestEnum
import com.leinyo.httpclient.retrofit.HttpClient
import com.leinyo.httpclient.retrofit.HttpRequest
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.core.Const
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.core.net.InterceptorResponse
import com.xstore.tms.android.core.net.URLHandler
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.MonitorInfo
import com.xstore.tms.android.entity.RealTask
import com.xstore.tms.android.entity.net.request.TaskHttpRequest
import com.xstore.tms.android.utils.CryptoUtil
import com.xstore.tms.android.utils.LogUtils
import com.xstore.tms.taskunline.TaskDBManager
import java.text.MessageFormat
import java.util.*


class MonitorRepository {
    private val TAG= "MonitorRepository"
    fun uploadMonitorInfo(entity: MonitorInfo) {
        val time = System.currentTimeMillis()
        val tempStr = LoginedCarrier.sfTempStr
        val md5Str = CryptoUtil.generate(tempStr, time.toString())
        entity.sendTime = time
        entity.tempStr=tempStr
        entity.md5Str = md5Str
        val uri = MessageFormat.format(URLHandler.UPLOAD_MONITOR_INFO_SAFE, time.toString())
        val id="m_${UUID.randomUUID()}"
        val realTask = RealTask(id, Const.UNLINETASK_TYPE_MONITOR,
                TaskHttpRequest()
                        .relateUrl(uri)
                        .jsonObject(entity)
                        .buildContent()
        )
        TaskDBManager.getIntance().insert(realTask)
    }
}