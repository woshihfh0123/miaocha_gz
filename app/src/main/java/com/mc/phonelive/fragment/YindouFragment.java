package com.mc.phonelive.fragment;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.activity.foxtone.MusicalCenterActivity;
import com.mc.phonelive.adapter.foxtone.YinDouAdapter;
import com.mc.phonelive.bean.foxtone.YindouBean;
import com.mc.phonelive.dialog.SubmitGiveDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 乐豆列表
 * created by WWL on 2020/4/14 0014:14
 */
public class YindouFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.yindou_layout)
    RelativeLayout yindou_layout;
    @BindView(R.id.yindou_price)
    TextView yindouPrice;
    @BindView(R.id.yindou_Give)
    TextView yindouGive;
    @BindView(R.id.yinpiao_Give)
    TextView yinpiaoGive;
    @BindView(R.id.yindou_recyclerview)
    RecyclerView yindouRecyclerview;
    @BindView(R.id.yindou_title)
    TextView yindouTitle;
    @BindView(R.id.yindou_tip)
    ImageView yindouTip;
    @BindView(R.id.list_layout)
    LinearLayout list_layout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.free_title_layout)
    LinearLayout freeTitleLayout;
    private String state = "";//0.是可用音豆  1.是冻结音豆
    View layoutView;
    private int pages = 1;
    private boolean isFirst = true;
    private YinDouAdapter mDouListAdapter;
    List<YindouBean.InfoBean.ListBean> mList = new ArrayList<>();
    private String mRules = "";//解冻音豆规则

    public static YindouFragment newInstance() {
        YindouFragment fragment = new YindouFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isFirst) {
            pages = 1;
            initHttpData();
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.yindou_fragment, container, false);
        unbinder = ButterKnife.bind(this, layoutView);
        return layoutView;
    }

    @Override
    protected void initView(View view) {
        getListAdapter(mList);

        if (state.equals("1")) {
            yindouTitle.setText("可用音豆");
            yindouTip.setVisibility(View.GONE);
            yindouGive.setText("转赠");
            yinpiaoGive.setVisibility(View.VISIBLE);
            yindou_layout.setBackgroundResource(R.mipmap.yindou_bg1);
        } else if (state.equals("2")) {
            yindouTitle.setText("冻结音豆");
            yindouGive.setText("解冻");
            yindouTip.setVisibility(View.VISIBLE);
            yinpiaoGive.setVisibility(View.GONE);
            yindou_layout.setBackgroundResource(R.mipmap.yindou_bg2);
        }

        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pages++;
                initHttpData();
            }
        });

    }


    /**
     * 明细
     *
     * @param detalsList
     */
    private void getListAdapter(List<YindouBean.InfoBean.ListBean> detalsList) {
        mDouListAdapter = new YinDouAdapter(R.layout.dou_list_item_view, detalsList);
        yindouRecyclerview.setAdapter(mDouListAdapter);
        yindouRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void initHttpData() {
        showDialog();
        HttpUtil.getYinDouFeeList(pages, this.state, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                dismissDialog();
                isFirst = false;
                if (code == 0) {
                    if (!DataSafeUtils.isEmpty(info)) {
                        JSONObject obj = JSONObject.parseObject(info[0]);
                        YindouBean.InfoBean.ProfitsBean profitsBean = JSON.parseObject(obj.getString("profits"), YindouBean.InfoBean.ProfitsBean.class);
                        List<YindouBean.InfoBean.ListBean> infoList = JSON.parseArray(obj.getString("list"), YindouBean.InfoBean.ListBean.class);
                        if (!DataSafeUtils.isEmpty(profitsBean)) {
                            getYindouData(profitsBean);
                        }

                        if (!DataSafeUtils.isEmpty(infoList) && infoList.size() > 0) {
                            if (list_layout != null) {
                                list_layout.setVisibility(View.VISIBLE);
                            }
                            if (pages == 1) {
                                if (mDouListAdapter != null) {
                                    mDouListAdapter.setNewData(infoList);
                                }
                            } else {
                                if (mDouListAdapter != null) {
                                    mDouListAdapter.addData(infoList);
                                }
                            }
                            if (infoList.size() < 15) {
                                if (refreshLayout != null) {
                                    refreshLayout.setEnableLoadMore(false);
                                }
                            } else {
                                if (refreshLayout != null)
                                    refreshLayout.setEnableLoadMore(true);
                            }
                        } else {
                            if (pages == 1)
                                if (list_layout != null) {
                                    list_layout.setVisibility(View.GONE);
                                }

                            if (refreshLayout != null)
                                refreshLayout.setEnableLoadMore(false);
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
                if (refreshLayout != null) {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    private void getYindouData(YindouBean.InfoBean.ProfitsBean profitsBean) {
        if (this.state.equals("1")) {
            if (!DataSafeUtils.isEmpty(profitsBean.getFee()))
                yindouPrice.setText(profitsBean.getFee());
        } else {
            if (!DataSafeUtils.isEmpty(profitsBean.getFreeze_fee()))
                yindouPrice.setText(profitsBean.getFreeze_fee());
        }
        if (!DataSafeUtils.isEmpty(profitsBean.getWeb_url())) {
            mRules = profitsBean.getWeb_url();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.yindou_Give, R.id.yinpiao_Give})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.yindou_Give:
                if (state.equals("1"))
                    GiveDialog(yindouPrice.getText().toString(), "1");
                else
                    getActivity().startActivity(new Intent(getActivity(), MusicalCenterActivity.class));
                break;
            case R.id.yinpiao_Give:
                GiveDialog(yindouPrice.getText().toString(), "2");
                break;
        }
    }


    private void GiveDialog(String nums, String type) {
        String title1 = "";
        if (type.equals("1")) {
            title1 = "赠送人账号";
        } else {
            title1 = "音票数量 ";
        }
        SubmitGiveDialog giveDialog = new SubmitGiveDialog(getActivity(), "可转音豆", title1, nums, type) {
            @Override
            public void doConfirmUp(String account, String fee) {
                if (type.equals("1")) {
                    GiveHttpData(account, fee);
                } else {
                    GiveTransferVoteData(fee);
                }
            }

            @Override
            public void doCancel(View v) {

            }
        };
        giveDialog.show();
    }

    /**
     * 转赠音豆
     */
    private void GiveHttpData(String acount, String fee) {
        Log.v("tags", "account=" + acount + "------fee=" + fee);
        HttpUtil.yinDouTransfer(acount, fee, new HttpCallback() {

            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (code == 0) {
                    initHttpData();
                }
            }
        });
    }

    /**
     * 音豆转音票
     *
     * @param fee
     */
    private void GiveTransferVoteData(String fee) {
        Log.v("tags", "------fee=" + fee);
        HttpUtil.yinDouTransferVote(fee, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (code == 0) {
                    initHttpData();
                }
            }
        });
    }

    @OnClick(R.id.free_title_layout)
    public void onViewClicked() {
        if (this.state.equals("1")) {
            return;
        }
        if (!DataSafeUtils.isEmpty(mRules))
            WebViewActivity.forward(getActivity(), mRules);
    }
}
