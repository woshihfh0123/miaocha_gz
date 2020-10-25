package com.mc.phonelive.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.adapter.MainListAdapter;
import com.mc.phonelive.bean.ListBean;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.interfaces.OnItemClickListener;

/**
 * Created by cxf on 2018/9/27.
 * 首页 排行子页面的 父类
 */

public abstract class AbsMainListViewHolder extends AbsMainChildViewHolder implements OnItemClickListener<ListBean> {

    public static final String DAY = "day";
    public static final String WEEK = "week";
    public static final String MONTH = "month";
    public static final String TOTAL = "total";
    protected String mType;
    protected RefreshView mRefreshView;
    protected MainListAdapter mAdapter;

    public AbsMainListViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
        mType = DAY;
    }


    public void refreshData(String type) {
        if (TextUtils.isEmpty(type) || type.equals(mType)) {
            return;
        }
        mType = type;
        mFirstLoadData = true;
        loadData();
    }

    public void setCanRefresh(boolean canRefresh) {
        if (mRefreshView != null) {
            mRefreshView.setRefreshEnable(canRefresh);
        }
    }

    public void onFollowEvent(String touid, int isAttention) {
        if (mAdapter != null) {
            mAdapter.updateItem(touid, isAttention);
        }
    }

    @Override
    public void onItemClick(ListBean bean, int position) {
        UserHomeActivity.forward(mContext, bean.getUid());
    }
}
