package com.bs.xyplibs.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.bs.xyplibs.R;


public class TextViewBorder extends android.support.v7.widget.AppCompatTextView {
    private int strokeWidth =2; // 默认边框宽度
    private int borderCol;
    private Paint borderPaint;

    public TextViewBorder(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TextViewBorder, 0, 0);
        try {
            borderCol = a.getInteger(R.styleable.TextViewBorder_tvborderColor, 0);//0 is default
            strokeWidth = a.getInteger(R.styleable.TextViewBorder_tvBorderWidth, 2);

        } finally {
            a.recycle();
        }

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(strokeWidth);
        borderPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (0 == this.getText().toString().length()) {
            return;
        }
        borderPaint.setColor(borderCol);
        int w = this.getMeasuredWidth();
        int h = this.getMeasuredHeight();
        RectF r = new RectF(2, 2, w - 2, h - 2);
        canvas.drawRoundRect(r, 40, 40, borderPaint);

        super.onDraw(canvas);
    }

    public int getBordderColor() {
        return borderCol;
    }


    public void setBorderColor(int newColor) {
        borderCol = newColor;
        invalidate();
        requestLayout();
    }

    public void setBorderWidth(int width) {
        strokeWidth = width;
        invalidate();
        requestLayout();
    }
}