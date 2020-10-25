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

public class ZbTypeAdapter extends BaseQuickAdapter<FarmilyBean, BaseViewHolder> {
    public ZbTypeAdapter() {
        super(R.layout.item_zb_top);
    }
    @Override
    protected void convert(BaseViewHolder helper, FarmilyBean item) {
        int position=helper.getAdapterPosition();
        TextView tv_title=helper.getView(R.id.tv_title);
        MyImageView miv=helper.getView(R.id.miv);
        switch(position){
            case 0:
                GlideUtils.loadImage(mContext,R.drawable.wy_1,miv);
                tv_title.setText("免费观看");
                break;
            case 1:
                GlideUtils.loadImage(mContext,R.drawable.wy_2,miv);
                tv_title.setText("门票付费");
                break;
            case 2:
                GlideUtils.loadImage(mContext,R.drawable.wy_3,miv);
                tv_title.setText("计时付费");
                break;
            case 3:
                GlideUtils.loadImage(mContext,R.drawable.wy_4,miv);
                tv_title.setText("1对1付费");
                break;
            default:

                break;
        }
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
