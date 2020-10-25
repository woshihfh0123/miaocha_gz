package com.mc.phonelive.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.SystemMessageActivity;
import com.mc.phonelive.bean.SystemMessageBean;
import com.mc.phonelive.dialog.SystemMessageDialogFragment;
import com.mc.phonelive.event.FollowEvent;
import com.mc.phonelive.event.SystemMsgEvent;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.ImListAdapter;
import com.mc.phonelive.im.ImMessageUtil;
import com.mc.phonelive.im.ImUserBean;
import com.mc.phonelive.im.ImUserMsgEvent;
import com.mc.phonelive.utils.SpUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by cxf on 2018/10/24.
 * 消息列表
 */

public class ChatListViewHolder extends AbsViewHolder implements View.OnClickListener, ImListAdapter.ActionListener {

    public static final int TYPE_ACTIVITY = 0;
    public static final int TYPE_DIALOG = 1;
    private int mType;
    //private View mNoData;
    private View mBtnSystemMsg;
    private RecyclerView mRecyclerView;
    private ImListAdapter mAdapter;
    private ActionListener mActionListener;
    private View mSystemMsgRedPoint;//系统消息的红点
    private TextView mSystemMsgContent;
    private TextView mSystemTime;
    private HttpCallback mSystemMsgCallback;
    private View mBtnBack;
    private String mLiveUid;//主播的uid

    public ChatListViewHolder(Context context, ViewGroup parentView, int type) {
        super(context, parentView, type);
    }

    @Override
    protected void processArguments(Object... args) {
        mType = (int) args[0];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_chat_list;
    }

