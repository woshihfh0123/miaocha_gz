//package com.zhiboshow.phonelive.views.shopmall;
//
//import android.content.Context;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.bspopupwindow.model.TreeVO;
//import com.bspopupwindow.utils.Utils;
//import com.bspopupwindow.view.BSPopupWindowsTitle;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.zhiboshow.phonelive.AppContext;
//import com.zhiboshow.phonelive.Constants;
//import com.zhiboshow.phonelive.R;
//import com.zhiboshow.phonelive.adapter.MainListAdapter;
//import com.zhiboshow.phonelive.adapter.MainShopMallListAdapter;
//import com.zhiboshow.phonelive.adapter.RefreshAdapter;
//import com.zhiboshow.phonelive.bean.ListBean;
//import com.zhiboshow.phonelive.bean.MyShopBean;
//import com.zhiboshow.phonelive.bean.MyShopGoodsList;
//import com.zhiboshow.phonelive.bean.ShopMallGoodsList;
//import com.zhiboshow.phonelive.bean.ShopMallPicBean;
//import com.zhiboshow.phonelive.custom.ItemDecoration;
//import com.zhiboshow.phonelive.custom.RefreshView;
//import com.zhiboshow.phonelive.http.HttpCallback;
//import com.zhiboshow.phonelive.http.HttpConsts;
//import com.zhiboshow.phonelive.http.HttpUtil;
//import com.zhiboshow.phonelive.interfaces.LifeCycleAdapter;
//import com.zhiboshow.phonelive.utils.DataSafeUtils;
//import com.zhiboshow.phonelive.utils.L;
//import com.zhiboshow.phonelive.utils.ToastUtil;
//import com.zhiboshow.phonelive.utils.VideoStorge;
//import com.zhiboshow.phonelive.views.AbsMainListViewHolder;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Created by cxf on 2018/9/27.
// * 商城推荐或者折扣列表
// */
//
//public class MainShopMainListViewHolder extends AbsMainShopMallListViewHolder implements View.OnClickListener {
//
//    private LinearLayout mTypeLayout01, mTypeLayout02, mTypeLayout03;
//    private TextView mTypeTv01, mTypeTv02, mTypeTv03;
//    private ImageView mTypeImg01, mTypeImg02, mTypeImg03;
//    private View mDivider;
//    private String mOrder = "";//1.类型排序 2.销量排序 3.价格排序
//    //    private String mType02 = "";//销量
////    private String mType03 = "";//价格
//    BSPopupWindowsTitle mBsPopupWindowsTitle;
//    private int page = 1;
//
//    public MainShopMainListViewHolder(Context context, ViewGroup parentView) {
//        super(context, parentView);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.shop_main_list_page;
//    }
//
//    @Override
//    public void init() {
//        //super.init();
//        mDivider = (View) findViewById(R.id.lines);
//        mTypeLayout01 = (LinearLayout) findViewById(R.id.type_layout01);
//        mTypeLayout02 = (LinearLayout) findViewById(R.id.type_layout02);
//        mTypeLayout03 = (LinearLayout) findViewById(R.id.type_layout03);
//        mTypeTv01 = (TextView) findViewById(R.id.type_tv01);
//        mTypeTv02 = (TextView) findViewById(R.id.type_tv02);
//        mTypeTv03 = (TextView) findViewById(R.id.type_tv03);
//        mTypeImg01 = (ImageView) findViewById(R.id.type_img01);
//        mTypeImg02 = (ImageView) findViewById(R.id.type_img02);
//        mTypeImg03 = (ImageView) findViewById(R.id.type_img03);
//        mTypeLayout01.setOnClickListener(this);
//        mTypeLayout02.setOnClickListener(this);
//        mTypeLayout03.setOnClickListener(this);
//
//        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
//        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_list);
//        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
//        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 5, 5);
//        decoration.setOnlySetItemOffsetsButNoDraw(true);
//        mRefreshView.setItemDecoration(decoration);
//        mRefreshView.setRefreshEnable(false);
//        mRefreshView.setDataHelper(new RefreshView.DataHelper<ShopMallGoodsList>() {
//            @Override
//            public RefreshAdapter<ShopMallGoodsList> getAdapter() {
//                if (mAdapter == null) {
//                    mAdapter = new MainShopMallListAdapter(mContext, MainShopMallListAdapter.TYPE_PROFIT);
////                    mAdapter.setOnItemClickListener(MainShopMainListViewHolder.this);
//                }
//                return mAdapter;
//            }
//
//            @Override
//            public void loadData(int p, HttpCallback callback) {
//                Log.v("tags", mType + "----------------------type");
//                HttpUtil.GetShopMall(page + "", mType, mOrder,"", callback);
//            }
//
//            @Override
//            public List<ShopMallGoodsList> processData(String[] info) {
//                try {
//                    JSONObject resJson = new JSONObject(info[0]);
//                    List<ShopMallGoodsList> goodsList = JSON.parseArray(resJson.getString("list"), ShopMallGoodsList.class);
//
//                    if (!DataSafeUtils.isEmpty(goodsList) && goodsList.size() > 0) {
//                        return goodsList;
//                    }
//                } catch (JSONException e) {
//
//                }
//                return null;
//            }
//
//            @Override
//            public void onRefresh(List<ShopMallGoodsList> list) {
//                Log.v("tags", "刷新--------");
//            }
//
//            @Override
//            public void onNoData(boolean noData) {
//
//            }
//
//            @Override
//            public void onLoadDataCompleted(int dataCount) {
//                if (dataCount < 10) {
//                    mRefreshView.setLoadMoreEnable(false);
//                } else {
//                    mRefreshView.setLoadMoreEnable(true);
//                }
//            }
//        });
//        mLifeCycleListener = new LifeCycleAdapter() {
//
//            @Override
//            public void onDestroy() {
//                L.e("main----MainListProfitViewHolder-------LifeCycle---->onDestroy");
//                HttpUtil.cancel(HttpConsts.GET_GOODS_LIST);
//            }
//        };
//
//    }
//
//
//    /**
//     * 通过接口获取-轮播图
//     */
//    @Override
//    public void loadData() {
//        Log.v("tags", mType + "-----------2-----------type");
//        if (!isFirstLoadData()) {
//            return;
//        }
//        if (mRefreshView != null) {
//            mRefreshView.initData();
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.type_layout01:
//                mTypeTv01.setTextColor(mContext.getResources().getColor(R.color.redlive));
//                mTypeTv02.setTextColor(mContext.getResources().getColor(R.color.textColor));
//                mTypeTv03.setTextColor(mContext.getResources().getColor(R.color.textColor));
//                if (!mOrder.equals("1")) {
//                    mOrder = "1";
//                    if (mRefreshView != null) {
//                        mRefreshView.initData();
//                    }
//                }
//                //一级菜单
////                mBsPopupWindowsTitle = new BSPopupWindowsTitle(AppContext.sInstance, initPopDowns(), callback, 600);
////                mBsPopupWindowsTitle.showAsDropDown(mDivider);
//                break;
//            case R.id.type_layout02:
//                mTypeTv01.setTextColor(mContext.getResources().getColor(R.color.textColor));
//                mTypeTv02.setTextColor(mContext.getResources().getColor(R.color.redlive));
//                mTypeTv03.setTextColor(mContext.getResources().getColor(R.color.textColor));
//                if (!mOrder.equals("2")) {
//                    mOrder = "2";
//                    if (mRefreshView != null) {
//                        mRefreshView.initData();
//                    }
//                }
//                break;
//            case R.id.type_layout03:
//                mTypeTv01.setTextColor(mContext.getResources().getColor(R.color.textColor));
//                mTypeTv02.setTextColor(mContext.getResources().getColor(R.color.textColor));
//                mTypeTv03.setTextColor(mContext.getResources().getColor(R.color.redlive));
//                if (!mOrder.equals("3")) {
//                    mOrder = "3";
//                    if (mRefreshView != null) {
//                        mRefreshView.initData();
//                    }
//                }
//                break;
//        }
//    }
//
//
//    private ArrayList<TreeVO> initPopDowns() {
//        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
//        TreeVO allTreeVo = new TreeVO();
//        allTreeVo.setId(0);
//        allTreeVo.setParentId(0);
//        allTreeVo.setName("全部");
//        allTreeVo.setLevel(1);
//        allTreeVo.setHaschild(false);
//        allTreeVo.setDepartmentid("-1");
//        allTreeVo.setDname("全部");
//        list.add(allTreeVo);
////        if (Utils.isNotEmpty(mServiceList)) {
////            for (int i = 0; i < mServiceList.size(); i++) {
////                TreeVO parentVo = new TreeVO();
////                parentVo.setId(Integer.parseInt(mServiceList.get(i).getId()));
////                parentVo.setParentId(0);
////                parentVo.setName(mServiceList.get(i).getTitle());
////                parentVo.setParentSerachId(i + "");
////                parentVo.setLevel(1);
////                list.add(parentVo);
////                parentVo.setHaschild(false);
////
////            }
////        }
//
//        return list;
//    }
//
//
////    BSPopupWindowsTitle.TreeCallBack callback = new BSPopupWindowsTitle.TreeCallBack() {
////
////        @Override
////        public void callback(TreeVO vo) {
////            if (vo.getLevel() == 1) {
////                // 审批一级菜单
////                if (Utils.isNotEmpty(vo) && Utils.isNotEmpty(vo.getId())) {
////                    mType01 = vo.getId() + "";
////                    if (vo.getId() == 0) {//全部
////                        mTypeTv01.setText("全部");
////                    } else {
////                        mTypeTv01.setText(vo.getName());
////                    }
////                    page = 1;
////
////                }
////            }
////        }
////    };
//
//    @Override
//    public void onItemClick(ShopMallGoodsList bean, int position) {
//        ToastUtil.show("点击了跳转---" + bean.getTitle());
//    }
//}
