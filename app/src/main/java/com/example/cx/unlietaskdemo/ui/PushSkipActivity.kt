package com.xstore.tms.android.ui

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import com.xstore.tms.android.receiver.MyPushReceiver
import org.json.JSONException
import org.json.JSONObject


class PushSkipActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent:Intent  = getIntent();
        if(intent != null && intent.hasExtra("msg")) {
            //中转信息
            try {
                var pushJsonObj  = JSONObject(intent.getStringExtra("msg"))
                var msg:String  = pushJsonObj.optString("MSG")
                var msgObj = JSONObject(msg)
                var extras:String  = msgObj.optString("EXTRAS");
                var pushIntent:Intent  = MyPushReceiver.showIntent(this, extras)
                startActivity(pushIntent)
            } catch (e: JSONException) {
            } finally {
            }
        }
        finish()
    }
}
