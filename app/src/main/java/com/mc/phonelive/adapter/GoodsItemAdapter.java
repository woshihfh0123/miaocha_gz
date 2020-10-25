package com.mc.phonelive.adapter;

import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.GoodsItemBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class GoodsItemAdapter extends BaseQuickAdapter<GoodsItemBean, BaseViewHolder> {
    public GoodsItemAdapter() {
        super(R.layout.item_goods);
    }
    @Override
    protected void convert(BaseViewHolder helper, GoodsItemBean item) {
        MyImageView miv=helper.getView(R.id.miv);
        ImageView iv_check=helper.getView(R.id.iv_check);
        helper.setText(R.id.tv_goods_name,item.getGoods_name());
        helper.setText(R.id.tv_price,"¥"+item.getPrice());
        TextView tv_ord_price=helper.getView(R.id.tv_ord_price);
        tv_ord_price.setText("原价：¥"+item.getOt_price());
        tv_ord_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if(Utils.isNotEmpty(item.getImg_list())){
            GlideUtils.loadImage(mContext,item.getImg_list().get(0),miv);
        }
        boolean isCheck = item.isCheck();
        helper.addOnClickListener(R.id.iv_check);
        if(isCheck){
            iv_check.setImageResource(R.drawable.check_y);
        }else{
            iv_check.setImageResource(R.drawable.check_u);
        }
    }
}
