package com.mc.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.adapter.shop.DouDetailsListAdapter;
import com.mc.phonelive.bean.LedouBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 福豆列表
 * created by WWL on 2020/4/14 0014:14
 */
public class FudouFragment extends BaseFragment {


    @BindView(R.id.myledou)
    TextView myledou;
    @BindView(R.id.fudou_price)
    TextView fudouPrice;
    @BindView(R.id.fudou_num)
    TextView fudouNum;
    @BindView(R.id.fudou_freeze_num)
    TextView fudouFreezeNum;
    @BindView(R.id.fudou_Give)
    TextView fudouGive;
    @BindView(R.id.fudou_withdraw)
    TextView fudouWithdraw;
    @BindView(R.id.fudou_method)
    TextView fudouMethod;
    @BindView(R.id.fudou_rule)
    TextView fudouRule;
    @BindView(R.id.ledou_recyclerview)
    RecyclerView ledouRecyclerview;
    Unbinder unbinder;
    private String state = "";
    View layoutView;

    private DouDetailsListAdapter mDouListAdapter;//乐豆明细
    private List<LedouBean.InfoBean.DetailsBean> mDetalsList = new ArrayList<>();
    private LedouBean.InfoBean mData = new LedouBean.InfoBean();

    private int pages=1;

    public static FudouFragment newInstance() {
        FudouFragment fragment = new FudouFragment();
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
        layoutView = inflater.inflate(R.layout.fudou_fragment, container, false);
        unbinder = ButterKnife.bind(this, layoutView);
        return layoutView;
    }

    @Override
    protected void initView(View view) {
        getListAdapter(mDetalsList);
    }

    @Override
    protected void initHttpData() {
        mData = LedouBean.getLeDouData();
        mDetalsList = mData.getDetaillist();
        mDouListAdapter.setNewData(mDetalsList);

        HttpUtil.FudouList(pages + "", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

            }
        });
    }

    /**
     * 福豆明细
     *
     * @param detalsList
     */
    private void getListAdapter(List<LedouBean.InfoBean.DetailsBean> detalsList) {
        mDouListAdapter = new DouDetailsListAdapter(R.layout.dou_list_item_view, detalsList);
        ledouRecyclerview.setAdapter(mDouListAdapter);
        ledouRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fudou_Give, R.id.fudou_withdraw, R.id.fudou_method, R.id.fudou_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fudou_Give:
                break;
            case R.id.fudou_withdraw:
                break;
            case R.id.fudou_method:
                break;
            case R.id.fudou_rule:
                break;
        }
    }
}
