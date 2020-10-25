package com.mc.phonelive.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.mc.phonelive.R;

/**
*
 * 账号与安全
 * 7777777
 */

public class HeiMingDanActivity extends AbsActivity{

    private RelativeLayout mRl_ewm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hmd;
    }

    @Override
    protected void main() {
        setTitle("隐私设置");
        mRl_ewm = (RelativeLayout) findViewById(R.id.rl_ewm);
        mRl_ewm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CenterHmdActivity.class));
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
