package com.mc.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.shop.DouDetailsListAdapter;
import com.mc.phonelive.bean.LedouBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 乐豆列表
 * created by WWL on 2020/4/14 0014:14
 */
public class LedouFragment extends BaseFragment {
    @BindView(R.id.myledou)
    TextView myledou;
    @BindView(R.id.leddou_price)
    TextView leddouPrice;
    @BindView(R.id.leddou_Give)
    TextView leddouGive;
    @BindView(R.id.leddou_method)
    TextView leddouMethod;
    @BindView(R.id.leddou_rule)
    TextView leddouRule;
    @BindView(R.id.myqd)
    TextView myqd;
    @BindView(R.id.mysign)
    TextView mysign;
    @BindView(R.id.mysign_days_tv)
    TextView mysignDaysTv;
    @BindView(R.id.leddou_sign_tv)
    TextView leddouSignTv;
    @BindView(R.id.sign_recyclerview)
    RecyclerView signRecyclerview;
    @BindView(R.id.ledou_recyclerview)
    RecyclerView ledouRecyclerview;
    Unbinder unbinder;
    private String state = "";
    View layoutView;
    BaseQuickAdapter<LedouBean.InfoBean.SignBean, BaseViewHolder> mSignAdapter;//签到列表
    private DouDetailsListAdapter mDouListAdapter;//乐豆明细
    private List<LedouBean.InfoBean.SignBean> mSignList = new ArrayList<>();
    private List<LedouBean.InfoBean.DetailsBean> mDetalsList = new ArrayList<>();
    private LedouBean.InfoBean mData = new LedouBean.InfoBean();
    private int pages=1;
    public static LedouFragment newInstance() {
        LedouFragment fragment = new LedouFragment();
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
        layoutView = inflater.inflate(R.layout.ledou_fragment, container, false);
        unbinder = ButterKnife.bind(this, layoutView);
        return layoutView;
    }

    @Override
    protected void initView(View view) {
        getSignAdapter(mSignList);
        getListAdapter(mDetalsList);

    }

    /**
     * 签到列表
     *
     * @param signList
     */
    private void getSignAdapter(List<LedouBean.InfoBean.SignBean> signList) {
        mSignAdapter = new BaseQuickAdapter<LedouBean.InfoBean.SignBean, BaseViewHolder>(R.layout.dou_sign_list_item, signList) {
            @Override
            protected void convert(BaseViewHolder helper, LedouBean.InfoBean.SignBean item) {
                LinearLayout layout = helper.getView(R.id.sign_bg_layout);
                ImageView imgs = helper.getView(R.id.sign_dou_img);
                TextView dounums = helper.getView(R.id.sign_dou_num);
                if (!DataSafeUtils.isEmpty(item.getNums()))
                    dounums.setText("+"+item.getNums());
                if (!DataSafeUtils.isEmpty(item.getIs_sign()) && item.getIs_sign().equals("1")) {
                    layout.setBackgroundResource(R.color.FUBColor7);
                    dounums.setTextColor(getResources().getColor(R.color.white));
                    Glide.with(mContext).load(R.mipmap.ledou_img2).into(imgs);
                } else {
                    layout.setBackgroundResource(R.color.background);
                    dounums.setTextColor(getResources().getColor(R.color.gray1));
                    Glide.with(mContext).load(R.mipmap.ledou_img1).into(imgs);

                }
            }
        };
        signRecyclerview.setAdapter(mSignAdapter);
        signRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 7));
//        mSignAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                LedouBean.InfoBean.SignBean signBean = mSignAdapter.getData().get(position);
//                if (signBean.getIs_sign().equals("0")) {
//                    ToastUtil.show("调用签到接口");
//                }
//            }
//        });
    }

    /**
     * 乐豆明细
     *
     * @param detalsList
     */
    private void getListAdapter(List<LedouBean.InfoBean.DetailsBean> detalsList) {
        mDouListAdapter = new DouDetailsListAdapter(R.layout.dou_list_item_view, detalsList);
        ledouRecyclerview.setAdapter(mDouListAdapter);
        ledouRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void initHttpData() {
        mData = LedouBean.getLeDouData();
        mSignList = mData.getSignlist();
        mDetalsList = mData.getDetaillist();
        mSignAdapter.setNewData(mSignList);
        mDouListAdapter.setNewData(mDetalsList);
        HttpUtil.LedouList(pages + "", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.leddou_Give, R.id.leddou_method, R.id.leddou_rule, R.id.leddou_sign_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leddou_Give://转赠
                break;
            case R.id.leddou_method://如何获取乐豆
                break;
            case R.id.leddou_rule://规则
                break;
            case R.id.leddou_sign_tv://签到
                ToastUtil.show("调用签到接口");
                break;
        }
    }
}
