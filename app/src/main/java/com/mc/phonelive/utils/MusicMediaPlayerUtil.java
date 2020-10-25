package com.mc.phonelive.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import com.mc.phonelive.AppConfig;

/**
 * Created by cxf on 2018/7/19.
 */

public class MusicMediaPlayerUtil {

    private MediaPlayer mPlayer;
    private boolean mStarted;
    private boolean mPaused;
    private boolean mDestroy;
    private String mCurPath;

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mDestroy) {
                destroy();
            } else {
                if(AppConfig.getInstance().isVideoView){
                    mPlayer.start();
                    mStarted = true;
                }
            }
        }
    };

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mStarted = false;
            mCurPath = null;
        }
    };

    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mStarted = false;
            mCurPath = null;
            return false;
        }
    };


    public MusicMediaPlayerUtil() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(mOnPreparedListener);
        mPlayer.setOnErrorListener(mOnErrorListener);
        mPlayer.setOnCompletionListener(mOnCompletionListener);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void startPlay(String path) {
        Log.e("yyyyy:",path);

        if(AppConfig.getInstance().isVideoView){
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (!mStarted) {
            mCurPath = path;
            try {
                mPlayer.reset();
                mPlayer.setDataSource(path);
                mPlayer.setLooping(true);
                mPlayer.setVolume(1f, 1f);
                mPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (!path.equals(mCurPath)) {
                mCurPath = path;
                mStarted = false;
                try {
                    mPlayer.stop();
                    mPlayer.reset();
                    mPlayer.setDataSource(path);
                    mPlayer.setLooping(true);
                    mPlayer.setVolume(1f, 1f);
                    mPlayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        }
    }

    public void pausePlay() {
        if (mStarted && !mDestroy) {
            if (mPlayer != null) {
                mPlayer.pause();
            }
        }
        mPaused = true;
    }

    public void resumePlay() {
        Log.e("yyyyyy:","llllll");
        if(AppConfig.getInstance().isVideoView){


        if (mStarted && !mDestroy && mPaused) {
            if (mPlayer != null) {
                mPlayer.start();
            }
        }
        mPaused = false;
        }
    }

    public void stopPlay() {
        if (mStarted && !mDestroy) {
            if (mPlayer != null) {
                mPlayer.stop();
            }
            mCurPath = null;
        }
        mStarted = false;
    }

    public void destroy() {
        if (mStarted) {
            mPlayer.stop();
        }
        if (mPlayer != null) {
            mPlayer.release();
        }
        mStarted = false;
        mCurPath = null;
        mDestroy = true;
    }
}
