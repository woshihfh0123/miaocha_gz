package com.mc.phonelive.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.VideoPlayActivity;
import com.mc.phonelive.adapter.PSVideoHomeAdapter;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.custom.ItemDecoration;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.event.VideoDeleteEvent;
import com.mc.phonelive.event.VideoScrollPageEvent;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.interfaces.VideoScrollDataHelper;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.VideoStorge;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by cxf on 2018/12/14.
 * 用户个人中心发布的视频列表
 */

public class PersonDongTai extends AbsViewHolder implements OnItemClickListener<VideoBean> {

    private RefreshView mRefreshView;
    private PSVideoHomeAdapter mAdapter;
    private String mToUid;
    private VideoScrollDataHelper mVideoScrollDataHelper;
    private ActionListener mActionListener;
    private String mKey;
    private int page=1;

    public PersonDongTai(Context context, ViewGroup parentView, String toUid) {
        super(context, parentView, toUid);
    }

    @Override
    protected void processArguments(Object... args) {
        mToUid = (String) args[0];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.person_dt;
    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
//    }



    @Override
    public void init() {
//        if (TextUtils.isEmpty(mToUid)) {
//            return;
//        }
        Log.e("DDDDDDDDDD",mToUid+"");
//        EventBusUtil.register(mContext);
        mKey = Constants.VIDEO_USER + this.hashCode();
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setRefreshEnable(false);
        if (Utils.isNotEmpty(mToUid)&&mToUid.equals(AppConfig.getInstance().getUid())) {
            mRefreshView.setNoDataLayoutId(R.layout.view_no_data_video_home);
        } else {

                mRefreshView.setNoDataLayoutId(R.layout.view_no_data_video_home_2);

        }
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 0, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<VideoBean>() {
            @Override
            public RefreshAdapter<VideoBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new PSVideoHomeAdapter(mContext);
                    mAdapter.setOnItemClickListener(PersonDongTai.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getHomeVideo("1",mToUid, p, callback);
            }

            @Override
            public List<VideoBean> processData(String[] info) {
                Log.e("ffffff:",info.length+"");

                JSONObject obj = JSON.parseObject(info[0]);
                List<VideoBean>videoList=JSON.parseArray(obj.getString("list"),VideoBean.class);
                if(Utils.isNotEmpty(videoList)){
                    return  videoList;
                }else{
                    if(page==1){
                        mRefreshView.showNoData();
                    }else{
                        mRefreshView.hideNoData();

                    }
                }
                return null;
            }

            @Override
            public void onRefresh(List<VideoBean> list) {
                VideoStorge.getInstance().put(mKey, list);
            }

            @Override
            public void onNoData(boolean noData) {
                Log.e("DDDDDDDDDDD:",noData+"");
                if(noData){
                    mRefreshView.showNoData();
                }
            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (dataCount < 20) {
                    mRefreshView.setLoadMoreEnable(false);
                } else {
                    mRefreshView.setLoadMoreEnable(true);
                }
            }
        });
        mVideoScrollDataHelper = new VideoScrollDataHelper() {

            @Override
            public void loadData(int p, HttpCallback callback) {
                page=p;
                HttpUtil.getHomeVideo("1",mToUid, p, callback);
            }
        };
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onCreate() {
//                EventBus.getDefault().register(PersonDongTai.this);
            }

            @Override
            public void onDestroy() {
                HttpUtil.cancel(HttpConsts.GET_HOME_VIDEO);
//                EventBus.getDefault().unregister(PersonDongTai.this);
            }
        };
        loadData();

    }

    public void setRefreshEnable(boolean enable) {
//        if (mRefreshView != null) {
//            mRefreshView.setRefreshEnable(enable);
//        }
    }

    public void loadData() {
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    public void release() {
//        EventBusUtil.unregister(mContext);
        Log.e("PPPPPPPttttssssss:","");
        HttpUtil.cancel(HttpConsts.GET_HOME_VIDEO);
        mActionListener = null;
//        EventBus.getDefault().unregister(this);
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoScrollPageEvent(VideoScrollPageEvent e) {
        Log.e("PPPPPPPtttt:",e.getPage()+"");
        if (!TextUtils.isEmpty(mKey) && mKey.equals(e.getKey()) && mRefreshView != null) {
            mRefreshView.setPage(e.getPage());
        }
    }//send_info_from_click_view
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(EventBean event){
        switch(event.getCode()){
            case "send_info_from_click_view":
                Log.e("WWWWW","6666");
                VideoBean   bean= (VideoBean) event.getFirstObject();
                int position= (int) event.getSecondObject();
                int page = 1;
                if (mRefreshView != null) {
                    page = mRefreshView.getPage();
                }
                VideoPlayActivity.setVideoBean(bean);
                VideoStorge.getInstance().putDataHelper(mKey, mVideoScrollDataHelper);
                VideoPlayActivity.forward(bean.getIs_shopping(),mContext, position, mKey, page);

                break;
            default:

                break;
        }
    };
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoDeleteEvent(VideoDeleteEvent e) {
        Log.e("PPPPPPP:",e.getVideoId()+"");
        if (mAdapter != null) {
            mAdapter.deleteVideo(e.getVideoId());
            if (mAdapter.getItemCount() == 0 && mRefreshView != null) {
                mRefreshView.showNoData();
            }
        }
        if (mActionListener != null) {
            mActionListener.onVideoDelete(1);
        }
    }

    @Override
    public void onItemClick(VideoBean bean, int position) {
        Log.e("kkkkkk",position+"");
//        int page = 1;
//        if (mRefreshView != null) {
//            page = mRefreshView.getPage();
//        }
//        MainActivity.setVideoBean(bean);
//        VideoStorge.getInstance().putDataHelper(mKey, mVideoScrollDataHelper);
//        VideoPlayActivity.forward(bean.getIs_shopping(),mContext, position, mKey, page);



    }

    public interface ActionListener {
        void onVideoDelete(int deleteCount);
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

}
