package com.mc.phonelive.activity.foxtone;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 立即上传
 */
public class TradingBuyUpLoadActivity extends AbsActivity {
    @BindView(R.id.upload_name)
    EditText uploadName;
    @BindView(R.id.upload_phone)
    EditText uploadPhone;
    @BindView(R.id.upload_type)
    TextView uploadType;
    @BindView(R.id.upload_img)
    ImageView uploadImg;
    @BindView(R.id.load_tv)
    TextView loadTv;

    private String mId = "";
    private String mType="1";//付款类型
    private String mImages="";
    @Override
    protected int getLayoutId() {
        return R.layout.trading_buy_upload_layout;
    }

    @Override
    protected void main() {
        setTitle("上传");
        String id = this.getIntent().getStringExtra("id");
        if (!DataSafeUtils.isEmpty(id)) {
            this.mId = id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.upload_type, R.id.upload_img, R.id.load_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upload_type:
                break;
            case R.id.upload_img:
                break;
            case R.id.load_tv:
                String mName = uploadName.getText().toString();
                String mPhone =uploadPhone.getText().toString();
                if (DataSafeUtils.isEmpty(mName)){
                    ToastUtil.show("请输入姓名");
                    return;
                }
                if (DataSafeUtils.isEmpty(mPhone)){
                    ToastUtil.show("请输入联系方式");
                    return;
                }
                if (DataSafeUtils.isEmpty(mImages)){
                    ToastUtil.show("请上传支付凭证");
                    return;
                }

                loadSubmit(mName,mPhone,mType,mImages);
                break;
        }
    }

    private void loadSubmit(String mName, String mPhone, String mType, String mImages) {
//        HttpUtil.setTradeAccount();
    }
}
