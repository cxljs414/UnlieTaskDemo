package com.xstore.tms.android.ui.delivery

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.CompoundButton
import com.amap.api.services.core.LatLonPoint
import com.leinyo.android.appbar.AppBar
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BaseTitleActivityKt
import com.xstore.tms.android.contract.DeliveryDetailContract
import com.xstore.tms.android.core.Const
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.delivery.DeliveryDetailEntity
import com.xstore.tms.android.entity.delivery.RequestDeliveryReject
import com.xstore.tms.android.entity.delivery.ResponseExceptionDelivery
import com.xstore.tms.android.presenter.DeliveryDetailPresenter
import com.xstore.tms.android.utils.JDMaUtils
import com.xstore.tms.android.utils.ToastUtils
import com.xstore.tms.android.utils.textChange
import com.xstore.tms.android.utils.textWatch
import kotlinx.android.synthetic.main.activity_refuse_reason.*
import org.json.JSONObject
import java.lang.Exception

/**
 * Created by hly on 2018/7/19.
 * email hly910206@gmail.com
 */
class DeliveryRefuseActivity : BaseTitleActivityKt<DeliveryDetailPresenter>(),
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener,
        DeliveryDetailContract.IView {

    private val mCheckSet: MutableSet<String> = mutableSetOf()
    private lateinit var mDetailEntity: DeliveryDetailEntity
    private var unlineFailureType:String?=null
    private lateinit var storePoint: LatLonPoint

    companion object {
        private const val KEY_ENTITY: String = "KEY_ENTITY"

        //用companion object包裹方法，实现java中static的效果
        fun startDeliveryRefuseActivity(context: Context, entity: DeliveryDetailEntity) {
            val intent = Intent(context, DeliveryRefuseActivity::class.java)
            intent.putExtra(KEY_ENTITY, entity)
            context.startActivity(intent)
        }
    }

    override fun getTitleViewConfig(): AppBar.TitleConfig = buildDefaultConfig(getString(R.string.title_refuse_reason))

    override fun getLayoutId(): Int = R.layout.activity_refuse_reason

    override fun initData() {
        super.initData()
        if (intent != null) {
            mDetailEntity = intent.getParcelableExtra(DeliveryRefuseActivity.KEY_ENTITY)
        }
        storePoint = LatLonPoint(mDetailEntity.store[0], mDetailEntity.store[1])
        mPresenter?.loadUnlineTask("${mDetailEntity.doNo}")
        this.cb_type1.setOnCheckedChangeListener(this)
        this.cb_type2.setOnCheckedChangeListener(this)
        this.cb_other.setOnCheckedChangeListener(this)
        this.bt_submit.setOnClickListener(this)
        this.et_count.textChange { updateButton() }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (buttonView.id == R.id.cb_other) {
            if (isChecked) {
//                cb_type1.isChecked = false
//                cb_type2.isChecked = false
                this.ll_other_reason.visibility = View.VISIBLE
//                mCheckSet.clear()
            } else {
                this.ll_other_reason.visibility = View.GONE
            }
        } else {
            if (isChecked) {
                if (buttonView.id == R.id.cb_type1) {
                    mCheckSet.add(buttonView.text.toString())
                } else if (buttonView.id == R.id.cb_type2) {
                    mCheckSet.add(buttonView.text.toString())
                }
//                cb_other.isChecked = false
//                this.ll_other_reason.visibility = View.GONE
            } else {
                if (buttonView.id == R.id.cb_type1) {
                    mCheckSet.remove(buttonView.text.toString())
                } else if (buttonView.id == R.id.cb_type2) {
                    mCheckSet.remove(buttonView.text.toString())
                }
            }
        }
        updateButton()
    }

    private fun updateButton() {
        val available: Boolean = !mCheckSet.isEmpty() || (this.cb_other.isChecked && this.et_count.text.isNotBlank())
        if (available) {
            this.bt_submit.setBackgroundColor(resources.getColor(R.color.colorTheme))
            this.bt_submit.setTextColor(resources.getColor(android.R.color.white))
        } else {
            this.bt_submit.setBackgroundColor(resources.getColor(R.color.colorAccent))
            this.bt_submit.setTextColor(resources.getColor(R.color.c2c2c2))
        }
        this.bt_submit.isEnabled = available
    }

    override fun onClick(v: View) {
        if(mDetailEntity.receivedTime !=null && System.currentTimeMillis() < mDetailEntity.receivedTime){
            ToastUtils.showToast(R.string.delivery_reject_time_exception)
        }else{
            showAlert()
        }
    }

    private fun showAlert(){
        val stringBuilder = StringBuilder()
        mCheckSet.forEach {
            stringBuilder.append(it).append(";")
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert_title)
        builder.setMessage(R.string.alert_delivery_reject)
        builder.setPositiveButton(R.string.alert_confirm
        ) { _, _ ->
            val stringReason = if (this.cb_other.isChecked && !TextUtils.isEmpty(this.et_count.text.toString().trim())) {
                getString(R.string.refuse_reason_post, stringBuilder.toString(), this.et_count.text.toString().trim())
            } else {
                getString(R.string.refuse_reason_post_no_other, stringBuilder.toString())
            }
            if(Const.UNLINETASK_TYPE_REJECT==unlineFailureType){
                //有离线失败任务
                mPresenter?.orderRejectUnlineTask(stringReason)
            }else{
                mPresenter?.orderReject(RequestDeliveryReject(mDetailEntity.collectionId, mDetailEntity.doNo.toString(), stringReason, mDetailEntity.customerDistance),storePoint)
            }
            try {
                var json = JSONObject()
                json.put("pin", LoginedCarrier.carrierPin)
                json.put("orderId",mDetailEntity.doNo.toString())
                json.put("collectionId",mDetailEntity.collectionId)
                json.put("stringReason",stringReason)
                json.put("customerDistance",mDetailEntity.customerDistance)
                JDMaUtils.sendCommonData(this,
                        "xstore_tms_1540273172816|6",
                        json.toString(),
                        "orderReject",
                        this,
                        mDetailEntity.doNo.toString(),
                        "",
                        "DeliveryRefuseActivity")

            }catch (e:Exception){

            }
        }

        builder.setNegativeButton(R.string.cancel
        ) { _, _ -> }

        builder.setCancelable(false)
        val dialog: AlertDialog = builder.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    override fun orderReject(result: Boolean) {
        if (result) {
            EventDispatchManager.getInstance().dispatchEvent(
                    EventDispatchManager.Event(
                            EventDispatchManager.EventType.EVENT_DELIVERY_STATUS_CHANGED,
                            mDetailEntity.doNo.toString()))
            DeliveryListActivityKt.startDeliveryListActivity(this)
        }
    }

    override fun showUnlineFailureTip(type: String?) {
        unlineFailureType = type
        if(type!= null && "reject" == type){
            unlinetasktip.visibility = View.VISIBLE
        }else{
            unlinetasktip.visibility = View.GONE
        }
    }

}