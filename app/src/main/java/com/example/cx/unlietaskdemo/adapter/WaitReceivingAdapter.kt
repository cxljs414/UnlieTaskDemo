package com.xstore.tms.android.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.leinyo.easy_refresh.widget.ptr.BaseRefreshAdapter
import com.leinyo.easy_refresh.widget.ptr.PullToRefreshView
import com.xstore.tms.android.R
import com.xstore.tms.android.entity.home.HomeDeliveryOrder
import kotlinx.android.synthetic.main.item_home_receive_list.view.*

/**
 * Created by hly on 2018/3/30.
 * email hly910206@gmail.com
 */
class WaitReceivingAdapter(context: Context, list: List<HomeDeliveryOrder>, refreshView: PullToRefreshView) : BaseRefreshAdapter<WaitReceivingAdapter.ViewHolder, HomeDeliveryOrder>(context, list, refreshView) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): WaitReceivingAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_receive_list, parent, false))
    }

    override fun onBindHolder(holder: WaitReceivingAdapter.ViewHolder, position: Int) {
        holder.bind(mDataList[position])
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
        private var checkbox: CheckBox = item.cb_check

        fun bind(entity: HomeDeliveryOrder) {
            orderId.text = mContext.getString(R.string.item_home_delivery_list_order_id, entity.homeId)
            consignee.text = mContext.getString(R.string.item_delivery_list_consignee, entity.consigneeName)
            mobilePhone.text = mContext.getString(R.string.item_delivery_list_mobilePhone, entity.consigneeMobile)
            address.text = mContext.getString(R.string.item_delivery_list_address, entity.address?: "")
            deliveryId.text = mContext.getString(R.string.item_home_delivery_list_id, entity.homeNo)
            checkbox.isChecked = entity.isCheck
            content.setOnClickListener{
                entity.isCheck = !entity.isCheck
                checkbox.isChecked = entity.isCheck
            }
        }
    }
}