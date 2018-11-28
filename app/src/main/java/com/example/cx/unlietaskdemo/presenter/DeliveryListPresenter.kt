package com.xstore.tms.android.presenter

import android.annotation.SuppressLint
import com.alibaba.fastjson.JSON
import com.jd.ace.utils.NetworkUtil
import com.leinyo.httpclient.exception.HttpErrorEnum
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BasePresenter
import com.xstore.tms.android.contract.DeliveryListContract
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.entity.delivery.*
import com.xstore.tms.android.repository.DeliverOrderRepository
import com.xstore.tms.android.utils.LogUtils
import com.xstore.tms.android.utils.ToastUtils
import com.xstore.tms.taskunline.TaskDBManager
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by hly on 2018/3/16.
 * email hly910206@gmail.com
 */
class DeliveryListPresenter(private var mView: DeliveryListContract.IView) : BasePresenter(),
        DeliveryListContract.IPresenter, NetworkStringResponse{

    private var mRepository: DeliverOrderRepository? = null
    private var deliveryList:MutableList<DeliveryListEntity> = ArrayList()

    override fun getDeliveryList() {
        if (mRepository == null) {
            mRepository = DeliverOrderRepository()
        }
        if(!NetworkUtil.isConnected()){
            onDataError(0,HttpErrorEnum.RESPONSE_ERR_CONNECT.type,"")
            return
        }
        mRepository!!.getOrderList(2, mTag, this)
    }

    override fun callOutbound(outbound: RequestOutbound) {
        if(!NetworkUtil.isConnected()){
            ToastUtils.showToast(R.string.no_network)
            return
        }
        mView.showLoading()
        mRepository?.callOutbound(mTag, outbound, this)
    }

    override fun getOrderListByCollectionId(collectionId: String) {
        if (mRepository == null) {
            mRepository = DeliverOrderRepository()
        }
        if(!NetworkUtil.isConnected()){
            ToastUtils.showToast(R.string.no_network)
            return
        }
        mView.showLoading()
        mRepository!!.getOrderListByCollectionId(mTag, collectionId, this)
    }

    @SuppressLint("CheckResult")
    override fun onDataReady(response: String, requestCode: Int) {
        if(isCancelTag)return
        when (requestCode) {
            HttpConstants.REQUEST_DELIVER_LIST -> {
                val listEntity: ResponseDeliveryListEntity = JSON.parseObject(response, ResponseDeliveryListEntity::class.java)
                if (listEntity.dOrderList != null && listEntity.dOrderList!!.isNotEmpty()) {
                    //新旧list对比，如果旧有新无，则应该删掉本地数据库记录
                    val newList= buildListResult(listEntity.dOrderList!!) as MutableList<DeliveryListEntity>
                    mRepository?.deleteDeliveryTask(deliveryList,newList)
                    deliveryList = newList
                    mView.getDeliveryList(deliveryList)
                    TaskDBManager.getIntance().loadExcuteTaskIds()
                            .map {
                                it.forEach {
                                    var dono= it
                                    deliveryList.forEach {
                                        if(it.doNo==dono){
                                            it.unlineTaskState = 1
                                        }
                                    }
                                }
                            }
                            .subscribe(Consumer {
                                mView.getDeliveryList(deliveryList)
                            })

                    TaskDBManager.getIntance().loadFailureTaskIds()
                            .map {
                                it.forEach {
                                    var dono= it
                                    deliveryList.forEach {
                                        if(it.doNo==dono){
                                            it.unlineTaskState = 2
                                        }
                                    }
                                }
                            }
                            .subscribe(Consumer {
                                mView.getDeliveryList(deliveryList)
                            })
                } else {
                    mView.getDeliveryList(ArrayList())
                }
            }
            HttpConstants.COLLECTION_ORDERS -> {
                mView.hideLoading()
                val collect: ResponseCollectionOrder = JSON.parseObject(response, ResponseCollectionOrder::class.java)
                if (collect.deliveryOrderList != null && collect.deliveryOrderList!!.isNotEmpty()) {
                    mView.getDeliveryList(buildListResult(collect.deliveryOrderList!!))
                }
            }
            HttpConstants.OUTBOUND -> {
                mView.hideLoading()
                val map = JSON.parseObject(response, HashMap<String, Int>().javaClass)
                mView.outboundBack(map)
            }

        }
    }

    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
        if(isCancelTag)return
        mView.hideLoading()
        mView.onLoadComplete()
        if(responseCode ==0 ||
                responseCode == HttpErrorEnum.RESPONSE_ERR_CONNECT.type ||
                responseCode == HttpErrorEnum.RESPONSE_ERR_TIME_OUT.type){
            mView.showNoNet()
        }else{
            ToastUtils.showToast(message)
        }
    }

    private fun buildListResult(list: List<ResponseDeliveryOrder>): List<DeliveryListEntity> {
        return list.map {
            val entity = DeliveryListEntity()
            entity.address = it.address
            entity.doNo = it.doNo
            entity.consignee = it.customerName
            entity.mobilePhone = it.customerMobile
            entity.status = it.processStatus
            entity.lastDate = it.expectedArriveTime
            entity.callTime = it.callTime
            entity.transportPriority = it.transportPriority
            if (it.leaveTime != null) {
                entity.leaveTime = BigDecimal(it.leaveTime!!)
            }
            entity.sellerOrderSource = it.sellerOrderSource
            entity
        }
    }

    override fun removeItemByDoNo(needRemoveItemDoNo: String,needCheckUnlineTask:Boolean) {
        if(!deliveryList.isEmpty()){
            var position = -1
            for (i in 0 until deliveryList.size){
                if(deliveryList[i].doNo == needRemoveItemDoNo){
                    position = i
                    break
                }
            }
            if(position != -1){
                removeItem(position,needCheckUnlineTask)
            }
        }
    }

    /**
     * 判断有无离线任务
     */
    override fun removeItem(position: Int,needCheckUnlineTask:Boolean) {
        if(needCheckUnlineTask && checkUnlineTask(position)){
            LogUtils.i("listpresenter","show delete changed")
            mView.getDeliveryList(deliveryList)
            return
        }
        deliveryList.removeAt(position)
        mView.removeItem(position)
    }

    @SuppressLint("CheckResult")
    fun checkUnlineTask(position: Int):Boolean{
        var flag= false
        TaskDBManager.getIntance().queryTaskById(deliveryList[position].doNo)
                .subscribe(Consumer {
                    if(it.isNotEmpty()){
                        deliveryList[position].unlineTaskState = when(it[0].getState()){
                            0,1->1
                            2->2
                            else -> 0
                        }
                        LogUtils.i("listpresenter","changed state=${deliveryList[position].unlineTaskState}")
                        flag = true
                    }
                })
        return flag
    }

    override fun updateLeaveTime() {
        deliveryList.forEach {
            if(it.leaveTime>BigDecimal(0)){
                it.leaveTime--
            }
        }
    }

}