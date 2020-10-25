package com.mc.phonelive.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.fragment.XBaseFragment;
import com.mc.phonelive.fragment.ZuoPinFragment;
import com.mc.phonelive.interfaces.LifeCycleListener;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 新个人中心
 */

public class PersonMyTb extends AbsMainChildViewHolder  {
    private LayoutInflater mInflater;
    private static final int PRC_PHOTO_PREVIEW = 1;
    String[] mTitle = null;
    private List<XBaseFragment> fs=new ArrayList<>();
    private String shop_id="";
    private String lat="";
    private String lng="";
    private String is_fav="";
    private List<String> imgList=new ArrayList<>();


    private RelativeLayout mParallax;
    private ImageView mIv_bg;
    private ImageView mIv_pic;
    private TextView mTv_username;
    private com.scwang.smartrefresh.layout.SmartRefreshLayout mRefreshLayout;
    private android.support.design.widget.CoordinatorLayout mCly;
    private android.support.design.widget.AppBarLayout mAbl;
    private LinearLayout mHead_layout;
    private MyImageView mMiv;
    private TextView mTv_gz;
    private TextView mTv_nick_name;
    private TextView mTv_id;
    private TextView mTv_qm;
    private TextView mTv_hz_fs;
    private android.support.v7.widget.Toolbar mToolbar1;
    private com.flyco.tablayout.SlidingTabLayout mTablayout;
    private android.support.v4.view.ViewPager mTab_viewpager;
    private RelativeLayout mRl;
    private RelativeLayout mMrl_back;
    private View mTv_center_title1;
    private RelativeLayout mRl_255;
    private RelativeLayout mMrl_back_255;
    private RelativeLayout mMrl_right_255;



    private int mOffset = 0;
    private int mScrollY = 0;
    private String userId="";

    //  mContext.startActivity(new Intent(mContext, SettingActivity.class));
    public PersonMyTb(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_ps_main_tb;
    }

    @Override
    public void init() {
        mInflater = LayoutInflater.from(mContext);
        mParallax = (RelativeLayout) findViewById(R.id.parallax);
        mIv_bg = (ImageView) findViewById(R.id.iv_bg);
        mIv_pic = (ImageView) findViewById(R.id.iv_pic);
        mTv_username = (TextView) findViewById(R.id.tv_username);
        mRefreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.refreshLayout);
        mCly = (android.support.design.widget.CoordinatorLayout) findViewById(R.id.cly);
        mAbl = (android.support.design.widget.AppBarLayout) findViewById(R.id.abl);
        mHead_layout = (LinearLayout) findViewById(R.id.head_layout);
        mMiv = (MyImageView) findViewById(R.id.miv);
        mTv_gz = (TextView) findViewById(R.id.tv_gz);
        mTv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        mTv_id = (TextView) findViewById(R.id.tv_id);
        mTv_qm = (TextView) findViewById(R.id.tv_qm);
        mTv_hz_fs = (TextView) findViewById(R.id.tv_hz_fs);
        mToolbar1 = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        mTablayout = (com.flyco.tablayout.SlidingTabLayout) findViewById(R.id.tablayout);
        mTab_viewpager = (ViewPager) findViewById(R.id.tab_viewpager);
        mRl = (RelativeLayout) findViewById(R.id.rl);
        mMrl_back = (RelativeLayout) findViewById(R.id.mrl_back);
        mTv_center_title1 =  findViewById(R.id.tv_center_title1);
        mRl_255 = (RelativeLayout) findViewById(R.id.rl_255);
        mMrl_back_255 = (RelativeLayout) findViewById(R.id.mrl_back_255);
        mMrl_right_255 = (RelativeLayout) findViewById(R.id.mrl_right_255);
        fs.add(ZuoPinFragment.getInstance("1"));
        fs.add(ZuoPinFragment.getInstance("3"));
        initTbAndVp();
        mRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                mParallax.setTranslationY(mOffset - mScrollY);
//                toolbar.setAlpha(1 - Math.min(percent, 1));       }
            }
        });

        mMrl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBusUtil.postEvent(new EventBean("change_to_video_fragment_from_back"));
            }
        });
    }
//    public List<LifeCycleListener> getLifeCycleListenerList() {
//        return mLifeCycleListeners;
//    }

    public LifeCycleListener getLifeCycleListener() {
        return mLifeCycleListener;
    }
    @Override
    public void loadData() {

    }
    private void initTbAndVp() {
        mTab_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mTab_viewpager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTab_viewpager.setAdapter(new FragmentPagerAdapter(((AbsActivity) mContext).getSupportFragmentManager()) {
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle[position];
            }

            @Override
            public Fragment getItem(int position) {
                //创建Fragment并返回


                return  fs.get(position);
            }

            @Override
            public int getCount() {
                return mTitle.length;
            }
        });
        mTablayout.setViewPager(mTab_viewpager);
        mTablayout.onPageSelected(0);
    }








    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        mContext.startActivity(intent);
    }


}
