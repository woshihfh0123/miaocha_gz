package com.mc.phonelive.activity.shop;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.GoodsItemAdapter;
import com.mc.phonelive.bean.GoodsItemBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *商品
 * 新888888
 *
 */

public class GoodsListActivity extends AbsActivity {

    private com.scwang.smartrefresh.layout.SmartRefreshLayout mSrl;
    private android.support.v7.widget.RecyclerView mRv;
    private GoodsItemAdapter mAdapter;
    private String type="";
    private int page=1;
    private TextView tv_noinfo;
    private String store_id="";
    private TextView tv_submit;
    private LinearLayout ll_check;
    private ImageView iv_check;
    private boolean checkAll=false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_goods;
    }

    @Override
    protected void main() {
        setTitle("商品");
        setBarModel(true);
        store_id=getIntent().getStringExtra("id");
        mSrl = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.srl);
        mRv = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv);
        tv_noinfo=findViewById(R.id.tv_noinfo);
        tv_submit=findViewById(R.id.tv_submit);
        ll_check=findViewById(R.id.ll_check);
        iv_check=findViewById(R.id.iv_check);
        mAdapter=new GoodsItemAdapter();
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
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGoods();
            }
        });
        ll_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAll){
                    checkAll=false;
                    iv_check.setImageResource(R.drawable.check_u);
                }else{
                    checkAll=true;
                    iv_check.setImageResource(R.drawable.check_y);
                }
                List<GoodsItemBean> checkList = mAdapter.getData();
                for(int i=0;i<checkList.size();i++){
                    checkList.get(i).setCheck(checkAll);
                }
                mAdapter.setNewData(checkList);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.iv_check){
                    if(mAdapter.getData().get(position).isCheck()){
                        mAdapter.getData().get(position).setCheck(false);
                    }else{
                        mAdapter.getData().get(position).setCheck(true);
                    }
                    mAdapter.notifyItemChanged(position);
                }
            }
        });
    }

    private void saveGoods() {
            String goodsIds="";
        List<GoodsItemBean> nlist = mAdapter.getData();
        for(int i=0;i<nlist.size();i++){
            if(nlist.get(i).isCheck()){
                goodsIds=goodsIds+nlist.get(i).getId()+",";
            }
        }
        if(goodsIds.endsWith(",")){
            goodsIds=goodsIds.substring(0,goodsIds.length()-1);
        }
        Log.e("SSSSS:",goodsIds);
        EventBusUtil.postEvent(new EventBean("send_ids_from_add",goodsIds));
        finish();

    }

    private void getList() {
        HttpUtil.getGoodsList(store_id,page, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<GoodsItemBean> nowsBean = (ArrayList<GoodsItemBean>) JSON.parseArray(obj.getString("goods_list"), GoodsItemBean.class);
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
