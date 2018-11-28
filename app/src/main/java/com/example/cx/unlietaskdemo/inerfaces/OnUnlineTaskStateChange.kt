package com.xstore.tms.android.inerfaces

interface OnUnlineTaskStateChange {
    fun onUnlineTaskSuccess(dono: String, type: String?)

    fun onUnlineTaskFailure(dono: String, type: String?)
}