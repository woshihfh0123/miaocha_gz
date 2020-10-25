package com.mc.phonelive.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.adapter.OrderMyAdapter;
import com.mc.phonelive.bean.FarmilyBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/12/14.
 * 用户个人中心发布的视频列表
 */

public class VideoHomeViewHolder extends AbsViewHolder  {

    private String mToUid;
    private String mKey;
    private int page=1;
    private RecyclerView rv;
    private OrderMyAdapter mAdapter;

    public VideoHomeViewHolder(Context context, ViewGroup parentView, String toUid) {
        super(context, parentView, toUid);
    }

    @Override
    protected void processArguments(Object... args) {
        mToUid = (String) args[0];
    }

    @Override
    protected int getLayoutId() {
        Log.e("aaaaaaaa:","=========");
        return R.layout.view_video_home;
    }

    @Override
    public void init() {
//        if (TextUtils.isEmpty(mToUid)) {
//            return;
//        }
        mKey = Constants.VIDEO_USER + this.hashCode();
        rv= (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(Utils.getLvManager(mContext));
        mAdapter=new OrderMyAdapter();
        rv.setAdapter(mAdapter);
        List<FarmilyBean> nlist=new ArrayList<>();
        for(int i=0;i<10;i++){
            nlist.add(new FarmilyBean());
        }
        mAdapter.setNewData(nlist);
        Log.e("aaaaaaaa:","=========");

        getList();

    }

    private void getList() {
        HttpUtil.getHomeVideo("1","",page,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
//                    ArrayList<StoreBean> bannerList = (ArrayList<StoreBean>) JSON.parseArray(obj.getString("storelist"), StoreBean.class);
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }


}
