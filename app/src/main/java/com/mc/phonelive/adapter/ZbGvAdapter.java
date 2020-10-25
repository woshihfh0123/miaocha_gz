package com.mc.phonelive.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.WyZbBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class ZbGvAdapter extends BaseQuickAdapter<WyZbBean, BaseViewHolder> {
    public ZbGvAdapter() {
        super(R.layout.item_zb_gv);
    }
    @Override
    protected void convert(BaseViewHolder helper, WyZbBean item) {
        ImageView iv_thumb=helper.getView(R.id.iv_thumb);
        if(Utils.isNotEmpty(item.getThumb())){
            GlideUtils.loadImage(mContext,item.getThumb(),iv_thumb);
        }else{
            iv_thumb.setImageResource(R.drawable.default_img);
        }
        helper.setText(R.id.tv_type_name,item.getType_name());
        helper.setText(R.id.tv_title,item.getUser_nicename());
        helper.setText(R.id.tv_nbs,item.getNums());
        String is_shopping=item.getIs_shopping();
        ImageView iv_gwc=helper.getView(R.id.iv_gwc);
        if(Utils.isNotEmpty(is_shopping)&&is_shopping.equals("1")){
            iv_gwc.setVisibility(View.VISIBLE);
        }else{
            iv_gwc.setVisibility(View.GONE);
        }
        RelativeLayout rl_type=helper.getView(R.id.rl_type);
        if(Utils.isNotEmpty(item.getType())){
            if(item.getType().equals("0")){
                rl_type.setBackgroundResource(R.drawable.wy_mfgk);
            }else if(item.getType().equals("1")){
                rl_type.setBackgroundResource(R.drawable.wy_mpff);
            }else if(item.getType().equals("2")){
                rl_type.setBackgroundResource(R.drawable.wy_jsff);
            }else if(item.getType().equals("3")){
                rl_type.setBackgroundResource(R.drawable.wy_ydyff);
            }
        }
        MyImageView miv=helper.getView(R.id.miv);
        if(Utils.isNotEmpty(item.getAvatar())){
            GlideUtils.loadImage(mContext,item.getAvatar(),miv);
        }else{
            miv.setImageResource(R.drawable.default_img);
        }




    }
}
