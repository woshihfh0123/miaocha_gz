package com.mc.phonelive.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.shop.MyCardActivity;

/**
*
 * 账号与安全
 * 7777777
 */

public class ZhangHaoAqActivity extends AbsActivity{

    private TextView mTv_mch;
    private RelativeLayout mRl_ewm;
    private TextView mTv_phone;
    private RelativeLayout mRl_psw;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zh_aq;
    }

    @Override
    protected void main() {
        setTitle("账号与安全");
        mTv_mch = (TextView) findViewById(R.id.tv_mch);
        mRl_ewm = (RelativeLayout) findViewById(R.id.rl_ewm);
        mTv_phone = (TextView) findViewById(R.id.tv_phone);
        mRl_psw = (RelativeLayout) findViewById(R.id.rl_psw);
        String phone=AppConfig.getInstance().getUserBean().getMobile();
        if(Utils.isNotEmpty(phone)&&phone.length()==11){
                phone=phone.substring(0,3)+"****"+phone.substring(7);
        }
            mTv_phone.setText(phone+"");

        mTv_mch.setText( AppConfig.getInstance().getUserBean().getLiangNameTip());
        mRl_ewm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MyCardActivity.class));
            }
        });
        mRl_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ModifyPwdActivity.class));
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
