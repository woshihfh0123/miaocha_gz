package com.mc.phonelive.activity.shop;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.ordertk.MySHApplyTKDetailActivity;
import com.mc.phonelive.bean.OrderDetailVO;
import com.mc.phonelive.dialog.CancelORderDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mc.phonelive.utils.DataUtils.isDestroy;

/**
 * created by WWL on 2019/6/28 0028:17
 * 我的订单详情
 */
public class MyOrderDetailActivity extends AbsActivity {
    @BindView(R.id.back_aytout)
    LinearLayout backAytout;
    @BindView(R.id.order_title_status)
    TextView orderTitleStatus;
    @BindView(R.id.order_title_detail)
    TextView orderTitleDetail;
    @BindView(R.id.order_title_status_img)
    ImageView orderTitleStatusImg;
    @BindView(R.id.detail_username)
    TextView detailUsername;
    @BindView(R.id.detail_userphone)
    TextView detailUserphone;
    @BindView(R.id.detail_useraddress)
    TextView detailUseraddress;
    @BindView(R.id.detail_food_recyclerview)
    RecyclerView detailFoodRecyclerview;
    @BindView(R.id.payment_price)
    TextView paymentPrice;
    @BindView(R.id.beizhu_content)
    TextView beizhuContent;
    @BindView(R.id.order_number)
    TextView orderNumber;
    @BindView(R.id.order_pay_number)
    TextView order_pay_number;
    @BindView(R.id.order_number_copy_layout)
    RelativeLayout orderNumberCopyLayout;
    @BindView(R.id.order_date)
    TextView orderDate;
    @BindView(R.id.phone_layout)
    LinearLayout phoneLayout;
    @BindView(R.id.head_layout)
    LinearLayout headLayout;
    @BindView(R.id.order_btn_layout)
    LinearLayout mBtnLayout;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.order_over_layout)//交易已完成就显示，其他状态就隐藏
            RelativeLayout order_over_layout;
    @BindView(R.id.end_time)
    TextView end_time;//已完成，签收时间

    @BindView(R.id.foodAll_price)
    TextView foodAllPrice;//商品总价
    @BindView(R.id.shipping_price)
    TextView shippingPrice;//配送费

    @BindView(R.id.shipping_layout)
    RelativeLayout shippingLayout;
    @BindView(R.id.address_layout)
    RelativeLayout addressLayout;
    @BindView(R.id.order_shop_name)
    TextView order_shop_name;

    OrderDetailVO.InfoBean mDatas = new OrderDetailVO.InfoBean();

    List<OrderDetailVO.InfoBean.GoodsListBean> mList = new ArrayList<>();
    BaseQuickAdapter<OrderDetailVO.InfoBean.GoodsListBean, BaseViewHolder> mFoodListAdapter;//订单列表
    @BindView(R.id.paytype_layout)
    RelativeLayout paytype_layout;
    @BindView(R.id.pay_type)
    TextView payType;

    private String mOrderId = "";
    //倒计时
    CountDownTimer timer;

    @Override
    protected boolean isStatusBarWhite() {
        return true;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.myorder_detail_activity;
    }

    @Override
    protected void main() {
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }


        String id = this.getIntent().getStringExtra("order_id");
        if (!TextUtils.isEmpty(id)) {
            this.mOrderId = id;
        }


        getFoodListAdapter(mList);

        initListeners();
        showDialog();
        initHttpData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void initHttpData() {

        HttpUtil.OrderGetOrderDetail(mOrderId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0)
                    if (info.length > 0) {
                        OrderDetailVO.InfoBean bean = JSON.parseObject(info[0], OrderDetailVO.InfoBean.class);
                        getInitUl(bean);
                    }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

    }

    private void getInitUl(OrderDetailVO.InfoBean mData) {
        mDatas = mData;

        if (!DataSafeUtils.isEmpty(mData.getPay_type())) {
            payType.setText(mData.getPay_type());
            paytype_layout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mData.getReciver_name())) {//收货人
            detailUsername.setText("收件人：" + mData.getReciver_name());
        }
        if (!TextUtils.isEmpty(mData.getReciver_phone())) {//收货人电话
            detailUserphone.setText(mData.getReciver_phone());
        }
        if (!TextUtils.isEmpty(mData.getReciver_info())) {//地址
            detailUseraddress.setText(Html.fromHtml(mData.getReciver_info()));
        }

        if (!DataSafeUtils.isEmpty(mData.getOrder_state_arr())) {//状态名称
            if (!DataSafeUtils.isEmpty(mData.getOrder_state_arr().get_status())) {
                orderTitleStatus.setText(mData.getOrder_state_arr().get_status().getTitle());
                if (!TextUtils.isEmpty(mData.getOrder_state_arr().get_status().getMsg())) {//状态说明
                    orderTitleDetail.setText(mData.getOrder_state_arr().get_status().getMsg() + "");
                } else {
                    orderTitleDetail.setVisibility(View.GONE);
                }
                if (!DataSafeUtils.isEmpty(mData.getOrder_state_arr().get_status().getImg())) {
                    //添加判断
                    if(!isDestroy((Activity)mContext)){
                        RoundedCorners roundedCorners = new RoundedCorners(20);
                        RequestOptions options = new RequestOptions().bitmapTransform(roundedCorners);
                        Glide.with(mContext).load(mData.getOrder_state_arr().get_status().getImg()).apply(options).into(orderTitleStatusImg);
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(mData.getTotal_postage())) {//配送费
            shippingPrice.setText("￥" + mData.getTotal_postage());
        }

        if (!TextUtils.isEmpty(mData.getExplain())) {//订单备注
            beizhuContent.setText(mData.getExplain());
        } else {
            beizhuContent.setText("无");
        }

        if (!TextUtils.isEmpty(mData.getOrder_code())) {//订单编号
            orderNumber.setText(mData.getOrder_code());
        }
        if (!TextUtils.isEmpty(mData.getPay_sn())) {//交易账号
        order_pay_number.setText(mData.getPay_sn()+"");
        }

        if (!TextUtils.isEmpty(mData.getAdd_time())) {//下单时间
            orderDate.setText(mData.getAdd_time());
        }
        if (!TextUtils.isEmpty(mData.getStore_name())) {//名称
            order_shop_name.setText(mData.getStore_name());
        }

        if (!DataSafeUtils.isEmpty(mData.getGoods_list()))
            mFoodListAdapter.setNewData(mData.getGoods_list());

        if (!DataSafeUtils.isEmpty(mData.getButton_list())) {
            mBtnLayout.setVisibility(View.VISIBLE);
            mBtnLayout.removeAllViews();
            for (int i = 0; i < mData.getButton_list().size(); i++) {
                addTextView(mBtnLayout, mData.getButton_list().get(i), mData.getId());
            }
        }else{
            mBtnLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mData.getTotal_price())) {//商品总价
            foodAllPrice.setText("￥" + mData.getTotal_price());
        }
        if (!TextUtils.isEmpty(mData.getPay_price())) {//需付款
            paymentPrice.setText("￥" + mData.getPay_price());
        }

    }


    private void getFoodListAdapter(List<OrderDetailVO.InfoBean.GoodsListBean> mFoodList) {
        mFoodListAdapter = new BaseQuickAdapter<OrderDetailVO.InfoBean.GoodsListBean, BaseViewHolder>(R.layout.foodorder_foodlist_item, mFoodList) {
            @Override
            protected void convert(BaseViewHolder helper, final OrderDetailVO.InfoBean.GoodsListBean item) {
                if (!TextUtils.isEmpty(item.getGoods_name()))
                    helper.setText(R.id.food_name, item.getGoods_name());
                if (!TextUtils.isEmpty(item.getGoods_num()))
                    helper.setText(R.id.food_num, "X" + item.getGoods_num());
                if (!TextUtils.isEmpty(item.getGoods_price()))
                    helper.setText(R.id.food_newprice, "￥" + item.getGoods_price() + "");

                TextView tex_tk = helper.getView(R.id.btn_tk);
                if(null != item.getIs_refund() && Integer.parseInt(item.getIs_refund())>=0 && Integer.parseInt(item.getIs_refund())<=3){
                    tex_tk.setEnabled(false);
                    if(item.getIs_refund().equals("0")){
                        tex_tk.setText("申请退款");
                    }else if(item.getIs_refund().equals("1")){
                        tex_tk.setText("退款中");
                    }else if(item.getIs_refund().equals("2")){
                        tex_tk.setText("退款成功");
                    }else if(item.getIs_refund().equals("3")){
                        tex_tk.setText("拒绝退款");
                    }

                    tex_tk.setVisibility(View.VISIBLE);
//                    tex_tk.setTextColor(Color.parseColor(item.getButton_lists().get(0).getColor()));
                    if(item.getIs_refund().equals("0")){
                        tex_tk.setVisibility(View.VISIBLE);
                        tex_tk.setEnabled(true);
                    }else{
                        tex_tk.setEnabled(false);
                    }
                }else{
                    tex_tk.setVisibility(View.GONE);
                }


                helper.setGone(R.id.food_guige, false);

                if (!TextUtils.isEmpty(item.getGoods_image())) {
                    Glide.with(mContext).load(item.getGoods_image()).into((ImageView) helper.getView(R.id.food_img));
                } else {
                    Glide.with(mContext).load(R.drawable.default_icon).into((ImageView) helper.getView(R.id.food_img));
                }

                helper.getView(R.id.btn_tk).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent iten = new Intent(MyOrderDetailActivity.this, MySHApplyTKDetailActivity.class);
                        iten.putExtra("goodsId",item.getGoods_id());
                        iten.putExtra("orderId",item.getOrder_id());
                        iten.putExtra("imag",item.getGoods_image());
                        iten.putExtra("goodsname",item.getGoods_name());
                        iten.putExtra("goodsnum",item.getGoods_num());
                        iten.putExtra("goodsprice",item.getGoods_price());
                        iten.putExtra("goodspayprice",item.getGoods_pay_price());
                        startActivity(iten);
                    }
                });

                helper.getView(R.id.outtime_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        copy(item.getDelivery_id());
//                        Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        };
        detailFoodRecyclerview.setAdapter(mFoodListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        detailFoodRecyclerview.setLayoutManager(manager);
    }

    /**
     * 复制内容到剪切板
     *
     * @param copyStr
     * @return
     */
    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 动态添加按钮
     *
     * @param linearLayout
     * @param operationsBean
     */
    private void addTextView(LinearLayout linearLayout, final OrderDetailVO.InfoBean.ButtonListBean operationsBean, final String orderId) {
        TextView textView = new TextView(this);
        textView.setText(operationsBean.getTitle());
        if (!DataSafeUtils.isEmpty(operationsBean.getColor())) {
            textView.setTextColor(Color.parseColor(operationsBean.getColor()));
        } else {
            textView.setTextColor(Color.parseColor("#ff0000"));
        }
        GradientDrawable gd = new GradientDrawable();//创建drawable
        if (!DataSafeUtils.isEmpty(operationsBean.getColor())) {
            gd.setStroke(1, Color.parseColor(operationsBean.getColor()));
        } else {
            gd.setStroke(1, Color.parseColor("#ff0000"));
        }
//        gd.setColor(Color.parseColor(operationsBean.getBgcolor()));
        gd.setCornerRadius(CommentUtil.dpToPx(25));
        textView.setBackground(gd);
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
                    sendOperations(operationsBean, orderId);
                }
            }
        });
    }

    private void sendOperations(OrderDetailVO.InfoBean.ButtonListBean operationsBean, String orderId) {
        //1删除 2取消 3去付款 4 查看物流 5确认收货 6评价
        if (operationsBean.getId().equals("1")) {//删除
            delOrder("2");
        } else if (operationsBean.getId().equals("2")) {//取消
            delOrder("1");
        } else if (operationsBean.getId().equals("3")) {//去付款
            PayNow();
        } else if (operationsBean.getId().equals("4")) {//查看物流
            Intent intent = new Intent(mContext, OrderLogisticsActivity.class);
            intent.putExtra("order_code", mDatas.getOrder_code());
            intent.putExtra("order_id", mDatas.getId());
            mContext.startActivity(intent);
        } else if (operationsBean.getId().equals("5")) {//5确认收货
            ConfirmProduct(orderId);
        } else if (operationsBean.getId().equals("6")) {//6评价
            Intent intent = new Intent(mContext, FoodWritingCommentActivity.class);
            intent.putExtra("order_id", mDatas.getId());
            mContext.startActivity(intent);
        }
//        Intent intent = new Intent(mContext, FoodWritingCommentActivity.class);
//        intent.putExtra("order_id", mDatas.getId());
//        mContext.startActivity(intent);
    }


    /**
     * 确认收货
     *
     * @param orderId
     */
    private void ConfirmProduct(final String orderId) {
        CancelORderDialog oRderDialog = new CancelORderDialog(this, "确定收货", "确定已收到货吗？") {
            @Override
            public void doConfirmUp() {
                getConfirmProduct(orderId);

            }
        };
        oRderDialog.show();
    }

    /**
     * @param mOrderId
     */
    private void getConfirmProduct(final String mOrderId) {
        HttpUtil.OrderTakeOrder(mOrderId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    initHttpData();
                }
                ToastUtil.show(msg);
            }
        });
    }


    /**
     * 订单删除
     */
    private void delOrder(String type) {
        String title = "";
        String msg = "";
        if (type.equals("1")) {
            title = "取消订单";
            msg = "确定取消订单？";
        } else {
            title = "删除订单";
            msg = "确定删除订单？";
        }
        CancelORderDialog oRderDialog = new CancelORderDialog(this, title, msg) {
            @Override
            public void doConfirmUp() {
                getDelOrder(mDatas.getId(), type);
            }
        };
        oRderDialog.show();
    }


    /**
     * 删除订单
     *
     * @param mOrderId
     */
    private void getDelOrder(final String mOrderId, String type) {
        showDialog();
        HttpUtil.OrderDelOrder(mOrderId, type, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (code == 0) {
                    MyOrderDetailActivity.this.finish();
                }
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        });
    }

    /**
     * 立即支付
     */
    private void PayNow() {
        Intent intent = new Intent(mContext, PayActivity.class);
        intent.putExtra("out_trade_no", mDatas.getPay_sn());
        intent.putExtra("order_id", mDatas.getId());
        intent.putExtra("money", mDatas.getPay_price());
        startActivity(intent);
        MyOrderDetailActivity.this.finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.back_aytout, R.id.phone_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_aytout:
                MyOrderDetailActivity.this.finish();
                break;
            case R.id.phone_layout:
//                call(this,"11111111111");
                break;
//            case R.id.delivery_number:
//                if (!DataSafeUtils.isEmpty(mDatas.getDelivery_id())) {
//                    copy(mDatas.getDelivery_id());
//                    Toast.makeText(MyOrderDetailActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
//                }
//                break;
        }
    }

    // 打电话
    public static void call(Context context, String phone) {
        String str = "tel:" + phone;
        Uri uri = Uri.parse(str);

        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void initListeners() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = topLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                topLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView v, int x, int y, int oldx, int oldy) {
                        if (y <= 0) {
                            topLayout.setBackgroundColor(Color.argb((int) 0, 221, 26, 32));//AGB由相关工具获得，或者美工提供
                        } else if (y <= 200) {
                            float scale = (float) y / 200;
                            float alpha = (255 * scale);
                            topLayout.setBackgroundColor(Color.argb((int) alpha, 221, 26, 32));
                        } else {
                            topLayout.setBackgroundColor(Color.argb((int) 255, 221, 26, 32));
                        }
                    }


                });
            }
        });
    }


    /**
     * 倒数计时器
     */
