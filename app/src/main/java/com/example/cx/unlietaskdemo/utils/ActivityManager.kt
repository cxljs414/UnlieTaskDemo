package com.xstore.tms.android.utils

import android.app.Activity

object ActivityManager {

    val activeActivities:MutableList<Activity> = ArrayList()

    fun exit(){
        activeActivities.forEach {
            it.finish()
        }
    }

    fun add(activity: Activity){
        activeActivities.add(activity)
    }

    fun remove(baseActivityKt: Activity) {
        activeActivities.remove(baseActivityKt)
    }
}