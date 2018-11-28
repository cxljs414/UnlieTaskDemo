package com.example.cx.unlietaskdemo.ui.ro;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leinyo.android.appbar.AppBar;
import com.xstore.tms.android.R;
import com.xstore.tms.android.base.BaseTitleActivityKt;
import com.xstore.tms.android.contract.ReturnOrderDetailContract;
import com.xstore.tms.android.entity.net.ResponseReturnOrder;
import com.xstore.tms.android.entity.net.ResponseReturnOrderItem;
import com.xstore.tms.android.entity.net.ro.RequestTerminateOrConfirmRo;
import com.xstore.tms.android.presenter.ReturnOrderDetailPresenter;
import com.xstore.tms.android.utils.ToastUtils;
import com.xstore.tms.android.widget.BottomConfirmBar;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class ReturnOrderDetailActivity extends BaseTitleActivityKt<ReturnOrderDetailPresenter> implements ReturnOrderDetailContract.IView {
    private LinearLayout ll_notice_bar;
    private ImageView iv_notice_bar_handle;
    private TextView tv_return_order_no;
    private TextView tv_consignee;
    private TextView tv_phone_number;
    private TextView tv_address;
    private ImageView iv_phone_button;
    private TextView tv_return_order_count;
    private LinearLayout ll_sku_list;
    private BottomConfirmBar bottom_confirm_bar;
    private LinearLayout ll_notice_bar_2;
    private View v_notice_bar_2;
    private String[] cancelChoice = new String[]{ "客户不退了", "客户不在家", "商品不全" };
    private HashSet<String> checkedCancelChoice = new HashSet<>(3);


    @NotNull
    @Override
    public AppBar.TitleConfig getTitleViewConfig() {
        return buildDefaultConfig(R.string.title_return_order_detail);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_return_order_detail;
    }

    @Override
    protected void initData() {
        initViews();

        Intent intent = getIntent();
        String roNo = intent.getStringExtra("roNo");
        getMPresenter().loadReturnOrderDetail(roNo);
    }

    private void initViews() {
        ll_notice_bar = findViewById(R.id.ll_notice_bar);
        iv_notice_bar_handle = findViewById(R.id.iv_notice_bar_handle);
        tv_return_order_no = findViewById(R.id.tv_return_order_no);
        tv_consignee = findViewById(R.id.tv_consignee);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_address = findViewById(R.id.tv_address);
        iv_phone_button = findViewById(R.id.iv_phone_button);
        tv_return_order_count = findViewById(R.id.tv_return_order_count);
        ll_sku_list = findViewById(R.id.ll_sku_list);
        bottom_confirm_bar = findViewById(R.id.bottom_confirm_bar);
        ll_notice_bar_2 = findViewById(R.id.ll_notice_bar_2);
        v_notice_bar_2 = findViewById(R.id.v_notice_bar_2);

        bottom_confirm_bar.setOnConfirmListener(this::onConfirmClick);

        iv_phone_button.setOnClickListener(this::onClick);
        iv_notice_bar_handle.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        if (v == iv_phone_button) {
            getMPresenter().decryptMobileNumber(tv_return_order_no.getText().toString());
            return;
        }

        if (v == iv_notice_bar_handle) {
            // - 关闭提醒 bar
            ll_notice_bar.setVisibility(View.GONE);
            return;
        }
    }

    private void onConfirmClick(View v, int action) {
        if (action == BottomConfirmBar.Companion.getCANCEL()) {
            showTerminaDialog();
        } else if (action == BottomConfirmBar.Companion.getCONFIRM()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            AlertDialog dialog = null;
            dialog = dialogBuilder.setTitle("提示").setMessage("确认取件？").setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog1, which) -> {
                        RequestTerminateOrConfirmRo terminateRo = new RequestTerminateOrConfirmRo();
                        terminateRo.setRoNo(tv_return_order_no.getText().toString());
                        getMPresenter().confirmReturnOrder(terminateRo);
                    }).show();
            if (dialog != null) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        }

    }

    private void showTerminaDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view= View.inflate(this,R.layout.dialog_reject_return,null);
        view.findViewById(R.id.tv_submit).setOnClickListener(v -> {
            HashSet<String> checkedChoices = new HashSet<>(3);
            if(((CheckBox)(view.findViewById(R.id.reason1))).isChecked()){
                checkedChoices.add("客户不买了");
            }
            if(((CheckBox)(view.findViewById(R.id.reason2))).isChecked()){
                checkedChoices.add("客户不在家");
            }
            if(((CheckBox)(view.findViewById(R.id.reason3))).isChecked()){
                checkedChoices.add("商品有损或不全");
            }
            if(checkedChoices.isEmpty()){
                ToastUtils.showToast(getString(R.string.please_termina_reason));
            }else{
                RequestTerminateOrConfirmRo terminateRo = new RequestTerminateOrConfirmRo();
                terminateRo.setRoNo(tv_return_order_no.getText().toString());
                terminateRo.setTermination(checkedChoices.toArray(new String[checkedChoices.size()]));
                getMPresenter().terminateReturnOrder(terminateRo);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void onDataReady(ResponseReturnOrder returnOrder) {
        if(returnOrder.isShouldCheck() != null && returnOrder.isShouldCheck().equals(1)) {
            ll_notice_bar_2.setVisibility(View.VISIBLE);
            v_notice_bar_2.setVisibility(View.VISIBLE);
        }
        tv_return_order_no.setText(returnOrder.getRoNo());
        tv_consignee.setText(returnOrder.getCustomerName());
        tv_phone_number.setText(returnOrder.getCustomerMobile());
        tv_address.setText(returnOrder.getAddress());
        tv_return_order_count.setText(String.valueOf(returnOrder.getTotalQuantity()));
        if (returnOrder.getRoItemList() != null && returnOrder.getRoItemList().size() > 0) {
            for (ResponseReturnOrderItem item : returnOrder.getRoItemList()) {
                ll_sku_list.addView(createSkuItemView(item.getProductName() + "　　X" + item.getQuantity()));
            }
        }
    }

    /**
     * 确认和取消返回
     */
    @Override
    public void operateComeback() {
        finish();
    }

    /**
     * 返回电话号码
     *
     * @param phoneNo
     */
    @Override
    public void backMobileNumber(String phoneNo) {
        if (StringUtils.isBlank(phoneNo)) {
            ToastUtils.showToast("电话号码不能为空。");
        } else {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
            startActivity(intent);
        }
    }

    private TextView createSkuItemView(String text) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, this.getResources().getDimensionPixelOffset(R.dimen.dp_6), 0, 0);
        TextView tv = new TextView(this);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setText(text);
        return tv;
    }
}
