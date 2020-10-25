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

public class MsAdapter extends BaseQuickAdapter<ShopsHomeBean.GoodslistBean, BaseViewHolder> {
    public MsAdapter() {
        super(R.layout.item_ms);
    }
    @Override
    protected void convert(BaseViewHolder helper, ShopsHomeBean.GoodslistBean item) {
      helper.setText(R.id.tv_title,item.getGoodsname());
      helper.setText(R.id.tv_price,item.getPrice());
        MyImageView miv=helper.getView(R.id.miv);
        if(Utils.isNotEmpty(item.getGoods_image())){
            GlideUtils.loadImage(mContext,item.getGoods_image(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
