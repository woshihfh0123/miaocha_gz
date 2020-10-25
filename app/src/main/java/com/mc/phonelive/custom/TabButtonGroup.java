package com.mc.phonelive.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.mc.phonelive.AppConfig;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.SpUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 */

public class TabButtonGroup extends LinearLayout implements View.OnClickListener {

    private List<TabButton> mList;
    private ViewPager mViewPager;
    private int mCurPosition;
    private ValueAnimator mAnimator;
    private View mAnimView;
    private Runnable mRunnable;

    public TabButtonGroup(Context context) {
        this(context, null);
    }

    public TabButtonGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabButtonGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mList = new ArrayList<>();
        mCurPosition = 0;
        mAnimator = ValueAnimator.ofFloat(0.9f, 1.4f, 1f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                if (mAnimView != null) {
                    mAnimView.setScaleX(v);
                    mAnimView.setScaleY(v);
                }
            }
        });
        mAnimator.setDuration(300);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimView = null;
            }
        });
        mRunnable=new Runnable() {
            @Override
            public void run() {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(mCurPosition, false);
                }
            }
        };
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0, count = getChildCount(); i < count; i++) {
            View v = getChildAt(i);
            v.setTag(i);
            v.setOnClickListener(this);
            mList.add((TabButton) v);
        }
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null) {
            int position = (int) tag;
            Log.e("FFFFFggggg:",position+"");

            if(position==0&&AppConfig.getInstance().lstPostion==0){
                EventBus.getDefault().post(new EventBusModel("send_position_from_check_tab", AppConfig.getInstance().tabPostion));
            }
            if(position==3){
                EventBus.getDefault().post(new EventBusModel("refreh_xh_sb_view"));

            }
            AppConfig.getInstance().lstPostion=position;
            SpUtil.getInstance().setBooleanValue("setMe",position==3);
            if (position == mCurPosition) {
                return;
            }
            mList.get(mCurPosition).setChecked(false);
            TabButton tbn = mList.get(position);
            tbn.setChecked(true);
            mCurPosition = position;
            mAnimView = tbn;
            mAnimator.start();
            postDelayed(mRunnable,150);
        }
    }

    /**
     * 切换tab
     * @param positon
     */
    public void checkedPositionTab(int positon){
        for(TabButton btn:mList){
            btn.setChecked(false);
        }
        mList.get(positon).setChecked(true);
        TabButton tbn = mList.get(positon);
        tbn.setChecked(true);
        mCurPosition = positon;
        mAnimView = tbn;
        mAnimator.start();
        postDelayed(mRunnable,150);
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    public void cancelAnim() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }
}
