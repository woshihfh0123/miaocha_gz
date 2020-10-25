package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.ShopsHomeBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class GoodsTypeAdapter extends BaseQuickAdapter<ShopsHomeBean.CateBean, BaseViewHolder> {
    public GoodsTypeAdapter() {
        super(R.layout.item_goods_type);
    }
    @Override
    protected void convert(BaseViewHolder helper, ShopsHomeBean.CateBean item) {
        helper.setText(R.id.tv_title,item.getName());
        MyImageView miv=helper.getView(R.id.miv);
        if(Utils.isNotEmpty(item.getThumb())){
            GlideUtils.loadImage(mContext,item.getThumb(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }
    }
}
