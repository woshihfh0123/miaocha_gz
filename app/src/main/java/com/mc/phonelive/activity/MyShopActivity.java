package com.mc.phonelive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.shop.MyOrderActivity;
import com.mc.phonelive.im.EventBusModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.MyShopBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.AppBarStateListener;
import com.mc.phonelive.interfaces.MainAppBarExpandListener;
import com.mc.phonelive.interfaces.MainAppBarLayoutListener;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mc.phonelive.bean.MyShopBean.*;

/**
 * 我的小店
 */
public class MyShopActivity extends AbsActivity implements View.OnClickListener {
    private RoundedImageView mShopImg;
    private TextView mShopName, mShopContent, titleView, all_foodnumber;
    private TextView no_data;
    private TextView forward_myorder;
    private ImageView HeadImg;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout refreshLayout;
    private ClassicsFooter footer;
    private LinearLayout add_layout;
    private FrameLayout mTop;

    private BaseQuickAdapter<MyShopBean.DataBean.InfoBean.ListBean, BaseViewHolder> mShopAdapter;
    private List<MyShopBean.DataBean.InfoBean.ListBean> mList = new ArrayList<>();

    protected AppBarLayout mAppBarLayout;
    protected MainAppBarLayoutListener mAppBarLayoutListener;
    protected MainAppBarExpandListener mAppBarExpandListener;
    protected boolean mAppBarExpand = true;//AppBarLayout是否展开
    protected boolean mNeedDispatch;//是否需要执行AppBarLayoutListener的回调

    private String mType;//
    private String store_id = "0";
    private int pages;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_myshop;
    }

    @Override
    protected void main() {
        initView();
        appBarInit();
    }

    @Override
    protected boolean isStatusBarWhite() {
        return true;
    }

    private void appBarInit() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        if (mAppBarLayout != null) {
            mAppBarLayout.addOnOffsetChangedListener(new AppBarStateListener() {

                @Override
                public void onStateChanged(AppBarLayout appBarLayout, int state) {
                    switch (state) {
                        case AppBarStateListener.EXPANDED:
                            mAppBarExpand = true;
                            if (mAppBarExpandListener != null) {
                                mAppBarExpandListener.onExpand(true);
                            }
                            break;
                        case AppBarStateListener.MIDDLE:
                        case AppBarStateListener.COLLAPSED:
                            mAppBarExpand = false;
                            if (mAppBarExpandListener != null) {
                                mAppBarExpandListener.onExpand(false);
                            }
                            break;
                    }
                }
            });
            mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    float totalScrollRange = appBarLayout.getTotalScrollRange();
                    Log.v("tags", totalScrollRange + "-----" + verticalOffset);
                    float rate = -1 * verticalOffset / totalScrollRange;
                    if (mNeedDispatch && mAppBarLayoutListener != null) {
                        mAppBarLayoutListener.onOffsetChanged(rate);
                    }
                }
            });
        }
        mAppBarLayoutListener = new MainAppBarLayoutListener() {
            @Override
            public void onOffsetChanged(float rate) {
                Log.v("tags", rate + "----rate");
                titleView.setAlpha(rate);
                if (rate > 0.4) {
                    mTop.setBackgroundResource(R.color.textColor22);
                } else {
                    mTop.setBackgroundResource(R.color.color801);
                }
            }
        };
        mNeedDispatch = true;
    }

    private void initView() {
        refreshLayout = findViewById(R.id.refreshLayout);
        forward_myorder = findViewById(R.id.forward_myorder);
        mTop = findViewById(R.id.top);
        footer = findViewById(R.id.footer);
        HeadImg = findViewById(R.id.HeadImg);
        mShopImg = findViewById(R.id.avatar);
        all_foodnumber = findViewById(R.id.all_foodnumber);
        titleView = findViewById(R.id.titleView);
        mShopName = findViewById(R.id.name);
        mShopContent = findViewById(R.id.content_tv);
        mRecyclerView = findViewById(R.id.recyclerView);
        add_layout = findViewById(R.id.add_layout);
        no_data = findViewById(R.id.no_data);
        findViewById(R.id.btn_back).setOnClickListener(this);
        forward_myorder.setOnClickListener(this);
//        refreshLayout.setEnableRefresh(false);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String status = bundle.getString("status");
            String store_id = bundle.getString("store_id");
            if (!DataSafeUtils.isEmpty(store_id)) {
                this.store_id = store_id;
            }
            Log.v("tags", status + "------status");
            if (!DataSafeUtils.isEmpty(status)) {
                this.mType = status;
                if (status.equals("1")) {
                    add_layout.setVisibility(View.GONE);
                    forward_myorder.setVisibility(View.GONE);
                } else {
                    this.store_id = AppConfig.getInstance().getUid();
//                    add_layout.setVisibility(View.VISIBLE);
                    forward_myorder.setVisibility(View.VISIBLE);
                }
            }
        }
        no_data.setVisibility(View.GONE);
        setShopAdapater(mList);


//        refreshLayout.autoRefresh();

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pages = 1;
                getData(pages);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pages++;
                getData(pages);
            }
        });
        add_layout.setOnClickListener(this);

