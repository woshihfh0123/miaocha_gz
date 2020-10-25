package com.mc.phonelive.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.MyCoinActivity;
import com.mc.phonelive.bean.CoinBean;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.event.CoinChangeEvent;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.pay.PayCallback;
import com.mc.phonelive.pay.ali.AliPayBuilder;
import com.mc.phonelive.pay.wx.WxPayBuilder;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MainCoinViewHolder extends AbsMainViewHolder implements OnItemClickListener<CoinBean>, View.OnClickListener {

    public MainCoinViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    private View mTop;
    private TextView mBalance;
    private TextView coin_submit;
    private long mBalanceValue;
    private RecyclerView mRecyclerView;
    //    private CoinAdapter mAdapter;
    private String mCoinName;
    private CoinBean mCheckedCoinBean;
    private String mAliPartner;// 支付宝商户ID
    private String mAliSellerId; // 支付宝商户收款账号
    private String mAliPrivateKey; // 支付宝商户私钥，pkcs8格式
    private String mWxAppID;//微信AppID
    private boolean mFirstLoad = true;
    private SparseArray<String> mSparseArray;
    List<CoinBean> mConList = new ArrayList<>();
    BaseQuickAdapter<CoinBean, BaseViewHolder> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.holder_coin;
    }

    @Override
    public void init() {
        mSparseArray = new SparseArray<>();
        mCoinName = AppConfig.getInstance().getCoinName();
        mTop = findViewById(R.id.top);
        mBalance = (TextView) findViewById(R.id.balance);
        coin_submit = (TextView) findViewById(R.id.coin_submit);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        coin_submit.setOnClickListener(this);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
//        mAdapter = new CoinAdapter(mContext, DpUtil.dp2px(150), mCoinName);
//        mAdapter.setContactView(mTop);
//        mAdapter.setOnItemClickListener(this);
//        mRecyclerView.setAdapter(mAdapter);
        getAdapter(mConList);

    }

    @Override
    public void loadData() {
        super.loadData();
        if (mFirstLoad) {
            mFirstLoad = false;
            loadNewData();
        } else {
            checkPayResult();
        }
    }


    private void getAdapter(List<CoinBean> data) {
        mAdapter = new BaseQuickAdapter<CoinBean, BaseViewHolder>(R.layout.item_coin, data) {
            @Override
            protected void convert(BaseViewHolder helper, CoinBean bean) {
                TextView mCoin = helper.getView(R.id.coin);
                TextView mMoney = helper.getView(R.id.money);
                LinearLayout itemView = helper.getView(R.id.coin_item);
                mCoin.setText(bean.getCoin() + "钻石");
                if (!DataSafeUtils.isEmpty(bean.getMoney())) {
                    mMoney.setText(bean.getMoney() + "元");
                    mMoney.setVisibility(View.VISIBLE);
                } else {
                    mMoney.setVisibility(View.GONE);
                    mMoney.setText("");
                }
                if (bean.isChecked()) {
                    itemView.setBackgroundResource(R.drawable.bg_coin_money);
                } else {
                    itemView.setBackgroundResource(R.drawable.bg_coin_money1);
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mCheckedCoinBean = (CoinBean) adapter.getData().get(position);
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (i == position) {
                        mAdapter.getData().get(i).setChecked(true);
                    } else {
                        mAdapter.getData().get(i).setChecked(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    private void loadNewData() {
        HttpUtil.getBalance(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String coin = obj.getString("coin");
                    mBalanceValue = Long.parseLong(coin);
                    mBalance.setText(coin);
                    List<CoinBean> list = JSON.parseArray(obj.getString("rules"), CoinBean.class);
                    if (mAdapter != null) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setChecked(false);
                        }
                        mConList = new ArrayList<>();
                        mConList.addAll(list);
                        mAdapter.setNewData(list);
                    }
                    mAliPartner = obj.getString("aliapp_partner");
                    mAliSellerId = obj.getString("aliapp_seller_id");
                    mAliPrivateKey = obj.getString("aliapp_key_android");
                    mWxAppID = obj.getString("wx_appid");
                    boolean aliPayEnable = obj.getIntValue("aliapp_switch") == 1;//支付宝是否开启
                    boolean wxPayEnable = obj.getIntValue("wx_switch") == 1;//微信支付是否开启
                    if (aliPayEnable) {
                        mSparseArray.put(Constants.PAY_TYPE_ALI, WordUtil.getString(R.string.coin_pay_ali));
                    }
                    if (wxPayEnable) {
                        mSparseArray.put(Constants.PAY_TYPE_WX, WordUtil.getString(R.string.coin_pay_wx));
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(CoinBean bean, int position) {
        mCheckedCoinBean = bean;
//        if (mSparseArray == null || mSparseArray.size() == 0) {
//            ToastUtil.show(Constants.PAY_ALL_NOT_ENABLE);
//            return;
//        }
//        DialogUitl.showStringArrayDialog(mContext, mSparseArray, mArrayDialogCallback);
    }

    @Override
    public void onClick(View v) {
        if (mCheckedCoinBean != null) {
            if (mSparseArray == null || mSparseArray.size() == 0) {
                ToastUtil.show(Constants.PAY_ALL_NOT_ENABLE);
                return;
            }
            DialogUitl.showStringArrayDialog(mContext, mSparseArray, mArrayDialogCallback);
        } else {
            ToastUtil.show("请选择充值金额");
        }
    }

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
        AliPayBuilder builder = new AliPayBuilder((Activity) mContext, mAliPartner, mAliSellerId, mAliPrivateKey);
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

    /**
     * 检查支付结果
     */
    private void checkPayResult() {
        HttpUtil.getBalance(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String coin = obj.getString("coin");
                    mBalance.setText(coin);
                    long balanceValue = Long.parseLong(coin);
                    if (balanceValue > mBalanceValue) {
                        mBalanceValue = balanceValue;
                        ToastUtil.show(R.string.coin_charge_success);
                        UserBean u = AppConfig.getInstance().getUserBean();
                        if (u != null) {
                            u.setCoin(coin);
                        }
                        EventBus.getDefault().post(new CoinChangeEvent(coin, true));
                    }
                }
            }
        });
    }

    public static void forward(Context context) {
        context.startActivity(new Intent(context, MyCoinActivity.class));
    }
}
