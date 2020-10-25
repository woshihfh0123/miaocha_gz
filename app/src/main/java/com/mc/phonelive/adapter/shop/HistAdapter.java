package com.mc.phonelive.adapter.shop;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;

/**
 * Created by Administrator on 2018/8/7.
 */

public class HistAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public HistAdapter() {
        super(R.layout.item_hist);
    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name,item);
      helper.addOnClickListener(R.id.iv_clear);
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
