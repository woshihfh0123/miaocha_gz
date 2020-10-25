package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.FansItemBean;

/**
 * Created by Administrator on 2018/8/7.
 */

public class FsTopAdapter extends BaseQuickAdapter<FansItemBean, BaseViewHolder> {
    public FsTopAdapter() {
        super(R.layout.item_fs_top);
    }
    @Override
    protected void convert(BaseViewHolder helper, FansItemBean item) {
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
