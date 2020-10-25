package com.mc.phonelive.views.foxtone;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.VideoPlayActivity;
import com.mc.phonelive.adapter.GzHomeAdapter;
import com.mc.phonelive.adapter.foxtone.VideoHomeScrollAdapter;
import com.mc.phonelive.bean.LiveBean;
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
import com.mc.phonelive.presenter.CheckLivePresenter;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.views.AbsViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by cxf on 2018/11/26.
 * 视频滑动
 */

public class VideoHomeFollowScrollViewHolderGz extends AbsViewHolder implements
        VideoPlayHomeViewHolder.ActionListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, VideoHomeScrollAdapter.ActionListener {

    private VideoPlayHomeViewHolder mVideoPlayViewHolder;
    private View mPlayView;
    private SwipeRefreshLayout mRefreshLayout;
    RelativeLayout noContent;
    private RecyclerView mRecyclerView;
    private VideoHomeScrollAdapter mVideoScrollAdapter;
    private VideoPlayHomeWrapViewHolder mVideoPlayWrapViewHolder;
    //    private VideoScrollDataHelper mVideoScrollDataHelper;
    private VideoLoadingBar mVideoLoadingBar;
    private int mPages = 1;
    private HttpCallback mRefreshCallback;//下拉刷新回调
    private HttpCallback mLoadMoreCallback;//上拉加载更多回调
    private VideoScrollDataHelper mVideoDataHelper;
    private VideoBean mVideoBean;
    private boolean mPaused;//生命周期暂停
    private CheckLivePresenter mCheckLivePresenter;
    private String mTypeId = "0";
    List<VideoBean> mVideoList = new ArrayList<>();
    private boolean isFirst = true;
    private boolean isFirstChoiseFollow = true;
    private RecyclerView rv_h;
    public VideoHomeFollowScrollViewHolderGz(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected void processArguments(Object... args) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_home_gzscroll;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);

        initVideo();
        mLifeCycleListener = new LifeCycleAdapter() {

            @Override
            public void onResume() {
                mPaused = false;
                if (mVideoPlayViewHolder != null) {
                    mVideoPlayViewHolder.resumePlay();
                }
            }

            @Override
            public void onPause() {
                mPaused = true;
                if (mVideoPlayViewHolder != null) {
                    mVideoPlayViewHolder.pausePlay();
                }
            }

        };


        mVideoDataHelper = new VideoScrollDataHelper() {

            @Override
            public void loadData(int p, HttpCallback callback) {
                mPages=p;
                HttpUtil.getvideoFollowGz(p,  mRefreshCallback);
            }
        };
        mRefreshCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                getTopList();
                Log.e("INFO:",info+"");
                if (code == 0) {
//                    List<VideoBean> infolist = JSON.parseArray(Arrays.toString(info), VideoBean.class);
                    if(Utils.isNotEmpty(info)){
                    JSONObject obj = JSON.parseObject(info[0]);
                    List<VideoBean> infolist = JSON.parseArray(obj.getString("list"), VideoBean.class);
                    if (!DataSafeUtils.isEmpty(infolist)) {
                        if (noContent != null) noContent.setVisibility(View.GONE);
                        mVideoList = infolist;
                        if (mVideoScrollAdapter == null) {
                            mVideoScrollAdapter = new VideoHomeScrollAdapter(mContext, mVideoList, 0);
                            mVideoScrollAdapter.setActionListener(VideoHomeFollowScrollViewHolderGz.this);
                            mRecyclerView.setAdapter(mVideoScrollAdapter);
                        } else if (mVideoScrollAdapter != null) {
                            mVideoScrollAdapter.setList(infolist);
                        }
                    }else{
                        if (mPages == 1) {
                            if (mRecyclerView != null)
                                mRecyclerView.setVisibility(View.GONE);
                            if (noContent != null) noContent.setVisibility(View.VISIBLE);
                        }
                    }
//                    List<VideoHomeListBean.DataBean.InfoBean> infolist = JSON.parseArray(Arrays.toString(info), VideoHomeListBean.DataBean.InfoBean.class);
//
//                    if (!DataSafeUtils.isEmpty(infolist.get(0).getVideolist())) {
//                        mVideoList = infolist.get(0).getVideolist();
//                        if (mVideoScrollAdapter == null) {
//                            mVideoScrollAdapter = new VideoHomeScrollAdapter(mContext, mVideoList, 0);
//                            mVideoScrollAdapter.setActionListener(VideoHomeFollowScrollViewHolder.this);
//                            mRecyclerView.setAdapter(mVideoScrollAdapter);
//                        } else if (mVideoScrollAdapter != null) {
//                            mVideoScrollAdapter.setList(infolist.get(0).getVideolist());
//                        }
//                    }
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
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    List<VideoBean> infolist = JSON.parseArray(obj.getString("list"), VideoBean.class);
//                    List<VideoBean> infolist = JSON.parseArray(Arrays.toString(info),VideoBean.class);
//                    List<VideoBean> infolist = JSON.parseArray(Arrays.toString(info),VideoBean.class);
                    Log.e("tttttt:",infolist.toString());
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


//                    List<VideoHomeListBean.DataBean.InfoBean> infolist = JSON.parseArray(Arrays.toString(info), VideoHomeListBean.DataBean.InfoBean.class);
//                    if (infolist.get(0).getVideolist().size() > 0) {
//                        mVideoList = infolist.get(0).getVideolist();
//                        if (mVideoScrollAdapter != null) {
//                            mVideoScrollAdapter.insertList(infolist.get(0).getVideolist());
//                        }
////                        if (mVideoPlayViewHolder != null) {
////                            mVideoPlayViewHolder.startPlay(mVideoBean);
////                        }
//                    } else {
//                        ToastUtil.show(R.string.video_no_more_video);
//                    }
                }
            }
        };
        initHttpData();

    }

    private void getTopList() {
        HttpUtil.getFollow(1, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
//                    List<VideoBean> infolist = JSON.parseArray(Arrays.toString(info), VideoBean.class);
                    if(Utils.isNotEmpty(info)){
                        JSONObject obj = JSON.parseObject(info[0]);
                        List<LiveBean> infolist = JSON.parseArray(obj.getString("list"), LiveBean.class);
                        if (!DataSafeUtils.isEmpty(infolist)) {
                            hAdapter.setNewData(infolist);
                            tv_show.setText(infolist.size()+"个直播");
                            tv_show.setVisibility(View.VISIBLE);
                        }else{
                            tv_show.setVisibility(View.GONE);
                        }
                        hAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {//33333333
                                LiveBean liveBean=new LiveBean();
                                liveBean=hAdapter.getData().get(position);
                                watchLive(liveBean, Constants.LIVE_FOLLOW,position);
                            }
                        });
                    }
                }
            }
        });
    }
    public void watchLive(LiveBean liveBean, String key, int position) {
        if (mCheckLivePresenter == null) {
            mCheckLivePresenter = new CheckLivePresenter(mContext);
        }
        mCheckLivePresenter.watchLive(liveBean, key, position);
    }
    private GzHomeAdapter hAdapter;
    private TextView tv_show;
    private void initVideo() {
        tv_show= (TextView) findViewById(R.id.tv_show);
        mVideoPlayViewHolder = new VideoPlayHomeViewHolder(mContext, null);
        mVideoPlayViewHolder.setActionListener(this);
        mPlayView = mVideoPlayViewHolder.getContentView();
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        noContent = (RelativeLayout) findViewById(R.id.no_content);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rv_h= (RecyclerView) findViewById(R.id.rv_h);
        rv_h.setLayoutManager(Utils.getHvManager(mContext));
        hAdapter=new GzHomeAdapter();
        rv_h.setAdapter(hAdapter);
//        List<FarmilyBean> nlist=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            nlist.add(new FarmilyBean());
//        }
//        hAdapter.setNewData(nlist);
       getTopList();
        mVideoLoadingBar = (VideoLoadingBar) findViewById(R.id.video_loading);
        findViewById(R.id.input_tip).setOnClickListener(this);
        findViewById(R.id.btn_face).setOnClickListener(this);

        mRefreshLayout.setOnRefreshListener(this);
        tv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rv_h.getVisibility()==View.VISIBLE){
                    rv_h.setVisibility(View.GONE);
                    tv_show.setVisibility(View.VISIBLE);
                }else{
                    rv_h.setVisibility(View.VISIBLE);
                    tv_show.setVisibility(View.GONE);
                }
            }
        });
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(rv_h.getVisibility()==View.VISIBLE){
                    rv_h.setVisibility(View.GONE);
                    tv_show.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

    }

    private void initHttpData() {
        Log.e("ttttttsssss：",mPages+"");
        if (mPages > 1) {
            HttpUtil.getvideoFollowGz(mPages,  mLoadMoreCallback);
        } else {
            Log.v("tags", "数据加载...");
            HttpUtil.getvideoFollowGz(1,  mRefreshCallback);
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
                    if (!isFirst) {
                        mVideoPlayViewHolder.startPlay(videoBean);
                    }
                }
                if (mVideoLoadingBar != null) {
                    mVideoLoadingBar.setLoading(true);
                }
                isFirst = false;
            }
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
        ((VideoPlayActivity) mContext).onBackPressed();
    }

    public void release() {
        HttpUtil.cancel(HttpConsts.GETVideoFOLLOW);
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoTypeEvent(EventBusModel e) {
        String code = e.getCode();
        if (code.equals("video_type")) {
            int mIndex = (int) e.getObject();
            if (mIndex!=1) {
                HttpUtil.cancel(HttpConsts.GETVideoFOLLOW);
                if (mVideoPlayViewHolder != null) {
                    mVideoPlayViewHolder.pausePlay();
                }
            } else {
                if (mVideoPlayViewHolder != null) {
                    mVideoPlayViewHolder.resumePlay();
                }
            }
        } else if (code.equals("main_video_puase") || code.equals("main_video_puase1")) {//主界面底部点击了其他模块按钮
            if (mVideoPlayViewHolder != null)
                mVideoPlayViewHolder.pausePlay();
        } else if (code.equals("main_video_play") && AbsMainVideoParentViewHolder.mIndex == 1) {//主界面点击了首页按钮
            if (mVideoPlayViewHolder != null)
                mVideoPlayViewHolder.resumePlay();
        } else if (code.equals("main_video_stop")) {
            if (mVideoPlayViewHolder != null) {
                mVideoPlayViewHolder.stopPlay();
                mVideoPlayViewHolder.release();
            }
        } else if (code.equals("video_play")) {//第一次打开项目，关注也在播放，处理了，但是滑动过来，没播放，下面是处理结果
            int mIndex = (int) e.getObject();
            if (isFirstChoiseFollow && mIndex == 1) {
                isFirstChoiseFollow = false;
                if (mVideoPlayViewHolder != null)
                    mVideoPlayViewHolder.startPlay(mVideoBean);
            }
        }else if(code.equals("send_position_from_check_tab")){
            int mIndex = (int) e.getObject();
            if(mIndex==1) {
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
            ((VideoPlayActivity) mContext).openCommentInputWindow(openFace, mVideoBean, null);
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
        mPages++;
        if (mVideoDataHelper != null) {
//            mVideoDataHelper.loadData(mPages, mLoadMoreCallback);
            initHttpData();
        }
    }
}
