package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.StoreBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class ShopAllAdapter extends BaseQuickAdapter<StoreBean, BaseViewHolder> {
    public ShopAllAdapter() {
        super(R.layout.item_all_shops);
    }
    @Override
    protected void convert(BaseViewHolder helper, StoreBean item) {
       MyImageView miv= helper.getView(R.id.miv);
       if(Utils.isNotEmpty(item.getLogo())){
           GlideUtils.loadImage(mContext,item.getLogo(),miv);
       }else{
           miv.setImageResource(R.drawable.default_img);
       }
       helper.setText(R.id.tv_title,item.getTitle());
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
