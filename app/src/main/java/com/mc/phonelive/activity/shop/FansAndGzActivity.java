package com.mc.phonelive.activity.shop;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.SlidingTabLayout;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.MyPagerAdapter;
import com.mc.phonelive.bean.foxtone.FindSchoolBean;
import com.mc.phonelive.bean.foxtone.MainFindBean;
import com.mc.phonelive.fragment.GzFsFragmentLeft;
import com.mc.phonelive.fragment.GzFsFragmentRight;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.EventBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注和粉丝
 */
public class FansAndGzActivity extends AbsActivity  {
    private SlidingTabLayout mTablayout;
    private android.support.v4.view.ViewPager mTab_viewpager;
    //    private final String[] mTitles = {"全部", "待使用", "已使用","已失效"};
    private ArrayList<String> mTitles;
    private ArrayList<Fragment> mFragmentList;
    private  TextView titleView;
    private String checkPosition="0";
    private String gznum="";
    private String fsnum="";
    private String name="";
//     intent1.putExtra("gznum",gzNum);
//                intent1.putExtra("fsnum",fansNum);
    @Override
    public int getLayoutId() {
        return R.layout.order_activity;
    }


    @Override
    public void main() {
        mTitles=new ArrayList<>();
        checkPosition=getIntent().getStringExtra("pos");
        fsnum=getIntent().getStringExtra("fsnum");
        gznum=getIntent().getStringExtra("gznum");
        String usname = getIntent().getStringExtra("name");
        mTitles.add("关注"+gznum);
        mTitles.add("粉丝"+fsnum);
        mTablayout =findViewById(R.id.tablayout);
        mTab_viewpager =findViewById(R.id.tab_viewpager);
        titleView=findViewById(R.id.titleView);
        titleView.setText(usname);
        initFragments();
        initViewPager();
        initHttpData();
    }

    @Override
    protected boolean isRegisterEventBus() {

        return true;//num_from_remove
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(EventBean event){
        switch(event.getCode()){
            case "num_from_remove":
                Log.e("ZZZZZ:",(Integer.parseInt(fsnum)-1)+"");
                fsnum=(Integer.parseInt(fsnum)-1)+"";
              mTitles.set(1,"粉丝"+fsnum);
                mTablayout.notifyDataSetChanged();
//                mTablayout.postInvalidate();
                break;
            default:

                break;
        }
    };
    private void initFragments() {


        mFragmentList = new ArrayList<>();
        GzFsFragmentLeft fragment02 = GzFsFragmentLeft.newInstance();
        GzFsFragmentRight fragment03 = GzFsFragmentRight.newInstance();

        mFragmentList.add(fragment02);
        mFragmentList.add(fragment03);
    }

    private void initViewPager() {
        mTab_viewpager.setOffscreenPageLimit(6);
        mTab_viewpager.setAdapter(new MyPagerAdapter(((AbsActivity) mContext).getSupportFragmentManager(), mFragmentList, mTitles));
        mTablayout.setViewPager(mTab_viewpager);
        mTablayout.onPageSelected(Integer.parseInt(checkPosition));
        mTab_viewpager.setCurrentItem(Integer.parseInt(checkPosition));

//        mTablayout.onPageSelected(0);
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





}
