package com.mc.phonelive.activity.foxtone;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.foxtone.TradeAccountBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 银行卡绑定
 */
public class YinlianBindingActivity extends AbsActivity {
    @BindView(R.id.name_edit)
    EditText nameEdit;
    @BindView(R.id.bank_edit)
    EditText bankEdit;
    @BindView(R.id.account_edit)
    EditText accountEdit;
    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.binding_submit)
    TextView bindingSubmit;

    String mName = "";
    String mBankName = "";
    String mCardNumer = "";
    String mPhone = "";

    String mCardId = "";
    TradeAccountBean.InfoBean.ListInfoBean mData=null;
    @Override
    protected int getLayoutId() {
        return R.layout.yinlian_binding_layout;
    }

    @Override
    protected void main() {
        setTitle("收款绑定");

        TradeAccountBean.InfoBean.ListInfoBean data = (TradeAccountBean.InfoBean.ListInfoBean) this.getIntent().getSerializableExtra("data");
        if (!DataSafeUtils.isEmpty(data)){
            Log.v("tags","--------"+data.getName());
            mData =data;
            nameEdit.setText(data.getName());
            bankEdit.setText(data.getAccount());
            phoneEdit.setText(data.getMobile());
            accountEdit.setText(data.getCard());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.binding_submit)
    public void onViewClicked() {
        mName = nameEdit.getText().toString();
        mBankName = bankEdit.getText().toString();
        mCardNumer = accountEdit.getText().toString();
        mPhone = phoneEdit.getText().toString();
        if (DataSafeUtils.isEmpty(mName)) {
            return;
        }
        if (DataSafeUtils.isEmpty(mBankName)) {
            return;
        }
        if (DataSafeUtils.isEmpty(mCardNumer)) {
            return;
        }
        if (DataSafeUtils.isEmpty(mPhone)) {
            return;
        }

        cardBindingSubmit(mName,mBankName,mPhone,mCardNumer);
    }

    private void cardBindingSubmit(String mName, String mBankName, String mPhone, String mCardNumer) {
        HttpUtil.setTradeAccount("2", mName,mBankName,  mPhone, "", mCardNumer, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (code==0){
                    EventBus.getDefault().post(new EventBusModel("refresh_data"));
                    YinlianBindingActivity.this.finish();
                }
            }
        });
    }
}
