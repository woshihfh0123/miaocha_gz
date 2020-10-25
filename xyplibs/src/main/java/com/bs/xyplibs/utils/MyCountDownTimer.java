package com.bs.xyplibs.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

import com.bs.xyplibs.R;


/**
 * Created by Administrator on 2017/12/9.
 */

public class MyCountDownTimer extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private Activity mActivity;
    private Button btn;//按钮


    public MyCountDownTimer( Activity mActivity,long millisInFuture, long countDownInterval,Button btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn =btn;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        btn.setText(millisUntilFinished / 1000 + "S");//设置倒计时时间
        btn.setTextSize(14);
        btn.setBackground(mActivity.getResources().getDrawable(R.drawable.ease_btn_yzm_press_shape));

        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
        span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
        btn.setText(span);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onFinish() {
        btn.setText("重新发送");
        btn.setClickable(true);//重新获得点击
        btn.setTextSize(14);
        btn.setBackground(mActivity.getResources().getDrawable(R.drawable.min_bt_shape));//还原背景色
    }
}
