package com.mc.phonelive.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.FarmilyBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class ZsAdapter extends BaseQuickAdapter<FarmilyBean, BaseViewHolder> {
    public ZsAdapter() {
        super(R.layout.item_zs);
    }
    @Override
    protected void convert(BaseViewHolder helper, FarmilyBean item) {
        int position=helper.getAdapterPosition();
        MyImageView miv=helper.getView(R.id.miv);
        TextView tv_title=helper.getView(R.id.tv_title);
        if(position==0){
            GlideUtils.loadImage(mContext,R.drawable.gwc_01,miv);
            tv_title.setText("渺茶小店");
        }else if(position==1){
            GlideUtils.loadImage(mContext,R.drawable.gwc_02,miv);
            tv_title.setText("我的订单");
        }
//        else if(position==2){
//            GlideUtils.loadImage(mContext,R.drawable.gwc_03,miv);
//            tv_title.setText("购物车");
//        }
        else if(position==2){
            GlideUtils.loadImage(mContext,R.drawable.gwc_04,miv);
            tv_title.setText("优惠券");
        }else if(position==3){
            GlideUtils.loadImage(mContext,R.drawable.gwc_05,miv);
            tv_title.setText("积分商城");
        }
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
