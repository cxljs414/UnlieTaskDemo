package com.xstore.tms.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.xstore.tms.android.R
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.entity.delivery.DeliveryListEntity
import com.xstore.tms.android.utils.DensityUtil
import com.xstore.tms.android.widget.OrderDateView
import kotlinx.android.synthetic.main.item_delivery_list.view.*
import java.math.BigDecimal
import java.util.*

/**
 * Created by hly on 2018/3/19.
 * email hly910206@gmail.com
 */
class DeliveryListAdapterKt(
        val mContext: Context,
        private val mList: MutableList<DeliveryListEntity>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object {
        const val EMPTY_VIEW_TYPE = -1
        const val BOTTOM_VIEW_TYPE = -2
    }

    val selectedList = emptySet<DeliveryListEntity>().toMutableSet()

    private var mOnClickListener: OnItemClickListener? = null

    fun setClickListener(onClickListener: OnItemClickListener) {
        this.mOnClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < 0 || position >= mList.size)
            return
        (holder as? DeliveryListViewHolder)?.bind(mList[position],position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_VIEW_TYPE -> EmptyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_empty_recycler_view, parent, false))
            BOTTOM_VIEW_TYPE -> {
                val v = View(mContext)
                v.layoutParams = ViewGroup.LayoutParams(1, DensityUtil.dip2px(mContext, 40f))
                EmptyHolder(v)
            }
            else -> DeliveryListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_delivery_list, parent, false), this)
        }
    }

    override fun getItemViewType(position: Int) = when {
        mList.isEmpty() -> EMPTY_VIEW_TYPE
        position >= mList.size -> BOTTOM_VIEW_TYPE
        else -> super.getItemViewType(position)
    }

    override fun getItemCount(): Int = if (mList.isEmpty()) 1 else mList.size + 1

    fun setData(list: List<DeliveryListEntity>?) {
        if (mList is ArrayList) {
            mList.clear()
            if (list != null && list.isNotEmpty()) {
                mList.addAll(list)
            }
        }
        notifyDataSetChanged()
    }

    class DeliveryListViewHolder(
            val item: View,
            var parent: DeliveryListAdapterKt) : RecyclerView.ViewHolder(item) {
        private var orderId: TextView = item.tv_order_id
        private var mobilePhone: TextView = item.tv_mobile_phone
        private var address: TextView = item.tv_address
        private var content: LinearLayout = item.ll_content
        private var orderDate: OrderDateView = item.order_date
        private var check = item.cb_check
        private var ivJsd= item.iv_jsd
        private var tempTime:Long = 0
        private var typeIcon:ImageView = item.order_type_icon
        private var unlineing:TextView = item.unlineing
        private var tipContainer:LinearLayout = item.tipcontainer
        private var ivDkh:TextView = item.tv_dkh
        @SuppressLint("SetTextI18n")
        fun bind(entity: DeliveryListEntity, position: Int) {
            orderId.text = parent.mContext.getString(R.string.item_delivery_list_order_id, entity.doNo!!.toLong())
            mobilePhone.text = parent.mContext.getString(R.string.item_delivery_list_mobilePhone, entity.mobilePhone)
            address.text = parent.mContext.getString(R.string.item_delivery_list_address, entity.address)
            content.setOnClickListener {
                if (parent.mOnClickListener != null) {
                    parent.mOnClickListener!!.onClick(entity,position)
                }
            }
            if (entity.leaveTime == null) {
                orderDate.visibility = View.GONE
            } else {
                orderDate.visibility = View.VISIBLE
                orderDate.finish()
                if(tempTime == 0L){
                    tempTime = System.currentTimeMillis()
                    orderDate.setData(entity.leaveTime)
                }else{
                    var minutes = Math.round((System.currentTimeMillis() - tempTime).toDouble()/(1000*60)).toInt()
                    orderDate.setData(entity.leaveTime - BigDecimal(minutes))
                }
            }

            if (entity.callTime == null) {
                check.isEnabled = true
                check.setOnClickListener { v ->
                    if ((v as CheckBox).isChecked) parent.selectedList.add(entity) else parent.selectedList.remove(entity)
                }
                check.isChecked = parent.selectedList.contains(entity)
            } else {
                check.isChecked = false
                check.isEnabled = false
            }


            typeIcon.visibility = View.VISIBLE
            when(entity.sellerOrderSource){
                1 -> typeIcon.setImageResource(R.drawable.icon_7fresh)
                3 -> typeIcon.setImageResource(R.drawable.icon_jddj)
                else -> typeIcon.visibility = View.GONE
            }

            var isTipContainerShow = false
            if(entity.transportPriority != null && entity.transportPriority == 99){
                ivJsd.visibility = View.VISIBLE
                isTipContainerShow = true
            }else{
                ivJsd.visibility = View.GONE
                isTipContainerShow = false
            }

            /*if(position == 0){
                isTipContainerShow = true
                ivDkh.visibility = View.VISIBLE
            }else{
                ivDkh.visibility = View.GONE
            }
*/
            if(entity.unlineTaskState>0){
                unlineing.visibility = View.VISIBLE
                isTipContainerShow = true
                if(entity.unlineTaskState == 1){
                    unlineing.text = "正在执行离线任务"
                }else{
                    unlineing.text = "离线任务失败，请手动重试"
                }
            }else{
                unlineing.visibility = View.GONE
            }

            if(!isTipContainerShow){
                tipContainer.visibility = View.GONE
            }else{
                tipContainer.visibility = View.VISIBLE
            }


        }
    }


    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is DeliveryListViewHolder) {
            val view = holder.item.findViewById<OrderDateView>(R.id.order_date)
            view?.finish()
        }

    }

    fun removeItem(position: Int) {
        mList.removeAt(position)
        notifyDataSetChanged()
    }

    class EmptyHolder(item: View) : RecyclerView.ViewHolder(item) {
        init {
            item.findViewById<TextView>(R.id.tv_empty_notify_message)?.text = "暂无订单，快去扫描接单吧 ~"
        }
    }

    interface OnItemClickListener {
        fun onClick(entity: DeliveryListEntity,position:Int)
    }

}
