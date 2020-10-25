package com.mc.phonelive.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.shop.OrderLogisticsActivity;
import com.mc.phonelive.bean.OrderListBean;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.mc.phonelive.utils.GlideUtils;

/**
 * Created by Administrator on 2018/8/7.
 */

public class OrderScoreAdapter extends BaseQuickAdapter<OrderListBean, BaseViewHolder> {
    public OrderScoreAdapter() {
        super(R.layout.item_order_score);
    }
    @Override
    protected void convert(BaseViewHolder helper, OrderListBean item) {
        helper.setText(R.id.tv_orderno,"订单号:"+item.getOrderno());
        helper.setText(R.id.tv_addtime,item.getAddtime());
        helper.setText(R.id.food_name,item.getGoods_title());
        String str3 = "<font><small><small>￥</small></small></font>" + item.getGoods_price()+"<font><small><small>积分</small></small></font>" ;
        helper.setText(R.id.food_newprice,Html.fromHtml(str3));
        helper.setText(R.id.food_num,"x"+item.getGoods_num());
        helper.setText(R.id.food_name,item.getGoods_title());
        ImageView food_img = helper.getView(R.id.food_img);
        if(Utils.isNotEmpty(item.getGoods_pic())){
            GlideUtils.loadImage(mContext,item.getGoods_pic(),food_img);
        }else{
            food_img.setImageResource(R.drawable.default_img);
        }
        TextView tv_status=helper.getView(R.id.tv_status);
        TextView tv_wl = helper.getView(R.id.tv_wl);
        String status=item.getStatus();
        if(Utils.isNotEmpty(status)&&status.equals("1")){
            tv_status.setText("确认收货");
            tv_status.setTextColor(Color.parseColor("#ff0000"));
            tv_status.setBackgroundResource(R.drawable.red_null_bg);
            tv_wl.setText("查看物流");
            tv_wl.setVisibility(View.VISIBLE);
        }else if(Utils.isNotEmpty(status)&&status.equals("2")){
            tv_status.setText("查看物流");
            tv_status.setTextColor(Color.parseColor("#666666"));
            tv_status.setBackgroundResource(R.drawable.gray_null_bg);
            tv_wl.setVisibility(View.GONE);
        }

        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNotEmpty(status)&&status.equals("2")){
                    Intent intent = new Intent(mContext, OrderLogisticsActivity.class);
                    String morderid = item.getId();
                    intent.putExtra("order_id", morderid);
                    intent.putExtra("type","jf");
                    mContext.startActivity(intent);
//                    EventBusUtil.postEvent(new EventBean("LookWL",item.getId()));
                }else{
                    EventBusUtil.postEvent(new EventBean("MalltakeOrder",item.getId()));
                }

            }
        });
        tv_wl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EventBusUtil.postEvent(new EventBean("LookWL",item.getId()));
                Intent intent = new Intent(mContext, OrderLogisticsActivity.class);
                intent.putExtra("order_id",item.getId());
                intent.putExtra("type","jf");
                mContext.startActivity(intent);
//                EventBusUtil.postEvent(new EventBean("MalllogisticsDetail",item.getId()));
//                EventBusUtil.postEvent(new EventBean("LookWL",item.getId()));
            }
        });
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
