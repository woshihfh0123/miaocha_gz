//package com.bs.xyplibs.utils;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.bs.xyplibs.bean.WXpayOrderBean;
//import com.bs.xyplibs.callback.MyPayCallBack;
//
//import java.util.Map;/**/
//
///**
// * Created by Administrator on 2018/4/4.
// */
//
//public class PayUtils {
//    private static final int SDK_PAY_FLAG = 1;
//    private static MyPayCallBack mPayCallback;
//    public  static void AliPayUtil(final Activity activity, final String payInfo, MyPayCallBack payCallBack){
//        mPayCallback=payCallBack;
//        Runnable payRunnable = new Runnable() {
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(activity);
//                Map<String, String> result = alipay.payV2(payInfo, true);
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//        // 必须异步调用
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//    }
//
//
//    static Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            @SuppressWarnings("unchecked")
//            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
//            Log.i("支付结果", "handleMessage: "+payResult.toString());
//
//            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//            String resultStatus = payResult.getResultStatus();
//            // 判断resultStatus 为9000则代表支付成功
//            if (TextUtils.equals(resultStatus, "9000")) {
//                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知
//                mPayCallback.payResult(1);
//            } else {
//                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                Log.i("支付结果状态", "handleMessage: "+resultStatus);
//                Log.i("支付结果info", "handleMessage: "+resultInfo+"-----memo:"+payResult.getMemo());
//
//                mPayCallback.payResult(0);
//            }
//        }
//    };
//    public static void weiXinPay(Context context, WXpayOrderBean.DataBean.OrderBean payResult) {
//        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
//        msgApi.registerApp(payResult.getAppid());
//        PayReq request = new PayReq();
//        request.appId = payResult.getAppid();
//        request.partnerId = payResult.getPartnerid();
//        request.prepayId = payResult.getPrepayid();
//        request.packageValue = payResult.getPkg();
//        request.nonceStr = payResult.getNoncestr();
//        request.timeStamp = payResult.getTimestamp();
//        request.sign = payResult.getSign();
//        msgApi.sendReq(request);
//
//    }
//
//}
