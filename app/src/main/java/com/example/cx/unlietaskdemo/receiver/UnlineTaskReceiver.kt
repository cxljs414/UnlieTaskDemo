package com.xstore.tms.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.xstore.tms.android.inerfaces.OnUnlineTaskStateChange
import com.xstore.tms.android.utils.LogUtils

class UnlineTaskReceiver(var listener: OnUnlineTaskStateChange) : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        intent.let {
            var dono= intent?.getStringExtra("dono")
            var type= intent?.getStringExtra("type")
            LogUtils.i("UnlineTaskReceiver","action=${intent?.action} dono=$dono")
            dono.let {
                when(intent?.action){
                    "xstore_unlinetask_success"->{
                        listener.onUnlineTaskSuccess(dono!!,type)
                    }
                    "xstore_unlinetask_failure"->{
                        listener.onUnlineTaskFailure(dono!!,type)
                    }
                }
            }
        }
    }
}