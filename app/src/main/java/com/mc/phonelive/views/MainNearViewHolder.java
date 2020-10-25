package com.mc.phonelive.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mc.phonelive.R;
import com.mc.phonelive.adapter.ViewPagerAdapter;
import com.mc.phonelive.interfaces.MainAppBarExpandListener;
import com.mc.phonelive.utils.WordUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 附近
 */

public class MainNearViewHolder extends AbsMainParentViewHolder {

    public MainNearViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_near;
    }

    @Override
    public void init() {
        super.init();
        mViewHolders = new AbsMainChildTopViewHolder[1];
        mViewHolders[0] = new MainLiveNearViewHolder(mContext, mViewPager);
        MainAppBarExpandListener expandListener = new MainAppBarExpandListener() {
            @Override
            public void onExpand(boolean expand) {
                if (mViewPager != null) {
                    mViewPager.setCanScroll(expand);
                    mViewHolders[mViewPager.getCurrentItem()].setCanRefresh(expand);
                }
            }
        };
        List<View> list = new ArrayList<>();
        for (AbsMainChildTopViewHolder vh : mViewHolders) {
            vh.setAppBarExpandListener(expandListener);
            list.add(vh.getContentView());
        }
        mViewPager.setAdapter(new ViewPagerAdapter(list));
        mIndicator.setTitles(new String[]{
                WordUtil.getString(R.string.near)
        });
        mIndicator.setViewPager(mViewPager);
        mViewHolders[0].addTopView(mTopView);
    }

}
