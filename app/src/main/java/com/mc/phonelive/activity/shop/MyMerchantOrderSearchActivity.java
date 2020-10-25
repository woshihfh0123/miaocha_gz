package com.mc.phonelive.activity.shop;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.shop.MyShoppingOrderSearchAdapter;
import com.mc.phonelive.bean.OrderListVo;
import com.mc.phonelive.dialog.CancelORderDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的小店里面的 订单列表搜索
 * created by WWL on 2020/4/13 0013:14
 */
public class MyMerchantOrderSearchActivity extends AbsActivity {
    @BindView(R.id.shopping_order_recycler_view)
    RecyclerView shoppingOrderRecyclerView;
    @BindView(R.id.no_content)
    RelativeLayout NocontentTv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty_textview)
    TextView emptyTextview;
    private EditText mEditText;
    private MyShoppingOrderSearchAdapter mAdapter;
    private InputMethodManager imm;
    private String mKey;
    private MyMerchantOrderSearchActivity.MyHandler mHandler;
    private int mCurrentPage = 1;

    OrderListVo.InfoBean mListbean = new OrderListVo.InfoBean();
    List<OrderListVo.InfoBean> mOrderList = new ArrayList<>();

    public static void forward(Context context) {
        context.startActivity(new Intent(context, MyMerchantOrderSearchActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myorder_search;
    }

    @Override
    protected void main() {
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }

        emptyTextview.setText("没有搜索到相关内容");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditText = (EditText) findViewById(R.id.edit);

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mCurrentPage=1;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCurrentPage=1;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HttpUtil.cancel(HttpConsts.ORDERSTORELIST);
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
                if (!TextUtils.isEmpty(s)) {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(0, 500);
                    }
                } else {
                    mKey = null;
                    if (mAdapter != null) {
                        shoppingOrderRecyclerView.setVisibility(View.GONE);
                        NocontentTv.setVisibility(View.VISIBLE);
                    }
                    if (refreshLayout != null) {
                        refreshLayout.setEnableRefresh(false);
                        refreshLayout.setEnableLoadMore(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mHandler = new MyMerchantOrderSearchActivity.MyHandler(this);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage++;
                getOrderData();
            }
        });

        getAdapter(mOrderList);
    }


    private void search() {
        mCurrentPage=1;
        String key = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            ToastUtil.show(R.string.content_empty);
            return;
        }
        HttpUtil.cancel(HttpConsts.ORDERSTORELIST);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mKey = key;
        mOrderList =new ArrayList<>();
        getOrderData();
    }


    private void getAdapter(List<OrderListVo.InfoBean> goodslist) {
        mAdapter = new MyShoppingOrderSearchAdapter(this, R.layout.myorder_adapter_type, goodslist, "0");
        shoppingOrderRecyclerView.setAdapter(mAdapter);
        shoppingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    private void getOrderData() {
        if (mAdapter != null) {
            Log.v("tags", mOrderList.size() + "--" + mAdapter.getData().size());
        }
        HttpUtil.OrderStoreList(mCurrentPage + "", "0",mKey, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (refreshLayout != null) {
                    refreshLayout.finishLoadMore();
                }
                if (info.length > 0) {
                    if (shoppingOrderRecyclerView != null)
                        shoppingOrderRecyclerView.setVisibility(View.VISIBLE);
                    if (NocontentTv != null)
                        NocontentTv.setVisibility(View.GONE);

                    mOrderList=new ArrayList<>();
                    List< OrderListVo.InfoBean> list = JSON.parseArray(Arrays.toString(info),  OrderListVo.InfoBean.class);
                    mOrderList.addAll(list);
                    if (mAdapter != null) {

                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(list);
                        } else {
                            mAdapter.addData(list);
                        }
                        Log.v("tags","--mAdapter.size="+mAdapter.getData().size());
                    }
                    if (info.length < 15) {
                        refreshLayout.setEnableLoadMore(false);
                    } else {
                        refreshLayout.setEnableLoadMore(true);
                    }
                } else {
                    if (mCurrentPage == 1) {
                        if (shoppingOrderRecyclerView != null)
                            shoppingOrderRecyclerView.setVisibility(View.GONE);
                        if (NocontentTv != null)
                            NocontentTv.setVisibility(View.VISIBLE);
                    } else {
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
                return DialogUitl.loadingDialog(MyMerchantOrderSearchActivity.this);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventBusModel eventBusModel) {
        String code = eventBusModel.getCode();
        switch (code) {
            case "setDelData_search01"://删除订单
                String mStatus = (String) eventBusModel.getSecondObject();
                mListbean = (OrderListVo.InfoBean) eventBusModel.getObject();
                delOrder(mListbean, "2");
                break;
            case "orderdetail_refresh_search"://刷新数据,详情界面数据或许已经改变了，那么在关闭详情的时候，订单列表自动刷新
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
        String title = "";
        String msg = "";
        if (type.equals("1")) {
            title = "取消订单";
            msg = "确定取消订单？";
        } else {
            title = "删除订单";
            msg = "确定删除订单？";
        }
        CancelORderDialog oRderDialog = new CancelORderDialog(mContext, title, msg) {
            @Override
            public void doConfirmUp() {
                getDelOrder(mListbean.getId(), type);

            }
        };
        oRderDialog.show();
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




    @Override
    protected void onDestroy() {
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered) {
            EventBus.getDefault().unregister(this);
        }
        HttpUtil.cancel(HttpConsts.ORDERSTORELIST);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.release();
        }
        mHandler = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private static class MyHandler extends Handler {

        private MyMerchantOrderSearchActivity mActivity;

        public MyHandler(MyMerchantOrderSearchActivity activity) {
            mActivity = new WeakReference<>(activity).get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null) {
                mActivity.search();
            }
        }

        public void release() {
            mActivity = null;
        }
    }

}