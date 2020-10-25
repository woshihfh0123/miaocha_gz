package com.bspopupwindow.utils;

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
 * created by WWL on 2019/8/17 0017:11
 */
public class Utils {
    public static boolean isNotEmpty(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return false;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return false;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return false;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return false;
        }
        if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty()) {
            return false;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return false;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return false;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return false;
            }
        }
        if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (obj instanceof LongSparseArray
                    && ((LongSparseArray) obj).size() == 0) {
                return false;
            }
        }
        return true;
    }

}
