package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.TagYhqBean;

/**
 * Created by Administrator on 2018/8/7.
 */

public class YhqHvAdapter extends BaseQuickAdapter<TagYhqBean, BaseViewHolder> {
    public YhqHvAdapter() {
        super(R.layout.item_yhq_ss);
    }
    @Override
    protected void convert(BaseViewHolder helper, TagYhqBean item) {
        helper.setText(R.id.tv_name,item.getName());

    }
}
