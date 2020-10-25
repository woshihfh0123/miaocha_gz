package com.mc.phonelive.activity.shop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.JfBtmAdapter;
import com.mc.phonelive.adapter.JfTopAdapter;
import com.mc.phonelive.bean.ScoreBtmBean;
import com.mc.phonelive.bean.ScoreTopBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 *积分
 * 新888888
 *
 */

public class ScoreActivity extends AbsActivity {
    private SmartRefreshLayout smr;
    private TextView mTv_jf;
    private TextView mTv_jfmx;
    private TextView mTv_jfdd;
    private android.support.v7.widget.RecyclerView mRv_gv3;
    private android.support.v7.widget.RecyclerView mRv_gv2;
    private JfTopAdapter topAdapter;
    private JfBtmAdapter btmAdapter;
    private int page=1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_score_fw;
    }

    @Override
    protected void main() {
        setTitle("积分");
        smr = (SmartRefreshLayout) findViewById(R.id.smr);
        mTv_jf = (TextView) findViewById(R.id.tv_jf);
        mTv_jfmx = (TextView) findViewById(R.id.tv_jfmx);
        mTv_jfdd = (TextView) findViewById(R.id.tv_jfdd);
        mRv_gv3 = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_gv3);
        mRv_gv2 = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_gv2);
        topAdapter=new JfTopAdapter();
        mRv_gv2.setLayoutManager(Utils.getGvManager(mContext,2));

        btmAdapter=new JfBtmAdapter();
        mRv_gv3.setLayoutManager(Utils.getGvManager(mContext,3));
        mRv_gv3.setAdapter(topAdapter);
        mRv_gv2.setAdapter(btmAdapter);
        mTv_jfmx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,ScoreListActivity.class));
            }
        });
        mTv_jfdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,ScoreOrderActivity.class));
            }
        });
        topAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(mContext,ScoreGoodsInfoActivity.class);
                intent.putExtra("goodsId",topAdapter.getData().get(position).getId());
                startActivity(intent);
            }
        });
        btmAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(mContext,ScoreGoodsInfoActivity.class);
                intent.putExtra("goodsId",btmAdapter.getData().get(position).getId());
                startActivity(intent);
            }
        });
        getList();
        smr.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                refreshLayout.finishRefresh(1000);
                getList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        page=1;
        getList();
    }

    private void getList() {
        HttpUtil.getScoreHome(page,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String num=obj.getString("integra");
                    mTv_jf.setText(num+"");
                    ArrayList<ScoreTopBean> hotlist = (ArrayList<ScoreTopBean>) JSON.parseArray(obj.getString("hotlist"), ScoreTopBean.class);
                    ArrayList<ScoreBtmBean> selectlist = (ArrayList<ScoreBtmBean>) JSON.parseArray(obj.getString("hotlist"), ScoreBtmBean.class);
                    if(Utils.isNotEmpty(hotlist)){
                        topAdapter.setNewData(hotlist);
                    }
                    if(Utils.isNotEmpty(selectlist)){
                        btmAdapter.setNewData(selectlist);
                    }
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

}
