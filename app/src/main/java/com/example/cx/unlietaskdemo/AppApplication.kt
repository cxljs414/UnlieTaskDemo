package com.xstore.tms.android

import android.support.multidex.MultiDexApplication
import com.jd.ace.Ace
import com.jd.ace.common.http.URLHelper
import com.jd.push.lib.MixPushManager
import com.jingdong.sdk.jdcrashreport.JDCrashReportConfig
import com.jingdong.sdk.jdcrashreport.JdCrashReport
import com.leinyo.httpclient.retrofit.HttpClient
import com.leinyo.httpclient.retrofit.HttpClientConfig
import com.tencent.bugly.crashreport.CrashReport
import com.xstore.tms.android.core.AppConfig
import com.xstore.tms.android.core.MyLifecycleHandler
import com.xstore.tms.android.core.WebRequestWrap
import com.xstore.tms.android.core.net.InterceptorStringFilter
import com.xstore.tms.android.core.net.LoadingDialogFilter
import com.xstore.tms.android.core.net.MyOkHttpConfigProvider
import com.xstore.tms.android.utils.*
import com.xstore.tms.taskunline.TaskDBManager


/**
 * Created by yao.zhai on 2017/7/12.
 */
class AppApplication : MultiDexApplication() {
    companion object {
        var instance: AppApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(MyLifecycleHandler())
        ToastUtils.init(this)
        PreferencesUtils.init(this)
        val httpClientConfig = HttpClientConfig
                .HttpClientBuilder(this, AppConfig.APP_ROOT_URL).OkHttpConfigProvider(MyOkHttpConfigProvider())
        HttpClient.getInstance().init(OtherUtils.setTimeOut(httpClientConfig.builder()))
        initRequestFilter()
        //设置筋斗云 HostType.HOST_TYPE_DEV:测试环境  HOST_TYPE_PROD:生产环境  HOST_TYPE_GATEWAY:正式
        Ace.init(this, URLHelper.HostType.HOST_TYPE_GATEWAY,false,true)
        initCrashReport()
        initJDCrashReport()
        TimeOutUtil.getInstance()
        SoundPoolUtil.getInstance()
        LocationxUtil.startLocation()
        MixPushManager.initConfiguration(this.applicationContext)
        JDMaUtils.init(this)
        TaskDBManager.init(this)
    }

    /**
     * 京东鹰眼
     */
    private fun initJDCrashReport() {
        var config= JDCrashReportConfig
                .Builder()
                .setContext(this)
                .setAppId("54ecbb68e57d5272b8d7b3c897b1e72f")
                .setReportDelay(60)
                .enableRecover(false)
                .addFilters("com.((jingdong.(?!aura.core))|jd.)\\S+", "\\S+jd.\\S+")//定位崩溃代码行的正则，按添加的顺序匹配，默认是包名
                .build()
        JdCrashReport.init(config)
        JdCrashReport.setCrashHandleCallback { _, _ ->
            var map:LinkedHashMap<String,String>  = linkedMapOf()
            map.put("xstore_test","test")
            return@setCrashHandleCallback map
        }
    }

    fun initCrashReport() {
        val packageName = packageName
        // 获取当前进程名
        val processName = DeviceUtil.getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(this)
        strategy.isUploadProcess = processName == null || processName == packageName
        // 初始化Bugly
        CrashReport.initCrashReport(this, "83e2ae04b0", BuildConfig.DEBUG, strategy)
    }

    /**
     * 初始化请求过滤器, 请参照业务按顺序将链插入集合中。
     */
    private fun initRequestFilter() {
        WebRequestWrap.accessChain.add(InterceptorStringFilter)
        WebRequestWrap.accessChain.add(LoadingDialogFilter)
    }

}
