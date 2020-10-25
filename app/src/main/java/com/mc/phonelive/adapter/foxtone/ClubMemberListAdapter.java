package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.ClubMemberBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 俱乐部成员
 */
public class ClubMemberListAdapter extends BaseQuickAdapter<ClubMemberBean.InfoBean, BaseViewHolder> {
    private String mFounder="";
    InfoClubListener mClubListener;
    public ClubMemberListAdapter(int layoutResId, @Nullable List<ClubMemberBean.InfoBean> data,String founder) {
        super(layoutResId, data);
        this.mFounder =founder;
    }

    @Override
    protected void convert(BaseViewHolder helper, ClubMemberBean.InfoBean item) {
        if (!DataSafeUtils.isEmpty(item.getMobile())) {
            helper.setText(R.id.item_username, item.getMobile());
        }
        if (!DataSafeUtils.isEmpty(item.getAvatar_thumb())) {
            Glide.with(mContext).load(item.getAvatar_thumb()).into((ImageView) helper.getView(R.id.item_avator));
        }
        if (!DataSafeUtils.isEmpty(item.getGrade_id())) {
            helper.setText(R.id.item_level, "Lv." + item.getGrade_id());
        }
        if (!DataSafeUtils.isEmpty(item.getArea())) {
            helper.setText(R.id.item_level_name, "" + item.getArea());
        }else{
            helper.setText(R.id.item_level_name, "无");
        }

        LinearLayout btn_layout = helper.getView(R.id.btn_layout);
        if (this.mFounder.equals("1")){
            btn_layout.setVisibility(View.VISIBLE);
        }else{
            btn_layout.setVisibility(View.GONE);
        }
        btn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClubListener.delClubData(item.getId(),item.getUid());
            }
        });
    }

    public interface  InfoClubListener{
        void delClubData(String id,String touid);
    }

    public void DelClubListener(InfoClubListener mListener) {
        this.mClubListener = mListener;
    }

}
