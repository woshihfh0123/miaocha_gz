package com.mc.phonelive.activity.shop;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.ChatActivity;
import com.mc.phonelive.adapter.MsgLvAdapter;
import com.mc.phonelive.adapter.MsgTopAdapter;
import com.mc.phonelive.bean.FarmilyBean;

import java.util.ArrayList;
import java.util.List;

/**
 *消息
 * 新888888
 *
 */

public class MsgActivity extends AbsActivity {
    private android.support.v7.widget.RecyclerView mRv_h;
    private MsgTopAdapter mAdapter;
    private android.support.v7.widget.RecyclerView mRv_gv;
    private MsgLvAdapter lvAdapter;
    private ImageView btn_back;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg;
    }
    @Override
    protected void main() {
//        setTitle("消息");
        setBarModel(false);
        mRv_h = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_h);
        mRv_gv = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_gv);
        btn_back=findViewById(R.id.btn_back);
        mAdapter=new MsgTopAdapter();
        lvAdapter=new MsgLvAdapter();
        mRv_gv.setLayoutManager(Utils.getLvManager(mContext));
        mRv_h.setLayoutManager(Utils.getGvManager(mContext,3));
        mRv_gv.setAdapter(lvAdapter);
        mRv_h.setAdapter(mAdapter);
        List<FarmilyBean> list=new ArrayList<>();
        for(int i=0;i<3;i++){
            list.add(new FarmilyBean());
        }
        mAdapter.setNewData(list);
        lvAdapter.setNewData(list);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==1){
                   Intent intent=new Intent(mContext, ChatActivity.class);
                   mContext.startActivity(intent);
                }else if(position==2){
                    Intent intent=new Intent(mContext,PingLunListActivity.class);
                    mContext.startActivity(intent);
                }else{//
                    Intent intent=new Intent(mContext,FunsActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void release(){
//        if(mVideoHomeViewHolder!=null){
//            mVideoHomeViewHolder.release();
//        }
//        mVideoHomeViewHolder=null;
    }

    @Override
    public void backClick(View v) {
       finish();
    }

    @Override
    public void onBackPressed() {
        release();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }
}
