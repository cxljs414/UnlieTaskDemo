package com.example.cx.unlietaskdemo.ui.performance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leinyo.android.appbar.AppBar;
import com.xstore.tms.android.R;
import com.xstore.tms.android.base.BaseTitleActivityKt;
import com.xstore.tms.android.contract.CourierPerformanceOrderDetailContract;
import com.xstore.tms.android.entity.delivery.ResponseDeliveryOrder;
import com.xstore.tms.android.entity.home.ResponseHomeDeliveryDetail;
import com.xstore.tms.android.entity.net.ResponseReturnOrder;
import com.xstore.tms.android.presenter.CourierPerformanceOrderDetailPresenter;
import com.xstore.tms.android.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;

/**
 * Created by wangwenming1 on 2018/3/19.
 */

public class CourierPerformanceOrderDetailActivity extends BaseTitleActivityKt<CourierPerformanceOrderDetailPresenter>
        implements CourierPerformanceOrderDetailContract.IView {

    private LinearLayout llDeliveryOrderDetail = null;
    private LinearLayout llReturnOrderDetail = null;
    private LinearLayout llHomeDeliveryOrderDetail = null;

    /** -- 订单详情 -- */
    private TextView tv_do_no;
    private TextView tv_do_consignee;
    private TextView tv_do_phone_number;
    private TextView tv_do_address;
    private TextView tv_do_count;
    private TextView tv_do_time;
    private TextView tv_do_complete_time_label;
    private TextView tv_do_complete_time;

    /** -- 取件详情 -- */
    private TextView tv_ro_no;
    private TextView tv_ro_consignee;
    private TextView tv_ro_phone_number;
    private TextView tv_ro_address;
    private TextView tv_ro_count;
    private TextView tv_ro_complete_time;

    /** -- 宅配详情 -- */
    private TextView tv_home_no;
    private TextView tv_home_consignee;
    private TextView tv_home_phone_number;
    private TextView tv_home_address;
    private TextView tv_home_count;
    private TextView tv_home_time;
    private TextView tv_home_complete_time_label;
    private TextView tv_home_complete_time;
    private TextView tv_refuse_cause;
    private LinearLayout ll_refuse_cause;

    private LinearLayout loadingviewe;

    /**
     *
     * @param from 来自
     * @param type 数据类型， 1、正常妥投；2、取件单；3、宅配订单
     * @param number
     */
    public static void startActivity(AppCompatActivity from, int type, String number) {
        Intent intent = new Intent(from, CourierPerformanceOrderDetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("number", number);
        from.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_courier_performance_order_detail;
    }

    @NotNull
    @Override
    public AppBar.TitleConfig getTitleViewConfig() {
        int type = getIntent().getIntExtra("type", -1);
        if (type == 1) {
            return buildDefaultConfig(R.string.performance_detail_title_delivery_order);
        } else if (type == 2) {
            return buildDefaultConfig(R.string.performance_detail_title_return_order);
        } else if(type == 3) {
            return buildDefaultConfig(R.string.performance_detail_title_home_delivery_order);
        }
        return buildDefaultConfig(R.string.performance_detail_title_delivery_order);
    }

    @Override
    protected void initData() {
        super.initData();

        bindLayoutViews();

        Intent intent = getIntent();
        int type = intent.getIntExtra("type", -1);
        String number = intent.getStringExtra("number");
        if (type == 1) {
            getMPresenter().loadDeliveryOrderDetail(number);
        } else if (type == 2) {
            getMPresenter().loadReturnOrderDetail(number);
        } else if(type == 3) {
            getMPresenter().loadHomeDeliveryOrderDetail(number);
        }
    }

    /**
     * 绑定视图控件
     */
    private void bindLayoutViews() {
        llDeliveryOrderDetail = findViewById(R.id.ll_delivery_order_detail);
        llReturnOrderDetail = findViewById(R.id.ll_return_order_detail);
        llHomeDeliveryOrderDetail = findViewById(R.id.ll_home_delivery_order_detail);

        tv_do_no = findViewById(R.id.tv_do_no);
        tv_do_consignee = findViewById(R.id.tv_do_consignee);
        tv_do_phone_number = findViewById(R.id.tv_do_phone_number);
        tv_do_address = findViewById(R.id.tv_do_address);
        tv_do_count = findViewById(R.id.tv_do_count);
        tv_do_time = findViewById(R.id.tv_do_time);
        tv_do_complete_time_label = findViewById(R.id.tv_do_complete_time_label);
        tv_do_complete_time = findViewById(R.id.tv_do_complete_time);

        tv_ro_no = findViewById(R.id.tv_ro_no);
        tv_ro_consignee = findViewById(R.id.tv_ro_consignee);
        tv_ro_phone_number = findViewById(R.id.tv_ro_phone_number);
        tv_ro_address = findViewById(R.id.tv_ro_address);
        tv_ro_count = findViewById(R.id.tv_ro_count);
        tv_ro_complete_time = findViewById(R.id.tv_ro_complete_time);

        tv_home_no = findViewById(R.id.tv_home_no);
        tv_home_consignee = findViewById(R.id.tv_home_consignee);
        tv_home_phone_number = findViewById(R.id.tv_home_phone_number);
        tv_home_address = findViewById(R.id.tv_home_address);
        tv_home_count = findViewById(R.id.tv_home_count);
        tv_home_time = findViewById(R.id.tv_home_time);
        tv_home_complete_time_label = findViewById(R.id.tv_home_complete_time_label);
        tv_home_complete_time = findViewById(R.id.tv_home_complete_time);
        tv_refuse_cause = findViewById(R.id.tv_refuse_cause);
        ll_refuse_cause = findViewById(R.id.ll_refuse_cause);
        loadingviewe = findViewById(R.id.loadingviewe);
        hideLoading();
    }

    @Override
    public void deliveryOrderDetailReady(ResponseDeliveryOrder deliveryOrder) {
        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        llDeliveryOrderDetail.setVisibility(View.VISIBLE);
        llReturnOrderDetail.setVisibility(View.GONE);
        llHomeDeliveryOrderDetail.setVisibility(View.GONE);

        tv_do_no.setText(deliveryOrder.getDoNo());
        tv_do_consignee.setText(deliveryOrder.getCustomerName());
        tv_do_phone_number.setText(deliveryOrder.getCustomerMobile());
        tv_do_address.setText(deliveryOrder.getAddress());
        tv_do_count.setText(deliveryOrder.getShippedQty() == null ? "0" : String.valueOf(deliveryOrder.getShippedQty()));
        //tv_do_count.setText(deliveryOrder.getDOrderItemList() == null ? "0": String.valueOf(deliveryOrder.getDOrderItemList().size()));
        tv_do_time.setText(TimeUtils.date2String(deliveryOrder.getOrderCreateDate()));
        if (deliveryOrder.getProcessStatus() != null && deliveryOrder.getProcessStatus().equals(4)) {
            tv_do_complete_time_label.setText("妥投时间：");
            tv_do_complete_time.setText(TimeUtils.date2String(deliveryOrder.getCompleteTime()));
        } else if (deliveryOrder.getProcessStatus() != null && deliveryOrder.getProcessStatus().equals(5)) {
            tv_do_complete_time_label.setText("拒收时间：");
            tv_do_complete_time.setText(TimeUtils.date2String(deliveryOrder.getRejectTime()));

        } else {
            tv_do_complete_time_label.setText("未知状态" + deliveryOrder.getProcessStatus() + "：");
        }
    }

    @Override
    public void returnOrderDetailReady(ResponseReturnOrder returnOrder) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        llDeliveryOrderDetail.setVisibility(View.GONE);
        llReturnOrderDetail.setVisibility(View.VISIBLE);
        llHomeDeliveryOrderDetail.setVisibility(View.GONE);

        tv_ro_no.setText(returnOrder.getRoNo());
        tv_ro_consignee.setText(returnOrder.getCustomerName());
        tv_ro_phone_number.setText(returnOrder.getCustomerMobile());
        tv_ro_address.setText(returnOrder.getAddress());
        tv_ro_count.setText(String.valueOf(returnOrder.getTotalQuantity()));
        tv_ro_complete_time.setText(TimeUtils.date2String(returnOrder.getCompleteTime()));
    }

    @Override
    public void homeDeliveryOrderDetailReady(@NotNull ResponseHomeDeliveryDetail homeDeliveryDetail) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        llDeliveryOrderDetail.setVisibility(View.GONE);
        llReturnOrderDetail.setVisibility(View.GONE);
        llHomeDeliveryOrderDetail.setVisibility(View.VISIBLE);

        tv_home_no.setText(homeDeliveryDetail.getHomeNo());
        tv_home_consignee.setText(homeDeliveryDetail.getConsigneeName());
        tv_home_phone_number.setText(homeDeliveryDetail.getConsigneeMobile());
        tv_home_address.setText(homeDeliveryDetail.getAddress());
        tv_home_count.setText(String.valueOf(homeDeliveryDetail.getTotalQuantity()));
        tv_home_time.setText(TimeUtils.date2String(homeDeliveryDetail.getUnderOrderTime()));
        if (homeDeliveryDetail.getHomeOrderStatus() != null && homeDeliveryDetail.getHomeOrderStatus().equals(4)) {
            ll_refuse_cause.setVisibility(View.GONE);
            tv_home_complete_time_label.setText("妥投时间：");
            tv_home_complete_time.setText(TimeUtils.date2String(homeDeliveryDetail.getCompleteTime()));
        } else if (homeDeliveryDetail.getHomeOrderStatus() != null && homeDeliveryDetail.getHomeOrderStatus().equals(5)) {
            ll_refuse_cause.setVisibility(View.VISIBLE);
            tv_home_complete_time_label.setText("拒收时间：");
            tv_home_complete_time.setText(TimeUtils.date2String(homeDeliveryDetail.getRejectTime()));
            tv_refuse_cause.setText(homeDeliveryDetail.getRejectReason());
        } else {
            ll_refuse_cause.setVisibility(View.GONE);
            tv_home_complete_time_label.setText("未知状态" + homeDeliveryDetail.getHomeOrderStatus() + "：");
        }

    }

    @Override
    public void showLoading() {
        loadingviewe.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingviewe.setVisibility(View.GONE);
    }
}
