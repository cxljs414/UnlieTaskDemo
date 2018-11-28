package com.xstore.tms.android.ui

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import com.jd.push.lib.MixPushManager
import com.xstore.tms.android.AppApplication
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BaseActivityKt
import com.xstore.tms.android.contract.LoginContract
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.presenter.LoginPresenter
import com.xstore.tms.android.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import java.util.*


/**
 * Created by hly on 2018/3/12.
 * email hly910206@gmail.com
 */
class LoginActivityKt : BaseActivityKt<LoginPresenter>(), View.OnClickListener, LoginContract.IView {
    private var mIsExit = false
    companion object {
        var startIn = false
        fun startActivity() {
            if (startIn)
                return
            synchronized(LoginActivityKt::class.java) {
                if (startIn)
                    return
                startIn = true
                val activity = BaseActivityKt.currentActivity
                if (activity != null) {
                    activity.startActivity(Intent(activity, LoginActivityKt::class.java))
                } else {
                    AppApplication.instance!!.startActivity(Intent(AppApplication.instance!!.applicationContext, LoginActivityKt::class.java))
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        if (!TextUtils.isEmpty(LoginedCarrier.carrierPin)) {
            this.et_user_name.setText(LoginedCarrier.carrierPin)
        }
        this.version.text = "V${DeviceUtil.getAppVersionName(this)}"
        this.bt_login.setOnClickListener(this)
        et_user_name.disableCopyAndPaste()
        et_password.disableCopyAndPaste()
        showpass_check.onCheckChanged { et_password.showPassword(it) }

        MixPushManager.unBindClientId(this,LoginedCarrier.carrierPin)
        PreferencesUtils.safePut("l_c_token","")
        LoginedCarrier.token = ""
        TimeOutUtil.getInstance().clearAll()
    }

    override fun onStart() {
        super.onStart()
        checkPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA))
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bt_login -> {
                when {
                    et_user_name.text.isBlank() -> ToastUtils.showToast("用户名不允许为空")
                    et_password.text.isBlank() -> ToastUtils.showToast("密码不允许为空")
                    else -> mPresenter!!.login(et_user_name.text.toString().trim(), et_password.text.toString().trim())
                }
            }
        }
    }

    override fun login(success: Boolean, response: String) {
        if (success) {
            MixPushManager.bindClientId(this,et_user_name.text.toString())
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        startIn = false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun showFailCountTip(count: Int) {
        failure_count.visibility = View.VISIBLE
        if(count == 0){
            failure_count.text = getString(R.string.tip_login_diable)
        }else{
            failure_count.text = getString(R.string.tip_login_failure,count)
        }
        var animation = AnimationUtils.loadAnimation(this,R.anim.shake_lr)
        failure_count.startAnimation(animation)
        PreferencesUtils.put(et_user_name.text.toString(),"${DateUtil.dateToString(DateUtil.getNowDate())}:$count")
    }

    override fun hideFailCountTip() {
        failure_count.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        val tExit: Timer
        if (!mIsExit) {
            mIsExit = true // 准备退出
            ToastUtils.showToast(getString(R.string.app_back_hint))
            tExit = Timer()
            tExit.schedule(object : TimerTask() {
                override fun run() {
                    mIsExit = false // 取消退出
                }
            }, 2000) // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ActivityManager.exit()
        }
    }
}