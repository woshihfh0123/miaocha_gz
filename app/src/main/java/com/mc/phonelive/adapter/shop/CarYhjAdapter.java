package com.mc.phonelive.adapter.shop;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.AddressVO;

import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */

public class CarYhjAdapter extends BaseQuickAdapter<AddressVO.DataBean.InfoBean, BaseViewHolder> {

    public CarYhjAdapter(int layoutResId, @Nullable List<AddressVO.DataBean.InfoBean> data) {
        super(R.layout.yhq_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final AddressVO.DataBean.InfoBean vo) {


    }


}
