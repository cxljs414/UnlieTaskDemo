package com.xstore.tms.android.entity.net.ro

/**
 * Created by wangwenming1 on 2018/3/22.
 */

class RequestTerminateOrConfirmRo {

    /**
     * 取件单单号
     */
    var roNo: String? = null

    /**
     * 终止原因
     */
    var termination: Array<String>? = null

    /**
     * 随机字符
     */
    var tempStr: String? = null
    /**
     * MD5加密
     */
    var md5Str: String? = null
}
