package com.mc.phonelive.views;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ViewGroup;

import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.VideoPlayActivity;
import com.mc.phonelive.adapter.MainHomeVideoAdapter;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.interfaces.VideoScrollDataHelper;
import com.mc.phonelive.utils.VideoStorge;
import com.mc.phonelive.views.foxtone.AbsMainVideoParentViewHolder;

import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 首页视频关注
 */

public class MainHomeFollowViewHolder extends AbsMainChildTopViewHolder implements OnItemClickListener<VideoBean> {

    private MainHomeVideoAdapter mAdapter;
    private VideoScrollDataHelper mVideoScrollDataHelper;

    public MainHomeFollowViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.view_main_home_new_follow;
    }

    @Override
    public void init() {
        super.init();
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_video_follow);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRefreshView.setLayoutManager(manager);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<VideoBean>() {
            @Override
            public RefreshAdapter<VideoBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MainHomeVideoAdapter(mContext);
                    mAdapter.setOnItemClickListener(MainHomeFollowViewHolder.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getvideoFollow(p, callback);
            }

            @Override
            public List<VideoBean> processData(String[] info) {
//                if (info.length > 0) {
//                    return JSON.parseArray(Arrays.toString(info), VideoBean.class);
//                }
                return null;
            }

            @Override
            public void onRefresh(List<VideoBean> list) {
                VideoStorge.getInstance().put(Constants.VIDEO_HOME_FOLLOW, list);
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

        mVideoScrollDataHelper = new VideoScrollDataHelper() {

            @Override
            public void loadData(int p, HttpCallback callback) {
                Log.v("log", "1111111111---");
                HttpUtil.getvideoFollow(p, callback);
            }
        };
    }

    @Override
    public void loadData() {
        Log.v("tags","生命周期1111111111111111111111111--------"+ AbsMainVideoParentViewHolder.mIndex);
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    @Override
    public void onItemClick(VideoBean bean, int position) {
        int page = 1;
        if (mRefreshView != null) {
            page = mRefreshView.getPage();
        }
        VideoPlayActivity.setVideoBean(bean);
        VideoStorge.getInstance().putDataHelper(Constants.VIDEO_HOME_FOLLOW, mVideoScrollDataHelper);
        VideoPlayActivity.forward(bean.getIs_shopping(), mContext, position, Constants.VIDEO_HOME_FOLLOW, page);
    }
}
