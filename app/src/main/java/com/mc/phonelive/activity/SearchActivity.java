package com.mc.phonelive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.adapter.SearchAdapter;
import com.mc.phonelive.adapter.shop.HistAdapter;
import com.mc.phonelive.bean.SearchUserBean;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/10/25.
 * 搜索
 */

public class SearchActivity extends AbsActivity {

    private EditText mEditText;
    private RefreshView mRefreshView;
    private SearchAdapter mSearchAdapter;
    private InputMethodManager imm;
    private String mKey;
    private MyHandler mHandler;
    private TextView tv_clear;
    private RecyclerView recyclerView;
    private HistAdapter hsdAdapter;
    private LinearLayout ll_has_ls;
    private boolean isClick=false;
    public static void forward(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void main() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditText = (EditText) findViewById(R.id.edit);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        ll_has_ls = (LinearLayout) findViewById(R.id.ll_has_ls);
        recyclerView=findViewById(R.id.recyclerView);
        hsdAdapter=new HistAdapter();
        recyclerView.setLayoutManager(Utils.getLvManager(mContext));
        recyclerView.setAdapter(hsdAdapter);
        List<String>list= AppConfig.getInstance().getUserBean().getHistList();
        if(Utils.isNotEmpty(list)){
            ll_has_ls.setVisibility(View.VISIBLE);
            hsdAdapter.setNewData(list);
        }else{
            ll_has_ls.setVisibility(View.GONE);
        }
        hsdAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                hsdAdapter.remove(position);
                List<String>list= hsdAdapter.getData();
                AppConfig.getInstance().getUserBean().setHistList(list);

            }
        });
        hsdAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                isClick=true;
                mKey=hsdAdapter.getItem(position);
                mEditText.setText(mKey);
                ll_has_ls.setVisibility(View.GONE);
                search();
            }
        });
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hsdAdapter.setNewData(null);
                AppConfig.getInstance().getUserBean().setHistList(null);
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                ll_has_ls.setVisibility(View.GONE);
                isClick=false;
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

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_search);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new RefreshView.DataHelper<SearchUserBean>() {

            @Override
            public RefreshAdapter<SearchUserBean> getAdapter() {
                if (mSearchAdapter == null) {
                    mSearchAdapter = new SearchAdapter(mContext, Constants.FOLLOW_FROM_SEARCH);
                    mSearchAdapter.setOnItemClickListener(new OnItemClickListener<SearchUserBean>() {
                        @Override
                        public void onItemClick(SearchUserBean bean, int position) {
                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                            UserHomeActivity.forward(mContext, bean.getId());
                        }
                    });
                }
                return mSearchAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                if (!TextUtils.isEmpty(mKey)) {
                    List<String>list= AppConfig.getInstance().getUserBean().getHistList();
                    if(!isClick){
                        if(Utils.isNotEmpty(list)){
                            list.add(mKey);
                            AppConfig.getInstance().getUserBean().setHistList(list);
                        }else{
                            List<String>nlist=new ArrayList<>();
                            nlist.add(mKey);
                            AppConfig.getInstance().getUserBean().setHistList(nlist);
                        }
                    }
                    HttpUtil.search(mKey, p, callback);
                }
            }

            @Override
            public List<SearchUserBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), SearchUserBean.class);
            }

            @Override
            public void onRefresh(List<SearchUserBean> list) {
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
        HttpUtil.cancel(HttpConsts.SEARCH);
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
//        if (imm != null && mEditText != null) {
//            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
//        }
        HttpUtil.cancel(HttpConsts.SEARCH);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.release();
        }
        mHandler = null;
        super.onDestroy();
    }

    private static class MyHandler extends Handler {

        private SearchActivity mActivity;

        public MyHandler(SearchActivity activity) {
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
