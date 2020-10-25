package com.mc.phonelive.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.MyShopActivity;
import com.mc.phonelive.activity.MyShopDetailActivity;
import com.mc.phonelive.bean.LiveGuardInfo;
import com.mc.phonelive.bean.MyShopBean;
import com.mc.phonelive.bean.MyShopGoodsList;
import com.mc.phonelive.custom.RatioRoundImageView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/12.
 * 店铺商品列表的弹窗
 */

public class ShopShowDialogFragment extends AbsDialogFragment {

    private TextView mShopName;
    private TextView mShopSales;
    private RatioRoundImageView mShopImg;
    private TextView mShopSend;
    private RecyclerView recyclerView;
    private RelativeLayout no_data;
    private SmartRefreshLayout refreshLayout;
    private View mLoading;
    private String mLiveUid = "";

    private BaseQuickAdapter<MyShopGoodsList.DataBean.InfoBean.GoodsListBean, BaseViewHolder> mLiveGiftPagerAdapter;
    private MyShopBean mShopBean;
    List<MyShopGoodsList.DataBean.InfoBean.GoodsListBean> mList = new ArrayList<>();
    private LiveGuardInfo mLiveGuardInfo;
    private int page = 1;
    private String sid="";

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_shoplist;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog3;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    public void setLiveGuardInfo(LiveGuardInfo liveGuardInfo) {
        mLiveGuardInfo = liveGuardInfo;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mShopName = (TextView) mRootView.findViewById(R.id.shop_name);
        mShopSales = (TextView) mRootView.findViewById(R.id.shop_sales_num);
        mShopImg = (RatioRoundImageView) mRootView.findViewById(R.id.shop_img);
        mShopSend = (TextView) mRootView.findViewById(R.id.send_shop);

        mLoading = mRootView.findViewById(R.id.loading);
        no_data = mRootView.findViewById(R.id.no_data);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        recyclerView = mRootView.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        no_data.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mLiveUid = bundle.getString(Constants.LIVE_UID);
        }
        showGiftList(mList);
        loadData(1);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                loadData(page);
            }
        });

        mShopSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("mmmmm:",mLiveUid);
                if(Utils.isNotEmpty(sid)){
                    Intent intent = new Intent(mContext, MyShopActivity.class);
                    intent.putExtra("status", "1");
//                intent.putExtra("store_id", mLiveUid);
                    intent.putExtra("store_id", sid);
                    mContext.startActivity(intent);
                }


            }
        });
    }


    private void loadData(int page) {
        HttpUtil.shopZbGoodsList(page, mLiveUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

                if (code == 0 && info.length > 0) {
//                        JSONObject obj = JSON.parseObject(info[0]);
                    MyShopGoodsList.DataBean.InfoBean infoBean = JSON.parseObject(info[0], MyShopGoodsList.DataBean.InfoBean.class);
                    if (!DataSafeUtils.isEmpty(infoBean.getStore_info())) {
                        MyShopGoodsList.DataBean.InfoBean.StoreInfoBean store = infoBean.getStore_info();
                        mShopName.setText(store.getTitle() + "");
                        sid=store.getId();
//                        mShopSales.setText("已售"+"件");
                        if (!DataSafeUtils.isEmpty(store.getLogo()))
                            Glide.with(mContext).load(store.getLogo()).into(mShopImg);
                    }
                    if (!DataSafeUtils.isEmpty(infoBean.getGoods_list()) && infoBean.getGoods_list().size() > 0) {
                        List<MyShopGoodsList.DataBean.InfoBean.GoodsListBean> list = infoBean.getGoods_list();
                        if (page == 1) {
                            mLiveGiftPagerAdapter.setNewData(list);
                        } else {
                            mLiveGiftPagerAdapter.addData(list);
                            refreshLayout.finishLoadMore();
                        }


                        if (infoBean.getGoods_list().size() < 10) {
                            if (refreshLayout != null) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                                refreshLayout.setNoMoreData(true);
                            }
                        } else {
                            if (refreshLayout != null) {
                                refreshLayout.setEnableLoadMore(true);
                                refreshLayout.setNoMoreData(false);
                            }
                        }

                    } else {

                        if (page == 1) {
                            if (recyclerView != null) {
                                recyclerView.setVisibility(View.GONE);
                            }
                            if (no_data != null) {
                                no_data.setVisibility(View.VISIBLE);
                            }
                        }
                        if (refreshLayout != null) {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            refreshLayout.setNoMoreData(true);
                        }
                    }
                } else {
                    if (page == 1) {
                        if (recyclerView != null) {
                            recyclerView.setVisibility(View.GONE);
                        }
                        if (no_data != null) {
                            no_data.setVisibility(View.VISIBLE);
                        }
                        ToastUtil.show("暂无商品");
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mLoading != null) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
                if (refreshLayout != null) {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    private void showGiftList(List<MyShopGoodsList.DataBean.InfoBean.GoodsListBean> list) {
        mLiveGiftPagerAdapter = new BaseQuickAdapter<MyShopGoodsList.DataBean.InfoBean.GoodsListBean, BaseViewHolder>(R.layout.activity_live_shop_item_layout, list) {
            @Override
            protected void convert(BaseViewHolder helper, MyShopGoodsList.DataBean.InfoBean.GoodsListBean item) {
                helper.setText(R.id.item_name, item.getGoods_name());
                helper.setText(R.id.item_price, "￥" + item.getPrice() + "");
//                helper.setText(R.id.item_price1, "￥" + item.getOt_price());
//                TextView textView = helper.getView(R.id.item_price1);
//                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                ImageView imgs = (ImageView) helper.getView(R.id.item_img);
                if (!DataSafeUtils.isEmpty(item.getImg_list())) {
                    Glide.with(mContext).load(item.getImg_list()[0]).into(imgs);
                }
                helper.getView(R.id.item_shop_cart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addGoodsToCart(item.getId());
                    }
                });
                helper.getView(R.id.item_shop_forwad).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!DataSafeUtils.isEmpty(item.getStock()))
                            if (Integer.parseInt(item.getStock()) > 0) {
//                                Intent intent = new Intent(mContext, GoodsOrderActivity.class);
//                                intent.putExtra("goods_id", item.getId() + "");
//                                mContext.startActivity(intent);
                                Intent intent = new Intent(mContext, MyShopDetailActivity.class);
                                intent.putExtra("id", item.getId());
//                intent.putExtra("status", mySaveAdapter.getData().get(position).get());
                                intent.putExtra("store_id", "0");
                                mContext.startActivity(intent);
                            }
                    }
                });
            }
        };
        recyclerView.setAdapter(mLiveGiftPagerAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);

        mLiveGiftPagerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyShopGoodsList.DataBean.InfoBean.GoodsListBean data = (MyShopGoodsList.DataBean.InfoBean.GoodsListBean) adapter.getData().get(position);
                Intent intent = new Intent(mContext, MyShopDetailActivity.class);
                intent.putExtra("id", data.getId() + "");
                intent.putExtra("status", "1");
                intent.putExtra("store_id", mLiveUid);
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
    public void onDestroy() {
        HttpUtil.cancel(HttpConsts.GET_GOODS_LIST);
        HttpUtil.cancel(HttpConsts.ADDCART);

        super.onDestroy();
    }


}
