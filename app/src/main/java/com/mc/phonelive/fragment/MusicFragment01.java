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
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.foxtone.FindMusicListAdapter;
import com.mc.phonelive.bean.foxtone.MusicCenterBean;
import com.mc.phonelive.dialog.RedpackDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 任务大厅
 */
public class MusicFragment01 extends BaseFragment implements FindMusicListAdapter.MusicBuyListener {

    Unbinder unbinder;
    View layoutView;
    @BindView(R.id.task_recyclerview)
    RecyclerView taskRecyclerview;
    @BindView(R.id.no_content)
    RelativeLayout noContent;
    @BindView(R.id.empty_textview)
    TextView emptyTextview;
    private FindMusicListAdapter mAdapter;
    private List<MusicCenterBean.InfoBean.ListBean> mList = new ArrayList<>();
    private boolean ISTART = false;

    public static MusicFragment01 newInstance() {
        MusicFragment01 fragment = new MusicFragment01();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.find_music_fragment01, container, false);
        unbinder = ButterKnife.bind(this, layoutView);
        return layoutView;
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
    protected void initView(View view) {
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
                    List<MusicCenterBean.InfoBean.ListBean> list = JSON.parseArray(obj.getString("list"), MusicCenterBean.InfoBean.ListBean.class);
                    if (!DataSafeUtils.isEmpty(list) && list.size() > 0) {
                        if (taskRecyclerview != null) {
                            taskRecyclerview.setVisibility(View.VISIBLE);
                        }
                        if (noContent != null) {
                            noContent.setVisibility(View.GONE);
                        }
                        mList = list;
                        if (mAdapter != null) {
                            mAdapter.setNewData(list);
                        }
                    } else {
                        if (taskRecyclerview != null) {
                            taskRecyclerview.setVisibility(View.GONE);
                        }
                        if (noContent != null) {
                            emptyTextview.setText("暂无任务哟~");
                            noContent.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }


    private void setAdapter(List<MusicCenterBean.InfoBean.ListBean> mList) {
        mAdapter = new FindMusicListAdapter(R.layout.find_music_fragment01_item_view, mList);
        mAdapter.setMusicBuyLister(this);
        taskRecyclerview.setAdapter(mAdapter);
        taskRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void MusicBuyData(String id, String name) {
        showDialog(id, name);

    }

    /**
     * 确认购买吗？
     *
     * @param id
     */
    private void showDialog(String id, String name) {
        RedpackDialog oRderDialog = new RedpackDialog(getActivity(), name + "购买", "确定购买吗？") {
            @Override
            public void doConfirmUp() {
                if (!ButtonUtils.isFastDoubleClick(R.id.music_pay))
                    buyMusicData(id);
            }
        };
        oRderDialog.show();
    }

    private void buyMusicData(String id) {
        showDialog();
        HttpUtil.getMusicBuy(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                dismissDialog();
                ToastUtil.show(msg + "");
            }
        });
    }
}
