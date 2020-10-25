package com.mc.phonelive.fragment;

import android.app.Dialog;
import android.content.Intent;
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
import com.mc.phonelive.activity.shop.PayActivity;
import com.mc.phonelive.adapter.shop.MyOrderAdapter;
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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by WWL on 2019/4/25 0025:17
 * 我的订单列表
 */
public class OrderFragment extends BaseFragment implements OnRefreshLoadMoreListener {

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
    private MyOrderAdapter mAdapter;
    private String state = "";//0全部 1待付款 2待发货 3待评价  4已关闭
    private String mType = "";//新零售(2:购买区)
    private int mCurrentPage = 1;
    private boolean ISTART = false;
    OrderListVo.InfoBean mListbean = new OrderListVo.InfoBean();
    List<OrderListVo.InfoBean> mOrderList = new ArrayList<>();

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
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
        if (!ISTART) {
            getOrderData();
        }
    }


    private void getAdapter(List<OrderListVo.InfoBean> mPayVO) {
        String mStatus = this.state;
        mAdapter = new MyOrderAdapter(getActivity(), R.layout.myorder_adapter_type, mPayVO, mStatus);
        shoppingOrderRecyclerView.setAdapter(mAdapter);
        shoppingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getOrderData() {
        HttpUtil.OrderGetList(mCurrentPage + "", this.getState(),"", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ISTART = true;
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
                if (info.length > 0) {
                    Log.v("tags", "info=" + info[0]);
                    if (shoppingOrderRecyclerView != null)
                        shoppingOrderRecyclerView.setVisibility(View.VISIBLE);
                    if (NocontentTv != null)
                        NocontentTv.setVisibility(View.GONE);

                    List< OrderListVo.InfoBean> list = JSON.parseArray(Arrays.toString(info),  OrderListVo.InfoBean.class);
                    if (mAdapter != null) {
                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(list);
                        } else {
                            mAdapter.addData(list);
                        }
                    }
                    if (info.length < 15) {
                        refreshLayout.setEnableLoadMore(false);
                    } else {
                        refreshLayout.setEnableLoadMore(true);
                    }
                } else {
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
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventBusModel eventBusModel) {
        String code = eventBusModel.getCode();
        switch (code) {
            case "refreshOrder":
                mCurrentPage = 1;
                getOrderData();
                break;
            case "setDelData"://删除订单
                String mStatus = (String) eventBusModel.getSecondObject();
                mListbean = (OrderListVo.InfoBean) eventBusModel.getObject();
                if (this.state.equals(mStatus)) {
                    delOrder(mListbean, "2");
                }
                break;
            case "setCancleData"://取消订单
                mStatus = (String) eventBusModel.getSecondObject();
                mListbean = (OrderListVo.InfoBean) eventBusModel.getObject();
                if (this.state.equals(mStatus)) {
                    delOrder(mListbean, "1");
                }
                break;
            case "PayNow"://立即支付
                mStatus = (String) eventBusModel.getSecondObject();
                mListbean = (OrderListVo.InfoBean) eventBusModel.getObject();
                if (this.state.equals(mStatus)) {
                    PayNow(mListbean);
                }
                break;
            case "orderdetail_refresh"://刷新数据,详情界面数据或许已经改变了，那么在关闭详情的时候，订单列表自动刷新
                mCurrentPage = 1;
                getOrderData();
                break;
            case "ConfirmProduct"://确认收货
                mListbean = (OrderListVo.InfoBean) eventBusModel.getObject();
                mStatus = (String) eventBusModel.getSecondObject();
                if (this.state.equals(mStatus)) {
                    ConfirmProduct(mListbean);
                }
                break;

        }
    }

    /**
     * 确认收货
     *
     * @param listbean
     */
    private void ConfirmProduct(final OrderListVo.InfoBean listbean) {
        CancelORderDialog oRderDialog = new CancelORderDialog(getActivity(), "确定收货", "确定已收到货吗？") {
            @Override
            public void doConfirmUp() {
                getConfirmProduct(listbean.getId());

            }
        };
        oRderDialog.show();
    }


    /**
     * 订单删除
     *
     * @param mListbean
     */
    private void delOrder(final OrderListVo.InfoBean mListbean, String type) {
        String title = "";
        String msg = "";
        if (type.equals("1")) {
            title = "取消订单";
            msg = "确定取消订单？";
        } else {
            title = "删除订单";
            msg = "确定删除订单？";
        }
        CancelORderDialog oRderDialog = new CancelORderDialog(getActivity(), title, msg) {
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
                    mCurrentPage=1;
                    getOrderData();
                }
                ToastUtil.show(msg);
            }
        });

    }

    /**
     * 删除订单/取消订单
     * type  1取消订单   2删除订单
     *
     * @param mOrderId
     */
    private void getDelOrder(final String mOrderId, String type) {
        HttpUtil.OrderDelOrder(mOrderId, type, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (code == 0) {
                    mCurrentPage=1;
                    getOrderData();
                }
            }
        });
    }


    /**
     * 立即支付
     *
     * @param
     */
    private void PayNow(OrderListVo.InfoBean listBean) {
        Intent intent = new Intent(getActivity(), PayActivity.class);
        intent.putExtra("out_trade_no", listBean.getPay_sn());
        intent.putExtra("order_id", listBean.getId());
        intent.putExtra("money", listBean.getPay_price());
        startActivity(intent);
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
