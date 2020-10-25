package com.mc.phonelive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * 显示信息的dialog
 *
 * @author wwl
 */
public class SellYinDouDialog extends Dialog implements OnClickListener {

    private LayoutInflater factory;

    private EditText mCountEdit, mHandlingFeeEdit, mPhoneEdit, mPasswordEdit;
    private TextView confirm, cancel;
    private String mRate;//手续费比例
    private String mNums="";

    public SellYinDouDialog(Context context, String rate,String count) {
        super(context);
        factory = LayoutInflater.from(context);
        this.mRate = rate;
        this.mNums =count;
    }

    public SellYinDouDialog(Context context, int theme) {
        super(context, theme);
        factory = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题
        this.setContentView(factory.inflate(R.layout.sell_yindou_dialog, null));
        mCountEdit = (EditText) this.findViewById(R.id.yd_count);
        mCountEdit.setEnabled(false);
        mHandlingFeeEdit = (EditText) this.findViewById(R.id.yd_handlingfee);
        mPhoneEdit = (EditText) this.findViewById(R.id.yd_phone);
        mPasswordEdit = (EditText) this.findViewById(R.id.yd_password);
        confirm = this.findViewById(R.id.confirm);
        cancel = this.findViewById(R.id.cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        if (!DataSafeUtils.isEmpty(mNums)){
            try {
                mCountEdit.setText(mNums);
                NumberFormat nf = NumberFormat.getPercentInstance();
                Number m = nf.parse(mRate + "");
                mHandlingFeeEdit.setText(m.floatValue() * Integer.parseInt(mNums) + "");
            } catch (ParseException e) {
            }
        }

//        mCountEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!DataSafeUtils.isEmpty(s)) {
//                    if (s.toString().startsWith("0")){
//                        mCountEdit.setText("");
//                        return;
//                    }
//                    try {
//                        Long count = Long.parseLong(s.toString());
//                        NumberFormat nf = NumberFormat.getPercentInstance();
//                        Number m = nf.parse(mRate + "");
//                        mHandlingFeeEdit.setText(m.floatValue() * count + "");
//                    } catch (ParseException e) {
//
//                    }
//                } else {
//                    mHandlingFeeEdit.setText("");
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirm) { // 确定
            if (DataSafeUtils.isEmpty(mCountEdit.getText().toString())) {
                ToastUtil.show("请输入数量");
                return;
            }
            if (DataSafeUtils.isEmpty(mPhoneEdit.getText().toString())) {
                ToastUtil.show("请输入手机号");
                return;
            }
            if (DataSafeUtils.isEmpty(mPasswordEdit.getText().toString())) {
                ToastUtil.show("请输入交易密码");
                return;
            }
            doConfirmUp(mCountEdit.getText().toString(), mHandlingFeeEdit.getText().toString(), mPhoneEdit.getText().toString(), mPasswordEdit.getText().toString());
//            this.dismiss();
//            this.cancel();
        } else if (id == R.id.cancel) {
            this.dismiss();
            this.cancel();
        }
    }

    public void doConfirmUp(String count, String consume, String mobile, String password) {
    }

    public void doCancel(View v) {

    }

}
