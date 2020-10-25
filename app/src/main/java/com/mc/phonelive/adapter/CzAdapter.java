package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.ScoreItemBean;

/**
 * Created by Administrator on 2018/8/7.
 */

public class CzAdapter extends BaseQuickAdapter<ScoreItemBean, BaseViewHolder> {
    public CzAdapter() {
        super(R.layout.item_jf_mx);
    }
    @Override
    protected void convert(BaseViewHolder helper, ScoreItemBean item) {
        helper.setText(R.id.tv_note,"茶籽充值");
        helper.setText(R.id.tv_addtime,item.getAdddate());
        helper.setText(R.id.tv_score,"￥"+item.getMoney());
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
