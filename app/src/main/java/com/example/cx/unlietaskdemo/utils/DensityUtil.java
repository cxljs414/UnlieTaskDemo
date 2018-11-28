package com.example.cx.unlietaskdemo.utils;

import android.content.Context;
import android.view.View;

import java.math.BigDecimal;

/**
 * Created by hly on 2017/10/9.
 * email hly910206@gmail.com
 */

public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return new BigDecimal((double) dpValue * (double) scale + 0.5).intValue();
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return new BigDecimal((double) pxValue / (double) scale + 0.5).intValue();
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return new BigDecimal((double) pxValue / (double) fontScale + 0.5).intValue();
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return new BigDecimal((double)spValue * (double) fontScale + 0.5).intValue();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidthpx(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightpx(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }
}
