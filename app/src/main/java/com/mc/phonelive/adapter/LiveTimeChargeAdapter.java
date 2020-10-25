package com.mc.phonelive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.ConfigBean;
import com.mc.phonelive.bean.LiveTimeChargeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/8.
 * 直播间计时收费类型
 */

public class LiveTimeChargeAdapter extends RecyclerView.Adapter<LiveTimeChargeAdapter.Vh> {

    private List<LiveTimeChargeBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition = -1;
    private String mCoinName;

    public LiveTimeChargeAdapter(Context context, int checkedCoin) {
        mList = new ArrayList<>();
        ConfigBean configBean = AppConfig.getInstance().getConfig();
        if (configBean != null) {
            mCoinName = configBean.getCoinName();
            String[] coins = configBean.getLiveTimeCoin();
            if (coins != null) {
                for (int i = 0, length = coins.length; i < length; i++) {
                    int coin = Integer.parseInt(coins[i]);
                    LiveTimeChargeBean bean = new LiveTimeChargeBean(coin);
                    if (coin == checkedCoin) {
                        bean.setChecked(true);
                        mCheckedPosition = i;
                    }
                    mList.add(bean);
                }
                if (mCheckedPosition < 0) {
                    mCheckedPosition = 0;
                    mList.get(0).setChecked(true);
                }
            }
        }
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    if (mCheckedPosition == position) {
                        return;
                    }
                    if (mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
                        mList.get(mCheckedPosition).setChecked(false);
                        notifyItemChanged(mCheckedPosition);
                    }
                    mList.get(position).setChecked(true);
                    notifyItemChanged(position);
                    mCheckedPosition = position;
                }
            }
        };
    }


    public int getCheckedCoin() {
        if (mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
            return mList.get(mCheckedPosition).getCoin();
        }
        return 0;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_time_charge, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        vh.setData(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTextView;

        public Vh(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(LiveTimeChargeBean bean, int position) {
            itemView.setTag(position);
            mTextView.setText(bean.getCoin() + "/" + mCoinName);
            if (bean.isChecked()) {
                mTextView.setTextColor(0xffffdd00);
            } else {
                mTextView.setTextColor(0xffffffff);
            }
        }
    }
}
