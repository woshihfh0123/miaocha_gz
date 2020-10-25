package com.mc.phonelive.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.views.LiveUserHomeViewHolderMenu;

/**
 * 个人主页666666
 */

public class UserHomeActivityMenu extends AbsActivity {
//mContext.startActivity(new Intent(mContext, SettingActivity.class));
    private LiveUserHomeViewHolderMenu mLiveUserHomeViewHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_empty;
    }

    @Override
    protected void main() {
        String toUid = getIntent().getStringExtra(Constants.TO_UID);
        if (TextUtils.isEmpty(toUid)) {
            return;
        }
        mLiveUserHomeViewHolder = new LiveUserHomeViewHolderMenu(mContext, (ViewGroup) findViewById(R.id.container), toUid);
        addAllLifeCycleListener(mLiveUserHomeViewHolder.getLifeCycleListenerList());
        mLiveUserHomeViewHolder.addToParent();
        mLiveUserHomeViewHolder.loadData();
    }

    public static void forward(Context context, String toUid) {
        Intent intent = new Intent(context, UserHomeActivityMenu.class);
        intent.putExtra(Constants.TO_UID, toUid);
        context.startActivity(intent);
    }

    public void addImpress(String toUid) {
        Intent intent = new Intent(mContext, LiveAddImpressActivity.class);
        intent.putExtra(Constants.TO_UID, toUid);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (mLiveUserHomeViewHolder != null) {
                mLiveUserHomeViewHolder.refreshImpress();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mLiveUserHomeViewHolder != null) {
            mLiveUserHomeViewHolder.release();
        }
        super.onDestroy();
    }
}
