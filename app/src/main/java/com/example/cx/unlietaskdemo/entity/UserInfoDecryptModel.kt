package com.xstore.tms.android.entity

data class UserInfoDecryptModel (
        /**
         * 随机字符
         */
        var tempStr: String,
        /**
         * MD5加密
         */
        var md5Str: String
)