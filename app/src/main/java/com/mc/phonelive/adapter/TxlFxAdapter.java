package com.mc.phonelive.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.TxlBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class TxlFxAdapter extends BaseQuickAdapter<TxlBean, BaseViewHolder> {
    public TxlFxAdapter() {
        super(R.layout.item_fx_txl);
    }
    @Override
    protected void convert(BaseViewHolder helper, TxlBean item) {
        helper.setText(R.id.tv_username,item.getNickname());
        TextView tv_msg=helper.getView(R.id.tv_msg);
        if(Utils.isNotEmpty(item.getNote())){
            tv_msg.setVisibility(View.VISIBLE);
        }else{
            tv_msg.setVisibility(View.GONE);
        }
        tv_msg.setText(item.getNote());
        MyImageView miv=helper.getView(R.id.miv);
        helper.addOnClickListener(R.id.tv_status);
        TextView tv_status=helper.getView(R.id.tv_status);
        String isattent=item.getIsattent();
        if(Utils.isNotEmpty(isattent)&&isattent.equals("1")){
            tv_status.setText("已关注");
            tv_status.setBackgroundResource(R.drawable.shape_blk);
        }else{
            tv_status.setText("关注");
            tv_status.setBackgroundResource(R.drawable.shape_red);
        }
        if(Utils.isNotEmpty(item.getAvatar())){
            GlideUtils.loadImage(mContext,item.getAvatar(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }
    }
}
