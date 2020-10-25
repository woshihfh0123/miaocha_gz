package com.mc.phonelive.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.foxtone.CollectionBindingActivity;
import com.mc.phonelive.adapter.foxtone.TradingBuyListAdapter;
import com.mc.phonelive.bean.foxtone.TradingCenterBean;
import com.mc.phonelive.bean.foxtone.TradingCenterListBean;
import com.mc.phonelive.dialog.SellYinDouDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 求购中心
 * created by WWL on 2020/4/14 0014:14
 */
public class TradingFindBuyListFragment extends BaseFragment implements TradingBuyListAdapter.InfoClubListener {
    View layoutView;

    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.search_img)
    ImageView searchImg;
    @BindView(R.id.search_close_img)
    ImageView searchCloseImg;
    @BindView(R.id.search_submit)
    TextView searchSubmit;
    @BindView(R.id.trading_recyclerview)
    RecyclerView tradingRecyclerview;
    @BindView(R.id.no_data)
    TextView noData;
    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;

    private InputMethodManager imm;
    private int pages = 1;
    TradingBuyListAdapter mBuyListAdapter;//求购列表
    List<TradingCenterListBean> mList = new ArrayList<>();
    private String mRate = "";
    private String keywords = "";
    private boolean ISTART =true;

    public static TradingFindBuyListFragment newInstance() {
        TradingFindBuyListFragment fragment = new TradingFindBuyListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.trading_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, layoutView);
        return layoutView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (!ISTART) {
                pages=1;
                initHttpData();
            }
        }
    }

    @Override
    protected void initView(View view) {
        boolean isRegist = EventBus.getDefault().isRegistered(this);
        if (!isRegist) {
            EventBus.getDefault().register(this);
        }
        getSignAdapter(mList);

        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pages++;
                if (refreshLayout != null) {
                    refreshLayout.finishLoadMore(1000);
                }
                initHttpData();
            }
        });

        if (!DataSafeUtils.isEmpty(searchCloseImg)) {
            searchCloseImg.setVisibility(View.GONE);
        }
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (DataSafeUtils.isEmpty(s)) {
                    searchCloseImg.setVisibility(View.GONE);
                    keywords = "";
                    pages = 1;
                    initHttpData();
                } else {
                    searchCloseImg.setVisibility(View.VISIBLE);
                    keywords = s.toString();
                    pages = 1;
                    initHttpData();
                }

            }
        });
    }

    /**
     * 求购列表
     *
     * @param signList
     */
    private void getSignAdapter(List<TradingCenterListBean> signList) {
        mBuyListAdapter = new TradingBuyListAdapter(R.layout.trading_list_item_view, signList);
        mBuyListAdapter.AddClubListener(this);
        tradingRecyclerview.setAdapter(mBuyListAdapter);
        tradingRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    /**
     * 卖出
     *
     * @param id
     */
    private void showSellDialog(String id, String count) {
        SellYinDouDialog oRderDialog = new SellYinDouDialog(getActivity(), mRate, count) {
            @Override
            public void doConfirmUp(String count, String consume, String mobile, String password) {

                if (!ButtonUtils.isFastDoubleClick02()) {
                    HttpUtil.buyTrade(id, count, mobile, password, new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            ToastUtil.show(msg);
                            if (code == 0) {
                                dismiss();
                                initHttpData();
                            } else if (code == 1006) {
                                getActivity().startActivity(new Intent(getActivity(), CollectionBindingActivity.class));
                            }

                        }
                    });
                }
            }
        };
        oRderDialog.setCancelable(false);
        oRderDialog.show();
    }


    @Override
    protected void initHttpData() {
        HttpUtil.getTradeList(pages, keywords, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ISTART =false;
                List<TradingCenterBean.InfoBean> data = JSON.parseArray(Arrays.toString(info), TradingCenterBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(data) && data.size() > 0) {
                    if (!DataSafeUtils.isEmpty(data.get(0).getData())) {
                        if (!DataSafeUtils.isEmpty(data.get(0).getData().getRate()))
                            mRate = data.get(0).getData().getRate();
                    }

                    if (!DataSafeUtils.isEmpty(data.get(0).getList()) && data.get(0).getList().size() > 0) {
                        if (tradingRecyclerview != null)
                            tradingRecyclerview.setVisibility(View.VISIBLE);
                        if (noData != null) noData.setVisibility(View.GONE);
                        mList = data.get(0).getList();
                        if (pages == 1) {
                            if (mBuyListAdapter != null) {
                                mBuyListAdapter.setNewData(data.get(0).getList());
                            }
                        } else {
                            if (mBuyListAdapter != null) {
                                mBuyListAdapter.addData(data.get(0).getList());
                            }
                        }
                        if (data.get(0).getList().size() < 15) {
                            if (refreshLayout != null) {
                                refreshLayout.setEnableLoadMore(false);
                                footer.setNoMoreData(true);
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        } else {
                            if (refreshLayout != null) {
                                refreshLayout.setEnableLoadMore(true);
                            }
                        }
                    } else {
                        if (pages == 1) {
                            if (tradingRecyclerview != null)
                                tradingRecyclerview.setVisibility(View.GONE);
                            if (noData != null) noData.setVisibility(View.VISIBLE);
                        }
                        if (refreshLayout != null) {
                            refreshLayout.setEnableLoadMore(false);
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }
                } else {
                    if (pages == 1) {
                        if (tradingRecyclerview != null)
                            tradingRecyclerview.setVisibility(View.GONE);
                        if (noData != null) noData.setVisibility(View.VISIBLE);
                    }
                    if (refreshLayout != null) {
                        refreshLayout.setEnableLoadMore(false);
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }
            }

            @Override
            public void onFinish() {
                if (refreshLayout != null) {
                    refreshLayout.finishLoadMore();
                }

                View v = getActivity().getWindow().peekDecorView();
                if (null != v) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ViewEvent(EventBusModel event) {
        String code = event.getCode();
        switch (code) {
            case "refresh_buy_list":
                pages = 1;
                initHttpData();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        boolean isRegist = EventBus.getDefault().isRegistered(this);
        if (isRegist) {
            EventBus.getDefault().unregister(this);
        }

        if (imm != null && searchEdit != null) {
            imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
        }
    }

    @Override
    public void addClubData(String id, String count) {
        showSellDialog(id, count);
    }

    @OnClick({R.id.search_img, R.id.search_close_img, R.id.search_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.search_close_img:
                searchEdit.setText("");
                searchCloseImg.setVisibility(View.GONE);
                break;
            case R.id.search_img:
            case R.id.search_submit:
                pages = 1;
                initHttpData();
                if (imm != null && searchEdit != null) {
                    imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
                }
                break;
        }
    }
}
