package com.xstore.tms.android.core

import android.os.Handler
import android.os.Message
import com.leinyo.httpclient.retrofit.HttpClient
import com.leinyo.httpclient.retrofit.HttpRequest
import com.leinyo.httpclient.retrofit.NetworkResponse
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.utils.LogUtils
import java.lang.reflect.Method
import java.util.concurrent.Executors

object WebRequestWrap : NetworkStringResponse {

    private val getRequestCode: Method = HttpRequest::class.java.getDeclaredMethod("getRequestCode")

    init {
        getRequestCode.isAccessible = true
    }

    private var sorted = false
    private val threadLocal = ThreadLocal<Box>()

    val accessChain = ArrayList<WebRequestWrapFilter>()

    private val responseCache = hashMapOf<Int, NetworkResponse>()

    fun asyncNetWork(httpRequest: HttpRequest, responseListener: NetworkResponse) {
        // - 同步排序， 注： 暂时不实现自动排序，先参照业务按顺序将链插入集合中。
//        if (!sorted) {
//            synchronized(accessChain) {
//                if(!sorted) {
//                    accessChain.sortBy { it.squeueNumber }
//                    sorted = true
//                }
//            }
//        }
        val requestCode = getRequestCode.invoke(httpRequest) as Int
        val chain = RequestChainImpl()
        chain.request(httpRequest, requestCode, responseListener)
    }

    /**
     * 服务器返回成功回调 自己反序列化
     *
     * @param response 网络请求返信息
     */
    override fun onDataReady(response: String?, requestCode: Int) {
//        if (!accessChain.isEmpty()) {
//            for (i in accessChain.size - 1 .. 0) {
//                accessChain[i].responseBack(requestCode, true)
//            }
//        }
        val box = threadLocal.get()
        box.requestCode = requestCode
        box.response = response
        BoxCache.setData(box.hashCode(), box)
        mHandler.sendMessage(Message().apply { arg1 = box.hashCode()  })
    }

    /**
     * 调用失败回调
     */
    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
//        if (!accessChain.isEmpty()) {
//            for (i in accessChain.size - 1 .. 0){
//                accessChain[i].responseBack(requestCode, false)
//            }
//        }
         val box = threadLocal.get()
        box.requestCode = requestCode
        box.responseCode = responseCode
        box.message = message
        BoxCache.setData(box.hashCode(), box)
        mHandler.sendMessage(Message().apply { arg1 = box.hashCode()  })
    }

    private object BoxCache {
        private val map = HashMap<Int, Box>()
        @Synchronized
        fun setData(key: Int, box: Box) = map.put(key, box)

        @Synchronized
        fun getData(key: Int) : Box? = map[key]

        @Synchronized
        fun removeData(key: Int) = map.remove(key)
    }

    /**
     * 回调消息
     */
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var box = BoxCache.getData(msg.arg1)
            BoxCache.removeData(msg.arg1)
            val chain = ResponseChainImpl()
            chain.response(box?.requestCode!!, box?.responseCode, box?.response, box?.message, box?.networkResponse)
        }
    }

    interface WebRequestWrapFilter {
        /**
         * 过滤器排序号， 注：暂时无效，请参照业务按顺序将链插入集合中。
         */
        val squeueNumber: Int
        /**
         * 请求开始
         */
        fun request(httpRequest: HttpRequest, requestCode: Int, responseListener: NetworkResponse, chain: RequestChain)

        /**
         * 请求返回
         */
        fun response(requestCode: Int, responseCode: Int?, response: String?, message: String?, responseListener: NetworkResponse, chain: ResponseChain)
    }

    /**
     * 请求链
     */
    interface RequestChain {
        /**
         * 继续请求
         */
        fun request(httpRequest: HttpRequest, requestCode: Int, responseListener: NetworkResponse)
    }

    /**
     * 返回链
     */
    interface ResponseChain {
        /**
         * 继续返回
         */
        fun response(requestCode: Int, responseCode: Int?, response: String?, message: String?, responseListener: NetworkResponse)
    }

    /**
     * 请求链实现
     */
    private class RequestChainImpl : RequestChain {

        private var i = 0
        /**
         * 继续请求
         */
        override fun request(httpRequest: HttpRequest, requestCode: Int, responseListener: NetworkResponse) {
            if (i >= accessChain.size) {
                Executors.newCachedThreadPool().execute {
                    // - 此处 ThreadLocal，在同方法直调返回的情况下是不需要的， 后续连勇框架扩展了，或者经过业务评审后直接在此处实现网络访问后，就可以去掉了。
                    // - 并且本对象的 NetworkStringResponse 接口的实现也是不需要的了。
                    threadLocal.set(Box(httpRequest = httpRequest, networkResponse = responseListener))
                    HttpClient.getInstance().syncNetWork<Any>(httpRequest, WebRequestWrap)
                }
            } else {
                accessChain[i++].request(httpRequest, requestCode, responseListener, this)
            }
        }
    }

    /**
     * 返回链实现
     */
    private class ResponseChainImpl : ResponseChain {

        private var i = WebRequestWrap.accessChain.size - 1
        /**
         * 继续返回
         */
        override fun response(requestCode: Int, responseCode: Int?, response: String?, message: String?, responseListener: NetworkResponse) {
            if (i < 0) {
                if (responseCode != null || message != null) {
                    responseListener.onDataError(requestCode, responseCode!!, message)
                } else if (responseListener is NetworkStringResponse) {
                    responseListener.onDataReady(response, requestCode)
                }
            } else {
                try {
                    accessChain[i--].response(requestCode, responseCode, response, message, responseListener, this)
                } catch (t : Throwable) {
                    LogUtils.e(t)
                }
            }
        }
    }


    /**
     * 请求包
     */
    private data class Box (
        val httpRequest: HttpRequest,
        val networkResponse: NetworkResponse,
        var response: String? = null,
        var requestCode: Int? = null,
        var responseCode: Int? = null,
        var message: String? = null
    )
}