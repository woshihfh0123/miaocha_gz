package com.mc.phonelive.views.shopmall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bspopupwindow.model.TreeVO;
import com.bspopupwindow.utils.Utils;
import com.bspopupwindow.view.BSPopupWindowsTitle;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.AppContext;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.MyShopDetailActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.activity.shop.CartMainActivity;
import com.mc.phonelive.activity.shop.MyWalletMainActivity;
import com.mc.phonelive.activity.shop.ShopMallGoodsListActivity;
import com.mc.phonelive.activity.shop.ShopMallGoodsSearchActivity;
import com.mc.phonelive.activity.shop.ShopMallMoreMenuListActivity;
import com.mc.phonelive.adapter.MainShopMallListAdapter;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.adapter.shop.ShopMallMenuListAdapter;
import com.mc.phonelive.bean.Categorylist;
import com.mc.phonelive.bean.ShopMallBean;
import com.mc.phonelive.bean.ShopMallGoodsList;
import com.mc.phonelive.bean.ShopMallPicBean;
import com.mc.phonelive.custom.ItemDecoration;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.views.AbsMainViewHolder;

import java.util.ArrayList;
import java.util.List;

import cece.com.bannerlib.callback.OnItemViewClickListener;
import cece.com.bannerlib.model.PicBean;

import static cece.com.bannerlib.BannerGetData.getBannerData;

/**
 * 商城主界面
 */

public class MainShopMainViewHolder extends AbsMainViewHolder implements OnItemViewClickListener, View.OnClickListener {
    private View mDivider;
    ImageView shopSearchImg, forward_cart;
    RecyclerView menu_recyclerview;
    RelativeLayout rl_banner;
    AppBarLayout appBarLayout;
    protected RefreshView mRefreshView;
    private LinearLayout mTypeLayout01, mTypeLayout02, mTypeLayout03;
    private TextView mTypeTv01, mTypeTv02, mTypeTv03;
    private ImageView mTypeImg01, mTypeImg02, mTypeImg03;
    List<ShopMallBean.DataBean.InfoBean.ShopMallMenuList> mMenuList = new ArrayList<>();
    public static ArrayList<Categorylist> mTypelist = new ArrayList<>();//商品分类
    ShopMallMenuListAdapter mMenuAdapter;
    protected MainShopMallListAdapter mAdapter;
    private String mOrder = "";//1.类型排序 2.销量排序 3.价格排序
    private String mCateId = "0";//类型分类
    BSPopupWindowsTitle mBsPopupWindowsTitle;
    private int page = 1;
    private boolean isShowMenu = false;//处理点击筛选功能的时候，列表显示位置

    public MainShopMainViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_shop_list;
    }

    @Override
    public void init() {
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        menu_recyclerview = (RecyclerView) findViewById(R.id.menu_recyclerview);
        shopSearchImg = (ImageView) findViewById(R.id.shop_search_img);
        forward_cart = (ImageView) findViewById(R.id.forward_cart);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);

        mDivider = (View) findViewById(R.id.lines);
        mTypeLayout01 = (LinearLayout) findViewById(R.id.type_layout01);
        mTypeLayout02 = (LinearLayout) findViewById(R.id.type_layout02);
        mTypeLayout03 = (LinearLayout) findViewById(R.id.type_layout03);
        mTypeTv01 = (TextView) findViewById(R.id.type_tv01);
        mTypeTv02 = (TextView) findViewById(R.id.type_tv02);
        mTypeTv03 = (TextView) findViewById(R.id.type_tv03);
        mTypeImg01 = (ImageView) findViewById(R.id.type_img01);
        mTypeImg02 = (ImageView) findViewById(R.id.type_img02);
        mTypeImg03 = (ImageView) findViewById(R.id.type_img03);
        mTypeLayout01.setOnClickListener(this);
        mTypeLayout02.setOnClickListener(this);
        mTypeLayout03.setOnClickListener(this);


        forward_cart.setOnClickListener(this);
        shopSearchImg.setOnClickListener(this);
