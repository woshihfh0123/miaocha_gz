package com.mc.phonelive.activity.shop;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.ViewPagerAdapter;
import com.mc.phonelive.views.AbsMainViewHolder;
import com.mc.phonelive.views.MainListViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜
 * created by WWL on 2020/4/13 0013:09
 */
public class LeaderboardListActivity extends AbsActivity {
    private ViewPager mViewPager;
    private AbsMainViewHolder[] mViewHolders;

    @Override
    protected int getLayoutId() {
        return R.layout.leaderboard_list_activity;
    }

    protected boolean isStatusBarWhite() {
        return true;
    }

    @Override
    protected void main() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(1);
        mViewHolders = new AbsMainViewHolder[1];
        mViewHolders[0] = new MainListViewHolder(mContext, mViewPager);

        List<View> list = new ArrayList<>();
        for (AbsMainViewHolder vh : mViewHolders) {
            addAllLifeCycleListener(vh.getLifeCycleListenerList());
            list.add(vh.getContentView());
        }
        mViewPager.setAdapter(new ViewPagerAdapter(list));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0, length = mViewHolders.length; i < length; i++) {
                    if (i == position) {
                        mViewHolders[i].setShowed(true);
                    } else {
                        mViewHolders[i].setShowed(false);
                    }
                }
                mViewHolders[position].loadData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewHolders[0].loadData();
    }

}
