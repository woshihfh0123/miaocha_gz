package com.mc.phonelive.activity.foxtone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.foxtone.TeamMessageAdapter;
import com.mc.phonelive.bean.foxtone.MessageBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息
 */
public class MyMessageActivity extends AbsActivity {
    @BindView(R.id.message_recyclerview)
    RecyclerView messageRecyclerview;
    @BindView(R.id.no_content)
    RelativeLayout noContent;

    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String mTuid = "";
    private int page = 1;
    List<MessageBean.InfoBean> mList = new ArrayList<>();
    TeamMessageAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.my_message_view;
    }

    @Override
    protected void main() {
        setTitle("团队详情");

        String tuid = this.getIntent().getStringExtra("tuid");
        if (!DataSafeUtils.isEmpty(tuid)) {
            this.mTuid = tuid;
        }

        setAdapter(mList);
        showDialog();
        initHttpData();

        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initHttpData();
            }
        });

    }

    private void setAdapter(List<MessageBean.InfoBean> mList) {
        mAdapter = new TeamMessageAdapter(R.layout.my_message_item_view, mList);
        messageRecyclerview.setAdapter(mAdapter);
        messageRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private void initHttpData() {
        HttpUtil.getTeamDetail(page, mTuid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                dismissDialog();
                List<MessageBean.InfoBean> data = JSON.parseArray(Arrays.toString(info), MessageBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(data) && data.size() > 0) {
                    if (messageRecyclerview != null)
                        messageRecyclerview.setVisibility(View.VISIBLE);
                    if (noContent != null) noContent.setVisibility(View.GONE);
                    mList = data;
                    if (page == 1) {
                        if (mAdapter != null) {
                            mAdapter.setNewData(data);
                        }
                    } else {
                        if (mAdapter != null) {
                            mAdapter.addData(data);
                        }
                    }
                    if (refreshLayout != null) {
                        if (data.size() < 15) {
                            refreshLayout.setEnableLoadMore(false);
                            footer.setNoMoreData(true);
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            refreshLayout.setEnableLoadMore(true);
                        }
                    }
                } else {
                    if (page == 1) {
                        if (messageRecyclerview != null)
                            messageRecyclerview.setVisibility(View.GONE);
                        if (noContent != null) noContent.setVisibility(View.VISIBLE);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