//    private void TimeStart(long mTimes) {
//        //将前一个缓存清除
//        if (timer != null) {
//            timer.cancel();
//        }
//        /****
//         * 倒计时方法如下-
//         */
//        timer = new CountDownTimer(mTimes * 1000, 1000) {
//            /**
//             * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
//             * @param millisUntilFinished
//             */
//            @Override
//            public void onTick(long millisUntilFinished) {
//                pintuanEndTime.setText(TimeTools.getCountTimeByLong(millisUntilFinished));
//            }
//
//            /**
//             * 倒计时完成时被调用
//             */
//            @Override
//            public void onFinish() {
//                Log.v("logger", "倒计时完毕了");
//                pintuanEndTime.setText("00:00:00");
//                if (!isStopTuan) {
//                    isStopTuan = true;
//                    initHttpData();
//                }
//            }
//        };
//        timer.start();
//    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventBusModel eventBusModel) {
        String code = eventBusModel.getCode();
        switch (code) {
            case "orderdetail_refresh"://刷新数据,详情界面数据或许已经改变了，那么在关闭详情的时候，订单列表自动刷新
              initHttpData();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtil.cancel(HttpConsts.ORDERGETORDERDETAIL);
        EventBus.getDefault().post(new EventBusModel("orderdetail_refresh"));
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered) {
            EventBus.getDefault().unregister(this);
        }
    }

}
