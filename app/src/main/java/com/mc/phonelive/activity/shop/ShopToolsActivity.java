package com.mc.phonelive.activity.shop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.MyShopActivity;
import com.mc.phonelive.activity.MyShopDetailActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.adapter.MySaveAdapter;
import com.mc.phonelive.adapter.ZsAdapter;
import com.mc.phonelive.bean.FarmilyBean;
import com.mc.phonelive.bean.MySaveBean;
import com.mc.phonelive.dialog.TishiDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 *直播服务
 * 新888888
 *
 */

public class ShopToolsActivity extends AbsActivity {

    private RecyclerView mRv,rv_save;
    private ZsAdapter mAdapter;
    private MySaveAdapter mySaveAdapter;
    private SmartRefreshLayout smr;
    private int page=1;
    private String is_live_auth="";
    private String is_store="";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_tools;
    }

    @Override
    protected void main() {
        setTitle("购物助手");
        setBarModel(true);
        is_store=AppConfig.getInstance().getUserBean().getIs_store();
        is_live_auth=AppConfig.getInstance().getUserBean().getIs_live_auth();
        mRv = (RecyclerView)findViewById(R.id.rv);
        smr = (SmartRefreshLayout) findViewById(R.id.smr);
        rv_save = (RecyclerView)findViewById(R.id.rv_save);
        mAdapter=new ZsAdapter();
        mySaveAdapter=new MySaveAdapter();
        mRv.setLayoutManager(Utils.getGvManager(mContext,4));
        rv_save.setLayoutManager(Utils.getGvManager(mContext,2));
        mRv.setAdapter(mAdapter);
        rv_save.setAdapter(mySaveAdapter);
        List<FarmilyBean> list=new ArrayList<>();
        for(int i=0;i<4;i++){
            list.add(new FarmilyBean());
        }
        mAdapter.setNewData(list);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==0){
                    Log.e("HHHHHHHH:",is_live_auth+"-----"+is_store);
                    if(Utils.isNotEmpty(is_live_auth)){
                        if(is_live_auth.equals("1")){//是主播
                            if(is_store.equals("1")){//开通了小店
                                Intent intent = new Intent(mContext, MyShopActivity.class);
                                intent.putExtra("status", "0");
                                intent.putExtra("store_id", AppConfig.getInstance().getUid() + "");
                                mContext.startActivity(intent);
                            }else{//没有小店
                                WebViewActivity.forward(mContext, AppConfig.HOST + "/Appapi/store/apply.html");
                            }

                        }else{//不是主播
                            new TishiDialog(mContext,"","").show();
                        }
                    }

                }else if(position==1){
//                    startActivity(new Intent(mContext,OrderMyActivity.class));
                    startActivity(new Intent(mContext, MyOrderActivity.class));
                }
//                else if(position==2){
//                   Intent intent = new Intent(mContext, CartMainActivity.class);
//                    mContext.startActivity(intent);
//                }
                else if(position==2){
                    startActivity(new Intent(mContext,YhqActivity.class));
                }else if(position==3){
                    startActivity(new Intent(mContext,ScoreActivity.class));
                }
            }
        });
        smr.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                page=1;
                getList();
            }
        });
        smr.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                page++;
                getList();
            }
        });
        mySaveAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, MyShopDetailActivity.class);
                intent.putExtra("id", mySaveAdapter.getData().get(position).getId());
//                intent.putExtra("status", mySaveAdapter.getData().get(position).get());
                intent.putExtra("store_id", "0");
                mContext.startActivity(intent);
            }
        });
        getList();
    }

    private void getList() {
        HttpUtil.getSave(page,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<MySaveBean> nowsBean = (ArrayList<MySaveBean>) JSON.parseArray(obj.getString("list"), MySaveBean.class);
                    if(Utils.isNotEmpty(nowsBean)){
                        if(page==1){
                            mySaveAdapter.setNewData(nowsBean);
                        }else{
                            mySaveAdapter.addData(nowsBean);
                        }
                    }else{
                        smr.setEnableLoadMore(false);
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
