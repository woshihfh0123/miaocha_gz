package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.FarmilyBean;

/**
 * Created by Administrator on 2018/8/7.
 */

public class OrderMyAdapter extends BaseQuickAdapter<FarmilyBean, BaseViewHolder> {
    public OrderMyAdapter() {
        super(R.layout.item_order_my);
    }
    @Override
    protected void convert(BaseViewHolder helper, FarmilyBean item) {
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
