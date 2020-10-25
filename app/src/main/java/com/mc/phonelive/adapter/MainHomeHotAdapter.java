package com.mc.phonelive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.MyShopDetailActivity;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.IconUtil;

import java.util.List;

/**
 * Created by cxf on 2018/9/26.
 * 直播列表
 */

public class MainHomeHotAdapter extends RefreshAdapter<LiveBean> {

    private static final int FIRST_LINE = 1;
    private static final int NORMAL = 0;

    private View.OnClickListener mOnClickListener;

    public MainHomeHotAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!canClick()){
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
        if (Integer.parseInt(mList.get(position).getGoods_count())>0) {
            return NORMAL;
        } else {
            return FIRST_LINE;
        }

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FIRST_LINE) {
            return new Vh(mInflater.inflate(R.layout.item_main_live_live, parent, false));
        } else {
            return new Vh(mInflater.inflate(R.layout.item_main_live_live_2, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
//        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position), position);
//        } else {
//            ((Adv) vh).setData(mList.get(position), position);
//        }
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView mCover;
        ImageView mAvatar;
        TextView mName;
        TextView mTitle;
        TextView mNum;
        ImageView mType;

        TextView adv_num;
        RecyclerView live_goods_recyclerView;
        BaseQuickAdapter<LiveBean.GoodsListBean, BaseViewHolder> mLiveGoodsAdapter;

        public Vh(View itemView) {
            super(itemView);
            mCover = (ImageView) itemView.findViewById(R.id.cover);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mNum = (TextView) itemView.findViewById(R.id.num);
            mType = (ImageView) itemView.findViewById(R.id.type);
            itemView.setOnClickListener(mOnClickListener);

            adv_num =itemView.findViewById(R.id.adv_num);
            live_goods_recyclerView =itemView.findViewById(R.id.live_goods_recyclerView);
        }

        void setData(LiveBean bean, int position) {
            itemView.setTag(position);
            ImgLoader.display(bean.getThumb(), mCover);
            ImgLoader.display(bean.getAvatar(), mAvatar);
            mName.setText(bean.getUserNiceName());
            if (TextUtils.isEmpty(bean.getTitle())) {
                if (mTitle.getVisibility() == View.VISIBLE) {
                    mTitle.setVisibility(View.GONE);
                }
            } else {
                if (mTitle.getVisibility() != View.VISIBLE) {
                    mTitle.setVisibility(View.VISIBLE);
                }
                mTitle.setText(bean.getTitle());
            }
            mNum.setText(bean.getNums());
            mType.setImageResource(IconUtil.getLiveTypeIcon(bean.getType()));

            if (Integer.parseInt(bean.getGoods_count())>0){
                adv_num.setText(bean.getGoods_count()+"件\n直播购");
                setLiveGoodsAdapter(live_goods_recyclerView,bean.getGoods_list(),bean.getUid());
            }
        }
        public void setLiveGoodsAdapter(RecyclerView live_goods_recyclerView, List<LiveBean.GoodsListBean> goods_list,String uid){
            mLiveGoodsAdapter = new BaseQuickAdapter<LiveBean.GoodsListBean, BaseViewHolder>(R.layout.live_adv_item_item_layout,goods_list) {
                @Override
                protected void convert(BaseViewHolder helper, LiveBean.GoodsListBean item) {
                    ImageView imgs = (ImageView) helper.getView(R.id.live_adv_item_img);
                    if (!DataSafeUtils.isEmpty(item.getGoods_img())){
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imgs.getLayoutParams();
                        params.width = CommentUtil.getDisplayWidth(mContext)/8-CommentUtil.dip2px(mContext,5);
                        params.height = params.width;
                        imgs.setLayoutParams(params);
                        Glide.with(mContext).load(item.getGoods_img()).into(imgs);
                    }
                }
            };
            live_goods_recyclerView.setAdapter(mLiveGoodsAdapter);
            live_goods_recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
            mLiveGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    LiveBean.GoodsListBean bean = mLiveGoodsAdapter.getData().get(position);
                    if (bean!=null){
                        Intent  intent = new Intent(mContext,MyShopDetailActivity.class);
                        intent.putExtra("id",bean.getId());
                        intent.putExtra("status","1");
                        intent.putExtra("store_id",bean.getStore_id());
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

//    class Adv extends RecyclerView.ViewHolder {
//    TextView adv_num;
//    RecyclerView live_goods_recyclerView;
//    BaseQuickAdapter<LiveBean.GoodsListBean, BaseViewHolder> mLiveGoodsAdapter;
//        public Adv(View itemView) {
//            super(itemView);
//            adv_num =itemView.findViewById(R.id.adv_num);
//            live_goods_recyclerView =itemView.findViewById(R.id.live_goods_recyclerView);
//        }
//
//        void setData(LiveBean bean, int position) {
//            itemView.setTag(position);
//            adv_num.setText(bean.getGoods_count()+"件\n直播购");
//            setLiveGoodsAdapter(live_goods_recyclerView,bean.getGoods_list(),bean.getUid());
//        }
//        public void setLiveGoodsAdapter(RecyclerView live_goods_recyclerView, List<LiveBean.GoodsListBean> goods_list,String uid){
//            mLiveGoodsAdapter = new BaseQuickAdapter<LiveBean.GoodsListBean, BaseViewHolder>(R.layout.live_adv_item_item_layout,goods_list) {
//                @Override
//                protected void convert(BaseViewHolder helper, LiveBean.GoodsListBean item) {
//                    ImageView imgs = (ImageView) helper.getView(R.id.live_adv_item_img);
//                    if (!DataSafeUtils.isEmpty(item.getGoods_img())){
//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imgs.getLayoutParams();
//                        params.width = CommentUtil.getDisplayWidth(mContext)/8-CommentUtil.dip2px(mContext,5);
//                        params.height = params.width;
//                        imgs.setLayoutParams(params);
//                        Glide.with(mContext).load(item.getGoods_img()).into(imgs);
//                    }
//                }
//            };
//            live_goods_recyclerView.setAdapter(mLiveGoodsAdapter);
//            live_goods_recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
//            mLiveGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    LiveBean.GoodsListBean bean = mLiveGoodsAdapter.getData().get(position);
//                    if (bean!=null){
//                        Intent  intent = new Intent(mContext,MyShopDetailActivity.class);
//                        intent.putExtra("id",bean.getId());
//                        intent.putExtra("status","1");
//                        intent.putExtra("store_id",bean.getStore_id());
//                        mContext.startActivity(intent);
//                    }
//                }
//            });
//        }
//    }
}
