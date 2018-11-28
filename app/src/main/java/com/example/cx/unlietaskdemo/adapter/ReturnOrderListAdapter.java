package com.example.cx.unlietaskdemo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.xstore.tms.android.R;
import com.xstore.tms.android.entity.net.ResponseReturnOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwenming1 on 2018/3/20.
 */

public class ReturnOrderListAdapter extends RecyclerView.Adapter<ReturnOrderListAdapter.ViewHolder> {

    private List<ResponseReturnOrder> data = new ArrayList<>();
    private List<ResponseReturnOrder> checkedData = new ArrayList<>();
    private boolean showRadioButton = false;
    private OnContainerItemClick onContainerItemClick;

    /**
     * 是否需要显示 Radio button
     * @param showRadioButton
     */
    public ReturnOrderListAdapter(boolean showRadioButton, OnContainerItemClick onContainerItemClick){
        this.showRadioButton = showRadioButton;
        this.onContainerItemClick = onContainerItemClick;
    }

    /**
     * 数据
     * @return
     */
    public List<ResponseReturnOrder> getData() {
        return this.data;
    }

    public void setData(List<ResponseReturnOrder> data) {
        this.data.clear();
        this.checkedData.clear();
        if (data != null && !data.isEmpty() ){
            this.data.addAll(data);
        }
        this.notifyDataSetChanged();
    }

    /**
     * 选中的数据
     * @return
     */
    public List<ResponseReturnOrder> getCheckedData() {
        return checkedData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.return_order_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResponseReturnOrder returnOrder = getData().get(position);
        if (showRadioButton) {
            //holder.getRb_checked().setOnCheckedChangeListener(holder);
        } else {
            holder.getRb_checked().setVisibility(View.GONE);
            holder.getItemView().setOnClickListener(this::onItemClick);
        }
        holder.getTv_return_order_no().setText(returnOrder.getRoNo());
        holder.getTv_order_id().setText(String.valueOf(returnOrder.getOrderId()));
        holder.getTv_phone_number().setText(returnOrder.getCustomerMobile());
        holder.getTv_address().setText(returnOrder.getAddress());
        holder.getRb_checked().setChecked(false);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void onItemClick(View view) {
        TextView tv = view.findViewById(R.id.tv_return_order_no);
        onContainerItemClick.onItemClick(tv.getText().toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_return_order_no;
        private TextView tv_order_id;
        private TextView tv_phone_number;
        private TextView tv_address;
        private CheckBox rb_checked;

        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tv_return_order_no = itemView.findViewById(R.id.tv_return_order_no);
            this.tv_order_id = itemView.findViewById(R.id.tv_order_id);
            this.tv_phone_number = itemView.findViewById(R.id.tv_phone_number);
            this.tv_address = itemView.findViewById(R.id.tv_address);
            this.rb_checked = itemView.findViewById(R.id.rb_checked);

            this.itemView = itemView;
            this.itemView.setOnClickListener(this);
            this.rb_checked.setOnClickListener(this);
        }

        public TextView getTv_return_order_no() {
            return tv_return_order_no;
        }

        public TextView getTv_order_id() {
            return tv_order_id;
        }

        public TextView getTv_phone_number() {
            return tv_phone_number;
        }

        public TextView getTv_address() {
            return tv_address;
        }

        public CheckBox getRb_checked() {
            return rb_checked;
        }

        public View getItemView() {
            return itemView;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            ResponseReturnOrder returnOrder = ReturnOrderListAdapter.this.getData().get(super.getAdapterPosition());
            if (this.itemView == v ){
                if (getRb_checked().isChecked()){
                    ReturnOrderListAdapter.this.getCheckedData().remove(returnOrder);
                    getRb_checked().setChecked(false);
                } else {
                    ReturnOrderListAdapter.this.getCheckedData().add(returnOrder);
                    getRb_checked().setChecked(true);
                }
            } else if (this.rb_checked == v) {
                if (getRb_checked().isChecked()){
                    ReturnOrderListAdapter.this.getCheckedData().add(returnOrder);
                } else {
                    ReturnOrderListAdapter.this.getCheckedData().remove(returnOrder);
                }
            }
        }
    }

    public interface OnContainerItemClick {
        void onItemClick(String roNo);
    }
}
