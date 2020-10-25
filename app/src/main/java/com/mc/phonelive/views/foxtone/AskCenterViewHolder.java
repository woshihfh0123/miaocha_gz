package com.mc.phonelive.views.foxtone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.MyShopDetailActivity;
import com.mc.phonelive.activity.TypeZbListActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.adapter.ZbGvAdapter;
import com.mc.phonelive.adapter.ZbTypeAdapter;
import com.mc.phonelive.bean.FarmilyBean;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.bean.WyBannerBean;
import com.mc.phonelive.bean.WyZbBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.presenter.CheckLivePresenter;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.AbsMainChildTopViewHolder;
import com.picture.android.PictureActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cece.com.bannerlib.model.PicBean;

/**
 *同城问医888888
 *
 */

public class AskCenterViewHolder extends AbsMainChildTopViewHolder {
    private android.support.v7.widget.RecyclerView mRv_top;
    private com.youth.banner.Banner mBanner;
    private TextView mTv_ys;
    private TextView mTv_wy;
    private ZbTypeAdapter topAdapter;
    private ZbGvAdapter mAdapter;
    private SmartRefreshLayout smr;
    private int page=1;
    private android.support.v7.widget.RecyclerView mRv_zb;
    public AskCenterViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_center_wy;
    }

    @Override
    public void init() {
        mRv_top = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_top);
        mBanner = (com.youth.banner.Banner) findViewById(R.id.banner);
        mTv_ys = (TextView) findViewById(R.id.tv_ys);
        mTv_wy = (TextView) findViewById(R.id.tv_wy);
        smr = (SmartRefreshLayout) findViewById(R.id.smr);
        mRv_zb = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_zb);
        topAdapter=new ZbTypeAdapter();
        mAdapter=new ZbGvAdapter();
        mRv_top.setLayoutManager(Utils.getGvManager(mContext,4));
        mRv_top.setAdapter(topAdapter);
        mRv_zb.setLayoutManager(Utils.getGvManager(mContext,2));
        mRv_zb.setAdapter(mAdapter);
        List<FarmilyBean> topList=new ArrayList<>();
        for(int i=0;i<4;i++){
            topList.add(new FarmilyBean());
        }
        topAdapter.setNewData(topList);
//        mAdapter.setNewData(topList);
        topAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(mContext, TypeZbListActivity.class);
                intent.putExtra("type",position+"");
                intent.putExtra("catid","");
                mContext.startActivity(intent);
            }
        });
        mTv_ys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, TypeZbListActivity.class);
                intent.putExtra("catid","1");
                intent.putExtra("type","");
                mContext.startActivity(intent);
            }
        });
        mTv_wy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, TypeZbListActivity.class);
                intent.putExtra("catid","2");
                intent.putExtra("type","");
                mContext.startActivity(intent);
            }
        });
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object o, ImageView imageView){
                WyBannerBean advBean= (WyBannerBean) o;
                if(Utils.isNotEmpty(advBean)&&Utils.isNotEmpty(advBean.getSlide_pic())){
                    GlideUtils.loadImage(mContext,advBean.getSlide_pic(),imageView);
                }
            }
        }).setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER);



        getList();
        smr.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                page=1;
                getList();
            }
        });
        smr.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                page++;
                getList();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LiveBean liveBean=new LiveBean();//LiveBean
                liveBean.setUid(mAdapter.getData().get(position).getUid());
                liveBean.setStream(mAdapter.getData().get(position).getStream());
                liveBean.setThumb(mAdapter.getData().get(position).getThumb());
                liveBean.setPull(mAdapter.getData().get(position).getPull());
                liveBean.setUserNiceName(mAdapter.getData().get(position).getUser_nicename());
                liveBean.setIs_shopping(mAdapter.getData().get(position).getIs_shopping());
                liveBean.setAvatar(mAdapter.getData().get(position).getAvatar());
                liveBean.setStoreid(mAdapter.getData().get(position).getStoreid());
                watchLive(liveBean, Constants.LIVE_FOLLOW,position);
            }
        });
    }
    private CheckLivePresenter mCheckLivePresenter;
    public void watchLive(LiveBean liveBean, String key, int position) {
        if (mCheckLivePresenter == null) {
            mCheckLivePresenter = new CheckLivePresenter(mContext);
        }
        mCheckLivePresenter.watchLive(liveBean, key, position);
    }
    private void getList() {//String p,String type,String catid
        HttpUtil.getWyzB(page+"","","",new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<WyBannerBean> bannerList = (ArrayList<WyBannerBean>) JSON.parseArray(obj.getString("banner"), WyBannerBean.class);
                    ArrayList<WyZbBean> nlist = (ArrayList<WyZbBean>) JSON.parseArray(obj.getString("list"), WyZbBean.class);
                    if(Utils.isNotEmpty(bannerList)){
                        mBanner.setImages(bannerList);
                        mBanner.start();
                    }
                    mBanner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            if(Utils.isNotEmpty(bannerList.get(position).getSlide_url())){
                                WebViewActivity.forward(mContext, bannerList.get(position).getSlide_url());
//                                WebViewActivity.forward(mContext,"http://miaocha.yanshi.hbbeisheng.com/appapi/auth/index.html?uid=11910&token=3096d196d7e66262042ce56db8dbe540");
                            }else{
                                ArrayList<String> mpiclist = new ArrayList<>();
                                for (int i = 0; i < bannerList.size(); i++) {
                                  String currentpic = bannerList.get(i).getSlide_pic();
                                    mpiclist.add(currentpic);
                                }
                                Bundle bundle = new Bundle();
                                bundle.putStringArrayList("list", mpiclist);
                                bundle.putString("id", position + "");
                                Intent intent = new Intent(mContext, PictureActivity.class);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        }
                    });
                    if(Utils.isNotEmpty(nlist)){
                        smr.setEnableLoadMore(true);
                        if(page==1){
                            mAdapter.setNewData(nlist);
                        }else{
                            mAdapter.addData(nlist);
                        }

                    }else{
                        if(page==1){
                            mAdapter.setNewData(null);
                        }
                        smr.setEnableLoadMore(false);
                    }
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }
    @Override
    public void loadData() {

    }


}
