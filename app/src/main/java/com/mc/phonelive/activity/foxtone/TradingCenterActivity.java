package com.mc.phonelive.activity.foxtone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.bean.foxtone.TradingCenterBean;
import com.mc.phonelive.dialog.BuyYinDouDialog;
import com.mc.phonelive.dialog.RedpackDialog;
import com.mc.phonelive.fragment.TradingFindBuyListFragment;
import com.mc.phonelive.fragment.TradingMailboxFragment;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 交易中心
 */
public class TradingCenterActivity extends AbsActivity {
    @BindView(R.id.rightView)
    TextView rightView;
    @BindView(R.id.Coin_tv)
    TextView CoinTv;
    //    @BindView(R.id.buy_order_tv)
//    TextView buyOrderTv;
//    @BindView(R.id.buy_order_cancle_tv)
//    TextView buyOrderCancleTv;
    @BindView(R.id.hand_fee_tv)
    TextView handFeeTv;
    @BindView(R.id.change_count_tv)
    TextView changeCountTv;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private String[] mTitleDataList = {"求购列表", "交易信箱"};
    private ArrayList<Fragment> mFragmentList;
    private String mRules = "";//规则

    @Override
    protected int getLayoutId() {
        return R.layout.trading_center_layout;
    }

    @Override
    protected void main() {
        setTitle("交易中心");
        initMagicIndicator();

        initViewData();
    }

    private void initViewData() {


        HttpUtil.getTradeList(1,"", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<TradingCenterBean.InfoBean> data = JSON.parseArray(Arrays.toString(info), TradingCenterBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(data) && data.size() > 0) {
                    if (!DataSafeUtils.isEmpty(data.get(0).getData())) {
                        setTradingMsg(data.get(0).getData());
                    }
                }
            }
        });

    }

    private void setTradingMsg(TradingCenterBean.InfoBean.DataBean data) {
        if (!DataSafeUtils.isEmpty(data.getTotal()))
            CoinTv.setText(data.getTotal());
        if (!DataSafeUtils.isEmpty(data.getTrade_fee()))
            handFeeTv.setText(data.getTrade_fee());
        if (!DataSafeUtils.isEmpty(data.getTrade_counts()))
            changeCountTv.setText(data.getTrade_counts());
        if (!DataSafeUtils.isEmpty(data.getWeb_url())) {
            mRules = data.getWeb_url();
        }
    }


    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);//是固定

        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitleDataList.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);// 文字变化样式
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mTitleDataList[index]);
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 3));
                indicator.setLineWidth(UIUtil.dip2px(context, 20));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#FF4443"));
                return indicator;
            }

        });

        initViewPagerData();
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }


    public void initViewPagerData() {
        mFragmentList = new ArrayList<>();

        TradingFindBuyListFragment fragment = TradingFindBuyListFragment.newInstance();
        mFragmentList.add(fragment);

        TradingMailboxFragment mailboxFragment = TradingMailboxFragment.newInstance();
        mFragmentList.add(mailboxFragment);

        mViewPager.setOffscreenPageLimit(mTitleDataList.length);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rightView, R.id.buy_order_tv, R.id.buy_order_cancle_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightView://交易规则
                if (!DataSafeUtils.isEmpty(mRules))
                    WebViewActivity.forward(mContext, mRules);
                break;
            case R.id.buy_order_tv://挂买单
                showBuyDialog();
                break;
            case R.id.buy_order_cancle_tv://取消买单
                cancleOrderDialog();
                break;
        }
    }

    /**
     * 挂买单
     */
    private void showBuyDialog() {
        BuyYinDouDialog oRderDialog = new BuyYinDouDialog(mContext) {
            @Override
            public void doConfirmUp(String count, String price, String password, String type) {

                if (!ButtonUtils.isFastDoubleClick02()) {
                    HttpUtil.addTrade(count, price, type, password, new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            ToastUtil.show(msg);
                            if (code == 0) {
                                dismiss();
                                EventBus.getDefault().post(new EventBusModel("refresh_buy_list"));
                            }else  if (code == 1001){
                                mContext.startActivity(new Intent(mContext,SetTransactionPwdActivity.class));
                            }
                        }
                    });
                }
            }
        };
        oRderDialog.setCancelable(false);
        oRderDialog.show();
    }

    private void cancleOrderDialog() {
        RedpackDialog dialog = new RedpackDialog(mContext, "温馨提示", "确定取消买单吗？") {
            @Override
            public void doConfirmUp() {
                if (!ButtonUtils.isFastDoubleClick02()) {
                    HttpUtil.cancelTrade(new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0) {
                                dismiss();
                                EventBus.getDefault().post(new EventBusModel("refresh_buy_list"));
                            }
                            ToastUtil.show(msg);
                        }
                    });
                }
            }
        };
        dialog.show();
    }


}
