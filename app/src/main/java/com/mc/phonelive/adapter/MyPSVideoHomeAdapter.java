package com.mc.phonelive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.TxVideoPlayerController;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.MainActivity;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.MyImageView;

import java.lang.reflect.Field;

/**
 * Created by cxf on 2018/12/14.
 * 个人主页视频
 */

public class MyPSVideoHomeAdapter extends RefreshAdapter<VideoBean> {

    private View.OnClickListener mOnClickListener;

    public MyPSVideoHomeAdapter(Context context) {
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
        return new Vh(mInflater.inflate(R.layout.item_psdtvideo_home, parent, false));
    }
//  ((MainActivity) mContext).openCommentWindow(mVideoBean);
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
        //        ImageView mImg;
//        TextView mNum;
//        TextView mTitle;
//        CardView video_cardview;
        MyImageView miv;
        TextView tv_username;
        TextView tv_content;
        TextView tv_date;
        TextView tv_dzl;
        LinearLayout ll_zan;
        LinearLayout ll_pl;
        TextView tv_add_pl;
        LinearLayout ll_item;
        TextView tv_lookmore;
        ImageView iv_z;
        NiceVideoPlayer nice_video_player;

        public Vh(View itemView) {
            super(itemView);
            miv=itemView.findViewById(R.id.miv);
            tv_username=itemView.findViewById(R.id.tv_username);
            tv_content=itemView.findViewById(R.id.tv_content);
            tv_date=itemView.findViewById(R.id.tv_date);
            ll_pl=itemView.findViewById(R.id.ll_pl);
            iv_z=itemView.findViewById(R.id.iv_z);
            tv_add_pl=itemView.findViewById(R.id.tv_add_pl);
            tv_lookmore=itemView.findViewById(R.id.tv_lookmore);
            ll_item=itemView.findViewById(R.id.ll_item);
            tv_dzl=itemView.findViewById(R.id.tv_dzl);
            ll_zan=itemView.findViewById(R.id.ll_zan);
            tv_lookmore=itemView.findViewById(R.id.tv_lookmore);
            nice_video_player=itemView.findViewById(R.id.nice_video_player);


//            mImg = (ImageView) itemView.findViewById(R.id.img);
//            video_cardview = (CardView) itemView.findViewById(R.id.video_cardview);
//            mNum = (TextView) itemView.findViewById(R.id.num);
//            mTitle = (TextView) itemView.findViewById(R.id.video_title1);
//            itemView.setOnClickListener(mOnClickListener);
//            ViewGroup.LayoutParams params = video_cardview.getLayoutParams();
//            params.width = CommentUtil.getDisplayWidth(mContext) / 3 - CommentUtil.dip2px(mContext, 10);
//            params.height = (int) (params.width );
//            video_cardview.setLayoutParams(params);
        }

        void setData(VideoBean bean, int position) {
            itemView.setTag(position);
            ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("vvvvv:",bean.getHref());
                    EventBusUtil.postEvent(new EventBean("send_info_from_click_view",bean,position));

                }
            });
            ll_pl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mContext).openCommentWindow(bean);
                }
            });
            tv_lookmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mContext).openCommentWindow(bean);
                }
            });
            tv_add_pl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mContext).openCommentWindow(bean);
                }
            });

            GlideUtils.loadImage(mContext,bean.getUserBean().getAvatar(),miv);
//            ImgLoader.display(bean.getUserBean().getAvatar(), miv);
            tv_username.setText(bean.getUserBean().getUserNiceName());
            tv_content.setText(bean.getTitle());
            tv_date.setText(bean.getDatetime());
            if(Utils.isNotEmpty(bean.getLikeNum())&&!bean.getLikeNum().equals("0")){

                tv_dzl.setText(bean.getLikeNum()+"人赞过");
                tv_dzl.setVisibility(View.VISIBLE);
            }else{
                tv_dzl.setVisibility(View.VISIBLE);
                tv_dzl.setText("0人赞过");
            }
            ll_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int like=bean.getLike();
                    int likeNum=Integer.parseInt(bean.getLikeNum());
                    int disNum=0;
                    if(like==1){
                        disNum=likeNum+1;
                        bean.setLike(0);
                        iv_z.setImageResource(R.drawable.zan_ic);
                    }else{
                        disNum=likeNum-1;
                        bean.setLike(1);
                        iv_z.setImageResource(R.drawable.zan_red);
                    }
                    bean.setLikeNum(disNum+"");
                    tv_dzl.setText(disNum+"人赞过");
                    clickLike(bean);
                }
            });
            if(bean.getLike()==1){
                iv_z.setImageResource(R.drawable.zan_red);
            }else{
                iv_z.setImageResource(R.drawable.zan_ic);
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
            GlideUtils.loadBannerImage(mContext,bean.getThumb(),controller.imageView());
            nice_video_player.setController(controller);
            if(position==0){
                nice_video_player.start();
            }
//            mVideoView.setVideoController(holder.mController);
        }

    }
//    /**
//     * 显示评论
//     */
//    public void openCommentWindow(VideoBean videoBean) {
//        if (mVideoCommentViewHolder == null) {
//            mVideoCommentViewHolder = new VideoCommentViewHolder(mContext, (ViewGroup) findViewById(R.id.root));
//            mVideoCommentViewHolder.addToParent();
//        }
//        mVideoCommentViewHolder.setVideoBean(videoBean);
//        mVideoCommentViewHolder.showBottom();
//    }
private void clickLike(VideoBean mVideoBean ) {

    HttpUtil.setVideoLike("sss", mVideoBean.getId(), new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {

        }
    });
}

}
