package com.example.cx.unlietaskdemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PreferencesUtils {
    public static final String FILE_NAME = "qishou_tms_app";

    private static SharedPreferences sSharedPreferences;

    @SuppressLint("StaticFieldLeak")
    private static Context context = null;
    private static JniUtil jniUtil = null;
    public static void clear() {

    }

    public static void init(Context context) {
        PreferencesUtils.context = context;
        sSharedPreferences = getSharedPreferences(FILE_NAME);
        jniUtil = new JniUtil();
    }

    /**
     * 获取 sp.
     * @param fileName
     * @return
     */
    public static SharedPreferences getSharedPreferences(String fileName){
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 加密缓存
     * @param key
     * @param value
     */
    public static void safePut(String key, String value) {
        if (StringUtils.isBlank(value)) {
            remove(key);
        } else {
            putStr(key,jniUtil.encrypt(value));
        }
    }

    /**
     * 解密提取
     * @param key
     * @param defValue
     * @return
     */
    public static String safeGet(String key, String defValue){
        String value = getStr(key, defValue);
        if (StringUtils.isBlank(value)) {
            return defValue;
        } else {
            return jniUtil.decrypt(value);
        }
    }

    public synchronized static void putStr(String key, String value) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    public synchronized static String getStr(String key, String defValue) {
        return sSharedPreferences.getString(key, defValue);
    }


    public static void put(String key, Object object) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    public static void putStoreId(Long token) {
        put("store_id", token);
    }

    /**
     * 登入登录标记， 登录一天后失效
     *
     * @return
     */
    public static Long getStoreId() {
        return sSharedPreferences.getLong("store_id", 0);
    }

    public static void putToken(String token) {
        put("token", token);
    }

    /**
     * 登入登录标记， 登录一天后失效
     *
     * @return
     */
    public static String getToken() {
        return sSharedPreferences.getString("token", "");
    }

    /**
     * 登入的账户PIN，（配送员）
     *
     * @return
     */
    public static String getCarrierPin() {
        return sSharedPreferences.getString("carrierPin", "");
    }

    public static Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sSharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sSharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sSharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sSharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sSharedPreferences.getLong(key, (Long) defaultObject);
        }
        return null;
    }


    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clearAll() {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return sSharedPreferences.contains(key);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}  
