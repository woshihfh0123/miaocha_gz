//package com.zhiboshow.phonelive.im;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.zhiboshow.phonelive.Constants;
//import com.zhiboshow.phonelive.R;
//import com.zhiboshow.phonelive.bean.VideoChooseBean;
//import com.zhiboshow.phonelive.bean.VideoChooseBean;
//import com.zhiboshow.phonelive.glide.ImgLoader;
//
//import java.io.File;
//import java.util.List;
//
//import cn.jpush.im.android.api.content.VideoContent;
//
///**
// * Created by cxf on 2018/6/20.
// * 聊天时候选择视频的Adapter
// */
//
//public class ImChatChooseVideoAdapter extends RecyclerView.Adapter<ImChatChooseVideoAdapter.Vh> {
//
//    private static final int POSITION_NONE = -1;
//    private List<VideoContent> mList;
//    private LayoutInflater mInflater;
//    private int mSelectedPosition;
//
//    public ImChatChooseVideoAdapter(Context context, List<VideoContent> list) {
//        mList = list;
//        mInflater = LayoutInflater.from(context);
//        mSelectedPosition = POSITION_NONE;
//    }
//
//    @Override
//    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new Vh(mInflater.inflate(R.layout.item_chat_choose_img, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(Vh vh, int position) {
//
//    }
//
//    @Override
//    public void onBindViewHolder(Vh vh, int position, List<Object> payloads) {
//        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
//        vh.setData(mList.get(position), position, payload);
//    }
//
//    public File getSelectedFile() {
//        if (mSelectedPosition != POSITION_NONE) {
//            Log.v("tags","file----"+mList.get(mSelectedPosition).getVideoPath());
//            File file = new File(mList.get(mSelectedPosition).getVideoPath());
//            return file;
//        }
//        return null;
//    }
//
//    @Override
//    public int getItemCount() {
//        return mList.size();
//    }
//
//    class Vh extends RecyclerView.ViewHolder {
//
//        ImageView mCover;
//        ImageView mImg;
//        VideoContent mBean;
//        int mPosition;
//
//        public Vh(View itemView) {
//            super(itemView);
//            mCover = (ImageView) itemView.findViewById(R.id.cover);
//            mImg = (ImageView) itemView.findViewById(R.id.img);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mPosition == mSelectedPosition) {
//                        return;
//                    }
//                    if (mSelectedPosition == POSITION_NONE) {
//                        mBean.setmChecked(true);
//                        notifyItemChanged(mPosition, Constants.PAYLOAD);
//                    } else {
//                        mList.get(mSelectedPosition).setmChecked(false);
//                        mBean.setmChecked(true);
//                        notifyItemChanged(mSelectedPosition, Constants.PAYLOAD);
//                        notifyItemChanged(mPosition, Constants.PAYLOAD);
//                    }
//                    mSelectedPosition = mPosition;
//                }
//            });
//        }
//
//        void setData(VideoContent bean, int position, Object payload) {
//            mBean = bean;
//            mPosition = position;
//            if (payload == null) {
//                Log.v("tags","getVideoPath----"+bean.getFileName());
//                ImgLoader.display(bean.getFileName(), mCover);
//            }
//            if (bean.ismChecked()) {
//                mImg.setImageResource(R.mipmap.icon_checked);
//            } else {
//                mImg.setImageResource(R.mipmap.icon_checked_none);
//            }
//        }
//    }
//
//}
