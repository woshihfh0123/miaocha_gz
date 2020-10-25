package com.mc.phonelive.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.adapter.ZbGvAdapter;
import com.mc.phonelive.bean.FarmilyBean;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.bean.WyZbBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.presenter.CheckLivePresenter;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.views.ClearEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 分类直播
 */

public class TypeZbListActivity extends AbsActivity {

    private RecyclerView mRv;
    private ZbGvAdapter mAdapter;
    private ClearEditText cet;
    private String type;
    private String catid;
    private SmartRefreshLayout smr;
    private int page=1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_type_zb;
    }

    @Override
    protected void main() {
        type=getIntent().getStringExtra("type");
        catid=getIntent().getStringExtra("catid");
        if(Utils.isEmpty(catid)){
            catid="";
        }else{
            if(catid.equals("1")){
                setTitle("养生");
            }else if(catid.equals("2")){
                setTitle("问医");
            }
        }
        if(Utils.isEmpty(type)){
            type="";
        }else{
            if(type.equals("0")){
                setTitle("免费观看");
            }else if(type.equals("1")){
                setTitle("门票付费");
            }else if(type.equals("2")){
                setTitle("计时付费");
            }else if(type.equals("3")){
                setTitle("1对1付费");
            }
        }

        setBarModel(false);
        smr = (SmartRefreshLayout) findViewById(R.id.smr);
        mRv = (RecyclerView)findViewById(R.id.rv);
        cet = (ClearEditText)findViewById(R.id.cet);
        mAdapter=new ZbGvAdapter();
        mRv.setLayoutManager(Utils.getGvManager(mContext,2));
        mRv.setAdapter(mAdapter);
        List<FarmilyBean> list=new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add(new FarmilyBean());
        }
        getList();
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
    private void getList() {//String p,String type,String catid
        HttpUtil.getWy(page+"",type,catid,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<WyZbBean> nlist = (ArrayList<WyZbBean>) JSON.parseArray(obj.getString("list"), WyZbBean.class);
                    if(Utils.isNotEmpty(nlist)){
                        smr.setEnableLoadMore(true);
                        if(page==1){
                            mAdapter.setNewData(nlist);
                        }else{
                            mAdapter.addData(nlist);
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

            }
        });
    }


}
