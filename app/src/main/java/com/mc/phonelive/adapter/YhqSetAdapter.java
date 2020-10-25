package com.mc.phonelive.adapter;

import com.bspopupwindow.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.YhqSelBean;

/**
 * Created by Administrator on 2018/8/7.
 */

public class YhqSetAdapter extends BaseQuickAdapter<YhqSelBean, BaseViewHolder> {

    public YhqSetAdapter() {
        super(R.layout.item_set_yhq);
    }

    @Override
    protected void convert(BaseViewHolder helper, final YhqSelBean item) {
        if(Utils.isNotEmpty(item)){
            helper.setText(R.id.tv_price,item.getPrice());
            helper.setText(R.id.tv_title,item.getName());
            helper.setText(R.id.tv_msg,item.getEnddate());
//          TextView tv_lq=helper.getView(R.id.tv_lq);
//            String is_get=item.getIs_get();
//            if(Utils.isNotEmpty(is_get)&&is_get.equals("1")){
//               tv_lq.setText("已领取");
//                tv_lq.setTextColor(mContext.getResources().getColor(R.color.gray));
//                tv_lq.setBackgroundResource(R.drawable.gray_null_bg);
//            }else{
//                tv_lq.setText("立即领取");
//                tv_lq.setTextColor(mContext.getResources().getColor(R.color.red));
//                tv_lq.setBackgroundResource(R.drawable.red_null_bg);
//            }
            helper.addOnClickListener(R.id.tv_lq);
        }


    }
}
