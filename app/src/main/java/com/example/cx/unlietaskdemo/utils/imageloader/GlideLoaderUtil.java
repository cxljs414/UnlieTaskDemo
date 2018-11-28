package com.example.cx.unlietaskdemo.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.xstore.tms.android.R;
import com.xstore.tms.android.core.gilde.GlideApp;

import java.io.File;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

public class GlideLoaderUtil {

    /**
     * 加载指定宽高的图片，使用时只注意ImageView的宽高即可，底层自动获取需要加载的宽高
     *
     * @param mContext
     * @param url
     * @param defaultId
     * @param iv
     */
    public static void loadFullWidthImage(final Context mContext, String url, final int defaultId, final ImageView iv) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defaultId == -1 ? R.drawable.bg_gray_shape : defaultId);
            return;
        }

        GlideApp.with(mContext).asBitmap().load(url).dontAnimate().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                int width = resource.getWidth();
                int height = resource.getHeight();
                int swidth = mContext.getResources().getDisplayMetrics().widthPixels;
                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                layoutParams.height = new BigDecimal(height *swidth / width).intValue();
                iv.setLayoutParams(layoutParams);
                iv.setImageBitmap(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                iv.setImageResource(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId);
            }
        });
    }

    /**
     * 加载普通图片
     *
     * @param mContext
     * @param url
     * @param defaultId 为-1时加载灰色图片
     * @param iv
     */
    public static void loadNormalImage(Context mContext, String url, int defaultId, ImageView iv) {
        loadNormalImage(mContext, url, defaultId, iv, true, null);
    }

    /**
     * 如果需要在图片加载成功之后做回调用此方法
     * 默认全屏 fitCenter
     *
     * @param mContext
     * @param url
     * @param defaultId
     * @param iv
     * @param cache
     * @param callback
     */
    public static void loadNormalImage(Context mContext, String url, final int defaultId, final ImageView iv, boolean cache, final LoadImageCallback callback) {
        if (mContext == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId);
            return;
        }
        GlideApp.with(mContext).asBitmap()
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId)
                .error(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId)
//                .override(DensityUtil.getScreenWidthpx(mContext), DensityUtil.getScreenHeightpx(mContext))
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        iv.setImageResource(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            iv.setImageBitmap(resource);
                            if (callback != null && iv.getDrawable() != null) {
                                callback.onBitmapLoaded(resource);
                            }
                        }
                    }
                });
    }

    /**
     * 加载普通图片
     * 无默认大小无scaleType
     *
     * @param mContext
     * @param url
     * @param defaultId 为-1时加载灰色图片
     * @param iv
     */
    public static void loadNormalImageWithBase(Context mContext, String url, int defaultId, ImageView iv) {
        loadNormalImageWithBase(mContext, url, defaultId, iv, true, null);
    }

    public static void loadNormalImageWithBase(Context mContext, String url, final int defaultId, final ImageView iv, boolean cache, final LoadImageCallback callback) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId);
            return;
        }
        GlideApp.with(mContext).asBitmap()
                .load(url)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId)
                .error(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId)
                // .override(DeviceUtils.getScreenWH(mContext)[0],DeviceUtils.getScreenWH(mContext)[1])
                //.fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        iv.setImageResource(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId);
                    }

                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        if (bitmap != null) {
                            iv.setImageBitmap(bitmap);
                            if (callback != null && iv.getDrawable() != null) {
                                callback.onBitmapLoaded(bitmap);
                            }
                        }
                    }
                });
    }

    public static void loadNormalPreview(Context mContext, String url, final int defaultId, final ImageView iv, final LoadImageCallback callback) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId);
            return;
        }
        GlideApp.with(mContext).asBitmap()
                .load(url)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId)
                .error(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        iv.setImageResource(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId);
                    }

                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        if (bitmap != null) {
                            iv.setImageBitmap(bitmap);
                            if (callback != null && iv.getDrawable() != null) {
                                callback.onBitmapLoaded(bitmap);
                            }
                        }
                    }
                });
    }


    /**
     * 加载用户正常方形头像时使用该函数
     *
     * @param mContext
     * @param avatarUrl
     * @param iv
     */
    public static void loadSquareAvatar(Context mContext, String avatarUrl, int defaultId, ImageView iv) {
        if (TextUtils.isEmpty(avatarUrl)) {
            iv.setImageResource(defaultId == -1 ? R.drawable.ic_default_user_icon : defaultId);
            return;
        }

        GlideApp.with(mContext).asBitmap()
                .load(avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(defaultId == -1 ? R.drawable.ic_default_user_icon : defaultId)
                .error(defaultId == -1 ? R.drawable.ic_default_user_icon : defaultId)
                .into(iv);
    }


    public static void loadCenterCrop(Context mContext, String url, int defaultId, ImageView iv) {
        if (mContext == null || iv == null) return;//2016/6/6  HongyangJia
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId);
            return;
        }
        GlideApp.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .placeholder(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .error(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .into(iv);
    }


    /**
     * 加载圆角矩形图片
     *
     * @param mContext
     * @param url
     */
    public static void loadRoundCornerImage(Context mContext, String url, int defaultId, int radiusDp, ImageView image) {

        if (TextUtils.isEmpty(url)) {
            image.setImageResource(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId);
            return;
        }

        GlideApp.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(new GlideRoundTransform(mContext, radiusDp))
                .placeholder(defaultId)
                .error(defaultId)
                .into(image);
    }

    public static void loadImageWithPath(Context mContext, String path, int defaultId, ImageView image) {
        GlideApp.with(mContext)
                .load(new File(path))
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .error(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .into(image);
    }

    /**
     * 加载圆形图片
     *
     * @param mContext
     * @param url
     */
    public static void loadCircleImage(Context mContext, String url, int defaultId, ImageView image, boolean isUser) {

        if (TextUtils.isEmpty(url)) {
            if (isUser) {
                image.setImageResource(R.drawable.ic_default_user_icon);
                return;
            }
            image.setImageResource(R.drawable.ic_default_user_icon);
            return;
        }

        GlideApp.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(new CircleTransform(mContext))
                .placeholder(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .error(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .into(image);
    }


    /**
     * 下载一张图片
     *
     * @param ctx
     * @param url
     * @return
     */
    public static Bitmap downloadImage(Context ctx, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            return GlideApp.with(ctx).asBitmap().load(url).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void loadNormalView(Context mContext, String url, final int defaultId, final View iv) {
        if (TextUtils.isEmpty(url)) {
            iv.setBackgroundResource(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId);
            return;
        }
        GlideApp.with(mContext).asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId)
                .error(defaultId == -1 ? R.drawable.ic_default_square_small : defaultId)
                // .override(DeviceUtils.getScreenWH(mContext)[0],DeviceUtils.getScreenWH(mContext)[1])
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        iv.setBackgroundResource(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId);
                    }

                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        if (bitmap != null) {
                            iv.setBackgroundDrawable(new BitmapDrawable(bitmap));
                        }
                    }
                });
    }
}
