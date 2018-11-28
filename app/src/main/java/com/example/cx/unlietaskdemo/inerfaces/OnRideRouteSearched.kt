package com.xstore.tms.android.inerfaces

import com.amap.api.services.route.RideRouteResult

/**
 * @Description
 * @Author ${user}
 * @Date $time$ $date$
 **/
interface OnRideRouteSearched {
    fun onSearched(result: RideRouteResult?, errorCode: Int)
}