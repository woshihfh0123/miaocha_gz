package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.YindouBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 音豆
 */
public class YinDouAdapter extends BaseQuickAdapter<YindouBean.InfoBean.ListBean, BaseViewHolder> {
    public YinDouAdapter(int layoutResId, @Nullable List<YindouBean.InfoBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, YindouBean.InfoBean.ListBean item) {
        if (!DataSafeUtils.isEmpty(item.getNote()))
            helper.setText(R.id.item_title, item.getNote());
        if (!DataSafeUtils.isEmpty(item.getCreate_time()))
            helper.setText(R.id.item_time, item.getCreate_time());

        TextView mPrice = helper.getView(R.id.item_price);
        if (!DataSafeUtils.isEmpty(item.getChanges())) {
            if (item.getType().equals("1")) {
                mPrice.setText("+" + item.getChanges());
                mPrice.setTextColor(mContext.getResources().getColor(R.color.yellow1));
            } else {
                mPrice.setText("-" + item.getChanges());
                mPrice.setTextColor(mContext.getResources().getColor(R.color.white));
            }
        }
    }
}
