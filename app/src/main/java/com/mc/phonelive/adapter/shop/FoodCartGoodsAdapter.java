package com.mc.phonelive.adapter.shop;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.FoodCartVo;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 购物车第二层商品列表
 */
public class FoodCartGoodsAdapter extends BaseQuickAdapter<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean, BaseViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnGoodsSelectedkListener onGoodsSelectedkListener;
    private List<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean> goodsList;
    private FoodCartVo.DataBean.InfoBean.CartListBean mList;
    private int currentShopPosition = 0;

    public FoodCartGoodsAdapter(Context mContext, int currentShopPosition, int layoutResId, @Nullable List<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean> data, FoodCartVo.DataBean.InfoBean.CartListBean item) {
        super(layoutResId, data);
        this.currentShopPosition = currentShopPosition;
        this.context = mContext;
        this.goodsList = data;
        this.mList = item;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean item) {
        ImageView mImg = helper.getView(R.id.food_img);
        final ImageView mFoodCheckBox = helper.getView(R.id.food_checkbox);
        helper.setText(R.id.food_name, item.getGoods_title());
        helper.setText(R.id.food_num, item.getCart_num() + "");
        TextView foodPrice = helper.getView(R.id.food_price);

        if (!TextUtils.isEmpty(item.getPrice())) {
            foodPrice.setText("￥" + item.getPrice() + "");
        }

        if (!TextUtils.isEmpty(item.getGoods_image_url())) {
            RequestOptions options = new RequestOptions().error(R.mipmap.ic_launcher).centerCrop();
            Glide.with(mContext).load(item.getGoods_image_url()).apply(options).into(mImg);
        }

        /**
         * 商品选择点击事件
         */
        if (item.getIs_check().equals("1")) {
            mFoodCheckBox.setImageResource(R.drawable.check_select);
        } else if (item.getIs_check().equals("0")) {
            mFoodCheckBox.setImageResource(R.drawable.check_normal);
        }


        mFoodCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGoodsSelectedkListener != null) {
                    if (item.getIs_check().equals("1")) {
                        item.setIs_check("0");
                        mFoodCheckBox.setImageResource(R.drawable.check_normal);
                    } else if (item.getIs_check().equals("0")) {
                        mFoodCheckBox.setImageResource(R.drawable.check_select);
                        item.setIs_check("1");
                    }
                    onGoodsSelectedkListener.onGoodsSelected(helper.getLayoutPosition());

                }
            }
        });
        TextView mCartMinus = helper.getView(R.id.cart_minus);
        TextView mCartAdd = helper.getView(R.id.cart_add);
        if (item.getCart_num().equals("1")) {
            mCartMinus.setTextColor(mContext.getResources().getColor(R.color.gray1));
        } else {
            mCartMinus.setTextColor(mContext.getResources().getColor(R.color.C4));
        }
        mCartAdd.setTextColor(mContext.getResources().getColor(R.color.C4));

        /**
         * 购物车数量减少
         */
        mCartMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ButtonUtils.isFastDoubleClick(R.id.cart_minus)) {
                    if (Integer.parseInt(item.getCart_num()) > 1) {
                        EventBus.getDefault().post(new EventBusModel("cart_minus", item.getGoods_id(), mList));
                    }
                }
            }
        });
        /**
         * 购物车数量增加
         */
        mCartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ButtonUtils.isFastDoubleClick(R.id.cart_add)) {
//                    if (Integer.parseInt(item.getCart_num()) < Integer.parseInt(item.getStock())) {
                    EventBus.getDefault().post(new EventBusModel("cart_add", item.getGoods_id(), mList));
//                    } else {
//                        ToastUtil.show("库存不足");
//                    }
                }
            }
        });
        helper.getView(R.id.food_item_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusModel("shop_into_food", mList.getStore_id(), mList.getStore_name(), item.getGoods_id()));
            }
        });
        helper.getView(R.id.food_item_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                EventBus.getDefault().post(new EventBusModel("del_cart_list", item, 1));
                return true;
            }
        });
        helper.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventBusModel("del_cart_list", item, 1));
            }
        });

        helper.getView(R.id.food_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventBusModel("del_cart_list", item, 1));
            }
        });
    }


    /**
     * 列表点击事件
     */
    public interface OnGoodsSelectedkListener {

        void onGoodsSelected(int position);
    }


    /**
     * 设置条目点击事件
     *
     * @param onGoodsSelectedkListener
     */
    public void setOnGoodsSelectedkListener(OnGoodsSelectedkListener onGoodsSelectedkListener) {
        this.onGoodsSelectedkListener = onGoodsSelectedkListener;
    }

}
