package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.ScoreTopBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class JfTopAdapter extends BaseQuickAdapter<ScoreTopBean, BaseViewHolder> {
    public JfTopAdapter() {
        super(R.layout.item_jf_top);
    }
    @Override
    protected void convert(BaseViewHolder helper, ScoreTopBean item) {
        MyImageView miv=helper.getView(R.id.miv);
        if(Utils.isNotEmpty(item.getThumb())){
            GlideUtils.loadImage(mContext,item.getThumb(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }
        helper.setText(R.id.tv_title,item.getTitle());
        helper.setText(R.id.tv_jf,item.getPrice()+"积分");
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
