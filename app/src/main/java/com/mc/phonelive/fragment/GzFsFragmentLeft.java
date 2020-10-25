package com.mc.phonelive.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.adapter.GzLeftAdapter;
import com.mc.phonelive.adapter.foxtone.TaskListAdapter;
import com.mc.phonelive.bean.FansItemBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 关注
 */
public class GzFsFragmentLeft extends BaseFragment implements TaskListAdapter.TaskListener {

    View layoutView;
    private com.scwang.smartrefresh.layout.SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRv;
    private TextView mTv_noinfo;
    private GzLeftAdapter mAdapter;
    private int page=1;

    public static GzFsFragmentLeft newInstance() {
        GzFsFragmentLeft fragment = new GzFsFragmentLeft();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.left_fragment_gz, container, false);
        return layoutView;
    }

    @Override
    protected void initView(View view) {
        mRefreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mTv_noinfo = (TextView) view.findViewById(R.id.tv_noinfo);
        mAdapter=new GzLeftAdapter();
        mRv.setLayoutManager(Utils.getLvManager(getContext()));
        mRv.setAdapter(mAdapter);
//        List<FarmilyBean> list=new ArrayList<>();
//        for(int i=0;i<20;i++){
//            list.add(new FarmilyBean());
//        }
//        mAdapter.setNewData(list);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                page=1;
                getList();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                page++;
                getList();
            }
        });
        getList();

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserHomeActivity.forward(getActivity(), mAdapter.getData().get(position).getId());
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch(view.getId()){
                    case R.id.li_item:
                        UserHomeActivity.forward(getContext(), mAdapter.getItem(position).getId());
                        break;
                    case R.id.tv_status:
                        if(mAdapter.getData().get(position).getIsattention().equals("1")){
                            mAdapter.getData().get(position).setIsattention("0");
                        }else{
                            mAdapter.getData().get(position).setIsattention("1");
                        }
                        mAdapter.notifyItemChanged(position);
                        changeGz(position,mAdapter.getData().get(position).getId());
                        break;
                    default:

                        break;
                }
            }
        });
    }

    private void changeGz(int position,String id) {
        HttpUtil.setGz(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String isattent = obj.getString("isattent");
                    mAdapter.getData().get(position).setIsattention(isattent);
                    mAdapter.notifyItemChanged(position);
                }
            }
        });

    }
    private void getList() {
        HttpUtil.getFansListLL("", page, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    ArrayList<FansItemBean> bannerList = (ArrayList<FansItemBean>) JSON.parseArray(Arrays.toString(info), FansItemBean.class);
                    if(Utils.isNotEmpty(bannerList)){
                        mRefreshLayout.setEnableLoadMore(true);
                        if(page==1){
                            mAdapter.setNewData(bannerList);
                        }else{
                            mAdapter.addData(bannerList);
                        }

                    }else{
                        if(page==1){
                            mAdapter.setNewData(null);
                        }
                        mRefreshLayout.setEnableLoadMore(false);
                    }
                }
            }
        });
    }

    @Override
    protected void initHttpData() {



    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 领取任务
     * @param id
     * @param type
     */
    @Override
    public void taskReceive(String id, String type) {
//        taskReceiveHttpData(id,type);
    }

    private void taskReceiveHttpData(String id, String type) {
//        HttpUtil.getReceiveTask(id, new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//
//            }
//        });
    }
}
