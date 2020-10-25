package com.bs.xyplibs.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by Administrator on 2018/7/4.
 */

public class CoypUtils {
    public  static void copy(Activity activity,String str){
        ClipboardManager cm = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(str);
    }
}
