package com.mc.phonelive.fragment;

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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.ChatRoomActivity;
import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.adapter.FamilyAdapter;
import com.mc.phonelive.adapter.foxtone.TaskListAdapter;
import com.mc.phonelive.bean.FamilyLeftBean;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.KeyboardUtil;
import com.mc.phonelive.views.ClearEditText;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 * 家人列表
 */
public class FamilyFragmentLeft extends BaseFragment implements TaskListAdapter.TaskListener {

    View layoutView;
    private com.scwang.smartrefresh.layout.SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRv;
    private TextView mTv_noinfo;
    private FamilyAdapter mAdapter;
    private int page=1;
    private ClearEditText cet;
    public static FamilyFragmentLeft newInstance() {
        FamilyFragmentLeft fragment = new FamilyFragmentLeft();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.left_fragment_family, container, false);
        return layoutView;
    }

    @Override
    protected void initView(View view) {
        mRefreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        cet = (ClearEditText) view.findViewById(R.id.cet);
        mTv_noinfo = (TextView) view.findViewById(R.id.tv_noinfo);
        mAdapter=new FamilyAdapter();
        mRv.setLayoutManager(Utils.getLvManager(getContext()));
        mRv.setAdapter(mAdapter);
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
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserHomeActivity.forward(getActivity(), mAdapter.getData().get(position).getId());
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.iv_msg){
                String attion = mAdapter.getData().get(position).getIsattention();
                FamilyLeftBean leftBean = mAdapter.getData().get(position);
                UserBean userBean=new UserBean();
                userBean.setId(leftBean.getId());
                userBean.setUserNiceName(leftBean.getUser_nicename());
                userBean.setAvatar(leftBean.getAvatar());
                userBean.setIs_attention(leftBean.getIsattention());
                ChatRoomActivity.forward(getActivity(), userBean, attion.equals("1"));
                }
            }
        });
    }

    private void getList() {
        HttpUtil.getFamilyList(page,cet.getText().toString().trim(),new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<FamilyLeftBean> nowsBean = (ArrayList<FamilyLeftBean>) JSON.parseArray(obj.getString("list"), FamilyLeftBean.class);
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
