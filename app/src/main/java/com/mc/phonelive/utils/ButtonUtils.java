package com.mc.phonelive.utils;

import android.util.Log;

public class ButtonUtils {
    private static long lastClickTime = 0;
    private static long DIFF = 1000;
    private static long DIFF2 = 2000;
    private static long DIFF1 = 3000;
    private static int lastButtonId = -1;

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }
    public static boolean isFastDoubleClick02() {
        return isFastDoubleClick(-1, DIFF2);
    }
    public static boolean isFastDoubleClick01() {
        return isFastDoubleClick(-1, DIFF1);
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }
    /**
     * 判断两次点击的间隔，如果小于2000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick02(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF2);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            Log.v("isFastDoubleClick", "短时间内按钮多次触发");
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }


    public static boolean isOutTimeDoubleShow(long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( lastClickTime > 0 && timeD < diff) {
            Log.v("isFastDoubleClick", "短时间内按钮多次触发");
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
