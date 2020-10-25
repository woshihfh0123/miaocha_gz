package com.mc.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.bean.MyYhqBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.adapter.shop.YhqAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 优惠券Fragment  888888
 */
public class YhqFragment extends BaseFragment  {
    private YhqAdapter mAdapter;
    private String state = "";//12全部 0待付款 1待发货 5待评价  6已关闭
   //button类型  1删除 2取消 3去付款 4 查看物流 5确认收货 6评价
    private int mCurrentPage = 1;
    private boolean ISTART = false;
    private View layoutView;
private RecyclerView mRv;
private SmartRefreshLayout refreshLayout;
private RelativeLayout no_content;

    public static YhqFragment newInstance() {
        YhqFragment fragment = new YhqFragment();
        return fragment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.order_fragment, container, false);
        mRv=layoutView.findViewById(R.id.shopping_order_recycler_view);
        no_content=layoutView.findViewById(R.id.no_content);
        refreshLayout=layoutView.findViewById(R.id.refreshLayout);
        mRv.setLayoutManager(Utils.getLvManager(getActivity()));
        mAdapter=new YhqAdapter();
        mRv.setAdapter(mAdapter);
        return layoutView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getList();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage=1;
                refreshLayout.finishRefresh(1000);
                getList();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage++;
                refreshLayout.finishLoadMore(1000);
                getList();
            }
        });
    }

    private void getList() {
        HttpUtil.getYhq(mCurrentPage,state,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<MyYhqBean> nowsBean = (ArrayList<MyYhqBean>) JSON.parseArray(obj.getString("list"), MyYhqBean.class);
                    if(Utils.isNotEmpty(nowsBean)){
                        if(mCurrentPage==1){
                            mAdapter.setNewData(nowsBean);
                        }else{
                            mAdapter.addData(nowsBean);
                        }
                    }else{
                        if(mCurrentPage==1){
                            mAdapter.setNewData(null);
                        }
                        refreshLayout.setEnableLoadMore(false);
                    }

                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
                if(Utils.isNotEmpty(mAdapter)&&Utils.isNotEmpty(mAdapter.getData())){
                    no_content.setVisibility(View.GONE);
                }else{
                    no_content.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    protected void initView(View view) {
    }

    @Override
    protected void initHttpData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered) {
            EventBus.getDefault().unregister(this);
        }
    }

}
