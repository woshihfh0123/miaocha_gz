package com.mc.phonelive.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;

/**
 * Created by Administrator on 2018/8/7.
 */

public class QdAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public QdAdapter() {
        super(R.layout.item_qd);
    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv_ard=helper.getView(R.id.tv_ard);
        ImageView iv_dq=helper.getView(R.id.iv_dq);
        View iv_yqd=helper.getView(R.id.iv_yqd);
        View view_zw=helper.getView(R.id.view_zw);
        int positoin=helper.getAdapterPosition();
        tv_ard.setText((positoin+1)+"");
        if(Utils.isNotEmpty(item)){
            int qdpositon=Integer.parseInt(item);
            if(positoin>(qdpositon-1)){
                iv_yqd.setVisibility(View.INVISIBLE);
                tv_ard.setVisibility(View.VISIBLE);
                iv_dq.setVisibility(View.GONE);
                tv_ard.setTextColor(Color.parseColor("#fd2c55"));
                tv_ard.setBackgroundResource(R.drawable.shape_red_gray);
            }else if(positoin==qdpositon-1){
                iv_yqd.setVisibility(View.VISIBLE);
                tv_ard.setVisibility(View.GONE);
                iv_dq.setVisibility(View.VISIBLE);
                view_zw.setVisibility(View.VISIBLE);
            }else if(positoin<qdpositon-1){
                iv_yqd.setVisibility(View.VISIBLE);
                tv_ard.setVisibility(View.VISIBLE);
                iv_dq.setVisibility(View.GONE);
                tv_ard.setTextColor(Color.parseColor("#ffffff"));
                tv_ard.setBackgroundResource(R.drawable.shape_red_circle);
            }
        }

    }
}