//        ShopMallMainBean.DataBean.InfoBean infoBean = ShopMallMainBean.getShopData();
//        setAdvAdapterData(infoBean.getAdv_list());
        getShopMenuAdapter(mMenuList);


        initHttpData();
    }


    /**
     * 通过接口获取-轮播图
     */
    private void initHttpData() {
        if (isShowMenu && page == 1) {
            appBarLayout.setExpanded(false);
        }
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 5, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setRefreshEnable(false);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<ShopMallGoodsList>() {
            @Override
            public RefreshAdapter<ShopMallGoodsList> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MainShopMallListAdapter(mContext, MainShopMallListAdapter.TYPE_PROFIT);
//                    mAdapter.setOnItemClickListener(MainShopMainListViewHolder.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                page = p;
                HttpUtil.GetShopMall(p + "", "1", mCateId, mOrder, "", callback);
            }

            @Override
            public List<ShopMallGoodsList> processData(String[] info) {
                try {
//                    if (mAdapter != null) {
//                        if (page == 1)
//                            mAdapter.clearData();
//                    }
                    ShopMallBean.DataBean.InfoBean infoBean = JSON.parseObject(info[0], ShopMallBean.DataBean.InfoBean.class);
                    if (infoBean.getSlide().size() > 0) {
                        rl_banner.setVisibility(View.VISIBLE);
                        setAdvAdapterData(infoBean.getSlide());
                    } else {
                        rl_banner.setVisibility(View.GONE);
                    }
                    if (!DataSafeUtils.isEmpty(infoBean.getCategory()) && infoBean.getCategory().size() > 0) {
                        if (mMenuAdapter != null) {
                            mMenuAdapter.setNewData(infoBean.getCategory());
                        }
                    }
                    if (!DataSafeUtils.isEmpty(infoBean.getCategorylist()) && infoBean.getCategorylist().size() > 0) {
                        mTypelist.clear();
                        mTypelist.addAll(infoBean.getCategorylist());
                    }
                    if (!DataSafeUtils.isEmpty(infoBean.getList()) && infoBean.getList().size() > 0) {
                        Log.v("tags", "size=" + infoBean.getList().size());
                        return infoBean.getList();
                    } else {
                        if (page == 1) {
                            if (mAdapter != null) {
                                mAdapter.clearData();
                            }
                            mRefreshView.showNoData();
                        } else {
                            mRefreshView.hideNoData();
                        }
                    }
                } catch (Exception e) {

                }
                return null;
            }

            @Override
            public void onRefresh(List<ShopMallGoodsList> list) {
                Log.v("tags", "刷新--------");
            }

            @Override
            public void onNoData(boolean noData) {
            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (dataCount < 20) {
                    mRefreshView.setLoadMoreEnable(false);
                } else {
                    mRefreshView.setLoadMoreEnable(true);
                }
            }
        });
        mLifeCycleListener = new LifeCycleAdapter() {

            @Override
            public void onDestroy() {
                L.e("main----MainListProfitViewHolder-------LifeCycle---->onDestroy");
                HttpUtil.cancel(HttpConsts.HOME_GETSHOP);
            }
        };

        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }


    /**
     * BannerLayout实现轮播图效果
     *
     * @param adv_list
     */
    private void setAdvAdapterData(List<ShopMallPicBean> adv_list) {
        ViewGroup.LayoutParams params = rl_banner.getLayoutParams();
        params.width = CommentUtil.getDisplayWidth(mContext);
        params.height = (int) ((int) (params.width * 0.42));
        rl_banner.setLayoutParams(params);
        List<PicBean> list = new ArrayList<>();
        for (int i = 0; i < adv_list.size(); i++) {
            PicBean b = new PicBean();
            b.setTitle("");
            b.setUrl(adv_list.get(i).getSlide_url());
            b.setType("0");
            b.setPic(adv_list.get(i).getSlide_pic());
            b.setId(adv_list.get(i).getInfo_id());
            b.setUid(adv_list.get(i).getGoods_store_uid());
            list.add(b);
        }
        //两种效果 带滑动缩放效果以及圆角以及普通轮播图
        getBannerData(mContext, this, rl_banner, list, false, true, mContext.getResources().getColor(R.color.gray1), mContext.getResources().getColor(R.color.colorAccent), 5, 5, 1);

    }

    @Override
    public void onItemClick(View view, PicBean bean) {
        if (!DataSafeUtils.isEmpty(bean.getUrl())) {
            forward(mContext, bean.getUrl());
        } else if (DataSafeUtils.isEmpty(bean.getUrl()) && !DataSafeUtils.isEmpty(bean.getId()) && !bean.getId().equals("0")) {
            Intent intent = new Intent(mContext, MyShopDetailActivity.class);
            intent.putExtra("id", bean.getId() + "");
            if (bean.getUid().equals(AppConfig.getInstance().getUid())) {
                intent.putExtra("status", "0");
            } else {
                intent.putExtra("status", "1");
            }
            intent.putExtra("store_id", AppConfig.getInstance().getUid());

            mContext.startActivity(intent);
        } else if (!DataSafeUtils.isEmpty(bean.getUrl()) && !DataSafeUtils.isEmpty(bean.getId())) {
            forward(mContext, bean.getUrl());
        }
    }


    /**
     * menu按钮排序
     *
     * @param list
     */
    private void getShopMenuAdapter(List<ShopMallBean.DataBean.InfoBean.ShopMallMenuList> list) {

        mMenuAdapter = new ShopMallMenuListAdapter(mContext, R.layout.view_main_shop_menu, list);
        menu_recyclerview.setAdapter(mMenuAdapter);
        menu_recyclerview.setLayoutManager(new GridLayoutManager(mContext, 4));
        mMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopMallBean.DataBean.InfoBean.ShopMallMenuList data = mMenuAdapter.getData().get(position);
                if (data.getId().equals("100")) {
                    mContext.startActivity(new Intent(mContext, ShopMallMoreMenuListActivity.class));
                } else {
                    if (!DataSafeUtils.isEmpty(data.getWeb_url())) {
                        WebViewActivity.forward(mContext, data.getWeb_url());
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("typelist", mTypelist);
                        bundle.putString("id", data.getId());
                        Intent intent = new Intent(mContext, ShopMallGoodsListActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
            }
        });
    }

    private void forward(Context context, String url) {
        url += "&uid=" + AppConfig.getInstance().getUid() + "&token=" + AppConfig.getInstance().getToken();
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.URL, url);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.type_layout01:
                isShowMenu = true;
                appBarLayout.setExpanded(false, false);
