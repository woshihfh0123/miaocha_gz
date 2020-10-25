package com.mc.phonelive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.MyShopDetailActivity;
import com.mc.phonelive.bean.ShopMallGoodsList;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.util.List;

/**
 * Created by cxf on 2018/9/27.
 * 商城列表
 */

public class MainShopMallListAdapter extends RefreshAdapter<ShopMallGoodsList> {

    public static final int TYPE_PROFIT = 1;//爆款推荐
    private static final int NORMAL = 0;
    private int mType;
    public MainShopMallListAdapter(Context context, int type) {
        super(context);
        mType = type;
        AppConfig appConfig = AppConfig.getInstance();

    }

    @Override
    public int getItemCount() {
        if (mList != null ) {
            return mList.size() ;
        }
        return 0;
    }

    @Override
    public void clearData() {
        super.clearData();
    }

    @Override
    public void insertList(List<ShopMallGoodsList> list) {
        if (mRecyclerView != null && mList != null && list != null && list.size() > 0) {
            int p = mList.size() + 1;
            mList.addAll(list);
            notifyItemRangeInserted(p, list.size());
            mRecyclerView.scrollBy(0, mLoadMoreHeight);
        }
    }

    @Override
    public void refreshData(List<ShopMallGoodsList> list) {
        int size = list.size();
        if (size > 0) {
            mList.addAll(list);
        }
        super.refreshData(list);
    }

    @Override
    public int getItemViewType(int position) {
        return NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.shop_main_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Vh) holder).setData(mList.get(position), position);
    }


    class Vh extends RecyclerView.ViewHolder {

        ImageView mGoodsImg;
        TextView mGoodsName;
        TextView mGoodsPrice;
        ImageView mGoodsCardImg;
        LinearLayout goods_item_layout;

        public Vh(View itemView) {
            super(itemView);
            mGoodsImg = itemView.findViewById(R.id.good_img);
            mGoodsName = itemView.findViewById(R.id.good_name);
            mGoodsPrice = itemView.findViewById(R.id.good_price);
            mGoodsCardImg = itemView.findViewById(R.id.good_cart_img);
            goods_item_layout = itemView.findViewById(R.id.goods_item_layout);
        }

        void setData(ShopMallGoodsList bean, int position) {
            itemView.setTag(bean);
            if (!DataSafeUtils.isEmpty(bean.getGoods_image())) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mGoodsImg.getLayoutParams();
                params.width = CommentUtil.getDisplayWidth(mContext)/2-CommentUtil.dip2px(mContext,5);
                params.height = params.width;
                mGoodsImg.setLayoutParams(params);
                Glide.with(mContext).load(bean.getGoods_image()).into(mGoodsImg);
            }
            if (!DataSafeUtils.isEmpty(bean.getTitle())) {
                mGoodsName.setText(bean.getTitle());
            }
            if (!DataSafeUtils.isEmpty(bean.getPrice())) {
                mGoodsPrice.setText("￥"+bean.getPrice());
            }

            mGoodsCardImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.Wave).duration(1000).playOn(mGoodsCardImg);
                    addGoodsToCart(bean.getId());
                }
            });
            goods_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MyShopDetailActivity.class);
                    intent.putExtra("id", bean.getId() + "");
                    if (bean.getUid().equals(AppConfig.getInstance().getUid())){
                        intent.putExtra("status", "0");
                        intent.putExtra("store_id", bean.getUid());
                    }else {
                        intent.putExtra("status", "1");
                        intent.putExtra("store_id", bean.getId());
                    }
                    mContext.startActivity(intent);
                }
            });
        }
    }


    /**
     * 加入购物车
     * @param id
     */
    private void addGoodsToCart(String id) {
        HttpUtil.addGoodsToCart(id, "1", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags",code+"------"+msg);
                if (code==0) {
                    ToastUtil.show("加入成功");
                }else{
                    ToastUtil.show(msg);
                }
            }
        });
    }

}