//        getData(pages);
//        refreshLayout.autoRefresh();
    }

    @Override
    protected void onResuname01() {
        pages = 1;
        showDialog();
        getData(pages);
    }

    private void getData(int page) {
        HttpUtil.shopListNew(page, store_id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                dismissDialog();
                if (code == 0) {
                    List<MyShopBean.DataBean.InfoBean> infolist = JSON.parseArray(Arrays.toString(info), MyShopBean.DataBean.InfoBean.class);
                    if (!DataSafeUtils.isEmpty(infolist)) {
                        setShopData(infolist.get(0).getStoreinfo());
                        if (!DataSafeUtils.isEmpty(infolist.get(0).getList())) {
                            if (mRecyclerView != null) {
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            if (no_data != null) {
                                no_data.setVisibility(View.GONE);
                            }
                            if (pages == 1) {
                                mShopAdapter.setNewData(infolist.get(0).getList());
                                refreshLayout.finishRefresh();
                            } else {
                                mShopAdapter.addData(infolist.get(0).getList());
                                refreshLayout.finishLoadMore();
                            }

                            if (infolist.get(0).getList().size() < 15) {
                                if (refreshLayout != null) {
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                    refreshLayout.setNoMoreData(true);
                                    footer.setNoMoreData(true);
                                }
                            } else {
                                if (refreshLayout != null) {
                                    refreshLayout.setEnableLoadMore(true);
                                    refreshLayout.setNoMoreData(false);
                                    footer.setNoMoreData(false);
                                }
                            }
                        } else {

                            if (page == 1) {
                                if (mRecyclerView != null) {
                                    mRecyclerView.setVisibility(View.GONE);
                                }
                                if (no_data != null) {
                                    no_data.setVisibility(View.VISIBLE);
                                }
                                all_foodnumber.setText("全部商品");
                            }
                            if (refreshLayout != null) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                                refreshLayout.setNoMoreData(true);
                                footer.setNoMoreData(true);
                            }
                        }
                    }
                }
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });
    }


    /**
     * 店铺基本信息
     *
     * @param mList
     */
    private void setShopData(MyShopBean.DataBean.InfoBean.StoreinfoBean mList) {
        MyShopBean.DataBean.InfoBean.StoreinfoBean bean = mList;
        if (!DataSafeUtils.isEmpty(bean.getLogo())) {
            RequestOptions options = new RequestOptions().error(R.mipmap.ic_launcher).centerCrop().placeholder(R.mipmap.ic_launcher);
            Glide.with(mContext).load(bean.getLogo()).apply(options).into(mShopImg);
            Glide.with(mContext).load(bean.getLogo()).apply(options).into(HeadImg);
        }
        titleView.setText(bean.getTitle());
        mShopName.setText(bean.getTitle());
        if (!DataSafeUtils.isEmpty(bean.getInfo())) {
            mShopContent.setVisibility(View.VISIBLE);
            mShopContent.setText(bean.getInfo());
        } else {
            mShopContent.setVisibility(View.GONE);
        }
//        all_foodnumber.setText("全部商品（" + bean.getCounts() + "）");
    }

    private void setShopAdapater(List<MyShopBean.DataBean.InfoBean.ListBean> list) {
        mShopAdapter = new BaseQuickAdapter<MyShopBean.DataBean.InfoBean.ListBean, BaseViewHolder>(R.layout.activity_myshop_item_layout, list) {
            @Override
            protected void convert(BaseViewHolder helper, MyShopBean.DataBean.InfoBean.ListBean item) {
                helper.setText(R.id.item_name, item.getTitle());
                helper.setText(R.id.item_price, "￥" + item.getPrice() + "");
//                helper.setText(R.id.item_price1,"￥"+item.getOt_price());
//                TextView textView =helper.getView(R.id.item_price1);
//                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG );

                TextView isshop_tv = helper.getView(R.id.isshop_tv);
//                if (!DataSafeUtils.isEmpty(item.getIs_show())) {
//                    if (item.getIs_show().equals("2")) {
//                        isshop_tv.setVisibility(View.VISIBLE);
//                    } else {
//                        isshop_tv.setVisibility(View.GONE);
//                    }
//                }
                String imglist = item.getGoods_img();

                ImageView imgs = (ImageView) helper.getView(R.id.item_img);
                ViewGroup.LayoutParams params = imgs.getLayoutParams();
                params.width = CommentUtil.getDisplayWidth(mContext) / 2 - 20;
                params.height = params.width;
                imgs.setLayoutParams(params);
                if (Utils.isNotEmpty(imglist)){

                    Glide.with(mContext).load(imglist).into(imgs);
                }else{
                    imgs.setImageResource(R.drawable.default_img);
                }

                helper.getView(R.id.add_cart_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Wave).duration(1000).playOn(helper.getView(R.id.add_cart_img));
                        addGoodsToCart(item.getId());
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mShopAdapter);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(manager);

        mShopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MyShopBean.DataBean.InfoBean.ListBean bean = (DataBean.InfoBean.ListBean) adapter.getData().get(position);
                Intent intent = new Intent(mContext, MyShopDetailActivity.class);
                EventBus.getDefault().post(new EventBusModel("refresh_detail_data",bean.getId()));


                intent.putExtra("id", bean.getId());
                intent.putExtra("status", mType);
                intent.putExtra("store_id", "0");
                mContext.startActivity(intent);



            }
        });
    }

    /**
     * 加入购物车
     *
     * @param id
     */
    private void addGoodsToCart(String id) {
        HttpUtil.addGoodsToCart(id, "1", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags", code + "------" + msg);
                if (code == 0) {
                    ToastUtil.show("加入成功");
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtil.cancel(HttpConsts.GET_USER_STORE);
        HttpUtil.cancel(HttpConsts.ADDCART);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_layout:
                mContext.startActivity(new Intent(mContext, myShopAddActivity.class));
                break;
            case R.id.forward_myorder://进入我的订单MyOrderActivity
                startActivity(new Intent(mContext, MyOrderActivity.class));
//                startActivity(new Intent(mContext, MyMerchantOrderActivity.class));
//                startActivity(new Intent(mContext, OrderMyActivity.class));
                break;
            case R.id.btn_back:
                this.finish();
                break;
        }
    }
}
