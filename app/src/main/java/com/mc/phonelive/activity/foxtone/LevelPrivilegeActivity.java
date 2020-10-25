package com.mc.phonelive.activity.foxtone;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.foxtone.LevelPrivilegeAdapter;
import com.mc.phonelive.bean.foxtone.LevelPrivilegeBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 等级特权
 */
public class LevelPrivilegeActivity extends AbsActivity {


    @BindView(R.id.privilege_recyclerview)
    RecyclerView privilegeRecyclerview;
    @BindView(R.id.no_data)
    TextView noContent;
    private List<LevelPrivilegeBean.InfoBean> mList = new ArrayList<>();
    private LevelPrivilegeAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.mylevel_privilege_layout;
    }

    @Override
    protected void main() {
        setTitle("等级特权");
        setAdapter();
        initHttpData();
    }

    private void setAdapter() {
        mAdapter = new LevelPrivilegeAdapter(R.layout.level_privilege_item_view, mList);
        privilegeRecyclerview.setAdapter(mAdapter);
        privilegeRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void initHttpData() {

        HttpUtil.getProfitGradeList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code==0){
                    List<LevelPrivilegeBean.InfoBean> data = JSON.parseArray(Arrays.toString(info),LevelPrivilegeBean.InfoBean.class);
                    if (!DataSafeUtils.isEmpty(data) && data.size()>0){
                        if (noContent!=null){
                            noContent.setVisibility(View.GONE);
                        }
                        if (privilegeRecyclerview!=null){
                            privilegeRecyclerview.setVisibility(View.VISIBLE);
                        }
                        mList =data;
                        if (mAdapter != null) {
                            mAdapter.setNewData(data);
                        }

                    }else{
                        if (noContent!=null){
                            noContent.setVisibility(View.VISIBLE);
                        }
                        if (privilegeRecyclerview!=null){
                            privilegeRecyclerview.setVisibility(View.GONE);
                        }
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
