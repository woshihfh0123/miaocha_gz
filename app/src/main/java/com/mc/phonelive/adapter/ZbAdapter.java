package com.mc.phonelive.adapter;

import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.bean.ShopsHomeBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class ZbAdapter extends BaseQuickAdapter<ShopsHomeBean.LivelistBean, BaseViewHolder> {//LiveBean
    public ZbAdapter() {
        super(R.layout.item_zb);
    }
    @Override
    protected void convert(BaseViewHolder helper, ShopsHomeBean.LivelistBean item) {
        LiveBean liveBean=new LiveBean();
        helper.setText(R.id.tv_gkrs,item.getLikelook());
        helper.setText(R.id.tv_dzl,item.getVotestotal());
        helper.setText(R.id.tv_title,item.getTitle());
        helper.setText(R.id.tv_dzl,item.getVotestotal());
        LinearLayout line_goodinfo = helper.getView(R.id.line_goodinfo);

        MyImageView miv=helper.getView(R.id.miv);
        if(Utils.isNotEmpty(item.getThumb())){
            GlideUtils.loadImage(mContext,item.getThumb(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }
        MyImageView miv_pic=helper.getView(R.id.miv_pic);
        if(Utils.isNotEmpty(item.getAvatar())){
            GlideUtils.loadImage(mContext,item.getAvatar(),miv_pic);
        }else{
            miv_pic.setImageResource(R.drawable.default_img);
        }
        MyImageView miv_logo=helper.getView(R.id.miv_logo);
        if(Utils.isNotEmpty(item.getGoodslist())){
            line_goodinfo.setVisibility(View.VISIBLE);
            if(Utils.isNotEmpty(item.getGoodslist().get(0).getGoods_img())){
                GlideUtils.loadImage(mContext,item.getGoodslist().get(0).getGoods_img(),miv_logo);
            }else{
                miv_logo.setImageResource(R.drawable.default_img);
            }
            helper.setText(R.id.tv_goods_name,""+item.getGoodslist().get(0).getTitle());
            helper.setText(R.id.tv_price,""+item.getGoodslist().get(0).getPrice());
        }else{
            line_goodinfo.setVisibility(View.GONE);
        }
    }
}
