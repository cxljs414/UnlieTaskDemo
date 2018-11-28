package com.example.cx.unlietaskdemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.text.TextUtils;
import android.util.Log;

import com.jd.ace.utils.AppUtils;
import com.jingdong.jdma.JDMaInterface;
import com.jingdong.jdma.minterface.ClickInterfaceParam;
import com.jingdong.jdma.minterface.CustomInterfaceParam;
import com.jingdong.jdma.minterface.MaInitCommonInfo;
import com.jingdong.jdma.minterface.OrderInterfaceParam;
import com.jingdong.jdma.minterface.PropertyInterfaceParam;
import com.jingdong.jdma.minterface.PvInterfaceParam;
import com.xstore.tms.android.AppApplication;
import com.xstore.tms.android.entity.LoginedCarrier;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import kotlin.Pair;

public class JDMaUtils {
    private static final String TAG = JDMaUtils.class.getSimpleName();

    public static MaInitCommonInfo maInitCommonInfo;


    public static String lastpageidstr;
    private static String event_param;

    public synchronized static MaInitCommonInfo getMaInitCommonInfo(Context context) {
        JDMaInterface.acceptProtocal(true);
        maInitCommonInfo = new MaInitCommonInfo();
        if (LogUtils.isDebug()) {
            maInitCommonInfo.site_id = "cs08";
        } else {
            maInitCommonInfo.site_id = "JA2018_3121425";
        }
        maInitCommonInfo.app_device = "ANDROID";
        maInitCommonInfo.appv = AppUtils.getAppVersionName(context);
        maInitCommonInfo.appc = String.valueOf(AppUtils.getAppVersionCode(context));
        maInitCommonInfo.build = String.valueOf(AppUtils.getAppVersionCode(context));
        maInitCommonInfo.channel = "tms";
        maInitCommonInfo.guid = DeviceUtil.getMacAddress();
        return maInitCommonInfo;
    }

    public static void init(Context context) {
        JDMaInterface.acceptProtocal(true);
        getMaInitCommonInfo(context);
        JDMaInterface.init(context, maInitCommonInfo);
        JDMaInterface.setShowLog(true);
    }


    /**
     * 统计埋点 点击量
     *
     * @param context         上下文环境
     * @param event_id        事件id，详见事件文档事件定义；
     * @param event_param     事件参数，详见事件文档事件定义；
     * @param event_func      点击事件函数名称
     * @param page            点击当前页面对象，如activity，fragment
     * @param page_param      当前页面参数， 如果该页面为商详页，则为商品ID;如果该页面为活动页，则为活动ID; 如果该页面为店铺页，则店铺ID;其他为空
     */
    public static void sendCommonData(Context context,
                                      String event_id,
                                      String event_param,
                                      String event_func,
                                      Object page,
                                      String orderId,
                                      String page_param,
                                      String page_id) {
        getMaInitCommonInfo(context);
        String className = "";
        if (page != null) {
            if ((page instanceof String)) {
                className = (String) page;
            } else {
                className = page.getClass().getName();
            }
        }
        ClickInterfaceParam clickInterfaceParam = new ClickInterfaceParam();
        Pair<Double, Double> location = LocationxUtil.INSTANCE.getCurLocation();
        if(location!=null){
            clickInterfaceParam.lat = ""+location.getFirst();
            clickInterfaceParam.lon = ""+location.getSecond();
        }
        clickInterfaceParam.event_id = event_id;
        clickInterfaceParam.event_param = event_param;
        clickInterfaceParam.event_func = event_func;
        clickInterfaceParam.page_name = className;
        clickInterfaceParam.page_param = page_param;
        clickInterfaceParam.pin = LoginedCarrier.INSTANCE.getCarrierPin();
        clickInterfaceParam.page_id = page_id;
        clickInterfaceParam.shop = String.valueOf(PreferencesUtils.getStoreId());
        clickInterfaceParam.ord = orderId;
        boolean result= JDMaInterface.sendClickData(context, maInitCommonInfo, clickInterfaceParam);
        LogUtils.i(TAG,"jdma click result="+result);
    }

