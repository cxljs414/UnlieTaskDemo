package com.xstore.tms.android.core.net

import com.alibaba.fastjson.JSON
import com.leinyo.httpclient.retrofit.HttpRequest
import com.leinyo.httpclient.retrofit.NetworkResponse
import com.xstore.tms.android.core.WebRequestWrap
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.net.Content
import com.xstore.tms.android.entity.net.Msg
import com.xstore.tms.android.ui.LoginActivityKt
import com.xstore.tms.android.utils.AesUtils
import com.xstore.tms.android.utils.LogUtils
import com.xstore.tms.android.utils.RSAUtil
import com.xstore.tms.android.utils.ToastUtils
import org.apache.commons.lang3.StringUtils

/**
 * 请求结果过滤，替代连勇实现的（InterceptorStringResponse）
 * Created by wangwenming3 on 2018/4/20.
 */
object InterceptorStringFilter : WebRequestWrap.WebRequestWrapFilter{
    /**
     * 过滤器排序号， 注：暂时无效，请参照业务按顺序将链插入集合中。
     */
    override val squeueNumber: Int = 0

    private var isLogin = false

    /**
     * 请求开始
     */
    override fun request(httpRequest: HttpRequest, requestCode: Int, responseListener: NetworkResponse, chain: WebRequestWrap.RequestChain) {
        chain.request(httpRequest, requestCode, responseListener)
    }

    /**
     * 请求返回
     */
    override fun response(requestCode: Int, responseCode: Int?, response: String?, message: String?, responseListener: NetworkResponse, chain: WebRequestWrap.ResponseChain) {
        if (StringUtils.isNotBlank(response)) {
            var msg: Msg?
            var resp = response
            if (!resp!!.trim().startsWith("{") && !resp.trim().startsWith("[")) {
                resp = RSAUtil.decrypt(resp, RSAUtil.PUBLIC_KEY)
            }
            if (!resp!!.trim().startsWith("{") && !resp.trim().startsWith("[")) {
                resp = AesUtils.aesDecrypt(resp, LoginedCarrier.sfKeyMD5)
            }
            msg = JSON.parseObject(resp, Msg::class.java)
            showMessage(msg)
            when (msg.flag) {
                Msg.SUCCESS -> {
                    if (msg.content != null && msg.content.isNotBlank() && msg.content.indexOf("success") >= 0) {
                        var content = JSON.parseObject(msg.content, Content::class.java)
                        if (content.success != null && !content.success!!) {
                            ToastUtils.showToast(content.errorMsg)
                        }
                    }
                    chain.response(requestCode, responseCode, msg.content, message, responseListener)
                }
                Msg.BADPARAMS ->
                    chain.response(requestCode, 500, null, msg.content, responseListener)
                Msg.NEEDLOGIN ->{
                    LogUtils.i("Intercept","StringFilter needrelogin msg=$msg")
                    login()
                }
                else ->
                    chain.response(requestCode, 444, null, msg.content, responseListener)
            }
        } else {
            chain.response(requestCode, responseCode, response, message, responseListener)
        }
    }

    private fun showMessage(msg: Msg) {
        if (msg.flag == Msg.BADPARAMS || msg.flag == Msg.ERROR || msg.flag == Msg.NEEDLOGIN) {
            ToastUtils.showToast(msg.content)
        }
    }

    private fun login() {
        LoginActivityKt.startActivity()

//        var activity = BaseActivityKt.currentActivity
        // - TODO 需要单独处理登录，同步方式虽然可以处理重复弹出登录窗体的问题，但是不合理，也存在漏洞。
//        if (!isLogin) {
//            synchronized(this) {
//                if (!isLogin) {
//        if (activity != null) {
//            activity.startActivity(Intent(activity, LoginActivityKt::class.java))
//        } else {
//            AppApplication.instance!!.startActivity(Intent(AppApplication.instance!!.applicationContext, LoginActivityKt::class.java))
//        }
//                    isLogin = true
//                }
//            }
//        }
    }
}