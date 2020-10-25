package com.mylocation.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.mcxtzhang.indexlib.IndexBar.helper.IIndexBarDataHelper;
import com.mcxtzhang.indexlib.IndexBar.helper.IndexBarDataHelperImpl;
import com.mylocation.android.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * created by WWL on 2019/3/13 0013:16
 */
public class IndexBar extends View {
    private static final String TAG = "zxt/IndexBar";
    public static String[] INDEX_STRING = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private boolean isNeedRealIndex;
    private List<String> mIndexDatas;
    private int mWidth;
    private int mHeight;
    private int mGapHeight;
    private Paint mPaint;
    private int mPressedBackground;
    private IIndexBarDataHelper mDataHelper;
    private TextView mPressedShowTextView;
    private boolean isSourceDatasAlreadySorted;
    private List<? extends BaseIndexPinyinBean> mSourceDatas;
    private LinearLayoutManager mLayoutManager;
    private int mHeaderViewCount;
    private IndexBar.onIndexPressedListener mOnIndexPressedListener;

    public IndexBar(Context context) {
        this(context, (AttributeSet)null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mHeaderViewCount = 0;
        this.init(context, attrs, defStyleAttr);
    }

    public int getHeaderViewCount() {
        return this.mHeaderViewCount;
    }

    public IndexBar setHeaderViewCount(int headerViewCount) {
        this.mHeaderViewCount = headerViewCount;
        return this;
    }

    public boolean isSourceDatasAlreadySorted() {
        return this.isSourceDatasAlreadySorted;
    }

    public IndexBar setSourceDatasAlreadySorted(boolean sourceDatasAlreadySorted) {
        this.isSourceDatasAlreadySorted = sourceDatasAlreadySorted;
        return this;
    }

    public IIndexBarDataHelper getDataHelper() {
        return this.mDataHelper;
    }

    public IndexBar setDataHelper(IIndexBarDataHelper dataHelper) {
        this.mDataHelper = dataHelper;
        return this;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        int textSize = (int) TypedValue.applyDimension(0, 10.0F, this.getResources().getDisplayMetrics());
        this.mPressedBackground = -16777216;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, com.mcxtzhang.indexlib.R.styleable.IndexBar, defStyleAttr, 0);
        int n = typedArray.getIndexCount();

        for(int i = 0; i < n; ++i) {
            int attr = typedArray.getIndex(i);
            if (attr == com.mcxtzhang.indexlib.R.styleable.IndexBar_indexBarTextSize) {
                textSize = typedArray.getDimensionPixelSize(attr, textSize);
            } else if (attr == com.mcxtzhang.indexlib.R.styleable.IndexBar_indexBarPressBackground) {
                this.mPressedBackground = typedArray.getColor(attr, this.mPressedBackground);
            }
        }

        typedArray.recycle();
        this.initIndexDatas();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextSize((float)textSize);
        this.mPaint.setColor(getResources().getColor(R.color.black));
        this.setmOnIndexPressedListener(new IndexBar.onIndexPressedListener() {
            public void onIndexPressed(int index, String text) {
                if (IndexBar.this.mPressedShowTextView != null) {
                    IndexBar.this.mPressedShowTextView.setVisibility(VISIBLE);
                    IndexBar.this.mPressedShowTextView.setText(text);
                }

                if (IndexBar.this.mLayoutManager != null) {
                    int position = IndexBar.this.getPosByTag(text);
                    if (position != -1) {
                        IndexBar.this.mLayoutManager.scrollToPositionWithOffset(position, 0);
                    }
                }

            }

            public void onMotionEventEnd() {
                if (IndexBar.this.mPressedShowTextView != null) {
                    IndexBar.this.mPressedShowTextView.setVisibility(GONE);
                }

            }
        });
        this.mDataHelper = new IndexBarDataHelperImpl();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidth = 0;
        int measureHeight = 0;
        Rect indexBounds = new Rect();

        for(int i = 0; i < this.mIndexDatas.size(); ++i) {
            String index = (String)this.mIndexDatas.get(i);
            this.mPaint.getTextBounds(index, 0, index.length(), indexBounds);
            measureWidth = Math.max(indexBounds.width(), measureWidth);
            measureHeight = Math.max(indexBounds.height(), measureHeight);
        }

        measureHeight *= this.mIndexDatas.size();
        switch(wMode) {
            case -2147483648:
                measureWidth = Math.min(measureWidth, wSize);
            case 0:
            default:
                break;
            case 1073741824:
                measureWidth = wSize;
        }

        switch(hMode) {
            case -2147483648:
                measureHeight = Math.min(measureHeight, hSize);
            case 0:
            default:
                break;
            case 1073741824:
                measureHeight = hSize;
        }

        this.setMeasuredDimension(measureWidth, measureHeight);
    }

