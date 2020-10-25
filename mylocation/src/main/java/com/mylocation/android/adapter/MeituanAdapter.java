package com.mylocation.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mylocation.android.R;
import com.mylocation.android.model.MeiTuanBean;

import java.util.List;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class MeituanAdapter extends CommonAdapter<MeiTuanBean> {
    public MeituanAdapter(Context context, int layoutId, List<MeiTuanBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, MeiTuanBean meiTuanBean) {
        holder.setText(R.id.tvCity, meiTuanBean.getCity());
    }

}