package com.xstore.tms.android.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.xstore.tms.android.core.event.EventDispatchManager

class ScreenReceiver :BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_SCREEN_ON ||
                intent?.action == Intent.ACTION_USER_PRESENT){
            EventDispatchManager.getInstance().dispatchEvent(
                    EventDispatchManager.Event(EventDispatchManager.EventType.EVENT_HINT_DELIVERYLIST))
        }
    }
}