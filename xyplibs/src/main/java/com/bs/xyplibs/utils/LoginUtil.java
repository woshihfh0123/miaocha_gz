package com.bs.xyplibs.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bs.xyplibs.base.BaseApp;
import com.bs.xyplibs.bean.EventBean;
import com.lzy.okgo.utils.HttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * Created by admin on 2017/12/22.
 */

public class LoginUtil {


    private final int SMS_CODE_TIME=60;
    private final int DH_DJS=3;
    private  int djs=DH_DJS;
    private  int second=SMS_CODE_TIME;
    private Timer timer;
    private static TextView textView;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    int time= (int) msg.obj;
                    textView.setText(time+"秒后重发");
                    if (time==0){
                        timer.cancel();
                        second=SMS_CODE_TIME;
                        textView.setEnabled(true);
                        textView.setAlpha(1.0f);
                        textView.setText("重新获取");
                        //                        textView.setTextColor(App.getInstance().getResources().getColor(R.color.mmCircle_pink));
                        //textView.setBackgroundColor(getResources().getColor(R.color.color_login_checkcode));
                    }
                    break;

                case 1:
                    int time1= (int) msg.obj;
                    textView.setText("跳过("+time1+"s)");//跳过(3s)
                    if (time1==0){
                        timer.cancel();
                        djs=DH_DJS;
                        textView.setEnabled(true);
                        textView.setAlpha(1.0f);
                        EventBusUtil.postEvent(new EventBean("go_from_djs"));
                        textView.setText("");
                        textView.setVisibility(View.INVISIBLE);
                        //                        textView.setTextColor(App.getInstance().getResources().getColor(R.color.mmCircle_pink));
                        //textView.setBackgroundColor(getResources().getColor(R.color.color_login_checkcode));
                    }
                    break;
                    //CommentUtil.showSingleToast(LoginActivity.this,"验证码错误，登录失败");
            }
        }
    };











    /**
     * 检查手机号
     * @param phone 手机号
     * @return  是否合格
     */
    public static boolean checkPhone(String phone){

        if (Utils.isEmpty(phone)){
            CommentUtil.showSingleToast("手机号不能为空");
            return false;
        }else if (phone.length()!=11){
            CommentUtil.showSingleToast("您的手机号不合法");
            return false;
        }else {
            return true;
        }


    }


    /**
     * 检查密码
     * @param password 密码
     * @return  是否合格
     */
    public static boolean checkPassword(String password){

        if (TextUtils.isEmpty(password)){

            CommentUtil.showSingleToast("密码不能为空");
            return false;

        }else {
            return true;
        }


    }


    /**
     * 检查短信验证码
     * @param smsCode
     * @return
     */
    public static boolean checkSmsCode(String smsCode){

        if (TextUtils.isEmpty(smsCode)){

            CommentUtil.showSingleToast("短信验证码不能为空");
            return false;

        }else {
            return true;
        }


    }


    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str)throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }


    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 开始倒计时
     * @param textView
     */
    public void startDaoJiShi(TextView textView) {
        this.textView=textView;

        textView.setEnabled(false);
        textView.setAlpha(0.7f);
        textView.setText(second+"秒后重发");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 0;
                message.obj = --second;
                handler.sendMessage(message);
            }
        }, 1000, 1000);


    }
    /**
     * 开始倒计时
     * @param textView
     */
    public void startDhDaoJiShi(TextView textView) {
        this.textView=textView;

        textView.setEnabled(false);
        textView.setAlpha(0.7f);
        textView.setText("跳过("+djs+"s)");//跳过(3s)

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = --djs;
                handler.sendMessage(message);
            }
        }, 1000, 1000);


    }


    /**
     * 获取定时器对象
     * @return
     */
    public void  cancelDaojishi(){
        if (timer!=null) {
            timer.cancel();
            this.second=SMS_CODE_TIME;
        }
    }



}
