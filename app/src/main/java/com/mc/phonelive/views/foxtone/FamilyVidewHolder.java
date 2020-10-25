package com.mc.phonelive.views.foxtone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.SlidingTabLayout;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.adapter.MyPagerAdapter;
import com.mc.phonelive.bean.foxtone.FindSchoolBean;
import com.mc.phonelive.bean.foxtone.MainFindBean;
import com.mc.phonelive.fragment.FamilyFragmentLeft;
import com.mc.phonelive.fragment.FamilyFragmentRight;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.views.AbsMainViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 家人模块
 */
public class FamilyVidewHolder extends AbsMainViewHolder {
    private SlidingTabLayout mTablayout;
    private android.support.v4.view.ViewPager mTab_viewpager;
//    private final String[] mTitles = {"全部", "待使用", "已使用","已失效"};
    private ArrayList<String> mTitles;
    private ArrayList<Fragment> mFragmentList;
    public FamilyVidewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {

        return R.layout.main_family_layout;
    }


    @Override
    public void init() {
        mTitles=new ArrayList<>();
        mTitles.add("好友列表");
        mTitles.add("发现好友");
        mTablayout =mContentView.findViewById(R.id.tablayout);
        mTab_viewpager = mContentView.findViewById(R.id.tab_viewpager);
        initFragments();
        initViewPager();
        initHttpData();
    }
    private void initFragments() {


        mFragmentList = new ArrayList<>();
        FamilyFragmentLeft fragment02 = FamilyFragmentLeft.newInstance();
        FamilyFragmentRight fragment03 = FamilyFragmentRight.newInstance();

        mFragmentList.add(fragment02);
        mFragmentList.add(fragment03);
    }

    private void initViewPager() {
        mTab_viewpager.setOffscreenPageLimit(6);
        mTab_viewpager.setAdapter(new MyPagerAdapter(((AbsActivity) mContext).getSupportFragmentManager(), mFragmentList, mTitles));
        mTablayout.setViewPager(mTab_viewpager);
        mTablayout.onPageSelected(0);
    }

    private void initHttpData() {

        HttpUtil.getFindIndex(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    List<MainFindBean.InfoBean.SlideBean> slideList = JSON.parseArray(obj.getString("slide"), MainFindBean.InfoBean.SlideBean.class);
                    List<MainFindBean.InfoBean.NoticeBean> noticeList = JSON.parseArray(obj.getString("notice"), MainFindBean.InfoBean.NoticeBean.class);
                    List<MainFindBean.InfoBean.ListBean> musicList = JSON.parseArray(obj.getString("list"), MainFindBean.InfoBean.ListBean.class);
                    List<MainFindBean.InfoBean.GameBean> gameList = MainFindBean.getFindData();
                    List<FindSchoolBean> newsList = JSON.parseArray(obj.getString("newsList"), FindSchoolBean.class);




                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
        });
    }


    private void forward(Context context, String url) {
        url += "&uid=" + AppConfig.getInstance().getUid() + "&token=" + AppConfig.getInstance().getToken();
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.URL, url);
        context.startActivity(intent);
    }


}
