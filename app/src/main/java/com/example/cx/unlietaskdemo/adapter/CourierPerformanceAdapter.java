package com.example.cx.unlietaskdemo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xstore.tms.android.R;
import com.xstore.tms.android.entity.net.performance.ResponseCompleteItem;
import com.xstore.tms.android.entity.net.performance.ResponseCompleteOrderItem;
import com.xstore.tms.android.entity.net.performance.ResponseCompleteReturnOrderItem;
import com.xstore.tms.android.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人业绩
 * Created by wangwenming1 on 2018/3/16.
 */
public class CourierPerformanceAdapter extends RecyclerView.Adapter<CourierPerformanceAdapter.ViewHolder> {

    private List<ResponseCompleteItem> data = new ArrayList<>();

    private OnItemClickListener itemClickListener;

    public CourierPerformanceAdapter(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public List<ResponseCompleteItem> getData() {
        return data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_courier_perf_tab_item, parent, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 0 || position >= getData().size())
            return;
        ResponseCompleteItem dataItem = getData().get(position);
        holder.completeItem = dataItem;
        if (dataItem instanceof ResponseCompleteOrderItem) {
            ResponseCompleteOrderItem orderItem = (ResponseCompleteOrderItem) dataItem;
            holder.setOrderId(orderItem.getDoNo());
            if (orderItem.getCompleteTime() != null)
                holder.setTimeText(TimeUtils.date2String(orderItem.getCompleteTime()));
        } else if (dataItem instanceof ResponseCompleteReturnOrderItem) {
            ResponseCompleteReturnOrderItem returnOrderItem = (ResponseCompleteReturnOrderItem) dataItem;
            holder.setTimeLabel("取件时间: ");
            holder.setOrderId(returnOrderItem.getRoNo());
            if (returnOrderItem.getCompleteTime() != null)
                holder.setTimeText(TimeUtils.date2String(returnOrderItem.getCompleteTime()));
        }
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ResponseCompleteItem completeItem;
        private TextView orderId;
        private TextView timeLabel;
        private TextView timeText;

        private CourierPerformanceAdapter parent = null;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            orderId = itemView.findViewById(R.id.order_id);
            timeLabel = itemView.findViewById(R.id.time_label);
            timeText = itemView.findViewById(R.id.time_text);
        }

        public ViewHolder(View itemView, CourierPerformanceAdapter parent) {
            this(itemView);
            this.parent = parent;
        }

        public String getOrderId() {
            return orderId.getText().toString();
        }

        public void setOrderId(String orderId) {
            this.orderId.setText(orderId);
        }

        public String getTimeLabel() {
            return timeLabel.getText().toString();
        }

        public void setTimeLabel(String timeLabel) {
            this.timeLabel.setText(timeLabel);
        }

        public String getTimeText() {
            return timeText.getText().toString();
        }

        public void setTimeText(String timeText) {
            this.timeText.setText(timeText);
        }

        @Override
        public void onClick(View v) {
             parent.itemClickListener.onItemClick(completeItem);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ResponseCompleteItem dataItem);
    }
}
