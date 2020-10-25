package com.mc.phonelive.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.MyTeamVO;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * created by WWL on 2019/6/27 0027:20
 * 我的团队
 */
public class MyTeamAdapter extends BaseQuickAdapter<MyTeamVO.DataBean.ListBean, BaseViewHolder> {

    public MyTeamAdapter(int layoutResId, @Nullable List<MyTeamVO.DataBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyTeamVO.DataBean.ListBean item) {
        if (!DataSafeUtils.isEmpty(item.getPhone())){
          helper.setText(R.id.user_name_tv,""+item.getNickname());
        }
        if (!DataSafeUtils.isEmpty(item.getAdd_time())){
            helper.setText(R.id.user_time_tv,""+item.getAdd_time());
        }
        if (!DataSafeUtils.isEmpty(item.getPrice())){
            helper.setText(R.id.user_ordermoney_tv,item.getPrice()+"");
        }
        if (!DataSafeUtils.isEmpty(item.getPrice())){
            helper.setText(R.id.user_money_tv,item.getPrice()+"元");
        }
    }
}
