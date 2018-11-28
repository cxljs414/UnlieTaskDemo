package com.example.cx.unlietaskdemo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class PackageUtils {
    /**
     * 取得版本号
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
//            .e(e.getLocalizedMessage());
        }
        return new PackageInfo();
    }

    /**
     * 获取build号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }


    /**
     * 判断应用是否已安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> list = pm
                .getInstalledPackages(PackageManager.PERMISSION_GRANTED);
        for (PackageInfo p : list) {
            if (packageName != null && packageName.equals(p.packageName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取Application的meta data.
     *
     * @param name
     * @return
     */
    public static String getMetadata(Context context, String name) {
        String meta_data = null;
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            meta_data = appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return meta_data;
    }
}
