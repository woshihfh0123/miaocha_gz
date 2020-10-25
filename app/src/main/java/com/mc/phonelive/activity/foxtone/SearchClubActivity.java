package com.mc.phonelive.activity.foxtone;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.adapter.foxtone.SearchClubAdapter;
import com.mc.phonelive.bean.foxtone.ClubBean;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by cxf on 2018/10/25.
 * 搜索俱乐部
 */

public class SearchClubActivity extends AbsActivity implements SearchClubAdapter.InfoClubListener {

    private EditText mEditText;
    private RefreshView mRefreshView;
    private SearchClubAdapter mSearchAdapter;
    private InputMethodManager imm;
    private String mKey;
    private MyHandler mHandler;

    public static void forward(Context context) {
        context.startActivity(new Intent(context, SearchClubActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.search_club_layout;
    }

    @Override
    protected void main() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditText = (EditText) findViewById(R.id.edit);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HttpUtil.cancel(HttpConsts.GETCLUBLIST);
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
                if (!TextUtils.isEmpty(s)) {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(0, 500);
                    }
                } else {
                    mKey = null;
                    if (mSearchAdapter != null) {
                        mSearchAdapter.clearData();
                    }
                    if (mRefreshView != null) {
                        mRefreshView.setRefreshEnable(false);
                        mRefreshView.setLoadMoreEnable(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_search);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new RefreshView.DataHelper<ClubBean.InfoBean.ListBean>() {

            @Override
            public RefreshAdapter<ClubBean.InfoBean.ListBean> getAdapter() {
                if (mSearchAdapter == null) {
                    mSearchAdapter = new SearchClubAdapter(mContext, Constants.FOLLOW_FROM_SEARCH);
                    mSearchAdapter.AddClubListener(SearchClubActivity.this);
                }
                return mSearchAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                if (!TextUtils.isEmpty(mKey)) {
                    HttpUtil.getClubList(p+"",mKey, callback);
                }
            }

            @Override
            public List<ClubBean.InfoBean.ListBean> processData(String[] info) {
//                return JSON.parseArray(Arrays.toString(info), ClubBean.InfoBean.ListBean.class);
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    return JSON.parseArray(obj.getString("club_list"), ClubBean.InfoBean.ListBean.class);
                }
                return null;
            }

            @Override
            public void onRefresh(List<ClubBean.InfoBean.ListBean> list) {
                if (mRefreshView != null) {
                    mRefreshView.setRefreshEnable(true);
                }
            }

            @Override
            public void onNoData(boolean noData) {
                if (mRefreshView != null) {
                    mRefreshView.setRefreshEnable(false);
                }
            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (mRefreshView != null) {
                    if (dataCount > 0) {
                        mRefreshView.setLoadMoreEnable(true);
                    }else{
                        mRefreshView.setLoadMoreEnable(false);
                    }
                }
            }
        });
        mHandler = new MyHandler(this);
    }

    private void search() {
        String key = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            ToastUtil.show(R.string.content_empty);
            return;
        }
        HttpUtil.cancel(HttpConsts.GETCLUBLIST);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mKey = key;
        mRefreshView.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshView.initData();
    }

    @Override
    protected void onDestroy() {
        if (imm != null && mEditText != null) {
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
        HttpUtil.cancel(HttpConsts.GETCLUBLIST);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.release();
        }
        mHandler = null;
        super.onDestroy();
    }

    @Override
    public void addClubData(String id) {
        HttpUtil.joinClub(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
            }

            @Override
            public void onError() {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private static class MyHandler extends Handler {

        private SearchClubActivity mActivity;

        public MyHandler(SearchClubActivity activity) {
            mActivity = new WeakReference<>(activity).get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null) {
                mActivity.search();
            }
        }

        public void release() {
            mActivity = null;
        }
    }


}
