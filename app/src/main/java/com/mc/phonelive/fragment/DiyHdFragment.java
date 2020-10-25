package com.mc.phonelive.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.adapter.FriendsFxAdapter;
import com.mc.phonelive.bean.FarmilyBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 私人定制
 */

public class DiyHdFragment extends XBaseFragment  {
    private com.scwang.smartrefresh.layout.SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRv_yhq;
    private TextView mTv_noinfo;
    private int page = 1;
    private FriendsFxAdapter mAdapter;
    private String mArgument;
    public static final String ARGUMENT = "argument";
    private boolean isVisible=false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ask_fragment_all, null);
        mRefreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRv_yhq = (RecyclerView) view.findViewById(R.id.rv);
        mTv_noinfo = (TextView) view.findViewById(R.id.tv_noinfo);
        mAdapter=new FriendsFxAdapter();
        mArgument=getArguments().getString(ARGUMENT);
        mRv_yhq.setLayoutManager(Utils.getLvManager(mActivity));
        mRv_yhq.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1500);
                page = 1;
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1500);
                page++;
            }
        });
        List<FarmilyBean> nlist=new ArrayList<>();
        for(int i=0;i<nlist.size();i++){
            nlist.add(new FarmilyBean());
        }
//        mAdapter.setNewData(nlist);

        return view;
    }


    /**
     * 传入需要的参数，设置给arguments
     * @param argument
     * @return
     */
    public static DiyHdFragment newInstance(String argument)
    {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        DiyHdFragment contentFragment = new DiyHdFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    @Override
    public void initData() {
    }

}
