package com.mc.phonelive.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.shop.AddressListAdapter;
import com.mc.phonelive.bean.AddressVO;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的地址管理
 */
public class MyAddressListActivity extends AbsActivity implements View.OnClickListener {

    private TextView mTitleView;

    @BindView(R.id.address_recyclerview)
    RecyclerView addressRecyclerview;
    @BindView(R.id.no_content)
    RelativeLayout no_content;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    AddressListAdapter mAddressAdapter;
    List<AddressVO.DataBean.InfoBean> mDataList = new ArrayList<>();
    AddressVO.DataBean.InfoBean bean = null;
    String mUid = "";
    String mToken = "";
    private String mAddressid = "";
    private int page = 1;
    private String mType = "";//判断是从个人中心进来，还是从其他地方进来，如果是从个人中心进来，点击列表，不作处理

    @Override
    protected int getLayoutId() {
        return R.layout.myaddress_activity;
    }


    @Override
    protected void main() {
        super.main();
        addressRecyclerview = findViewById(R.id.address_recyclerview);
        no_content = findViewById(R.id.no_content);
        refreshLayout = findViewById(R.id.refreshLayout);
        mTitleView = (TextView) findViewById(R.id.titleView);
        setBarModel(true);
        mTitleView.setText("地址管理");
        findViewById(R.id.btn_back).setOnClickListener(this);
        EventBus.getDefault().register(this);
        getAddressListAdapter();

        mUid = AppConfig.getInstance().getUid();
        mToken = AppConfig.getInstance().getToken();

        String addressid = this.getIntent().getStringExtra("addressid");
        if (!DataSafeUtils.isEmpty(addressid)) {
            this.mAddressid = addressid;
        }

        String type = this.getIntent().getStringExtra("id");
        if (!DataSafeUtils.isEmpty(type)) {//从个人中心进来
            this.mType = type;
        }
//        showDialog();
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initHttpData();
            }
        });
        initHttpData();
    }


    protected void initHttpData() {
        HttpUtil.getAddressList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (info.length > 0) {
                    Log.v("tags", "info=" + info[0]);
                    if (addressRecyclerview != null)
                        addressRecyclerview.setVisibility(View.VISIBLE);
                    if (no_content != null)
                        no_content.setVisibility(View.GONE);

                    if (info[0].length() > 0) {
                        mDataList.clear();
                        for (int i = 0; i < info.length; i++) {
                            AddressVO.DataBean.InfoBean infoBean = JSON.parseObject(info[i], AddressVO.DataBean.InfoBean.class);
                            mDataList.add(infoBean);
                        }
                        if (mAddressAdapter!=null) {
                            mAddressAdapter.setNewData(mDataList);
                        }
                    }
                } else {
                    if (addressRecyclerview != null)
                        addressRecyclerview.setVisibility(View.GONE);
                    if (no_content != null)
                        no_content.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void getAddressListAdapter() {
        mAddressAdapter = new AddressListAdapter(R.layout.address_adapter, mDataList);
        addressRecyclerview.setAdapter(mAddressAdapter);
        addressRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        mAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!DataSafeUtils.isEmpty(mType)) {
                    return;
                }
                selectAddress(mAddressAdapter.getData().get(position).getId(), mAddressAdapter.getData().get(position));
            }
        });
    }

    /**
     * 选择默认地址
     */
    private void selectAddress(final String mAddressId, final AddressVO.DataBean.InfoBean addressbean) {

        HttpUtil.AddAddress(mAddressId, addressbean.getReal_name() + "", addressbean.getPhone() + "", addressbean.getDistrict(), addressbean.getDetail(), "1", true, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ToastUtil.show(msg);
                    initHttpData();

                    EventBus.getDefault().post(new EventBusModel("FoodOrder_refresh", addressbean));
                    if (DataSafeUtils.isEmpty(mType)) {
                        MyAddressListActivity.this.finish();
                    }
                }
            }
        });
    }

    /**
     * 删除收货地址
     *
     * @param id
     * @param addressbean
     */
    private void deleteAddress(final String id, final AddressVO.DataBean.InfoBean addressbean) {
        HttpUtil.DelAddress(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ToastUtil.show(msg);
                    initHttpData();
                }
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick(R.id.address_add_layout)
    public void onViewClicked() {
        mContext.startActivity(new Intent(mContext, AddressAddActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        HttpUtil.cancel(HttpConsts.ADDRESS_GETLIST);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusModel event) {

        if ("address_delete".equals(event.getCode())) {

            //地址删除
            for (int i = 0; i < mAddressAdapter.getData().size(); i++) {
                if (mAddressAdapter.getData().get(i).getId().equals(event.getObject())) {
                    bean = mAddressAdapter.getItem(i);
//                    mAddressAdapter.getData().remove(i);
//                    mAddressAdapter.notifyDataSetChanged();
                    break;
                }
            }

            deleteAddress((String) event.getObject(), bean);
        } else if ("address_defalut".equals(event.getCode())) {

            //设置默认地址
            for (int i = 0; i < mAddressAdapter.getData().size(); i++) {
                if (mAddressAdapter.getData().get(i).getId().equals(event.getObject())) {
                    mAddressAdapter.getData().get(i).setIs_default("1");
                    bean = mAddressAdapter.getItem(i);
                } else {
                    mAddressAdapter.getData().get(i).setIs_default("0");
                }
            }
            mAddressAdapter.notifyDataSetChanged();
            selectAddress((String) event.getObject(), bean);
        } else if ("address_refresh".equals(event.getCode())) {
            //新增或者修改地址成功，刷新列表
            initHttpData();
            if (!DataSafeUtils.isEmpty(mAddressid)) {
                EventBus.getDefault().post(new EventBusModel("FoodOrder_refresh"));
            }

        }
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
