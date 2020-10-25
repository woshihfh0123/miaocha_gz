package com.mc.phonelive.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.QbBean;

/**
 * Created by Administrator on 2018/8/7.
 */

public class GvCheckOneItAdapter extends BaseQuickAdapter<QbBean.RulesBean, BaseViewHolder> {

    public GvCheckOneItAdapter() {
        super(R.layout.item_ts_jb);
    }
    private int clickTemp = -1;
//       gvAdapter.setSeclection(position);
    public void setSeclection(int position) {
        clickTemp = position;
    }
    @Override
    protected void convert(BaseViewHolder helper, QbBean.RulesBean item) {
        LinearLayout ll_gd=helper.getView(R.id.ll_gd);
        ImageView iv_check=helper.getView(R.id.iv_check);
        TextView tv_more=helper.getView(R.id.tv_more);
        RelativeLayout rl_bj=helper.getView(R.id.rl_bj);
        if(item.getId().equals("99999")){
            ll_gd.setVisibility(View.GONE);
            iv_check.setVisibility(View.GONE);
            tv_more.setVisibility(View.VISIBLE);
            rl_bj.setBackgroundResource(R.drawable.shape_qb_gray);
        }else{
            ll_gd.setVisibility(View.VISIBLE);
            tv_more.setVisibility(View.GONE);
            TextView tvname=helper.getView(R.id.tv_name);
            int pos = helper.getLayoutPosition();
            tvname.setText(item.getMoney());
            helper.setText(R.id.tv_t_name,item.getCoin());
            if(pos==clickTemp){
                rl_bj.setBackgroundResource(R.drawable.shape_qb_red);
                iv_check.setVisibility(View.VISIBLE);
            }else{
                rl_bj.setBackgroundResource(R.drawable.shape_qb_gray);
                iv_check.setVisibility(View.GONE);
            }
        }


    }
}
