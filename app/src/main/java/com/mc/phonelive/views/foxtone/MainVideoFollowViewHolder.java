package com.mc.phonelive.views.foxtone;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.MainActivity;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.views.AbsMainChildTopViewHolder;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cxf on 2018/9/22.
 * 首页关注视频
 */

public class MainVideoFollowViewHolder extends AbsMainChildTopViewHolder {
    private VideoHomeFollowScrollViewHolderGz mVideoScrollViewHolder;
    public MainVideoFollowViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_home_play;
    }

    @Override
    public void init() {
        super.init();

        mVideoScrollViewHolder = new VideoHomeFollowScrollViewHolderGz(mContext, (ViewGroup) findViewById(R.id.container));
        mVideoScrollViewHolder.addToParent();
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onResume() {
                Log.v("tags","生命周期onResume1");
                if (mVideoScrollViewHolder!=null && MainActivity.isMainVideoTab){
                    EventBus.getDefault().post(new EventBusModel("main_video_play"));//控制播放器播放
                }
            }

            @Override
            public void onPause() {
                if (mVideoScrollViewHolder!=null){
                    EventBus.getDefault().post(new EventBusModel("main_video_puase"));//控制播放器暂停
                }
                Log.v("tags","生命周期onPause1");
            }

            @Override
            public void onStart() {
                Log.v("tags","生命周期onStart1");
            }

            @Override
            public void onReStart() {
                Log.v("tags","生命周期onReStart1");
            }

            @Override
            public void onStop() {
//                mVideoScrollViewHolder.release();
                Log.v("tags","生命周期onStop1");
            }

        };

    }

    @Override
    public void loadData() {
        if (!isFirstLoadData()) {
            return;
        }
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }




}