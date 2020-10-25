package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.MyTeamBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 任务大厅
 */
public class MyTeamListAdapter extends BaseQuickAdapter<MyTeamBean.InfoBean.TeamList, BaseViewHolder> {
    public MyTeamListAdapter(int layoutResId, @Nullable List<MyTeamBean.InfoBean.TeamList> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyTeamBean.InfoBean.TeamList item) {
        if (!DataSafeUtils.isEmpty(item.getMobile())) {
            helper.setText(R.id.item_username, item.getMobile());
        }
        if (!DataSafeUtils.isEmpty(item.getAvatar())) {
            Glide.with(mContext).load(item.getAvatar()).into((ImageView) helper.getView(R.id.item_avator));
        }
        if (!DataSafeUtils.isEmpty(item.getGrade_id())) {
            helper.setText(R.id.item_level, "Lv." + item.getGrade_id());
        }
        if (!DataSafeUtils.isEmpty(item.getFees())) {
            helper.setText(R.id.item_yfz, "" + item.getFees());
        }
    }
}
