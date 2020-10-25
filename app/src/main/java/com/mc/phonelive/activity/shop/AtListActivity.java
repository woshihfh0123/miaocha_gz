package com.mc.phonelive.activity.shop;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.PingLunAdapter;
import com.mc.phonelive.bean.PingLunListBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 *头部消息分类列表---喜欢---评论---@我的
 * 新888888
 *
 */

public class AtListActivity extends AbsActivity {

    private com.scwang.smartrefresh.layout.SmartRefreshLayout mSrl;
    private android.support.v7.widget.RecyclerView mRv;
    private PingLunAdapter mAdapter;
    private String type="";
    private int page=1;
    private TextView tv_noinfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_sb;
    }

    @Override
    protected void main() {
        setTitle("@我的");
        mSrl = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.srl);
        mRv = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv);
        tv_noinfo=findViewById(R.id.tv_noinfo);
        mAdapter=new PingLunAdapter();
        mRv.setLayoutManager(Utils.getLvManager(mContext));
        mRv.setAdapter(mAdapter);
        getList();
        mSrl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                page=1;
                getList();
            }
        });
        mSrl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                page++;
                getList();
            }
        });
    }

    private void getList() {
        HttpUtil.getMyL(page, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<PingLunListBean> nowsBean = (ArrayList<PingLunListBean>) JSON.parseArray(obj.getString("list"), PingLunListBean.class);
                    if(Utils.isNotEmpty(nowsBean)){
                        if(page==1){
                            mAdapter.setNewData(nowsBean);
                        }else{
                            mAdapter.addData(nowsBean);
                        }
                    }else{
                        if(page==1){
                            mAdapter.setNewData(null);
                        }
                        mSrl.setEnableLoadMore(false);
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if(Utils.isNotEmpty(mAdapter)&&Utils.isNotEmpty(mAdapter.getData())){
                    tv_noinfo.setVisibility(View.GONE);
                }else{
                    tv_noinfo.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}
