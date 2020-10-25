package com.mc.phonelive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.shop.OrderLogisticsActivity;
import com.mc.phonelive.adapter.OrderScoreAdapter;
import com.mc.phonelive.bean.OrderListBean;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.OrderListVo;
import com.mc.phonelive.dialog.CancelORderDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
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
 * 积分订单88888888
 */
public class ScoreOrderFragment extends BaseFragment {

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
    private OrderScoreAdapter mAdapter;
    private String state = "";//12全部 0待付款 1待发货 5待评价  6已关闭
    //button类型  1删除 2取消 3去付款 4 查看物流 5确认收货 6评价
    private int mCurrentPage = 1;
    private boolean ISTART = false;
    OrderListVo.InfoBean mListbean = new OrderListVo.InfoBean();
    List<OrderListVo.InfoBean> mOrderList = new ArrayList<>();

    private static String type = "1";//默认全部  ,1待收货,2已收货

    public static ScoreOrderFragment newInstance(String mtype) {
        ScoreOrderFragment fragment = new ScoreOrderFragment();
        type = mtype;
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
        layoutView = inflater.inflate(R.layout.score_od_fragment, container, false);
        return layoutView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        getAdapter();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                refreshLayout.finishRefresh(1000);
                getOrderData();

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage++;
                refreshLayout.finishLoadMore(1000);
                getOrderData();
            }
        });
    }


    protected void initView(View view) {
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    protected void initHttpData() {
        Log.v("tags", "-----------------------------------222---------------5");
        getOrderData();
//
    }


    private void getAdapter() {
        mAdapter = new OrderScoreAdapter();
        shoppingOrderRecyclerView.setLayoutManager(Utils.getLvManager(getContext()));
        shoppingOrderRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                if(view.getId()==R.id.tv_wl){
//                    Intent intent = new Intent(getContext(), OrderLogisticsActivity.class);
//                    intent.putExtra("order_id",mAdapter.getItem(position).getId());
//                    getContext().startActivity(intent);
//                } else  if(view.getId()==R.id.tv_status){
//                    EventBusUtil.postEvent(new EventBean("MalltakeOrder",mAdapter.getItem(position).getId()));
//                }
//            }
//        });

    }

    /**
     * 我的小店 我的订单列表
     */
    private void getOrderData() {
        HttpUtil.getListOrder(getState(), mCurrentPage + "", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<OrderListBean> nlist = (ArrayList<OrderListBean>) JSON.parseArray(obj.getString("list"), OrderListBean.class);
                    if (Utils.isNotEmpty(nlist)) {
                        if (mCurrentPage == 1) {

                            mAdapter.setNewData(nlist);
                        } else {
                            mAdapter.addData(nlist);
                        }
                        refreshLayout.setEnableLoadMore(true);
                    } else {
                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(null);
                        }
                        refreshLayout.setEnableLoadMore(false);
                    }

                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (Utils.isNotEmpty(mAdapter) && Utils.isNotEmpty(mAdapter.getData())) {
                    NocontentTv.setVisibility(View.GONE);
                } else {
                    NocontentTv.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    /*
     * 我的积分订单  确认收货
     * */
    private void getMallSureOrder(String orderId) {
        HttpUtil.getSureOrderSH(orderId, mCurrentPage + "", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                getOrderData();
            }

        });
    }

    /*
     * 我的积分订单  查看物流
     * */
    private void getOrderWL(String orderId) {
        HttpUtil.getSureOrderWL(orderId, mCurrentPage + "", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<OrderListBean> nlist = (ArrayList<OrderListBean>) JSON.parseArray(obj.getString("list"), OrderListBean.class);
                    if (Utils.isNotEmpty(nlist)) {
                        if (mCurrentPage == 1) {

                            mAdapter.setNewData(nlist);
                        } else {
                            mAdapter.addData(nlist);
                        }
                        refreshLayout.setEnableLoadMore(true);
                    } else {
                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(null);
                        }
                        refreshLayout.setEnableLoadMore(false);
                    }

                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (Utils.isNotEmpty(mAdapter) && Utils.isNotEmpty(mAdapter.getData())) {
                    NocontentTv.setVisibility(View.GONE);
                } else {
                    NocontentTv.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(EventBean event) {
        switch (event.getCode()) {
            case "LookWL":
                Intent intent = new Intent(getActivity(), OrderLogisticsActivity.class);
                String morderid = (String) event.getFirstObject();
                intent.putExtra("order_id", morderid);
                intent.putExtra("type","jf");
                startActivity(intent);
                break;
            case "MalltakeOrder":
                String orderid = (String) event.getFirstObject();
                getMallSureOrder(orderid);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventBusModel eventBusModel) {
        String code = eventBusModel.getCode();
        switch (code) {
//            case "LookWL":
//                Intent intent = new Intent(getContext(), OrderLogisticsActivity.class);
//                String morderid = (String) eventBusModel.getSecondObject();
//                intent.putExtra("order_id",morderid);
//                getContext().startActivity(intent);
//                break;
            case "MalltakeOrder":
                String orderid = (String) eventBusModel.getSecondObject();
                getMallSureOrder(orderid);
                break;
            case "MalllogisticsDetail":
                String eorderid = (String) eventBusModel.getSecondObject();
                getOrderWL(eorderid);
                break;
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
