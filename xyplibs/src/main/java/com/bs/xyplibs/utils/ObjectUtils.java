package com.bs.xyplibs.utils;

/**
 * Created by Administrator on 2018/11/29.
 */

public class ObjectUtils {
    /**
     * Returns true if a and b are equal.
     * 如果a和b相等，返回TRUE
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(Object a, Object b) {
        return a == b || (a == null ? b == null : a.equals(b));
    }
}
