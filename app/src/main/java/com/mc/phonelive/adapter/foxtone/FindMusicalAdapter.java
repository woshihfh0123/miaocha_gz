package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.MainFindBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 乐器中心
 */
public class FindMusicalAdapter extends BaseQuickAdapter<MainFindBean.InfoBean.ListBean, BaseViewHolder> {
    public FindMusicalAdapter(int layoutResId, @Nullable List<MainFindBean.InfoBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainFindBean.InfoBean.ListBean item) {
        if (!DataSafeUtils.isEmpty(item.getName())){
            helper.setText(R.id.find_music_name,item.getName());
        }
        if (!DataSafeUtils.isEmpty(item.getFee())){
            helper.setText(R.id.find_music_price,"价格"+item.getFee());
        }
        if (!DataSafeUtils.isEmpty(item.getOutput())){
            helper.setText(R.id.find_music_num,"产量"+item.getOutput());
        }
        if (!DataSafeUtils.isEmpty(item.getDays())){
            helper.setText(R.id.find_music_cycle,"周期"+item.getDays());
        }
        if (!DataSafeUtils.isEmpty(item.getThumb())){
            Glide.with(mContext).load(item.getThumb()).into((ImageView) helper.getView(R.id.find_music_img));
        }else{
            Glide.with(mContext).load(R.drawable.default_img).into((ImageView) helper.getView(R.id.find_music_img));
        }
    }
}
