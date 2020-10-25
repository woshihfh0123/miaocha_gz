package com.mc.phonelive.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mc.phonelive.R;

/**
 * Created by cxf on 2018/9/21.
 */

public class TabButton extends LinearLayout {

    private Context mContext;
    private float mScale;
    private int mSelectedIcon;
    private int mUnSelectedIcon;
    private String mTip;
    private int mIconSize;
    private int mTextSize;
    private int mTextColor;
    private int mTextSelectColor;
    private boolean mChecked;
    private ImageView mImg;
    private TextView mText;

    public TabButton(Context context) {
        this(context, null);
    }

    public TabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabButton);
        mSelectedIcon = ta.getResourceId(R.styleable.TabButton_tbn_selected_icon, 0);
        mUnSelectedIcon = ta.getResourceId(R.styleable.TabButton_tbn_unselected_icon, 0);
        mTip = ta.getString(R.styleable.TabButton_tbn_tip);
        mIconSize = (int) ta.getDimension(R.styleable.TabButton_tbn_icon_size, 0);
        mTextSize = (int) ta.getDimension(R.styleable.TabButton_tbn_text_size, 0);
        mTextColor = (int) ta.getColor(R.styleable.TabButton_tbn_text_color, 0xff000000);
        mTextSelectColor = (int) ta.getColor(R.styleable.TabButton_tbn_text_select_color, 0xff000000);
        mChecked = ta.getBoolean(R.styleable.TabButton_tbn_checked, false);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        mImg = new ImageView(mContext);
        LayoutParams params1 = new LinearLayout.LayoutParams(mIconSize, mIconSize);
        params1.setMargins(0, dp2px(0), 0, 0);
        mImg.setLayoutParams(params1);
        mImg.setVisibility(GONE);

        mText = new TextView(mContext);
        LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params2.setMargins(0, dp2px(1), 0, 0);
        mText.setPadding(0,dp2px(1),0,0);
        mText.setLayoutParams(params2);
        mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mText.setGravity(Gravity.CENTER);
        mText.setText(mTip);


        if (mChecked) {
            mImg.setImageResource(mSelectedIcon);
            mText.setTextColor(mTextSelectColor);
        } else {
            mText.setTextColor(mTextColor);
            mImg.setImageResource(mUnSelectedIcon);
        }


        addView(mImg);
        addView(mText);
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        if (checked) {
            mImg.setImageResource(mSelectedIcon);
            mText.setTextColor(mTextSelectColor);
        } else {
            mImg.setImageResource(mUnSelectedIcon);
            mText.setTextColor(mTextColor);
        }
    }

    private int dp2px(int dpVal) {
        return (int) (mScale * dpVal + 0.5f);
    }

}
