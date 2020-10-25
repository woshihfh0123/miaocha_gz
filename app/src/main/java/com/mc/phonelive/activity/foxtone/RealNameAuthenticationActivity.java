package com.mc.phonelive.activity.foxtone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.RecommendActivity;
import com.mc.phonelive.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实名认证
 */
public class RealNameAuthenticationActivity extends AbsActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.right_View)
    TextView rightView;
    @BindView(R.id.step1_card_number)
    EditText step1CardNumber;
    @BindView(R.id.step1_realname)
    EditText step1Realname;
    @BindView(R.id.step2_submit)
    TextView step2Submit;
    @BindView(R.id.step3_submit)
    TextView step3Submit;
    private boolean mShowInvite;
    public static void forward(Context context, boolean showInvite) {
        Intent intent = new Intent(context, RealNameAuthenticationActivity.class);
        intent.putExtra(Constants.SHOW_INVITE, showInvite);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.realname_authenticaton_view;
    }

    @Override
    protected void main() {
        mShowInvite = getIntent().getBooleanExtra(Constants.SHOW_INVITE, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.right_View, R.id.step2_submit, R.id.step3_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_View:
                RecommendActivity.forward(mContext, mShowInvite);
                break;
            case R.id.step2_submit:
                ToastUtil.show("前去授权");
                break;
            case R.id.step3_submit:
                ToastUtil.show("前去支付");
                break;
        }
    }
}
