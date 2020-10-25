package com.mc.phonelive.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.activity.shop.CartMainActivity;
import com.youth.banner.listener.OnBannerListener;
import com.mc.phonelive.Constants;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.bean.ShopsHomeBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.presenter.CheckLivePresenter;
import com.mc.phonelive.utils.DataSafeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.shop.TypeShopsActivity;
import com.mc.phonelive.adapter.GoodsTypeAdapter;
import com.mc.phonelive.adapter.MsAdapter;
import com.mc.phonelive.adapter.ShopAdapter;
import com.mc.phonelive.adapter.ZbAdapter;
import com.mc.phonelive.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 *
 * 商城
 */

public class ShopsActivity extends AbsActivity {

    private RecyclerView mRv,rv_ms,rv_shops,rv_zb;
    private GoodsTypeAdapter mAdapter;
    private MsAdapter msAdapter;
    private ShopAdapter shopAdapter;
    private ZbAdapter zbAdapter;
    private com.youth.banner.Banner mBanner;
    private LinearLayout ll_all_shop;
    private LinearLayout ll_cars;
    private CountdownView tv_djs_time;
    private TextView tv_zc;
    private LinearLayout ll_goods;
    private SmartRefreshLayout smr;
    private TextView titleView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shops_activity;
    }

    @Override
    protected void main() {
        mRv = (RecyclerView)findViewById(R.id.rv_type);
        rv_ms = (RecyclerView)findViewById(R.id.rv_ms);
        ll_cars = findViewById(R.id.ll_cars);
        ll_all_shop = (LinearLayout) findViewById(R.id.ll_all_shop);
        rv_shops = (RecyclerView)findViewById(R.id.rv_shops);
        rv_zb = (RecyclerView)findViewById(R.id.rv_zb);
        tv_djs_time =findViewById(R.id.tv_djs_time);
        titleView =findViewById(R.id.titleView);
        ll_goods =findViewById(R.id.ll_goods);
        tv_zc =findViewById(R.id.tv_zc);
        smr =findViewById(R.id.smr);
        mBanner = (com.youth.banner.Banner)findViewById(R.id.banner);
        mAdapter=new GoodsTypeAdapter();
        msAdapter=new MsAdapter();
        shopAdapter=new ShopAdapter();
        zbAdapter=new ZbAdapter();ll_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CartMainActivity.class);
                mContext.startActivity(intent);
            }
        });

        mRv.setLayoutManager(Utils.getGvManager(mContext,6));
        mRv.setAdapter(mAdapter);
        rv_ms.setLayoutManager(Utils.getHvManager(mContext));
        rv_ms.setAdapter(msAdapter);
        rv_shops.setLayoutManager(Utils.getGvManager(mContext,4));
        rv_shops.setAdapter(shopAdapter);
        rv_zb.setLayoutManager(Utils.getGvManager(mContext,2));
        rv_zb.setAdapter(zbAdapter);
        Utils.setViewWhp(mContext,true,mBanner,995,366,30);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object o, ImageView imageView){
                ShopsHomeBean.BannerBean advBean= (ShopsHomeBean.BannerBean) o;
                if(Utils.isNotEmpty(advBean)&&Utils.isNotEmpty(advBean.getSlide_pic())){
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                GlideUtils.loadBannerImage(mContext,advBean.getSlide_pic(),imageView);
                }
            }
        }).setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER);
        ll_all_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(mContext,AllShopsActivity.class);
               startActivity(intent);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==4 || position==5){
                    WebViewActivity.forward(ShopsActivity.this,"http://hotel.yct365.net/h5/");
                }else{
                    Intent intent=new Intent(mContext, TypeShopsActivity.class);
                    intent.putExtra("catid",mAdapter.getData().get(position).getId());
                    intent.putExtra("catName",mAdapter.getData().get(position).getName());
                    startActivity(intent);
                }

            }
        });
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,AllShopsActivity.class);
                startActivity(intent);
            }
        });
        getList();
        smr.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                getList();
            }
        });
        msAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                MyShopBean.DataBean.InfoBean.ListBean bean = (MyShopBean.DataBean.InfoBean.ListBean) adapter.getData().get(position);
                Intent intent = new Intent(mContext, MyShopDetailActivity.class);
                intent.putExtra("id", msAdapter.getData().get(position).getGoodsid());
                intent.putExtra("status", msAdapter.getData().get(position).getStatus());
                intent.putExtra("store_id", "0");
                intent.putExtra("isMs","1");
                mContext.startActivity(intent);
            }
        });
        shopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, MyShopActivity.class);
                intent.putExtra("status", "1");
                intent.putExtra("store_id", shopAdapter.getData().get(position).getId()+ "");
                mContext.startActivity(intent);
            }
        });
        zbAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LiveBean liveBean=new LiveBean();//LiveBean
                liveBean.setUid(zbAdapter.getData().get(position).getUid());
                liveBean.setStream(zbAdapter.getData().get(position).getStream());
                liveBean.setThumb(zbAdapter.getData().get(position).getThumb());
                liveBean.setPull(zbAdapter.getData().get(position).getPull());
                liveBean.setUserNiceName(zbAdapter.getData().get(position).getUser_nicename());
                liveBean.setIs_shopping(zbAdapter.getData().get(position).getIs_shopping());
                liveBean.setAvatar(zbAdapter.getData().get(position).getAvatar());
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
    private void getList() {
        HttpUtil.getShopHome(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String djs=obj.getString("tonexttime");
                    tv_zc.setText(obj.getString("curtime")+"");
                    if(Utils.isNotEmpty(djs)){
                        tv_djs_time.start(Long.parseLong(djs)*1000);
                    }
                    ArrayList<ShopsHomeBean.BannerBean> bannerList = (ArrayList<ShopsHomeBean.BannerBean>) JSON.parseArray(obj.getString("banner"), ShopsHomeBean.BannerBean.class);
                    ArrayList<ShopsHomeBean.CateBean> cateList = (ArrayList<ShopsHomeBean.CateBean>) JSON.parseArray(obj.getString("cate"), ShopsHomeBean.CateBean.class);
                    ArrayList<ShopsHomeBean.GoodslistBean> goodsList = (ArrayList<ShopsHomeBean.GoodslistBean>) JSON.parseArray(obj.getString("goodslist"), ShopsHomeBean.GoodslistBean.class);
                    ArrayList<ShopsHomeBean.StorelistBean> storeList = (ArrayList<ShopsHomeBean.StorelistBean>) JSON.parseArray(obj.getString("storelist"), ShopsHomeBean.StorelistBean.class);
                    ArrayList<ShopsHomeBean.LivelistBean> liveList = (ArrayList<ShopsHomeBean.LivelistBean>) JSON.parseArray(obj.getString("livelist"), ShopsHomeBean.LivelistBean.class);
                    if(Utils.isNotEmpty(bannerList)){
                        mBanner.setImages(bannerList);
                        mBanner.start();
                        mBanner.setOnBannerListener(new OnBannerListener() {
                            @Override
                            public void OnBannerClick(int position) {
                                String url=bannerList.get(position).getSlide_url();
                                String id=bannerList.get(position).getInfo_id();

                                if(Utils.isNotEmpty(id) && id.equals("0")){
                                    if(Utils.isNotEmpty(url)){
                                        WebViewActivity.forward(mContext,url);
                                    }else{

                                    }

                                }else{
                                    Intent intent = new Intent(mContext, MyShopDetailActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("status", bannerList.get(position).getSlide_status());
                                    intent.putExtra("store_id", "0");
                                    mContext.startActivity(intent);

                                }
                            }
                        });
                    }
                    if(Utils.isNotEmpty(cateList)){
//                              * id : 1
//                                * name : 问医
//                                * thumb : http://miaocha.yanshi.hbbeisheng.com/uploads/livecate/20200623/8deba0cf3a25b441287912a9a342819f.png
//         * des : 是时候展示你真正的技术了
//                                * orderno : 1


                        List<ShopsHomeBean.CateBean> newCateList = new ArrayList<>();
                        for(int i=0;i<cateList.size();i++){
//                            if(i<=4){
//                                newCateList.add(cateList.get(i));
//                            }
                            if(i==0){
                                cateList.get(i).setName("养生");
                                newCateList.add(cateList.get(i));
                            }
                            if(i==1){
                                cateList.get(i).setName("茶园");
                                newCateList.add(cateList.get(i));
                            }
                            if(i==2){
                                cateList.get(i).setName("村货");
                                newCateList.add(cateList.get(i));
                            }
                            if(i==3){
                                cateList.get(i).setName("娱乐");
                                newCateList.add(cateList.get(i));
                            }

//                            if(i==4){
//                                cateList.get(i).setName("家用");
//                            }
                        }

//                        mAdapter.setNewData(newCateList);
//                        ShopsHomeBean.CateBean cateBean2 = new ShopsHomeBean.CateBean();
//                        cateBean2.setId("7");
//                        cateBean2.setName("酒店");
//                        cateBean2.setThumb(cateList.get(4).getThumb());
//                        cateBean2.setOrderno("6");
//                        cateList.add(cateBean2);
//                        mAdapter.setNewData(cateList);
                        mAdapter.setNewData(newCateList);
                    }
                    if(Utils.isNotEmpty(goodsList)){
                        msAdapter.setNewData(goodsList);
                        ll_goods.setVisibility(View.VISIBLE);
                    }else{
                        ll_goods.setVisibility(View.GONE);
                    }
                    if(Utils.isNotEmpty(storeList)){
                        shopAdapter.setNewData(storeList);
                    }
                    if(Utils.isNotEmpty(liveList)){
                        zbAdapter.setNewData(liveList);
                    }
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }
}
