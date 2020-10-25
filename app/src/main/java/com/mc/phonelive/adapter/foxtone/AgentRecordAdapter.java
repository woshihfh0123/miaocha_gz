package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.MessageBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 申请记录
 */
public class AgentRecordAdapter extends BaseQuickAdapter<MessageBean.InfoBean, BaseViewHolder> {
    public AgentRecordAdapter(int layoutResId, @Nullable List<MessageBean.InfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean.InfoBean item) {
        if (!DataSafeUtils.isEmpty(item.getMobile())){
            helper.setText(R.id.item_name,item.getMobile());
        }
        if (!DataSafeUtils.isEmpty(item.getAvatar())){
            Glide.with(mContext).load(item.getAvatar()).into((ImageView) helper.getView(R.id.item_avator));
        }
        if (!DataSafeUtils.isEmpty(item.getCreate_time())){
            helper.setText(R.id.item_time,""+item.getCreate_time());
        }
    }
}
