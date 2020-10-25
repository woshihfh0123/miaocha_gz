package com.mc.phonelive.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.FansItemBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class FsRightAdapter extends BaseQuickAdapter<FansItemBean, BaseViewHolder> {
    public FsRightAdapter() {
        super(R.layout.item_fs_friends);
    }
    @Override
    protected void convert(BaseViewHolder helper, FansItemBean item) {
        helper.setText(R.id.tv_username,item.getUser_nicename());
        helper.setText(R.id.tv_msg,item.getSignature());
        helper.addOnClickListener(R.id.iv_yc);
        helper.addOnClickListener(R.id.tv_status);
        MyImageView miv=helper.getView(R.id.miv);
        ImageView iv_hg=helper.getView(R.id.iv_hg);
        TextView tv_status_name=helper.getView(R.id.tv_status_name);
        LinearLayout tv_status=helper.getView(R.id.tv_status);
       String isattent= item.getIsattention();
       if(Utils.isNotEmpty(isattent)&&isattent.equals("1")){
           tv_status_name.setText("互相关注");
           iv_hg.setVisibility(View.GONE);
           tv_status.setBackgroundResource(R.drawable.shape_blk);
       }else{
           tv_status_name.setText("回关");
           iv_hg.setVisibility(View.VISIBLE);
           tv_status.setBackgroundResource(R.drawable.shape_red);
       }
        if(Utils.isNotEmpty(item.getAvatar())){
            GlideUtils.loadImage(mContext,item.getAvatar(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }

    }
}
