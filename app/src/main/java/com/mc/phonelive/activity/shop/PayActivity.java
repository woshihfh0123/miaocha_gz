package com.mc.phonelive.activity.shop;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.pay.PayResult;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.httpnet.PayCallback;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.PayUtis;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;

/**
 * created by WWL on 2020/4/6 0008:10
 * 支付界面
 */
public class PayActivity extends AbsActivity {
    @BindView(R.id.pay_over_time)
    TextView payOverTime;//支付超时倒计时
    @BindView(R.id.pay_money)
    TextView payMoney;//多少人民币
    @BindView(R.id.pay_cb_1)
    ImageView mPayCb1;
    @BindView(R.id.pay_layout_01)
    LinearLayout pay_layout_01;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.pay_cb_2)
    ImageView mPayCb2;
    @BindView(R.id.pay_layout_02)
    LinearLayout pay_layout_02;
    @BindView(R.id.pay_cb_3)
    ImageView mPayCb3;
    @BindView(R.id.pay_layout_03)
    LinearLayout pay_layout_03;
    @BindView(R.id.pay_cb_0)
    ImageView mPayCb0;
    @BindView(R.id.pay_layout_0)
    LinearLayout pay_layout_0;
    @BindView(R.id.ok_tv)
    TextView okTv;
    @BindView(R.id.tv_wallet_num)
    TextView tvWalletNum;
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    //============================支付方式=========================================
    private String mPayType = "1";     //"2" 微信支付，"1" 支付宝支付
    private String mOrderId = "";
    private String mOrderNo = "";

//    private OrderCrateVO mDataBean;
    private Double mPayMoney=0.00;//支付金额
    private Double mBalance=0.00;//余额
//    private String mOrderType = "";//如果1表示付款   2表示充值
    //倒计时
    CountDownTimer timer;

    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity;
    }

    @Override
    protected void main() {
        titleView.setText("支付");
        setBarModel(true);
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }


        Bundle mBundle = this.getIntent().getExtras();
        if (mBundle != null) {
            String Out_trade_no =mBundle.getString("out_trade_no");
            String order_id =mBundle.getString("order_id");
            String money =mBundle.getString("money");
            if (!DataSafeUtils.isEmpty(Out_trade_no)){
                mOrderNo = Out_trade_no;
            }
            if (!DataSafeUtils.isEmpty(order_id)){
                this.mOrderId = order_id;
            }
            if (!DataSafeUtils.isEmpty(money)){
                mPayMoney = Double.parseDouble(money);
                payMoney.setText("￥"+money+"");
            }
        }
