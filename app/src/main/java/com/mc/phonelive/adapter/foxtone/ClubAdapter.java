package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.ClubBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 游戏中心
 */
public class ClubAdapter extends BaseQuickAdapter<ClubBean.InfoBean.ListBean, BaseViewHolder> {
    private InfoClubListener mAddListener;
    public ClubAdapter(int layoutResId, @Nullable List<ClubBean.InfoBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClubBean.InfoBean.ListBean item) {
        if (!DataSafeUtils.isEmpty(item.getName())){
            helper.setText(R.id.club_head_name,item.getName());
        }
        if (!DataSafeUtils.isEmpty(item.getCounts())){
            helper.setText(R.id.club_head_people,"人数："+item.getCounts()+"");
        }
        if (!DataSafeUtils.isEmpty(item.getPhone())){
            helper.setText(R.id.club_head_phone,"电话："+item.getPhone()+"");
        }
        if (!DataSafeUtils.isEmpty(item.getBadge())){
            Glide.with(mContext).load(item.getBadge()).into((ImageView) helper.getView(R.id.club_head_img));
        }

        TextView club_head_submit = helper.getView(R.id.club_head_submit);
        club_head_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddListener.addClubData(item.getId());
            }
        });
    }

    public interface  InfoClubListener{
        void addClubData(String id);
    }

    public void AddClubListener(InfoClubListener mListener) {
        this.mAddListener = mListener;
    }
}
