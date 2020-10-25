package com.mc.phonelive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.ShopsActivity;
import com.mc.phonelive.activity.TxlActivity;
import com.mc.phonelive.adapter.FriendsFxAdapter;
import com.mc.phonelive.adapter.foxtone.TaskListAdapter;
import com.mc.phonelive.bean.FarmilyBean;
import com.mc.phonelive.bean.foxtone.MusicCenterBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现好友
 */
public class AskRightFragment extends BaseFragment implements TaskListAdapter.TaskListener {
    View layoutView;
    private com.scwang.smartrefresh.layout.SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRv;
    private FriendsFxAdapter mAdapter;
    private TextView mTv_noinfo;
    private LinearLayout ll_txl,ll_sys;
    public static AskRightFragment newInstance() {
        AskRightFragment fragment = new AskRightFragment();
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
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mAdapter=new FriendsFxAdapter();
        mRv.setLayoutManager(Utils.getLvManager(getContext()));
        mRv.setAdapter(mAdapter);
        List<FarmilyBean> list=new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add(new FarmilyBean());
        }
//        mAdapter.setNewData(list);
        ll_txl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), TxlActivity.class));
            }
        });
        ll_sys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().startActivity(new Intent(getActivity(), SecondActivity.class));
                getActivity().startActivity(new Intent(getActivity(), ShopsActivity.class));
            }
        });
    }

    @Override
    protected void initHttpData() {
        HttpUtil.getMusicCenterList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    MusicCenterBean.InfoBean.NowListBean nowsBean = JSON.parseObject(obj.getString("now_list"), MusicCenterBean.InfoBean.NowListBean.class);
                }
            }
        });


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
