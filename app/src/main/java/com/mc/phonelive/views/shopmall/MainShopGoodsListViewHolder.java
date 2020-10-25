//package com.zhiboshow.phonelive.views.shopmall;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import com.alibaba.fastjson.JSON;
//import com.zhiboshow.phonelive.AppConfig;
//import com.zhiboshow.phonelive.Constants;
//import com.zhiboshow.phonelive.R;
//import com.zhiboshow.phonelive.activity.MyShopDetailActivity;
//import com.zhiboshow.phonelive.activity.WebViewActivity;
//import com.zhiboshow.phonelive.activity.shop.CartMainActivity;
//import com.zhiboshow.phonelive.activity.shop.MyWalletMainActivity;
//import com.zhiboshow.phonelive.activity.shop.ShopMallGoodsSearchActivity;
//import com.zhiboshow.phonelive.adapter.ViewPagerAdapter;
//import com.zhiboshow.phonelive.bean.ShopMallPicBean;
//import com.zhiboshow.phonelive.custom.ShopViewPagerIndicator;
//import com.zhiboshow.phonelive.http.HttpCallback;
//import com.zhiboshow.phonelive.http.HttpUtil;
//import com.zhiboshow.phonelive.interfaces.MainAppBarExpandListener;
//import com.zhiboshow.phonelive.utils.CommentUtil;
//import com.zhiboshow.phonelive.utils.DataSafeUtils;
//import com.zhiboshow.phonelive.utils.DialogUitl;
//import com.zhiboshow.phonelive.views.AbsMainViewHolder;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cece.com.bannerlib.callback.OnItemViewClickListener;
//import cece.com.bannerlib.model.PicBean;
//
//import static cece.com.bannerlib.BannerGetData.getBannerData;
//
///**
// * 商城主界面
// */
//
//public class MainShopGoodsListViewHolder extends AbsMainViewHolder implements OnItemViewClickListener, View.OnClickListener {
//
//    ImageView shopSearchImg,forward_cart;
//    RelativeLayout rl_banner;
//    private LinearLayout shop_menu_01,shop_menu_02,shop_menu_03,shop_menu_04;
//    private ShopViewPagerIndicator mIndicator;
//    private ViewPager mViewPager;
//    private AbsMainShopMallListViewHolder[] mViewHolders;
//    public MainShopGoodsListViewHolder(Context context, ViewGroup parentView) {
//        super(context, parentView);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.view_main_shop_goods_list;
//    }
//
//    @Override
//    public void init() {
//        shopSearchImg = (ImageView) findViewById(R.id.shop_search_img);
//        forward_cart = (ImageView) findViewById(R.id.forward_cart);
//        shop_menu_01 = (LinearLayout) findViewById(R.id.shop_menu_01);
//        shop_menu_02 = (LinearLayout) findViewById(R.id.shop_menu_02);
//        shop_menu_03 = (LinearLayout) findViewById(R.id.shop_menu_03);
//        shop_menu_04 = (LinearLayout) findViewById(R.id.shop_menu_04);
//        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
//        mIndicator = (ShopViewPagerIndicator) findViewById(R.id.indicator);
//        mViewPager = (ViewPager) findViewById(R.id.viewPager);
//        shop_menu_01.setOnClickListener(this);
//        shop_menu_02.setOnClickListener(this);
//        shop_menu_03.setOnClickListener(this);
//        shop_menu_04.setOnClickListener(this);
//        forward_cart.setOnClickListener(this);
//        shopSearchImg.setOnClickListener(this);
////        ShopMallMainBean.DataBean.InfoBean infoBean = ShopMallMainBean.getShopData();
////        setAdvAdapterData(infoBean.getAdv_list());
////        getShopLoadData();
//
//        setInitMagicIndicator();
//
//        initHttpData();
//    }
//
//    /**
//     * 通过接口获取-轮播图
//     */
//    private void initHttpData() {
//        HttpUtil.GetShopMall("1", "1", "1","", new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//                if (info.length>0){
//                    try {
//                        Log.v("tags",info[0]);
//                        JSONObject resJson = new JSONObject(info[0]);
//                        List<ShopMallPicBean> images = JSON.parseArray(resJson.getString("slide"), ShopMallPicBean.class);
//                        if (images.size() > 0) {
//                           rl_banner.setVisibility(View.VISIBLE);
//                            setAdvAdapterData(images);
//                        } else {
//                            rl_banner.setVisibility(View.GONE);
//                        }
//                    } catch (JSONException e) {
//                    }
//                }
//            }
//            @Override
//            public boolean showLoadingDialog() {
//                return true;
//            }
//
//            @Override
//            public Dialog createLoadingDialog() {
//                return DialogUitl.loadingDialog(mContext);
//            }
//        });
//    }
//
//    private void setInitMagicIndicator() {
//        mViewPager.setOffscreenPageLimit(2);
//        mViewHolders = new AbsMainShopMallListViewHolder[2];
//        mViewHolders[0] = new MainShopMainListViewHolder(mContext, mViewPager);
//        mViewHolders[1] = new MainShopMainListViewHolder(mContext, mViewPager);
//        List<View> list = new ArrayList<>();
//        MainAppBarExpandListener expandListener = new MainAppBarExpandListener() {
//            @Override
//            public void onExpand(boolean expand) {
//                if (mViewPager != null) {
//                    mViewHolders[mViewPager.getCurrentItem()].setCanRefresh(expand);
//                }
//            }
//        };
//        for (AbsMainShopMallListViewHolder vh : mViewHolders) {
//            vh.setAppBarExpandListener(expandListener);
//            list.add(vh.getContentView());
//
//        }
//        mViewPager.setAdapter(new ViewPagerAdapter(list));
//        mViewPager.setCurrentItem(0);
//        mIndicator = (ShopViewPagerIndicator) findViewById(R.id.indicator);
//        mIndicator.setTitles(new String[]{"爆款推荐", "折扣专区"});
//        mIndicator.setViewPager(mViewPager);
//        mIndicator.setListener(new ShopViewPagerIndicator.OnPageChangeListener() {
//            @Override
//            public void onTabClick(int position) {
//                mViewHolders[position].refreshData(position+1+"");
//            }
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (position == 1 && positionOffset == 0) {
//                    positionOffset = 1;
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                mViewHolders[position].loadData();
//                mViewHolders[position].refreshData(position+1+"");
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        setCurrentPage(0);
//    }
//
//
//    public void setCurrentPage(int position) {
//        if (mViewHolders != null) {
//            for (int i = 0, length = mViewHolders.length; i < length; i++) {
//                if (position == i) {
//                    mViewHolders[i].setNeedDispatch(true);
//                } else {
//                    mViewHolders[i].setNeedDispatch(false);
//                }
//            }
//            if (position == 0) {
//                mViewHolders[0].loadData();
//            }
//        }
//        if (mViewPager != null) {
//            mViewPager.setCurrentItem(position, false);
//        }
//    }
//
//
//    /**
//     * BannerLayout实现轮播图效果
//     *
//     * @param adv_list
//     */
//    private void setAdvAdapterData(List<ShopMallPicBean> adv_list) {
//        ViewGroup.LayoutParams params = rl_banner.getLayoutParams();
//        params.width = CommentUtil.getDisplayWidth(mContext);
//        params.height = (int) ((int) (params.width * 0.42));
//        rl_banner.setLayoutParams(params);
//        List<PicBean> list = new ArrayList<>();
//        for (int i = 0; i < adv_list.size(); i++) {
//            PicBean b = new PicBean();
//            b.setTitle("");
//            b.setUrl(adv_list.get(i).getSlide_url());
//            b.setType("0");
//            b.setPic(adv_list.get(i).getSlide_pic());
//            b.setId(adv_list.get(i).getInfo_id());
//            b.setUid(adv_list.get(i).getGoods_store_uid());
//            list.add(b);
//        }
//        //两种效果 带滑动缩放效果以及圆角以及普通轮播图
//        getBannerData(mContext, this, rl_banner, list, false,true, mContext.getResources().getColor(R.color.gray1), mContext.getResources().getColor(R.color.colorAccent), 5, 5, 1);
//
//    }
//
//    @Override
//    public void onItemClick(View view, PicBean bean) {
//        if (!DataSafeUtils.isEmpty(bean.getUrl())){
//            forward(mContext,bean.getUrl());
//        }else if (DataSafeUtils.isEmpty(bean.getUrl()) && !DataSafeUtils.isEmpty(bean.getId()) && !bean.getId().equals("0")) {
//            Intent intent = new Intent(mContext, MyShopDetailActivity.class);
//            intent.putExtra("id", bean.getId() + "");
//            if (bean.getUid().equals( AppConfig.getInstance().getUid())){
//                intent.putExtra("status", "0");
//            }else{
//                intent.putExtra("status", "1");
//            }
//            intent.putExtra("store_id", AppConfig.getInstance().getUid());
//
//            mContext.startActivity(intent);
//        }else if (!DataSafeUtils.isEmpty(bean.getUrl()) && !DataSafeUtils.isEmpty(bean.getId())){
//            forward(mContext,bean.getUrl());
//        }
//    }
//
//    private void forward(Context context, String url) {
//        url += "&uid=" + AppConfig.getInstance().getUid() + "&token=" + AppConfig.getInstance().getToken();
//        Intent intent = new Intent(context, WebViewActivity.class);
//        intent.putExtra(Constants.URL, url);
//        context.startActivity(intent);
//    }
//
//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent();
//        switch (v.getId()){
//            case R.id.shop_menu_01://我的钱包
////                ToastUtil.show("开发中...");
//                intent = new Intent(mContext, MyWalletMainActivity.class);
//                intent.putExtra("state","1");
//                mContext.startActivity(intent);
//                break;
//            case R.id.shop_menu_02://我的团队
////                ToastUtil.show("开发中...");
//                mContext.startActivity(new Intent(mContext,MyTeamListActivity.class));
//                break;
//            case R.id.shop_menu_03://每日签到
////                ToastUtil.show("开发中...");
//                intent = new Intent(mContext, MyWalletMainActivity.class);
//                intent.putExtra("state","0");
//                mContext.startActivity(intent);
//                break;
//            case R.id.shop_menu_04://邀请好友
////                ToastUtil.show("开发中...");
//                String url="http://zhiboshow.yanshi.hbbeisheng.com/index.php?g=Appapi&m=Agent&a=index";
//                WebViewActivity.forward(mContext, url);
//                break;
//            case R.id.forward_cart://进入购物车
//                intent = new Intent(mContext, CartMainActivity.class);
//                mContext.startActivity(intent);
//              mContext.startActivity(intent);
//                break;
//            case R.id.shop_search_img:
//                 intent = new Intent(mContext, ShopMallGoodsSearchActivity.class);
//                mContext.startActivity(intent);
//                break;
//        }
//    }
//}
