package com.xstore.tms.android.entity.home

/**
 * Created by wangwenming1 on 2018/3/30.
 */
data class Page<T>(
        var pageIndex: Long? = null,
        var pageSize: Long? = null,
        var totalItems: Long? = null,
        var totalPages: Long? = null,
        var result: List<T>? = null
)