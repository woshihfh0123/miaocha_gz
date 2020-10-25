package com.mc.phonelive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.foxtone.MyMessageActivity;
import com.mc.phonelive.adapter.foxtone.MyTeamListAdapter;
import com.mc.phonelive.bean.foxtone.MyTeamBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 团队列表
 */
public class TeamFragment extends BaseFragment {

    Unbinder unbinder;
    View layoutView;
    @BindView(R.id.task_recyclerview)
    RecyclerView taskRecyclerview;
    @BindView(R.id.no_data)
    TextView noContent;
    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private MyTeamListAdapter mAdapter;
    private List<MyTeamBean.InfoBean.TeamList> mList = new ArrayList<>();
    MyTeamBean.InfoBean mData = new MyTeamBean.InfoBean();
    private boolean ISTART = false;
    private int page=1;
    private String mType="0";
    public static TeamFragment newInstance() {
        TeamFragment fragment = new TeamFragment();
        return fragment;
    }

    public void setStatus(String type){
        this.mType =type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.team_fragment_view, container, false);
        unbinder = ButterKnife.bind(this, layoutView);
        return layoutView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (ISTART) {
                page=1;
                initHttpData();
            }
        }
    }

    @Override
    protected void initView(View view) {
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initHttpData();
            }
        });
        setAdapter(mList);
    }

    @Override
    protected void initHttpData() {

        HttpUtil.TeamListData(page,mType, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
            ISTART =true;
                List<MyTeamBean.InfoBean> data = JSON.parseArray(Arrays.toString(info), MyTeamBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(data) && data.size() > 0) {
                    if (!DataSafeUtils.isEmpty(data.get(0).getTeam_list()) && data.get(0).getTeam_list().size()>0) {
                        if (taskRecyclerview!=null)taskRecyclerview.setVisibility(View.VISIBLE);
                        if (noContent!=null)noContent.setVisibility(View.GONE);

                        if (page==1){
                            if (mAdapter!=null){
                                mAdapter.setNewData(data.get(0).getTeam_list());
                            }
                        }else{
                            if (mAdapter!=null){
                                mAdapter.addData(data.get(0).getTeam_list());
                            }
                        }
                        if (refreshLayout!=null){
                            if (data.get(0).getTeam_list().size()<15){
                                refreshLayout.setEnableLoadMore(false);
                                footer.setNoMoreData(true);
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }else{
                                refreshLayout.setEnableLoadMore(true);
                            }
                        }
                    }else{
                        if (page==1){
                            if (taskRecyclerview!=null)taskRecyclerview.setVisibility(View.GONE);
                            if (noContent!=null)noContent.setVisibility(View.VISIBLE);
                        }
                        if (refreshLayout!=null){
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                if (refreshLayout!=null){
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }


    private void setAdapter(List<MyTeamBean.InfoBean.TeamList> mList) {
        mAdapter = new MyTeamListAdapter(R.layout.team_fragment_item_view, mList);
        taskRecyclerview.setAdapter(mAdapter);
        taskRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyTeamBean.InfoBean.TeamList data = mAdapter.getData().get(position);
                Intent intent = new Intent(getActivity(), MyMessageActivity.class);
                intent.putExtra("tuid",data.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
