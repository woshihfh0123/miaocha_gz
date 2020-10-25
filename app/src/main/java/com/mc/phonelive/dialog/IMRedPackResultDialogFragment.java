package com.mc.phonelive.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.bean.RedPackBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.DpUtil;
import com.mc.phonelive.utils.WordUtil;

/**
 * Created by cxf on 2018/11/21.
 * 红包领取详情弹窗
 */

public class IMRedPackResultDialogFragment extends AbsDialogFragment {

    private ImageView mAvatar;
    private TextView mName;
    private TextView mWinCoin;
    private TextView mCoinNameTextView;
    private TextView tips;
    private View mWinGroup;
    private TextView mNum;
    private RedPackBean mRedPackBean;
    private String mStream;
    private String mCoinName;


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_im_red_pack_result;
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
        params.height = DpUtil.dp2px(360);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }


    public void setRedPackBean(RedPackBean redPackBean) {
        mRedPackBean = redPackBean;
    }

    public void setStream(String stream) {
        mStream = stream;
    }

    public void setCoinName(String coinName) {
        mCoinName = coinName;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mRedPackBean == null || TextUtils.isEmpty(mStream)) {
            return;
        }
        mAvatar = mRootView.findViewById(R.id.avatar);
        tips = mRootView.findViewById(R.id.tips);
        mName = mRootView.findViewById(R.id.name);
        mWinGroup = mRootView.findViewById(R.id.win_group);
        mWinCoin = mRootView.findViewById(R.id.win_coin);
        mCoinNameTextView = mRootView.findViewById(R.id.coin_name);
        mNum = mRootView.findViewById(R.id.num);

            if (mAvatar != null) {
                ImgLoader.displayAvatar(mRedPackBean.getAvatar(), mAvatar);
            }
            if (mName != null) {
                mName.setText(String.format(WordUtil.getString(R.string.red_pack_17), mRedPackBean.getTitle()));
            }
            if (mNum != null) {
                if (mRedPackBean.getSendType()==1) {
                    mNum.setText(String.format(WordUtil.getString(R.string.red_pack_125), 1 / 1, mRedPackBean.getCoin(),
                            mCoinName));
                }else{
                    mNum.setText(String.format(WordUtil.getString(R.string.red_pack_19), 1 / 1, mRedPackBean.getCoin(),
                            mCoinName));
                }
            }
            if (tips!=null){
                tips.setText(mRedPackBean.getmTip());
            }
    }
}
