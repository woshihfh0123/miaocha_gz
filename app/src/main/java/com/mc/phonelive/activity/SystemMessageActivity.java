package com.mc.phonelive.activity;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import com.mc.phonelive.R;
import com.mc.phonelive.views.SystemMessageViewHolder;

/**
 * Created by cxf on 2018/11/24.
 * 系统消息
 */

public class SystemMessageActivity extends AbsActivity implements SystemMessageViewHolder.ActionListener {

    private SystemMessageViewHolder mSystemMessageViewHolder;

    public static void forward(Context context) {
        context.startActivity(new Intent(context, SystemMessageActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sys_msg;
    }

    @Override
    protected void main() {
        mSystemMessageViewHolder = new SystemMessageViewHolder(mContext, (ViewGroup) findViewById(R.id.root));
        mSystemMessageViewHolder.setActionListener(this);
        mSystemMessageViewHolder.addToParent();
        mSystemMessageViewHolder.loadData();
    }

    @Override
    protected void onDestroy() {
        if (mSystemMessageViewHolder != null) {
            mSystemMessageViewHolder.release();
        }
        super.onDestroy();
    }

    @Override
    public void onBackClick() {
        onBackPressed();
    }
}
