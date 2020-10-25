package com.mc.phonelive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.GuardUserBean;
import com.mc.phonelive.bean.LevelBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.IconUtil;
import com.mc.phonelive.utils.WordUtil;

/**
 * Created by cxf on 2018/11/6.
 * 守护列表
 */

public class GuardAdapter extends RefreshAdapter<GuardUserBean> {

    private static final int HEAD = 1;
    private static final int NORMAL = 0;
    private String mVotesName;
    private String mWeekContributeString;//本周贡献
    private boolean mDialog;

    public GuardAdapter(Context context, boolean dialog) {
        super(context);
        mDialog = dialog;
        mVotesName = AppConfig.getInstance().getVotesName();
        mWeekContributeString = WordUtil.getString(R.string.guard_week_con);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD;
        }
        return NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new HeadVh(mInflater.inflate(mDialog ? R.layout.guard_list_head : R.layout.guard_list_head_2, parent, false));
        }
        return new Vh(mInflater.inflate(mDialog ? R.layout.guard_list : R.layout.guard_list_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof HeadVh) {
            ((HeadVh) vh).setData(mList.get(position));
        } else {
            ((Vh) vh).setData(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class HeadVh extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mName;
        ImageView mSex;
        ImageView mLevel;
        TextView mVotes;


        public HeadVh(@NonNull View itemView) {
            super(itemView);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mSex = (ImageView) itemView.findViewById(R.id.sex);
            mLevel = (ImageView) itemView.findViewById(R.id.level);
            mVotes = (TextView) itemView.findViewById(R.id.votes);
        }

        void setData(GuardUserBean bean) {
            if(Utils.isNotEmpty(bean)){
            ImgLoader.displayAvatar(bean.getAvatar(), mAvatar);
            mName.setText(bean.getUserNiceName());
            mSex.setImageResource(IconUtil.getSexIcon(Integer.parseInt(bean.getSex())));
            LevelBean levelBean = AppConfig.getInstance().getLevel(bean.getLevel());
            if (levelBean != null) {
                ImgLoader.display(levelBean.getThumb(), mLevel);
            }
            mVotes.setText(Html.fromHtml(mWeekContributeString + "  <font color='#FF4A2D'>" + bean.getContribute() + "</font>  " + mVotesName));
        }
        }
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mIcon;
        ImageView mAvatar;
        TextView mName;
        ImageView mSex;
        ImageView mLevel;
        TextView mVotes;


        public Vh(@NonNull View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mSex = (ImageView) itemView.findViewById(R.id.sex);
            mLevel = (ImageView) itemView.findViewById(R.id.level);
            mVotes = (TextView) itemView.findViewById(R.id.votes);
        }

        void setData(GuardUserBean bean) {
            ImgLoader.displayAvatar(bean.getAvatar(), mAvatar);
            mName.setText(bean.getUserNiceName());
            mSex.setImageResource(IconUtil.getSexIcon(Integer.parseInt(bean.getSex())));
            LevelBean levelBean = AppConfig.getInstance().getLevel(bean.getLevel());
            if (levelBean != null) {
                ImgLoader.display(levelBean.getThumb(), mLevel);
            }
            mVotes.setText(bean.getContribute() + " " + mVotesName);
            if (bean.getType() == 1) {
                mIcon.setImageResource(R.mipmap.icon_guard_type_0);
            } else {
                mIcon.setImageResource(R.mipmap.icon_guard_type_1);
            }
        }
    }
}
