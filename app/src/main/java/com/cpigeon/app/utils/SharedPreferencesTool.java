package com.cpigeon.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antony on 2016/10/19.
 * SharedPreferences操作工具类
 */
public class SharedPreferencesTool {

    public static final String SP_FILE_LOGIN = "login";
    public static final String SP_FILE_APPUPDATE = "update";
    public static final String SP_FILE_ADUPDATE = "ad";
    public static final String SP_FILE_APPSETTING = "apcpSetting";// 应用配置
    public static final String SP_FILE_APPSTATE = "appState";// 应用状态
    public static final String SP_FILE_GUIDE = "pre_guide";
    public static final String SP_FILE_FUNCTION_GUIDE = "fun_guide";
    // 存储的sharedpreferences文件名
    private static final String SP_FILE_DEFAULT = "data";
    private static String TAG = "SharedPreferencesTool";

    /**
     * 保存数据到SharedPreferences
     *
     * @param context
     * @param key     节点名
     * @param data    值
     */
    public static void Save(Context context, String key, Object data) {
        SharedPreferencesTool.Save(context, key, data, "");
    }

    /**
     * 保存数据到SharedPreferences
     *
     * @param context
     * @param key      节点名
     * @param data     值
     * @param filename 文件名
     */
    public static void Save(Context context, String key, Object data, String filename) {

        if (filename.equals("")) filename = SharedPreferencesTool.SP_FILE_DEFAULT;
        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }
        editor.apply();
        Logger.i("Save();filename=" + filename + ";key=" + key
                + ";data=" + data);
    }

    /**
     * 批量保存数据到SharedPreferences
     *
     * @param context
     * @param key_data 数据键值对
     */
    private static void Save(Context context, Map<String, Object> key_data) {
        SharedPreferencesTool.Save(context, key_data, null);
    }

    /**
     * 批量保存数据到SharedPreferences
     *
     * @param context
     * @param key_data 数据键值对
     * @param filename SharedPreferences文件名
     */
    public static void Save(Context context, Map<String, Object> key_data, String filename) {
        if (TextUtils.isEmpty(filename)) filename = SharedPreferencesTool.SP_FILE_DEFAULT;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String type, key;
        Object data;
        for (int i = 0; i < key_data.size(); i++) {
            type = key_data.values().toArray()[i].getClass().getSimpleName();
            key = key_data.keySet().toArray()[i].toString();
            data = key_data.values().toArray()[i];
            if ("Integer".equals(type)) {
                editor.putInt(key, (Integer) data);
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean) data);
            } else if ("String".equals(type)) {
                editor.putString(key, (String) data);
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float) data);
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long) data);
            }

        }
        editor.apply();
    }

    /**
     * 从SharedPreferences中读取数据
     *
     * @param context
     * @param key      节点名
     * @param defValue 默认值(没有值将使用此值)
     * @return 获取的值
     */
    public static <T> T Get(Context context, String key, T defValue) {
        return Get(context, key, defValue, null);
    }

    /**
     * 从SharedPreferences中读取数据
     *
     * @param context
     * @param key      节点名
     * @param defValue 默认值(没有值将使用此值)
     * @param filename 文件名（不带后缀）
     * @return 获取的值
     */
    public static <T> T Get(Context context, String key, T defValue, String filename) {
        if (TextUtils.isEmpty(filename)) filename = SharedPreferencesTool.SP_FILE_DEFAULT;
        Object result = null;
        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                filename, Context.MODE_PRIVATE);

        // defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            result = sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            result = sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            result = sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            result = sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            result = sharedPreferences.getLong(key, (Long) defValue);
        }
        return (T) result;
    }

    /**
     * 批量从SharedPreferences中读取数据
     *
     * @param context
     * @param key_defValue 键值对
     * @return
     */
    public static Map<String, Object> Get(Context context, Map<String, Object> key_defValue) {
        return SharedPreferencesTool.Get(context, key_defValue, null);
    }

    /**
     * 批量从SharedPreferences中读取数据
     *
     * @param context
     * @param key_defValue 键值对
     * @param filename     SharedPreferences文件名
     * @return
     */
    public static Map<String, Object> Get(Context context, Map<String, Object> key_defValue, String filename) {
        if (TextUtils.isEmpty(filename)) filename = SharedPreferencesTool.SP_FILE_DEFAULT;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                filename, Context.MODE_PRIVATE);
        Object result = null, defValue;
        String type, key;
        for (int i = 0; i < key_defValue.size(); i++) {
            type = key_defValue.values().toArray()[i].getClass()
                    .getSimpleName();
            key = key_defValue.keySet().toArray()[i].toString();
            defValue = key_defValue.values().toArray()[i];
            if ("Integer".equals(type)) {
                result = sharedPreferences.getInt(key, (Integer) defValue);
            } else if ("Boolean".equals(type)) {
                result = sharedPreferences.getBoolean(key, (Boolean) defValue);
            } else if ("String".equals(type)) {
                result = sharedPreferences.getString(key, (String) defValue);
            } else if ("Float".equals(type)) {
                result = sharedPreferences.getFloat(key, (Float) defValue);
            } else if ("Long".equals(type)) {
                result = sharedPreferences.getLong(key, (Long) defValue);
            }
            resultMap.put(key, result);
        }
        return resultMap;
    }


}
