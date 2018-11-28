package com.example.cx.unlietaskdemo.core;

import android.util.Log;

import com.xstore.tms.android.core.event.EventDispatchManager;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hly on 2018/7/27.
 * email hly910206@gmail.com
 */
public class TimeController implements EventDispatchManager.SubscriberListener{

    private BigDecimal mMinutes;
    private TimeCallBack mTimeCallBack;

    public void setTimeCallBack(TimeCallBack timeCallBack) {
        mTimeCallBack = timeCallBack;
    }


    public void setTime(BigDecimal time) {
        mMinutes = time;
        handleTime();
        EventDispatchManager.getInstance().register(this);
    }

    private void handleTime() {
        if (mMinutes.intValue() >= 1) {
            mMinutes = mMinutes.subtract(new BigDecimal(1));
        }
        if (mTimeCallBack != null) {
            mTimeCallBack.getTime(mMinutes.toString());
        }
    }

    public void finish() {
        if (mTimeCallBack != null) {
            mTimeCallBack = null;
        }
        EventDispatchManager.getInstance().unRegister(this);

    }

    @Override
    public void onEventMain(EventDispatchManager.Event event) {
        if(event.eventType == EventDispatchManager.EventType.EVENT_HINT_ALARM){
            handleTime();
        }
    }

    public interface TimeCallBack {
        void getTime(String time);
    }

}
