package com.mc.phonelive.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.mc.phonelive.R;
import com.mc.phonelive.fragment.LuZhiFragment120;
import com.mc.phonelive.fragment.LuZhiFragment30;
import com.mc.phonelive.fragment.LuZhiFragmentKaiZb;
import com.mc.phonelive.fragment.LuZhiFragmentPz;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * FUCK Activity
 * 短视频加直播大界面
 */

public class NewPsActivity extends AbsActivity
{


    private String[]mTitles=new String[]{"拍照","拍120秒","拍30秒","开直播"};
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_ps;
    }

    @Override
    protected boolean isStatusBarWhite() {
        return true;
    }

    @Override
    protected boolean isRegisterEventBus() {//
        return true;
    }
    @Subscribe
    public void getEvent(EventBean event){
        switch(event.getCode()){
            case "close_act_from_go_live":
                Log.e("ccccc","1111");
             finish();
                break;
            default:

                break;
        }
    };
    @Override
    protected void main() {
        mTabLayout=findViewById(R.id.tablayout);
        mViewPager=findViewById(R.id.tab_viewpager);
        initFragments();
        initViewPager();
    }
    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(LuZhiFragmentPz.newInstance());
        mFragments.add(LuZhiFragment120.newInstance());
        mFragments.add(LuZhiFragment30.newInstance());
        mFragments.add(LuZhiFragmentKaiZb.newInstance());
    }
    private void initViewPager() {

//        mViewPager.setOffscreenPageLimit(4);
//        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
//        mTabLayout.setViewPager(mViewPager);
//        mTabLayout.onPageSelected(2);
//        mViewPager.setCurrentItem(2);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Log.e("TTTT:",position+"");
                EventBusUtil.postEvent(new EventBean("change_view_from_click",position+""));
                if(position==0){//拍照

                }else if(position==1){//120S

                }else if(position==2){//30S

                }else if(position==3){//开直播

                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }






}
