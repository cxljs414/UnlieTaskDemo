package com.example.cx.unlietaskdemo.core.net;

/**
 * Created by hly on 2017/1/16.
 * email hly910206@gmail.com
 */

public class HttpConstants {

    public static final int RESULT_OK = 200;

    /**
     * 登录 请求编号
     */
    public static final int REQUEST_LOGIN = 1;

    /**
     * 获取登录者信息 请求编号
     */
    public static final int REQUEST_GET_USR_INFO = 2;

    /**
     * 获取已完成运单/取件单数量 请求编号
     */
    public static final int COMPLETE_DO_AND_RO_COUNT_QN = 3;

    /**
     * 代配送列表
     *
     * @return
     */
    public static final int REQUEST_DELIVER_LIST = 4;

    /**
     * 根据collectionId获取运单列表接口
     */
    public static final int COLLECTION_ORDERS = 5;

    /**
     * 获取运单明细
     */
    public static final int REQUEST_DELIVERY_ORDER_DETAIL = 6;

    /**
     * 查询取件单明细
     */
    public static final int RETURN_ORDER_DETAIL = 7;

    /**
     * 获取上门取件单列表 (未接受)
     */
    public static final int RETURN_ORDER_LIST_UNRECEIVED = 8;

    /**
     * 获取上门取件单列表 (待处理)
     */
    public static final int RETURN_ORDER_LIST_WAITING_RECEIVE = 9;

    /**
     * 获取门店坐标
     */
    public static final int REQUEST_STORE_LOCATION = 10;

    /**
     * 配送员取消接单
     */
    public static final int CARRIER_CANCEL_RO = 11;

    /**
     * 配送员确认接单
     */
    public static final int CARRIER_CONFIRM_RO = 12;

    /**
     * 确认取件?
     */
    public static final int CONFIRM_RETURN_ORDER = 13;

    /**
     * 终止取件
     */
    public static final int TERMINATE_RETURN_ORDER = 14;

    /**
     * 确认妥投
     */
    public static final int COMPLETE_DELIVERY_ORDER = 15;

    /**
     * 确认拒收
     */
    public static final int REJECT_DELIVERY_ORDER = 16;

    /**
     * 提交定位
     */
    public static final int UPLOAD_LOCATION = 17;

    /**
     * 今日配送数量
     */
    public static final int DELIVERY_COUNT = 18;


    /** ###### 宅配业务 ###### */

    /**
     * 获取宅配列表
     */
    public static final int HOME_DELIVERY_LIST = 19;

    /**
     * 获取宅配订单详情
     */
    public static final int HOME_DELIVERY_DETAIL = 20;

    /**
     * 宅配 - 确认配送，取消配送，妥投，用户拒绝等操作
     */
    public static final int HOME_DELIVERY_HANDLE = 21;

    /** ###### 扫描集合单接单业务 ###### */

    /**
     * 根据collectionId获取运单列表
     */
    public static final int GET_COLLECTION_ORDER = 22;

    /**
     * 确认接受集合单
     */
    public static final int RECEIVE_COLLECTION_ORDER = 23;

    /**
     * 拒绝接受集合单
     */
    public static final int REFUSE_COLLECTION_ORDER = 24;

    /**
     * 获取配送完成列表
     */
    public static final int COMPLATE_DO_LIST = 25;

    /**
     * 获取已经完成取件列表
     */
    public static final int COMPLATE_RO_LIST = 26;

    /**
     * 解密手机号
     */
    public static final int DECRYPT_MOBILE_NUMBER = 27;

    /**
     * 一键外呼接口
     */
    public static final int OUTBOUND = 28;
    /**
     * 一键外呼接口
     */
    public static final int HOME_EXCEPTION_DELIVERY = 29;
    /**
     *
     */
    public static final int EXCEPTION_DELIVERY_ORDER = 30;


    /**
     * 我的业绩, 拒收单
     */
    public static final int COMPLATE_REJECT_LIST = 31;

    /**
     * 提交定位-push
     */
    public static final int UPLOAD_LOCATION_PUSH = 32;

    /**
     * 离线操作
     */
    public static final int DELAY_DELIVERY_ORDER = 33;

}

