package com.xstore.tms.android.ui.delivery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.leinyo.android.appbar.AppBar
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BaseTitleActivityKt
import com.xstore.tms.android.base.IBasePresenter
import com.xstore.tms.android.entity.delivery.DeliveryUnusualDataListEntity
import com.xstore.tms.android.entity.delivery.DeliveryUnusualListEntity
import com.xstore.tms.android.utils.ToastUtils
import com.xstore.tms.android.widget.DeliveryErrorReasonView
import com.xstore.tms.android.widget.DeliveryErrorReasonView.OverStepListener
import java.math.BigDecimal

/**
 * Created by hly on 2018/7/20.
 * email hly910206@gmail.com
 */
class DeliveryUnusualReasonActivity : BaseTitleActivityKt<IBasePresenter>(), OverStepListener{

    private lateinit var mUnusualEntity: DeliveryUnusualDataListEntity
    private var reasonMistakeView: DeliveryErrorReasonView? = null
    private var reasonLeakView: DeliveryErrorReasonView? = null
    private var reasonDamageView: DeliveryErrorReasonView? = null
    private var btn_Submit: Button? = null

    companion object {
        private const val KEY_ENTITY: String = "KEY_ENTITY"
        public const val REASON_REQUEST_CODE = 16597
        public const val REASON_RESULT_CODE = 16598

        //用companion object包裹方法，实现java中static的效果
        fun startDeliveryUnusualReasonActivity(context: Context, entity: DeliveryUnusualListEntity) {
            val intent = Intent(context, DeliveryUnusualReasonActivity::class.java)
            intent.putExtra(KEY_ENTITY, entity)
            context.startActivity(intent)
        }

        //用companion object包裹方法，实现java中static的效果
        fun startDeliveryUnusualReasonActivityForResult(context: Activity, entity: DeliveryUnusualDataListEntity) {
            val intent = Intent(context, DeliveryUnusualReasonActivity::class.java)
            intent.putExtra(KEY_ENTITY, entity)
            context.startActivityForResult(intent, REASON_REQUEST_CODE)
        }
    }

