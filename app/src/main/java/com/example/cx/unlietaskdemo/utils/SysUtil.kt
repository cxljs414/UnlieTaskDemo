package com.xstore.tms.android.utils


import com.xstore.tms.android.BuildConfig

/**
 * Created by wangwenming1 on 2018/3/26.
 */
object SysUtil {

    /**
     * 是否测试模式
     */
    fun isDebug() = BuildConfig.BUILD_TYPE == "debug"
}