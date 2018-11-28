package com.example.cx.unlietaskdemo.ui.ro;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leinyo.easy_refresh.widget.ptr.PullToRefreshView;
import com.xstore.tms.android.R;
import com.xstore.tms.android.adapter.ReturnOrderListAdapter;
import com.xstore.tms.android.contract.ReturnOrderContract;
import com.xstore.tms.android.core.event.EventDispatchManager;
import com.xstore.tms.android.entity.net.ro.ResponseReturnOrderListWrap;
import com.xstore.tms.android.presenter.ReturnOrderPresenter;
import com.xstore.tms.android.utils.TimeOutUtil;
import com.xstore.tms.android.widget.BottomConfirmBar;

public class ReturnOrderFragment extends Fragment implements
        ReturnOrderContract.IView,
        View.OnClickListener,
        PullToRefreshView.OnRefreshListener,
        EventDispatchManager.SubscriberListener {

    public static final int RETURN_ORDER_STATUS_UNRECEIVED = 1;
    public static final int RETURN_ORDER_STATUS_WAITING_RECEIVE = 3;

    private static final String RETURN_ORDER_STATUS = "RETURN_ORDER_STATUS";

    private ReturnOrderContract.IPresenter iPresenter = null;

    private ReturnOrderListAdapter adapter = null;

    private BottomConfirmBar bottom_confirm_bar;

    private PullToRefreshView rv_data_container;

    /**
     * 本Fragment所对应的数据状态
     */
    private int thisOrderStatus = 0;

    public ReturnOrderFragment() {
        iPresenter = new ReturnOrderPresenter(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param returnOrderStatus 取件单状态
     * @return A new instance of fragment ReturnOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReturnOrderFragment newInstance(int returnOrderStatus) {
        ReturnOrderFragment fragment = new ReturnOrderFragment();
        Bundle args = new Bundle();
        args.putInt(RETURN_ORDER_STATUS, returnOrderStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisOrderStatus = getArguments().getInt(RETURN_ORDER_STATUS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_return_order, container, false);
         rv_data_container = root.findViewById(R.id.rv_data_container);
        bottom_confirm_bar = root.findViewById(R.id.bottom_confirm_bar);

        adapter = new ReturnOrderListAdapter(
                thisOrderStatus == RETURN_ORDER_STATUS_UNRECEIVED, this::onContainerItemClick);
        rv_data_container.setAdapter(adapter);
        rv_data_container.setLayoutManager(new LinearLayoutManager(container.getContext()));
        rv_data_container.setOnRefreshListener(this);
        //loadData();
        return root;
    }

    public void loadData(){
        rv_data_container.setManualPullRefresh();
    }


    @Override
    public void onResume() {
        super.onResume();
        rv_data_container.setManualPullRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iPresenter.cancelTag();
    }

    @Override
    public void returnOrderListOnReady(ResponseReturnOrderListWrap returnOrderListWrap) {
        rv_data_container.onLoadComplete(false);
        if (returnOrderListWrap != null && returnOrderListWrap.getReturnOrderList().size() > 0) {
            adapter.setData(returnOrderListWrap.getReturnOrderList());
            if (thisOrderStatus == RETURN_ORDER_STATUS_UNRECEIVED) {
                bottom_confirm_bar.setVisibility(View.VISIBLE);
                /*if (getActivity() instanceof ReturnOrderActivity) {
                    ((ReturnOrderActivity) getActivity()).selectUnreceived();
                }*/
            }
        } else {
            adapter.setData(null);
            if (thisOrderStatus == RETURN_ORDER_STATUS_UNRECEIVED) {
                bottom_confirm_bar.setVisibility(View.GONE);
                /*if (getActivity() instanceof ReturnOrderActivity) {
                    ((ReturnOrderActivity) getActivity()).selectWaitingReceive();
                }*/
            }
        }

        if(thisOrderStatus == RETURN_ORDER_STATUS_UNRECEIVED){
            TimeOutUtil.Companion.getInstance().clearReceiveCount();
        }
    }

    private void onConfirmClick(View v, Integer action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext()).setTitle("提示");
        if (adapter.getCheckedData().size() < 1) {
            builder.setMessage("请选择取件单号").setPositiveButton("确定", null)
                    .show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            return;
        }

        AlertDialog dialog = null;
        AlertDialogEvent alertDialogEvent = new AlertDialogEvent();
        alertDialogEvent.action = action;
        builder.setCancelable(false);
        if (action != null && action.equals(BottomConfirmBar.Companion.getCANCEL())) {
            // - 取消接单
            dialog = builder.setMessage("你确认取消接单？")
                    .setPositiveButton("确定", alertDialogEvent)
                    .setNegativeButton("取消", null)
                    .show();

        } else if (action != null && action.equals(BottomConfirmBar.Companion.getCONFIRM())) {
            // - 取消接单
            dialog = builder.setMessage("你确认接单？")
                    .setPositiveButton("确定", alertDialogEvent)
                    .setNegativeButton("取消", null)
                    .show();
        }
        if (dialog != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 容器中的数据项被点击
     * @param roNo
     */
    private void onContainerItemClick(String roNo) {
        Intent intent = new Intent(this.getContext(), ReturnOrderDetailActivity.class);
        intent.putExtra("roNo", roNo);
        startActivity(intent);
    }

    @Override
    public void operateComplete() {
        rv_data_container.setManualPullRefresh();

    }

    @Override
    public void onPullRefresh(PullToRefreshView listView) {
        switch (thisOrderStatus) {
            case RETURN_ORDER_STATUS_UNRECEIVED:
                bottom_confirm_bar.setOnConfirmListener(this::onConfirmClick);
                iPresenter.loadUnreceivedReturnOrder();
                break;
            case RETURN_ORDER_STATUS_WAITING_RECEIVE:
                bottom_confirm_bar.setVisibility(View.GONE);
                iPresenter.loadWaitingReceiveReturnOrder();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMoreRefresh(PullToRefreshView refreshView) {
        rv_data_container.onLoadComplete(false);
    }

    @Override
    public void onEventMain(EventDispatchManager.Event event) {
        if(thisOrderStatus == RETURN_ORDER_STATUS_UNRECEIVED &&
                event.eventType == EventDispatchManager.EventType.EVENT_HINT_RECEVING){
            rv_data_container.setManualPullRefresh();
        }
    }

    private class AlertDialogEvent implements DialogInterface.OnClickListener {
        Integer action = null;
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (this.action != null && this.action.equals(BottomConfirmBar.Companion.getCANCEL())) {
                        // - POST 取消订单
                        ReturnOrderFragment.this.iPresenter.cancelTheOrder(adapter.getCheckedData());
                    } else if (this.action != null && this.action.equals(BottomConfirmBar.Companion.getCONFIRM())) {
                        // - POST 接受订单
                        ReturnOrderFragment.this.iPresenter.confirmTheOrder(adapter.getCheckedData());
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
        }
    }

}
