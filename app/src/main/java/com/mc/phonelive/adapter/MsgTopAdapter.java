package com.mc.phonelive.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.FarmilyBean;
import com.mc.phonelive.utils.GlideUtils;

/**
 * Created by Administrator on 2018/8/7.
 */

public class MsgTopAdapter extends BaseQuickAdapter<FarmilyBean, BaseViewHolder> {
    public MsgTopAdapter() {
        super(R.layout.item_msg_top);
    }
    @Override
    protected void convert(BaseViewHolder helper, FarmilyBean item) {
        ImageView iv_pic=helper.getView(R.id.iv_pic);
        TextView tv_title=helper.getView(R.id.tv_title);
        helper.setText(R.id.tv_unread,"3");
        int position=helper.getAdapterPosition();
      switch(position){
          case 0:
              GlideUtils.loadImage(mContext,R.drawable.msg_01,iv_pic);
                tv_title.setText("粉丝");
              break;
          case 1:
              GlideUtils.loadImage(mContext,R.drawable.msg_02,iv_pic);
              tv_title.setText("喜欢");
              break;
          case 2:
              GlideUtils.loadImage(mContext,R.drawable.msg_03,iv_pic);
              tv_title.setText("@我的");
              break;
          case 3:
              GlideUtils.loadImage(mContext,R.drawable.msg_04,iv_pic);
              tv_title.setText("评论");
              break;
          default:

              break;
      }
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
}
