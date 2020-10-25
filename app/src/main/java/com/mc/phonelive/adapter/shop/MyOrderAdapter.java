package com.mc.phonelive.adapter.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.shop.FoodWritingCommentActivity;
import com.mc.phonelive.activity.shop.MyOrderDetailActivity;
import com.mc.phonelive.activity.shop.OrderLogisticsActivity;
import com.mc.phonelive.bean.OrderListVo;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * created by WWL on 2019/1/5 0005:14
 * 我的订单
 */
public class MyOrderAdapter extends BaseQuickAdapter<OrderListVo.InfoBean, BaseViewHolder> {
    private Context mContext;
    private String mType;
    BaseQuickAdapter<OrderListVo.InfoBean.GoodsListBean, BaseViewHolder> mFoodListAdapter;

    public MyOrderAdapter(Context context, int layoutResId, @Nullable List<OrderListVo.InfoBean> data, String type) {
        super(layoutResId, data);
        this.mContext = context;
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderListVo.InfoBean item) {
        TextView mOrderNumber = helper.getView(R.id.order_number);
        TextView mOrderPayType = helper.getView(R.id.order_pay_type);
        TextView mTypeName = helper.getView(R.id.order_status_tv);
        RecyclerView mFoodRecyclerView = helper.getView(R.id.food_recyclerView);
        if (!TextUtils.isEmpty(item.getStore_name()))
            mOrderNumber.setText("" + item.getStore_name() + "");

        if (!TextUtils.isEmpty(item.getOrder_state_name())) {
            mTypeName.setText(item.getOrder_state_name() + "");
            mTypeName.setTextColor(Color.parseColor("#646464"));
        }

//        if (!DataSafeUtils.isEmpty(item.getPay_type())) {
//            if (item.getPay_type().equals("0")) {
//                mOrderPayType.setVisibility(View.GONE);
//            } else {
//                mOrderPayType.setVisibility(View.VISIBLE);
//                if (item.getPay_type().equals("1")) {
//                    mOrderPayType.setText("支付方式：微信");
//                } else if (item.getPay_type().equals("2")) {
//                    mOrderPayType.setText("支付方式：支付宝");
//                } else if (item.getPay_type().equals("3")) {
//                    mOrderPayType.setText("支付方式：余额");
//                } else if (item.getPay_type().equals("4")) {
//                    mOrderPayType.setText("支付方式：银联");
//                } else if (item.getPay_type().equals("5")) {
//                    mOrderPayType.setText("支付方式：购物券");
//                } else if (item.getPay_type().equals("6")) {
//                    mOrderPayType.setText("支付方式：批发券");
//                }
//            }
//        } else {
//            mOrderPayType.setVisibility(View.GONE);
//        }

        TextView foodnum = helper.getView(R.id.order_food_num);
        TextView orderAllPrice = helper.getView(R.id.orderallPrise);
        if (!TextUtils.isEmpty(item.getTotal_num()))
            foodnum.setText("共" + item.getTotal_num() + "件商品");

        if (!TextUtils.isEmpty(item.getPay_price())) {
            orderAllPrice.setText("￥" + item.getPay_price());
        }


        LinearLayout order_status_layout = helper.getView(R.id.order_status_layout);
            if (!DataSafeUtils.isEmpty(item.getButton_list())) {
                order_status_layout.setVisibility(View.VISIBLE);
                order_status_layout.removeAllViews();
                for (int i = 0; i < item.getButton_list().size(); i++) {
                    addTextView(order_status_layout, item, item.getButton_list().get(i), item.getId());
                }
            } else {
                order_status_layout.setVisibility(View.GONE);
            }

        helper.getView(R.id.order_title_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入订单详情，查看详情信息
                Intent intent = new Intent(mContext, MyOrderDetailActivity.class);
                intent.putExtra("order_id", item.getId());
                mContext.startActivity(intent);
            }
        });
        helper.getView(R.id.order_status_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入订单详情，查看详情信息
                Intent intent = new Intent(mContext, MyOrderDetailActivity.class);
                intent.putExtra("order_id", item.getId());
                mContext.startActivity(intent);
            }
        });
        helper.getView(R.id.order_adapter_type_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入订单详情，查看详情信息
                Intent intent = new Intent(mContext, MyOrderDetailActivity.class);
                intent.putExtra("order_id", item.getId());
                mContext.startActivity(intent);
            }
        });

        getFoodItemAdapter(item, mFoodRecyclerView);
    }

    private void getFoodItemAdapter(final OrderListVo.InfoBean mFoodList, RecyclerView mFoodRecyclerView) {
        mFoodListAdapter = new BaseQuickAdapter<OrderListVo.InfoBean.GoodsListBean, BaseViewHolder>(R.layout.myorder_foodlist_item, mFoodList.getGoods_list()) {

            @Override
            protected void convert(BaseViewHolder helper, OrderListVo.InfoBean.GoodsListBean item) {
                if (!TextUtils.isEmpty(item.getGoods_name()))
                    helper.setText(R.id.food_name, item.getGoods_name());
                if (!TextUtils.isEmpty(item.getGoods_num()))
                    helper.setText(R.id.food_num, "x" + item.getGoods_num());
                if (!TextUtils.isEmpty(item.getGoods_price()))
                    helper.setText(R.id.food_newprice, "￥" + item.getGoods_price() + "");


                if (!TextUtils.isEmpty(item.getGoods_image())) {
                    Glide.with(mContext).load(item.getGoods_image()).into((ImageView) helper.getView(R.id.food_img));
                } else {
                    Glide.with(mContext).load(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.food_img));
                }
            }
        };
        mFoodRecyclerView.setAdapter(mFoodListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mFoodRecyclerView.setLayoutManager(manager);

        mFoodListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //进入订单详情，查看详情信息
                Intent intent = new Intent(mContext, MyOrderDetailActivity.class);
                intent.putExtra("order_id", mFoodList.getId());
                mContext.startActivity(intent);
            }
        });
    }


    /**
     * 动态添加按钮
     *
     * @param linearLayout
     * @param operationsBean
     */
    private void addTextView(LinearLayout linearLayout, final OrderListVo.InfoBean item, final OrderListVo.InfoBean.ButtonListBean operationsBean, final String orderId) {
        TextView textView = new TextView(mContext);
        textView.setText(operationsBean.getTitle());
        if (!DataSafeUtils.isEmpty(operationsBean.getColor())) {
            textView.setTextColor(Color.parseColor(operationsBean.getColor()));
        } else {
            textView.setTextColor(Color.parseColor("#ff0000"));
        }
        textView.setTextSize(13);
        GradientDrawable gd = new GradientDrawable();//创建drawable
        if (!DataSafeUtils.isEmpty(operationsBean.getColor())) {
            gd.setStroke(1, Color.parseColor(operationsBean.getColor()));
        } else {
            gd.setStroke(1, Color.parseColor("#ff0000"));
        }
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(CommentUtil.dpToPx(25));
        textView.setBackground(gd);
//        textView.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5,R.color.FUBColor3, R.color.C1));//边框的颜色和背景的颜色
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, (int) CommentUtil.dpToPx(8), 0);
        textView.setLayoutParams(layoutParams);
        textView.setPadding((int) CommentUtil.dpToPx(10), (int) CommentUtil.dpToPx(3), (int) CommentUtil.dpToPx(10), (int) CommentUtil.dpToPx(3));
        linearLayout.addView(textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick()) {
                    sendOperations(operationsBean, item, orderId);
                }
            }
        });
    }


    /**
     * 列表中按钮
     *1删除 2取消 3去付款 4 查看物流 5确认收货 6评价
     * @param operationsBean
     * @param listBean
     * @param orderId
     */
    private void sendOperations(OrderListVo.InfoBean.ButtonListBean operationsBean, OrderListVo.InfoBean listBean, String orderId) {
        //1删除 2取消 3去付款 4 查看物流 5确认收货 6评价
        if (operationsBean.getId().equals("1")){//删除
            EventBus.getDefault().post(new EventBusModel("setDelData", listBean, mType));
        }else if (operationsBean.getId().equals("2")){//取消
            EventBus.getDefault().post(new EventBusModel("setCancleData", listBean, mType));
        }else if (operationsBean.getId().equals("3")){//去付款
            EventBus.getDefault().post(new EventBusModel("PayNow", listBean, mType));
        }else if (operationsBean.getId().equals("4")){//查看物流
            Intent intent = new Intent(mContext, OrderLogisticsActivity.class);
            intent.putExtra("order_id",listBean.getId());
            mContext.startActivity(intent);
        }else if (operationsBean.getId().equals("5")){//5确认收货
            EventBus.getDefault().post(new EventBusModel("ConfirmProduct", listBean, mType));
        }else if (operationsBean.getId().equals("6")){//6评价
            Intent intent = new Intent(mContext, FoodWritingCommentActivity.class);
            intent.putExtra("order_id", listBean.getId());
            mContext.startActivity(intent);
        }

    }
}
