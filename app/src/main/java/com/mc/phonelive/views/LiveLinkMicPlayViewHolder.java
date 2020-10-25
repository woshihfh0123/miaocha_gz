package com.mc.phonelive.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.mc.phonelive.R;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

import java.io.IOException;

/**
 * Created by cxf on 2018/10/25.
 * 连麦播放小窗口
 */

public class LiveLinkMicPlayViewHolder extends AbsViewHolder implements
        IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener {

    private static final String TAG = "LiveLinkMicPlayViewHolder";
    private KSYTextureView mVideoView;
    private View mBtnClose;
    private View mLoading;
    private boolean mPaused;//是否切后台了
    private boolean mStartPlay;//是否开始了播放
    private boolean mEndPlay;//是否结束了播放

    public LiveLinkMicPlayViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_link_mic_play;
    }

    @Override
    public void init() {
        mVideoView = (KSYTextureView) findViewById(R.id.video_view);
        mLoading = findViewById(R.id.loading);
        mBtnClose = findViewById(R.id.btn_close_link_mic);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setTimeout(5000, 5000);
        mVideoView.setVolume(2f, 2f);
        mVideoView.setLooping(true);//循环播放
        mVideoView.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
        mVideoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
    }

    public void setOnCloseListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            mBtnClose.setVisibility(View.VISIBLE);
            mBtnClose.setOnClickListener(onClickListener);
        }
    }

    /**
     * 开始播放
     *
     * @param url 流地址
     */
    public void play(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mEndPlay = false;
        if (mStartPlay) {
            mVideoView.reload(url, true, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_FAST);
        }
        try {
            mVideoView.setDataSource(url);
            mVideoView.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        L.e(TAG, "play----url--->" + url);
    }

    public void stop() {
        mVideoView.stop();
        mVideoView.reset();
        mStartPlay = false;
    }


    public void release() {
        mEndPlay = true;
        mVideoView.stop();
        mVideoView.reset();
        mVideoView.release();
        mVideoView.setOnPreparedListener(null);
        mVideoView.setOnErrorListener(null);
        mVideoView.setOnInfoListener(null);
        mStartPlay = false;
        if (mBtnClose != null) {
            mBtnClose.setOnClickListener(null);
        }
        L.e(TAG, "release------->");
    }


    @Override
    public void onPrepared(IMediaPlayer mp) {
        if (mEndPlay) {
            release();
            return;
        }
        mStartPlay = true;
        int width = mp.getVideoWidth();
        int height = mp.getVideoHeight();
        L.e(TAG, "流---width----->" + width);
        L.e(TAG, "流---height----->" + height);
        if (width >= height) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
            float rate = ((float) width) / height;
            params.height = (int) (mVideoView.getWidth() / rate);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mVideoView.requestLayout();
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
//        switch (what) {
//            //网络较差播放过程中触发设置超时时间，报错退出
//            case -10011://播放http+mp4点播流，弱网环境
//            case -10002://播放http(mp4/flv/hls)点播流，无效wifi环境,连接无效wifi，播放rtmp直播流
//            case -10004://播放rtmp直播流，弱网环境
//            case -1004://播播http+flv点播流，播放过程中断网
//            case -10007:
//            case -10008://播放无效的http地址,超时设置足够长
//            case 1:
//                break;
//        }
        ToastUtil.show(WordUtil.getString(R.string.live_play_error));
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int info, int extra) {
        switch (info) {
            case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START://缓冲开始
                if (mLoading != null && mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
                break;
            case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END://缓冲结束
                if (mLoading != null && mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
                break;
        }
        return false;
    }

    public void resume() {
        if (mPaused && mVideoView != null) {
            mVideoView.runInForeground();
            mVideoView.start();
        }
        mPaused = false;
    }

    public void pause() {
        if (mVideoView != null) {
            mVideoView.runInBackground(false);
        }
        mPaused = true;
    }


}
