package com.mc.phonelive.activity.shop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.ZbAdapter;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.bean.ShopsHomeBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.presenter.CheckLivePresenter;
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
 * 分类店铺
 */

public class TypeShopsActivity extends AbsActivity {

    private RecyclerView mRv;
    private ZbAdapter mAdapter;
    private ClearEditText cet;
    private int page=1;
    private SmartRefreshLayout smr;
    private TextView tv_noinfo;
    private String catid="";
    private String catName="";
    private String typeid="";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_type_shops;
    }

    @Override
    protected void main() {
        setBarModel(true);
        catid=getIntent().getStringExtra("catid");
        catName=getIntent().getStringExtra("catName");
        setTitle(catName);
        mRv = (RecyclerView)findViewById(R.id.rv);
        cet = (ClearEditText)findViewById(R.id.cet);
        smr = findViewById(R.id.smr);
        tv_noinfo = findViewById(R.id.tv_noinfo);
        mAdapter=new ZbAdapter();
        mRv.setLayoutManager(Utils.getGvManager(mContext,2));
        mRv.setAdapter(mAdapter);
        cet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //点击搜索的时候隐藏软键盘
                    KeyboardUtil.closeKeyboard((AbsActivity) mContext);
//                    getData(true);
                    // 在这里写搜索的操作,一般都是网络请求数据
                    page=1;
                    getList();
                    return true;
                }
                return false;
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
                LiveBean liveBean=new LiveBean();//LiveBean
                liveBean.setUid(mAdapter.getData().get(position).getUid());
                liveBean.setStream(mAdapter.getData().get(position).getStream());
                liveBean.setThumb(mAdapter.getData().get(position).getThumb());
                liveBean.setPull(mAdapter.getData().get(position).getPull());
                liveBean.setUserNiceName(mAdapter.getData().get(position).getUser_nicename());
                liveBean.setIs_shopping(mAdapter.getData().get(position).getIs_shopping());
                liveBean.setAvatar(mAdapter.getData().get(position).getAvatar());
                watchLive(liveBean, Constants.LIVE_FOLLOW,position);
            }
        });
    }
    private CheckLivePresenter mCheckLivePresenter;
    public void watchLive(LiveBean liveBean, String key, int position) {
        if (mCheckLivePresenter == null) {
            mCheckLivePresenter = new CheckLivePresenter(mContext);
        }
        mCheckLivePresenter.watchLive(liveBean, key, position);
    }
    private void getList() {
        HttpUtil.getZbList("",page+"",catid,typeid,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<ShopsHomeBean.LivelistBean> liveList = (ArrayList<ShopsHomeBean.LivelistBean>) JSON.parseArray(obj.getString("list"), ShopsHomeBean.LivelistBean.class);
                    if(Utils.isNotEmpty(liveList)){
                        smr.setEnableLoadMore(true);
                        if(page==1){
                            mAdapter.setNewData(liveList);
                        }else{
                            mAdapter.addData(liveList);
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
