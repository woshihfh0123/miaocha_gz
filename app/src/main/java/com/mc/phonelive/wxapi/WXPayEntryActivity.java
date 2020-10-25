package com.mc.phonelive.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.pay.wx.WxApiWrapper;

import org.greenrobot.eventbus.EventBus;


/**
 * 微信支付的回调页面
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//wx7362e75636eaf966
            WxApiWrapper.getInstance().setAppID("wxfd300020414fcd39");
        api = WxApiWrapper.getInstance().getWxApi();
        api.handleIntent(getIntent(), this);

//        api = WXAPIFactory.createWXAPI(this, "wxab29b24330fc0289");
//        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
//        EventBus.getDefault().post(resp);
        switch (resp.errCode){

            case 0:
                Log.e("logger", "" + "支付成功");
                EventBus.getDefault().post(new EventBusModel("payment_success", "wechatPay"));
                finish();

                break;

            case -1:
                EventBus.getDefault().post(new EventBusModel("payment_fail", "wechatPay"));
                Log.e("logger", "" + "支付失败");
                //                Toast.makeText(this,"支付失败", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);

                break;

            case -2:
//                CommentUtil.showSingleToast("用户取消");
                break;

        }
        finish();
    }

}