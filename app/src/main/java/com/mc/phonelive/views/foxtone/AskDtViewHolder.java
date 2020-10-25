package com.mc.phonelive.views.foxtone;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mc.phonelive.R;
import com.mc.phonelive.adapter.ViewPagerAdapter;
import com.mc.phonelive.views.AbsMainChildTopViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 问医888888
 */

public class AskDtViewHolder extends AbsMainAskHolder {
//    private VideoCountDownViewHolder mVideoCountDownViewHolder;
    public AskDtViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.ask_main;
    }
    @Override
    public void init() {
        super.init();
//        mVideoCountDownViewHolder = new VideoCountDownViewHolder(mContext, (ViewGroup) findViewById(R.id.countdowm_layout));
//        mVideoCountDownViewHolder.addToParent();
        mViewHolders = new AbsMainChildTopViewHolder[3];
        mViewHolders[0] = new AskLeftViewHolder(mContext, mViewPager);
        mViewHolders[1] = new AskCenterViewHolder(mContext, mViewPager);
        mViewHolders[2] = new AskRightViewHolder(mContext, mViewPager);
        List<View> list = new ArrayList<>();
        for (AbsMainChildTopViewHolder vh : mViewHolders) {
            list.add(vh.getContentView());
        }
        mViewPager.setAdapter(new ViewPagerAdapter(list));
        mIndicator.setTitles(new String[]{
               "同城",
               "直播",
               "关注",
        });

        mIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }
}
