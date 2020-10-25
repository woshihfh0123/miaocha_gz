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
 * 游戏中心
 */
public class FindGameAdapter extends BaseQuickAdapter<MainFindBean.InfoBean.GameBean, BaseViewHolder> {
    public FindGameAdapter(int layoutResId, @Nullable List<MainFindBean.InfoBean.GameBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainFindBean.InfoBean.GameBean item) {
        if (!DataSafeUtils.isEmpty(item.getName())){
            helper.setText(R.id.find_game_name,item.getName());
        }
        if (!DataSafeUtils.isEmpty(item.getNum())){
            helper.setText(R.id.find_game_num,item.getNum()+"人在玩");
        }
        if (!DataSafeUtils.isEmpty(item.getPic())){
            Glide.with(mContext).load(item.getPic()).into((ImageView) helper.getView(R.id.find_game_img));
        }else{
            Glide.with(mContext).load(R.mipmap.game_item_img).into((ImageView) helper.getView(R.id.find_game_img));
        }
        if (!DataSafeUtils.isEmpty(item.getAvator())){
            Glide.with(mContext).load(item.getAvator()).into((ImageView) helper.getView(R.id.find_game_avator));
        }else{
            Glide.with(mContext).load(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.find_game_avator));
        }
    }
}