    @Override
    public void init() {
        //mNoData = findViewById(R.id.no_data);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ImListAdapter(mContext);
        mAdapter.setActionListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mBtnBack = findViewById(R.id.btn_back);
        if (mType == TYPE_ACTIVITY) {
            mBtnBack.setOnClickListener(this);
        } else {
            mBtnBack.setVisibility(View.INVISIBLE);
            View top = findViewById(R.id.top);
            top.setBackgroundColor(0xffffffff);
        }
        findViewById(R.id.btn_ignore).setOnClickListener(this);
        mBtnSystemMsg = findViewById(R.id.btn_system_msg);
        mBtnSystemMsg.setOnClickListener(this);
        mAdapter.setContactView(mBtnSystemMsg);
        mSystemMsgRedPoint = findViewById(R.id.red_point);
        mSystemMsgContent = (TextView) findViewById(R.id.msg);
        mSystemTime = (TextView) findViewById(R.id.time);
        mSystemMsgCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    SystemMessageBean bean = JSON.parseObject(info[0], SystemMessageBean.class);
                    if (mSystemMsgContent != null) {
                        mSystemMsgContent.setText(bean.getContent());
                    }
                    if (mSystemTime != null) {
                        mSystemTime.setText(bean.getAddtime());
                    }
                    if (SpUtil.getInstance().getBooleanValue(SpUtil.HAS_SYSTEM_MSG)) {
                        if (mSystemMsgRedPoint != null && mSystemMsgRedPoint.getVisibility() != View.VISIBLE) {
                            mSystemMsgRedPoint.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        };
        EventBus.getDefault().register(this);
        if (AppConfig.SYSTEM_MSG_APP_ICON) {
            ImageView avatar = (ImageView) findViewById(R.id.avatar);
            avatar.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void release() {
        EventBus.getDefault().unregister(this);
        mActionListener = null;
        HttpUtil.cancel(HttpConsts.GET_SYSTEM_MESSAGE_LIST);
        HttpUtil.cancel(HttpConsts.GET_IM_USER_INFO);
    }

    public void setLiveUid(String liveUid) {
        mLiveUid = liveUid;
    }

    public void loadData() {
        getSystemMessageList();
        final boolean needAnchorItem = mType == TYPE_DIALOG
                && !TextUtils.isEmpty(mLiveUid) && !mLiveUid.equals(AppConfig.getInstance().getUid());
        String uids = ImMessageUtil.getInstance().getConversationUids();
        if (TextUtils.isEmpty(uids)) {
            if (needAnchorItem) {
                uids = mLiveUid;
            } else {
                return;
            }
        } else {
            if (needAnchorItem) {
                if (!uids.contains(mLiveUid)) {
                    uids = mLiveUid + "," + uids;
                }
            }
        }
        HttpUtil.getImUserInfo(uids, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<ImUserBean> list = JSON.parseArray(Arrays.toString(info), ImUserBean.class);
                    list = ImMessageUtil.getInstance().getLastMsgInfoList(list);
                    if (mRecyclerView != null && mAdapter != null && list != null) {
                        if (needAnchorItem) {
                            int anchorItemPosition = -1;
                            for (int i = 0, size = list.size(); i < size; i++) {
                                ImUserBean bean = list.get(i);
                                if (bean != null) {
                                    if (mLiveUid.equals(bean.getId())) {
                                        anchorItemPosition = i;
                                        bean.setAnchorItem(true);
                                        if (!bean.isHasConversation()) {
                                            bean.setLastMessage(WordUtil.getString(R.string.im_live_anchor_msg));
                                        }
                                        break;
                                    }
                                }
                            }
                            if (anchorItemPosition > 0) {//把主播的会话排在最前面
                                Collections.sort(list, new Comparator<ImUserBean>() {
                                    @Override
                                    public int compare(ImUserBean bean1, ImUserBean bean2) {
                                        if (mLiveUid.equals(bean1.getId())) {
                                            return -1;
                                        } else if (mLiveUid.equals(bean2.getId())) {
                                            return 1;
                                        }
                                        return 0;
                                    }
                                });
                            }
                        }
                        mAdapter.setList(list);
                    }
                }
            }
        });
//        else {
//            if (mNoData != null && mNoData.getVisibility() != View.VISIBLE) {
//                mNoData.setVisibility(View.VISIBLE);
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (mActionListener != null) {
                    mActionListener.onCloseClick();
                }
                break;
            case R.id.btn_ignore:
                ignoreUnReadCount();
                break;
            case R.id.btn_system_msg:
                forwardSystemMessage();
                break;
        }
    }

    /**
     * 前往系统消息
     */
    private void forwardSystemMessage() {
        SpUtil.getInstance().setBooleanValue(SpUtil.HAS_SYSTEM_MSG, false);
        if (mSystemMsgRedPoint != null && mSystemMsgRedPoint.getVisibility() == View.VISIBLE) {
            mSystemMsgRedPoint.setVisibility(View.INVISIBLE);
        }
        if (mType == TYPE_ACTIVITY) {
            SystemMessageActivity.forward(mContext);
        } else {
            SystemMessageDialogFragment fragment = new SystemMessageDialogFragment();
            fragment.show(((AbsActivity) mContext).getSupportFragmentManager(), "SystemMessageDialogFragment");
        }
    }

    @Override
    public void onItemClick(ImUserBean bean) {
        if (bean != null) {
            boolean res = ImMessageUtil.getInstance().markAllMessagesAsRead(bean.getId());
            if (res) {
                ImMessageUtil.getInstance().refreshAllUnReadMsgCount();
            }
            if (mActionListener != null) {
                mActionListener.onItemClick(bean);
            }
        }
    }

    @Override
    public void onItemDelete(ImUserBean bean, int size) {
        ImMessageUtil.getInstance().removeConversation(bean.getId());
//        if (size == 0) {
//            if (mNoData != null && mNoData.getVisibility() != View.VISIBLE) {
//                mNoData.setVisibility(View.VISIBLE);
//            }
//        }
    }

    public interface ActionListener {
        void onCloseClick();

        void onItemClick(ImUserBean bean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        if (e != null) {
            if (mAdapter != null) {
                mAdapter.setFollow(e.getToUid(), e.getIsAttention());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSystemMsgEvent(SystemMsgEvent e) {
        getSystemMessageList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImUserMsgEvent(final ImUserMsgEvent e) {
        if (e != null && mRecyclerView != null && mAdapter != null) {
            int position = mAdapter.getPosition(e.getUid());
            if (position < 0) {
                HttpUtil.getImUserInfo(e.getUid(), new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            ImUserBean bean = JSON.parseObject(info[0], ImUserBean.class);
                            bean.setLastMessage(e.getLastMessage());
                            bean.setUnReadCount(e.getUnReadCount());
                            bean.setLastTime(e.getLastTime());
                            mAdapter.insertItem(bean);
//                            if (mNoData != null && mNoData.getVisibility() == View.VISIBLE) {
//                                mNoData.setVisibility(View.INVISIBLE);
//                            }
                        }
                    }
                });
            } else {
                mAdapter.updateItem(e.getLastMessage(), e.getLastTime(), e.getUnReadCount(), position);
            }
        }
    }

    /**
     * 忽略未读
     */
    private void ignoreUnReadCount() {
        SpUtil.getInstance().setBooleanValue(SpUtil.HAS_SYSTEM_MSG, false);
        if (mSystemMsgRedPoint != null && mSystemMsgRedPoint.getVisibility() == View.VISIBLE) {
            mSystemMsgRedPoint.setVisibility(View.INVISIBLE);
        }
        ImMessageUtil.getInstance().markAllConversationAsRead();
        if (mAdapter != null) {
            mAdapter.resetAllUnReadCount();
        }
        ToastUtil.show(R.string.im_msg_ignore_unread_2);
    }

    /**
     * 获取系统消息
     */
    private void getSystemMessageList() {
        HttpUtil.getSystemMessageList(1, mSystemMsgCallback);
    }

}
