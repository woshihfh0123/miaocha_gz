package com.mc.phonelive.activity.foxtone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.foxtone.YinDouAdapter;
import com.mc.phonelive.bean.foxtone.YindouBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 音分值
 */
public class MyYinfenzhiActivity extends AbsActivity {
    @BindView(R.id.yfz_num)
    TextView yfzNum;
    @BindView(R.id.me_yfz_level)
    TextView meYfzLevel;
    @BindView(R.id.Level_bar)
    ProgressBar LevelBar;
    @BindView(R.id.level_scale)
    TextView levelScale;
    @BindView(R.id.yfz_recyclerview)
    RecyclerView yfzRecyclerview;
    @BindView(R.id.no_data)
    TextView noData;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<YindouBean.InfoBean.ListBean> mList = new ArrayList<>();
    private YinDouAdapter mDouListAdapter;

    private int pages = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.yinfenzhi_layout;
    }

    @Override
    protected void main() {
        setTitle("我的音分值");

        getListAdapter(mList);

        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pages++;
                initHttpData();
            }
        });
        showDialog();
        initHttpData();
    }

    private void initHttpData() {

        HttpUtil.getProfitCentList(pages, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                dismissDialog();
                if (!DataSafeUtils.isEmpty(info)) {
                    List<YindouBean.InfoBean> data = JSON.parseArray(Arrays.toString(info), YindouBean.InfoBean.class);
                    if (!DataSafeUtils.isEmpty(data) && data.size() > 0) {
                        if (!DataSafeUtils.isEmpty(data.get(0).getProfits())) {
                            getYfzData(data.get(0).getProfits());
                        }
                        if (!DataSafeUtils.isEmpty(data.get(0).getList()) && data.get(0).getList().size() > 0) {
                            if (noData != null) {
                                noData.setVisibility(View.GONE);
                            }
                            if (yfzRecyclerview != null) {
                                yfzRecyclerview.setVisibility(View.VISIBLE);
                            }
                            if (mDouListAdapter != null) {
                                if (pages == 1) {
                                    mDouListAdapter.setNewData(data.get(0).getList());
                                } else {
                                    mDouListAdapter.addData(data.get(0).getList());
                                }
                            }
                            if (data.get(0).getList().size() < 15) {
                                if (refreshLayout != null) {
                                    refreshLayout.setEnableLoadMore(false);
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            } else {
                                if (refreshLayout != null) {
                                    refreshLayout.setEnableLoadMore(true);
                                }
                            }
                        } else {
                            if (noData != null) {
                                noData.setVisibility(View.VISIBLE);
                            }
                            if (yfzRecyclerview != null) {
                                yfzRecyclerview.setVisibility(View.GONE);
                            }
                            if (refreshLayout != null) {
                                refreshLayout.setEnableLoadMore(false);
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getYfzData(YindouBean.InfoBean.ProfitsBean data) {

        if (!DataSafeUtils.isEmpty(data.getTotal()))
            yfzNum.setText(data.getTotal());

        if (!DataSafeUtils.isEmpty(data.getGrade_id()))
            meYfzLevel.setText("Lv." + data.getGrade_id());

        if (!DataSafeUtils.isEmpty(data.getNext_grade()))
            LevelBar.setMax(Integer.parseInt(data.getNext_grade()));

        if (!DataSafeUtils.isEmpty(data.getGrade_id()))
            LevelBar.setProgress(Integer.parseInt(data.getGrade_id()));

        if (!DataSafeUtils.isEmpty(data.getGrade_id()) && !DataSafeUtils.isEmpty(data.getNext_grade()))
            levelScale.setText(data.getGrade_id() + "/" + data.getNext_grade());
    }


    /**
     * 乐豆明细
     *
     * @param detalsList
     */
    private void getListAdapter(List<YindouBean.InfoBean.ListBean> detalsList) {
        mDouListAdapter = new YinDouAdapter(R.layout.dou_list_item_view, detalsList);
        yfzRecyclerview.setAdapter(mDouListAdapter);
        yfzRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
