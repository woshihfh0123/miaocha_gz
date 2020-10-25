package com.mc.phonelive.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.CourierBan;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by WWL on 2020/4/9 0009:11
 * 快递公司选择
 */
public class CourierCompanyChoiseActivity extends AbsActivity {
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_title)
    FrameLayout viewTitle;
    @BindView(R.id.courier_recyclerview)
    RecyclerView courierRecyclerview;
    List<CourierBan.DataBean.InfoBean> mInfolist = new ArrayList<>();
    BaseQuickAdapter<CourierBan.DataBean.InfoBean, BaseViewHolder> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.order_couriercompany_choise;
    }

    @Override
    protected void main() {
        super.main();
        titleView.setText("选择快递");

        setAdapter(mInfolist);
        initHttpData();
    }

    private void initHttpData() {
        HttpUtil.OrderLogisticsList("1", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (info.length > 0) {
                    Log.v("tags", code + "---" + msg + "----" + info[0]);
                    try {
                        mInfolist = JSON.parseArray(Arrays.toString(info), CourierBan.DataBean.InfoBean.class);
                        if (mAdapter != null)
                            mAdapter.setNewData(mInfolist);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }


    private void setAdapter(List<CourierBan.DataBean.InfoBean> mInfolist) {
        mAdapter = new BaseQuickAdapter<CourierBan.DataBean.InfoBean, BaseViewHolder>(R.layout.order_couriercompany_item, mInfolist) {
            @Override
            protected void convert(BaseViewHolder helper, CourierBan.DataBean.InfoBean item) {
                if (!DataSafeUtils.isEmpty(item.getName())) {
                    helper.setText(R.id.kuaidi_tv, item.getName());
                }
            }
        };
        courierRecyclerview.setAdapter(mAdapter);
        courierRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourierBan.DataBean.InfoBean data = mAdapter.getData().get(position);
                Intent intent = new Intent(mContext, OrderDeliveryActivity.class);
                intent.putExtra("name", data.getName());
                intent.putExtra("code", data.getCode());
                intent.putExtra("id", data.getId());
                setResult(RESULT_OK, intent);
                CourierCompanyChoiseActivity.this.finish();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        this.finish();
    }

    @Override
    protected void onDestroy01() {
        HttpUtil.cancel(HttpConsts.ORDERLOGISTICSLIST);
    }
}
