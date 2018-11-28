package com.example.cx.unlietaskdemo.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xstore.tms.android.R;
import com.xstore.tms.android.core.TimeController;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;

/**
 * Created by hly on 2018/3/22.
 * email hly910206@gmail.com
 */

public class OrderDateView extends LinearLayout implements TimeController.TimeCallBack {
    private TextView mTvTitle, mTvContent;
    private TimeController mTimeController;
    private MyHandler mMyHandler;

    public OrderDateView(Context context) {
        this(context, null);
    }

    public OrderDateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderDateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_order_time, this, true);

        mTvTitle = view.findViewById(R.id.tv_title);
        mTvContent = view.findViewById(R.id.tv_content);
    }

    public void setData(BigDecimal mins) {
        if (mins == null) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            if (mins.intValue() > 0) {
                mTimeController = new TimeController();
                mMyHandler = new MyHandler(this);
                mTimeController.setTimeCallBack(this);
                mTimeController.setTime(mins);
            }else {
                setBackgroundResource(R.drawable.ic_timeover);
                mTvContent.setText("0分");
            }
        }
    }

    public void finish() {
        if (mTimeController != null) {
            mTimeController.finish();
            mTimeController = null;
        }

        if (mMyHandler != null) {
            mMyHandler.removeCallbacksAndMessages(null);
            mMyHandler = null;
        }
    }

    @Override
    public void getTime(String time) {
        if (mMyHandler != null) {
            Message message = mMyHandler.obtainMessage();
            message.obj = time;
            mMyHandler.sendMessage(message);
        }
    }

    public String curLeaveTime(){
        return mTvContent.getText().toString();
    }

    static class MyHandler extends Handler {
        private final WeakReference<OrderDateView> mActivity;

        private MyHandler(OrderDateView activity) {
            mActivity = new WeakReference<OrderDateView>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            OrderDateView activity = mActivity.get();
            if (activity != null) {
                int time = Integer.parseInt((String) msg.obj);

                if (time < 5) {
                    mActivity.get().setBackgroundResource(R.drawable.ic_timeover);
                } else {
                    mActivity.get().setBackgroundResource(R.drawable.ic_time);
                }

                mActivity.get().mTvContent.setText(time + "分");
                if (time <= 0) {
                    mActivity.get().finish();
                }

//                String[] times = time.split(":");
//                if (Integer.parseInt(times[1]) <= 5 && Integer.parseInt(times[0]) < 1) {
//                    mActivity.get().mTVHours.setBackgroundColor(Color.RED);
//                    mActivity.get().mTvMins.setBackgroundColor(Color.RED);
//                    mActivity.get().mTvSplit.setTextColor(Color.RED);
//                    mActivity.get().mTvTitle.setTextColor(Color.RED);
//                } else {
//                    mActivity.get().mTVHours.setBackgroundColor(Color.BLACK);
//                    mActivity.get().mTvMins.setBackgroundColor(Color.BLACK);
//                    mActivity.get().mTvSplit.setTextColor(Color.BLACK);
//                    mActivity.get().mTvTitle.setTextColor(Color.BLACK);
//                }
//                mActivity.get().mTVHours.setText(times[0]);
//                mActivity.get().mTvMins.setText(times[1]);
            }
        }
    }

}
