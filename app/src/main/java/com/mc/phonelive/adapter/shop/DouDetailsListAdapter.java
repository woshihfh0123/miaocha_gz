package com.mc.phonelive.adapter.shop;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.LedouBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * created by WWL on 2020/4/14 0014:15
 */
public class DouDetailsListAdapter extends BaseQuickAdapter<LedouBean.InfoBean.DetailsBean, BaseViewHolder> {
    public DouDetailsListAdapter(int layoutResId, @Nullable List<LedouBean.InfoBean.DetailsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LedouBean.InfoBean.DetailsBean item) {
        helper.setText(R.id.item_title, item.getTitle());
        helper.setText(R.id.item_time, item.getTime());
        TextView mPrice = helper.getView(R.id.item_price);

        if (!DataSafeUtils.isEmpty(item.getStatus())) {
            if (item.getStatus().equals("1")) {
                mPrice.setText("+" + item.getPrice());
                mPrice.setTextColor(mContext.getResources().getColor(R.color.redlive));
            } else {
                mPrice.setText("-" + item.getPrice());
                mPrice.setTextColor(mContext.getResources().getColor(R.color.black));
            }
        }
    }
}
