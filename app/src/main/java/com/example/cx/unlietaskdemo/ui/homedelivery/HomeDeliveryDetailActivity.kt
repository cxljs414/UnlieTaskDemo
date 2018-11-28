package com.xstore.tms.android.ui.homedelivery

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.design.widget.BottomSheetDialog
import android.view.View
import com.leinyo.android.appbar.AppBar
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BaseTitleActivityKt
import com.xstore.tms.android.contract.HomeDeliveryDetailContract
import com.xstore.tms.android.entity.home.HomeDeliveryOrder
import com.xstore.tms.android.presenter.HomeDeliveryDetailPresenter
import com.xstore.tms.android.utils.DateUtil
import com.xstore.tms.android.utils.ToastUtils
import com.xstore.tms.android.utils.onClick
import kotlinx.android.synthetic.main.activity_home_delivery_detail.*
import kotlinx.android.synthetic.main.dialog_reject.view.*
import org.apache.commons.lang3.StringUtils
import java.math.BigDecimal

/**
 * Created by hly on 2018/4/2.
 * email hly910206@gmail.com
 */
class HomeDeliveryDetailActivity : BaseTitleActivityKt<HomeDeliveryDetailPresenter>(), View.OnClickListener, HomeDeliveryDetailContract.IView{
    override fun getLayoutId(): Int = R.layout.activity_home_delivery_detail

    override fun getTitleViewConfig(): AppBar.TitleConfig = buildDefaultConfig(R.string.title_home_delivery_detail)

    private lateinit var mDetail: HomeDeliveryOrder

    companion object {
        private val KEY_ENTITY: String = "KEY_ENTITY"

        fun startHomeDeliveryDetailActivity(context: Context, entity: HomeDeliveryOrder) {
            val intent = Intent(context, HomeDeliveryDetailActivity::class.java)
            intent.putExtra(KEY_ENTITY, entity)
            context.startActivity(intent)
        }
    }

    override fun initData() {
        super.initData()
        if (intent != null) {
            mDetail = intent.getSerializableExtra(KEY_ENTITY) as HomeDeliveryOrder
        }
        this.tv_delivery_id.text = getString(R.string.home_delivery_id, mDetail.homeNo)
        this.tv_consignee.text = getString(R.string.item_delivery_list_consignee, mDetail.consigneeName)
        this.tv_mobile_phone.text = getString(R.string.item_delivery_list_mobilePhone, mDetail.consigneeMobile)
        this.tv_address.text = getString(R.string.item_delivery_list_address, mDetail.address ?: "")
        this.tv_count.text = getString(R.string.home_delivery_count, mDetail.totalQuantity ?: 0L)
        this.tv_order_create_time.text = getString(R.string.detail_create_order_time, DateUtil.dateToString(mDetail.underOrderTime, DateUtil.DATETIME_PATTERN))
        if (mDetail.leaveTime != null) {
            this.order_date.setData(BigDecimal(mDetail.leaveTime!!))
            this.order_date.visibility = View.VISIBLE
        } else {
            this.order_date.visibility = View.GONE
        }
        this.iv_close.setOnClickListener(this)
        this.iv_phone_button.setOnClickListener(this)
        this.tv_cancel.setOnClickListener(this)
        this.tv_confirm.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_close -> this.rl_tip.visibility = View.GONE
            R.id.iv_phone_button -> {
                if (StringUtils.isNotBlank(mDetail.homeNo))
                    mPresenter!!.decryptMobileNumber(homeNo = mDetail.homeNo!!)
            }
            R.id.tv_confirm -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_title)
                builder.setMessage(R.string.alert_delivery_send)
                builder.setPositiveButton(R.string.alert_confirm
                ) { _, _ ->
                    mPresenter!!.confirm(mDetail.homeNo!!, mDetail.storeId!!)
                }
                builder.setNegativeButton(R.string.cancel
                ) { _, _ -> }
                val dialog: AlertDialog = builder.show()

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            }
            R.id.tv_cancel -> {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = BottomSheetDialog(this)
        val view= View.inflate(this,R.layout.dialog_reject,null)
        view.tv_submit.onClick {
            val reason= when(view.rg_reason.checkedRadioButtonId){
                R.id.reason1 -> "客户不买了"
                R.id.reason2 -> "客户不在家"
                R.id.reason3 -> "商品有损或不全"
                else-> ""
            }
            if(reason.isEmpty()){
                ToastUtils.showToast(getString(R.string.please_choose_reason))
            }else{
                mPresenter!!.reject(mDetail.homeNo!!, reason, mDetail.storeId!!)
                dialog.dismiss()
            }
        }
        view.tv_cancel.onClick {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }

    override fun sendBack(isSuccess: Boolean) {
        ToastUtils.showToast(getString(R.string.submit_success))
        finish()
    }

    override fun backMobileNumber(mobileNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$mobileNumber")
        intent.data = data
        startActivity(intent)
    }
}