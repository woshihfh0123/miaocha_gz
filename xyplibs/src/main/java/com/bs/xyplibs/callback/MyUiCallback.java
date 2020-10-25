package com.bs.xyplibs.callback;

import android.app.Activity;

/**
 * Created by Administrator on 2018/5/14.
 */

public interface MyUiCallback<T> {
    public abstract void baseHasData();
    public abstract void baseNoData();
    public abstract void baseNoNet();
    public abstract void show();
//    public abstract void finishActivity(Activity activity, int code);
}
