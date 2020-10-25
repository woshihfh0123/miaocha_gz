package com.mc.phonelive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.TxVideoPlayerController;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

import java.lang.reflect.Field;

/**
 * Created by cxf on 2018/12/14.
 * 个人主页视频
 */

public class CenterVideoHomeAdapter extends RefreshAdapter<VideoBean> {

    private View.OnClickListener mOnClickListener;

    public CenterVideoHomeAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                VideoBean bean = mList.get(position);
                if (bean != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, position);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_sb_dt, parent, false));
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
        ImageView mImg;
        TextView mNum;
        TextView mTitle;
        NiceVideoPlayer nice_video_player;
        CardView video_cardview;
        TextView tv_content;
        MyImageView miv;
        TextView tv_username;
        public Vh(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.img);
            nice_video_player=itemView.findViewById(R.id.nice_video_player);
            miv=itemView.findViewById(R.id.miv);
            video_cardview = (CardView) itemView.findViewById(R.id.video_cardview);
            mNum = (TextView) itemView.findViewById(R.id.num);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            mTitle = (TextView) itemView.findViewById(R.id.video_title1);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            itemView.setOnClickListener(mOnClickListener);
            ViewGroup.LayoutParams params = video_cardview.getLayoutParams();
            params.width = CommentUtil.getDisplayWidth(mContext) / 3 - CommentUtil.dip2px(mContext, 10);
//            params.height = (int) (params.width * 1.32);
            params.height = (int) (params.width );
            video_cardview.setLayoutParams(params);
        }

        void setData(VideoBean bean, int position) {
            itemView.setTag(position);
//            ImgLoader.display(bean.getThumb(), mImg);
            GlideUtils.loadImage(mContext,bean.getThumb(),mImg);
            mNum.setText(bean.getViewNum());
            tv_username.setText(bean.getUserBean().getUserNiceName());
            if(Utils.isNotEmpty(bean.getTitle())){
                tv_content.setText(bean.getTitle());
            }
            GlideUtils.loadImage(mContext,bean.getUserBean().getAvatar(),miv);
            if (!DataSafeUtils.isEmpty(bean.getTitle())){
//                mTitle.setText(bean.getTitle());
            }
            nice_video_player.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
            String url=bean.getHref();
            if(Utils.isNotEmpty(url)){
                if(url.contains("https")){
                    url=url.replace("https","http");
                }
                nice_video_player.setUp(url, null);
            }
            TxVideoPlayerController controller = new TxVideoPlayerController(mContext);
            controller.setTitle("");
            try {
                Field sharedFiled = controller.getClass().getDeclaredField("mShare");
                sharedFiled.setAccessible(false);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            controller.imageView().setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            GlideUtils.loadBannerImage(mContext,bean.getThumb(),controller.imageView());
            nice_video_player.setController(controller);
//            if(position==0){
//                nice_video_player.start();
//            }
        }

    }
}