    override fun initData() {
        super.initData()

        if (intent != null) {
            mUnusualEntity = intent.getParcelableExtra(DeliveryUnusualReasonActivity.KEY_ENTITY)
        }

        reasonMistakeView = this.findViewById(R.id.rev_Mistake)
        reasonLeakView = this.findViewById(R.id.rev_Leak)
        reasonDamageView = this.findViewById(R.id.rev_Damage)
        btn_Submit = this.findViewById(R.id.btn_Submit)
        val totalCount : BigDecimal = mUnusualEntity.shippedQty!!
        reasonMistakeView!!.setOverStepListener(this)
        reasonLeakView!!.setOverStepListener(this)
        reasonDamageView!!.setOverStepListener(this)

        reasonMistakeView!!.setmNumber("0")
        reasonLeakView!!.setmNumber("0")
        reasonDamageView!!.setmNumber("0")

        if (mUnusualEntity.saleMode == 1) {

            val isMisChecked = mUnusualEntity.isMisCheck
            val isLeakChecked = mUnusualEntity.isLeakCheck
            val isDamageChecked = mUnusualEntity.isDamageCheck

            if (isMisChecked){
                reasonMistakeView!!.isChecked = true
                reasonMistakeView!!.setmNumber(mUnusualEntity.misTakeNum)
            }
            if (isLeakChecked){
                reasonLeakView!!.isChecked = true
                reasonLeakView!!.setmNumber(mUnusualEntity.leakeNum)
            }
            if (isDamageChecked){
                reasonDamageView!!.isChecked = true
                reasonDamageView!!.setmNumber(mUnusualEntity.damageNum)
            }


            var misTakeNum = reasonMistakeView!!.getmNumber()
            var leakeNum = reasonLeakView!!.getmNumber()
            var damageNum = reasonDamageView!!.getmNumber()

            var factTotal = BigDecimal.ZERO

            val bigMis = BigDecimal(misTakeNum)
            val bigLeak = BigDecimal(leakeNum)
            val bigDam = BigDecimal(damageNum)

            if (isMisChecked){
                factTotal = factTotal.add(bigMis)
            }
            if (isLeakChecked){
                factTotal = factTotal.add(bigLeak)
            }
            if (isDamageChecked){
                factTotal = factTotal.add(bigDam)
            }
            if (factTotal.compareTo(totalCount) >= 0 ){
                toggleInputStatu(true)
            }


            //等于1说明是计件的
            reasonMistakeView!!.setmTextWatch(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().equals("")){
                        return
                    }
                    val returnType = compareTotalNum(totalCount,BigDecimal(s.toString()))

                    var originTotal = totalCount

                    if (reasonLeakView!!.isChecked){
                        var leakeNum = reasonLeakView!!.getmNumber()
                        originTotal = originTotal.subtract(BigDecimal(leakeNum))

                    }
                    if (reasonDamageView!!.isChecked){
                        var damageNum = reasonDamageView!!.getmNumber()
                        originTotal = originTotal.subtract(BigDecimal(damageNum))
                    }

                    if (returnType == 1){

                        toggleInputStatu(true)

                        if(originTotal > BigDecimal.ZERO ){
                            ToastUtils.showToast("最多输入"+ originTotal.toInt().toString());
                            reasonMistakeView!!.setmNumber(originTotal.toInt().toString())
                        }else /*if (originTotal == BigDecimal.ZERO)*/{
                            ToastUtils.showToast("不能输入更多");
                            reasonMistakeView!!.setmNumber(originTotal.toInt().toString())
                        }
                    }
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
            reasonLeakView!!.setmTextWatch(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().equals("")){
                        return
                    }

                    val returnType = compareTotalNum(totalCount,BigDecimal(s.toString()))

                    var originTotal = totalCount

                    if (reasonMistakeView!!.isChecked){
                        var misNum = reasonMistakeView!!.getmNumber()
                        originTotal = originTotal.subtract(BigDecimal(misNum))

                    }
                    if (reasonDamageView!!.isChecked){
                        val damageNum = reasonDamageView!!.getmNumber()
                        originTotal = originTotal.subtract(BigDecimal(damageNum))
                    }

                    if (returnType == 1){
                        toggleInputStatu(true)


                        if(originTotal > BigDecimal.ZERO ){
                            ToastUtils.showToast("最多输入"+ originTotal.toInt().toString());
                            reasonLeakView!!.setmNumber(originTotal.toInt().toString())
                        }else /*if (originTotal == BigDecimal.ZERO)*/{
                            reasonLeakView!!.setmNumber(originTotal.toInt().toString())
                            ToastUtils.showToast("不能输入更多");
                        }
                    }/*else if (returnType == 0 || returnType == -1){
                        reasonLeakView!!.isChecked = true
                    }*/
                }

                override fun afterTextChanged(s: Editable) {

                }
            })
            reasonDamageView!!.setmTextWatch(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().equals("")){
                        return
                    }

                    val returnType = compareTotalNum(totalCount,BigDecimal(s.toString()))

                    var originTotal = totalCount

                    if (reasonMistakeView!!.isChecked){
                        var misNum = reasonMistakeView!!.getmNumber()
                        originTotal = originTotal.subtract(BigDecimal(misNum))

                    }
                    if (reasonLeakView!!.isChecked){
                        var leakNum = reasonLeakView!!.getmNumber()
                        originTotal = originTotal.subtract(BigDecimal(leakNum))
                    }

                    if (returnType == 1){
                        toggleInputStatu(true)
                        if(originTotal > BigDecimal.ZERO ){
                            ToastUtils.showToast("最多输入"+ originTotal.toInt().toString());
                            reasonDamageView!!.setmNumber(originTotal.toInt().toString())
                        }else /*if (originTotal == BigDecimal.ZERO)*/{
                            ToastUtils.showToast("不能输入更多");
                            reasonDamageView!!.setmNumber(originTotal.toInt().toString())
                        }

                    }/*else if (returnType == 0 || returnType == -1){
                        reasonDamageView!!.isChecked = true
                    }*/
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
            reasonMistakeView!!.isCountNum = true
            reasonLeakView!!.isCountNum = true
            reasonDamageView!!.isCountNum = true

            reasonMistakeView!!.setCheckListener(View.OnClickListener {
                val isChecked = reasonMistakeView!!.isChecked
                reasonMistakeView!!.isChecked = !isChecked

                val isLeakChecked = reasonLeakView!!.isChecked
                val isDamageChecked = reasonDamageView!!.isChecked

                if (isChecked){
                    reasonMistakeView!!.setmNumber("0")
                    toggleInputStatu(false)

//                    if (!isLeakChecked && !isDamageChecked){
//                        btn_Submit!!.isEnabled = false
//                    }

                }else{
                    btn_Submit!!.isEnabled = true
                    if (compareTotalNum(totalCount)){


                        var originTotal = totalCount

                        if (isLeakChecked){
                            var leakeNum = reasonLeakView!!.getmNumber()
                            originTotal = originTotal.subtract(BigDecimal(leakeNum))

                        }
                        if (isDamageChecked){
                            var damageNum = reasonDamageView!!.getmNumber()
                            originTotal = originTotal.subtract(BigDecimal(damageNum))
                        }

                        if(originTotal > BigDecimal.ZERO ){
                            reasonMistakeView!!.setmNumber(originTotal.toInt().toString())
                            ToastUtils.showToast("最多输入"+ originTotal.toInt());
                        }else /*if (originTotal == BigDecimal.ZERO)*/{
                            reasonMistakeView!!.setmNumber(originTotal.toInt().toString())
                            ToastUtils.showToast("不能输入更多");
                        }
                    }
                }

            })
            reasonLeakView!!.setCheckListener(View.OnClickListener {

                val isChecked = reasonLeakView!!.isChecked
                reasonLeakView!!.isChecked = !isChecked

                val isMisChecked = reasonMistakeView!!.isChecked
                val isDamageChecked = reasonDamageView!!.isChecked

                if (isChecked){
                    reasonLeakView!!.setmNumber("0")
                    toggleInputStatu(false)

//                    if (!isMisChecked && !isDamageChecked){
//                        btn_Submit!!.isEnabled = false
//                    }
                }else{
                    btn_Submit!!.isEnabled = true
                    if (compareTotalNum(totalCount)){

                        var originTotal = totalCount

                        if (isMisChecked){
                            var misNum = reasonMistakeView!!.getmNumber()
                            originTotal = originTotal.subtract(BigDecimal(misNum))

                        }
                        if (isDamageChecked){
                            var damageNum = reasonDamageView!!.getmNumber()
                            originTotal = originTotal.subtract(BigDecimal(damageNum))
                        }

                        if(originTotal > BigDecimal.ZERO ){
                            reasonLeakView!!.setmNumber(originTotal.toInt().toString())
                            ToastUtils.showToast("最多输入"+ originTotal.toInt());
                        }else /*if (originTotal == BigDecimal.ZERO)*/{
                            reasonLeakView!!.setmNumber(originTotal.toInt().toString())
                            ToastUtils.showToast("不能输入更多");
                        }
                    }
                }

            })
            reasonDamageView!!.setCheckListener(View.OnClickListener {
//                mUnusualEntity.checkIndex = 2
                val isChecked = reasonDamageView!!.isChecked
                reasonDamageView!!.isChecked = !isChecked

                val isMisChecked = reasonMistakeView!!.isChecked
                val isLeakChecked = reasonLeakView!!.isChecked

                if (isChecked){
                    reasonDamageView!!.setmNumber("0")
                    toggleInputStatu(false)
//                    if (!isMisChecked && !isLeakChecked){
//                        btn_Submit!!.isEnabled = false
//                    }
                }else{
                    btn_Submit!!.isEnabled = true
                    if (compareTotalNum(totalCount)){



                        var originTotal = totalCount

                        if (isMisChecked){
                            var misNum = reasonMistakeView!!.getmNumber()
                            originTotal = originTotal.subtract(BigDecimal(misNum))

                        }
                        if (isLeakChecked){
                            var leakNum = reasonLeakView!!.getmNumber()
                            originTotal = originTotal.subtract(BigDecimal(leakNum))
                        }

                        if(originTotal > BigDecimal.ZERO ){
                            reasonDamageView!!.setmNumber(originTotal.toInt().toString())

                            ToastUtils.showToast("最多输入"+ originTotal.toInt());
                        }/*else if (originTotal == BigDecimal.ZERO){
                            reasonDamageView!!.setmNumber(originTotal.toInt().toString())
                            ToastUtils.showToast("不能输入更多");
                        }*/else{
                            reasonDamageView!!.setmNumber(BigDecimal.ZERO.toString())
                            ToastUtils.showToast("不能输入更多");
                        }
                    }
                }
            })



        } else {
            reasonMistakeView!!.isCountNum = false
            reasonLeakView!!.isCountNum = false
            reasonDamageView!!.isCountNum = false
            reasonMistakeView!!.unit = mUnusualEntity.unit
            reasonLeakView!!.unit = mUnusualEntity.unit
            reasonDamageView!!.unit = mUnusualEntity.unit

            if (mUnusualEntity.checkIndex == 0){
                mistakeChecked()
            } else if (mUnusualEntity.checkIndex == 1){
                leakChecked()
            }else if (mUnusualEntity.checkIndex == 2){
                damageChecked()
            }

            reasonMistakeView!!.setCheckListener(View.OnClickListener {

                if (reasonMistakeView!!.isChecked){
                    mUnusualEntity.checkIndex = -1
                    reasonMistakeView!!.setmNumber("0")
                    reasonMistakeView!!.isChecked = false
                }else{
                    mUnusualEntity.checkIndex = 0
                    mistakeChecked()
                }
            })
            reasonLeakView!!.setCheckListener(View.OnClickListener {
                if (reasonLeakView!!.isChecked){
                    mUnusualEntity.checkIndex = -1
                    reasonLeakView!!.setmNumber("0")
                    reasonLeakView!!.isChecked = false
                }else {
                    mUnusualEntity.checkIndex = 1
                    leakChecked()
                }
            })
            reasonDamageView!!.setCheckListener(View.OnClickListener {
                if (reasonDamageView!!.isChecked){
                    mUnusualEntity.checkIndex = -1
                    reasonDamageView!!.setmNumber("0")
                    reasonDamageView!!.isChecked = false
                }else {
                    mUnusualEntity.checkIndex = 2
                    damageChecked()
                }
            })
        }

        btn_Submit!!.setOnClickListener(View.OnClickListener {
            if (mUnusualEntity.saleMode != 1 && mUnusualEntity.checkIndex == null){
                finish()

            }else if (mUnusualEntity.saleMode != 1 && mUnusualEntity.checkIndex == -1){
                val data = Intent()
                var bd = Bundle()
                bd.putString("misTakeNum", "0")
                bd.putString("leakeNum", "0")
                bd.putString("damageNum", "0")
                bd.putInt("index", -1)
                data.putExtras(bd)
                setResult(REASON_RESULT_CODE, data)
                finish()
            } else{

                var misTakeCheck = reasonMistakeView!!.isChecked
                var leakeCheck = reasonLeakView!!.isChecked
                var damageCheck = reasonDamageView!!.isChecked

                val data = Intent()
                var misTakeNum = reasonMistakeView!!.getmNumber()
                var leakeNum = reasonLeakView!!.getmNumber()
                var damageNum = reasonDamageView!!.getmNumber()
                var bd = Bundle()
                if (mUnusualEntity.saleMode == 1) {

                    misTakeNum = misTakeNum.replace(mUnusualEntity.unit!!, "", true)
                    leakeNum = leakeNum.replace(mUnusualEntity.unit!!, "", true)
                    damageNum = damageNum.replace(mUnusualEntity.unit!!, "", true)

                    var factTotal = BigDecimal.ZERO

                    val bigMis = BigDecimal(misTakeNum)
                    val bigLeak = BigDecimal(leakeNum)
                    val bigDam = BigDecimal(damageNum)

                    if (misTakeCheck){
                        factTotal = factTotal.add(bigMis)
                    }
                    if (leakeCheck){
                        factTotal = factTotal.add(bigLeak)
                    }
                    if (damageCheck){
                        factTotal = factTotal.add(bigDam)
                    }
                    if (factTotal.compareTo(totalCount) > 0 ){
                        ToastUtils.showToast("总数不能大于商品实际数量")

                        return@OnClickListener
                    }


                } else {
                    bd.putInt("index", mUnusualEntity.checkIndex!!)
                }
                val totalNum = BigDecimal(misTakeNum).add(BigDecimal(leakeNum)).add(BigDecimal(damageNum))
                if (totalNum > BigDecimal.ZERO){
                    if (misTakeCheck) {
                        bd.putString("misTakeNum", misTakeNum)
                    }else{
                        bd.putString("misTakeNum", "0")
                    }
                    if (leakeCheck){
                        bd.putString("leakeNum", leakeNum)
                    }else{
                        bd.putString("leakeNum", "0")
                    }
                    if (damageCheck) {
                        bd.putString("damageNum", damageNum)
                    }else{
                        bd.putString("damageNum", "0")
                    }
                }else{
                    bd.putString("misTakeNum", "0")
                    bd.putString("leakeNum", "0")
                    bd.putString("damageNum", "0")
                }
                data.putExtras(bd)
                setResult(REASON_RESULT_CODE, data)
                finish()
            }
        })

        var misTakeCheck = reasonMistakeView!!.isChecked
        var leakeCheck = reasonLeakView!!.isChecked
        var damageCheck = reasonDamageView!!.isChecked
        if (!misTakeCheck && !leakeCheck && !damageCheck){
            btn_Submit!!.isEnabled = false
        }else{
            btn_Submit!!.isEnabled = true
        }

    }

    fun mistakeChecked (){
        btn_Submit!!.isEnabled = true
        val shipQty = BigDecimal(mUnusualEntity.shippedQty.toString())
        val number  = shipQty.setScale(2,BigDecimal.ROUND_DOWN)
        reasonMistakeView!!.setmNumber(number.toString())
        reasonLeakView!!.setmNumber("0")
        reasonDamageView!!.setmNumber("0")

        reasonMistakeView!!.isChecked = true
        reasonLeakView!!.isChecked = false;
        reasonDamageView!!.isChecked = false;
    }

    fun leakChecked(){
        btn_Submit!!.isEnabled = true
        reasonMistakeView!!.setmNumber("0")
        val shipQty = BigDecimal(mUnusualEntity.shippedQty.toString())
        val number  = shipQty.setScale(2,BigDecimal.ROUND_DOWN)
        reasonLeakView!!.setmNumber(number.toString())
        reasonDamageView!!.setmNumber("0")

        reasonLeakView!!.isChecked = true
        reasonMistakeView!!.isChecked = false;
        reasonDamageView!!.isChecked = false;
    }

    fun damageChecked(){
        btn_Submit!!.isEnabled = true
        reasonMistakeView!!.setmNumber("0")
        reasonLeakView!!.setmNumber("0")
        val shipQty = BigDecimal(mUnusualEntity.shippedQty.toString())
        val number  = shipQty.setScale(2,BigDecimal.ROUND_DOWN)
        reasonDamageView!!.setmNumber(number.toString())

        reasonDamageView!!.isChecked = true
        reasonLeakView!!.isChecked = false;
        reasonMistakeView!!.isChecked = false;
    }

    fun compareTotalNum(totalCount : BigDecimal) : Boolean{
        val isMisChecked = reasonMistakeView!!.isChecked
        val isLeakChecked = reasonLeakView!!.isChecked
        val isDamageChecked = reasonDamageView!!.isChecked
        var factTotal = BigDecimal.ZERO

        var misTakeNum = reasonMistakeView!!.getmNumber()
        var leakeNum = reasonLeakView!!.getmNumber()
        var damageNum = reasonDamageView!!.getmNumber()

        val bigMis = BigDecimal(misTakeNum)
        val bigLeak = BigDecimal(leakeNum)
        val bigDam = BigDecimal(damageNum)

        if (isMisChecked){
            factTotal = factTotal.add(bigMis)
        }
        if (isLeakChecked){
            factTotal = factTotal.add(bigLeak)
        }
        if (isDamageChecked){
            factTotal = factTotal.add(bigDam)
        }
        if (factTotal.compareTo(totalCount) >= 0 ){
            return true
        }

        return false
    }

    fun compareTotalNum(totalCount : BigDecimal,cmpareNum : BigDecimal) : Int{
        val isMisChecked = reasonMistakeView!!.isChecked
        val isLeakChecked = reasonLeakView!!.isChecked
        val isDamageChecked = reasonDamageView!!.isChecked
        var factTotal = BigDecimal.ZERO
        var returnType = -1;
        if (!isMisChecked && !isLeakChecked && !isDamageChecked){
            factTotal = cmpareNum
            if (factTotal.compareTo(totalCount) > 0 ){
                returnType = 1
            }
            if (factTotal.compareTo(totalCount) == 0 ){
                returnType = 0
            }
        }else{

            var misTakeNum = reasonMistakeView!!.getmNumber()
            var leakeNum = reasonLeakView!!.getmNumber()
            var damageNum = reasonDamageView!!.getmNumber()

            val bigMis = BigDecimal(misTakeNum)
            val bigLeak = BigDecimal(leakeNum)
            val bigDam = BigDecimal(damageNum)

            if (isMisChecked){
                factTotal = factTotal.add(bigMis)
            }
            if (isLeakChecked){
                factTotal = factTotal.add(bigLeak)
            }
            if (isDamageChecked){
                factTotal = factTotal.add(bigDam)
            }
//            if (factTotal.compareTo(totalCount) > 0 ){
//                return true
//            }
            if (factTotal.compareTo(totalCount) > 0 ){
                returnType = 1
            }
            if (factTotal.compareTo(totalCount) == 0 ){
                returnType = 0
            }
        }

        return returnType
    }


    override fun getTitleViewConfig(): AppBar.TitleConfig {
        return buildDefaultConfig(getString(R.string.title_unusual_error_reason))
    }

    override fun getLayoutId(): Int {
        return R.layout.activitiy_error_reason
    }

    override fun toggleInputStatu(isOver: Boolean) {
        reasonMistakeView!!.isOverStep = isOver
        reasonLeakView!!.isOverStep = isOver
        reasonDamageView!!.isOverStep = isOver
    }

    override fun onChecked(isChecked: Boolean) {
        if (isChecked){
            btn_Submit!!.isEnabled = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}