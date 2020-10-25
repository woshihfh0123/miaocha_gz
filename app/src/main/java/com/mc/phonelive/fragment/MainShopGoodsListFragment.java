package com.mc.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.MainShopMallListAdapter;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.bean.ShopMallGoodsList;
import com.mc.phonelive.custom.ItemDecoration;
import com.mc.phonelive.custom.RefreshView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 商品列表
 * created by WWL on 2020/4/17 0017:14
 */
public class MainShopGoodsListFragment extends BaseFragment {
    @BindView(R.id.refreshView)
    RefreshView mRefreshView;
    Unbinder unbinder;
    View layoutView;
    private MainShopMallListAdapter mAdapter;
    private String state = "";//
    private int mCurrentPage = 1;

    public static MainShopGoodsListFragment newInstance() {
        MainShopGoodsListFragment fragment = new MainShopGoodsListFragment();
        return fragment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.shopmall_goodslist_fragment, container, false);
        return layoutView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView(view);

    }


    protected void initView(View view) {
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }

        mRefreshView = (RefreshView) layoutView.findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(Objects.requireNonNull(getActivity()), 0x00000000, 5, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<ShopMallGoodsList>() {
            @Override
            public RefreshAdapter<ShopMallGoodsList> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MainShopMallListAdapter(getActivity(), MainShopMallListAdapter.TYPE_PROFIT);
//                    mAdapter.setOnItemClickListener(MainShopMainListViewHolder.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                mCurrentPage =p;
                Log.v("tags", state + "----------------------type");
                HttpUtil.GetShopMall(p + "", "1",state, "","", callback);
            }


            @Override
            public List<ShopMallGoodsList> processData(String[] info) {
                try {
                    JSONObject resJson = new JSONObject(info[0]);
                    List<ShopMallGoodsList> goodsList = JSON.parseArray(resJson.getString("list"), ShopMallGoodsList.class);

                    if (!DataSafeUtils.isEmpty(goodsList) && goodsList.size() > 0) {
                        return goodsList;
                    }else{
                        if (mCurrentPage==1){
                            mRefreshView.showNoData();
                        }else{
                            mRefreshView.hideNoData();
                        }
                    }
                } catch (JSONException e) {

                }
                return null;
            }

            @Override
            public void onRefresh(List<ShopMallGoodsList> list) {
                Log.v("tags", "刷新--------");
            }

            @Override
            public void onNoData(boolean noData) {
            Log.v("tags",noData+"------nodata--");
                if (mRefreshView != null) {
                    mRefreshView.setRefreshEnable(false);
                }
            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
                Log.v("tags", dataCount+"----dataCount---");
                if (dataCount < 20) {
                    mRefreshView.setLoadMoreEnable(false);
                } else {
                    mRefreshView.setLoadMoreEnable(true);
                }

            }
        });

    }


    @Override
    protected void initHttpData() {
            if (mRefreshView != null) {
                mRefreshView.initData();
            }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventBusModel eventBusModel) {
        String code = eventBusModel.getCode();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        HttpUtil.cancel(HttpConsts.HOME_GETSHOP);
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered) {
            EventBus.getDefault().unregister(this);
        }
    }

}
