package com.mc.phonelive.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.adapter.ShopAllAdapter;
import com.mc.phonelive.bean.StoreBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.KeyboardUtil;
import com.mc.phonelive.views.ClearEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 *
 * 全部店铺
 */

public class AllShopsActivity extends AbsActivity {

    private RecyclerView mRv;
    private ShopAllAdapter mAdapter;
    private ClearEditText cet;
    private SmartRefreshLayout smr;
    private int page=1;
    private TextView tv_noinfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_shops;
    }

    @Override
    protected void main() {
        setTitle("全部店铺");
        setBarModel(true);
        mRv = (RecyclerView)findViewById(R.id.rv);
        cet = (ClearEditText)findViewById(R.id.cet);
        smr = findViewById(R.id.smr);
        tv_noinfo = findViewById(R.id.tv_noinfo);
        mAdapter=new ShopAllAdapter();
        mRv.setLayoutManager(Utils.getGvManager(mContext,3));
        mRv.setAdapter(mAdapter);
//        List<FarmilyBean> list=new ArrayList<>();
//        for(int i=0;i<20;i++){
//            list.add(new FarmilyBean());
//        }
//        mAdapter.setNewData(list);
        cet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //点击搜索的时候隐藏软键盘
                    KeyboardUtil.closeKeyboard((AbsActivity) mContext);
                    // 在这里写搜索的操作,一般都是网络请求数据
                    page=1;
                    getList();
                    return true;
                }
                return false;
            }
        });
        cet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Utils.isEmpty(s.toString())){
                    page=1;
                    getList();
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
        page=1;
        getList();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, MyShopActivity.class);
                intent.putExtra("status", "1");
                intent.putExtra("store_id", mAdapter.getData().get(position).getId()+ "");
                mContext.startActivity(intent);
            }
        });
    }

    private void getList() {
        HttpUtil.getShopsList(cet.getText().toString().trim(),page+"",new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<StoreBean> bannerList = (ArrayList<StoreBean>) JSON.parseArray(obj.getString("storelist"), StoreBean.class);
                    if(Utils.isNotEmpty(bannerList)){
                        smr.setEnableLoadMore(true);
                        if(page==1){
                            mAdapter.setNewData(bannerList);
                        }else{
                            mAdapter.addData(bannerList);
                        }

                    }else{
                        if(page==1){
                            mAdapter.setNewData(null);
                        }
                        smr.setEnableLoadMore(false);
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
