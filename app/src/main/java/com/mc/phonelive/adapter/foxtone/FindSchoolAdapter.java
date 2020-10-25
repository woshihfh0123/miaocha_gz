package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.FindSchoolBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 商学院
 */
public class FindSchoolAdapter extends BaseQuickAdapter<FindSchoolBean, BaseViewHolder> {
    public FindSchoolAdapter(int layoutResId, @Nullable List<FindSchoolBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FindSchoolBean item) {
        if (!DataSafeUtils.isEmpty(item.getPic())){
            Glide.with(mContext).load(item.getPic()).into((ImageView) helper.getView(R.id.find_school_img));
        }else{
            Glide.with(mContext).load(R.drawable.default_img).into((ImageView) helper.getView(R.id.find_school_img));
        }

        if (!DataSafeUtils.isEmpty(item.getTitle())){
            helper.setText(R.id.find_school_name,item.getTitle());
        }
//        if (!DataSafeUtils.isEmpty(item.getTime())){
//            helper.setText(R.id.find_school_time,"报名时间："+item.getTime());
//        }

    }
}
