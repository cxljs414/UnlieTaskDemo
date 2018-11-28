package com.xstore.tms.android.ui.collection

import android.content.Intent
import android.graphics.Color
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import com.leinyo.android.appbar.AppBar
import com.xstore.tms.android.R
import com.xstore.tms.android.adapter.QSOrderListAdapter
import com.xstore.tms.android.base.BaseTitleActivityKt
import com.xstore.tms.android.contract.CollectionOrderContract
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.collection.ResponseReceiveOrRefuse
import com.xstore.tms.android.entity.delivery.ResponseCollectionOrder
import com.xstore.tms.android.presenter.CollectionOrderPresenter
import com.xstore.tms.android.utils.SysUtil
import com.xstore.tms.android.utils.ToastUtils
import com.xstore.tms.android.widget.BottomConfirmBar
import com.xstore.tms.android.widget.BottomConfirmBar.OnConfirmClickListener
import kotlinx.android.synthetic.main.activity_collection_order.*
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Created by wangwenming1 on 2018/3/31.
 */
class CollectionOrderActivity
    : BaseTitleActivityKt<CollectionOrderPresenter>(), CollectionOrderContract.IView, OnConfirmClickListener {

    companion object {
        /**
         * 请求码
         */
        val REQUEST_CODE = 0x12
        private val PARAM_KEY = "collectionId"
        fun startActivityForResult(from: AppCompatActivity, collectionId: String, requestCode: Int) {
            var intent = Intent(from, CollectionOrderActivity::class.java)
            intent.putExtra(PARAM_KEY, collectionId)
            from.startActivityForResult(intent, requestCode) //.startActivity(intent )
        }
    }

    private var adapter: QSOrderListAdapter = QSOrderListAdapter()

    private var collectionId: String? = null

    override fun getLayoutId(): Int = R.layout.activity_collection_order

    override fun getTitleViewConfig(): AppBar.TitleConfig = buildDefaultConfig("接单")

    override fun initData() {
        super.initData()
        this.bottom_confirm_bar.onConfirmListener = this

        this.rv_data_container.adapter = adapter
        this.rv_data_container.layoutManager = LinearLayoutManager(this)

//        if (SysUtil.isDebug() && StringUtils.isBlank(LoginedCarrier.token)) {
//            LoginedCarrier.initLoginInfo(null,"xnsjzhangminjuan", "8399488dbe0c441bb3ba4e81960f662f", null)
//        }
        // TODO: 传入集合单ID
        collectionId = intent.getStringExtra(PARAM_KEY)

//        if (collectionId == null) {
//            collectionId = "7FRESH-TC-9119429"
//        }
        if (collectionId != null) {
            mPresenter?.loadCollectionOrderList(collectionId!!)
        } else {
            ToastUtils.showToast("集合单号为空")
            finish()
        }
    }

    override fun onConfirmClick(v: View, action: Int) {
        when(action) {
            BottomConfirmBar.CANCEL -> {
                val b = BottomSheetDialog(this)
                val v = layoutInflater.inflate(R.layout.dialog_collection_order, null)
                val tv_close = v.findViewById<TextView>(R.id.tv_close)
                val rb_stockout = v.findViewById<RadioButton>(R.id.rb_stockout)
                val rb_damaged = v.findViewById<RadioButton>(R.id.rb_damaged)
                val bottom_confirm_bar_dialog = v.findViewById<BottomConfirmBar>(R.id.bottom_confirm_bar_dialog)
                tv_close.setOnClickListener({ b.hide() })
                bottom_confirm_bar_dialog.onConfirmListener = object : OnConfirmClickListener {
                    override fun onConfirmClick(v: View, action: Int) {

                        if(action == BottomConfirmBar.CONFIRM) {
                            var rejectList: String? = null
                            if (rb_stockout.isChecked) {
                                rejectList = rb_stockout.text.toString()
                            } else if (rb_damaged.isChecked) {
                                rejectList = rb_damaged.text.toString()
                            }

                            mPresenter?.refuseCollectionOrder(collectionId!!, rejectList)
                        }
                        b.hide()
                    }
                }
                b.setContentView(v)
                b.show()
            }
            BottomConfirmBar.CONFIRM -> {
                mPresenter?.receiveCollectionOrder(collectionId!!)
            }
        }
    }

    override fun collectionOrderListReady(responseCollectionOrder: ResponseCollectionOrder) {
        if (!responseCollectionOrder.success!!) {
            ToastUtils.showToast(responseCollectionOrder.errorMsg)
            finish()
            return
        }
        var text = "共${responseCollectionOrder.collectionInfo?.containerQuantity}箱，共${responseCollectionOrder.collectionInfo?.units}单"
        var sp = SpannableString(text)
        sp.setSpan(ForegroundColorSpan(Color.RED), 1, text.indexOf('箱'), 0)
        sp.setSpan(ForegroundColorSpan(Color.RED), text.indexOf('，') + 2, text.length - 1 , 0)
        this.tv_info.text = sp
        adapter.data.clear()
        adapter.data.addAll(responseCollectionOrder.deliveryOrderList!!)
        adapter.notifyDataSetChanged()
    }

    /**
     * 请求集合单异常
     */
    override fun collectionOrderListError(message: String) {
        ToastUtils.showToast(message)
        intent.putExtra("receive", false)
        setResult(REQUEST_CODE, intent)
        finish()
    }

    override fun receiveResponse(responseReceiveOrRefuse: ResponseReceiveOrRefuse) {
        if (responseReceiveOrRefuse.success) {
            ToastUtils.showToast("确认接单，操作成功")
            intent.putExtra("result", collectionId)
            intent.putExtra("receive", true)
            setResult(REQUEST_CODE, intent)
            finish()
        } else {
            ToastUtils.showToast("确认接单，操作失败（${responseReceiveOrRefuse.errorMsg}）")
        }
    }

    override fun refuseResponse(responseReceiveOrRefuse: ResponseReceiveOrRefuse) {
        if (responseReceiveOrRefuse.success) {
            ToastUtils.showToast("拒绝接单，操作成功")
            intent.putExtra("result", collectionId)
            intent.putExtra("receive", false)
            setResult(REQUEST_CODE, intent)
            finish()
        } else {
            ToastUtils.showToast("拒绝接单，操作失败（${responseReceiveOrRefuse.errorMsg}）")
        }
    }
}
