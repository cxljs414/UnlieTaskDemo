package com.xstore.tms.android.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.arseeds.ar.Qr.Image
import com.xstore.tms.android.R
import com.xstore.tms.android.entity.delivery.DeliveryUnusualDataListEntity
import com.xstore.tms.android.entity.delivery.DeliveryUnusualListEntity
import com.xstore.tms.android.utils.imageloader.GlideLoaderUtil
import kotlinx.android.synthetic.main.item_delivery_unusual.view.*

/**
 * Created by hly on 2018/7/20.
 * email hly910206@gmail.com
 */
class DeliveryUnusualAdapter(val mContext: Context,
                             private val mList: List<DeliveryUnusualDataListEntity>) : RecyclerView.Adapter<DeliveryUnusualAdapter.DeliveryUnusualHolder>() {
    var mOnClickListener: OnImageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryUnusualHolder {
        return DeliveryUnusualHolder(LayoutInflater.from(mContext).inflate(R.layout.item_delivery_unusual, parent, false), this)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: DeliveryUnusualHolder, position: Int) {
        holder.bind(position, mList[position])
    }

    class DeliveryUnusualHolder(
            val item: View,
            var parent: DeliveryUnusualAdapter) : RecyclerView.ViewHolder(item) {
        private val name: TextView = item.tv_name
        private val image: ImageView = item.iv_pic
        private val number: TextView = item.tv_number
        private val reason: TextView = item.tv_reason
        private var mListEntity: DeliveryUnusualDataListEntity? = null
        private var content: LinearLayout = item.ll_content
        private var reasonTitle: TextView = item.tv_reason_title
        private var img_More: ImageView = item.img_More

        fun bind(position: Int, listEntity: DeliveryUnusualDataListEntity) {
            this.mListEntity = listEntity
            name.text = listEntity.productName
            if (listEntity.unit == null){
                listEntity.unit = ""
            }
            number.text = "实际数量: ${listEntity.shippedQty}${listEntity.unit}"

            if (listEntity.abnormalCompleteFlag == 0){
                reasonTitle.setTextColor(parent.mContext.resources.getColor(R.color.color_red))
                reasonTitle.text = "该商品售后请联系客服 4006088768"
                img_More.visibility = View.GONE
            }else{
                reasonTitle.setTextColor(parent.mContext.resources.getColor(R.color.colorViewText00))
                img_More.visibility = View.VISIBLE
            }
            if (!TextUtils.isEmpty(listEntity.reason)) {
                reason.visibility = View.VISIBLE
                reason.text = listEntity.reason
            } else {
                reason.visibility = View.GONE
            }

            if (!TextUtils.isEmpty(listEntity.thumbUrl)) {
                GlideLoaderUtil.loadNormalImage(parent.mContext, listEntity.thumbUrl, R.drawable.ic_default_square_small, image)
            }
            if (!TextUtils.isEmpty(listEntity.imageUrl)) {
                image.setOnClickListener {
                    parent.mOnClickListener?.onClick(listEntity.imageUrl!!)
                }
            }
            this.content.setOnClickListener {
                parent.mOnClickListener?.onItemClick(position, listEntity)
            }
        }
    }

    public interface OnImageClickListener {
        fun onClick(url: String)

        fun onItemClick(position: Int, entity: DeliveryUnusualDataListEntity)
    }

}