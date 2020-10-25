package com.mc.phonelive.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.LiveActivity;
import com.mc.phonelive.adapter.LiveAdminListAdapter;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.utils.WordUtil;

import java.util.List;

/**
 * Created by cxf on 2018/10/16.
 * 直播间管理
 */

public class LiveAdminListViewHolder extends AbsLivePageViewHolder implements OnItemClickListener<UserBean> {

    private String mLiveUid;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private LiveAdminListAdapter mLiveAdminListAdapter;
    private HttpCallback mHttpCallback;
    private String mTotalCount;


    public LiveAdminListViewHolder(Context context, ViewGroup parentView, String liveUid) {
        super(context, parentView);
        mLiveUid = liveUid;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_admin_list;
    }

    @Override
    public void init() {
        super.init();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mTextView = (TextView) findViewById(R.id.text);
        mHttpCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    List<UserBean> list = JSON.parseArray(obj.getString("list"), UserBean.class);
                    if (mLiveAdminListAdapter == null) {
                        mLiveAdminListAdapter = new LiveAdminListAdapter(mContext, list);
                        mLiveAdminListAdapter.setOnItemClickListener(LiveAdminListViewHolder.this);
                        mRecyclerView.setAdapter(mLiveAdminListAdapter);
                    } else {
                        mLiveAdminListAdapter.setList(list);
                    }
                    mTotalCount = obj.getString("total");
                    showTip();
                }
            }
        };
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onDestroy() {
                HttpUtil.cancel(HttpConsts.GET_ADMIN_LIST);
                HttpUtil.cancel(HttpConsts.SET_ADMIN);
            }
        };
    }

    private void showTip() {
        mTextView.setText(WordUtil.getString(R.string.live_admin_count) + "(" + mLiveAdminListAdapter.getItemCount() + "/" + mTotalCount + ")");
    }


    @Override
    public void loadData() {
        HttpUtil.getAdminList(mLiveUid, mHttpCallback);
    }

    @Override
    public void onHide() {
        if (mLiveAdminListAdapter != null) {
            mLiveAdminListAdapter.clear();
        }
    }

    @Override
    public void onItemClick(final UserBean bean, int position) {
        HttpUtil.setAdmin(mLiveUid, bean.getId(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    int res = JSON.parseObject(info[0]).getIntValue("isadmin");
                    if (res == 0) {//被取消管理员
                        if (mLiveAdminListAdapter != null) {
                            mLiveAdminListAdapter.removeItem(bean.getId());
                            showTip();
                        }
                        ((LiveActivity) mContext).sendSetAdminMessage(0, bean.getId(), bean.getUserNiceName());
                    }
                }
            }
        });
    }

    @Override
    public void release() {
        if (mLiveAdminListAdapter != null) {
            mLiveAdminListAdapter.release();
        }
        mLiveAdminListAdapter = null;
        super.release();
    }
}
