package com.xstore.tms.android.ui.delivery

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import com.leinyo.android.appbar.AppBar
import com.xstore.tms.android.R
import com.xstore.tms.android.adapter.DeliveryUnusualAdapter
import com.xstore.tms.android.adapter.MenuShowDialogAdapter
import com.xstore.tms.android.base.BaseTitleActivityKt
import com.xstore.tms.android.contract.DeliveryUnusualContract
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.core.location.LocationService
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.delivery.*
import com.xstore.tms.android.presenter.DeliveryUnusualPresenter
import com.xstore.tms.android.utils.DensityUtil
import com.xstore.tms.android.utils.JDMaUtils
import com.xstore.tms.android.utils.ToastUtils
import com.xstore.tms.android.widget.ImageDialog
import com.xstore.tms.android.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_delivery_unusual.*
import org.json.JSONObject
import java.lang.Exception
import java.math.BigDecimal

/**
 * Created by hly on 2018/7/19.
 * email hly910206@gmail.com
 */
class DeliveryUnusualActivity : BaseTitleActivityKt<DeliveryUnusualPresenter>(), DeliveryUnusualContract.IView, DeliveryUnusualAdapter.OnImageClickListener {

    private lateinit var mDetailEntity: ResponseExceptionDelivery
    private lateinit var mUnusualEntity: DeliveryUnusualEntity
    private var mDialog: Dialog? = null
    private var currentPos: Int = 0
    var dialogList: MutableList<DeliveryUnusualDataListEntity>? = null
    private lateinit var mBinder: LocationService.MyBinder

    companion object {
        private const val KEY_ENTITY: String = "KEY_ENTITY"

        //用companion object包裹方法，实现java中static的效果
        fun startDeliveryUnusualActivity(context: Context, entity: ResponseExceptionDelivery) {
            val intent = Intent(context, DeliveryUnusualActivity::class.java)
            intent.putExtra(KEY_ENTITY, entity)
            context.startActivity(intent)
        }
    }

    override fun getTitleViewConfig(): AppBar.TitleConfig = buildDefaultConfig(getString(R.string.title_unusual_delivery))


    override fun handSuccess() {
        mBinder.startLocation(mDetailEntity.storePoint, mDetailEntity.doNo)
        EventDispatchManager.getInstance().dispatchEvent(
                EventDispatchManager.Event(
                        EventDispatchManager.EventType.EVENT_DELIVERY_STATUS_CHANGED,
                        mDetailEntity.doNo.toString()))
        DeliveryListActivityKt.startDeliveryListActivity(this@DeliveryUnusualActivity)
    }

    override fun getLayoutId(): Int = R.layout.activity_delivery_unusual

    private val connection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {}

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mBinder = service as LocationService.MyBinder
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    override fun initData() {
        super.initData()
        if (intent != null) {
            mDetailEntity = intent.getParcelableExtra(DeliveryUnusualActivity.KEY_ENTITY)
        }
        startService(Intent(this, LocationService::class.java))
        bindService(Intent(this, LocationService::class.java), connection, Context.BIND_AUTO_CREATE)
        mUnusualEntity = mPresenter!!.buildUnusualEntity(mDetailEntity)
        val adapter = DeliveryUnusualAdapter(this, mUnusualEntity.list!!)
        adapter.mOnClickListener = this
        this.recyclerView.layoutManager = LinearLayoutManager(this)
        this.recyclerView.adapter = adapter
        this.recyclerView.addItemDecoration(SpaceItemDecoration(DensityUtil.dip2px(this, 10f)))

        initDialogView()
        this.bt_submit.setOnClickListener {

            if (dialogList!!.size > 0) {
                mDialog!!.show()
                val lp = WindowManager.LayoutParams()
                val window = mDialog!!.getWindow()
                lp.copyFrom(window.getAttributes())
                val scrrenWidth = DensityUtil.getScreenWidthpx(this)
                val dialogWidth = scrrenWidth * 0.8f + 0.5
                lp.width = dialogWidth.toInt()
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                window.setAttributes(lp)
            }else{
                ToastUtils.showToast(R.string.exception_selection)
            }
        }
    }

    override fun onClick(url: String) {
        val imageDialog = ImageDialog.createDialogFragment(url)
        imageDialog.show(this)
    }



    override fun onItemClick(position: Int, entity: DeliveryUnusualDataListEntity) {
        if (entity.abnormalCompleteFlag == 1){
            currentPos = position
            DeliveryUnusualReasonActivity.startDeliveryUnusualReasonActivityForResult(this@DeliveryUnusualActivity, entity)
        }
    }

