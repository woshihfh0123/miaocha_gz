package com.mc.phonelive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.MyShopDetailActivity;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.DataSafeUtils;

/**
 * Created by cxf on 2018/9/26.
 * 首页视频
 */

public class MainHomeVideoAdapter extends RefreshAdapter<VideoBean> {

    private static final int FIRST_LINE = 1;
    private static final int NORMAL = 0;

    private View.OnClickListener mOnClickListener;

    public MainHomeVideoAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mList.get(position), position);
                    }
                }
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getIs_goods().equals("1")) {
            return NORMAL;
        } else {
            return FIRST_LINE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FIRST_LINE) {
            return new Vh(mInflater.inflate(R.layout.item_main_home_video, parent, false));
        } else {
            return new Vh(mInflater.inflate(R.layout.item_main_home_video_2, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    /**
     * 删除视频
     */
    public void deleteVideo(String videoId) {
        if (TextUtils.isEmpty(videoId)) {
            return;
        }
        for (int i = 0, size = mList.size(); i < size; i++) {
            if (videoId.equals(mList.get(i).getId())) {
                notifyItemRemoved(i);
                break;
            }
        }
    }


    class Vh extends RecyclerView.ViewHolder {

        ImageView mCover;
        ImageView mAvatar;
        TextView mName;
        TextView mTitle;
        TextView mNum;

        ImageView mAdvImg;
        TextView mAdvName;
        LinearLayout adv_layout;

        public Vh(View itemView) {
            super(itemView);
            mCover = (ImageView) itemView.findViewById(R.id.cover);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mNum = (TextView) itemView.findViewById(R.id.num);
            itemView.setOnClickListener(mOnClickListener);

            mAdvImg = (ImageView) itemView.findViewById(R.id.adv_img);
            mAdvName = (TextView) itemView.findViewById(R.id.adv_title);
            adv_layout = itemView.findViewById(R.id.adv_layout);
        }

        void setData(VideoBean bean, int position) {
            itemView.setTag(position);
            ImgLoader.display(bean.getThumb(), mCover);
            mTitle.setText(bean.getTitle());
            mNum.setText(bean.getViewNum());
            UserBean userBean = bean.getUserBean();
            if (userBean != null) {
                ImgLoader.display(userBean.getAvatar(), mAvatar);
                mName.setText(userBean.getUserNiceName());
            }

            if (bean.getIs_goods().equals("1")) {
                if (!DataSafeUtils.isEmpty(bean.getGoods_info().getGoods_img()))
                    ImgLoader.display(bean.getGoods_info().getGoods_img(), mAdvImg);
                else
                    ImgLoader.display(R.mipmap.ic_launcher, mAdvImg);
                if (!DataSafeUtils.isEmpty(bean.getGoods_info().getTitle())) {
                    mAdvName.setText(bean.getGoods_info().getTitle());
                }else {
                    mAdvName.setText("");
                }
                adv_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, MyShopDetailActivity.class);
                        intent.putExtra("id", bean.getGoods_info().getId());
                        intent.putExtra("status", "1");
                        intent.putExtra("store_id", bean.getUid());
                        mContext.startActivity(intent);

                    }
                });
            }
        }
    }

}