//                mTypeTv01.setTextColor(mContext.getResources().getColor(R.color.redlive));
//                mTypeTv02.setTextColor(mContext.getResources().getColor(R.color.textColor));
//                mTypeTv03.setTextColor(mContext.getResources().getColor(R.color.textColor));
//                mTypeImg01.setImageResource(R.mipmap.b_icon_choise);
//                mTypeImg02.setImageResource(R.mipmap.b_icon);
//                mTypeImg03.setImageResource(R.mipmap.b_icon);
//                if (!mOrder.equals("1")) {
//                    mOrder = "1";
//                    if (mRefreshView != null) {
//                        mRefreshView.initData();
//                    }
//                }
                //一级菜单
                mBsPopupWindowsTitle = new BSPopupWindowsTitle(AppContext.sInstance, initPopDowns(), callback, 500);
                mBsPopupWindowsTitle.showAsDropDown(mDivider);
                break;
            case R.id.type_layout02:
                isShowMenu = true;
                appBarLayout.setExpanded(false);
//                mTypeTv01.setTextColor(mContext.getResources().getColor(R.color.textColor));
                mTypeTv02.setTextColor(mContext.getResources().getColor(R.color.redlive));
                mTypeTv03.setTextColor(mContext.getResources().getColor(R.color.textColor));
//                mTypeImg01.setImageResource(R.mipmap.b_icon);
                mTypeImg02.setImageResource(R.mipmap.b_icon_choise);
                mTypeImg03.setImageResource(R.mipmap.b_icon);
