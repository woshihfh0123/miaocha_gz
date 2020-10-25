package com.mc.phonelive.activity.shop;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.DianPuMyProfitActivity;
import com.mc.phonelive.activity.MyProfitActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.adapter.GvCheckOneItAdapter;
import com.mc.phonelive.bean.CoinBean;
import com.mc.phonelive.bean.QbBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.pay.PayCallback;
import com.mc.phonelive.pay.ali.AliPayBuilder;
import com.mc.phonelive.pay.wx.WxPayBuilder;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.SpUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;
import com.mc.phonelive.views.InputTsDialog;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 *钱包
 * 新888888
 *
 */

public class MoneyPActivity extends AbsActivity {
    private TextView mTitleView;
    private ImageView mBtn_back;
    private TextView mTv_czmx;
    private TextView mTv_cz;
    private android.support.v7.widget.RecyclerView mRv_gv;
    private TextView mTv_zbsy;
    private TextView mTv_ktcps;
    private TextView mTv_tx_top;
    private TextView mTv_dpsy;
    private TextView mTv_ktxje;
    private TextView mTv_tx_btm;
    private GvCheckOneItAdapter mAdapter;
    private SparseArray<String> mSparseArray;
    private String mAliPartner;// 支付宝商户ID
    private String mAliSellerId; // 支付宝商户收款账号
    private String mAliPrivateKey; // 支付宝商户私钥，pkcs8格式
    private String mWxAppID;//微信AppID
    private String mCoinName;
    private CoinBean mCheckedCoinBean;
    private TextView tv_xy;
    private String xieyiUrl="";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_money_p;
    }
    @Override
    protected void main() {
        SpUtil.getInstance().setBooleanValue("setMe",false);
        setTitle("钱包");
        mCoinName = AppConfig.getInstance().getCoinName();
        mTitleView = (TextView) findViewById(R.id.titleView);
        tv_xy = (TextView) findViewById(R.id.tv_xy);
        mBtn_back = (ImageView) findViewById(R.id.btn_back);
        mTv_czmx = (TextView) findViewById(R.id.tv_czmx);
        mTv_cz = (TextView) findViewById(R.id.tv_cz);
        mRv_gv = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_gv);
        mTv_zbsy = (TextView) findViewById(R.id.tv_zbsy);
        mTv_ktcps = (TextView) findViewById(R.id.tv_ktcps);
        mTv_tx_top = (TextView) findViewById(R.id.tv_tx_top);
        mTv_dpsy = (TextView) findViewById(R.id.tv_dpsy);
        mTv_ktxje = (TextView) findViewById(R.id.tv_ktxje);
        mTv_tx_btm = (TextView) findViewById(R.id.tv_tx_btm);
        mSparseArray = new SparseArray<>();
        mAdapter=new GvCheckOneItAdapter();
        mRv_gv.setLayoutManager(Utils.getGvManager(mContext,3));
        mRv_gv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if(mAdapter.getData().get(position).getId().equals("999")){
                    new InputTsDialog(mContext,"输入充值金额","请输入金额",true,true,"send_diy_money_cz", InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL).show();
                }else{
                    mCheckedCoinBean = mAdapter.getData().get(position);
                mAdapter.setSeclection(position);
                mAdapter.notifyDataSetChanged();
                if (mCheckedCoinBean!=null) {
                    if (mSparseArray == null || mSparseArray.size() == 0) {
                        ToastUtil.show(Constants.PAY_ALL_NOT_ENABLE);
                        return;
                    }
                    DialogUitl.showStringArrayDialog(mContext, mSparseArray, mArrayDialogCallback);
                }else{
                    ToastUtil.show("请选择充值金额");
                }
                }
            }
        });
        mTv_czmx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(mContext,ChongZhiListActivity.class));
            }
        });
        getList();
        mTv_tx_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MyProfitActivity.class);
