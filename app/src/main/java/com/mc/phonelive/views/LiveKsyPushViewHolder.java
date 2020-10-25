package com.mc.phonelive.views;

import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.view.ViewGroup;

import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyProFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautySpecialEffectsFilter;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.logstats.StatsLogReport;
import com.mc.beauty.bean.FilterBean;
import com.mc.phonelive.LiveConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.beauty.DefaultEffectListener;
import com.mc.phonelive.beauty.TiFilter;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.utils.ToastUtil;

/**
 * Created by cxf on 2018/10/7.
 * 金山云直播推流
 */

public class LiveKsyPushViewHolder extends AbsLivePushViewHolder implements
        KSYStreamer.OnInfoListener, KSYStreamer.OnErrorListener, StatsLogReport.OnLogEventListener {

    private KSYStreamer mStreamer;//金山推流器
    private ImgBeautyProFilter mImgBeautyProFilter;//金山自带美颜
    private float mMeiBaiVal;//基础美颜 美白
    private float mMoPiVal;//基础美颜 磨皮
    private float mHongRunVal;//基础美颜 红润


    public LiveKsyPushViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_push_ksy;
    }

    @Override
    public void init() {
        super.init();
        mPreView = findViewById(R.id.camera_preview);
        mStreamer = new KSYStreamer(mContext);
        mStreamer.setPreviewFps(LiveConfig.PUSH_FRAME_RATE);//预览采集帧率
        mStreamer.setTargetFps(LiveConfig.PUSH_FRAME_RATE);//推流采集帧率
        mStreamer.setVideoKBitrate(LiveConfig.PUSH_VIDEO_BITRATE * 3 / 4, LiveConfig.PUSH_VIDEO_BITRATE, LiveConfig.PUSH_VIDEO_BITRATE / 4);//视频码率
        mStreamer.setAudioKBitrate(LiveConfig.PUSH_AUDIO_BITRATE);//音频码率
        mStreamer.setCameraCaptureResolution(LiveConfig.PUSH_CAP_RESOLUTION);//采集分辨率
        mStreamer.setPreviewResolution(LiveConfig.PUSH_PREVIEW_RESOLUTION);//预览分辨率
        mStreamer.setTargetResolution(LiveConfig.PUSH_VIDEO_RESOLUTION);//推流分辨率
        mStreamer.setVideoCodecId(LiveConfig.PUSH_ENCODE_TYPE);//H264
        mStreamer.setEncodeMethod(LiveConfig.PUSH_ENCODE_METHOD);//软编
        mStreamer.setVideoEncodeScene(LiveConfig.PUSH_ENCODE_SCENE);//秀场模式
        mStreamer.setVideoEncodeProfile(LiveConfig.PUSH_ENCODE_PROFILE);
        mStreamer.setAudioChannels(2);//双声道推流
        mStreamer.setVoiceVolume(2f);
        mStreamer.setEnableAudioMix(true);//设置背景音乐可用
        mStreamer.getAudioPlayerCapture().setVolume(0.5f);//设置背景音乐音量
        mStreamer.setEnableRepeatLastFrame(false);  // 切后台的时候不使用最后一帧
        mStreamer.setEnableAutoRestart(true, 3000); // 自动重启推流
        mStreamer.setCameraFacing(CameraCapture.FACING_FRONT);
        mStreamer.setFrontCameraMirror(true);
        mStreamer.setOnInfoListener(this);
        mStreamer.setOnErrorListener(this);
        mStreamer.setOnLogEventListener(this);
        if (mStreamer != null && mTiSDKManager != null) {
            mStreamer.getImgTexFilterMgt().setFilter(new TiFilter(mTiSDKManager, mStreamer.getGLRender()));
        }
        mStreamer.setDisplayPreview((GLSurfaceView) mPreView);
        mStreamer.startCameraPreview();//启动预览

    }


    public DefaultEffectListener getDefaultEffectListener() {
        return new DefaultEffectListener() {

            @Override
            public void onFilterChanged(FilterBean bean) {
                if (bean == null) {
                    return;
                }
                int type = bean.getKsyFilterType();
                if (type == 0) {
                    if (mImgBeautyProFilter == null) {
                        initBaseBeauty();
                    }
                } else {
                    if (mStreamer != null) {
                        ImgBeautySpecialEffectsFilter filter = new ImgBeautySpecialEffectsFilter(
                                mStreamer.getGLRender(), mContext, type);
                        mStreamer.getImgTexFilterMgt().setFilter(filter);
                        mImgBeautyProFilter = null;
                    }
                }
            }

            @Override
            public void onMeiBaiChanged(int progress) {
                if (mImgBeautyProFilter == null) {
                    initBaseBeauty();
                }
                if (mImgBeautyProFilter != null && mImgBeautyProFilter.isWhitenRatioSupported()) {
                    mMeiBaiVal = progress / 100f;
                    mImgBeautyProFilter.setWhitenRatio(mMeiBaiVal);
                }
            }

            @Override
            public void onMoPiChanged(int progress) {
                if (mImgBeautyProFilter == null) {
                    initBaseBeauty();
                }
                if (mImgBeautyProFilter != null && mImgBeautyProFilter.isGrindRatioSupported()) {
                    mMoPiVal = progress / 100f;
                    mImgBeautyProFilter.setGrindRatio(mMoPiVal);
                }
            }

            @Override
            public void onHongRunChanged(int progress) {
                if (mImgBeautyProFilter == null) {
                    initBaseBeauty();
                }
                if (mImgBeautyProFilter != null && mImgBeautyProFilter.isRuddyRatioSupported()) {
                    mHongRunVal = progress / 100f;
                    mImgBeautyProFilter.setRuddyRatio(mHongRunVal);
                }
            }
        };
    }


    /**
     * 初始化基础美颜
     */
    private void initBaseBeauty() {
        if (mStreamer != null) {
            ImgBeautyProFilter filter = new ImgBeautyProFilter(mStreamer.getGLRender(), mContext);
            if (filter.isWhitenRatioSupported()) {
                filter.setWhitenRatio(mMeiBaiVal);
            }
            if (filter.isGrindRatioSupported()) {
                filter.setGrindRatio(mMoPiVal);
            }
            if (filter.isRuddyRatioSupported()) {
                filter.setRuddyRatio(mHongRunVal);
            }
            mStreamer.getImgTexFilterMgt().setFilter(filter);
            mImgBeautyProFilter = filter;
        }
    }


    @Override
    public void onInfo(int what, int msg1, int msg2) {
        switch (what) {
            case 1000://初始化完毕
                L.e(TAG, "mStearm--->初始化完毕");
                if (mLivePushListener != null) {
                    mLivePushListener.onPreviewStart();
                }
                break;
            case 0://推流成功
                L.e(TAG, "mStearm--->推流成功");
                if (!mStartPush) {
                    mStartPush = true;
                    if (mLivePushListener != null) {
                        mLivePushListener.onPushStart();
                    }
                }
                break;
        }
    }

    @Override
    public void onError(int what, int msg1, int msg2) {
        boolean needStopPushStream = false;//是否需要停止推流
        switch (what) {
            case -1009://推流url域名解析失败
                L.e(TAG, "mStearm--->推流url域名解析失败");
                break;
            case -1006://网络连接失败，无法建立连接
                L.e(TAG, "mStearm--->网络连接失败，无法建立连接");
                break;
            case -1010://跟RTMP服务器完成握手后,推流失败
                L.e(TAG, "mStearm--->跟RTMP服务器完成握手后,推流失败");
                break;
            case -1007://网络连接断开
                L.e(TAG, "mStearm--->网络连接断开");
                break;
            case -2004://音视频采集pts差值超过5s
                L.e(TAG, "mStearm--->音视频采集pts差值超过5s");
                break;
            case -1004://编码器初始化失败
                L.e(TAG, "mStearm--->编码器初始化失败");
                needStopPushStream = true;
                break;
            case -1003://视频编码失败
                L.e(TAG, "mStearm--->视频编码失败");
                needStopPushStream = true;
                break;
            case -1008://音频初始化失败
                L.e(TAG, "mStearm--->音频初始化失败");
                needStopPushStream = true;
                break;
            case -1011://音频编码失败
                L.e(TAG, "mStearm--->音频编码失败");
                needStopPushStream = true;
                break;
            case -2001: //摄像头未知错误
                L.e(TAG, "mStearm--->摄像头未知错误");
                needStopPushStream = true;
                break;
            case -2002://打开摄像头失败
                L.e(TAG, "mStearm--->打开摄像头失败");
                needStopPushStream = true;
                break;
            case -2003://录音开启失败
                L.e(TAG, "mStearm--->录音开启失败");
                needStopPushStream = true;
                break;
            case -2005://录音开启未知错误
                L.e(TAG, "mStearm--->录音开启未知错误");
                needStopPushStream = true;
                break;
            case -2006://系统Camera服务进程退出
                L.e(TAG, "mStearm--->系统Camera服务进程退出");
                needStopPushStream = true;
                break;
            case -2007://Camera服务异常退出
                L.e(TAG, "mStearm--->Camera服务异常退出");
                needStopPushStream = true;
                break;
        }
        if (mStreamer != null) {
            if (needStopPushStream) {
                mStreamer.stopCameraPreview();
            }
            if (mStartPush && mLivePushListener != null) {
                mLivePushListener.onPushFailed();
            }
        }
    }

    @Override
    public void onLogEvent(StringBuilder singleLogContent) {
        //打印推流信息
        //L.e("mStearm--->" + singleLogContent.toString());
    }


    /**
     * 切换镜头
     */
    @Override
    public void toggleCamera() {
        if (mStreamer != null) {
            if (mFlashOpen) {
                toggleFlash();
            }
            mStreamer.switchCamera();
            mCameraFront = !mCameraFront;
        }
    }

    /**
     * 打开关闭闪光灯
     */
    @Override
    public void toggleFlash() {
        if (mCameraFront) {
            ToastUtil.show(R.string.live_open_flash);
            return;
        }
        if (mStreamer != null) {
            CameraCapture capture = mStreamer.getCameraCapture();
            Camera.Parameters parameters = capture.getCameraParameters();
            if (parameters == null) {
                if (!mFlashOpen) {
                    ToastUtil.show(R.string.live_open_flash_error);
                }
            } else {
                if (Camera.Parameters.FLASH_MODE_TORCH.equals(parameters.getFlashMode())) {//如果闪光灯已开启
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//设置成关闭的
                    mFlashOpen = false;
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//设置成开启的
                    mFlashOpen = true;
                }
                capture.setCameraParameters(parameters);
            }

        }
    }

    /**
     * 开始推流
     *
     * @param pushUrl 推流地址
     */
    @Override
    public void startPush(String pushUrl) {
        if (mStreamer != null) {
            mStreamer.setUrl(pushUrl);
            mStreamer.startStream();
        }
        startCountDown();
    }


    @Override
    public void onPause() {
        if (mStreamer != null) {
            mStreamer.onPause();
            // 切后台时，将SDK设置为离屏推流模式，继续采集camera数据
            mStreamer.setOffscreenPreview(mStreamer.getPreviewWidth(), mStreamer.getPreviewHeight());
        }
    }

    @Override
    public void onResume() {
        if (mStreamer != null) {
            mStreamer.onResume();
        }
    }

    @Override
    public void startBgm(String path) {
        if (mStreamer != null) {
            mStreamer.startBgm(path, true);
        }
    }

    @Override
    public void pauseBgm() {
        if (mStreamer != null) {
            mStreamer.getAudioPlayerCapture().getMediaPlayer().pause();
        }
    }

    @Override
    public void resumeBgm() {
        if (mStreamer != null) {
            mStreamer.getAudioPlayerCapture().getMediaPlayer().start();
        }
    }

    @Override
    public void stopBgm() {
        if (mStreamer != null) {
            mStreamer.stopBgm();
        }
    }

    @Override
    protected void onCameraRestart() {
        if (mStreamer != null) {
            mStreamer.startCameraPreview();//启动预览
        }
    }

    @Override
    public void onRelease() {
        if (mStreamer != null) {
            mStreamer.stopStream();
            mStreamer.stopCameraPreview();
            mStreamer.release();
            mStreamer.setOnInfoListener(null);
            mStreamer.setOnErrorListener(null);
            mStreamer.setOnLogEventListener(null);
        }
        mStreamer = null;
    }
}
