package com.mc.phonelive.activity.shop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.FoodCommentAdapter;
import com.mc.phonelive.bean.CommentVO;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by WWL on 2019/5/16 0016:19
 * 更多评论
 */
public class MoreCommentListActivity extends AbsActivity {
    @BindView(R.id.comment_recyclerview)
    RecyclerView commentRecyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    List<CommentVO.DataBean.ListBean> mList = new ArrayList<>();
    FoodCommentAdapter mAdapter;
    @BindView(R.id.iv_noinfo)
    ImageView ivNoinfo;
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    private String mGoodsId = "";
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.more_comment_list;
    }

    @Override
    protected void main() {
        titleView.setText("评论");
        setBarModel(true);
        String id = this.getIntent().getStringExtra("id");
        if (!DataSafeUtils.isEmpty(id)) {
            mGoodsId = id;
        }
        showDialog();
        getCommentAdapter(mList);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initHttpData();
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initHttpData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                initHttpData();
            }
        });
        showDialog();
        initHttpData();
    }

    protected void initHttpData() {

        HttpUtil.OrderGetEvalList(page + "", mGoodsId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                dismissDialog();
                if (code == 0) {
                    if (info.length > 0) {
                        if (commentRecyclerview != null)
                            commentRecyclerview.setVisibility(View.VISIBLE);
                        ivNoinfo.setVisibility(View.GONE);
                        List<CommentVO.DataBean.ListBean> list = JSON.parseArray(Arrays.toString(info), CommentVO.DataBean.ListBean.class);
                        if (!DataSafeUtils.isEmpty(list) && list.size() > 0) {
                            mList = list;
                            if (page == 1) {
                                mAdapter.setNewData(list);
                            } else {
                                if (mAdapter != null) {
                                    mAdapter.addData(list);
                                }
                            }
                        }
                        if (info.length < 15) {
                            refreshLayout.setEnableLoadMore(false);
                        } else {
                            refreshLayout.setEnableLoadMore(true);
                        }
                    } else {
                        if (page == 1) {
                            if (commentRecyclerview != null)
                                commentRecyclerview.setVisibility(View.GONE);
                            ivNoinfo.setVisibility(View.VISIBLE);
                        } else {
                            refreshLayout.setEnableLoadMore(false);
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

    }


    private void getCommentAdapter(List<CommentVO.DataBean.ListBean> redlist) {
        mAdapter = new FoodCommentAdapter(R.layout.commentlist_item_adapter, redlist);
        commentRecyclerview.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        commentRecyclerview.setLayoutManager(manager);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        this.finish();
    }
}