    protected void onDraw(Canvas canvas) {
        int t = this.getPaddingTop();

        for(int i = 0; i < this.mIndexDatas.size(); ++i) {
            String index = (String)this.mIndexDatas.get(i);
            Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
            int baseline = (int)(((float)this.mGapHeight - fontMetrics.bottom - fontMetrics.top) / 2.0F);
            canvas.drawText(index, (float)(this.mWidth / 2) - this.mPaint.measureText(index) / 2.0F, (float)(t + this.mGapHeight * i + baseline), this.mPaint);
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case 0:
                this.setBackgroundColor(this.mPressedBackground);
            case 2:
                float y = event.getY();
                int pressI = (int)((y - (float)this.getPaddingTop()) / (float)this.mGapHeight);
                if (pressI < 0) {
                    pressI = 0;
                } else if (pressI >= this.mIndexDatas.size()) {
                    pressI = this.mIndexDatas.size() - 1;
                }

                if (null != this.mOnIndexPressedListener && pressI > -1 && pressI < this.mIndexDatas.size()) {
                    this.mOnIndexPressedListener.onIndexPressed(pressI, (String)this.mIndexDatas.get(pressI));
                }
                break;
            case 1:
            case 3:
            default:
                this.setBackgroundResource(R.color.transparent);
                if (null != this.mOnIndexPressedListener) {
                    this.mOnIndexPressedListener.onMotionEventEnd();
                }
        }

        return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        if (null != this.mIndexDatas && !this.mIndexDatas.isEmpty()) {
            this.computeGapHeight();
        }
    }

    public IndexBar.onIndexPressedListener getmOnIndexPressedListener() {
        return this.mOnIndexPressedListener;
    }

    public void setmOnIndexPressedListener(IndexBar.onIndexPressedListener mOnIndexPressedListener) {
        this.mOnIndexPressedListener = mOnIndexPressedListener;
    }

    public IndexBar setmPressedShowTextView(TextView mPressedShowTextView) {
        this.mPressedShowTextView = mPressedShowTextView;
        return this;
    }

    public IndexBar setmLayoutManager(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
        return this;
    }

    public IndexBar setNeedRealIndex(boolean needRealIndex) {
        this.isNeedRealIndex = needRealIndex;
        this.initIndexDatas();
        return this;
    }

    private void initIndexDatas() {
        if (this.isNeedRealIndex) {
            this.mIndexDatas = new ArrayList();
        } else {
            this.mIndexDatas = Arrays.asList(INDEX_STRING);
        }

    }

    public IndexBar setmSourceDatas(List<? extends BaseIndexPinyinBean> mSourceDatas) {
        this.mSourceDatas = mSourceDatas;
        this.initSourceDatas();
        return this;
    }

    private void initSourceDatas() {
        if (null != this.mSourceDatas && !this.mSourceDatas.isEmpty()) {
            if (!this.isSourceDatasAlreadySorted) {
                this.mDataHelper.sortSourceDatas(this.mSourceDatas);
            } else {
                this.mDataHelper.convert(this.mSourceDatas);
                this.mDataHelper.fillInexTag(this.mSourceDatas);
            }

            if (this.isNeedRealIndex) {
                this.mDataHelper.getSortedIndexDatas(this.mSourceDatas, this.mIndexDatas);
                this.computeGapHeight();
            }

        }
    }

    private void computeGapHeight() {
        this.mGapHeight = (this.mHeight - this.getPaddingTop() - this.getPaddingBottom()) / this.mIndexDatas.size();
    }

    private void sortData() {
    }

    private int getPosByTag(String tag) {
        if (null != this.mSourceDatas && !this.mSourceDatas.isEmpty()) {
            if (TextUtils.isEmpty(tag)) {
                return -1;
            } else {
                for(int i = 0; i < this.mSourceDatas.size(); ++i) {
                    if (tag.equals(((BaseIndexPinyinBean)this.mSourceDatas.get(i)).getBaseIndexTag())) {
                        return i + this.getHeaderViewCount();
                    }
                }

                return -1;
            }
        } else {
            return -1;
        }
    }

    public interface onIndexPressedListener {
        void onIndexPressed(int var1, String var2);

        void onMotionEventEnd();
    }
}
