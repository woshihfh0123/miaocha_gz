package com.mc.phonelive.activity.shop;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by WWL on 2020/4/9 0009:10
 * 订单发货
 */
public class OrderDeliveryActivity extends AbsActivity {
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_title)
    FrameLayout viewTitle;
    @BindView(R.id.devery_name)
    TextView deveryName;
    @BindView(R.id.devery_number)
    EditText deveryNumber;
    @BindView(R.id.devery_submit)
    TextView deverySubmit;
    String mCode = "";
    String mId = "";
    String mOrderId = "";

    @Override
    protected int getLayoutId() {
        return R.layout.order_delivery_activity;
    }

    @Override
    protected void main() {
        titleView.setText("发货");
        String orderid = this.getIntent().getStringExtra("order_id");
        if (!DataSafeUtils.isEmpty(orderid)) {
            this.mOrderId = orderid;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back, R.id.devery_submit, R.id.devery_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.devery_name:
                startActivityForResult(new Intent(mContext, CourierCompanyChoiseActivity.class), 200);
                break;
            case R.id.devery_submit:
                if (!ButtonUtils.isFastDoubleClick(R.id.devery_submit))
                    sendShippingSubmit();
                break;
        }
    }

    private void sendShippingSubmit() {
        if (DataSafeUtils.isEmpty(mId)) {
            ToastUtil.show("请选择快递公司");
            return;
        }
        if (DataSafeUtils.isEmpty(deveryNumber.getText().toString().trim())) {
            ToastUtil.show("请输入快递单号");
            return;
        }
        HttpUtil.OrderSendOrder(mOrderId, mId, deveryNumber.getText().toString().trim(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (code == 0) {
                    EventBus.getDefault().post(new EventBusModel("orderdetail_refresh"));
                    OrderDeliveryActivity.this.finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            if (!DataSafeUtils.isEmpty(data.getStringExtra("name")))
                deveryName.setText(data.getStringExtra("name"));
            mCode = data.getStringExtra("code");
            mId = data.getStringExtra("id");
        }
    }
}
