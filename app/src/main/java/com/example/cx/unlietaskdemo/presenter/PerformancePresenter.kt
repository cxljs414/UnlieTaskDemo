package com.xstore.tms.android.presenter

import com.alibaba.fastjson.JSON
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.base.BasePresenter
import com.xstore.tms.android.contract.PerformanceContract
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.entity.home.RequestHomeDeliveryList
import com.xstore.tms.android.entity.home.ResponseHomeDeliveryList
import com.xstore.tms.android.entity.net.performance.RequestCompleteDoAndRo
import com.xstore.tms.android.entity.net.performance.ResponseCompleteDo
import com.xstore.tms.android.entity.net.performance.ResponseCompleteRo
import com.xstore.tms.android.repository.CourierPerformanceRepository
import com.xstore.tms.android.repository.HomeDeliveryRepository
import com.xstore.tms.android.ui.performance.PerformanceActivity
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.lastMonth_delivery
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.lastMonth_homeComplete
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.lastMonth_homeRejection
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.lastMonth_return
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.lastMonth_reject
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.thisMonth_delivery
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.thisMonth_homeComplete
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.thisMonth_homeRejection
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.thisMonth_reject
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.thisMonth_return
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.today_Reject
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.today_delivery
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.today_homeComplete
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.today_homeRejection
import com.xstore.tms.android.ui.performance.PerformanceActivity.Companion.today_return
import com.xstore.tms.android.utils.TimeUtils
import com.xstore.tms.android.utils.ToastUtils
import org.apache.commons.lang3.StringUtils
import java.text.SimpleDateFormat

/**
 * Created by wangwenming1 on 2018/4/4.
 */
class PerformancePresenter(val mView: PerformanceContract.IView)
    : BasePresenter(), PerformanceContract.IPresenter, NetworkStringResponse {

    private val repository = CourierPerformanceRepository()

    private val homeDeliveryRepository = HomeDeliveryRepository()

    private var pageNumber: Int = 1

    override fun loadData(navigation: PerformanceActivity.Navigation) {
        when(navigation) {
            today_delivery, lastMonth_delivery, thisMonth_delivery -> {
                repository.getCompleteDoList(HttpConstants.COMPLATE_DO_LIST, mTag, RequestCompleteDoAndRo(navigation.key, pageNumber), this)
            }
            today_Reject, lastMonth_reject, thisMonth_reject -> {
                repository.getCompleteRejectList(HttpConstants.COMPLATE_REJECT_LIST, mTag, RequestCompleteDoAndRo(navigation.key, pageNumber), this)//提供新接口gary
            }
            today_return, lastMonth_return, thisMonth_return -> {
                repository.getCompleteRoList(HttpConstants.COMPLATE_RO_LIST, mTag, RequestCompleteDoAndRo(navigation.key, pageNumber), this)
            }
            today_homeComplete, lastMonth_homeComplete, thisMonth_homeComplete -> {
                homeDeliveryRepository.getHomeDeliveryList(mTag,
                        RequestHomeDeliveryList(pageNo = pageNumber.toLong(), searchType = 3, dateType = navigation.key, pageSize = 10), this)
            }
            today_homeRejection, lastMonth_homeRejection, thisMonth_homeRejection -> {
                homeDeliveryRepository.getHomeDeliveryList(mTag,
                        RequestHomeDeliveryList(pageNo = pageNumber.toLong(), searchType = 4, dateType = navigation.key, pageSize = 10), this)
            }
        }
    }

    override fun refresh(navigation: PerformanceActivity.Navigation) {
        pageNumber = 1
        loadData(navigation)
    }

    override fun loadMore(navigation: PerformanceActivity.Navigation) {
        pageNumber++
        loadData(navigation)
    }

    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
        if(isCancelTag)return
        ToastUtils.showToast(message)
    }

    override fun onDataReady(response: String?, requestCode: Int) {
        if(isCancelTag)return
        if (StringUtils.isBlank(response)) {
            ToastUtils.showToast("请求结果为空， - $requestCode")
            return
        }

        when (requestCode) {
            HttpConstants.COMPLATE_DO_LIST -> {
                val v = JSON.parseObject(response, ResponseCompleteDo::class.java)
                if (v?.dOrderList == null || v.dOrderList!!.isEmpty()) {
                    if(pageNumber > 1) pageNumber--
                }
                val data = v.dOrderList!!.map {
                    with(it) {
                        PerformanceContract.ItemData(doNo.toString(), "运单号：", TimeUtils.date2String(completeTime), "妥投时间：")
                    }
                }
                mView.onDataReady(data, pageNumber == 1)
            }
            HttpConstants.COMPLATE_REJECT_LIST -> {
                val v = JSON.parseObject(response, ResponseCompleteDo::class.java)
                if (v?.dOrderList == null || v.dOrderList!!.isEmpty()) {
                    if(pageNumber > 1) pageNumber--
                }
                val data = v.dOrderList!!.map {
                    with(it) {
                        PerformanceContract.ItemData(doNo.toString(), "运单号：", TimeUtils.date2String(rejectTime), "拒收时间：")
                    }
                }
                mView.onDataReady(data, pageNumber == 1)
            }
            HttpConstants.COMPLATE_RO_LIST -> {
                val v = JSON.parseObject(response, ResponseCompleteRo::class.java)
                if (v?.returnOrderList == null || v.returnOrderList!!.isEmpty()) {
                    if (pageNumber > 1) pageNumber--
                }
                val data = v.returnOrderList!!.map {
                    with(it) {
                        PerformanceContract.ItemData(roNo.toString(), "取件单号：", TimeUtils.date2String(completeTime), "取件时间：")
                    }
                }
                mView.onDataReady(data, pageNumber == 1)
            }
            HttpConstants.HOME_DELIVERY_LIST -> {
                val v = JSON.parseObject(response, ResponseHomeDeliveryList::class.java)
                if (v?.returnPage == null || v.returnPage?.result == null || v.returnPage!!.result!!.isEmpty()) {
                    if (pageNumber > 1) pageNumber--
                }
                val data = v.returnPage!!.result!!.map {
                    with(it) {
                        if (homeOrderStatus == 4)
                            PerformanceContract.ItemData(homeNo.toString(), "宅配单号：", TimeUtils.date2String(completeTime),"妥投时间：")
                        else
                            PerformanceContract.ItemData(homeNo.toString(), "宅配单号：", TimeUtils.date2String(rejectTime),"拒收时间：")
                    }
                }
                mView.onDataReady(data, pageNumber == 1)
            }
        }
    }
}