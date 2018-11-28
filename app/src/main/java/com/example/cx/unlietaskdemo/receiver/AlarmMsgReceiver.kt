package com.xstore.tms.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xstore.tms.android.core.event.EventDispatchManager

class AlarmMsgReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.xstore.tms.android.clarmmsg") {
            Log.i("alarm", "时钟走了一分钟")
            EventDispatchManager.getInstance().dispatchEvent(
                    EventDispatchManager.Event(EventDispatchManager.EventType.EVENT_HINT_ALARM))
        }
    }
}
