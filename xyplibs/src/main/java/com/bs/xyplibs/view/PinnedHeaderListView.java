package com.bs.xyplibs.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PinnedHeaderListView extends ListView {
    private boolean mDrawFlag = true;
    private View mHeaderView;
    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private PinnedHeaderAdapter mPinnedHeaderAdapter;

    public interface PinnedHeaderAdapter {
        public static final int PINNED_HEADER_GONE = 0;
        public static final int PINNED_HEADER_PUSHED_UP = 2;
        public static final int PINNED_HEADER_VISIBLE = 1;

        void configurePinnedHeader(View view, int i, int i2);

        int getPinnedHeaderState(int i);
    }

    public PinnedHeaderListView(Context context) {
        super(context);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPinnedHeader(View pHeader) {
        this.mHeaderView = pHeader;
        requestLayout();
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        this.mPinnedHeaderAdapter = (PinnedHeaderAdapter) adapter;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mHeaderView != null) {
            measureChild(this.mHeaderView, widthMeasureSpec, heightMeasureSpec);
            this.mMeasuredWidth = this.mHeaderView.getMeasuredWidth();
            this.mMeasuredHeight = this.mHeaderView.getMeasuredHeight();
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (this.mHeaderView != null) {
            this.mHeaderView.layout(0, 0, this.mMeasuredWidth, this.mMeasuredHeight);
            controlPinnedHeader(getFirstVisiblePosition());
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mHeaderView != null && this.mDrawFlag) {
            drawChild(canvas, this.mHeaderView, getDrawingTime());
        }
    }

    public void controlPinnedHeader(int position) {
        if (this.mHeaderView != null) {
            switch (this.mPinnedHeaderAdapter.getPinnedHeaderState(position)) {
                case 0:
                    this.mDrawFlag = false;
                    return;
                case 1:
                    this.mPinnedHeaderAdapter.configurePinnedHeader(this.mHeaderView, position, 0);
                    this.mDrawFlag = true;
                    this.mHeaderView.layout(0, 0, this.mMeasuredWidth, this.mMeasuredHeight);
                    return;
                case 2:
                    this.mPinnedHeaderAdapter.configurePinnedHeader(this.mHeaderView, position, 0);
                    this.mDrawFlag = true;
                    View topItem = getChildAt(0);
                    if (topItem != null) {
                        int y;
                        int bottom = topItem.getBottom();
                        int height = this.mHeaderView.getHeight();
                        if (bottom < height) {
                            y = bottom - height;
                        } else {
                            y = 0;
                        }
                        if (this.mHeaderView.getTop() != y) {
                            this.mHeaderView.layout(0, y, this.mMeasuredWidth, this.mMeasuredHeight + y);
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
