package com.mc.phonelive.activity.foxtone;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cxf on 2018/10/7.
 * 设置交易密码
 */

public class SetTransactionPwdActivity extends AbsActivity{


//    @BindView(R.id.edit_phone)
//    EditText editPhone;
    @BindView(R.id.edit_pwd)
    EditText editPwd;
    @BindView(R.id.edit_pwd_again)
    EditText editPwdAgain;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;

    @Override
    protected int getLayoutId() {
        return R.layout.set_transaction_pwd;
    }

    @Override
    protected void main() {
        setTitle("设置交易密码");

    }


    private void modify() {
//        String mPhone = editPhone.getText().toString();
        String mPwd = editPwd.getText().toString();
        String mPwdConfirm =editPwdAgain.getText().toString();
        if (DataSafeUtils.isEmpty(mPwd) || mPwd.length()<6){
            ToastUtil.show("请输入6位密码");
            return;
        }
        if (DataSafeUtils.isEmpty(mPwdConfirm)){
            ToastUtil.show("请输入确认密码");
            return;
        }
        if (!mPwd.equals(mPwdConfirm)) {
          ToastUtil.show("两次密码不一致");
            return;
        }

        HttpUtil.setTradePass( mPwd, mPwdConfirm, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ToastUtil.show("设置成功");
                   SetTransactionPwdActivity.this.finish();
                } else {
                    ToastUtil.show(msg);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpConsts.SETTRADEPASS);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        modify();
    }
}
