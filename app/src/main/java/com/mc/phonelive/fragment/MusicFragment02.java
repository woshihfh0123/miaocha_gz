package com.mc.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.foxtone.FindMusicListAdapter;
import com.mc.phonelive.adapter.foxtone.TaskListAdapter;
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
 * 现有乐器
 */
public class MusicFragment02 extends BaseFragment implements TaskListAdapter.TaskListener {

    Unbinder unbinder;
    View layoutView;
    @BindView(R.id.music_layout)
    LinearLayout music_layout;
    @BindView(R.id.music_recyclerview)
    RecyclerView musicRecyclerview;
    @BindView(R.id.task_recyclerview01)
    RecyclerView taskRecyclerview01;
    @BindView(R.id.task_recyclerview02)
    RecyclerView taskRecyclerview02;
    @BindView(R.id.task_layout01)
    RelativeLayout taskLayout01;
    @BindView(R.id.task_layout02)
    RelativeLayout taskLayout02;

    private FindMusicListAdapter mAdapter;
    private TaskListAdapter mTaskAdapter01;
    private TaskListAdapter mTaskAdapter02;
    private List<MusicCenterBean.InfoBean.ListBean> mList = new ArrayList<>();
    private List<MusicCenterBean.InfoBean.NowListBean.ExtraListBean> mTaskList01 = new ArrayList<>();
    private boolean ISTART = false;

    public static MusicFragment02 newInstance() {
        MusicFragment02 fragment = new MusicFragment02();
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
        layoutView = inflater.inflate(R.layout.find_music_fragment02, container, false);
        unbinder = ButterKnife.bind(this, layoutView);
        return layoutView;
    }

    @Override
    protected void initView(View view) {
        setAdapter(mList);
        setTask01Adapter(mTaskList01);
        setTask02Adapter(mTaskList01);
    }

    @Override
    protected void initHttpData() {
        HttpUtil.getMusicCenterList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ISTART = true;
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    MusicCenterBean.InfoBean.NowListBean nowsBean = JSON.parseObject(obj.getString("now_list"), MusicCenterBean.InfoBean.NowListBean.class);
                    if (!DataSafeUtils.isEmpty(nowsBean)) {
                        if (!DataSafeUtils.isEmpty(nowsBean.getNow_list()) && nowsBean.getNow_list().size() > 0) {
                            music_layout.setVisibility(View.VISIBLE);
                            mList = nowsBean.getNow_list();
                            if (mAdapter != null) {
                                mAdapter.setNewData(nowsBean.getNow_list());
                            }
                        } else {
                            music_layout.setVisibility(View.GONE);
                        }

                        if (!DataSafeUtils.isEmpty(nowsBean.getCommon_list()) && nowsBean.getCommon_list().size() > 0) {
                            taskLayout01.setVisibility(View.VISIBLE);
                            if (mTaskAdapter01 != null) {
                                mTaskAdapter01.setNewData(nowsBean.getCommon_list());
                            }
                        } else {
                            taskLayout01.setVisibility(View.GONE);
                        }
                        if (!DataSafeUtils.isEmpty(nowsBean.getExtra_list()) && nowsBean.getExtra_list().size() > 0) {
                            taskLayout02.setVisibility(View.VISIBLE);
                            if (mTaskAdapter02 != null) {
                                mTaskAdapter02.setNewData(nowsBean.getExtra_list());
                            }
                        } else {
                            taskLayout02.setVisibility(View.GONE);
                        }

                    }
                }
            }
        });


    }

    /**
     * 我的乐器
     *
     * @param mList
     */
    private void setAdapter(List<MusicCenterBean.InfoBean.ListBean> mList) {
        mAdapter = new FindMusicListAdapter(R.layout.find_music_fragment01_item_view, mList);
        musicRecyclerview.setAdapter(mAdapter);
        musicRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    /**
     * 我的普通任务
     *
     * @param mList
     */
    private void setTask01Adapter(List<MusicCenterBean.InfoBean.NowListBean.ExtraListBean> mList) {
        mTaskAdapter01 = new TaskListAdapter(R.layout.find_music_fragment02_task_view, mList);
        mTaskAdapter01.setReceiveLister(this);
        taskRecyclerview01.setAdapter(mTaskAdapter01);
        taskRecyclerview01.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    /**
     * 我的额外任务
     *
     * @param mList
     */
    private void setTask02Adapter(List<MusicCenterBean.InfoBean.NowListBean.ExtraListBean> mList) {
        mTaskAdapter02 = new TaskListAdapter(R.layout.find_music_fragment02_task_view, mList);
        mTaskAdapter02.setReceiveLister(this);
        taskRecyclerview02.setAdapter(mTaskAdapter02);
        taskRecyclerview02.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
