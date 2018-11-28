package com.example.cx.unlietaskdemo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xstore.tms.android.R;
import com.xstore.tms.android.entity.delivery.ResponseDeliveryOrder;
import com.xstore.tms.android.widget.OrderDateView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwenming1 on 2018/3/7.
 */
public class QSOrderListAdapter extends RecyclerView.Adapter<QSOrderListAdapter.ViewHolder> {

    private final List<ResponseDeliveryOrder> data = new ArrayList<>(100);

    public List<ResponseDeliveryOrder> getData() {
        return data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_qsorder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ResponseDeliveryOrder item = data.get(position);
//        Long difftime = item.getExpectedArriveTime().getTime() - System.currentTimeMillis();
        holder.id_waybill_number.setText(item.getDoNo());
        if (item.getLeaveTime() != null) {
            holder.order_date.setVisibility(View.VISIBLE);
            holder.order_date.setData(new BigDecimal(item.getLeaveTime()));
        } else {
            holder.order_date.setVisibility(View.GONE);
        }
//        if (difftime > 0) {
//            difftime = difftime / 60000L;
//            holder.id_time_remaining.setText("还剩\n" + difftime +"分");
//        } else {
//            holder.id_time_remaining.setText("还剩\n0分");
//        }
        holder.id_phone_number.setText(item.getCustomerMobile());
        holder.id_address.setText(item.getAddress());
    }


    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        OrderDateView view = holder.itemView.findViewById(R.id.order_date);
        if (view != null) {
            view.finish();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 运单号
         */
        private TextView id_waybill_number;
//        /**
//         * 剩余时间
//         */
//        private TextView id_time_remaining;
        /**
         * 剩余时间
         */
        private OrderDateView order_date;
        /**
         * 电话号码
         */
        private TextView id_phone_number;
        /**
         * 配送地址
         */
        private TextView id_address;

        public ViewHolder(View itemView) {
            super(itemView);
            id_waybill_number = itemView.findViewById(R.id.id_waybill_number);
            //id_time_remaining = itemView.findViewById(R.id.id_time_remaining);
            order_date = itemView.findViewById(R.id.order_date);
            id_phone_number = itemView.findViewById(R.id.id_phone_number);
            id_address = itemView.findViewById(R.id.id_address);
        }
    }
}
