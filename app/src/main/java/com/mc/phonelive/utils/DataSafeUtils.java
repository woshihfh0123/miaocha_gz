package com.mc.phonelive.utils;

import android.os.Build;
import android.support.v4.util.SimpleArrayMap;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * created by WWL on 2019/1/23 0023:15
 * 字段安全判断
 */
public class DataSafeUtils {

    public static boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (obj instanceof LongSparseArray
                    && ((LongSparseArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }




    public static boolean SafeObject(Object obj) {
        return obj == null;
    }

    /**
     * 字符串判断
     * @param obj
     * @return
     */
    public static String SafeString(Object obj) {
        if (SafeObject(obj)){
            return "";
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return "";
        }else{
            return (String) obj;
        }
    }

    /**
     * 替换字符串
     * @param obj
     * @param text
     * @return
     */
   public static String ReplaceString(Object obj, String text) {
        String ret = SafeString(obj);
        return ret.length()>0? ret: SafeString(text);
    }

    /**
     * 判断是否是数组
     * @param obj
     * @return
     */
    public static Object SafeArray(Object obj) {
        if (SafeObject(obj)){
            return "[]";
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return "[]";
        }else{
            return (Array) obj;
        }

    }

    /**
     * 判断是否是集合
     * @param obj
     * @return
     */
    public static Object SafeDictionary(Object obj) {
        if (SafeObject(obj)){
            return "{}";
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return "{}";
        }else{
            return obj;
        }
    }
}
