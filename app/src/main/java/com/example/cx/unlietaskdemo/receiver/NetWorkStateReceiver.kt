package com.xstore.tms.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.jd.ace.utils.NetworkUtil
import com.xstore.tms.android.core.event.EventDispatchManager

class NetWorkStateReceiver :BroadcastReceiver(){
    private val TAG= "NetWorkStateReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG,"网络状态改变")
        if(NetworkUtil.isConnected()){
            EventDispatchManager.getInstance().dispatchEvent(
                    EventDispatchManager.Event(EventDispatchManager.EventType.EVENT_HINT_NETWORK_ENABLE))
        }
    }
}