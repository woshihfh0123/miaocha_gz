package com.mc.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.adapter.FsRightAdapter;
import com.mc.phonelive.adapter.foxtone.TaskListAdapter;
import com.mc.phonelive.bean.FansItemBean;
import com.mc.phonelive.custom.ActionSheetDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 发现好友
 */
public class GzFsFragmentRight extends BaseFragment implements TaskListAdapter.TaskListener {
    View layoutView;
    private com.scwang.smartrefresh.layout.SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRv;
    private FsRightAdapter mAdapter;
    private TextView mTv_noinfo;
    private LinearLayout ll_txl,ll_sys;
    private int page=1;
    public static GzFsFragmentRight newInstance() {
        GzFsFragmentRight fragment = new GzFsFragmentRight();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.right_fragment_fs, container, false);
        return layoutView;
    }

    @Override
    protected void initView(View view) {
        mRefreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        mTv_noinfo = (TextView) view.findViewById(R.id.tv_noinfo);
        ll_txl =view.findViewById(R.id.ll_txl);
        ll_sys =view.findViewById(R.id.ll_sys);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mAdapter=new FsRightAdapter();
        mRv.setLayoutManager(Utils.getLvManager(getContext()));
        mRv.setAdapter(mAdapter);
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
        getList();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch(view.getId()){
                    case R.id.iv_yc:
                        ActionSheetDialog asd = new ActionSheetDialog(getActivity())
                                .builder()
                                .setCancelable(true)
                                .setCanceledOnTouchOutside(true)
                                .addSheetItem("移除粉丝",ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {

                                        removeFans(position,mAdapter.getData().get(position).getId());
                                    }
                                });

                        asd.show();

                        break;
                    case R.id.tv_status:
                        if(mAdapter.getData().get(position).getIsattention().equals("1")){
                            mAdapter.getData().get(position).setIsattention("0");
                        }else{
                            mAdapter.getData().get(position).setIsattention("1");
                        }
                        mAdapter.notifyItemChanged(position);
                        changeGz(position,mAdapter.getData().get(position).getId());

                        break;
                    default:

                        break;
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
                    mAdapter.getData().get(position).setIsattention(isattent);
                    mAdapter.notifyItemChanged(position);
                }
            }
        });

    }

    private void removeFans(int position, String id) {
        HttpUtil.removeGz(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                mAdapter.remove(position);
                EventBusUtil.postEvent(new EventBean("num_from_remove"));
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String isremove = obj.getString("isremove");
                    if(Utils.isNotEmpty(isremove)&&isremove.equals("1")){
                        ToastUtil.show("移除成功");
                    }
                }
            }
        });
    }

    private void getList() {
        HttpUtil.getFansList("", page, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    ArrayList<FansItemBean> bannerList = (ArrayList<FansItemBean>) JSON.parseArray(Arrays.toString(info), FansItemBean.class);
                    if(Utils.isNotEmpty(bannerList)){
                        mRefreshLayout.setEnableLoadMore(true);
                        if(page==1){
                            mAdapter.setNewData(bannerList);
                        }else{
                            mAdapter.addData(bannerList);
                        }

                    }else{
                        if(page==1){
                            mAdapter.setNewData(null);
                        }
                        mRefreshLayout.setEnableLoadMore(false);
                    }
                }
            }
        });
    }
    @Override
    protected void initHttpData() {


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
