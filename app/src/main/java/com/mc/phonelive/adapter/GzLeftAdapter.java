package com.mc.phonelive.adapter;

import android.content.Intent;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.bean.FansItemBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class GzLeftAdapter extends BaseQuickAdapter<FansItemBean, BaseViewHolder> {
    public GzLeftAdapter() {
        super(R.layout.item_gz_left);
    }
    @Override
    protected void convert(BaseViewHolder helper, FansItemBean item) {
        helper.setText(R.id.tv_username,item.getUser_nicename());
        helper.setText(R.id.tv_msg,item.getSignature());
        MyImageView miv=helper.getView(R.id.miv);//isattention
        TextView tv_status=helper.getView(R.id.tv_status);
        String isattent= item.getAll_attention();
        helper.addOnClickListener(R.id.tv_status);
        if(Utils.isNotEmpty(isattent)&&isattent.equals("1")){
            tv_status.setText("互相关注");
        }else{
            tv_status.setText("已关注");
        }
        if(Utils.isNotEmpty(item.getAvatar())){
            GlideUtils.loadImage(mContext,item.getAvatar(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }

//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }


}
