package com.mc.phonelive.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.adapter.HeiMingDanAdapter;
import com.mc.phonelive.bean.HeiMdBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Arrays;
import java.util.List;

/**
 *
 * 黑名单管理
 */

public class CenterHmdActivity extends AbsActivity {

    private RecyclerView mRv;
    private HeiMingDanAdapter mAdapter;
    private SmartRefreshLayout refreshLayout;
    private int page=1;
    private TextView tv_noinfo;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_center_hmd;
    }

    @Override
    protected void main() {
        setTitle("黑名单管理");
        mRv = (RecyclerView)findViewById(R.id.rv);
        tv_noinfo = (TextView) findViewById(R.id.tv_noinfo);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        mAdapter=new HeiMingDanAdapter();
        mRv.setLayoutManager(Utils.getLvManager(mContext));
        mRv.setAdapter(mAdapter);
        getList();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                page=1;
                getList();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(500);
                page++;
                getList();
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.tv_status){
                    setBlack(mAdapter.getData().get(position).getId(),position);
                }
            }
        });
    }

    private void setBlack(String id,int position) {
        HttpUtil.setBlack(id,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String isblack = obj.getString("isblack");
                    if(Utils.isNotEmpty(isblack)){//拉黑了
                        mAdapter.getData().get(position).setIsblack(isblack);
                        mAdapter.notifyItemChanged(position);
                    }

                }
            }

        });
    }

    private void getList() {
        HttpUtil.getHmdList(page,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    List<HeiMdBean> list = JSON.parseArray(Arrays.toString(info), HeiMdBean.class);
                    if(Utils.isNotEmpty(list)){
                        refreshLayout.setEnableLoadMore(true);
                        if(page==1){
                            mAdapter.setNewData(list);
                        }else{
                            mAdapter.addData(list);
                        }

                    }else{
                        refreshLayout.setEnableLoadMore(false);
                    }
                }else{
                    refreshLayout.setEnableLoadMore(false);
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
