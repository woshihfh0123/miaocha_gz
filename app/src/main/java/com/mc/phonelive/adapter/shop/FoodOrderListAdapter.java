package com.mc.phonelive.adapter.shop;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.FoodOrderVo;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * created by WWL on 2020/4/3 0003:15
 */
public class FoodOrderListAdapter extends BaseQuickAdapter<FoodOrderVo.InfoBean.StoreBean, BaseViewHolder> {
    BaseQuickAdapter<FoodOrderVo.InfoBean.StoreBean.GoodsBean,BaseViewHolder> mGoodsAdapter;
    public FoodOrderListAdapter(int layoutResId, @Nullable List<FoodOrderVo.InfoBean.StoreBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FoodOrderVo.InfoBean.StoreBean item) {
        TextView shop_name=helper.getView(R.id.shop_name);
        RecyclerView foodcart_recyclerview =helper.getView(R.id.foodcart_recyclerview);
        TextView peisong_type=helper.getView(R.id.peisong_type);
        TextView peisong_choise=helper.getView(R.id.peisong_choise);
        EditText beizhu_tv=helper.getView(R.id.beizhu_tv);
        TextView food_num=helper.getView(R.id.food_num);
        TextView shop_price=helper.getView(R.id.shop_price);
        if (!DataSafeUtils.isEmpty(item.getStore_name())){
            shop_name.setText(item.getStore_name());
        }
        if (!DataSafeUtils.isEmpty(item.getStore_total())){
            shop_price.setText("￥"+item.getStore_total());
        }
        if (!DataSafeUtils.isEmpty(item.getGoods())){
            food_num.setText("共"+item.getStore_num()+"件");
        }

        if (!DataSafeUtils.isEmpty(item.getStore_freight())){
            peisong_choise.setText("邮费   "+item.getStore_freight());
        }
//        if (!DataSafeUtils.isEmpty(item.getTips())){
//            beizhu_tv.setText(item.getTips());
//        }else{
//            beizhu_tv.setText("");
//        }
        getFoodRemark(beizhu_tv, item);
        getGoodsAdapter(foodcart_recyclerview,item.getGoods());
    }

    public void getGoodsAdapter(RecyclerView foodcart_recyclerview, List<FoodOrderVo.InfoBean.StoreBean.GoodsBean> list){
        mGoodsAdapter = new BaseQuickAdapter<FoodOrderVo.InfoBean.StoreBean.GoodsBean, BaseViewHolder>(R.layout.order_food_item_item_layout,list) {
            @Override
            protected void convert(BaseViewHolder helper, FoodOrderVo.InfoBean.StoreBean.GoodsBean item) {
               ImageView food_img=helper.getView(R.id.food_img);
               TextView food_name =helper.getView(R.id.food_name);
               TextView food_price = helper.getView(R.id.food_price);
               TextView food_num =helper.getView(R.id.food_num);
               if (!DataSafeUtils.isEmpty(item.getGoods_image_url())){
                   Glide.with(mContext).load(item.getGoods_image_url()).into(food_img);
               }
               if (!DataSafeUtils.isEmpty(item.getGoods_title())){
                   food_name.setText(item.getGoods_title());
               }
               if (!DataSafeUtils.isEmpty(item.getPrice())){
                   food_price.setText("￥"+item.getPrice());
               }
               food_num.setText("x"+item.getCart_num());

            }
        };
        foodcart_recyclerview.setAdapter(mGoodsAdapter);
        foodcart_recyclerview.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
    }


    private void getFoodRemark(EditText mFoodRemark, final FoodOrderVo.InfoBean.StoreBean food) {
        if (mFoodRemark.getTag() instanceof TextWatcher) {
            mFoodRemark.removeTextChangedListener((TextWatcher) mFoodRemark.getTag());
        }
//        mFoodRemark.setText(food.getBeizhu());
        if (!DataSafeUtils.isEmpty(food.getRemark())) {
            mFoodRemark.setText(food.getRemark());
        }else{
            mFoodRemark.setText("");
        }
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                food.setRemark(s.toString());
                EventBus.getDefault().post(new EventBusModel("remark",s.toString(),food.getStore_id()));
            }
        };
        mFoodRemark.addTextChangedListener(watcher);
        mFoodRemark.setTag(watcher);
    }

}