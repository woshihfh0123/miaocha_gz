package com.bs.xyplibs.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * -----老铁亲测可用----2019--04--13---
 * 此类不仅可以解决软键盘遮挡视图问题
 * 还可以解决导航栏遮挡视图问题
 * 最主要是适配了变态的华为机器
 *
 *
 *  setlayoutview(R.layout.activity_webview);
 AndroidBug5497Workaround1.assistActivity(findViewById(android.R.id.content));
 *
 */

public class AndroidBug5497Workaround1 {
    public static void assistActivity(View viewObserving) {
        new AndroidBug5497Workaround1(viewObserving);
    }

    private View mViewObserved;//被监听的视图
    private int usableHeightPrevious;//视图变化前的可用高度
    private ViewGroup.LayoutParams frameLayoutParams;

    private AndroidBug5497Workaround1(View viewObserving) {
        mViewObserved = viewObserving;
        //给View添加全局的布局监听器
        mViewObserved.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                resetLayoutByUsableHeight(computeUsableHeight());
            }
        });
        frameLayoutParams = mViewObserved.getLayoutParams();
    }

    private void resetLayoutByUsableHeight(int usableHeightNow) {
        //比较布局变化前后的View的可用高度
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致
            //将当前的View的可用高度设置成View的实际高度
            frameLayoutParams.height = usableHeightNow;
            mViewObserved.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 计算视图可视高度
     *
     * @return
     */
    private int computeUsableHeight() {
//        Rect r = new Rect();
//        mViewObserved.getWindowVisibleDisplayFrame(r);
//        return (r.bottom - r.top);
        Rect r = new Rect();
        mViewObserved.getWindowVisibleDisplayFrame(r);
//        return (r.bottom - r.top);// 全屏模式下： return r.bottom
        return r.bottom;
    }
}
