package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.QianDaoBean;

/**
 * Created by Administrator on 2018/8/7.
 */

public class QdScoreAdapter extends BaseQuickAdapter<QianDaoBean.BonusListBean, BaseViewHolder> {
    public QdScoreAdapter() {
        super(R.layout.item_order_sc);
    }
    @Override
    protected void convert(BaseViewHolder helper, QianDaoBean.BonusListBean item) {
        helper.setText(R.id.tv_sc,"+"+item.getCoin());
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
