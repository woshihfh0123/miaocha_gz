package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;

/**
 * Created by Administrator on 2018/8/7.
 */

public class SelectorLxAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SelectorLxAdapter() {
        super(R.layout.item_slct_lx);
    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name,item);
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