    public static boolean sendCustomData(
            Context context, String eid,String ela,String par,String ctp) {
        getMaInitCommonInfo(context);
        CustomInterfaceParam customInterfaceParam = new CustomInterfaceParam();
        Pair<Double, Double> location = LocationxUtil.INSTANCE.getCurLocation();
        customInterfaceParam.lat = ""+location.getFirst();
        customInterfaceParam.lon = ""+location.getSecond();
        customInterfaceParam.eid = eid;
        customInterfaceParam.ela = ela;
        customInterfaceParam.pin = LoginedCarrier.INSTANCE.getCarrierPin();
        customInterfaceParam.par = par;
        customInterfaceParam.ctp = ctp;
        customInterfaceParam.shp = String.valueOf(LoginedCarrier.INSTANCE.getStoreId());
        return JDMaInterface.sendCustomData(context, maInitCommonInfo, customInterfaceParam);
    }


    private static int uid = -1;

    public static void saveAlexJDPV(Object page, String pageID, String url) {
        //sendPagePv(MyApp.mInstance, page, "", pageID, StoreIdUtils.getStoreId(), "", url, "");
        lastpageidstr = pageID;
    }

    public static void saveJDPV(Object page, String pageID, String page_param, String skuId, String shopId) {
        if (null != page_param) {
            event_param = page_param;
        }
        //sendPagePv(MyApp.mInstance, page, page_param, pageID, shopId, "", "", skuId);
        lastpageidstr = pageID;
    }

    public void saveJDClick(String event_id) {
        saveJDClick(event_id, "", "", new HashMap<String, String>(), "", "");
    }

    public static void saveJDClick(String event_id, String param, String skuid, HashMap<String, String> map) {
        saveJDClick(event_id, param, skuid, map, "", "");
    }

    public static void saveJDClick(String event_id, String param, String skuid, String orderId, HashMap<String, String> map) {
        saveJDClick(event_id, param, skuid, map, orderId, "");
    }

    public static void saveJDClick(String event_id, String param, String skuid, HashMap<String, String> map, String orderid, String shopId) {
        Log.i(TAG, "event_id--" + event_id + "-param--" + param + "-skuid--" + skuid + "-map--" + map + "-orderid--" + orderid);
        try {

            if (skuid == null) {
                skuid = "";
            }
            if (param == null) {
                param = "";
            }
            if (map == null) {
                map = new HashMap<>();
            }
           /* String pin = "";
            String lon = PreferenceUtil.getString("lon");
            String lat = PreferenceUtil.getString("lat");
            if (ClientUtils.isLogin()) {
                if (ClientUtils.getWJLoginHelper().getPin() != null) {
                    pin = ClientUtils.getWJLoginHelper().getPin();
                }
            }*/
            Log.i(TAG, event_id + "---" + param + "---" + skuid + "---" + map.size() + "---");
            ClickInterfaceParam clickInterfaceParam = new ClickInterfaceParam();
            clickInterfaceParam.pin = LoginedCarrier.INSTANCE.getCarrierPin();
            /*clickInterfaceParam.lat = lat;
            clickInterfaceParam.lon = lon;*/
            clickInterfaceParam.page_name = lastpageidstr;
            clickInterfaceParam.event_id = event_id;
            clickInterfaceParam.event_param = param;
            clickInterfaceParam.sku = skuid;
            clickInterfaceParam.map = map;
            if (!TextUtils.isEmpty(map.get("pageId"))) {
                clickInterfaceParam.page_id = map.get("pageId");
            }
            clickInterfaceParam.ord = orderid;
            clickInterfaceParam.shop = shopId;
            JDMaInterface.sendClickData(AppApplication.Companion.getInstance().getApplicationContext(),
                    getMaInitCommonInfo(AppApplication.Companion.getInstance().getApplicationContext()),
                    clickInterfaceParam);
        } catch (Exception e) {
        }
    }

