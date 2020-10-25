package com.mc.phonelive.views.foxtone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.ChatActivity;
import com.mc.phonelive.custom.MyViewPager;
import com.mc.phonelive.custom.ViewPagerIndicator;
import com.mc.phonelive.interfaces.LifeCycleListener;
import com.mc.phonelive.interfaces.MainAppBarLayoutListener;
import com.mc.phonelive.views.AbsMainChildTopViewHolder;
import com.mc.phonelive.views.AbsMainViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/26.
 * MainActivity 中的首页
 */

public abstract class AbsMainAskHolder extends AbsMainViewHolder {

    protected ViewGroup mTopContainer; //放置头部导航条的容器
    protected AbsMainChildTopViewHolder[] mViewHolders;
    protected MyViewPager mViewPager;
    protected View mTopView;
    protected ViewPagerIndicator mIndicator;
    protected boolean mFirst = true;
    public static int mIndex=0;//判断是视频列表还是关注列表
    private TextView btn_msg;
    public AbsMainAskHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        mTopContainer = (ViewGroup) findViewById(R.id.top_container);
        mViewPager = (MyViewPager) findViewById(R.id.viewPager);
//        mViewPager.setOffscreenPageLimit(1);
        mTopView = LayoutInflater.from(mContext).inflate(R.layout.view_main_video_ask, null, false);
        btn_msg=mTopView.findViewById(R.id.btn_msg);
        mIndicator = (ViewPagerIndicator) mTopView.findViewById(R.id.indicator);
        addTopView(mTopView);
        mIndicator.setListener(new ViewPagerIndicator.OnPageChangeListener() {
            @Override
            public void onTabClick(int position) {
                mIndex=position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mFirst) {
                    mFirst = false;
                } else {
//                    addTopView(mTopView);
                }
            }

            @Override
            public void onPageSelected(int position) {
                mIndex=position;
                if (mViewHolders != null) {
                    for (int i = 0, length = mViewHolders.length; i < length; i++) {
                        if (position == i) {
                            mViewHolders[i].setNeedDispatch(true);
                            mViewHolders[i].loadData();
                            mViewHolders[i].setShowed(true);
                        } else {
                            mViewHolders[i].setNeedDispatch(false);
                            mViewHolders[i].setShowed(false);
                        }

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
//                    removeTopView();
//                    if (mViewHolders != null) {
//                        mViewHolders[mViewPager.getCurrentItem()].addTopView(mTopView);
//                    }
                }
            }
        });
        btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ChatActivity.class));
            }
        });
    }

    public void addTopView(View view) {
        if (view != null && mTopContainer != null) {
            ViewParent parent = view.getParent();
            if (parent != null) {
                if (parent != mTopContainer) {
                    ((ViewGroup) parent).removeView(view);
                    mTopContainer.addView(view);
                }
            } else {
                mTopContainer.addView(view);
            }
        }
    }

    public void removeTopView() {
        if (mTopContainer != null && mTopContainer.getChildCount() > 0) {
            mTopContainer.removeAllViews();
        }
    }

    @Override
    public void setAppBarLayoutListener(MainAppBarLayoutListener appBarLayoutListener) {
        if (mViewHolders != null) {
            for (AbsMainChildTopViewHolder vh : mViewHolders) {
                vh.setAppBarLayoutListener(appBarLayoutListener);
            }
        }
    }

    @Override
    public List<LifeCycleListener> getLifeCycleListenerList() {
        List<LifeCycleListener> list = new ArrayList<>();
        if(mLifeCycleListener!=null){
            list.add(mLifeCycleListener);
        }
        for (AbsMainChildTopViewHolder vh : mViewHolders) {
            LifeCycleListener listener = vh.getLifeCycleListener();
            if (listener != null) {
                list.add(listener);
            }
        }
        return list;
    }

    @Override
    public void loadData() {
        mViewHolders[mViewPager.getCurrentItem()].loadData();
    }

    @Override
    public void setShowed(boolean showed) {
        super.setShowed(showed);
        if (showed) {
            for (int i = 0, length = mViewHolders.length; i < length; i++) {
                if (i == mViewPager.getCurrentItem()) {
                    mViewHolders[i].setNeedDispatch(true);
                } else {
                    mViewHolders[i].setNeedDispatch(false);
                }
            }
        } else {
            for (AbsMainChildTopViewHolder vh : mViewHolders) {
                vh.setNeedDispatch(false);
            }
        }
    }

}
