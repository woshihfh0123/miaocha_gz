package com.mc.phonelive.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.LiveRoomTypeAdapter;
import com.mc.phonelive.bean.LiveRoomTypeBean;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.utils.DpUtil;

/**
 * Created by cxf on 2018/10/8.
 */

public class LiveRoomTypeDialogFragment extends AbsDialogFragment implements OnItemClickListener<LiveRoomTypeBean> {

    private RecyclerView mRecyclerView;
    private LiveRoomTypeAdapter mAdapter;
    private CommonCallback<LiveRoomTypeBean> mCallback;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_room_type;
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
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DpUtil.dp2px(80);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
//        int checkedId = bundle.getInt(Constants.CHECKED_ID, Constants.LIVE_TYPE_NORMAL);
        int checkedId = bundle.getInt(Constants.CHECKED_ID,-1);
        mAdapter = new LiveRoomTypeAdapter(mContext, checkedId);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(LiveRoomTypeBean bean, int position) {
        dismiss();
        if (mCallback != null) {
            mCallback.callback(bean);
        }
    }

    public void setCallback(CommonCallback<LiveRoomTypeBean> callback) {
        mCallback = callback;
    }

    @Override
    public void onDestroy() {
        mCallback = null;
        super.onDestroy();
    }
}
