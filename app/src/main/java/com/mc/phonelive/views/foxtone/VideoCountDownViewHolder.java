package com.mc.phonelive.views.foxtone;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.views.AbsViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by cxf on 2018/11/26.
 * 视频倒计时
 */

public class VideoCountDownViewHolder extends AbsViewHolder {

    private CountDownView video_countdown;
    private boolean isStart = true;//倒计时是否在开始

    public VideoCountDownViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected void processArguments(Object... args) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_countdown;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);

        initCountDownTime();
    }


    private void initCountDownTime() {
        video_countdown = (CountDownView) findViewById(R.id.video_countdown);//倒计时控件
        video_countdown.setCountdownTime(Constants.COOUNTDOWMTIME);//设置倒计时时间
        setCircleProgress();
    }


    private void setCircleProgress() {
        video_countdown.startCountDown();
        video_countdown.setAddCountDownListener(new CountDownView.OnCountDownFinishListener() {
            @Override
            public void countDownFinished() {
                Log.v("tags", "倒计时时长：" + video_countdown.getCurrentProgress());
                AddTaskHttp();
                if (isStart)
                    video_countdown.startCountDown();
                else
                    video_countdown.puaseCountDown();
            }
        });
    }

    /**
     * 倒计时完成，调用任务接口
     */
    private void AddTaskHttp() {
        HttpUtil.getAddTask(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
            }
        });
    }


    /**
     * 通过生命周期 判断视频是否播放或者暂停
     *
     * @param e
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoTypeEvent(EventBusModel e) {
        Log.v("tags", e.getCode() + "------------------code----22");
        String code = e.getCode();
        switch (code) {
            case "main_video_puase":
//            case "pause_video_videoplay":
                isStart = false;
                if (video_countdown != null) {
                    video_countdown.puaseCountDown();//倒计时暂停
                }
                break;
            case "main_video_play":
                isStart = true;
                if (video_countdown != null)
                    video_countdown.startCountDown();//倒计时重启
                break;
//            case "main_video_play1":
//                isStart = true;
//                if (video_countdown != null)
//                    video_countdown.reStartCountDown();//倒计时重启
//                break;
        }
    }


}
