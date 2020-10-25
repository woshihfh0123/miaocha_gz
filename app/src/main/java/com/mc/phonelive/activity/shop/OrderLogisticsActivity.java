package com.mc.phonelive.activity.shop;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.shop.TraceAdapter;
import com.mc.phonelive.bean.OrderListBean;
import com.mc.phonelive.bean.Trace;
import com.mc.phonelive.custom.RatioRoundImageView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by WWL on 2020/4/9 0009:10
 */
public class OrderLogisticsActivity extends AbsActivity {
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_title)
    FrameLayout viewTitle;
    @BindView(R.id.wl_good_img)
    RatioRoundImageView wlGoodImg;
    @BindView(R.id.status_tip)
    TextView statusTip;
    @BindView(R.id.order_status_tv)
    TextView orderStatusTv;
    @BindView(R.id.gs_tip)
    TextView gsTip;
    @BindView(R.id.order_delivery_tv)
    TextView orderDeliveryTv;
    @BindView(R.id.cod_tip)
    TextView codTip;
    @BindView(R.id.order_code_tv)
    TextView orderCodeTv;
    @BindView(R.id.goods_num)
    TextView goods_num;
    @BindView(R.id.traceRv)
    RecyclerView traceRv;

    private List<Trace> mTraceList = new ArrayList<>(); //物流追踪列表的数据源
    private TraceAdapter mAdapter;
    private String mOrderId = "";

    @Override
    protected int getLayoutId() {
        return R.layout.order_logistics_activity;
    }

    @Override
    protected void main() {
        super.main();
        titleView.setText("查看物流");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderLogisticsActivity.this.finish();

            }
        });

        String orderid = this.getIntent().getStringExtra("order_id");
        if (!DataSafeUtils.isEmpty(orderid)) {
            this.mOrderId = orderid;
        }

        if(Utils.isNotEmpty(getIntent().getStringExtra("type"))){
            getOrderWL(mOrderId);
        }else{
            initData();
        }


        orderCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //获取剪贴板管理器
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", orderCodeTv.getText().toString());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }

    //加载物流信息的数据，这里是模拟一些假数据
    private void initData() {
        HttpUtil.OrderLogisicsDetail(mOrderId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (info.length > 0) {
                    Log.v("tags", code + "----" + msg + "---" + info[0]);
                    JSONObject obj = JSON.parseObject(info[0]);
                    String orderstatus = obj.getString("order_state_name");
                    String goods_image = obj.getString("goods_image");
                    String order_code = obj.getString("order_code");
                    String shipping_name = obj.getString("shipping_name");
                    String shipping_code = obj.getString("shipping_code");
                    String total_num = obj.getString("total_num");
                    if (!DataSafeUtils.isEmpty(orderstatus)) {
                        orderStatusTv.setText(orderstatus);
                    }
                    if (!DataSafeUtils.isEmpty(total_num)) {
                        goods_num.setText(total_num+"件商品");
                    }
                    if (!DataSafeUtils.isEmpty(shipping_code)) {
                        orderCodeTv.setText(shipping_code);
                    }
                    if (!DataSafeUtils.isEmpty(goods_image)) {
                        Glide.with(mContext).load(goods_image).into(wlGoodImg);
                    }
                    if (!DataSafeUtils.isEmpty(shipping_name)) {
                        orderDeliveryTv.setText(shipping_name);
                    }
                    Log.v("tags",obj.getString("list").length()+"----l");
                    if (!DataSafeUtils.isEmpty(obj.getString("list"))) {
                        Log.v("tags",obj.getString("list"));
                        mTraceList = new ArrayList<>();
                        List<Trace> traceList = JSON.parseArray(obj.getString("list"), Trace.class);
                        Log.v("tags","traceList.size="+traceList.size());
                        if (traceList.size()>0) {
                            for (int i = 0; i < traceList.size(); i++) {
                                Trace trace = traceList.get(i);
                                if (i == 0) {
                                    trace.setType(0);
                                } else {
                                    trace.setType(1);
                                }
                                mTraceList.add(trace);
                            }
                        }else{
                            mTraceList.add(new Trace(0, "", "快递已发出",""));
                        }
                        initRecyclerView();
                    }
                }
            }
            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
        });



    }

    /*
     * 我的积分订单  查看物流
     * */
    private void getOrderWL(String orderId) {
        HttpUtil.getSureOrderWL(orderId, "1", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    if (info.length > 0) {
                        Log.v("tags", code + "----" + msg + "---" + info[0]);
                        JSONObject obj = JSON.parseObject(info[0]);
                        String orderstatus = obj.getString("order_state_name");
                        String goods_image = obj.getString("goods_image");
                        String order_code = obj.getString("order_code");
                        String shipping_name = obj.getString("shipping_name");
                        String shipping_code = obj.getString("shipping_code");
                        String total_num = obj.getString("total_num");
                        if (!DataSafeUtils.isEmpty(orderstatus)) {
                            orderStatusTv.setText(orderstatus);
                        }
                        if (!DataSafeUtils.isEmpty(total_num)) {
                            goods_num.setText(total_num + "件商品");
                        }
                        if (!DataSafeUtils.isEmpty(shipping_code)) {
                            orderCodeTv.setText(shipping_code);
                        }
                        if (!DataSafeUtils.isEmpty(goods_image)) {
                            Glide.with(mContext).load(goods_image).into(wlGoodImg);
                        }
                        if (!DataSafeUtils.isEmpty(shipping_name)) {
                            orderDeliveryTv.setText(shipping_name);
                        }
                        Log.v("tags", obj.getString("list").length() + "----l");
                        if (!DataSafeUtils.isEmpty(obj.getString("list"))) {
                            Log.v("tags", obj.getString("list"));
                            mTraceList = new ArrayList<>();
                            List<Trace> traceList = JSON.parseArray(obj.getString("list"), Trace.class);
                            Log.v("tags", "traceList.size=" + traceList.size());
                            if (traceList.size() > 0) {
                                for (int i = 0; i < traceList.size(); i++) {
                                    Trace trace = traceList.get(i);
                                    if (i == 0) {
                                        trace.setType(0);
                                    } else {
                                        trace.setType(1);
                                    }
                                    mTraceList.add(trace);
                                }
                            } else {
                                mTraceList.add(new Trace(0, "", "快递已发出", ""));
                            }
                            initRecyclerView();

                        }
                    }
                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
        });
    }

    //初始化显示物流追踪的RecyclerView
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mAdapter = new TraceAdapter(this, mTraceList);
        traceRv.setLayoutManager(layoutManager);
        traceRv.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
