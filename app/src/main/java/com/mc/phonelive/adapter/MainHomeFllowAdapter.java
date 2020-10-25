//package com.zhiboshow.phonelive.adapter;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.zhiboshow.phonelive.R;
//import com.zhiboshow.phonelive.bean.LiveBean;
//import com.zhiboshow.phonelive.bean.VideoBean;
//import com.zhiboshow.phonelive.glide.ImgLoader;
//import com.zhiboshow.phonelive.utils.DataSafeUtils;
//import com.zhiboshow.phonelive.utils.IconUtil;
//
///**
// * Created by cxf on 2018/9/26.
// * 直播列表
// */
//
//public class MainHomeFllowAdapter extends RefreshAdapter<VideoBean> {
//
//    private static final int FIRST_LINE = 1;
//    private static final int NORMAL = 0;
//
//    private View.OnClickListener mOnClickListener;
//
//    public MainHomeFllowAdapter(Context context) {
//        super(context);
//        mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!canClick()){
//                    return;
//                }
//                Object tag = v.getTag();
//                if (tag != null) {
//                    int position = (int) tag;
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(mList.get(position), position);
//                    }
//                }
//            }
//        };
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (mList.get(position).get().equals("1")) {
//            return NORMAL;
//        } else {
//            return FIRST_LINE;
//        }
//
//    }
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == FIRST_LINE) {
//            return new Vh(mInflater.inflate(R.layout.item_main_home_video, parent, false));
//        } else {
//            return new Adv(mInflater.inflate(R.layout.video_adv_item_layout, parent, false));
//        }
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
//
//        if (vh instanceof Vh) {
//            ((Vh) vh).setData(mList.get(position), position);
//        } else {
//            ((Adv) vh).setData(mList.get(position), position);
//        }
//    }
//
//    class Vh extends RecyclerView.ViewHolder {
//        ImageView mCover;
//        ImageView mAvatar;
//        TextView mName;
//        TextView mTitle;
//        TextView mNum;
//        ImageView mType;
//
//        public Vh(View itemView) {
//            super(itemView);
//            mCover = (ImageView) itemView.findViewById(R.id.cover);
//            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
//            mName = (TextView) itemView.findViewById(R.id.name);
//            mTitle = (TextView) itemView.findViewById(R.id.title);
//            mNum = (TextView) itemView.findViewById(R.id.num);
//            mType = (ImageView) itemView.findViewById(R.id.type);
//            itemView.setOnClickListener(mOnClickListener);
//        }
//
//        void setData(VideoBean bean, int position) {
//            itemView.setTag(position);
//            ImgLoader.display(bean.getThumb(), mCover);
//            ImgLoader.display(bean.getAvatar(), mAvatar);
//            mName.setText(bean.getUserNiceName());
//            if (TextUtils.isEmpty(bean.getTitle())) {
//                if (mTitle.getVisibility() == View.VISIBLE) {
//                    mTitle.setVisibility(View.GONE);
//                }
//            } else {
//                if (mTitle.getVisibility() != View.VISIBLE) {
//                    mTitle.setVisibility(View.VISIBLE);
//                }
//                mTitle.setText(bean.getTitle());
//            }
//            mNum.setText(bean.getNums());
//            mType.setImageResource(IconUtil.getLiveTypeIcon(bean.getType()));
//        }
//    }
//    class Adv extends RecyclerView.ViewHolder {
//    TextView adv_num;
//    RecyclerView live_goods_recyclerView;
//    BaseQuickAdapter<VideoBean, BaseViewHolder> mLiveGoodsAdapter;
//        public Adv(View itemView) {
//            super(itemView);
//            adv_num =itemView.findViewById(R.id.adv_num);
//            live_goods_recyclerView =itemView.findViewById(R.id.live_goods_recyclerView);
//        }
//
//        void setData(LiveBean bean, int position) {
//            itemView.setTag(position);
//            adv_num.setText(bean.getNums()+"件\n直播购");
//            setLiveGoodsAdapter(live_goods_recyclerView);
//        }
//        public void setLiveGoodsAdapter(RecyclerView live_goods_recyclerView){
//            mLiveGoodsAdapter = new BaseQuickAdapter<VideoBean, BaseViewHolder>(R.layout.live_adv_item_item_layout,mList) {
//                @Override
//                protected void convert(BaseViewHolder helper, LiveBean item) {
//                    if (!DataSafeUtils.isEmpty(item.getThumb())){
//                        Glide.with(mContext).load(item.getThumb()).into((ImageView) helper.getView(R.id.live_adv_item_img));
//                    }
//                }
//            };
//            live_goods_recyclerView.setAdapter(mLiveGoodsAdapter);
//            live_goods_recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
//        }
//    }
//}
