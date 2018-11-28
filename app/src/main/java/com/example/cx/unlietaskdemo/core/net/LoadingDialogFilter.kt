package com.xstore.tms.android.core.net

import com.leinyo.httpclient.retrofit.HttpRequest
import com.leinyo.httpclient.retrofit.NetworkResponse
import com.xstore.tms.android.base.BaseActivityKt
import com.xstore.tms.android.core.WebRequestWrap
import com.xstore.tms.android.widget.LoadingDialog
import java.util.*

/**
 * Loading 窗体
 */
object LoadingDialogFilter : WebRequestWrap.WebRequestWrapFilter {

    val delayClose = Timer()
    var delayCloseTask: MyTimerTask? = null


    /**
     * 过滤器排序号,
     */
    override val squeueNumber: Int = 99

//    init {
//        WebRequestWrap.accessChain.add(this)
//    }

    private var number = 0

    private var sMLoadingDialogFragment: LoadingDialog? = null

    @Synchronized
    private fun increase(): Int = ++number

    @Synchronized
    private fun decrease(): Int = --number

    /**
     * 显示提示框
     */
    private fun showLoadingDialog() {
//        var temp = delayCloseTask
//        if (temp != null) {
//            temp?.cancel()
//        }
//        temp = MyTimerTask(this)
//        delayCloseTask = temp
//        delayClose.schedule(temp, 1000 * 10)

        if (increase() > 1)
            return
        sMLoadingDialogFragment = LoadingDialog.createDialogFragment("正在请求中，请稍后...")
        sMLoadingDialogFragment!!.isCancelable = false
        val ft = BaseActivityKt.currentActivity?.supportFragmentManager?.beginTransaction()
        ft?.add(sMLoadingDialogFragment, "Loading...")
        ft?.commitAllowingStateLoss()
    }

    /**
     * 关闭加载提示框
     */
    private fun closeLoadingDialog() {
        if (decrease() > 0)
            return
        if (sMLoadingDialogFragment != null) {
            sMLoadingDialogFragment!!.dismissAllowingStateLoss()
            if (sMLoadingDialogFragment!!.isDetached) {
                sMLoadingDialogFragment!!.onDetach()
            }
            sMLoadingDialogFragment = null
        }
    }

    /**
     * 请求开始
     */
    override fun request(httpRequest: HttpRequest, requestCode: Int, responseListener: NetworkResponse, chain: WebRequestWrap.RequestChain) {
        showLoadingDialog()
        chain.request(httpRequest, requestCode, responseListener)
    }

    /**
     * 请求返回
     */
    override fun response(requestCode: Int, responseCode: Int?, response: String?, message: String?, responseListener: NetworkResponse, chain: WebRequestWrap.ResponseChain) {
        closeLoadingDialog()
        chain.response(requestCode, responseCode, response, message, responseListener)
    }

    class MyTimerTask(val parent : LoadingDialogFilter) : TimerTask() {
        /**
         * The action to be performed by this timer task.
         */
        override fun run() {
            parent.delayCloseTask = null
            parent.closeLoadingDialog()
        }
    }


//    override fun requestStart(httpRequest: HttpRequest, requestCode: Int, responseListener: NetworkResponse) {
//        showLoadingDialog()
//    }
//
//    override fun responseBack(requestCode: Int, success: Boolean) {
//        closeLoadingDialog()
//    }
}