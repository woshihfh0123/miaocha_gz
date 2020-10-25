package com.mc.phonelive.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.views.LiveUserHomeViewHolder;
import com.mc.phonelive.views.foxtone.VideoCommentHomeViewHolder;

/**
 * Created by cxf on 2018/9/25.
 * 个人主页
 */

public class UserHomeActivity extends AbsActivity {

    private LiveUserHomeViewHolder mLiveUserHomeViewHolder;
    private VideoCommentHomeViewHolder mVideoCommentViewHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_empty;
    }

    @Override
    protected void main() {
        String toUid = getIntent().getStringExtra(Constants.TO_UID);
        if (TextUtils.isEmpty(toUid)) {
            return;
        }
        mLiveUserHomeViewHolder = new LiveUserHomeViewHolder(mContext, (ViewGroup) findViewById(R.id.container), toUid);
//        addAllLifeCycleListener(mLiveUserHomeViewHolder.getLifeCycleListenerList());
        mLiveUserHomeViewHolder.addToParent();
        mLiveUserHomeViewHolder.loadData();
    }

    public static void forward(Context context, String toUid) {
        Intent intent = new Intent(context, UserHomeActivity.class);
        intent.putExtra(Constants.TO_UID, toUid);
        context.startActivity(intent);
    }

    public void addImpress(String toUid) {
        Intent intent = new Intent(mContext, LiveAddImpressActivity.class);
        intent.putExtra(Constants.TO_UID, toUid);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (mLiveUserHomeViewHolder != null) {
                mLiveUserHomeViewHolder.refreshImpress();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mLiveUserHomeViewHolder != null) {
            mLiveUserHomeViewHolder.release();
        }
        super.onDestroy();
    }
    /**
     * 显示评论
     */
    public void openCommentWindow(VideoBean videoBean) {
        if (mVideoCommentViewHolder == null) {
            mVideoCommentViewHolder = new VideoCommentHomeViewHolder(mContext, (ViewGroup) findViewById(R.id.rootView));
            mVideoCommentViewHolder.addToParent();
        }
        mVideoCommentViewHolder.setVideoBean(videoBean);
        mVideoCommentViewHolder.showBottom();
    }

    @Override
    protected void onStop() {
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
        super.onStop();
    }
}
