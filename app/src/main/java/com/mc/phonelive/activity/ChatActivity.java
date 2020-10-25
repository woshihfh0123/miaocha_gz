package com.mc.phonelive.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.shop.AtListActivity;
import com.mc.phonelive.activity.shop.FunsActivity;
import com.mc.phonelive.activity.shop.PingLunListActivity;
import com.mc.phonelive.activity.shop.TypeMsgListActivity;
import com.mc.phonelive.adapter.MsgTopAdapter;
import com.mc.phonelive.bean.FarmilyBean;
import com.mc.phonelive.im.ImUserBean;
import com.mc.phonelive.views.ChatListViewHolder;
import com.mc.phonelive.views.ChatListViewHolderAct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/24.
 * 消息列表
 */

public class ChatActivity extends AbsActivity {

    private ChatListViewHolderAct mChatListViewHolder;
    private android.support.v7.widget.RecyclerView mRv_h;
    private MsgTopAdapter mAdapter;
    public static void forward(Context context) {
        context.startActivity(new Intent(context, ChatActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_list;
    }

    @Override
    protected void main() {
        setBarModel(false);
        mRv_h = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_h);
        mRv_h.setLayoutManager(Utils.getGvManager(mContext,4));
        mAdapter=new MsgTopAdapter();
        mRv_h.setAdapter(mAdapter);
        List<FarmilyBean> list=new ArrayList<>();
        for(int i=0;i<4;i++){
            list.add(new FarmilyBean());
        }
        mAdapter.setNewData(list);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==1){
                    Intent intent=new Intent(mContext, TypeMsgListActivity.class);
                    mContext.startActivity(intent);
                }else if(position==2){
                    Intent intent=new Intent(mContext, AtListActivity.class);
                    mContext.startActivity(intent);
                }else if(position==3){
                    Intent intent=new Intent(mContext, PingLunListActivity.class);
                    mContext.startActivity(intent);

                }else{//
                    Intent intent=new Intent(mContext, FunsActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
        mChatListViewHolder = new ChatListViewHolderAct(mContext, (ViewGroup) findViewById(R.id.root),ChatListViewHolder.TYPE_ACTIVITY);
        mChatListViewHolder.setActionListener(new ChatListViewHolderAct.ActionListener() {
            @Override
            public void onCloseClick() {
                onBackPressed();
            }

            @Override
            public void onItemClick(ImUserBean bean) {
                ChatRoomActivity.forward(mContext, bean, bean.getAttent() == 1);
            }
        });
        mChatListViewHolder.addToParent();
        mChatListViewHolder.loadData();

    }

    @Override
    protected void onDestroy() {
        if (mChatListViewHolder != null) {
            mChatListViewHolder.release();
        }
        super.onDestroy();
    }
}