    /**
     * 统计埋点 点击量
     *
     * @param context    上下文环境
     * @param page       点击当前页面对象，如activity，fragment
     * @param page_param 当前页面参数，如果该页面为商详页，则为商品ID;如果该页面为活动页，则为活动ID;如果该页面为店铺页，则店铺ID;为空
     * @param page_id    页面id
     * @param shop_id    店铺id
     * @param sku_tag    商品详情页商品促销活动标签
     * @param click_url  广告跳转链接
     */
    public static void sendPagePv(Context context, Object page, String page_param, String page_id, String shop_id, String sku_tag, String click_url, String sku) {

        getMaInitCommonInfo(context);
        if (context == null || page == null) {
            return;
        }
        String newClassName = (page.toString().equals("PromotionListPage") || page.toString().equals("CouponBatchListPage")) ? page.toString() : page.getClass().getName();
        PvInterfaceParam pvInterfaceParam = new PvInterfaceParam();
        pvInterfaceParam.lat = "";
        pvInterfaceParam.lon = "";
        pvInterfaceParam.page_name = newClassName;
        pvInterfaceParam.page_param = page_param;
        pvInterfaceParam.pin = LoginedCarrier.INSTANCE.getCarrierPin();
        pvInterfaceParam.page_id = page_id;
        pvInterfaceParam.sku_tag = sku_tag;
        pvInterfaceParam.click_url = click_url;
        pvInterfaceParam.loadTime = "";
        pvInterfaceParam.uct = "";
        pvInterfaceParam.lastPage = lastpageidstr;  //上一个页面参数，暂时填空，等框架改后再加上
        pvInterfaceParam.sku_tag = "";
        pvInterfaceParam.shp = shop_id;
        pvInterfaceParam.ord = "";
        pvInterfaceParam.sku = sku;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            if (uid == -1) {
                PackageManager pm = context.getPackageManager();
                @SuppressLint("WrongConstant") ApplicationInfo ai = pm.getApplicationInfo("com.xstore.sevenfresh", PackageManager.GET_ACTIVITIES);
                uid = ai.uid;
            }
            // 获取到目前为止此uid共接收的总字节数//2.2以上版本适用
            long rxBytes = TrafficStats.getUidRxBytes(uid);
            long txBytes = TrafficStats.getUidTxBytes(uid);

            hashMap.put("c_r_byte", "" + rxBytes);
            hashMap.put("c_t_byte", "" + txBytes);
            hashMap.put("actUrl", click_url);
            pvInterfaceParam.map = hashMap;
            JDMaInterface.sendPvData(context, maInitCommonInfo, pvInterfaceParam);
        } catch (Throwable e) {
        }

    }

    /**
     * 统计埋点 点击量
     *
     * @param context    上下文环境
     * @param page       点击当前页面对象，如activity，fragment
     * @param page_param 当前页面参数，如果该页面为商详页，则为商品ID;如果该页面为活动页，则为活动ID;如果该页面为店铺页，则店铺ID;为空
     * @param page_id    页面id
     * @param shop_id    店铺id
     * @param sku_tag    商品详情页商品促销活动标签
     * @param click_url  广告跳转链接
     */
    public static void sendPagePv(Context context, Object page, String page_param, String page_id, String shop_id, String sku_tag, String click_url, String lastPage, String lastPageParam) {

        getMaInitCommonInfo(context);
        if (context == null || page == null) {
            return;
        }
        String newClassName = (page.toString().equals("PromotionListPage") || page.toString().equals("CouponBatchListPage")) ? page.toString() : page.getClass().getName();
        PvInterfaceParam pvInterfaceParam = new PvInterfaceParam();
        pvInterfaceParam.lat = "187.3";
        pvInterfaceParam.lon = "231.4";
        pvInterfaceParam.page_name = newClassName;
        pvInterfaceParam.page_param = page_param;
        pvInterfaceParam.pin = "pin_test";
        pvInterfaceParam.page_id = page_id;
        pvInterfaceParam.sku_tag = sku_tag;
        pvInterfaceParam.click_url = click_url;
        pvInterfaceParam.loadTime = "155";
        pvInterfaceParam.uct = "uct_test";
        pvInterfaceParam.lastPage = lastPage;          //上一个页面，暂时填空,等框架改后再加上
        pvInterfaceParam.lastPage_param = lastPageParam;          //上一个页面，暂时填空,等框架改后再加上
        pvInterfaceParam.sku_tag = "sku_tag_test";
        pvInterfaceParam.click_url = "click_url_test";
        pvInterfaceParam.shp = shop_id;
        pvInterfaceParam.ord = "ord_test";
       /* HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            if (uid == -1) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo ai = pm.getApplicationInfo("com.jingdong.jdma.sample", PackageManager.GET_ACTIVITIES);
                uid = ai.uid;
            }
            // 获取到目前为止此uid共接收的总字节数//2.2以上版本适用
            long rxBytes = TrafficStats.getUidRxBytes(uid);
            long txBytes = TrafficStats.getUidTxBytes(uid);

            hashMap.put("c_r_byte", "" + rxBytes);
            hashMap.put("c_t_byte", "" + txBytes);
        } catch (Throwable e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        pvInterfaceParam.map = hashMap;*/
        JDMaInterface.sendPvData(context, maInitCommonInfo, pvInterfaceParam);
    }

    /**
     * @param context
     * @param page_name  页面名称
     * @param page_param 页面参数，商详为商品id
     */
    public static void sendPropertyData(Context context, String page_ts, String page_name, String page_param, String end_time) {
        getMaInitCommonInfo(context);
        if (context == null || page_name == null) {
            return;
        }
        PropertyInterfaceParam propertyInterfaceParam = new PropertyInterfaceParam();
        propertyInterfaceParam.pin = "pin_test";
        propertyInterfaceParam.lon = "134.6";
        propertyInterfaceParam.lat = "176.8";
        propertyInterfaceParam.page_name = page_name;
        propertyInterfaceParam.page_param = page_param;
        propertyInterfaceParam.end_ts = end_time;
        propertyInterfaceParam.ldns_ip = getIPAddress();

        JDMaInterface.sendPropertyData(context, maInitCommonInfo, propertyInterfaceParam);
    }


    private static String ipAddress = "";

    public static String getIPAddress() {
        if (TextUtils.isEmpty(ipAddress)) {
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress()) {
                            ipAddress = addr.getHostAddress().toUpperCase();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } // for now eat exceptions
        }
        return ipAddress;
    }


    /**
     * 订单数据埋点
     *
     * @param context    上下文环境
     * @param order      订单号
     * @param totalMoney 订单总金额
     * @param productId  商品或套装id
     * @param productNum 商品数量
     */

    public static void sendOrderDatas(Context context, String order, String totalMoney, String productId, String productNum, boolean isQinSongGou, String ord_ext) {
        getMaInitCommonInfo(context);
        OrderInterfaceParam orderInterfaceParam = new OrderInterfaceParam();
        orderInterfaceParam.lat = "159.8";
        orderInterfaceParam.lon = "175.6";
        orderInterfaceParam.pv_sid = "33";
        orderInterfaceParam.pv_seq = "22";
        orderInterfaceParam.lv1_page_name = "lv1_page_name_test";
        orderInterfaceParam.lv1_page_param = "lv1_page_param_test";
        orderInterfaceParam.lv1_event_id = "lv1_event_id_test";
        orderInterfaceParam.lv1_event_param = "lv1_event_param_test";
        orderInterfaceParam.lv2_page_name = "lv2_page_name_test";
        orderInterfaceParam.lv2_page_param = "lv2_page_param_test";
        orderInterfaceParam.lv2_event_id = "lv2_event_id_test";
        orderInterfaceParam.lv2_event_param = "lv2_event_param_test";
        orderInterfaceParam.lv3_page_name = "lv3_page_name_test";
        orderInterfaceParam.lv3_page_param = "lv3_page_param_test";
        orderInterfaceParam.lv3_event_id = "lv3_event_id_test";
        orderInterfaceParam.lv3_event_param = "lv3_event_param_test";
        orderInterfaceParam.lv4_page_name = "lv4_page_name_test";
        orderInterfaceParam.lv4_page_param = "lv4_page_param_test";
        orderInterfaceParam.lv4_event_id = "lv4_event_id_test";
        orderInterfaceParam.lv4_event_param = "lv4_event_param_test";
        orderInterfaceParam.lv5_page_name = "lv5_page_name_test";
        orderInterfaceParam.lv5_page_param = "lv5_page_param_test";
        orderInterfaceParam.lv5_event_id = "lv5_event_id_test";
        orderInterfaceParam.lv5_event_param = "lv5_event_param_test";
        orderInterfaceParam.order_total_fee = totalMoney;
        orderInterfaceParam.order_ts = System.currentTimeMillis() + "";
        orderInterfaceParam.prod_id = productId;
        orderInterfaceParam.quantity = productNum;
        orderInterfaceParam.sale_ord_id = order;
        orderInterfaceParam.pin = "pin_test";
        orderInterfaceParam.ord_ext = ord_ext;
        JDMaInterface.sendOrderData(context, maInitCommonInfo, orderInterfaceParam);
    }

    /**
     * 回收埋点引擎
     */
    public static void destroy() {
        JDMaInterface.destroy();
    }


    public static String getCurrentMicrosecond() {
        return "" + String.format("%.6f", ((System.currentTimeMillis() + 0.0) / 1000.0f));
    }


    /**
     * 设置本地保存的M页埋点信息,只用于覆盖本地埋点数据，其他地方不能使用
     * 用于内部M页跳转，这种业务场景需要保存M页的sid
     *
     * @param content
     */
    public static void setMtaContent(Context context, String content) {
        if (TextUtils.isEmpty(content))
            return;
        try {
            JDMaInterface.setMtaContent4Inside(content);
            JSONObject jsonObject = new JSONObject(content);
            //获取event_series字段，覆盖本地埋点对应的坑位
            if (jsonObject.has("event_series")) {
            }
            setSessionInfo(context, content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置本地保存的M页埋点信息,只用于覆盖本地埋点数据，其他地方不能使用
     * 用于openApp通过外部浏览器跳转过来的业务场景，这种业务场景不需要保存M页的sid
     * 这个地方只会把content保存到成员变量中
     *
     * @param content
     * @see JDMaUtils#setSessionInfo
     */
    public static void setMtaContent4OpenApp(Context context, String content) {
        if (TextUtils.isEmpty(content))
            return;

        try {

            JDMaInterface.setMtaContent4OpenApp(context, content);
            JSONObject jsonObject = new JSONObject(content);
            //获取event_series字段，覆盖本地埋点对应的坑位
            if (jsonObject.has("event_series")) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空M页埋点数据,新用户登陆成功后调用
     */
    public static void clearMtaContent() {
        JDMaInterface.clearMtaSource();
    }

    /**
     * 供M页JS直接调用
     * 原生页和M页的埋点session数据同步，供M页调用(现网)
     * 这个地方会把content保存到session文件中去
     *
     * @param content
     */

    private static void setSessionInfo(Context context, String content) {
        JDMaInterface.setSessionInfo(context, content);
    }

    /**
     * M页调用埋点数据接口(现网)
     *
     * @return
     */
    public static String getSessionInfo(Context context) {
        return JDMaInterface.getSessionInfo(context);
    }


    /**
     * 设置用户访问渠道来源跟踪 参数接口
     *
     * @param
     */

    public static void setSourceData(String sourceType, String sourceValue, Context context) {
        if (TextUtils.isEmpty(sourceType) || TextUtils.isEmpty(sourceValue))
            return;
        JDMaInterface.setSourceData(sourceType, sourceValue, context);
    }

}

