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

/**
 * 显示信息的dialog
 *
 * @author wwl
 */
public class ApplyDialog extends Dialog implements OnClickListener {

    private LayoutInflater factory;

    private EditText apply_edit;
    private TextView confirm, cancel;


    public ApplyDialog(Context context) {
        super(context);
        factory = LayoutInflater.from(context);
    }

    public ApplyDialog(Context context, int theme) {
        super(context, theme);
        factory = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题
        this.setContentView(factory.inflate(R.layout.trading_apply_dialog, null));
        apply_edit = (EditText) this.findViewById(R.id.apply_edit);
        confirm = this.findViewById(R.id.confirm);
        cancel = this.findViewById(R.id.cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirm) { // 确定
            doConfirmUp(apply_edit.getText().toString());
            this.dismiss();
            this.cancel();
        } else if (id == R.id.cancel) {
            this.dismiss();
            this.cancel();
        }
    }

    public void doConfirmUp(String editTv) {
    }

    public void doCancel(View v) {

    }

}
