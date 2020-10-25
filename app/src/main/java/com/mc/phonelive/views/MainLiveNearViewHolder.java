package com.mc.phonelive.views;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.MainHomeHotAdapter;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.utils.LiveStorge;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 直播附近
 */

public class MainLiveNearViewHolder extends AbsMainChildTopViewHolder implements OnItemClickListener<LiveBean> {

    private MainHomeHotAdapter mAdapter;


    public MainLiveNearViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_home_follow;
    }

    @Override
    public void init() {
        super.init();
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRefreshView.setLayoutManager(manager);
//        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
//        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 5, 5);
//        decoration.setOnlySetItemOffsetsButNoDraw(true);
//        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<LiveBean>() {
            @Override
            public RefreshAdapter<LiveBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MainHomeHotAdapter(mContext);
                    mAdapter.setOnItemClickListener(MainLiveNearViewHolder.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getNear(p, callback);
            }

            @Override
            public List<LiveBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), LiveBean.class);
            }

            @Override
            public void onRefresh(List<LiveBean> list) {
                LiveStorge.getInstance().put(Constants.LIVE_NEAR, list);
            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (dataCount < 10) {
                    mRefreshView.setLoadMoreEnable(false);
                } else {
                    mRefreshView.setLoadMoreEnable(true);
                }
            }
        });
        mLifeCycleListener = new LifeCycleAdapter() {

            @Override
            public void onDestroy() {
                L.e("main----MainLiveNearViewHolder-------LifeCycle---->onDestroy");
                HttpUtil.cancel(HttpConsts.GET_NEAR);
            }
        };
    }

    @Override
    public void loadData() {
        if (!isFirstLoadData()) {
            return;
        }
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    @Override
    public void onItemClick(LiveBean bean, int position) {
        watchLive(bean, Constants.LIVE_NEAR, position);
    }
}
