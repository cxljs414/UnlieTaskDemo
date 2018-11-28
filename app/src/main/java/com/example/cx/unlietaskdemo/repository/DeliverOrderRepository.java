package com.example.cx.unlietaskdemo.repository;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.leinyo.httpclient.exception.HttpRequestEnum;
import com.leinyo.httpclient.retrofit.HttpClient;
import com.leinyo.httpclient.retrofit.HttpRequest;
import com.leinyo.httpclient.retrofit.NetworkResponse;
import com.xstore.tms.android.core.Const;
import com.xstore.tms.android.core.net.HttpConstants;
import com.xstore.tms.android.core.net.InterceptorResponse;
import com.xstore.tms.android.core.net.URLHandler;
import com.xstore.tms.android.entity.LoginedCarrier;
import com.xstore.tms.android.entity.RealTask;
import com.xstore.tms.android.entity.delivery.DelayOrderEntity;
import com.xstore.tms.android.entity.delivery.DeliveryListEntity;
import com.xstore.tms.android.entity.delivery.RequestCollectionOrders;
import com.xstore.tms.android.entity.delivery.RequestDeliveryComplete;
import com.xstore.tms.android.entity.delivery.RequestDeliveryException;
import com.xstore.tms.android.entity.delivery.RequestDeliveryReject;
import com.xstore.tms.android.entity.delivery.RequestLocation;
import com.xstore.tms.android.entity.delivery.RequestOutbound;
import com.xstore.tms.android.entity.net.RequestDeliveryOrderList;
import com.xstore.tms.android.entity.net.performance.RequestDeliveryOrderDetail;
import com.xstore.tms.android.entity.net.request.TaskHttpRequest;
import com.xstore.tms.android.utils.CryptoUtil;
import com.xstore.tms.android.utils.LocationxUtil;
import com.xstore.tms.android.utils.LogUtils;
import com.xstore.tms.android.utils.MonitorUtil;
import com.xstore.tms.taskunline.TaskDBManager;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangwenming1 on 2018/3/8.
 */

public class DeliverOrderRepository {
    /**
     * 获取待配送列表接口
     */
    public void getOrderList(int status, String tag, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(
                URLHandler.D_ORDER_LIST, HttpConstants.REQUEST_DELIVER_LIST, tag).requestWay(HttpRequestEnum.POST).jsonObject(new RequestDeliveryOrderList(status))
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 根据集合单号获取待配送列表
     * 返回结果：com.xstore.tms.android.entity.delivery.ResponseCollectionOrder
     */
    public void getOrderListByCollectionId(String tag, String collectionId, NetworkResponse networkResponse) {

        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.COLLECTION_ORDERS, HttpConstants.COLLECTION_ORDERS, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(new RequestCollectionOrders(collectionId))
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 获取运单明细
     *
     * @param tag
     * @param query
     * @param networkResponse
     */
    public void getDeliveryOrderDetail(String tag, RequestDeliveryOrderDetail query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.DELIVERY_ORDER_DETAIL, HttpConstants.REQUEST_DELIVERY_ORDER_DETAIL, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 异常妥投
     */
    public void orderExceptionDelivery(String tag, RequestDeliveryException params, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.EXCEPTION_DELIVERY_ORDER, HttpConstants.EXCEPTION_DELIVERY_ORDER, tag)
                .requestWay(HttpRequestEnum.POST).jsonObject(params)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 确认妥投
     */
    public void orderConfirmSafe(String tag, RequestDeliveryComplete params, NetworkResponse networkResponse) {
        String time =  String.valueOf(System.currentTimeMillis());
        String tempStr = LoginedCarrier.INSTANCE.getSfTempStr();
        String md5Str = CryptoUtil.generate(tempStr, time);
        params.setTempStr(tempStr);
        params.setMd5Str(md5Str);
        String uri = MessageFormat.format(URLHandler.COMPLETE_DELIVERY_ORDER_SAFE, time);
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(uri, HttpConstants.COMPLETE_DELIVERY_ORDER, tag)
                .requestWay(HttpRequestEnum.POST).jsonObject(params)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 离线操作
     */
    public void orderUnlineTask(Boolean isUnlineTask,String type,String tag, DelayOrderEntity entity, NetworkResponse networkResponse){
        String time =  String.valueOf(System.currentTimeMillis());
        String tempStr = LoginedCarrier.INSTANCE.getSfTempStr();
        String md5Str = CryptoUtil.generate(tempStr, time);
        entity.setTempStr(tempStr);
        entity.setMd5Str(md5Str);
        String uri = MessageFormat.format(URLHandler.DELAY_DELIVERY_ORDER, time);
        if(isUnlineTask) {
            //放入离线队列
            RealTask realTask = new RealTask(entity.getDoNo(),type,
                    new TaskHttpRequest()
                            .relateUrl(uri)
                            .jsonObject(entity)
                            .buildContent()
            );
            TaskDBManager.Companion.getIntance().insert(realTask);
        }else{
            //直接调用离线接口
            HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(
                    uri,
                    HttpConstants.DELAY_DELIVERY_ORDER,
                    tag)
                    .requestWay(HttpRequestEnum.POST)
                    .jsonObject(entity)
                    .create();
            HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
        }
    }

    /**
     * 确认拒收
     */
    public void orderRejectSafe(String tag, RequestDeliveryReject params, NetworkResponse networkResponse) {
        String time =  String.valueOf(System.currentTimeMillis());
        String tempStr = LoginedCarrier.INSTANCE.getSfTempStr();
        String md5Str = CryptoUtil.generate(tempStr, time);
        params.setTempStr(tempStr);
        params.setMd5Str(md5Str);
        String uri = MessageFormat.format(URLHandler.REJECT_DELIVERY_ORDER_SAFE, time);
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(uri, HttpConstants.REJECT_DELIVERY_ORDER, tag)
                .requestWay(HttpRequestEnum.POST).jsonObject(params)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 提交定位
     */
    public void postLocation(String tag, RequestLocation params, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.UPLOAD_LOCATION, HttpConstants.UPLOAD_LOCATION, tag)
                .requestWay(HttpRequestEnum.POST).jsonObject(params)
                .create();
//        WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     *  一键外呼功能 <br />
     *  返回结构为 Map[doNo(String), 0|1(int)].  0 失败， 1 成功。
     * @param tag
     * @param outbound
     * @param networkResponse
     */
    public void callOutbound(String tag, RequestOutbound outbound, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.OUTBOUND, HttpConstants.OUTBOUND, tag)
                .requestWay(HttpRequestEnum.POST).jsonObject(outbound)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    @SuppressLint("CheckResult")
    public void deleteDeliveryTask(@NotNull List<DeliveryListEntity> oldList, @NotNull List<DeliveryListEntity> newList) {
        Flowable.just(oldList)
                .flatMap((Function<List<DeliveryListEntity>, Publisher<DeliveryListEntity>>) Flowable::fromIterable)
                .map(oldEntity -> {
                    Boolean isExist = false;
                    for (int i = 0;i<newList.size();i++){
                        if(oldEntity.getDoNo().equals(newList.get(i).getDoNo())){
                            isExist = true;
                            break;
                        }
                    }
                    if(!isExist){
                        return oldEntity.getDoNo();
                    }
                    return "";
                })
                .map(id -> {
                    if(!id.isEmpty()){
                        TaskDBManager.Companion.getIntance().deleteTask(id);
                    }
                    return id;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s -> LogUtils.i("deleteTask","delete task="+s+" 成功"));
    }
}
