package com.xstore.tms.android.entity.net.performance

/**
 * Created by wangwenming1 on 2018/3/19.
 */

data class ResponseCompleteDoAndRoCount (
    /**
     * 已完成的妥投单数
     */
    var completeDoNum: Int? = 0,
    /**
     * 已经完成的取件单数
     */
    var completeRoNum: Int? = 0,
    /**
     * 已经完成的宅配妥投单数
     */
    var completeHomeNum: Int? = 0,
    /**
     * 已经完成的宅配拒收单数
     */
    var rejectHomeNum: Int? = 0,
    /**
     * 已经完成的拒收单数
     */
    var rejectDoNum: Int? = 0
)
