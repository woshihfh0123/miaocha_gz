package com.mc.phonelive.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.MainShopMallListAdapter;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.bean.ShopMallGoodsList;
import com.mc.phonelive.custom.ItemDecoration;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by cxf on 2018/10/25.
 * 搜索商品
 */

public class ShopMallGoodsSearchActivity extends AbsActivity {

    private EditText mEditText;
    private RefreshView mRefreshView;
    private MainShopMallListAdapter mSearchAdapter;
    private InputMethodManager imm;
    private String mKey;
    private MyHandler mHandler;
    private int page = 1;

    public static void forward(Context context) {
        context.startActivity(new Intent(context, ShopMallGoodsSearchActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopmall_goods_search;
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
                page = 1;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HttpUtil.cancel(HttpConsts.HOME_GETSHOP);
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
                        mRefreshView.showNoData();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_search);
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 5, 5);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<ShopMallGoodsList>() {

            @Override
            public RefreshAdapter<ShopMallGoodsList> getAdapter() {
                if (mSearchAdapter == null) {
                    mSearchAdapter = new MainShopMallListAdapter(mContext, MainShopMallListAdapter.TYPE_PROFIT);
//                    mSearchAdapter.setOnItemClickListener(new OnItemClickListener<ShopMallGoodsList>() {
//                        @Override
//                        public void onItemClick(ShopMallGoodsList bean, int position) {
//                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
////                            UserHomeActivity.forward(mContext, bean.getId());
//                        }
//                    });
                }
                return mSearchAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                if (!TextUtils.isEmpty(mKey)) {
                    HttpUtil.GetShopMall(page + "", "1","", "", mKey, callback);
                }
            }

            @Override
            public List<ShopMallGoodsList> processData(String[] info) {
//                return JSON.parseArray(Arrays.toString(info), ShopMallGoodsList.class);
                try {
                    JSONObject resJson = new JSONObject(info[0]);
                    List<ShopMallGoodsList> goodsList = JSON.parseArray(resJson.getString("list"), ShopMallGoodsList.class);
                    Log.v("tags", goodsList.size() + "---sze");
                    if (!DataSafeUtils.isEmpty(goodsList) && goodsList.size() > 0) {
                        mRefreshView.hideNoData();
                        return goodsList;
                    } else {
                        if (page == 1)
                            mRefreshView.showNoData();
                    }
                } catch (JSONException e) {

                }
                return null;
            }

            @Override
            public void onRefresh(List<ShopMallGoodsList> list) {
                if (mRefreshView != null) {
                    mRefreshView.setRefreshEnable(true);
                }
            }

            @Override
            public void onNoData(boolean noData) {
                Log.v("tags", "nodata=" + noData);
                if (mRefreshView != null) {
                    mRefreshView.setRefreshEnable(false);

                }
            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                if (mRefreshView != null) {
                    if (dataCount > 0) {
                        mRefreshView.setLoadMoreEnable(true);
                    } else {
                        mRefreshView.setLoadMoreEnable(false);
                    }
                }
            }
        });
        mHandler = new MyHandler(this);
    }

    private void search() {
        page = 1;
        String key = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            ToastUtil.show(R.string.content_empty);
            return;
        }
        HttpUtil.cancel(HttpConsts.HOME_GETSHOP);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mKey = key;
        mRefreshView.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mRefreshView.initData();
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpConsts.HOME_GETSHOP);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.release();
        }
        mHandler = null;
        super.onDestroy();
    }

    private static class MyHandler extends Handler {

        private ShopMallGoodsSearchActivity mActivity;

        public MyHandler(ShopMallGoodsSearchActivity activity) {
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
