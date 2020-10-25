package com.mc.phonelive.activity.ordertk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.RefundORderBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySHDetailActivity extends AbsActivity {

    @BindView(R.id.titleView)
    TextView titleView;

    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.view_spot2)
    View view_spot2;

    @BindView(R.id.one_price)
    TextView one_price;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.tk_money)
    TextView tk_money;
    @BindView(R.id.aready_tk_money)
    TextView aready_tk_money;
    @BindView(R.id.goods_name)
    TextView goods_name;
    @BindView(R.id.goods_pic)
    ImageView goods_pic;
    @BindView(R.id.tk_state)
    TextView tk_state;
    @BindView(R.id.kf_line)
    LinearLayout kf_line;
    @BindView(R.id.step1_time)
    TextView step1_time;
    @BindView(R.id.order_title)
   TextView order_title;


    @BindView(R.id.step1_time3)
            TextView step1_time3;


    String storeID, orderId;
    String mobile;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_applay_sh_detail;
    }

    @Override
    protected void main() {
        titleView.setText("申请售后");


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        orderId = getIntent().getStringExtra("orderId");
        storeID = getIntent().getStringExtra("goodsId");
        loadData();
        kf_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneTel(mContext, mobile);
            }
        });
    }

    /**
     * 拨打客服电话
     */
    public static void callPhoneTel(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        context.startActivity(intent);
    }

    private void loadData() {
        HttpUtil.getOrderRefundDetail(storeID, orderId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
//                JSONObject obj = JSON.parseObject(info[0]);
//                RefundORderBean.InfoBean mData= JSON.toJavaObject(info[0],  RefundORderBean.InfoBean.class);
                RefundORderBean.InfoBean infoBean = JSON.parseObject(info[0], RefundORderBean.InfoBean.class);

                tk_money.setText("￥" + infoBean.getRefund_price());
                aready_tk_money.setText("￥" + infoBean.getRefund_price());
                ImgLoader.display(infoBean.getGoods_image(), goods_pic);
                goods_name.setText(infoBean.getGoods_name());
                one_price.setText("￥" +infoBean.getRefund_price());
                num.setText("x"+infoBean.getGoods_num());
                step1_time.setText(infoBean.getAccount_time());
                step1_time3.setText(infoBean.getUpdate_time());
                if(!TextUtils.isEmpty(infoBean.getRefund_status()) && infoBean.getRefund_status().equals("2")){
                    order_title.setText("退款成功");
                    view2.setBackgroundColor(Color.parseColor("#4CBF70"));
                    view_spot2.setBackgroundResource(R.drawable.sh_statu);
                }

                tk_state.setText("退款原因:" + infoBean.getReason_name() + "\n" +
                        "退款金额:" + infoBean.getRefund_price() + "\n" +
                        "申请时间:" + infoBean.getAccount_time() + "\n" +
                        "退款编号:" + infoBean.getRefund_no() + "\n"

                );
                mobile = infoBean.getMobile();
            }
        });
    }


}
