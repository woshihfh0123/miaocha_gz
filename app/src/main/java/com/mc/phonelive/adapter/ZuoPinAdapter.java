package com.mc.phonelive.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;

/**
 * Created by Administrator on 2018/8/7.
 */

public class ZuoPinAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ZuoPinAdapter() {
        super(R.layout.item_zuo_pin);
    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {
//        ImageView iv_pic=helper.getView(R.id.iv_pic);
//        if(Utils.isNotEmpty(item.getPic1())){
//          String bgcover=  Constant.BK_URL_SAME+item.getPic1();
//            int width= (int) ((CommentUtil.getDisplayWidth(mContext)-CommentUtil.dpToPx(8))/3);
//            GlideUtils.loadImageSize(mContext,bgcover,iv_pic,width,width);
//        }
//        if(Utils.isNotEmpty(item.getLoves())){
//            helper.setText(R.id.tv_dz_num,item.getLoves()+"");
//        }
    }
}
