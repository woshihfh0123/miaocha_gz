package com.mc.phonelive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.SecondActivity;
import com.mc.phonelive.activity.TxlActivity;
import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.adapter.FriendsFxAdapter;
import com.mc.phonelive.adapter.foxtone.TaskListAdapter;
import com.mc.phonelive.bean.FamilyRightBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.KeyboardUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.views.ClearEditText;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

/**
 * 发现好友
 */
public class FamilyFragmentRight extends BaseFragment implements TaskListAdapter.TaskListener {
    View layoutView;
    private com.scwang.smartrefresh.layout.SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRv;
    private FriendsFxAdapter mAdapter;
    private TextView mTv_noinfo;
    private LinearLayout ll_txl,ll_sys;
    private int page=1;
    private ClearEditText cet;
    public static FamilyFragmentRight newInstance() {
        FamilyFragmentRight fragment = new FamilyFragmentRight();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.right_fragment_family, container, false);
        return layoutView;
    }

    @Override
    protected void initView(View view) {
        mRefreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        mTv_noinfo = (TextView) view.findViewById(R.id.tv_noinfo);
        ll_txl =view.findViewById(R.id.ll_txl);
        ll_sys =view.findViewById(R.id.ll_sys);
        cet = (ClearEditText) view.findViewById(R.id.cet);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mAdapter=new FriendsFxAdapter();
        mRv.setLayoutManager(Utils.getLvManager(getContext()));
        mRv.setAdapter(mAdapter);
        ll_txl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), TxlActivity.class));
            }
        });
        ll_sys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                startActivityForResult(intent, 112);
//                getActivity().startActivity(new Intent(getActivity(), ShopsActivity.class));
//                getActivity().startActivity(new Intent(getActivity(), MyMerchantOrderActivity.class));
            }
        });
        getList();
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                page=1;
                getList();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                page++;
                getList();
            }
        });
        cet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //点击搜索的时候隐藏软键盘
                    KeyboardUtil.closeKeyboard(getActivity());
//                    getData(true);
                    // 在这里写搜索的操作,一般都是网络请求数据
                    page=1;
                    getList();
                    return true;
                }
                return false;
            }
        });
        cet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Utils.isEmpty(s.toString())){
                    page=1;
                    getList();
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.tv_status){
//                    guanZhu(mAdapter.getData().get(position).getId());
                    if(mAdapter.getData().get(position).getIsattention().equals("1")){
                        mAdapter.getData().get(position).setIsattention("0");
                    }else{
                        mAdapter.getData().get(position).setIsattention("1");
                    }
                    mAdapter.notifyItemChanged(position);
                    changeGz(position,mAdapter.getData().get(position).getId());
                }
            }
        });
    }
    private void changeGz(int position,String id) {
        HttpUtil.setGz(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String isattent = obj.getString("isattent");
//                    mAdapter.getData().get(position).setIsattention(isattent);
//                    mAdapter.notifyItemChanged(position);
                }
            }
        });

    }
//    private void guanZhu(String touid) {
//        HttpUtil.setGz(touid, new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//                page=1;
//                getList();
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 112) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    getInfo(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtil.show("无法识别");
                }
            }
        }
    }

    private void getInfo(String result) {
        HttpUtil.getUserHome(result, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    UserHomeActivity.forward(getActivity(), result);
                }else{
                    ToastUtil.show("无法识别："+result);
                }
            }
        });
    }
    private void getList() {
        HttpUtil.getFriendsList(page,cet.getText().toString().trim(),new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<FamilyRightBean> nowsBean = (ArrayList<FamilyRightBean>) JSON.parseArray(obj.getString("list"), FamilyRightBean.class);
                    if(Utils.isNotEmpty(nowsBean)){
                        if(page==1){
                            mAdapter.setNewData(nowsBean);
                        }else{
                            mAdapter.addData(nowsBean);
                        }
                    }else{
                        if(page==1){
                            mAdapter.setNewData(null);
                        }
                        mRefreshLayout.setEnableLoadMore(false);
                    }

                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
                if(Utils.isNotEmpty(mAdapter)&&Utils.isNotEmpty(mAdapter.getData())){
                    mTv_noinfo.setVisibility(View.GONE);
                }else{
                    mTv_noinfo.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    protected void initHttpData() {
//        HttpUtil.getMusicCenterList(new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//                if (!DataSafeUtils.isEmpty(info)) {
//                    JSONObject obj = JSON.parseObject(info[0]);
//                    MusicCenterBean.InfoBean.NowListBean nowsBean = JSON.parseObject(obj.getString("now_list"), MusicCenterBean.InfoBean.NowListBean.class);
//                }
//            }
//        });


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 领取任务
     * @param id
     * @param type
     */
    @Override
    public void taskReceive(String id, String type) {
        taskReceiveHttpData(id,type);
    }

    private void taskReceiveHttpData(String id, String type) {
        HttpUtil.getReceiveTask(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

            }
        });
    }
}
