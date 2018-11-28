package com.example.cx.unlietaskdemo.core.gilde;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by hly on 2017/8/15.
 * email hly910206@gmail.com
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //获取系统分配给应用的总内存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //设置图片内存缓存占用八分之一
        int memoryCacheSize = maxMemory / 8;
        //设置内存缓存大小
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
    }
}
