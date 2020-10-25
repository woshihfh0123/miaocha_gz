package com.bs.xyplibs.view;

import android.view.Gravity;

import com.hjq.toast.IToastStyle;

/**
 * 自定义吐司样式
 */
public class MyToastStyle implements IToastStyle {

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 400;
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public int getCornerRadius() {
        return 4;
    }

    @Override
    public int getBackgroundColor() {
        return 0XFF333333;
    }

    @Override
    public int getTextColor() {
        return 0XFFE3E3E3;
    }

    @Override
    public float getTextSize() {
        return 12;
    }

    @Override
    public int getMaxLines() {
        return 3;
    }

    @Override
    public int getPaddingLeft() {
        return 10;
    }

    @Override
    public int getPaddingTop() {
        return 8;
    }

    @Override
    public int getPaddingRight() {
        return getPaddingLeft();
    }

    @Override
    public int getPaddingBottom() {
        return getPaddingTop();
    }
}