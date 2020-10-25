package com.mc.phonelive.adapter.foxtone;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.foxtone.TradingPayconfirmActivity;
import com.mc.phonelive.bean.foxtone.TradingMailBean;
import com.mc.phonelive.fragment.TradingMailboxFragment;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * 交易信箱
 */
public class TradingMailAdapter extends BaseQuickAdapter<TradingMailBean.InfoBean, BaseViewHolder> {
    private MailOnClickLisenter mOnClickLisenter;

    //    private String mType;//类型 1.买入  2.卖出  3.完成
    public TradingMailAdapter(int layoutResId, @Nullable List<TradingMailBean.InfoBean> data, String type) {
        super(layoutResId, data);
//        this.mType =type;
    }

    @Override
    protected void convert(BaseViewHolder helper, TradingMailBean.InfoBean item) {
        TextView orderNumber = helper.getView(R.id.trading_order_number);
        TextView orderTime = helper.getView(R.id.trading_order_time);
        TextView orderContent = helper.getView(R.id.trading_order_content);
        TextView orderCountdown = helper.getView(R.id.trading_order_countdown);
        TextView orderApplyBtn = helper.getView(R.id.trading_appeal_btn);
        CountdownView cuntdown_view = helper.getView(R.id.cuntdown_view);
        TextView orderCodeBtn = helper.getView(R.id.trading_code_btn);

        if (!DataSafeUtils.isEmpty(item.getTrade_sn())) {
            orderNumber.setText("订单号：" + item.getTrade_sn());
        }
        if (!DataSafeUtils.isEmpty(item.getAddtime())) {
            orderTime.setText(item.getAddtime());
        }
        if (!DataSafeUtils.isEmpty(item.getDesc())) {
            orderContent.setText(item.getDesc());
        }
//        if (!DataSafeUtils.isEmpty(item.getTimes())) {
//            orderCountdown.setText(item.getTimes());
//        }

        if (!DataSafeUtils.isEmpty(item.getButton_name())) {
            orderCodeBtn.setText(item.getButton_name());
        } else {
            if (item.getState().equals("1"))
                orderCodeBtn.setText("待匹配");
            if (item.getState().equals("2"))
                orderCodeBtn.setText("2匹配成功");
            if (item.getState().equals("3"))
                orderCodeBtn.setText("3已付款");
            if (item.getState().equals("4"))
                orderCodeBtn.setText("已完成");
        }

        //交易状态(0取消 1 交易中 2匹配成功 3已付款 4确认收款已完成 5申诉)
        //TODO:这个判断主要是针对申诉按钮
        if (item.getState().equals("4")) {
            orderApplyBtn.setVisibility(View.GONE);
            orderCodeBtn.setTextColor(mContext.getResources().getColor(R.color.white));
            orderCodeBtn.setBackgroundResource(R.drawable.trading_bg03);
        } else {
            orderApplyBtn.setVisibility(View.VISIBLE);
            //TODO:这个判断针对是否能点击按钮
            Log.v("tags", TradingMailboxFragment.mType + "----------");
            if (item.getState().equals("2") || (item.getState().equals("3") && TradingMailboxFragment.mType.equals("2"))) {
                orderCodeBtn.setTextColor(mContext.getResources().getColor(R.color.black));
                orderCodeBtn.setBackgroundResource(R.drawable.trading_bg01);
            } else {
                orderCodeBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                orderCodeBtn.setBackgroundResource(R.drawable.trading_bg03);
            }
        }


        //TODO:这个判断主要是针对倒计时
        if (item.getState().equals("3")) {//已付款 开始倒计时，其他状态，没得倒计时
            orderCountdown.setVisibility(View.VISIBLE);
            orderCountdown.setFocusable(true);
            if (!DataSafeUtils.isEmpty(item.getTimes()) && !item.getTimes().equals("0")) {
                if (Long.parseLong(item.getTimes()) > 0) {
                    Log.v("tags", item.getTimes() + "------------times");
                    cuntdown_view.start(Long.parseLong(item.getTimes()));
                    cuntdown_view.setOnCountdownIntervalListener(10, new CountdownView.OnCountdownIntervalListener() {
                        @Override
                        public void onInterval(CountdownView cv, long remainTime) {
//                        int time = (int) (remainTime / 1000) + 1;
                            orderCountdown.setText("剩余" + cv.getMinute() + "分" + cv.getSecond() + "秒");
                        }
                    });

                    cuntdown_view.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                        @Override
                        public void onEnd(CountdownView cv) {
                            mOnClickLisenter.refreshListView();
                        }
                    });
                }
            }
        } else {
            orderCountdown.setVisibility(View.GONE);
        }

        orderApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickLisenter.ApplyOnClick(item.getId());
            }
        });
        orderCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getState().equals("2") && TradingMailboxFragment.mType.equals("1")) {//2.查看收款码  上传资料
                    if (!DataSafeUtils.isEmpty(item.getInfo())) {
                        mOnClickLisenter.CodeLookOnClick(item.getId(), item.getInfo(), item.getState(), item.getType_id());
                    }
                    return;
                } else if (item.getState().equals("3") && TradingMailboxFragment.mType.equals("2")) {// 3.已付款   待确认  点击查看信息，
                    Intent intent = new Intent(mContext, TradingPayconfirmActivity.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("img", item.getPay_img());
                    mContext.startActivity(intent);
                    return;
                }

            }
        });

        orderContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + 1);
                intent.setData(data);
                mContext.startActivity(intent);
            }
        });
    }

    public interface MailOnClickLisenter {
        void ApplyOnClick(String orderid);

        void CodeLookOnClick(String id, TradingMailBean.InfoBean.UserMsgBean bean, String status, String type);

        void refreshListView();
    }

    public void setMailListener(MailOnClickLisenter mailListener) {
        mOnClickLisenter = mailListener;
    }


}
