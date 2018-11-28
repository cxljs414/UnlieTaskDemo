package com.xstore.tms.android.entity.common

/**
 * Created by wangwenming3 on 2018/04/11.
 * @description 解密查询实体
 */
data class ResponseDecryptMobile(
    var success: Boolean = false,
    var mobile: String? = null,
    var errorMsg: String? = null
)