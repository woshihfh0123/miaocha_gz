package com.mc.phonelive.views.shopmall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.MyTeamAdapter;
import com.mc.phonelive.bean.MyTeamVO;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by WWL on 2019/6/27 0027:19
 * 我的团队
 */
public class MyTeamListActivity extends AbsActivity {

    LinearLayout backLayout;
    TextView title_level;
    TextView oneUserTv;
    TextView twoUserTv;
    TextView userNumTv;
    TextView commissionNumTv;//累计佣金
    RecyclerView teamRecyclerview;
    RelativeLayout noContent;
    SmartRefreshLayout refreshLayout;
    ClassicsFooter footer;
    private String mType = "1";//1.以及用户  2.二级用户
    private MyTeamAdapter mAdapter;
    List<MyTeamVO.DataBean.ListBean> mList = new ArrayList<>();
    MyTeamVO.DataBean mData = new MyTeamVO.DataBean();
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.shop_myteam_activity;
    }

    protected boolean isStatusBarWhite() {
        return true;
    }

    private void initView() {
        title_level = findViewById(R.id.title_level);
        backLayout = findViewById(R.id.back_layout);
        oneUserTv = findViewById(R.id.one_user_tv);
        twoUserTv = findViewById(R.id.two_user_tv);
        userNumTv = findViewById(R.id.user_num_tv);
        commissionNumTv = findViewById(R.id.commission_num_tv);//累计佣金
        teamRecyclerview = findViewById(R.id.team_recyclerview);
        noContent = findViewById(R.id.no_content);
        refreshLayout = findViewById(R.id.refreshLayout);
        footer = findViewById(R.id.footer);

    }

    @Override
    protected void main() {
        super.main();

        initView();


        mData = MyTeamVO.getTeamData();
        mList = mData.getList();
        setAdapter(mList);
        refreshLayout.setEnableRefresh(false);
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

        initHttpData();
    }

    private void initHttpData() {
        mAdapter.setNewData(mList);

            HttpUtil.myTeamList(page+"",mType,new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (refreshLayout != null) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                    if (info.length > 0) {
                        Log.v("tags", "info=" + info[0]);
                        if (teamRecyclerview != null)
                            teamRecyclerview.setVisibility(View.VISIBLE);
                        if (noContent != null)
                            noContent.setVisibility(View.GONE);

                        List<MyTeamVO.DataBean.ListBean> list = JSON.parseArray(Arrays.toString(info),  MyTeamVO.DataBean.ListBean.class);
                        if (mAdapter != null) {
                            if (page == 1) {
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
                        if (page==1) {
                            if (teamRecyclerview != null)
                                teamRecyclerview.setVisibility(View.GONE);
                            if (noContent != null)
                                noContent.setVisibility(View.VISIBLE);
                        }else{
                            refreshLayout.setEnableLoadMore(false);
                        }
                    }

                }

                @Override
                public void onFinish() {
                    if (refreshLayout != null) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                }
            });
    }


    private void setAdapter(List<MyTeamVO.DataBean.ListBean> mList) {
        mAdapter = new MyTeamAdapter(R.layout.mytean_item_view, mList);
        teamRecyclerview.setAdapter(mAdapter);
        teamRecyclerview.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MyTeamVO.DataBean.ListBean bean = mAdapter.getData().get(i);
                Bundle bundle = new Bundle();
                bundle.putString("id", bean.getUid());
//                open(MyTeamDetailActivity.class, bundle, 0);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.back_layout, R.id.one_user_tv, R.id.two_user_tv, R.id.title_level})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                MyTeamListActivity.this.finish();
                break;
            case R.id.title_level:
                ToastUtil.show("代理级别");
                mContext.startActivity(new Intent(mContext,MyTeamAgentLevelActivity.class));
                break;
            case R.id.one_user_tv:
                mType = "1";
                oneUserTv.setBackgroundResource(R.drawable.myteam_one_bg);
                twoUserTv.setBackground(null);
                oneUserTv.setTextColor(getResources().getColor(R.color.white));
                twoUserTv.setTextColor(getResources().getColor(R.color.black));
                page = 1;
                initHttpData();
                break;
            case R.id.two_user_tv:
                mType = "2";
                oneUserTv.setBackground(null);
                twoUserTv.setBackgroundResource(R.drawable.myteam_two_bg);
                oneUserTv.setTextColor(getResources().getColor(R.color.black));
                twoUserTv.setTextColor(getResources().getColor(R.color.white));
                page = 1;
                initHttpData();
                break;
        }
    }
}
