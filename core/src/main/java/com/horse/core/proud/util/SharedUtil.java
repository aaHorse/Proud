package com.horse.core.proud.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.horse.core.proud.Proud;


/**
 * SharedPreferences封装
 *
 * @author liliyuan
 * @since 2020年4月7日16:52:57
 * */
public class SharedUtil {

    /**
     * 存储boolean类型
     * */
    public static void save(String key,boolean value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Proud.getContext()).edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    /**
     * 存储float类型
     * */
    public static void save(String key, float value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Proud.getContext()).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * 存储int类型
     * */
    public static void save(String key,int value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Proud.getContext()).edit();
        editor.putInt(key,value);
        editor.apply();
    }

    /**
     * 存储long类型
     * */
    public static void save(String key,long value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Proud.getContext()).edit();
        editor.putLong(key,value);
        editor.apply();
    }

    /**
     * 存储String类型
     * */
    public static void save(String key,String value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Proud.getContext()).edit();
        editor.putString(key,value);
        editor.apply();
    }

    /**
     * 读取boolean类型
     * */
    public static boolean read(String key, boolean defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Proud.getContext());
        return prefs.getBoolean(key, defValue);
    }

    /**
     * 读取float类型
     * */
    public static float read(String key, float defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Proud.getContext());
        return prefs.getFloat(key, defValue);
    }

    /**
     * 读取int类型
     * */
    public static int read(String key, int defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Proud.getContext());
        return prefs.getInt(key, defValue);
    }

    /**
     * 读取long类型
     * */
    public static long read(String key, long defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Proud.getContext());
        return prefs.getLong(key, defValue);
    }

    /**
     * 读取String类型
     * */
    public static String read(String key, String defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Proud.getContext());
        return prefs.getString(key, defValue);
    }

    /**
     * 判断SharedPreferences文件是否包含指定键值
     * */
    public static boolean contains(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Proud.getContext());
        return prefs.contains(key);
    }

    /**
     * 删除键值对
     * */
    public static void clear(String key) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Proud.getContext()).edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 清除SharedPreferences文件内容
     * */
    public static void clearAll() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Proud.getContext()).edit();
        editor.clear();
        editor.apply();
    }

}
