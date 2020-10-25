package com.mc.phonelive.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.shop.MyShoppingOrderAdapter;
import com.mc.phonelive.bean.OrderListVo;
import com.mc.phonelive.dialog.CancelORderDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by WWL on 2019/4/25 0025:17
 * 我的订单列表
 */
public class MyOrderFragment extends BaseFragment implements OnRefreshLoadMoreListener {

    @BindView(R.id.shopping_order_recycler_view)
    RecyclerView shoppingOrderRecyclerView;
    @BindView(R.id.no_content)
    RelativeLayout NocontentTv;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty_textview)
    TextView emptyTextview;
    @BindView(R.id.empty_img)
    ImageView emptyImg;
    View layoutView;
    private MyShoppingOrderAdapter mAdapter;
    private String state = "";//12全部 0待付款 1待发货 5待评价  6已关闭
   //button类型  1删除 2取消 3去付款 4 查看物流 5确认收货 6评价
    private int mCurrentPage = 1;
    private boolean ISTART = false;
    OrderListVo.InfoBean mListbean = new OrderListVo.InfoBean();
    List<OrderListVo.InfoBean> mOrderList = new ArrayList<>();

    public static MyOrderFragment newInstance() {
        MyOrderFragment fragment = new MyOrderFragment();
        return fragment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.order_fragment, container, false);
        return layoutView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView(view);

        getAdapter(mOrderList);
    }


    protected void initView(View view) {
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }

        refreshLayout.setOnRefreshLoadMoreListener(this);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (ISTART) {
                mCurrentPage = 1;
                getOrderData();
            }
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage = 1;
        getOrderData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage++;
        getOrderData();
    }

    @Override
    protected void initHttpData() {
        Log.v("tags","-----------------------------------222---------------5");
        if (!ISTART) {
            getOrderData();
        }
//
    }


    private void getAdapter(List<OrderListVo.InfoBean> mPayVO) {
        String mStatus = this.state;
        mAdapter = new MyShoppingOrderAdapter(getActivity(), R.layout.myorder_adapter_type, mPayVO, mStatus);
        shoppingOrderRecyclerView.setAdapter(mAdapter);
        shoppingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * 我的小店 我的订单列表
     */
    private void getOrderData() {
        HttpUtil.OrderStoreList(mCurrentPage+"", this.getState(),"", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ISTART=true;
                if (info.length > 0) {
                    Log.v("tags", "info=" + info[0]);
                    if (shoppingOrderRecyclerView != null)
                        shoppingOrderRecyclerView.setVisibility(View.VISIBLE);
                    if (NocontentTv != null)
                        NocontentTv.setVisibility(View.GONE);

                    mOrderList.clear();
                    for (int i = 0; i < info.length; i++) {
                        OrderListVo.InfoBean infoBean = JSON.parseObject(info[i], OrderListVo.InfoBean.class);
                        mOrderList.add(infoBean);
                    }
                    if (mAdapter!=null) {
                        if (mCurrentPage==1) {
                            mAdapter.setNewData(mOrderList);
                        }else{
                            mAdapter.addData(mOrderList);
                        }
                    }
                    if (info.length<15){
                        refreshLayout.setEnableLoadMore(false);
                    }else{
                        refreshLayout.setEnableLoadMore(true);
                    }
                }else {
                    if (mCurrentPage==1) {
                        if (shoppingOrderRecyclerView != null)
                            shoppingOrderRecyclerView.setVisibility(View.GONE);
                        if (NocontentTv != null)
                            NocontentTv.setVisibility(View.VISIBLE);
                    }else{
                        refreshLayout.setEnableLoadMore(false);
                    }
                }
            }
            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(getActivity());
            }
            @Override
            public void onFinish() {
                super.onFinish();
                ISTART=true;
                if (refreshLayout!=null){
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventBusModel eventBusModel) {
        String code = eventBusModel.getCode();
        switch (code) {
            case "setDelData"://删除订单
                String mStatus = (String) eventBusModel.getSecondObject();
                mListbean = (OrderListVo.InfoBean) eventBusModel.getObject();
                if (this.state.equals(mStatus)) {
                    delOrder(mListbean, "2");
                }
                break;
            case "orderdetail_refresh"://刷新数据,详情界面数据或许已经改变了，那么在关闭详情的时候，订单列表自动刷新
                mCurrentPage = 1;
                getOrderData();
                break;
        }
    }




    /**
     * 订单删除
     *
     * @param mListbean
     */
    private void delOrder(final OrderListVo.InfoBean mListbean, String type) {
        CancelORderDialog oRderDialog = new CancelORderDialog(getActivity(), "删除订单", "确定删除订单？") {
            @Override
            public void doConfirmUp() {
                getDelOrder(mListbean.getId(), type);

            }
        };
        oRderDialog.show();
    }


    /**
     * 确认收货
     *
     * @param mOrderId
     */
    private void getConfirmProduct(final String mOrderId) {
        HttpUtil.OrderTakeOrder(mOrderId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    getOrderData();
                }
                ToastUtil.show(msg);
            }
        });

    }

    /**
     * 删除订单
     * type  1取消订单
     *
     * @param mOrderId
     */
    private void getDelOrder(final String mOrderId, String type) {
        HttpUtil.OrderDelOrder(mOrderId, type, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (code == 0) {
                    getOrderData();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered) {
            EventBus.getDefault().unregister(this);
        }
    }

}
