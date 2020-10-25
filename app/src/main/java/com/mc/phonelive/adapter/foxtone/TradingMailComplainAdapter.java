package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.ComplainListBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 交易信箱
 */
public class TradingMailComplainAdapter extends BaseQuickAdapter<ComplainListBean.InfoBean, BaseViewHolder> {
    private MailOnClickLisenter mOnClickLisenter;

    public TradingMailComplainAdapter(int layoutResId, @Nullable List<ComplainListBean.InfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ComplainListBean.InfoBean item) {
        TextView orderTime = helper.getView(R.id.trading_time);
        TextView orderContent = helper.getView(R.id.trading_order_content);
        TextView orderApplyBtn = helper.getView(R.id.trading_appeal_btn);
        TextView trading_order_number = helper.getView(R.id.trading_order_number);

        if (!DataSafeUtils.isEmpty(item.getTrade_sn())) {
            trading_order_number.setText("订单号："+item.getTrade_sn());
        }
        if (!DataSafeUtils.isEmpty(item.getAddtime())) {
            orderTime.setText(item.getAddtime());
        }
        if (!DataSafeUtils.isEmpty(item.getContent())) {
            orderContent.setText(item.getContent());
        }
        if (!DataSafeUtils.isEmpty(item.getState_name())) {
            orderApplyBtn.setText(item.getState_name());
        }

    }

    public interface MailOnClickLisenter {
        void ApplyOnClick(String orderid);

        void CodeLookOnClick(String orderid, String codeimg);
    }

    public void setMailListener(MailOnClickLisenter mailListener) {
        mOnClickLisenter = mailListener;
    }
}
