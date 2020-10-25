package com.mc.phonelive.adapter.shop;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.LevelBean;
import com.mc.phonelive.bean.SearchUserBean;
import com.mc.phonelive.custom.MyRadioButton;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.IconUtil;

import java.util.List;

/**
 * created by WWL on 2019/1/5 0005:14
 * 用户管理
 */
public class RoomMangerAdapter extends BaseQuickAdapter<SearchUserBean, BaseViewHolder> {
    private Context mContext;

    public RoomMangerAdapter(Context context, int layoutResId, @Nullable List<SearchUserBean> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final SearchUserBean bean) {
        ImageView mAvatar ;
        TextView mName;
        TextView mSign;
        ImageView mSex;
        ImageView mLevelAnchor;
        ImageView mLevel;
        MyRadioButton mBtnFollow;

        mAvatar = (ImageView) helper.getView(R.id.avatar);
        mName = (TextView) helper.getView(R.id.name);
        mSign = (TextView) helper.getView(R.id.sign);
        mSex = (ImageView) helper.getView(R.id.sex);
        mLevelAnchor = (ImageView) helper.getView(R.id.level_anchor);
        mLevel = (ImageView) helper.getView(R.id.level);
        mBtnFollow = (MyRadioButton) helper.getView(R.id.btn_follow);
        if (bean!=null){
            ImgLoader.displayAvatar(bean.getAvatar(), mAvatar);
            mName.setText(bean.getUserNiceName());
            mSign.setText(bean.getSignature());
            mSex.setImageResource(IconUtil.getSexIcon(Integer.parseInt(bean.getSex())));
            LevelBean anchorLevelBean = AppConfig.getInstance().getAnchorLevel(bean.getLevelAnchor());
            if (anchorLevelBean != null) {
                ImgLoader.display(anchorLevelBean.getThumb(), mLevelAnchor);
            }
            LevelBean levelBean = AppConfig.getInstance().getLevel(bean.getLevel());
            if (levelBean != null) {
                ImgLoader.display(levelBean.getThumb(), mLevel);
            }
        }
        mBtnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }




}
