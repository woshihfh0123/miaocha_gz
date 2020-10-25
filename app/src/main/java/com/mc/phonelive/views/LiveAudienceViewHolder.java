package com.mc.phonelive.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.LiveActivity;
import com.mc.phonelive.activity.LiveAudienceActivity;
import com.mc.phonelive.utils.DataSafeUtils;

/**
 * Created by cxf on 2018/10/9.
 * 观众直播间逻辑
 */

public class LiveAudienceViewHolder extends AbsLiveViewHolder {

    private String mLiveUid;
    private String mStream;
    private String is_shopping;

    public LiveAudienceViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_audience;
    }

    @Override
    public void init() {
        super.init();
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.btn_red_pack).setOnClickListener(this);
        findViewById(R.id.btn_gift).setOnClickListener(this);
        findViewById(R.id.shop_show).setOnClickListener(this);
//        ImageView shopImg = (ImageView) findViewById(R.id.shop_show);
//        if (AppConfig.SHOW_SHOP_LIST) {
//            shopImg.setVisibility(View.VISIBLE);
//        } else {
//            shopImg.setVisibility(View.GONE);
//        }

    }

    public void setLiveInfo(String liveUid, String stream, String is_shopping) {
        mLiveUid = liveUid;
        mStream = stream;
        this.is_shopping = is_shopping;

        if (!DataSafeUtils.isEmpty(is_shopping) && is_shopping.equals("1")) {
            findViewById(R.id.shop_show).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.shop_show).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_close:
                close();
                break;
            case R.id.btn_share:
                openShareWindow();
                break;
            case R.id.btn_red_pack:
                ((LiveActivity) mContext).openRedPackSendWindow();
                break;
            case R.id.btn_gift:
                openGiftWindow();
                break;
            case R.id.shop_show:
                showShopWindow();
                break;
        }
    }

    /**
     * 显示店铺列表商品
     */
    private void showShopWindow() {
        ((LiveAudienceActivity) mContext).showshopWindow();
    }

    /**
     * 退出直播间
     */
    private void close() {
        ((LiveAudienceActivity) mContext).onBackPressed();
    }


    /**
     * 打开礼物窗口
     */
    private void openGiftWindow() {
        ((LiveAudienceActivity) mContext).openGiftWindow();
    }

    /**
     * 打开分享窗口
     */
    private void openShareWindow() {
        ((LiveActivity) mContext).openShareWindow();
    }

}
