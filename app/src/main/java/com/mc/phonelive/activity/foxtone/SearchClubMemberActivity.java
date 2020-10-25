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
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.adapter.foxtone.SearchClubMemberAdapter;
import com.mc.phonelive.bean.foxtone.ClubMemberBean;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/10/25.
 * 搜索俱乐部成员
 */

public class SearchClubMemberActivity extends AbsActivity implements SearchClubMemberAdapter.InfoClubListener {

    private EditText mEditText;
    private RefreshView mRefreshView;
    private SearchClubMemberAdapter mSearchAdapter;
    private InputMethodManager imm;
    private String mKey;
    private MyHandler mHandler;
    private String mId="";//俱乐部id
    private String mFounder="";//是否是创始人
    public static void forward(Context context) {
        context.startActivity(new Intent(context, SearchClubMemberActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.search_club_layout;
    }

    @Override
    protected void main() {
        String id =this.getIntent().getStringExtra("id");
        if (!DataSafeUtils.isEmpty(id)){
            this.mId =id;
        }
        String type =this.getIntent().getStringExtra("type");
        if (!DataSafeUtils.isEmpty(type)){
            this.mFounder =type;
        }

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditText = (EditText) findViewById(R.id.edit);
        mEditText.setHint("搜索成员");
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
                HttpUtil.cancel(HttpConsts.CLUBGETCLUBUSERS);
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
        mRefreshView.setDataHelper(new RefreshView.DataHelper<ClubMemberBean.InfoBean>() {

            @Override
            public RefreshAdapter<ClubMemberBean.InfoBean> getAdapter() {
                if (mSearchAdapter == null) {
                    mSearchAdapter = new SearchClubMemberAdapter(mContext,mFounder);
                    mSearchAdapter.DelClubListener(SearchClubMemberActivity.this);
                }
                return mSearchAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                if (!TextUtils.isEmpty(mKey)) {
                    HttpUtil.getClubUsers(p, mId, mKey, callback);
                }
            }

            @Override
            public List<ClubMemberBean.InfoBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), ClubMemberBean.InfoBean.class);
            }

            @Override
            public void onRefresh(List<ClubMemberBean.InfoBean> list) {
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
        HttpUtil.cancel(HttpConsts.CLUBGETCLUBUSERS);
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
        HttpUtil.cancel(HttpConsts.CLUBGETCLUBUSERS);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.release();
        }
        mHandler = null;
        super.onDestroy();
    }

    @Override
    public void delClubData(String id, String touid) {
        HttpUtil.delUserClub(touid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code==0){
                    mRefreshView.initData();
                }
            }
        });
    }

    private static class MyHandler extends Handler {

        private SearchClubMemberActivity mActivity;

        public MyHandler(SearchClubMemberActivity activity) {
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
