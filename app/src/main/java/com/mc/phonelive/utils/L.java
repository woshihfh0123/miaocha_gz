package com.mc.phonelive.utils;

import android.util.Log;

import com.mc.phonelive.AppContext;

/**
 * Created by cxf on 2017/8/3.
 */

public class L {
    private final static String TAG = "tags--->";

    public static void e(String s) {
        e(TAG, s);
    }

    public static void e(String tag, String s) {
        if (AppContext.sDeBug) {
            Log.e(tag, s);
        }
    }
}
