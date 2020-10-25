package com.mc.phonelive.adapter.shop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.shop.GoodsOrderActivity;
import com.mc.phonelive.activity.shop.PayGoodsOrderActivity;
import com.mc.phonelive.bean.FoodCartVo;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DoubleUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 购物车第一层店铺列表
 */
public class FoodCartAdapter extends BaseQuickAdapter<FoodCartVo.DataBean.InfoBean.CartListBean, BaseViewHolder> {
    private Context mContext;
    private FoodCartGoodsAdapter mFoodAdapter;
    private List<FoodCartVo.DataBean.InfoBean.CartListBean> shoplists;
    //    private String cartlist = "";
    int mFlash = 0;

    public FoodCartAdapter(Context context, int layoutResId, @Nullable List<FoodCartVo.DataBean.InfoBean.CartListBean> data) {
        super(layoutResId, data);
        this.mContext = context;
        this.shoplists = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FoodCartVo.DataBean.InfoBean.CartListBean item) {
        helper.setText(R.id.shop_name, item.getStore_name() + "");
        final ImageView mShopCheckBox = helper.getView(R.id.shop_checkbox);
        final TextView mShopAllPrice = helper.getView(R.id.shop_all_price);
        final TextView submit = helper.getView(R.id.submit);//去结算
        if (!DataSafeUtils.isEmpty(item.getStore_logo())) {
            Glide.with(mContext).load(item.getStore_logo()).into((ImageView) helper.getView(R.id.shop_imgs));
        }

        final RecyclerView FoodRecyclerview = helper.getView(R.id.foodcart_food_recyclerview);

        helper.getView(R.id.relayout_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventBusModel("shop_into", item.getStore_id(), item.getStore_name()));
            }
        });
        helper.getView(R.id.shop_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventBusModel("del_cart_shop", item, 0));
            }
        });

        /**
         * 选中店铺的商品
         */
        Double mCartTotalPrice = 0.00;
        if (!item.isShopcheck()) {
            mShopCheckBox.setImageResource(R.drawable.check_normal);
            mFlash = 0;
          if(Utils.isNotEmpty(item.getGoods())){
              for (int i = 0; i < item.getGoods().size(); i++) {
                  if (item.getGoods().get(i).getIs_check().equals("1")) {
                      mFlash = 1;
                      mCartTotalPrice = DoubleUtil.add(Double.parseDouble(item.getGoods().get(i).getGoods_total()), mCartTotalPrice);
                  }
              }
          }
        } else if (item.isShopcheck()) {
            mShopCheckBox.setImageResource(R.drawable.check_select);
            mCartTotalPrice = Double.parseDouble(item.getStore_total());
            mFlash = 1;
        }
//        Double prices1 = Double.parseDouble(item.getStore_total());
        Log.v("tags", "totlaprice=" + item.getStore_total() + "------计算后price=" + mCartTotalPrice);
        mShopAllPrice.setText("￥" + mCartTotalPrice);//支付金额
        if (mFlash == 0) {
            submit.setBackgroundColor(mContext.getResources().getColor(R.color.textColor2));
        } else {
            submit.setBackgroundColor(mContext.getResources().getColor(R.color.redlive));
        }

        setGoodsAdapter(helper, item, FoodRecyclerview);


        mShopCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selectedShop = item.isShopcheck();
                mFlash = 0;
                if (selectedShop) {
                    /**
                     * 取消选中
                     */
                    mShopCheckBox.setImageResource(R.drawable.check_normal);
                    setShopAllGoodsSelected("0", item.getGoods(), mFoodAdapter);
                    item.setShopcheck(false);

                } else {
                    /**
                     * 处于选中状态
                     */
                    mShopCheckBox.setImageResource(R.drawable.check_select);
                    setShopAllGoodsSelected("1", item.getGoods(), mFoodAdapter);
                    item.setShopcheck(true);
                }
                EventBus.getDefault().post(new EventBusModel("check_all_select_state", item.getGoods(), item.getStore_id(), item));


            }
        });


        mFoodAdapter.setOnGoodsSelectedkListener(new FoodCartGoodsAdapter.OnGoodsSelectedkListener() {
            @Override
            public void onGoodsSelected(int position) {
                EventBus.getDefault().post(new EventBusModel("check_item_select_state", item.getGoods(), item.getStore_id(), item));
            }
        });

        /**
         * 去结算
         */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick(R.id.submit)) {
                    String mClassId = "";
                    if (item.isShopcheck()) {
                        for (int i = 0; i < item.getGoods().size(); i++) {
                            mClassId = mClassId + "," + item.getGoods().get(i).getId();
                        }
                    } else {
                        if(Utils.isNotEmpty(item.getGoods())){

                            for (int i = 0; i < item.getGoods().size(); i++) {
                                if (item.getGoods().get(i).getIs_check().equals("1")) {
                                    mClassId = mClassId + "," + item.getGoods().get(i).getId();
                                }
                            }
//
                        }
                    }
                    if (!DataSafeUtils.isEmpty(mClassId)) {
                        mClassId = mClassId.substring(1);
                        Intent intent=new Intent(mContext, PayGoodsOrderActivity.class);
//
//                        intent.putExtra("goods_id",mGoodsId);
                        intent.putExtra("shopid", item.getStore_id());
                        intent.putExtra("cart_ids", mClassId);
                        intent.putExtra("iscart","1");
                        mContext.startActivity(intent);
//                        Intent intent = new Intent(mContext, GoodsOrderActivity.class);
//                        intent.putExtra("shopid", item.getStore_id());
//                        intent.putExtra("cart_ids", mClassId + "");
//                        mContext.startActivity(intent);
                    }
                }
            }
        });
    }

    private void setGoodsAdapter(BaseViewHolder helper, FoodCartVo.DataBean.InfoBean.CartListBean item, RecyclerView foodRecyclerview) {
        mFoodAdapter = new FoodCartGoodsAdapter(mContext, helper.getAdapterPosition(), R.layout.foodcart_list_fooditem, item.getGoods(), item);
        foodRecyclerview.setAdapter(mFoodAdapter);
        foodRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }


    /**
     * 设置商品所有的商品选中或者取消选中
     *
     * @param isSelecteed
     */
    private void setShopAllGoodsSelected(String isSelecteed, List<FoodCartVo.DataBean.InfoBean.CartListBean.GoodsBean> goodsList, FoodCartGoodsAdapter shopCartGoodsAdapter) {

        if (goodsList != null) {
            for (int i = 0; i < goodsList.size(); i++) {
                if (isSelecteed.equals("1")) {
                    goodsList.get(i).setIs_check("1");
                } else {
                    goodsList.get(i).setIs_check("0");
                }
            }

        }
    }

}
