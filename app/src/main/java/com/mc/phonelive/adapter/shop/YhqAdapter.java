package com.mc.phonelive.adapter.shop;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.MyYhqBean;

/**
 * Created by Administrator on 2018/8/7.
 */

public class YhqAdapter extends BaseQuickAdapter<MyYhqBean,BaseViewHolder> {

    public YhqAdapter() {
        super(R.layout.yhq_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyYhqBean item) {
        ImageView iv_left=helper.getView(R.id.iv_left);
        ImageView iv_ygq=helper.getView(R.id.iv_ygq);
        TextView tv_title=helper.getView(R.id.tv_title);
        TextView tv_status=helper.getView(R.id.tv_status);
        if(Utils.isNotEmpty(item)){
            helper.setText(R.id.tv_price,item.getPrice());
            tv_title.setText(item.getName());
            helper.setText(R.id.tv_enddate,item.getEnddate());
            tv_status.setText(item.getStatus_txt());
            if(Utils.isNotEmpty(item.getStatus())){
                if(item.getStatus().equals("0")){
                    iv_left.setImageResource(R.mipmap.sb_yhq);
                    iv_ygq.setVisibility(View.GONE);
                    tv_status.setVisibility(View.VISIBLE);
                    tv_title.setTextColor(Color.parseColor("#333333"));
                }else  if(item.getStatus().equals("1")){
                    iv_ygq.setVisibility(View.VISIBLE);
                    tv_status.setVisibility(View.GONE);
                    iv_ygq.setImageResource(R.mipmap.zgq_ysy);
                    iv_left.setImageResource(R.mipmap.z_gq);
                    tv_title.setTextColor(Color.parseColor("#666666"));
                }else  if(item.getStatus().equals("2")){
                    tv_status.setVisibility(View.GONE);
                    iv_ygq.setVisibility(View.VISIBLE);
                    iv_ygq.setImageResource(R.mipmap.zgq_1);
                    iv_left.setImageResource(R.mipmap.z_gq);
                    tv_title.setTextColor(Color.parseColor("#666666"));
                }
            }
        }


    }

}
