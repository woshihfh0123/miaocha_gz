package com.mc.phonelive.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.FamilyLeftBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class FamilyAdapter extends BaseQuickAdapter<FamilyLeftBean, BaseViewHolder> {
    public FamilyAdapter() {
        super(R.layout.item_fmy);
    }
    @Override
    protected void convert(BaseViewHolder helper, FamilyLeftBean item) {
        helper.setText(R.id.tv_username,item.getUser_nicename());
        TextView tv_msg=helper.getView(R.id.tv_msg);
        if(Utils.isNotEmpty(item.getSignature())){
            tv_msg.setVisibility(View.VISIBLE);
        }else{
            tv_msg.setVisibility(View.GONE);
        }
        tv_msg.setText(item.getSignature());
        MyImageView miv=helper.getView(R.id.miv);
        helper.addOnClickListener(R.id.iv_msg);
        if(Utils.isNotEmpty(item.getAvatar())){
            GlideUtils.loadImage(mContext,item.getAvatar(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }

//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
