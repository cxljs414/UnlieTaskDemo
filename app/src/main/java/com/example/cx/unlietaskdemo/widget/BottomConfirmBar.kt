package com.xstore.tms.android.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.xstore.tms.android.R

/**
 * Created by wangwenming1 on 2018/3/31.
 */
class BottomConfirmBar(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    companion object {
        /**
         * 取消
         */
        val CANCEL: Int = 0
        /**
         * 确认
         */
        var CONFIRM: Int = 1
    }

    private val tv_cancel_button : TextView
    private val tv_confirm_button: TextView
    /**
     * 事件监听
     */
    var onConfirmListener: OnConfirmClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_confirm_bar, this, true)
        tv_cancel_button = findViewById(R.id.tv_cancel_button)
        tv_confirm_button = findViewById(R.id.tv_confirm_button)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.BottomConfirmBar)
        tv_cancel_button.text = attributes.getString(R.styleable.BottomConfirmBar_cancel_button_text)
        tv_confirm_button.text = attributes.getString(R.styleable.BottomConfirmBar_confirm_button_text)
        attributes.recycle()

        tv_cancel_button.setOnClickListener { onConfirmListener?.onConfirmClick(this, CANCEL) }
        tv_confirm_button.setOnClickListener{ onConfirmListener?.onConfirmClick(this, CONFIRM) }

    }

    interface OnConfirmClickListener {
        fun onConfirmClick(v: View, action: Int)
    }
}