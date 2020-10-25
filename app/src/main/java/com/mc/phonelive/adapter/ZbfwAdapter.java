package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.ServiceBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class ZbfwAdapter extends BaseQuickAdapter<ServiceBean, BaseViewHolder> {
    public ZbfwAdapter() {
        super(R.layout.item_zb_fw);
    }
    @Override
    protected void convert(BaseViewHolder helper, ServiceBean item) {
        helper.setText(R.id.tv_name,item.getName());
        MyImageView miv=helper.getView(R.id.miv);
       if(Utils.isNotEmpty(item.getThumb())){
           GlideUtils.loadImage(mContext,item.getThumb(),miv);
       }
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