    fun initDialogView() {
        mDialog = Dialog(this, R.style.BottomDialog)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.view_dialog, null)
        val diaListView = dialogView.findViewById<ListView>(R.id.lis_Product)
        val imb_DialogClose = dialogView.findViewById<ImageButton>(R.id.imb_DialogClose)
        val btn_CloseRoot = dialogView.findViewById<LinearLayout>(R.id.btn_CloseRoot)
        imb_DialogClose.setOnClickListener({
            mDialog!!.dismiss()
        })
        btn_CloseRoot.setOnClickListener({
            mDialog!!.dismiss()
        })
        mDialog!!.setCanceledOnTouchOutside(true)
        val submitBtn = dialogView.findViewById<Button>(R.id.bt_submit)
        submitBtn.setOnClickListener {

            var abnormalCompleteSkus: ArrayList<AbnormalCompleteSkus> = ArrayList<AbnormalCompleteSkus>()
            var i = 0
            while (i < dialogList!!.size) {
                var originBean: DeliveryUnusualDataListEntity = dialogList!!.get(i)
                var skuBean = AbnormalCompleteSkus()
                var reasons = ArrayList<ExceptionReason>()

                if (originBean.saleMode == 1) {
                    if (BigDecimal(originBean.misTakeNum) > BigDecimal.ZERO) {
                        var reasonf = ExceptionReason()
                        reasonf.abnormalNum = originBean.misTakeNum
                        reasonf.abnormalType = 1
                        reasons.add(reasonf)
                    }
                    if (BigDecimal(originBean.leakeNum) > BigDecimal.ZERO) {
                        var reasonS = ExceptionReason()
                        reasonS.abnormalNum = originBean.leakeNum
                        reasonS.abnormalType = 2
                        reasons.add(reasonS)
                    }
                    if (BigDecimal(originBean.damageNum) > BigDecimal.ZERO) {
                        var reasonT = ExceptionReason()
                        reasonT.abnormalNum = originBean.damageNum
                        reasonT.abnormalType = 3
                        reasons.add(reasonT)
                    }
                } else {
                    val index = originBean.checkIndex
                    var reasonW = ExceptionReason()
                    if (index == 0) {
                        reasonW.abnormalWeight = originBean.misTakeNum
                        reasonW.abnormalType = 1
                    } else if (index == 1) {
                        reasonW.abnormalWeight = originBean.leakeNum
                        reasonW.abnormalType = 2
                    } else if (index == 2) {
                        reasonW.abnormalWeight = originBean.damageNum
                        reasonW.abnormalType = 3
                    }
                    reasons.add(reasonW)
                }
                skuBean.abnormalCompleteDetails = reasons
                skuBean.skuId = originBean.skuId.toString()
                skuBean.skuUuid = originBean.skuUuid.toString()
                abnormalCompleteSkus.add(skuBean)
                i++
            }

            mPresenter!!.commitExceptionDelivery(mDetailEntity.doNo.toString(), abnormalCompleteSkus, mDetailEntity.customerDistance)
            mDialog!!.dismiss()
        }
        dialogList = ArrayList()
        val adapter = MenuShowDialogAdapter(this@DeliveryUnusualActivity, dialogList!!)
        diaListView.adapter = adapter
        mDialog!!.setContentView(dialogView)
        mDialog!!.setCancelable(false)
    }

    override fun onBackPressed() {
        if (mDialog!!.isShowing) {
            mDialog!!.dismiss()
        } else {
            super.onBackPressed()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DeliveryUnusualReasonActivity.REASON_REQUEST_CODE) {
            if (resultCode == DeliveryUnusualReasonActivity.REASON_RESULT_CODE) {
                if (data != null) {
                    var bd: Bundle = data!!.extras
                    val misTakeNum = bd.getString("misTakeNum")
                    val leakeNum = bd.getString("leakeNum")
                    val damageNum = bd.getString("damageNum")
                    val reasonBean = mUnusualEntity.list?.get(currentPos)
                    val reaseonUnit = reasonBean!!.unit

                    val str = StringBuffer()
                    if (BigDecimal(misTakeNum) > BigDecimal.ZERO) {
                        reasonBean.isMisCheck = true
                        str.append("商品错发:$misTakeNum$reaseonUnit")
                        str.append("  ")
                    } else {
                        reasonBean.isMisCheck = false
                    }
                    if (BigDecimal(leakeNum) > BigDecimal.ZERO) {
                        reasonBean.isLeakCheck = true
                        str.append("商品漏发:$leakeNum$reaseonUnit")
                        str.append("  ")
                    } else {
                        reasonBean.isLeakCheck = false
                    }

                    if (BigDecimal(damageNum) > BigDecimal.ZERO) {
                        reasonBean.isDamageCheck = true
                        str.append("商品破损:$damageNum$reaseonUnit")
                    } else {
                        reasonBean.isDamageCheck = false
                    }
                    reasonBean.reason = str.toString()


                    if (!reasonBean.isMisCheck && !reasonBean.isLeakCheck && !reasonBean.isDamageCheck) {
                        reasonBean.checkIndex = null
                        if (dialogList!!.contains(reasonBean)) {
                            dialogList!!.remove(reasonBean)
                        }
                    } else {

                        if (reasonBean.saleMode != 1) {
                            var checkIndex = bd.getInt("index")
                            if (checkIndex == -1) {
                                reasonBean.checkIndex = null
                            } else {
                                reasonBean.checkIndex = checkIndex
                            }
                        }

                        reasonBean.misTakeNum = misTakeNum
                        reasonBean.leakeNum = leakeNum
                        reasonBean.damageNum = damageNum


                        if (!dialogList!!.contains(reasonBean)) {
                            dialogList!!.add(reasonBean)
                        } else {
                            val index = dialogList!!.indexOf(reasonBean)
                            dialogList!!.set(index, reasonBean)
                        }
                    }
                    this.bt_submit.isEnabled = dialogList!!.size > 0

                    this.recyclerView.adapter.notifyItemChanged(currentPos)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}

