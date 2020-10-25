package com.mc.phonelive.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.VideoPlayActivity;
import com.mc.phonelive.adapter.MainHomeVideoAdapter;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.bean.VideoHomeListBean;
import com.mc.phonelive.custom.RatioRoundImageView;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.event.VideoDeleteEvent;
import com.mc.phonelive.event.VideoScrollPageEvent;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.interfaces.VideoScrollDataHelper;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.VideoStorge;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by cxf on 2018/9/22.
 * 首页视频
 */

public class MainHomeVideoViewHolder extends AbsMainChildTopViewHolder implements OnItemClickListener<VideoBean> {

    @BindView(R.id.video_live_RecyclerView)
    RecyclerView videoLiveRecyclerView;
    @BindView(R.id.video_type_RecyclerView)
    RecyclerView videoTypeRecyclerView;
    private MainHomeVideoAdapter mAdapter;
    private VideoScrollDataHelper mVideoScrollDataHelper;
    private boolean isFirstLoad = true;
    BaseQuickAdapter<LiveBean, BaseViewHolder> mLiveAdapter;
    BaseQuickAdapter<VideoHomeListBean.DataBean.InfoBean.TypelistBean, BaseViewHolder> mTypeAdapter;

    List<LiveBean> mLiveList = new ArrayList<>();
    List<VideoHomeListBean.DataBean.InfoBean.TypelistBean> mVideoTypeList = new ArrayList<>();
    private String mTypeId = "";

