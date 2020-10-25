package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.ScoreBtmBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class JfBtmAdapter extends BaseQuickAdapter<ScoreBtmBean, BaseViewHolder> {
    public JfBtmAdapter() {
        super(R.layout.item_jf_btm);
    }
    @Override
    protected void convert(BaseViewHolder helper, ScoreBtmBean item) {
        MyImageView miv=helper.getView(R.id.miv);
        if(Utils.isNotEmpty(item.getThumb())){
            GlideUtils.loadImage(mContext,item.getThumb(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }
        helper.setText(R.id.tv_title,item.getTitle());
        helper.setText(R.id.tv_jf,item.getPrice()+"积分");
    }
}
