package com.example.cx.unlietaskdemo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {
    private static Context mContext;
    private static String mOldMsg;
    protected static Toast mToast = null;
    private static long mOneTime = 0;
    private static long mTwoTime = 0;

    /**
     * 初始化Toast
     *
     * @param ctx 必须使用Application，否则会造成内存泄漏
     */
    public static void init(Context ctx) {
        mContext = ctx;
    }


    public static void showToast(int resId) {
        showToast(mContext.getString(resId));
    }

    public static void showToast(String s) {
        if (TextUtils.isEmpty(s)) {
            //s = " ";
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(mContext, s, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
            mOneTime = System.currentTimeMillis();
        } else {
            mTwoTime = System.currentTimeMillis();
            if (s.equals(mOldMsg)) {
                if (mTwoTime - mOneTime > Toast.LENGTH_SHORT) {
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                    mToast.show();
                }
            } else {
                mOldMsg = s;
                mToast.setText(s);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
            }
        }
        mOneTime = mTwoTime;
    }
}