    public MainHomeVideoViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_home_video;
    }

    @Override
    public void init() {
        super.init();
        videoLiveRecyclerView = (RecyclerView) findViewById(R.id.video_live_RecyclerView);
        videoTypeRecyclerView = (RecyclerView) findViewById(R.id.video_type_RecyclerView);
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_live_video);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRefreshView.setLayoutManager(manager);
//        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
//        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 5, 0);
//        decoration.setOnlySetItemOffsetsButNoDraw(true);
//        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<VideoBean>() {
            @Override
            public RefreshAdapter<VideoBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MainHomeVideoAdapter(mContext);
                    mAdapter.setOnItemClickListener(MainHomeVideoViewHolder.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getHomeVideoList(p,mTypeId, callback);
            }

            @Override
            public List<VideoBean> processData(String[] info) {
                List<VideoHomeListBean.DataBean.InfoBean> infolist = JSON.parseArray(Arrays.toString(info), VideoHomeListBean.DataBean.InfoBean.class);

                if (!DataSafeUtils.isEmpty(infolist.get(0).getLivelist()) && infolist.get(0).getLivelist().size()>0){
                    mLiveList = infolist.get(0).getLivelist();
                    mVideoTypeList = infolist.get(0).getTypelist();
                    if (!DataSafeUtils.isEmpty(infolist.get(0).getLivelist()))
                        if (mLiveAdapter != null && infolist.get(0).getLivelist().size() > 0)
                            mLiveAdapter.setNewData(infolist.get(0).getLivelist());
                }
                if (isFirstLoad) {
                    isFirstLoad = false;
                    if (!DataSafeUtils.isEmpty(infolist.get(0).getTypelist()))
                        if (mTypeAdapter != null && infolist.get(0).getTypelist().size() > 0) {
                            mTypeId = infolist.get(0).getTypelist().get(0).getId();
                            for (int i = 0; i < infolist.get(0).getTypelist().size(); i++) {
                                if (i == 0)
                                    infolist.get(0).getTypelist().get(i).setSelcted(true);
                                else
                                    infolist.get(0).getTypelist().get(i).setSelcted(false);
                            }
                            mTypeAdapter.setNewData(infolist.get(0).getTypelist());
                        }
                }
                return infolist.get(0).getVideolist();
            }

            @Override
            public void onRefresh(List<VideoBean> list) {
                VideoStorge.getInstance().put(Constants.VIDEO_HOME, list);
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
            public void onCreate() {
                EventBus.getDefault().register(MainHomeVideoViewHolder.this);
            }

            @Override
            public void onDestroy() {
                EventBus.getDefault().unregister(MainHomeVideoViewHolder.this);
            }
        };
        mVideoScrollDataHelper = new VideoScrollDataHelper() {

            @Override
            public void loadData(int p, HttpCallback callback) {
                Log.v("log", "1111111111---");
                HttpUtil.getHomeVideoList(p,mTypeId, callback);
            }
        };

        setLiveAdapter(mLiveList);
        setVideoTypeAdapter(mVideoTypeList);
    }

    /**
     * 获取推荐直播用户
     *
     * @param mLiveList
     */
    private void setLiveAdapter(List<LiveBean> mLiveList) {
        mLiveAdapter = new BaseQuickAdapter<LiveBean, BaseViewHolder>(R.layout.home_video_livelist_item, mLiveList) {
            @Override
            protected void convert(BaseViewHolder helper, LiveBean item) {
                helper.setText(R.id.live_username, item.getUserNiceName());
                if (!DataSafeUtils.isEmpty(item.getAvatarThumb()))
                Glide.with(mContext).load(item.getAvatarThumb()).into((RatioRoundImageView) helper.getView(R.id.live_userimg));
                else{
                    Glide.with(mContext).load(R.mipmap.ic_launcher).into((RatioRoundImageView) helper.getView(R.id.live_userimg));
                }
            }
        };
        videoLiveRecyclerView.setAdapter(mLiveAdapter);
        videoLiveRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mLiveAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LiveBean bean = mLiveAdapter.getData().get(position);
                watchLive(bean, Constants.LIVE_HOME, position);
            }
        });
    }


    /**
     * 获取类型
     *
     * @param mVideoTypeList
     */
    private void setVideoTypeAdapter(List<VideoHomeListBean.DataBean.InfoBean.TypelistBean> mVideoTypeList) {
        mTypeAdapter = new BaseQuickAdapter<VideoHomeListBean.DataBean.InfoBean.TypelistBean, BaseViewHolder>(R.layout.home_video_typelist_item, mVideoTypeList) {
            @Override
            protected void convert(BaseViewHolder helper, VideoHomeListBean.DataBean.InfoBean.TypelistBean item) {

                TextView mTypeName = helper.getView(R.id.type_name);
                if (!DataSafeUtils.isEmpty(item.getTitle())) {
                    mTypeName.setText(item.getTitle());
                }
                if (item.isSelcted()) {
                    mTypeName.setTextColor(mContext.getResources().getColor(R.color.white));
                    mTypeName.setBackgroundResource(R.drawable.home_type_bg_select);
                } else {
                    mTypeName.setTextColor(mContext.getResources().getColor(R.color.textColor2));
                    mTypeName.setBackgroundResource(R.drawable.home_type_bg);
                }
            }
        };
        videoTypeRecyclerView.setAdapter(mTypeAdapter);
        videoTypeRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoHomeListBean.DataBean.InfoBean.TypelistBean bean = mTypeAdapter.getData().get(position);
                mTypeId = bean.getId();
                for (int i = 0; i < mTypeAdapter.getData().size(); i++) {
                    {
                        if (i == position) {
                            mTypeAdapter.getData().get(i).setSelcted(true);
                        } else {
                            mTypeAdapter.getData().get(i).setSelcted(false);
                        }
                    }
                }
                mTypeAdapter.notifyDataSetChanged();
                if (mRefreshView != null) {
                    mRefreshView.initData();
                }
            }
        });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoScrollPageEvent(VideoScrollPageEvent e) {
        if (Constants.VIDEO_HOME.equals(e.getKey()) && mRefreshView != null) {
            mRefreshView.setPage(e.getPage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoDeleteEvent(VideoDeleteEvent e) {
        if (mAdapter != null) {
            mAdapter.deleteVideo(e.getVideoId());
            if (mAdapter.getItemCount() == 0 && mRefreshView != null) {
                mRefreshView.showNoData();
            }
        }
    }

    @Override
    public void onItemClick(VideoBean bean, int position) {
        int page = 1;
        if (mRefreshView != null) {
            page = mRefreshView.getPage();
        }
        VideoPlayActivity.setVideoBean(bean);
        VideoStorge.getInstance().putDataHelper(Constants.VIDEO_HOME, mVideoScrollDataHelper);
        VideoPlayActivity.forward(bean.getIs_shopping(), mContext, position, Constants.VIDEO_HOME, page);
    }
}
