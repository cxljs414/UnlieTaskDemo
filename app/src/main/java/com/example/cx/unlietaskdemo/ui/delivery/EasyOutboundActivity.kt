package com.xstore.tms.android.ui.delivery

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BaseTitleActivityKt
import com.xstore.tms.android.base.IBasePresenter
import com.xstore.tms.android.utils.PreferencesUtils
import com.xstore.tms.android.utils.ToastUtils
import com.xstore.tms.android.widget.BottomConfirmBar
import kotlinx.android.synthetic.main.activity_easy_outbound.*
import org.apache.commons.lang3.StringUtils


/**
 * Created by wangwenming1 on 2018/3/20.
 */
class EasyOutboundActivity : BaseTitleActivityKt<IBasePresenter>(), BottomConfirmBar.OnConfirmClickListener {

    companion object {
        const val PARAM_IN_DO_COUNT = "param_in_do_count"
        const val FILE_NAME = "EASY_OUTBOUND_DATA"
        const val KEY_ADDRESS_LIST = "address-list"
        const val RESULT_KEY = "address"
        const val RESULT_CODE_SUCCESS = 1
        const val RESULT_CODE_CANCEL = 0

        fun startForResult(activity: Activity, requestCode: Int, doCount: Int) {
            val intent = Intent(activity, EasyOutboundActivity::class.java)
            intent.putExtra(PARAM_IN_DO_COUNT, doCount)
            activity.startActivityForResult(intent, requestCode)
        }

        /**
         * 缓存地址
         */
        fun cacheAddress(address: String) {
            val now = System.currentTimeMillis()

            val sp = PreferencesUtils.getSharedPreferences(FILE_NAME)

            val addressCache = sp.getString(KEY_ADDRESS_LIST, null)

            var addressList = if (StringUtils.isBlank(addressCache))
                ArrayList<CacheAddress>()
            else
                JSON.parseArray(addressCache, CacheAddress::class.java)

            var current = addressList.find { it.address == address }
            if (current != null) {
                current.time = now
            } else {
                addressList.add(CacheAddress(address, now))
            }
            /** 清理 */
            if(addressList.size > 30) {
                addressList = addressList.filter { (now - it.time!!) < 1000L * 3600 * 24 * 3 }
            }

            sp.edit().putString(KEY_ADDRESS_LIST, JSON.toJSONString(addressList)).commit()
        }
    }

    override fun getTitleViewConfig() = buildDefaultConfig("一键外呼")

    override fun getLayoutId() = R.layout.activity_easy_outbound

    override fun initData() {
        super.initData()
        var doCount = intent.getIntExtra(PARAM_IN_DO_COUNT, 0)
        this.et_outbound_address.hint = "共 $doCount 单请输入取餐地址"

        val sp = PreferencesUtils.getSharedPreferences(FILE_NAME)
        val addressCache = sp.getString(KEY_ADDRESS_LIST, null)
        var addressList = if (StringUtils.isBlank(addressCache))
            ArrayList<CacheAddress>()
        else
            JSON.parseArray(addressCache, CacheAddress::class.java)

        this.rv_address_list.adapter = Adapter(addressList.sortedByDescending { it.time }, this)
        this.rv_address_list.layoutManager = LinearLayoutManager(this)
        this.bottom_confirm_bar.onConfirmListener = this
    }

    override fun onConfirmClick(v: View, action: Int) {
        if(action == BottomConfirmBar.CANCEL) {
            setResult(RESULT_CODE_CANCEL)
            finish()
            return
        }
        if (StringUtils.isBlank(this.et_outbound_address.text)) {
            ToastUtils.showToast("请输入取餐地址")
            return
        }
        intent.putExtra(RESULT_KEY, this.et_outbound_address.text)
        setResult(RESULT_CODE_SUCCESS, intent)
        finish()
    }

    private fun setAddress(address: String) {
        this.et_outbound_address.setText(address)
    }

    private class Adapter(val data: List<CacheAddress>, val activity: EasyOutboundActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var v = LayoutInflater.from(parent.context).inflate(R.layout.item_easy_outbound_address, null)
            v.setOnClickListener { activity.setAddress( (it as TextView).text.toString()) }
            return Holder(v)
        }
        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is Holder) holder.address = data[position].address?: ""
        }

        private class Holder(val v: View) : RecyclerView.ViewHolder(v) {

            var address: String = ""
                set(value){
                    if (v is TextView)
                        v.text = value
                }
        }
    }

    /**
     * 地址缓存项
     */
    private data class CacheAddress (
            var address: String? = null,
            var time: Long? = null
    )
}
