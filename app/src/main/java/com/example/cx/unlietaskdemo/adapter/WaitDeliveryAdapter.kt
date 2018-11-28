package com.xstore.tms.android.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.leinyo.easy_refresh.widget.ptr.BaseRefreshAdapter
import com.leinyo.easy_refresh.widget.ptr.PullToRefreshView
import com.xstore.tms.android.R
import com.xstore.tms.android.entity.home.HomeDeliveryOrder
import com.xstore.tms.android.widget.OrderDateView
import kotlinx.android.synthetic.main.item_home_delivery_list.view.*
import java.math.BigDecimal

/**
 * Created by hly on 2018/3/30.
 * email hly910206@gmail.com
 */
class WaitDeliveryAdapter(context: Context, list: List<HomeDeliveryOrder>, refreshView: PullToRefreshView) : BaseRefreshAdapter<WaitDeliveryAdapter.ViewHolder, HomeDeliveryOrder>(context, list, refreshView) {
    var mOnClick: OnClickListener? = null

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): WaitDeliveryAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_delivery_list, parent, false))
    }

    override fun onBindHolder(holder: WaitDeliveryAdapter.ViewHolder, position: Int) {
        holder.bind(mDataList.get(position))
    }

    fun setData(list: List<HomeDeliveryOrder>?) {
        (mDataList as ArrayList).clear()
        if (list != null) {
            mDataList.addAll(list)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var orderId: TextView = item.tv_order_id
        private var consignee: TextView = item.tv_consignee
        private var mobilePhone: TextView = item.tv_mobile_phone
        private var address: TextView = item.tv_address
        private var content: LinearLayout = item.ll_content
        private var deliveryId: TextView = item.tv_delivery_id
        private var orderDate = item.order_date

        fun bind(entity: HomeDeliveryOrder) {
            if (entity.leaveTime == null) {
                this.orderDate.visibility = View.GONE
            } else {
                orderDate.setData(BigDecimal(entity.leaveTime!!))
                this.orderDate.visibility = View.VISIBLE
            }
             orderId.text = mContext.getString(R.string.item_home_delivery_list_order_id, entity.homeId)
             consignee.text = mContext.getString(R.string.item_delivery_list_consignee, entity.consigneeName)
             mobilePhone.text = mContext.getString(R.string.item_delivery_list_mobilePhone, entity.consigneeMobile)
             address.text = mContext.getString(R.string.item_delivery_list_address, entity.address
                     ?: "")
             deliveryId.text = mContext.getString(R.string.item_home_delivery_list_id, entity.homeNo)
             if (mOnClick != null) {
                 content.setOnClickListener {
                     mOnClick!!.onClick(entity)
                 }
             }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is ViewHolder) {
            val view = holder.itemView.findViewById<OrderDateView>(R.id.order_date)
            view?.finish()
        }
    }

    interface OnClickListener {
        fun onClick(entity: HomeDeliveryOrder)
    }
}