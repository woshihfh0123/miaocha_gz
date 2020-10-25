package com.bs.xyplibs.base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;


import com.bs.xyplibs.R;

import java.util.List;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2018/6/15.
 */

public abstract class RecyclerBaseAdapter<M> extends BGARecyclerViewAdapter {
    public Activity mActivity;
    public boolean mIsEmpty = true;

    public TextView mBaseAdapterTv;


    public abstract void initData(BGAViewHolderHelper helper, int position, Object model);

    public RecyclerBaseAdapter(RecyclerView recyclerView, int itemLayoutId) {
        super(recyclerView, itemLayoutId);
        mActivity = (Activity) recyclerView.getContext();

    }

    @Override
    public int getItemCount() {
        if (mData == null || mData.size() == 0) {
            return 1;
        } else {
            return mData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
//        &&!mIsEmpty
        if (mData.size() == 0) {
            return R.layout.base_adapter;
        } else {
            mIsEmpty = false;
            return super.getItemViewType(position);
        }

    }

    public M getItem(int position) {
        if (mData.size() != 0) {
            return (M) this.mData.get(position);
        } else {
            return null;
        }

    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, Object model) {
        if (mData.size() > 0)
            initData(helper, position, model);
    }

    public void updateData(List<M> data) {
        if (data != null) {
            mData = data;
        } else {
            mData.clear();
        }
        notifyDataSetChanged();
    }

    public void updateDataFrist(List<M> data) {
        if (data != null) {
            mData.addAll(0, data);
            notifyDataSetChanged();
        }
    }

    public void updateDataLast(List<M> data) {
        if (data != null) {
            mData.addAll(mData.size(), data);
//            mData.addAll(data);
            notifyDataSetChanged();
        }
    }
}
