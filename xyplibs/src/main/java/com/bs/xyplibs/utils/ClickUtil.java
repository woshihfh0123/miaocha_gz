package com.bs.xyplibs.utils;

/**
 * Created by Administrator on 2018/11/29.
 */

public class ClickUtil {
    // 上次点击时间
    private static long sLastTime;

    /**
     * 判断此次点击是否响应
     *
     * @return 响应则返回true，否则返回false
     */
    public static boolean isClick() {

        long time = DateUtils.getCurTimeMills();
        if (sLastTime > time || time - sLastTime > 500) {
            synchronized (ClickUtil.class) {
                if (sLastTime > time || time - sLastTime > 500) {
                    sLastTime = time;
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
