package com.mc.phonelive.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.HeiMdBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class HeiMingDanAdapter extends BaseQuickAdapter<HeiMdBean, BaseViewHolder> {
    public HeiMingDanAdapter() {
        super(R.layout.item_hmd);
    }
    @Override
    protected void convert(BaseViewHolder helper, HeiMdBean item) {
        helper.setText(R.id.tv_username,item.getUser_nicename());
        TextView tv_msg=helper.getView(R.id.tv_msg);
        if(Utils.isNotEmpty(item.getSignature())){
            tv_msg.setVisibility(View.VISIBLE);
        }else{
            tv_msg.setVisibility(View.GONE);
        }
        tv_msg.setText(item.getSignature());
        MyImageView miv=helper.getView(R.id.miv);
        if(Utils.isNotEmpty(item.getAvatar())){
            GlideUtils.loadImage(mContext,item.getAvatar(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }
        helper.addOnClickListener(R.id.tv_status);
        String isblack=item.getIsblack();
        if(Utils.isNotEmpty(isblack)&&isblack.equals("1")){//
            helper.setText(R.id.tv_status,"解除拉黑");
        }else{
            helper.setText(R.id.tv_status,"拉黑");
        }
    }
}
