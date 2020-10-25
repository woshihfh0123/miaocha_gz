package com.mc.phonelive.activity.shop;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.adapter.ZbfwAdapter;
import com.mc.phonelive.bean.ServiceBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.views.ClearEditText;

import java.util.Arrays;
import java.util.List;

/**
 *直播服务
 * 新888888
 *
 */

public class ZbServiceActivity extends AbsActivity {

    private RecyclerView mRv;
    private ZbfwAdapter mAdapter;
    private ClearEditText cet;
    private String gozb="";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_zb_service;
    }

    @Override
    protected void main() {
        setTitle("直播服务");
        gozb=getIntent().getStringExtra("gozb");
        mRv = (RecyclerView)findViewById(R.id.rv);
        cet = (ClearEditText)findViewById(R.id.cet);
        mAdapter=new ZbfwAdapter();
        mRv.setLayoutManager(Utils.getGvManager(mContext,4));
        mRv.setAdapter(mAdapter);
        getList();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(Utils.isNotEmpty(mAdapter.getData().get(position).getHref())){
                    WebViewActivity.forward(mContext, mAdapter.getData().get(position).getHref());
                }
            }
        });
    }
    private void getList() {
        HttpUtil.getService(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
//                    JSONObject obj = JSON.parseObject(info[0]);
                    List<ServiceBean> nowsBean = JSON.parseArray(Arrays.toString(info), ServiceBean.class);
                    if(Utils.isNotEmpty(nowsBean)){
                      mAdapter.setNewData(nowsBean);
                      if(Utils.isNotEmpty(gozb)){
                          WebViewActivity.forward(mContext, mAdapter.getData().get(0).getHref());
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  finish();
                              }
                          });

                      }
                    }
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
                if(Utils.isNotEmpty(mAdapter)&&Utils.isNotEmpty(mAdapter.getData())){
//                    tv_noinfo.setVisibility(View.GONE);
                }else{
//                    tv_noinfo.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
