package com.mc.phonelive.views.foxtone;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.ViewPagerAdapter;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.interfaces.LifeCycleListener;
import com.mc.phonelive.views.AbsMainChildTopViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * MainActivity 视频
 */

public class MainVideoViewHolder extends AbsMainVideoParentViewHolder {

    private boolean mPaused;
//    private CountDownView video_countdown;//倒计时控件
    private VideoCountDownViewHolder mVideoCountDownViewHolder;


    public MainVideoViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_video;
    }

    @Override
    public void init() {
        super.init();

        mVideoCountDownViewHolder = new VideoCountDownViewHolder(mContext, (ViewGroup) findViewById(R.id.countdowm_layout));
        mVideoCountDownViewHolder.addToParent();

        mViewHolders = new AbsMainChildTopViewHolder[3];
        mViewHolders[0] = new MainVideoListViewHolder(mContext, mViewPager); //同城
//        mViewHolders[1] = new MainVideoListViewHolder(mContext, mViewPager);
        mViewHolders[1] = new MainVideoFollowViewHolder(mContext, mViewPager); //关注
        mViewHolders[2] = new MainTjVideoListViewHolder(mContext, mViewPager); //推荐
        List<View> list = new ArrayList<>();
        for (AbsMainChildTopViewHolder vh : mViewHolders) {
            list.add(vh.getContentView());
        }
        mViewPager.setAdapter(new ViewPagerAdapter(list));
        mIndicator.setTitles(new String[]{
               "同城",
               "关注",
               "推荐",
        });
        mIndicator.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AppConfig.getInstance().tabPostion=position;
                Log.e("ppppppp:",position+"");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(2);
    }




    @Override
    public List<LifeCycleListener> getLifeCycleListenerList() {
        List<LifeCycleListener> list = new ArrayList<>();
        list.add(new LifeCycleAdapter() {
            @Override
            public void onResume() {
                Log.v("tags", "------------dd-------1---onResume=111");
                if (mPaused) {
                    mPaused = false;
                    if (mViewPager != null && mViewHolders != null) {
                        mViewHolders[mViewPager.getCurrentItem()].loadData();
                    }
                }
            }

            @Override
            public void onPause() {
                Log.v("tags", "------------dd-------1---onPause=111");
                EventBus.getDefault().post(new EventBusModel("main_video_puase"));
                mPaused = true;
            }

            @Override
            public void onDestroy() {
                Log.v("tags", "------------dd-------1---onDestroy=111");
                HttpUtil.cancel(HttpConsts.GET_HOME_VIDEO_LIST);
                HttpUtil.cancel(HttpConsts.GETVideoFOLLOW);
            }
        });
        for (AbsMainChildTopViewHolder vh : mViewHolders) {
            LifeCycleListener lifeCycleListener = vh.getLifeCycleListener();
            if (lifeCycleListener != null) {
                list.add(lifeCycleListener);
            }
        }
        return list;
    }

}
