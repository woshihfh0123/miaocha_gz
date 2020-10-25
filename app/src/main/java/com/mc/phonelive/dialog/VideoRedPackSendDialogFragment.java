package com.mc.phonelive.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.custom.RatioRoundImageView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DpUtil;
import com.mc.phonelive.utils.ToastUtil;

/**
 * Created by cxf on 2018/11/19.
 * 发红包的弹窗
 */

public class VideoRedPackSendDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private EditText mEditCoinPsq;//拼手气钻石数量
    private TextView mBtnSend;//发送按钮
    private RatioRoundImageView mUserImg;
    private TextView mUserName;
    private int mRedPackType;
    private String mStream;
    private String mCoinPsq = "";
    private VideoBean mVideoBean;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_video_red_pack_send;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(280);
        params.height = DpUtil.dp2px(390);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }


    public void setStream(String stream) {
        mStream = stream;
    }

    public void setUserMsg(VideoBean mvideo) {
        this.mVideoBean = mvideo;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (TextUtils.isEmpty(mStream)) {
            return;
        }
        mBtnSend = mRootView.findViewById(R.id.btn_send);
        mUserImg = mRootView.findViewById(R.id.userimg);
        mUserName = mRootView.findViewById(R.id.username);
        mBtnSend.setOnClickListener(this);
        mBtnSend.setText("打赏 " + mCoinPsq + "钻");
        mRedPackType = Constants.RED_PACK_TYPE_SHOU_QI;
        mEditCoinPsq = mRootView.findViewById(R.id.edit_coin_psq1);
//        mEditCoinPsq.setText("");

        AppConfig appConfig = AppConfig.getInstance();
        UserBean user = appConfig.getUserBean();
        if (!DataSafeUtils.isEmpty(user)) {
            Glide.with(mContext).load(user.getAvatar()).into(mUserImg);
            mUserName.setText(user.getUserNiceName());
        }

        mEditCoinPsq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String c = s.toString();
                if (TextUtils.isEmpty(c)) {
                    mCoinPsq = "0";
                } else {
                    mCoinPsq = c;
                }
                mBtnSend.setText("打赏 " + mCoinPsq + "钻");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send://发红包按钮
                mCoinPsq = mEditCoinPsq.getText().toString();
                sendRedPack();
                break;
        }
    }

    /**
     * 发红包
     */
    private void sendRedPack() {
        if (mVideoBean.getUid().equals(AppConfig.getInstance().getUid())) {
            ToastUtil.show("不能打赏自己");
            return;
        }

        RedpackDialog oRderDialog = new RedpackDialog(getActivity(), "打赏", "确定要打赏吗？") {
            @Override
            public void doConfirmUp() {
                if (!ButtonUtils.isFastDoubleClick02())
                    HttpUtil.RedSendReward(mVideoBean.getUid(), mVideoBean.getId(), mCoinPsq, new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0) {
                                dismiss();
                            }
                            ToastUtil.show(msg);
                        }
                    });
            }
        };
        oRderDialog.show();

    }

    @Override
    public void onDestroy() {
//        HttpUtil.cancel(HttpConsts.SEND_RED_PACK);
        super.onDestroy();
    }
}
