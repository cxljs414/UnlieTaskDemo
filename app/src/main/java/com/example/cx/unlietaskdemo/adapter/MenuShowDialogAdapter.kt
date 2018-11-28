package com.xstore.tms.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.xstore.tms.android.R
import com.xstore.tms.android.entity.delivery.DeliveryUnusualDataListEntity
import com.xstore.tms.android.entity.delivery.DeliveryUnusualListEntity
import com.xstore.tms.android.utils.imageloader.GlideLoaderUtil
import java.math.BigDecimal

class MenuShowDialogAdapter(private val mContext: Context, private val mList: List<DeliveryUnusualDataListEntity>) : BaseAdapter() {



    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun getItem(position: Int): DeliveryUnusualDataListEntity {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            convertView = inflater.inflate(R.layout.dialog_item, null)
            holder.img_ProductPic = convertView!!.findViewById<View>(R.id.img_ProductPic) as ImageView
            holder.txt_ProductDescription = convertView.findViewById<View>(R.id.txt_ProductDescription) as TextView
            holder.txt_DeliveryMistake = convertView.findViewById<View>(R.id.txt_DeliveryMistake) as TextView
            holder.txt_DeliveryLeak = convertView.findViewById<View>(R.id.txt_DeliveryLeak) as TextView
            holder.txt_ProductDamage = convertView.findViewById<View>(R.id.txt_ProductDamage) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val deliveryBean = mList.get(position)
        holder.txt_ProductDescription?.setText(deliveryBean.productName)

        GlideLoaderUtil.loadNormalImage(mContext, deliveryBean.thumbUrl, R.drawable.ic_default_square_small, holder.img_ProductPic)

        var misNum = BigDecimal(deliveryBean.misTakeNum)
        if ( misNum.compareTo(BigDecimal("0")) != 0) {
            holder.txt_DeliveryMistake?.setText("商品错发:" + deliveryBean.misTakeNum + deliveryBean.unit)
            holder.txt_DeliveryMistake?.visibility = View.VISIBLE
        }else{
            holder.txt_DeliveryMistake?.visibility = View.GONE
        }
        var leaNum = BigDecimal(deliveryBean.leakeNum)
        if ( leaNum.compareTo(BigDecimal("0")) != 0) {
            holder.txt_DeliveryLeak?.setText("商品漏发:" + deliveryBean.leakeNum + deliveryBean.unit)
            holder.txt_DeliveryLeak?.visibility = View.VISIBLE
        }else{
            holder.txt_DeliveryLeak?.visibility = View.GONE
        }

        var damNum = BigDecimal(deliveryBean.damageNum)
        if ( damNum.compareTo(BigDecimal("0")) != 0) {
            holder.txt_ProductDamage?.setText("商品破损:" + deliveryBean.damageNum + deliveryBean.unit)
            holder.txt_ProductDamage?.visibility = View.VISIBLE
        }else{
            holder.txt_ProductDamage?.visibility = View.GONE
        }
        //        holder.typeTextview.setText(getItem(position));
        return convertView
    }


    class ViewHolder {
        var img_ProductPic: ImageView? = null
        var txt_ProductDescription: TextView? = null
        var txt_DeliveryMistake: TextView? = null
        var txt_DeliveryLeak: TextView? = null
        var txt_ProductDamage: TextView? = null
    }
}
