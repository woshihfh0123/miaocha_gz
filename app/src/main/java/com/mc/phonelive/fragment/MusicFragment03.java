package com.mc.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.foxtone.FindMusicListAdapter;
import com.mc.phonelive.bean.foxtone.MusicCenterBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 历史乐器
 */
public class MusicFragment03 extends BaseFragment {

    Unbinder unbinder;
    View layoutView;
    @BindView(R.id.music_recyclerview)
    RecyclerView musicRecyclerview;
    @BindView(R.id.no_content)
    RelativeLayout noContent;
    @BindView(R.id.footer)
    ClassicsFooter footer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty_textview)
    TextView emptyTextview;

    private FindMusicListAdapter mAdapter;
    private List<MusicCenterBean.InfoBean.ListBean> mList = new ArrayList<>();

    private boolean ISTART = false;

    public static MusicFragment03 newInstance() {
        MusicFragment03 fragment = new MusicFragment03();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (ISTART) {
                initHttpData();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.find_music_fragment03, container, false);
        unbinder = ButterKnife.bind(this, layoutView);
        return layoutView;
    }

    @Override
    protected void initView(View view) {
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);

        setAdapter(mList);

    }

    @Override
    protected void initHttpData() {


        HttpUtil.getMusicCenterList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ISTART = true;
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    List<MusicCenterBean.InfoBean.ListBean> list = JSON.parseArray(obj.getString("old_list"), MusicCenterBean.InfoBean.ListBean.class);
                    if (!DataSafeUtils.isEmpty(list) && list.size() > 0) {
                        if (musicRecyclerview != null) {
                            musicRecyclerview.setVisibility(View.VISIBLE);
                        }
                        if (noContent != null) {
                            noContent.setVisibility(View.GONE);
                        }
                        mList = list;
                        if (mAdapter != null) {
                            mAdapter.setNewData(list);
                        }
                    } else {
                        if (musicRecyclerview != null) {
                            musicRecyclerview.setVisibility(View.GONE);
                        }
                        if (noContent != null) {
                            noContent.setVisibility(View.VISIBLE);
                            emptyTextview.setText("暂无历史乐器哟~");
                        }
                    }
                }
            }
        });
    }


    private void setAdapter(List<MusicCenterBean.InfoBean.ListBean> mList) {
        mAdapter = new FindMusicListAdapter(R.layout.find_music_fragment01_item_view, mList);
        musicRecyclerview.setAdapter(mAdapter);
        musicRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
