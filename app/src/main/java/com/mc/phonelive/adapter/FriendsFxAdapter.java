package com.mc.phonelive.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.FamilyRightBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class FriendsFxAdapter extends BaseQuickAdapter<FamilyRightBean, BaseViewHolder> {
    public FriendsFxAdapter() {
        super(R.layout.item_fx_friends);
    }
    @Override
    protected void convert(BaseViewHolder helper, FamilyRightBean item) {
        helper.setText(R.id.tv_username,item.getUser_nicename());
        helper.setText(R.id.tv_address,item.getCity());
        String sex=item.getSex();
        ImageView iv_sex=helper.getView(R.id.iv_sex);
        if(Utils.isNotEmpty(sex)&&sex.equals("1")){
            iv_sex.setImageResource(R.drawable.man_ic);
        }else{
            iv_sex.setImageResource(R.drawable.women_ic);
        }
        TextView tv_msg=helper.getView(R.id.tv_msg);
        if(Utils.isNotEmpty(item.getSignature())){
            tv_msg.setVisibility(View.VISIBLE);
        }else{
            tv_msg.setVisibility(View.GONE);
        }
        tv_msg.setText(item.getSignature());
        MyImageView miv=helper.getView(R.id.miv);
        TextView tv_status=helper.getView(R.id.tv_status);
        helper.addOnClickListener(R.id.tv_status);
        helper.addOnClickListener(R.id.iv_yc);
        String isattent= item.getIsattention();
        if(Utils.isNotEmpty(isattent)&&isattent.equals("1")){
            tv_status.setText("已关注");
            tv_status.setBackgroundResource(R.drawable.shape_blk);
        }else{
            tv_status.setText("+关注");
            tv_status.setBackgroundResource(R.drawable.shape_red);
        }
        if(Utils.isNotEmpty(item.getAvatar())){
            GlideUtils.loadImage(mContext,item.getAvatar(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }
    }
}
