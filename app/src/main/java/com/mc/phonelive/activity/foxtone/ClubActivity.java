package com.mc.phonelive.activity.foxtone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.adapter.foxtone.ClubAdapter;
import com.mc.phonelive.bean.foxtone.ClubBean;
import com.mc.phonelive.custom.RatioRoundImageView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
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

/**
 * 俱乐部
 */
public class ClubActivity extends AbsActivity implements ClubAdapter.InfoClubListener {
    @BindView(R.id.rightView)
    TextView rightView;
    @BindView(R.id.search_edit)
    TextView searchEdit;
    @BindView(R.id.club_search_img)
    ImageView clubSearchImg;
    @BindView(R.id.club_close_img)
    ImageView clubCloseImg;
    @BindView(R.id.club_search)
    TextView clubSearch;
    @BindView(R.id.club_rule)
    TextView clubRule;
    @BindView(R.id.club_head_img)
    RatioRoundImageView clubHeadImg;
    @BindView(R.id.club_head_name)
    TextView clubHeadName;
    @BindView(R.id.club_head_phone)
    TextView clubHeadPhone;
    @BindView(R.id.club_head_people)
    TextView clubHeadPeople;
    @BindView(R.id.club_head_submit)
    TextView clubHeadSubmit;
    @BindView(R.id.club_recyclerview)
    RecyclerView clubRecyclerview;
    @BindView(R.id.no_data)
    TextView noData;
    @BindView(R.id.myclub_layout)
    LinearLayout myclub_layout;
    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private ClubBean.InfoBean mData = new ClubBean.InfoBean();
    List<ClubBean.InfoBean.ListBean> mList = new ArrayList<>();
    ClubAdapter mAdapter;
    private int page = 1;
    private String mRule = "";//俱乐部规则
    private String mKeywords = "";//关键字搜索

    @Override
    protected int getLayoutId() {
        return R.layout.club_layout;
    }

    @Override
    protected void main() {
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initHttpData();
            }
        });
        setAdapter();
        showDialog();
        initHttpData();
    }

    private void setAdapter() {
        mAdapter = new ClubAdapter(R.layout.club_item_view, mList);
        mAdapter.AddClubListener(this);
        clubRecyclerview.setAdapter(mAdapter);
        clubRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    private void initHttpData() {

        HttpUtil.getClubList(page + "", mKeywords, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                dismissDialog();
                List<ClubBean.InfoBean> mData = JSON.parseArray(Arrays.toString(info), ClubBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(mData) && mData.size() > 0) {
                    if (!DataSafeUtils.isEmpty(mData.get(0).getMy_club())) {
                        setMyClub(mData.get(0).getMy_club());
                    }
                    if (!DataSafeUtils.isEmpty(mData.get(0).getClub_list()) && mData.get(0).getClub_list().size() > 0) {
                        if (clubRecyclerview != null) clubRecyclerview.setVisibility(View.VISIBLE);
                        if (noData != null) noData.setVisibility(View.GONE);
                        mList = mData.get(0).getClub_list();
                        if (mAdapter != null) {
                            if (page == 1) {
                                mAdapter.setNewData(mData.get(0).getClub_list());
                            } else {
                                mAdapter.addData(mData.get(0).getClub_list());
                            }
                        }
                        if (mData.get(0).getClub_list().size() < 15) {
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
                        if (page == 1) {
                            if (clubRecyclerview != null)
                                clubRecyclerview.setVisibility(View.GONE);
                            if (noData != null) noData.setVisibility(View.VISIBLE);
                        }
                        if (refreshLayout != null)
                            refreshLayout.setEnableLoadMore(false);
                    }
                }
            }

            @Override
            public void onFinish() {
                dismissDialog();
                if (refreshLayout != null) {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    private void setMyClub(ClubBean.InfoBean.MyClubBean data) {
        if (!DataSafeUtils.isEmpty(data.getUid())) {
            myclub_layout.setVisibility(View.VISIBLE);
        } else {
            myclub_layout.setVisibility(View.GONE);
        }
        if (!DataSafeUtils.isEmpty(data.getBadge())) {
            Glide.with(mContext).load(data.getBadge()).into(clubHeadImg);
        }
        if (!DataSafeUtils.isEmpty(data.getName())) {
            clubHeadName.setText(data.getName());
        }
        if (!DataSafeUtils.isEmpty(data.getPhone())) {
            clubHeadPhone.setText("电话：" + data.getPhone());
        }
        if (!DataSafeUtils.isEmpty(data.getCounts())) {
            clubHeadPeople.setText("人数：" + data.getCounts());
        }
        if (!DataSafeUtils.isEmpty(data.getWeb_url())) {
            mRule = data.getWeb_url();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rightView, R.id.edit_layout, R.id.club_rule, R.id.club_head_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightView://创建俱乐部
                mContext.startActivity(new Intent(mContext, CreateClubActivity.class));
                break;
            case R.id.edit_layout:
                mContext.startActivity(new Intent(mContext, SearchClubActivity.class));
                break;
            case R.id.club_rule:
                if (!DataSafeUtils.isEmpty(mRule)) {
                    WebViewActivity.forward(mContext, mRule);
                }
                break;
            case R.id.club_head_submit:
                mContext.startActivity(new Intent(mContext, MyClubActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusModel code) {
        Log.v("tags", code.getCode() + "===========code");
        switch (code.getCode()) {
            case "club_refresh":
                initHttpData();
                break;
        }
    }

    @Override
    protected void onDestroy01() {
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void addClubData(String id) {
        HttpUtil.joinClub(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                initHttpData();
            }

            @Override
            public void onError() {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
