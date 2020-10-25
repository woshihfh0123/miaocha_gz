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
import com.mc.phonelive.activity.shop.MyMerchantOrderDetailActivity;
import com.mc.phonelive.activity.shop.OrderDeliveryActivity;
import com.mc.phonelive.activity.shop.OrderLogisticsActivity;
import com.mc.phonelive.bean.OrderListVo;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * created by WWL on 2019/1/5 0005:14
 * 我的订单
 *
 * myorder_adapter_type
 */
public class ScoreAdapter extends BaseQuickAdapter<OrderListVo.InfoBean, BaseViewHolder> {
    private Context mContext;
    private String mType;
    BaseQuickAdapter<OrderListVo.InfoBean.GoodsListBean, BaseViewHolder> mFoodListAdapter;

    public ScoreAdapter(Context context, int layoutResId, @Nullable List<OrderListVo.InfoBean> data, String type) {
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
                Intent intent = new Intent(mContext, MyMerchantOrderDetailActivity.class);
                intent.putExtra("order_id", item.getId());
                mContext.startActivity(intent);
            }
        });
        helper.getView(R.id.order_status_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入订单详情，查看详情信息
                Intent intent = new Intent(mContext, MyMerchantOrderDetailActivity.class);
                intent.putExtra("order_id", item.getId());
                mContext.startActivity(intent);
            }
        });
        helper.getView(R.id.order_adapter_type_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入订单详情，查看详情信息
                Intent intent = new Intent(mContext, MyMerchantOrderDetailActivity.class);
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
        List<OrderListVo.InfoBean.GoodsListBean> nlist=new ArrayList<>();
        for(int i=0;i<5;i++){
            nlist.add(new OrderListVo.InfoBean.GoodsListBean());
        }
        mFoodListAdapter.setNewData(nlist);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mFoodRecyclerView.setLayoutManager(manager);

        mFoodListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //进入订单详情，查看详情信息
                Intent intent = new Intent(mContext, MyMerchantOrderDetailActivity.class);
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
        textView.setTextSize(14);
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
        layoutParams.setMargins(0, 0, (int) CommentUtil.dpToPx(10), 0);
        textView.setLayoutParams(layoutParams);
        textView.setPadding((int) CommentUtil.dpToPx(12), (int) CommentUtil.dpToPx(5), (int) CommentUtil.dpToPx(12), (int) CommentUtil.dpToPx(5));
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
     *
     * @param operationsBean
     * @param listBean
     * @param orderId
     */
    private void sendOperations(OrderListVo.InfoBean.ButtonListBean operationsBean, OrderListVo.InfoBean listBean, String orderId) {
        //1去发货 2和3 查看物流 4 删除
        if (operationsBean.getId().equals("1")) {//1去发货
            Intent intent = new Intent(mContext, OrderDeliveryActivity.class);
            intent.putExtra("order_id", orderId);
            mContext.startActivity(intent);
        } else if (operationsBean.getId().equals("2") || operationsBean.getId().equals("3")) {//2和3 查看物流
            Intent intent = new Intent(mContext, OrderLogisticsActivity.class);
            intent.putExtra("order_id", orderId);
            mContext.startActivity(intent);
        } else if (operationsBean.getId().equals("4")) {//删除
            EventBus.getDefault().post(new EventBusModel("setDelData", listBean, mType));
        }

    }
}
