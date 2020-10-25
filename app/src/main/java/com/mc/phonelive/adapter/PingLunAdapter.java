package com.mc.phonelive.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.PingLunListBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class PingLunAdapter extends BaseQuickAdapter<PingLunListBean, BaseViewHolder> {
    public PingLunAdapter() {
        super(R.layout.item_pl_msg);
    }
    @Override
    protected void convert(BaseViewHolder helper, PingLunListBean item) {
        MyImageView miv=helper.getView(R.id.miv);
        ImageView iv_pic=helper.getView(R.id.iv_pic);
        helper.setText(R.id.tv_nick_name,item.getUser_nicename());
        helper.setText(R.id.tv_note,item.getNote());
        helper.setText(R.id.tv_date,item.getAdddate());
        if(Utils.isNotEmpty(item.getAvatar())){
            GlideUtils.loadImage(mContext,item.getAvatar(),miv);
        }
        if(Utils.isNotEmpty(item.getThumb_s())){
            GlideUtils.loadImage(mContext,item.getThumb_s(),iv_pic);
        }
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
