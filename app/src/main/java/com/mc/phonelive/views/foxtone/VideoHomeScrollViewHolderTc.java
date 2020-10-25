package com.mc.phonelive.views.foxtone;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.MainActivity;
import com.mc.phonelive.adapter.foxtone.VideoHomeScrollAdapterTc;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.custom.VideoLoadingBar;
import com.mc.phonelive.event.FollowEvent;
import com.mc.phonelive.event.VideoCommentEvent;
import com.mc.phonelive.event.VideoLikeEvent;
import com.mc.phonelive.event.VideoShareEvent;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.interfaces.VideoScrollDataHelper;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.views.AbsViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/11/26.
 * 视频滑动
 */

public class VideoHomeScrollViewHolderTc extends AbsViewHolder implements
        VideoPlayHomeViewHolder.ActionListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, VideoHomeScrollAdapterTc.ActionListener {

    private VideoPlayHomeViewHolder mVideoPlayViewHolder;
    private View mPlayView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private VideoHomeScrollAdapterTc mVideoScrollAdapter;
    private VideoPlayHomeWrapViewHolder mVideoPlayWrapViewHolder;
    private VideoLoadingBar mVideoLoadingBar;
    private int mPages = 1;
    private HttpCallback mRefreshCallback;//下拉刷新回调
    private HttpCallback mLoadMoreCallback;//上拉加载更多回调
    private VideoScrollDataHelper mVideoDataHelper;
    private VideoBean mVideoBean;
    private boolean mPaused;//生命周期暂停

    private String mTypeId = "0";
    List<VideoBean> mVideoList = new ArrayList<>();

    public VideoHomeScrollViewHolderTc(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected void processArguments(Object... args) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_home_scroll;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);

        initVideo();
        mLifeCycleListener = new LifeCycleAdapter() {

            @Override
            public void onResume() {
                Log.v("tags", "------------onResume");
                mPaused = false;
                if (mVideoPlayViewHolder != null) {
                    mVideoPlayViewHolder.resumePlay();
                }
            }

            @Override
            public void onPause() {
                ToastUtil.show("暂停3333");
                mPaused = true;
                if (mVideoPlayViewHolder != null) {
                    mVideoPlayViewHolder.pausePlay();
                }
            }

            @Override
            public void onReStart() {
                super.onReStart();
                Log.v("tags", "------------onReStart");
            }
        };


        mVideoDataHelper = new VideoScrollDataHelper() {

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getHomeVideoListTc(p, mTypeId, mRefreshCallback);
            }
        };
        mRefreshCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<VideoBean> infolist = JSON.parseArray(Arrays.toString(info), VideoBean.class);
                    if (Utils.isNotEmpty(infolist)) {
                        mVideoList = infolist;
                        if (mVideoScrollAdapter == null) {
                            mVideoScrollAdapter = new VideoHomeScrollAdapterTc(mContext, mVideoList, 0);
//                            mVideoScrollAdapter.setActionListener(mContext);
                            mVideoScrollAdapter.setActionListener(VideoHomeScrollViewHolderTc.this);
                            mRecyclerView.setAdapter(mVideoScrollAdapter);
                        } else if (mVideoScrollAdapter != null) {
                            mVideoScrollAdapter.setList(infolist);
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mRefreshLayout != null) {
                    mRefreshLayout.setRefreshing(false);
                }
            }
        };
        mLoadMoreCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.e("mmmmmmxxxxsssssss:",mPages+"");
                Log.e("FFFFFF:",info.toString());
                if (code == 0) {
                    List<VideoBean> infolist = JSON.parseArray(Arrays.toString(info),VideoBean.class);
                    if (infolist.size() > 0) {
                        mVideoList = infolist;
                        if (mVideoScrollAdapter != null) {
                            mVideoScrollAdapter.insertList(infolist);
                        }
//                        if (mVideoPlayViewHolder != null) {
//                            mVideoPlayViewHolder.startPlay(mVideoBean);
//                        }
                    } else {
//                        ToastUtil.show(R.string.video_no_more_video);
                    }
                }
            }
        };
        initHttpData();

    }



    private void initVideo() {

        mVideoPlayViewHolder = new VideoPlayHomeViewHolder(mContext, null);
        mVideoPlayViewHolder.setActionListener(this);
        mPlayView = mVideoPlayViewHolder.getContentView();
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mVideoLoadingBar = (VideoLoadingBar) findViewById(R.id.video_loading);
        findViewById(R.id.input_tip).setOnClickListener(this);
        findViewById(R.id.btn_face).setOnClickListener(this);

        mRefreshLayout.setOnRefreshListener(this);

    }

    private void initHttpData() {
        Log.e("ssssss:",mPages+"");
        if (mPages > 1) {
            HttpUtil.getHomeVideoListTc(mPages, mTypeId, mLoadMoreCallback);
        } else {
            Log.v("tags", "数据加载...");
            HttpUtil.getHomeVideoListTc(1, mTypeId, mRefreshCallback);
        }
    }

    @Override
    public void onPageSelected(VideoPlayHomeWrapViewHolder videoPlayWrapViewHolder, boolean needLoadMore) {
        if (videoPlayWrapViewHolder != null) {
            VideoBean videoBean = videoPlayWrapViewHolder.getVideoBean();
            if (videoBean != null) {
                mVideoBean = videoBean;
                mVideoPlayWrapViewHolder = videoPlayWrapViewHolder;
                videoPlayWrapViewHolder.addVideoView(mPlayView);
                if (mVideoPlayViewHolder != null) {
                    Log.v("tags", "--------dd---------onPageSelected");
                        mVideoPlayViewHolder.startPlay(videoBean);
                }
                if (mVideoLoadingBar != null) {
                    mVideoLoadingBar.setLoading(true);
                }
            }
            Log.v("tags", "--------dd---------onPageSelected---"+needLoadMore);
            if (needLoadMore) {
                onLoadMore();
            }
        }
    }

    @Override
    public void onPageOutWindow(VideoPlayHomeWrapViewHolder vh) {
        if (mVideoPlayWrapViewHolder != null && mVideoPlayWrapViewHolder == vh && mVideoPlayViewHolder != null) {
            mVideoPlayViewHolder.stopPlay();
        }
    }


    @Override
    public void onVideoDeleteAll() {
//        ((VideoPlayActivity) mContext).onBackPressed();
    }

    public void release() {
        HttpUtil.cancel(HttpConsts.GET_HOME_VIDEO_LIST);
        EventBus.getDefault().unregister(this);
        if (mVideoPlayViewHolder != null) {
            mVideoPlayViewHolder.release();
        }
        mVideoPlayWrapViewHolder = null;
        if (mVideoLoadingBar != null) {
            mVideoLoadingBar.endLoading();
        }
        mVideoLoadingBar = null;
        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener(null);
        }
        mRefreshLayout = null;
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.release();
        }
        mVideoScrollAdapter = null;
        mVideoDataHelper = null;
    }


    @Override
    public void onPlayBegin() {
        if (mVideoLoadingBar != null) {
            mVideoLoadingBar.setLoading(false);
        }
    }

    @Override
    public void onPlayLoading() {
        if (mVideoLoadingBar != null) {
            mVideoLoadingBar.setLoading(true);
        }
    }

    @Override
    public void onFirstFrame() {
        if (mVideoPlayWrapViewHolder != null) {
            mVideoPlayWrapViewHolder.onFirstFrame();
        }
    }

    /**
     * 通过生命周期 判断视频是否播放或者暂停
     *
     * @param e
     */
    private boolean isFirstChoiseFollow = true;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoTypeEvent(EventBusModel e) {
        String code = e.getCode();
        if (code.equals("video_type")) {
            int mIndex = (int) e.getObject();
            if (mIndex!=0) {
                HttpUtil.cancel(HttpConsts.GET_HOME_VIDEO_LIST);
                if (mVideoPlayViewHolder != null) {
                    mVideoPlayViewHolder.pausePlay();
                }
            } else {
                if (mVideoPlayViewHolder != null) {
                    mVideoPlayViewHolder.resumePlay();

                }
            }
        } else if (code.equals("main_video_puase")|| code.equals("main_video_puase1")) {//主界面底部点击了其他模块按钮
            if (mVideoPlayViewHolder != null)
                mVideoPlayViewHolder.pausePlay();
        } else if (code.equals("main_video_play") && AbsMainVideoParentViewHolder.mIndex == 0) {//主界面点击了首页按钮
            if (mVideoPlayViewHolder != null)
                mVideoPlayViewHolder.resumePlay();
        } else if (code.equals("main_video_stop")) {
            if (mVideoPlayViewHolder != null) {
                mVideoPlayViewHolder.stopPlay();
                mVideoPlayViewHolder.release();
            }
        }else if(code.equals("send_position_from_check_tab")){
            int mIndex = (int) e.getObject();
            if(mIndex==0) {
                if (mVideoDataHelper != null) {
                    mPages = 1;
                    mVideoDataHelper.loadData(1, mRefreshCallback);
                }
                Log.e("bbbbbbbbbbbbbbbbbb2222:", "" + mIndex);
            }
        }

    }

    /**
     * 关注发生变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        if (mVideoScrollAdapter != null && mVideoPlayWrapViewHolder != null) {
            VideoBean videoBean = mVideoPlayWrapViewHolder.getVideoBean();
            if (videoBean != null) {
                mVideoScrollAdapter.onFollowChanged(!mPaused, videoBean.getId(), e.getToUid(), e.getIsAttention());
            }
        }
    }

    /**
     * 点赞发生变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoLikeEvent(VideoLikeEvent e) {
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.onLikeChanged(!mPaused, e.getVideoId(), e.getIsLike(), e.getLikeNum());
        }
    }

    /**
     * 分享数发生变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoShareEvent(VideoShareEvent e) {
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.onShareChanged(e.getVideoId(), e.getShareNum());
        }
    }

    /**
     * 评论数发生变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoCommentEvent(VideoCommentEvent e) {
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.onCommentChanged(e.getVideoId(), e.getCommentNum());
        }
    }


    /**
     * 删除视频
     */
    public void deleteVideo(VideoBean videoBean) {
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.deleteVideo(videoBean);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_tip:
                openCommentInputWindow(false);
                break;
            case R.id.btn_face:
                openCommentInputWindow(true);
                break;
        }
    }

    /**
     * 打开评论输入框
     */
    private void openCommentInputWindow(boolean openFace) {
        if (mVideoBean != null) {
            ((MainActivity) mContext).openCommentInputWindow(openFace, mVideoBean, null);
        }
    }

    /**
     * VideoBean 数据发生变化
     */
    public void onVideoBeanChanged(String videoId) {
        if (mVideoScrollAdapter != null) {
            mVideoScrollAdapter.onVideoBeanChanged(videoId);
        }
    }


    @Override
    public void onRefresh() {
        mPages = 1;
        if (mVideoDataHelper != null) {
            mVideoDataHelper.loadData(mPages, mRefreshCallback);
        }
    }

    /**
     * 加载更多
     */
    private void onLoadMore() {
        Log.e("mmmmmm:",mPages+"");
        Log.e("mmmmmm:",(mVideoDataHelper==null)+"");
        mPages++;
        if (mVideoDataHelper != null) {
            Log.e("mmmmmmxxxx:",mPages+"");
//            mVideoDataHelper.loadData(mPages, mLoadMoreCallback);
            initHttpData();
        }
    }
}
