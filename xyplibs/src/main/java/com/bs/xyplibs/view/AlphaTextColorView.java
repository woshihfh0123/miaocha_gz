package com.bs.xyplibs.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *  set TextColor alpha
 * @author luys samluys@foxmail.com
 * @date 2017/11/6
 */

public class AlphaTextColorView extends TextView {


    public AlphaTextColorView(Context context) {
        this(context, null);
    }

    public AlphaTextColorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaTextColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    /**
     * set alpha percent
     * @param percent
     */
    public void setAlphaPercent(int percent) {

        int color = this.getCurrentTextColor();
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);

        setTextColor(Color.argb(percent,red,green,blue));
    }
}
