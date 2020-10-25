package com.mc.phonelive.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

/**
 * Created by cxf on 2018/10/7.
 * 重置密码
 */

public class ModifyPwdActivity extends AbsActivity implements View.OnClickListener {

    private EditText mEditOld;
    private EditText mEditNew;
    private EditText mEditConfirm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_pwd;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.modify_pwd));
        mEditOld = (EditText) findViewById(R.id.edit_old);
        mEditNew = (EditText)findViewById(R.id.edit_new);
        mEditConfirm = (EditText)findViewById(R.id.edit_confirm);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        modify();
    }

    private void modify() {
        String pwdOld = mEditOld.getText().toString().trim();
        if (TextUtils.isEmpty(pwdOld)) {
            mEditOld.setError(WordUtil.getString(R.string.modify_pwd_old_1));
            return;
        }
        String pwdNew = mEditNew.getText().toString().trim();
        if (TextUtils.isEmpty(pwdNew)) {
            mEditNew.setError(WordUtil.getString(R.string.modify_pwd_new_1));
            return;
        }
        String pwdConfirm = mEditConfirm.getText().toString().trim();
        if (TextUtils.isEmpty(pwdConfirm)) {
            mEditConfirm.setError(WordUtil.getString(R.string.modify_pwd_confirm_1));
            return;
        }
        if (!pwdNew.equals(pwdConfirm)) {
            mEditConfirm.setError(WordUtil.getString(R.string.reg_pwd_error));
            return;
        }
        HttpUtil.modifyPwd(pwdOld, pwdNew, pwdConfirm, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ToastUtil.show(JSON.parseObject(info[0]).getString("msg"));
                    finish();
                } else {
                    ToastUtil.show(msg);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpConsts.MODIFY_PWD);
        super.onDestroy();
    }
}
