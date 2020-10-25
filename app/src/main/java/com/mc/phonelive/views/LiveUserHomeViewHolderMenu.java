package com.mc.phonelive.views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.SettingActivity;
import com.mc.phonelive.activity.UserHomeActivityMenu;
import com.mc.phonelive.activity.shop.ShopToolsActivity;
import com.mc.phonelive.activity.shop.ZbServiceActivity;
import com.mc.phonelive.interfaces.LifeCycleListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/18.
 * 个人资料页--侧滑页
 * 8888888888
 */

public class LiveUserHomeViewHolderMenu extends AbsLivePageViewHolder {

    private LayoutInflater mInflater;
    private ImageView btn_back;

    public LiveUserHomeViewHolderMenu(Context context, ViewGroup parentView, String toUid) {
        super(context, parentView, toUid);
    }

    @Override
    protected void processArguments(Object... args) {

    }
    private List<LifeCycleListener> mLifeCycleListeners;
    public List<LifeCycleListener> getLifeCycleListenerList() {
        return mLifeCycleListeners;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.view_live_user_home_menu;
    }
private LinearLayout ll_set,ll_server,ll_cars;
    @Override
    public void init() {
        super.init();
        mInflater = LayoutInflater.from(mContext);
        btn_back= (ImageView) findViewById(R.id.btn_back);
        ll_set= (LinearLayout) findViewById(R.id.ll_set);
        ll_server= (LinearLayout) findViewById(R.id.ll_server);
        ll_cars= (LinearLayout) findViewById(R.id.ll_cars);
        mLifeCycleListeners = new ArrayList<>();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        ll_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SettingActivity.class));
//                WebViewActivity.forward(mContext, url);
            }
        });
        ll_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ZbServiceActivity.class));
            }
        });
        ll_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ShopToolsActivity.class));
            }
        });//ScoreActivity
//ZbServiceActivity

    }
    /**
     * 刷新印象
     */
    public void refreshImpress() {
//        HttpUtil.getUserHome(mToUid, new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//                if (code == 0 && info.length > 0) {
//                    JSONObject obj = JSON.parseObject(info[0]);
//                    if (mImpressGroup != null) {
//                        mImpressGroup.removeAllViews();
//                    }
//                    showImpress(obj.getString("label"));
//                }
//            }
//        });
    }
    @Override
    public void loadData() {

    }


    private void back() {
        if (mContext instanceof UserHomeActivityMenu) {
            ((UserHomeActivityMenu) mContext).onBackPressed();
        }
    }




}
