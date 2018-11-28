package com.example.cx.unlietaskdemo.core.net;

/**
 * Created by wangwenming1 on 2018/3/8.
 */

public class URLHandler {

    /** ###### 系统登录 ###### */

    /**
     * 登录
     */
    public static final String URL_LOGIN = "login/pwLogin";

    /**
     * 新登录
     */
    public static final String URL_NEW_LOGIN = "login/newLogin?v={0}&source=app";

    /**
     * 获取登录者信息
     */
    public static final String REQUEST_GET_SKU_PRINT = "userInfo/queryUserInfo";

    /**
     * 获取登录者信息
     */
    public static final String REQUEST_GET_SKU_PRINT_SAFE = "userInfo/newQueryUserInfo?v={0}&source=app";

    /** ###### 妥投业务 ###### */

    /**
     * 获取待配送列表接口
     */
    public static final String D_ORDER_LIST = "deliveryOrderTask/getDOrderList";

    /**
     * 根据collectionId获取运单列表接口
     */
    public static final String COLLECTION_ORDERS = "deliveryOrderTask/getCOrder";

    /** ###### 妥投业务 ###### */

    /**
     * 确认妥投
     */
    public static final String COMPLETE_DELIVERY_ORDER = "deliveryOrderTask/completeDeliveryOrder";

    /**
     * 异常妥投访问链接
     */
    public static final String EXCEPTION_DELIVERY_ORDER = "deliveryOrderTask/getAbnormalCompleteList";

    /**
     * 确认妥投
     */
    public static final String COMPLETE_DELIVERY_ORDER_SAFE = "deliveryOrderTask/newCompleteDeliveryOrder?v={0}&source=app";

    /**
     * 离线订单
     */
    public static final String DELAY_DELIVERY_ORDER = "deliveryOrderTask/delayDeliveryOrder?v={0}&source=app";

    /**
     * 拒收
     */
    public static final String REJECT_DELIVERY_ORDER = "deliveryOrderTask/rejectDeliveryOrder";

    /**
     * 拒收
     */
    public static final String REJECT_DELIVERY_ORDER_SAFE = "deliveryOrderTask/newRejectDeliveryOrder?v={0}&source=app";

    /**
     * 上传位置信息接口
     */
    public static final String UPLOAD_LOCATION = "deliveryOrderTask/uploadLocation";

    /**
     * 上传位置信息接口-push
     */
    public static final String UPLOAD_LOCATION_PUSH = "carrierLocation/uploadLocation?v={0}&source=app";


    /**
     * 一键外呼接口
     */
    public static final String OUTBOUND = "deliveryOrderTask/sendAudioMessage";

    /**
     * 获取已完成运单/取件单数量
     */
    public static final String COMPLETE_DO_AND_RO_COUNT = "deliveryOrderTask/completeDoAndRoCount";

    /**
     * 已经完成的订单
     */
    public static final String COMPLATE_DO_LIST = "deliveryOrderTask/queryCompleteDoList";

    /**
     * 获取已经完成取件列表
     */
    public static final String COMPLATE_RO_LIST = "returnOrder/queryCompleteRoList";

    /**
     * 获取拒收单列表   gary待改
     */
    public static final String COMPLATE_REJECT_LIST = "deliveryOrderTask/queryRejectDoList";

    /**
     * 获取运单明细
     */
    public static final String DELIVERY_ORDER_DETAIL = "deliveryOrderTask/getDOrderItem";

    /**
     * 获取取件单明细
     */
    public static final String RETURN_ORDER_DETAIL = "returnOrder/queryReturnOrderInfo";

    /**
     * 获取上门取件单列表
     */
    public static final String RETURN_ORDER_LIST = "returnOrder/queryReturnOrderList";

    /**
     * 获取门店坐标
     */
    public static final String REQUEST_GET_STORE_LOCATION = "deliveryOrderTask/getStoreLocation";

    /**
     * 配送员取消接单
     */
    public static final String CARRIER_CANCEL_RO = "returnOrder/carrierCancelRo";

    /**
     * 配送员确认接单
     */
    public static final String CARRIER_CONFIRM_RO = "returnOrder/carrierConfirmRo";

    /**
     * 确认取件?
     */
    public static final String CONFIRM_RETURN_ORDER = "returnOrder/customerConfirmRo";

    /**
     * 确认取件?
     */
    public static final String CONFIRM_RETURN_ORDER_SAFE = "returnOrder/newCustomerConfirmRo?v={0}&source=app";

    /**
     * 终止取件
     */
    public static final String TERMINATE_RETURN_ORDER = "returnOrder/terminateReturnOrder";

    /**
     * 终止取件
     */
    public static final String TERMINATE_RETURN_ORDER_SAFE = "returnOrder/newTerminateReturnOrder?v={0}&source=app";

    /**
     * 今日配送数量
     */
    public static final String DELIVERY_COUNT = "deliveryOrderTask/deliveryCount";

    /** ###### 宅配业务 ###### */

    /**
     * 获取宅配列表
     */
    public static final String HOME_DELIVERY_LIST = "homeDelivery/list";

    /**
     * 异常妥投
     */
    public static final String HOME_EXCEPTION_DELIVERY = "deliveryOrderTask/abnormalComplete?v={0}&source=app";

    /**
     * 获取宅配订单详情
     */
    public static final String HOME_DELIVERY_DETAIL = "homeDelivery/detail/{0}";

    /**
     * 宅配 - 确认配送，取消配送，妥投，用户拒绝等操作
     */
    public static final String HOME_DELIVERY_HANDLE = "homeDelivery/handle";

    /**
     * 宅配 - 确认配送，取消配送，妥投，用户拒绝等操作
     */
    public static final String HOME_DELIVERY_HANDLE_SAFE = "homeDelivery/newHandle?v={0}&source=app";

    /** ###### 扫描集合单接单业务 ###### */

    /**
     * 根据collectionId获取运单列表
     */
    public static final String GET_COLLECTION_ORDER = "deliveryOrderTask/getCOrder";

    /**
     * 确认接受集合单
     */
    public static final String RECEIVE_COLLECTION_ORDER = "deliveryOrderTask/receiveCOrder";

    /**
     * 拒绝接受集合单
     */
    public static final String REFUSE_COLLECTION_ORDER = "deliveryOrderTask/refuseCOrder";

    /**
     * 解密手机号
     */
    public static final String DECRYPT_MOBILE_NUMBER = "decrypt/decryptMobile";

    /**
     * 解密手机号
     */
    public static final String DECRYPT_MOBILE_NUMBER_SAFE = "decrypt/newDecryptMobile?v={0}&source=app";

    /**
     * 埋点回传接口
     */
    public static final String UPLOAD_MONITOR_INFO_SAFE = "monitorInfo/uploadMonitorInfo?v={0}&source=app";
}
