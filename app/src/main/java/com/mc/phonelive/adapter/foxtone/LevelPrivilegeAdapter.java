package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.LevelPrivilegeBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 等级特权
 */
public class LevelPrivilegeAdapter extends BaseQuickAdapter<LevelPrivilegeBean.InfoBean, BaseViewHolder> {
    public LevelPrivilegeAdapter(int layoutResId, @Nullable List<LevelPrivilegeBean.InfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LevelPrivilegeBean.InfoBean item) {
        if (!DataSafeUtils.isEmpty(item.getName())){
            helper.setText(R.id.privi_title,item.getName());
        }
        if (!DataSafeUtils.isEmpty(item.getContent())){
            helper.setText(R.id.privi_content, item.getContent());

        }
        if (!DataSafeUtils.isEmpty(item.getThumb())){
            RequestOptions options = new RequestOptions().error(R.drawable.default_img);
            Glide.with(mContext).load(item.getThumb()).apply(options).into((ImageView) helper.getView(R.id.privi_img));
        }else{
            Glide.with(mContext).load(R.drawable.default_img).into((ImageView) helper.getView(R.id.privi_img));
        }

    }
}