//                intent.putExtra("left",mTv_zbsy.getText().toString());
//                intent.putExtra("right",mTv_ktcps.getText().toString());
                startActivity(intent);
            }
        });
        mTv_tx_btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DianPuMyProfitActivity.class));
            }
        });
        tv_xy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.forward(mContext,xieyiUrl);
            }
        });
    }//send_diy_money_cz

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    @Subscribe
    public void getEvent(EventBean event){
        switch(event.getCode()){
            case "send_diy_money_cz":
                String firstObj = (String)event.getFirstObject();
                mCheckedCoinBean = new CoinBean();
                mCheckedCoinBean.setId("999");
                mCheckedCoinBean.setCoin(firstObj);
                mCheckedCoinBean.setMoney(firstObj);
//                mCheckedCoinBean = mAdapter.getData().get(position);
//                mAdapter.setSeclection(position);
//                mAdapter.notifyDataSetChanged();
                if(Utils.isNotEmpty(firstObj)){
                if (mCheckedCoinBean!=null) {
                    if (mSparseArray == null || mSparseArray.size() == 0) {
                        ToastUtil.show(Constants.PAY_ALL_NOT_ENABLE);
                        return;
                    }
                    DialogUitl.showStringArrayDialog(mContext, mSparseArray, mArrayDialogCallback);
                }else{
                    ToastUtil.show("请选择充值金额");
                }
                }
                break;
            default:

                break;
        }
    };
    private DialogUitl.StringArrayDialogCallback mArrayDialogCallback = new DialogUitl.StringArrayDialogCallback() {
        @Override
        public void onItemClick(String text, int tag) {
            switch (tag) {
                case Constants.PAY_TYPE_ALI://支付宝支付
                    aliPay();
                    break;
                case Constants.PAY_TYPE_WX://微信支付
                    wxPay();
                    break;
            }
        }
    };

    private void aliPay() {
        if (!AppConfig.isAppExist(Constants.PACKAGE_NAME_ALI)) {
            ToastUtil.show(R.string.coin_ali_not_install);
            return;
        }
        if (TextUtils.isEmpty(mAliPartner) || TextUtils.isEmpty(mAliSellerId) || TextUtils.isEmpty(mAliPrivateKey)) {
            ToastUtil.show(Constants.PAY_ALI_NOT_ENABLE);
            return;
        }
        AliPayBuilder builder = new AliPayBuilder(this, mAliPartner, mAliSellerId, mAliPrivateKey);
        builder.setCoinName(mCoinName);
        builder.setCoinBean(mCheckedCoinBean);
        builder.setPayCallback(mPayCallback);
        builder.pay();
    }

    private void wxPay() {
        if (!AppConfig.isAppExist(Constants.PACKAGE_NAME_WX)) {
            ToastUtil.show(R.string.coin_wx_not_install);
            return;
        }
        if (TextUtils.isEmpty(mWxAppID)) {
            ToastUtil.show(Constants.PAY_WX_NOT_ENABLE);
            return;
        }
        WxPayBuilder builder = new WxPayBuilder(mContext, mWxAppID);
        builder.setCoinBean(mCheckedCoinBean);
        builder.setPayCallback(mPayCallback);
        builder.pay();
    }

    PayCallback mPayCallback = new PayCallback() {
        @Override
        public void onSuccess() {
            // checkPayResult();
        }

        @Override
        public void onFailed() {
            //ToastUtil.show(R.string.coin_charge_failed);
        }
    };

    private void getList() {
        HttpUtil.getMyQb(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String coin=obj.getString("coin");
                    String votes=obj.getString("votes");
                    String votestotal=obj.getString("votestotal");
                    String ordernums=obj.getString("ordernums");
                    String money=obj.getString("money");
                   xieyiUrl=obj.getString("xieyi");
                    mTv_cz.setText(coin);
                    mTv_zbsy.setText(votes);
                    mTv_ktcps.setText(votestotal);
                    mTv_dpsy.setText(ordernums);
                    mTv_ktxje.setText(money);
                    mAliPartner = obj.getString("aliapp_partner");
                    mAliSellerId = obj.getString("aliapp_seller_id");
                    mAliPrivateKey = obj.getString("aliapp_key_android");
                    mWxAppID = obj.getString("wx_appid");
                    Log.v("tags",mWxAppID+"----wx");
                    boolean aliPayEnable = obj.getIntValue("aliapp_switch") == 1;//支付宝是否开启
                    boolean wxPayEnable = obj.getIntValue("wx_switch") == 1;//微信支付是否开启
                    if (aliPayEnable) {
                        mSparseArray.put(Constants.PAY_TYPE_ALI, WordUtil.getString(R.string.coin_pay_ali));
                    }
                    if (wxPayEnable) {
                        mSparseArray.put(Constants.PAY_TYPE_WX, WordUtil.getString(R.string.coin_pay_wx));
                    }
                    ArrayList<QbBean.RulesBean> list = (ArrayList<QbBean.RulesBean>) JSON.parseArray(obj.getString("rules"), QbBean.RulesBean.class);
                    QbBean.RulesBean diyRule=new QbBean.RulesBean();
                    diyRule.setChecked(false);
                    diyRule.setId("999");
                    diyRule.setMoney("输入其他金额");
                    list.add(diyRule);
                    if(Utils.isNotEmpty(list)){
                        mAdapter.setNewData(list);
                    }


                }
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

    private void release(){
//        if(mVideoHomeViewHolder!=null){
//            mVideoHomeViewHolder.release();
//        }
//        mVideoHomeViewHolder=null;
    }

    @Override
    public void onBackPressed() {
        release();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        SpUtil.getInstance().setBooleanValue("setMe",true);
        release();
        super.onDestroy();
    }
}
