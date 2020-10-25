package com.mc.phonelive.views.shopmall;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.mc.phonelive.adapter.MainShopMallListAdapter;
import com.mc.phonelive.bean.ShopMallGoodsList;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.views.AbsMainChildViewHolder;

/**
 * Created by cxf on 2018/9/27.
 * 商城子页面的 父类
 */

public abstract class AbsMainShopMallListViewHolder extends AbsMainChildViewHolder implements OnItemClickListener<ShopMallGoodsList> {

    public static final String DAY = "day";
    protected String mType;
    protected RefreshView mRefreshView;
    protected MainShopMallListAdapter mAdapter;

    public AbsMainShopMallListViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
        mType = "1";
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

}