//                if (!mOrder.equals("2")) {
                mOrder = "2";
                if (mRefreshView != null) {
                    mRefreshView.initData();
                }
//                }
                break;
            case R.id.type_layout03:
                isShowMenu = true;
                appBarLayout.setExpanded(false);
//                mTypeTv01.setTextColor(mContext.getResources().getColor(R.color.textColor));
                mTypeTv02.setTextColor(mContext.getResources().getColor(R.color.textColor));
                mTypeTv03.setTextColor(mContext.getResources().getColor(R.color.redlive));
//                mTypeImg01.setImageResource(R.mipmap.b_icon);
                mTypeImg02.setImageResource(R.mipmap.b_icon);
                mTypeImg03.setImageResource(R.mipmap.b_icon_choise);
//                if (!mOrder.equals("3")) {
                mOrder = "3";
                if (mRefreshView != null) {
                    mRefreshView.initData();
                }
//                }
                break;
            case R.id.shop_menu_01://我的钱包
//                ToastUtil.show("开发中...");
                intent = new Intent(mContext, MyWalletMainActivity.class);
                intent.putExtra("state", "1");
                mContext.startActivity(intent);
                break;
            case R.id.shop_menu_02://我的团队
//                ToastUtil.show("开发中...");
                mContext.startActivity(new Intent(mContext, MyTeamListActivity.class));
                break;
            case R.id.shop_menu_03://每日签到
//                ToastUtil.show("开发中...");
                intent = new Intent(mContext, MyWalletMainActivity.class);
                intent.putExtra("state", "0");
                mContext.startActivity(intent);
                break;
            case R.id.shop_menu_04://邀请好友
//                ToastUtil.show("开发中...");
                String url = "http://zhiboshow.yanshi.hbbeisheng.com/index.php?g=Appapi&m=Agent&a=index";
                WebViewActivity.forward(mContext, url);
                break;
            case R.id.forward_cart://进入购物车
                intent = new Intent(mContext, CartMainActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.shop_search_img:
                intent = new Intent(mContext, ShopMallGoodsSearchActivity.class);
                mContext.startActivity(intent);
                break;
        }
    }


    private ArrayList<TreeVO> initPopDowns() {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        TreeVO allTreeVo = new TreeVO();
        allTreeVo.setId(0);
        allTreeVo.setParentId(0);
        allTreeVo.setName("全部");
        allTreeVo.setLevel(1);
        allTreeVo.setHaschild(false);
        allTreeVo.setDepartmentid("-1");
        allTreeVo.setDname("全部");
        list.add(allTreeVo);
        if (Utils.isNotEmpty(mTypelist)) {
            for (int i = 0; i < mTypelist.size(); i++) {
                TreeVO parentVo = new TreeVO();
                parentVo.setId(Integer.parseInt(mTypelist.get(i).getId()));
                parentVo.setParentId(0);
                parentVo.setName(mTypelist.get(i).getCate_name());
                parentVo.setParentSerachId(i + "");
                parentVo.setLevel(1);
                list.add(parentVo);
                parentVo.setHaschild(false);

            }
        }

        return list;
    }


    BSPopupWindowsTitle.TreeCallBack callback = new BSPopupWindowsTitle.TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (vo.getLevel() == 1) {
                // 审批一级菜单
                if (Utils.isNotEmpty(vo) && Utils.isNotEmpty(vo.getId())) {
                    mCateId = vo.getId() + "";
                    if (vo.getId() == 0) {//全部
                        mTypeTv01.setText("全部");
                        mTypeTv01.setTextColor(mContext.getResources().getColor(R.color.textColor));
                        mTypeImg01.setImageResource(R.mipmap.b_icon);
                    } else {
                        mTypeTv01.setText(vo.getName());
                        mTypeTv01.setTextColor(mContext.getResources().getColor(R.color.redlive));
                        mTypeImg01.setImageResource(R.mipmap.b_icon_choise);
                    }
                    if (mRefreshView != null) {
                        mRefreshView.initData();
                    }
                }
            }
        }
    };

}
