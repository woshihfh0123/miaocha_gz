package com.mc.phonelive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.foxtone.TradingBuyToReceivedActivity;
import com.mc.phonelive.adapter.foxtone.TradingMailAdapter;
import com.mc.phonelive.adapter.foxtone.TradingMailComplainAdapter;
import com.mc.phonelive.bean.foxtone.ComplainListBean;
import com.mc.phonelive.bean.foxtone.TradingMailBean;
import com.mc.phonelive.dialog.ApplyDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
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
 * 交易信箱
 * created by WWL on 2020/4/14 0014:14
 */
public class TradingMailboxFragment extends BaseFragment implements TradingMailAdapter.MailOnClickLisenter {
    View layoutView;

    @BindView(R.id.trading_recyclerview)
    RecyclerView tradingRecyclerview;
    @BindView(R.id.trading_complain_recyclerview)
    RecyclerView tradingComplainRecyclerview;
    @BindView(R.id.no_data)
    TextView noData;
    Unbinder unbinder;
    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.item_tv01)
    TextView itemTv01;
    @BindView(R.id.item_img01)
    ImageView itemImg01;
    @BindView(R.id.item_layout01)
    LinearLayout itemLayout01;
    @BindView(R.id.item_tv02)
    TextView itemTv02;
    @BindView(R.id.item_img02)
    ImageView itemImg02;
    @BindView(R.id.item_layout02)
    LinearLayout itemLayout02;
    @BindView(R.id.item_tv03)
    TextView itemTv03;
    @BindView(R.id.item_img03)
    ImageView itemImg03;
    @BindView(R.id.item_layout03)
    LinearLayout itemLayout03;
    @BindView(R.id.item_tv04)
    TextView itemTv04;
    @BindView(R.id.item_img04)
    ImageView itemImg04;
    @BindView(R.id.item_layout04)
    LinearLayout itemLayout04;
    private List<TradingMailBean.InfoBean> mList = new ArrayList<>();
    private List<ComplainListBean.InfoBean> mComplainList = new ArrayList<>();
    TradingMailAdapter mSignAdapter;//交易列表
    TradingMailComplainAdapter mComplainAdapter;//交易列表

    private boolean ISTART=true;
    public static String mType = "1";//交易方式(1.买入,2.卖出,3.完成,4.申诉)
    private int page = 1;

    public static TradingMailboxFragment newInstance() {
        TradingMailboxFragment fragment = new TradingMailboxFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.trading_mailbox_fragment, container, false);
        unbinder = ButterKnife.bind(this, layoutView);
        return layoutView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (!ISTART) {
                page=1;
                initHttpData();
            }
        }
    }

    @Override
    protected void initView(View view) {
        refreshLayout.setEnableRefresh(false);

        boolean isRegist = EventBus.getDefault().isRegistered(this);
        if (!isRegist) {
            EventBus.getDefault().register(this);
        }

        setMailAdapter(mList);
        setMailComplainAdapter(mComplainList);
    }

    /**
     * 交易信箱
     *
     * @param signList
     */
    private void setMailAdapter(List<TradingMailBean.InfoBean> signList) {
        mSignAdapter = new TradingMailAdapter(R.layout.trading_mailbox_list_item, signList, mType);
        mSignAdapter.setMailListener(this);
        tradingRecyclerview.setAdapter(mSignAdapter);
        tradingRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    /**
     * 交易信箱——申诉列表
     *
     * @param mComplainList
     */
    private void setMailComplainAdapter(List<ComplainListBean.InfoBean> mComplainList) {
        mComplainAdapter = new TradingMailComplainAdapter(R.layout.trading_mailbox_complain_list_item, mComplainList);
        tradingComplainRecyclerview.setAdapter(mComplainAdapter);
        tradingComplainRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }


    @Override
    protected void initHttpData() {
        HttpUtil.TradegetList(page, mType, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ISTART =false;
                dismissDialog();
                List<TradingMailBean.InfoBean> data = JSON.parseArray(Arrays.toString(info), TradingMailBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(data) && data.size() > 0) {
                    mList = data;
                    if (tradingRecyclerview != null)
                        tradingRecyclerview.setVisibility(View.VISIBLE);
                    if (noData != null) noData.setVisibility(View.GONE);
                    Log.v("tags", info[0]);
                    if (page == 1) {
                        if (mSignAdapter != null) {
                            mSignAdapter.setNewData(data);
                        }
                    } else {
                        if (mSignAdapter != null) {
                            mSignAdapter.addData(data);
                        }
                    }
                    if (data.size() < 15) {
                        if (refreshLayout != null) {
                            refreshLayout.setEnableLoadMore(false);
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            footer.setNoMoreData(true);
                        }
                    } else {
                        if (refreshLayout != null) {
                            refreshLayout.setEnableLoadMore(true);
                        }
                    }
                } else {
                    if (page == 1) {
                        if (tradingRecyclerview != null)
                            tradingRecyclerview.setVisibility(View.GONE);
                        if (noData != null) noData.setVisibility(View.VISIBLE);
                    }
                    if (refreshLayout != null) {
                        refreshLayout.setEnableLoadMore(false);
                    }
                }
            }

            @Override
            public void onFinish() {
                dismissDialog();
                if (refreshLayout!=null){
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    /**
     * 申诉
     *
     * @param orderid
     */
    @Override
    public void ApplyOnClick(String orderid) {

        ApplyDialog oRderDialog = new ApplyDialog(getActivity()) {
            @Override
            public void doConfirmUp(String editTv) {
                if (!ButtonUtils.isFastDoubleClick02()) {
                    HttpUtil.complainTrade(orderid, editTv, new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            ToastUtil.show(msg);
                            if (code == 0) {
                                page = 1;
                                initHttpData();
                            }
                            dismiss();
                        }
                    });
                }
            }
        };
        oRderDialog.show();

    }

    @Override
    public void CodeLookOnClick(String id, TradingMailBean.InfoBean.UserMsgBean bean, String status, String type) {
        Intent intent = new Intent(getActivity(), TradingBuyToReceivedActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("bean", bean);
        intent.putExtra("status", status);
        intent.putExtra("type", type);
        startActivity(intent);
//        ShowCodeDialog oRderDialog = new ShowCodeDialog(getActivity(),codeimg);
//        oRderDialog.show();
    }

    @Override
    public void refreshListView() {
        page = 1;
        initHttpData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(EventBusModel e) {
        if (e.getCode().equals("refresh_trading")) {
            page = 1;
            initHttpData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mType = "1";
        boolean isRegist = EventBus.getDefault().isRegistered(this);
        if (isRegist) {
            EventBus.getDefault().unregister(this);
        }
    }


    @OnClick({R.id.item_layout01, R.id.item_layout02, R.id.item_layout03, R.id.item_layout04})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_layout01:
                if (!mType.equals("1"))
                    checkedView(true, false, false, false);
                break;
            case R.id.item_layout02:
                if (!mType.equals("2"))
                    checkedView(false, true, false, false);
                break;
            case R.id.item_layout03:
                if (!mType.equals("3"))
                    checkedView(false, false, true, false);
                break;
            case R.id.item_layout04:
                if (!mType.equals("4"))
                checkedView(false, false, false, true);
                break;
        }
    }

    private void checkedView(boolean checked01, boolean checked02, boolean checked03, boolean checked04) {
        HttpUtil.cancel(HttpConsts.GETLIST);
        HttpUtil.cancel(HttpConsts.GETCOMPLAINLIST);
        if (mSignAdapter != null) {
            List<TradingMailBean.InfoBean> mList = new ArrayList<>();
            mSignAdapter.setNewData(mList);
        }
        if (checked01) {
            mType = "1";
            itemTv01.setTextColor(getResources().getColor(R.color.white));
            itemImg01.setVisibility(View.VISIBLE);
            itemTv02.setTextColor(getResources().getColor(R.color.gray1));
            itemImg02.setVisibility(View.INVISIBLE);
            itemTv03.setTextColor(getResources().getColor(R.color.gray1));
            itemImg03.setVisibility(View.INVISIBLE);
            itemTv04.setTextColor(getResources().getColor(R.color.gray1));
            itemImg04.setVisibility(View.INVISIBLE);
        }
        if (checked02) {
            mType = "2";
            itemTv02.setTextColor(getResources().getColor(R.color.white));
            itemImg02.setVisibility(View.VISIBLE);
            itemTv01.setTextColor(getResources().getColor(R.color.gray1));
            itemImg01.setVisibility(View.INVISIBLE);
            itemTv03.setTextColor(getResources().getColor(R.color.gray1));
            itemImg03.setVisibility(View.INVISIBLE);
            itemTv04.setTextColor(getResources().getColor(R.color.gray1));
            itemImg04.setVisibility(View.INVISIBLE);
        }
        if (checked03) {
            mType = "3";
            itemTv03.setTextColor(getResources().getColor(R.color.white));
            itemImg03.setVisibility(View.VISIBLE);
            itemTv01.setTextColor(getResources().getColor(R.color.gray1));
            itemImg01.setVisibility(View.INVISIBLE);
            itemTv02.setTextColor(getResources().getColor(R.color.gray1));
            itemImg02.setVisibility(View.INVISIBLE);
            itemTv04.setTextColor(getResources().getColor(R.color.gray1));
            itemImg04.setVisibility(View.INVISIBLE);
        }
        if (checked04) {
            mType = "4";
            itemTv04.setTextColor(getResources().getColor(R.color.white));
            itemImg04.setVisibility(View.VISIBLE);
            itemTv01.setTextColor(getResources().getColor(R.color.gray1));
            itemImg01.setVisibility(View.INVISIBLE);
            itemTv02.setTextColor(getResources().getColor(R.color.gray1));
            itemImg02.setVisibility(View.INVISIBLE);
            itemTv03.setTextColor(getResources().getColor(R.color.gray1));
            itemImg03.setVisibility(View.INVISIBLE);

        }

        page = 1;
        if (mType.equals("4")) {
            if (tradingComplainRecyclerview != null)
                tradingComplainRecyclerview.setVisibility(View.VISIBLE);
            if (tradingRecyclerview != null) tradingRecyclerview.setVisibility(View.GONE);
            getComplainList(page);
            return;
        } else {
            if (tradingComplainRecyclerview != null)
                tradingComplainRecyclerview.setVisibility(View.GONE);
            if (tradingRecyclerview != null) tradingRecyclerview.setVisibility(View.VISIBLE);
        }
        showDialog();
        initHttpData();
    }

    /**
     * 申诉列表
     *
     * @param page
     */
    private void getComplainList(int page) {
        showDialog();
        HttpUtil.getComplainList(page, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                dismissDialog();
                List<ComplainListBean.InfoBean> data = JSON.parseArray(Arrays.toString(info), ComplainListBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(data) && data.size() > 0) {
                    if (tradingComplainRecyclerview != null)
                        tradingComplainRecyclerview.setVisibility(View.VISIBLE);
                    if (noData != null) noData.setVisibility(View.GONE);
                    Log.v("tags", info[0]);
                    if (page == 1) {
                        if (mComplainAdapter != null) {
                            mComplainAdapter.setNewData(data);
                        }
                    } else {
                        if (mComplainAdapter != null) {
                            mComplainAdapter.addData(data);
                        }
                    }
                    if (data.size() < 15) {
                        if (refreshLayout != null) {
                            refreshLayout.setEnableLoadMore(false);
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            footer.setNoMoreData(true);
                        }
                    } else {
                        if (refreshLayout != null) {
                            refreshLayout.setEnableLoadMore(true);
                        }
                    }
                } else {
                    if (page == 1) {
                        if (tradingComplainRecyclerview != null)
                            tradingComplainRecyclerview.setVisibility(View.GONE);
                        if (noData != null) noData.setVisibility(View.VISIBLE);
                    }
                    if (refreshLayout != null) {
                        refreshLayout.setEnableLoadMore(false);
                    }
                }
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        });
    }
}
