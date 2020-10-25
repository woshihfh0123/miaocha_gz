package com.mc.phonelive.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

/**
 * 显示信息的dialog
 *
 * @author wwl
 */
public class SubmitGiveDialog extends Dialog implements OnClickListener {

    private LayoutInflater factory;

    private TextView dialogtitle, Nums, yindou_account_title;
    private EditText yindou_num_edit, account_edit;
    private TextView confirm, cancel;

    private String title, title1, yindouNum; // 显示的信息
    private String mType = "1";
    private String mVoteRate = "";//转换比例

    public SubmitGiveDialog(Context context, String title, String title1, String yindou, String type) {
        super(context);
        factory = LayoutInflater.from(context);
        this.title = title;
        this.title1 = title1;
        this.yindouNum = yindou;
        this.mType = type;
    }

    public SubmitGiveDialog(Context context, int theme) {
        super(context, theme);
        factory = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题
        this.setContentView(factory.inflate(R.layout.yindou_give_dialog, null));
        dialogtitle = (TextView) this.findViewById(R.id.dialogtitle);
        Nums = (TextView) this.findViewById(R.id.yindou_num);
        yindou_account_title = (TextView) this.findViewById(R.id.yindou_account_title);
        yindou_num_edit = (EditText) this.findViewById(R.id.yindou_num_edit);
        account_edit = (EditText) this.findViewById(R.id.yindou_account_edit);
        confirm = this.findViewById(R.id.confirm);
        cancel = this.findViewById(R.id.cancel);
        if (title != null && !title.equals("")) {
            dialogtitle.setText(title);
        }
        if (title1 != null && !title1.equals("")) {
            yindou_account_title.setText(title1);
        }
        if (mType.equals("2")) {
            account_edit.setEnabled(false);
            account_edit.setHint("");
        }
        if (yindouNum != null && !yindouNum.equals("")) {
            Nums.setText(yindouNum);
        }
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        if (!DataSafeUtils.isEmpty(AppConfig.getInstance().getConfig())) {
            if (!DataSafeUtils.isEmpty(AppConfig.getInstance().getConfig().getVote_rate()))
                mVoteRate = AppConfig.getInstance().getConfig().getVote_rate();
        }

        yindou_num_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (mType.equals("2")) {
                    if (!DataSafeUtils.isEmpty(s)) {
                        if (!DataSafeUtils.isEmpty(mVoteRate))
                            account_edit.setText((Integer.parseInt(s.toString()) * (Float.parseFloat(mVoteRate)) * 0.01) + "");
                    } else {
                        account_edit.setText("");
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirm) { // 确定
            if (mType.equals("1")) {
                if (DataSafeUtils.isEmpty(yindou_num_edit.getText().toString())) {
                    ToastUtil.show("请输入音豆数量");
                    return;
                }
                if (DataSafeUtils.isEmpty(account_edit.getText().toString())) {
                    ToastUtil.show("请输入转赠账户");
                    return;
                }
            } else {
                if (DataSafeUtils.isEmpty(yindou_num_edit.getText().toString())) {
                    ToastUtil.show("请输入音豆数量");
                    return;
                }
            }


            doConfirmUp(account_edit.getText().toString(), yindou_num_edit.getText().toString());
            this.dismiss();
            this.cancel();
        } else if (id == R.id.cancel) {
            doCancel(v);
            this.dismiss();
            this.cancel();
        }
    }

    public void doConfirmUp(String account, String count) {
    }

    public void doCancel(View v) {

    }

}
