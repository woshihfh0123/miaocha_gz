package com.mc.phonelive.activity.foxtone;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.foxtone.FindSchoolAdapter;
import com.mc.phonelive.bean.foxtone.FindSchoolBean;
import com.mc.phonelive.bean.foxtone.MainFindBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商学院列表
 */
public class SchoolListActivity extends AbsActivity {
    @BindView(R.id.music_recyclerview)
    RecyclerView schoolRecyclerview;
    @BindView(R.id.no_content)
    RelativeLayout noContent;
    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private FindSchoolAdapter mSchoolAdapter;//商学院
    private List<FindSchoolBean> mSchoolList = new ArrayList<>();
    private MainFindBean.InfoBean infoBean = new MainFindBean.InfoBean();
    private int pages = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.find_school_list_layout;
    }

    @Override
    protected void main() {
        setTitle("商学院");

        setSchoolAdapter(mSchoolList);
        initHttpData();

        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pages++;
                initHttpData();
            }
        });
    }

    private void initHttpData() {
        HttpUtil.getFindNewList(pages, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<FindSchoolBean> list = JSON.parseArray(Arrays.toString(info),  FindSchoolBean.class);
                if (!DataSafeUtils.isEmpty(list) && list.size()>0){
                    if (noContent!=null){
                        noContent.setVisibility(View.GONE);
                    }
                    if (schoolRecyclerview!=null){
                        schoolRecyclerview.setVisibility(View.VISIBLE);
                    }
                    if (!DataSafeUtils.isEmpty(list) && list.size() > 0) {
                        mSchoolAdapter.setNewData(list);
                    }

                }else{
                    if (noContent!=null){
                        noContent.setVisibility(View.VISIBLE);
                    }
                    if (schoolRecyclerview!=null){
                        schoolRecyclerview.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
        });
    }

    /**
     * 商学院
     *
     * @param mSchoolList
     */
    private void setSchoolAdapter(List<FindSchoolBean> mSchoolList) {
        mSchoolAdapter = new FindSchoolAdapter(R.layout.main_find_school_item, mSchoolList);
        schoolRecyclerview.setAdapter(mSchoolAdapter);
        schoolRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSchoolAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FindSchoolBean data =mSchoolAdapter.getData().get(position);
                if (!DataSafeUtils.isEmpty(data)){
                    if (!DataSafeUtils.isEmpty(data.getWeb_url())){
                        forwardWebview(mContext,data.getWeb_url());
                    }
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
}
