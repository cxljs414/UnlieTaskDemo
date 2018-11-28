package com.xstore.tms.android.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leinyo.easy_refresh.widget.ptr.BaseRefreshAdapter
import com.leinyo.easy_refresh.widget.ptr.PullToRefreshView
import com.xstore.tms.android.R
import com.xstore.tms.android.contract.PerformanceContract
import com.xstore.tms.android.entity.home.HomeDeliveryOrder
import com.xstore.tms.android.entity.net.performance.ResponseCompleteOrderItem
import com.xstore.tms.android.entity.net.performance.ResponseCompleteReturnOrderItem
import kotlinx.android.synthetic.main.activity_courier_perf_tab_item.view.*
import java.text.SimpleDateFormat

/**
 * Created by hly on 2018/3/30.
 * email hly910206@gmail.com
 */
class PerformanceAdapter(
        context: Context,
        list: List<PerformanceContract.ItemData>,
        refreshView: PullToRefreshView
) : BaseRefreshAdapter<PerformanceAdapter.ViewHolder, PerformanceContract.ItemData>(context, list, refreshView) {
    var mOnClick: OnClickListener? = null

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): PerformanceAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.activity_courier_perf_tab_item, parent, false))
    }

    override fun onBindHolder(holder: PerformanceAdapter.ViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    fun setData(list: List<PerformanceContract.ItemData>?, firstPage: Boolean) {
        if (firstPage)
            mDataList.clear()
        if (list != null) {
            mDataList.addAll(list)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(
            val item: View
    ) : RecyclerView.ViewHolder(item) {

        private val orderIdLabel = item.order_id_label
        private val orderId = item.order_id
        private val timeText = item.time_text
        private val timeLabel = item.time_label

        fun bind(entity: PerformanceContract.ItemData) {
            orderId.text = entity.number
            orderIdLabel.text = entity.numberLabel
            timeText.text = entity.time
            timeLabel.text = entity.timeLabel
            item.setOnClickListener({this@PerformanceAdapter.mOnClick!!.onClick(entity)})
        }
    }

    interface OnClickListener {
        fun onClick(entity: PerformanceContract.ItemData)
    }
}