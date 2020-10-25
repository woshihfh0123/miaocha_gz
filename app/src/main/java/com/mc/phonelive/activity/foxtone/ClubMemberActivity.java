package com.mc.phonelive.activity.foxtone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.foxtone.ClubMemberListAdapter;
import com.mc.phonelive.bean.foxtone.ClubMemberBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 俱乐部成员
 */
public class ClubMemberActivity extends AbsActivity implements ClubMemberListAdapter.InfoClubListener {
    @BindView(R.id.rightView)
    TextView rightView;
    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.club_search_img)
    ImageView clubSearchImg;
    @BindView(R.id.club_close_img)
    ImageView clubCloseImg;
    @BindView(R.id.club_search)
    TextView clubSearch;
    @BindView(R.id.club_people_count)
    TextView clubPeopleCount;
    @BindView(R.id.club_recyclerview)
    RecyclerView clubRecyclerview;
    @BindView(R.id.no_data)
    TextView noData;

    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


    private String mId = "";//俱乐部id
    private String mFounder = "";//是否是创始人
    private String mKeywords = "";//俱乐部关键字
    private int page = 1;

    List<ClubMemberBean.InfoBean> mList = new ArrayList<>();
    ClubMemberListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.my_club_member_layout;
    }

    @Override
    protected void main() {
        String id = this.getIntent().getStringExtra("id");
        if (!DataSafeUtils.isEmpty(id)) {
            this.mId = id;
        }
        String founder = this.getIntent().getStringExtra("founder");
        if (!DataSafeUtils.isEmpty(founder)) {
            this.mFounder = founder;
        }
        setAdapter();
        showDialog();
        initHttpData();

        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initHttpData();
            }
        });
    }

    private void setAdapter() {
        mAdapter = new ClubMemberListAdapter(R.layout.club_member_item_view, mList, this.mFounder);
        mAdapter.DelClubListener(this);
        clubRecyclerview.setAdapter(mAdapter);
        clubRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private void initHttpData() {
        HttpUtil.getClubUsers(page, mId, mKeywords, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                dismissDialog();
                if (!DataSafeUtils.isEmpty(info) && info.length > 0) {
                    List<ClubMemberBean.InfoBean> data = JSON.parseArray(Arrays.toString(info), ClubMemberBean.InfoBean.class);
                    if (!DataSafeUtils.isEmpty(data) && data.size() > 0) {
                        if (clubRecyclerview != null) clubRecyclerview.setVisibility(View.VISIBLE);
                        if (noData != null) noData.setVisibility(View.GONE);

                        clubPeopleCount.setText("当前成员" + data.size() + "人");
                        mList = data;
                        if (mAdapter != null) {
                            if (page == 1) {
                                mAdapter.setNewData(data);
                            } else {
                                mAdapter.addData(data);
                            }
                        }
                        if (data.size() < 15) {
                            if (refreshLayout != null) {
                                refreshLayout.setEnableLoadMore(false);
                                footer.setNoMoreData(true);
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        } else {
                            if (refreshLayout != null) {
                                refreshLayout.setEnableLoadMore(true);
                            }
                        }
                    } else {
                        if (page == 1) {
                            clubPeopleCount.setText("当前成员");
                            if (clubRecyclerview != null) clubRecyclerview.setVisibility(View.GONE);
                            if (noData != null) noData.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    if (page == 1) {
                        clubPeopleCount.setText("当前成员");
                        if (clubRecyclerview != null) clubRecyclerview.setVisibility(View.GONE);
                        if (noData != null) noData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFinish() {
                dismissDialog();
                if (refreshLayout != null) {
                    refreshLayout.finishLoadMore();
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

    @OnClick({R.id.rightView, R.id.search_edit, R.id.club_search_img, R.id.club_close_img, R.id.club_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightView:
                mContext.startActivity(new Intent(mContext, InviteFriendsActivity.class));
                break;
            case R.id.search_edit:
            case R.id.club_search_img:
            case R.id.club_close_img:
            case R.id.club_search:
                Intent intent = new Intent(mContext, SearchClubMemberActivity.class);
                intent.putExtra("id",mId);
                intent.putExtra("type",this.mFounder);
                mContext.startActivity(intent);
                break;
        }
    }

    @Override
    public void delClubData(String id, String touid) {
        HttpUtil.delUserClub(touid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code==0){
                    page=1;
                    initHttpData();
                }
            }
        });
    }
}
