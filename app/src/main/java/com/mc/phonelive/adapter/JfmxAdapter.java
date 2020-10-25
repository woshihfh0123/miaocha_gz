package com.mc.phonelive.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.ScoreItemBean;

/**
 * Created by Administrator on 2018/8/7.
 */

public class JfmxAdapter extends BaseQuickAdapter<ScoreItemBean, BaseViewHolder> {
    public JfmxAdapter() {
        super(R.layout.item_jf_mx);
    }
    @Override
    protected void convert(BaseViewHolder helper, ScoreItemBean item) {
        helper.setText(R.id.tv_note,item.getNote());
        helper.setText(R.id.tv_addtime,item.getAddtime());
//        helper.setText(R.id.tv_score,item.getScore());
        TextView tv_score=helper.getView(R.id.tv_score);
        String type=item.getType();
        if(Utils.isNotEmpty(type)&&type.equals("1")){//
            tv_score.setText("+"+item.getScore()+"积分");
            tv_score.setTextColor(Color.parseColor("#222222"));
        }else{
            tv_score.setText("-"+item.getScore()+"积分");
            tv_score.setTextColor(Color.parseColor("#EC1C24"));
        }
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
