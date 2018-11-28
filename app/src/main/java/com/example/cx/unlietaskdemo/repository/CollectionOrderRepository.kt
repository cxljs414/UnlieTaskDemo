package com.xstore.tms.android.repository

import com.leinyo.httpclient.exception.HttpRequestEnum
import com.leinyo.httpclient.retrofit.HttpClient
import com.leinyo.httpclient.retrofit.HttpRequest
import com.leinyo.httpclient.retrofit.NetworkResponse
import com.xstore.tms.android.core.WebRequestWrap
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.core.net.InterceptorResponse
import com.xstore.tms.android.core.net.URLHandler
import com.xstore.tms.android.entity.collection.RequestReceiveOrRefuse
import com.xstore.tms.android.entity.delivery.RequestCollectionOrders

/**
 * Created by wangwenming1 on 2018/3/31.
 */
class CollectionOrderRepository {

    /**
     * 根据集合单号获取待配送列表
     * 返回结果：com.xstore.tms.android.entity.delivery.ResponseCollectionOrder
     */
    fun getOrderListByCollectionId(tag: String, collectionId: String, networkResponse: NetworkResponse) {
        val httpRequest = HttpRequest.HttpRequestBuilder(URLHandler.GET_COLLECTION_ORDER, HttpConstants.GET_COLLECTION_ORDER, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(RequestCollectionOrders(collectionId))
                .create()
        //WebRequestWrap.asyncNetWork(httpRequest, networkResponse)
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }

    /**
     * 确认接受集合单 <br />
     * 返回参数：com.xstore.tms.android.entity.collection.ResponseReceiveOrRefuse
     */
    fun receiveCollectionOrder(tag: String, collectionId: String, networkResponse: NetworkResponse) {
        val httpRequest = HttpRequest.HttpRequestBuilder(URLHandler.RECEIVE_COLLECTION_ORDER, HttpConstants.RECEIVE_COLLECTION_ORDER, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(RequestReceiveOrRefuse(collectionId))
                .create()
        //WebRequestWrap.asyncNetWork(httpRequest, networkResponse)
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }

    /**
     * 拒绝接受集合单 <br />
     * 返回参数：com.xstore.tms.android.entity.collection.ResponseReceiveOrRefuse
     */
    fun refuseCollectionOrder(tag: String, collectionId: String, rejectList: String?, networkResponse: NetworkResponse) {
        val httpRequest = HttpRequest.HttpRequestBuilder(URLHandler.REFUSE_COLLECTION_ORDER, HttpConstants.REFUSE_COLLECTION_ORDER, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(RequestReceiveOrRefuse(collectionId, rejectList))
                .create()
        //WebRequestWrap.asyncNetWork(httpRequest, networkResponse)
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }

}