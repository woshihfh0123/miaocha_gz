package com.mc.phonelive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

/**
 * 显示信息的dialog
 *
 * @author wwl
 */
public class BuyYinDouDialog extends Dialog implements OnClickListener {

    private LayoutInflater factory;

    private EditText mCountEdit, mPriceEdit, mPasswordEdit;
    private RelativeLayout btnlayout01, btnlayout02;
    private ImageView btn01, btn02;
    private TextView confirm, cancel;

    private String mType = "1";


    public BuyYinDouDialog(Context context) {
        super(context);
        factory = LayoutInflater.from(context);
    }

    public BuyYinDouDialog(Context context, int theme) {
        super(context, theme);
        factory = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题
        this.setContentView(factory.inflate(R.layout.buy_yindou_dialog, null));
        mCountEdit = (EditText) this.findViewById(R.id.yd_count);
        mPriceEdit = (EditText) this.findViewById(R.id.yd_price);
        mPasswordEdit = (EditText) this.findViewById(R.id.yd_password);
        btnlayout01 = (RelativeLayout) this.findViewById(R.id.btn01_layout);
        btnlayout02 = (RelativeLayout) this.findViewById(R.id.btn02_layout);
        btn01 = this.findViewById(R.id.btn01);
        btn02 = this.findViewById(R.id.btn02);
        confirm = this.findViewById(R.id.confirm);
        cancel = this.findViewById(R.id.cancel);
        btnlayout01.setOnClickListener(this);
        btnlayout02.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirm) { // 确定
            if (DataSafeUtils.isEmpty(mCountEdit.getText().toString())) {
                ToastUtil.show("请输入数量");
                return;
            }
            if (DataSafeUtils.isEmpty(mPriceEdit.getText().toString())) {
                ToastUtil.show("请输入单价");
                return;
            }

            if (DataSafeUtils.isEmpty(mPasswordEdit.getText().toString()) || mPasswordEdit.getText().toString().length()<6) {
                ToastUtil.show("请输入6位交易密码");
                return;
            }
            doConfirmUp(mCountEdit.getText().toString(), mPriceEdit.getText().toString(), mPasswordEdit.getText().toString(), mType);
//            this.dismiss();
//            this.cancel();
        } else if (id == R.id.cancel) {
            this.dismiss();
            this.cancel();
        } else if (id == R.id.btn01_layout) {
            mType = "2";
            btn01.setImageResource(R.mipmap.choise_selected_img);
            btn02.setImageResource(R.mipmap.choise_nomal_img);
        } else if (id == R.id.btn02_layout) {
            mType = "1";
            btn01.setImageResource(R.mipmap.choise_nomal_img);
            btn02.setImageResource(R.mipmap.choise_selected_img);
        }
    }

    public void doConfirmUp(String count, String price, String password, String type) {
    }

    public void doCancel(View v) {

    }

}
