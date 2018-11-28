package com.xstore.tms.android.base

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.jingdong.jdma.JDMaInterface
import com.xstore.tms.android.R
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.utils.ActivityManager
import com.xstore.tms.android.utils.LogUtils
import com.xstore.tms.android.widget.LoadingDialog
import java.lang.reflect.Constructor
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 * Created by hly on 2018/3/12.
 * email hly910206@gmail.com
 */
abstract class BaseActivityKt<T : IBasePresenter> : AppCompatActivity(),IBaseView,
        EventDispatchManager.SubscriberListener {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var currentActivity: AppCompatActivity? = null
            private set
    }

    val TAG: String = this.javaClass.simpleName

    var mPresenter: T? = null

    var mLoadingDialog: LoadingDialog? = null

    private val PERMISSON_REQUESTCODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivity = this
        ActivityManager.add(this)
        initWindows()

        if (getLayoutId() != 0) {
            setContentView(getLayoutId())
        }

        EventDispatchManager.getInstance().register(this)

        mPresenter = initPresenter()

        mPresenter?.setTag(TAG)

        initData()
    }

    override fun onResume() {
        super.onResume()
        currentActivity = this
        JDMaInterface.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        JDMaInterface.onPause()
    }

    abstract fun getLayoutId(): Int

    protected open fun initWindows() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.statusBarColor = Color.parseColor("#204560")
            //window.navigationBarColor = Color.parseColor("#204560")
        }
    }

    protected open fun initData() {

    }

    override fun onStop() {
        super.onStop()
        LogUtils.i(TAG,"onstop()。。。。")
        hideLoading()
    }

    override fun onDestroy() {
        if (currentActivity == this)
            currentActivity = null
        super.onDestroy()
        mPresenter?.destroy()
        ActivityManager.remove(this)
        EventDispatchManager.getInstance().unRegister(this)
    }

    private fun initPresenter(): T? {
        //获取泛型presenter
        if (javaClass.genericSuperclass is ParameterizedType) {

            val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>

            if (types.isInterface) {
                return null
            }
//            if (types.canonicalName == "com.xstore.tms.android.base.IBasePresenter") {
//                return null
//            }

            val viewTypes = javaClass.genericInterfaces
            var viewType: Class<*>? = null
            //获取IView
            for (type in viewTypes) {
                if ((type as Class<*>).`package`.name == "com.xstore.tms.android.contract") {
                    viewType = type
                }
            }
            val parameterTypes: Array<Class<*>>
            val constructor: Constructor<T>
            //实例化对象
            return if (viewType != null) {
                parameterTypes = arrayOf(viewType)
                constructor = types.getConstructor(*parameterTypes)
                constructor.newInstance(this)
            } else {
                types.newInstance()
            }
        }
        return null
    }

    /**
     * 检查权限
     */
    protected fun checkPermissions(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
            val needRequestPermissonList = findDeniedPermissions(permissions)
            if (needRequestPermissonList.isNotEmpty()) {
                val array = needRequestPermissonList.toTypedArray()
                val method = javaClass.getMethod("requestPermissions", Array<String>::class.java, Int::class.javaPrimitiveType)
                method.invoke(this, array, PERMISSON_REQUESTCODE)
            }
        }
    }


    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private fun findDeniedPermissions(permissions: Array<String>): List<String> {
        val needRequestPermissonList = ArrayList<String>()
        for (perm in permissions) {
            val checkSelfMethod = javaClass.getMethod("checkSelfPermission", String::class.java)
            val shouldShowRequestPermissionRationaleMethod = javaClass.getMethod("shouldShowRequestPermissionRationale",
                    String::class.java)
            if (checkSelfMethod.invoke(this, perm) as Int != PackageManager.PERMISSION_GRANTED || shouldShowRequestPermissionRationaleMethod.invoke(this, perm) as Boolean) {
                needRequestPermissonList.add(perm)
            }
        }
        return needRequestPermissonList
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(grantResults)) {
                showMissingPermissionDialog()
            }
        }
    }


    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.notifyTitle)
        builder.setMessage(R.string.notifyMsg)

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel
        ) { _, _ -> finish() }

        builder.setPositiveButton(R.string.setting
        ) { _, _ -> startAppSettings() }

        builder.setCancelable(false)

        builder.show()
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private fun startAppSettings() {
        val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    /**
     * 检测是否所有的权限都已经授权
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean {
        return grantResults.none { it != PackageManager.PERMISSION_GRANTED }
    }

    open override fun onEventMain(event: EventDispatchManager.Event) {

    }

    override fun showLoading() {
        if(null == mLoadingDialog){
            mLoadingDialog = LoadingDialog.createDialogFragment("正在请求中，请稍后...")
            mLoadingDialog?.isCancelable= false
        }
        if(!mLoadingDialog?.isAdded!!){
            var ft= supportFragmentManager.beginTransaction()
            ft.add(mLoadingDialog,TAG)
            ft.commitAllowingStateLoss()
            supportFragmentManager.executePendingTransactions()
        }
    }

    override fun hideLoading() {
        mLoadingDialog.let {
            mLoadingDialog?.dismissAllowingStateLoss()
            mLoadingDialog?.onDetach()
        }
    }

}