//        tvWalletNum.setText("￥"+AppConfig.getInstance().getUserBean().getCoin());
        selectPayment(true,false,false,false);
    }



    @OnClick({R.id.ok_tv, R.id.pay_layout_0, R.id.pay_layout_01, R.id.pay_layout_02, R.id.pay_layout_03})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ok_tv:
                if (!ButtonUtils.isFastDoubleClick(R.id.ok_tv)) {
                    commit();
                }

                break;

            case R.id.pay_layout_0:
                /**
                 * 余额
                 */
                if (mPayMoney > mBalance) {
                    Toast.makeText(this, "余额不足", Toast.LENGTH_SHORT).show();
                } else {
                    selectPayment(false, false, true, false);
                }
                break;
            case R.id.pay_layout_01:
                /**
                 * 支付宝
                 */
                selectPayment(true, false, false, false);

                break;


            case R.id.pay_layout_02:
                /**
                 * 微信
                 */
                selectPayment(false, true, false, false);
                break;

            case R.id.pay_layout_03:
                /**
                 * 银联
                 */
                selectPayment(false, false, false, true);

                break;
        }
    }


    /**
     * 选择支付方式
     *
     * @param op1
     * @param op2
     */
    private void selectPayment(boolean op1, boolean op2, boolean op3, boolean op4) {


        if (op1) {//支付宝
            mPayType = "1";
            mPayCb1.setImageResource(R.drawable.check_select);
            mPayCb0.setImageResource(R.drawable.check_normal);
            mPayCb2.setImageResource(R.drawable.check_normal);
            mPayCb3.setImageResource(R.drawable.check_normal);
        }


        if (op2) {//微信
            mPayType = "2";
            mPayCb2.setImageResource(R.drawable.check_select);
            mPayCb0.setImageResource(R.drawable.check_normal);
            mPayCb1.setImageResource(R.drawable.check_normal);
            mPayCb3.setImageResource(R.drawable.check_normal);
        }

        if (op3) {//余额
            mPayType = "3";
            mPayCb0.setImageResource(R.drawable.check_select);
            mPayCb1.setImageResource(R.drawable.check_normal);
            mPayCb2.setImageResource(R.drawable.check_normal);
            mPayCb3.setImageResource(R.drawable.check_normal);
        }

        if (op4) {//银联
            mPayType = "4";
            mPayCb3.setImageResource(R.drawable.check_select);
            mPayCb0.setImageResource(R.drawable.check_normal);
            mPayCb1.setImageResource(R.drawable.check_normal);
            mPayCb2.setImageResource(R.drawable.check_normal);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusModel event) {
        Bundle bundle = new Bundle();
        bundle.putString("order_id", mOrderId);
        String code = event.getCode();
        switch (code) {
            case "payment_success":
                //支付成功
                payFinish(1);
                break;
            case "payment_fail":
                //失败
                payFinish(2);
                break;
        }

    }

    public void commit() {

        if ("1".equals(mPayType)) {
            /**
             * 支付宝支付
             */
            pay(mOrderNo);

        } else if ("2".equals(mPayType)) {
            /**
             * 微信支付
             */
            if (CommentUtil.isWeixinAvilible(this)) {
                pay(mOrderNo);
            } else {
                Toast.makeText(this, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                return;
            }

        } else if ("3".equals(mPayType)) {
            /**
             * 余额支付
             */
            pay(mOrderNo);

        } else if ("4".equals(mPayType)) {
            /**
             * 银联支付
             */
            pay(mOrderNo);

        }

    }


    public void pay(String mOrderNo) {
        HttpUtil.OrderGetPay(mOrderNo, mPayType, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (info.length>0){
                    if ("2".equals(mPayType)) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        String json = obj.getString("payOrder");
                        Gson gson = new Gson();
                        final PayResult vo = gson.fromJson(json, PayResult.class);
                        PayUtis.weiXinPay(PayActivity.this, vo);
                    } else if (mPayType.equals("1")) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        String payInfo = obj.getString("payOrder");
                        PayUtis.zhiFuBaoPay(PayActivity.this, payInfo, new PayCallback() {
                            @Override
                            public void payResult(int type) {
                                payFinish(type);
                            }
                        });
                    }
                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
        });

    }


    public void payFinish(int type) {
        if (type == 1) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.pay_result_dialog_layout, null);

            final Dialog dialog = new Dialog(PayActivity.this, R.style.MyRedPackageDialog3);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            Window dialogWindow = dialog.getWindow();
            WindowManager m = this.getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            p.height = (int) (d.getHeight() * 0.45); // 高度设置为屏幕的0.6
            p.width = (int) (d.getWidth() * 0.66); // 宽度设置为屏幕的0.65
            dialogWindow.setAttributes(p);

            dialog.show();
            CountdownView cuntdownView = view.findViewById(R.id.cuntdown_view);
            TextView payresulttv = view.findViewById(R.id.pay_result_tv);
                    payresulttv.setText("购买成功");
            cuntdownView.start(2000);
            cuntdownView.setOnCountdownIntervalListener(10, new CountdownView.OnCountdownIntervalListener() {
                @Override
                public void onInterval(CountdownView cv, long remainTime) {
                    int time = (int) (remainTime / 1000) + 1;
                }
            });
            cuntdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    inntentActivity();
                    dialog.dismiss();
                    dialog.cancel();
                }
            });
        } else {
            inntentActivity();
        }
    }

    /**
     * 是否支付成功
     *
     */
    private void inntentActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("order_id", mOrderId);
        Intent intent = new Intent(PayActivity.this, MyOrderDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        PayActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Message();
            return true;
        }
        return true;
    }


    /**
     * 离开弹框
     */
    private void Message() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.live_payback_dialog, null);

        final Dialog dialog = new Dialog(this, R.style.MyRedPackageDialog3);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.35); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.80); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
        dialog.show();
        TextView dialogtitle = view.findViewById(R.id.dialogtitle);
        TextView showmsg = view.findViewById(R.id.showmsg);
        TextView confim = view.findViewById(R.id.confirm);
        final TextView cancel = view.findViewById(R.id.cancel);
        confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                inntentActivity();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                dialog.dismiss();
            }
        });

    }


    /**
     * 倒数计时器
     */
    private void TimeStart(final int mTimes) {
        /****
         * 倒计时方法如下---------------------------------------------
         */
        timer = new CountDownTimer(mTimes * 1000, 1000) {

            /**
             * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
             * @param millisUntilFinished
             */
            @Override
            public void onTick(long millisUntilFinished) {
                String mTime = formatTime(millisUntilFinished);
                payOverTime.setText(mTime);
            }

            /**
             * 倒计时完成时被调用
             */
            @Override
            public void onFinish() {
                Log.v("logger", "倒计时完毕了");
                payOverTime.setText("00:00");

                final Bundle bundle = new Bundle();
                bundle.putString("order_id", mOrderId);
//                if (mType.equals("0")) {
//                    open(MyBalanceActivity.class, bundle, 0);
//                } else if (mType.equals("1")) {
//                open(MyOrderDetailActivity.class, bundle, 0);
//                }
                Intent intent = new Intent(PayActivity.this, MyOrderDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                PayActivity.this.finish();

            }
        };
        timer.start();
    }

    /**
     * 将毫秒转化为 分钟：秒 的格式
     *
     * @param millisecond 毫秒
     * @return
     */
    public String formatTime(long millisecond) {
        int minute;//分钟
        int second;//秒数
        minute = (int) ((millisecond / 1000) / 60);
        second = (int) ((millisecond / 1000) % 60);
        if (minute < 10) {
            if (second < 10) {
                return "0" + minute + ":" + "0" + second;
            } else {
                return "0" + minute + ":" + second;
            }
        } else {
            if (second < 10) {
                return minute + ":" + "0" + second;
            } else {
                return minute + ":" + second;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered) {
            EventBus.getDefault().unregister(this);
        }
        try {
            if (timer != null) {
                timer.cancel();
                timer.onFinish();
            }
        } catch (Exception e) {
            Log.i("logger", e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        Message();
    }
}

