package com.mc.phonelive.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.foxtone.SetTransactionPwdActivity;
import com.mc.phonelive.adapter.SettingAdapter;
import com.mc.phonelive.bean.ConfigBean;
import com.mc.phonelive.bean.SettingBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.GlideCatchUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.VersionUtil;
import com.mc.phonelive.utils.WordUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/9/30.
 * 设置
 */

public class SettingActivity extends AbsActivity implements OnItemClickListener<SettingBean> {

    private RecyclerView mRecyclerView;
    private Handler mHandler;
    private SettingAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.setting));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        HttpUtil.getSettingList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<SettingBean> list = JSON.parseArray(Arrays.toString(info), SettingBean.class);
                SettingBean bean = new SettingBean();
                bean.setName(WordUtil.getString(R.string.setting_exit));
                bean.setLast(false);
                bean.setId(999);
                list.add(bean);
                mAdapter = new SettingAdapter(mContext, list, VersionUtil.getVersion(), getCacheSize());
                mAdapter.setOnItemClickListener(SettingActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }


    @Override
    public void onItemClick(SettingBean bean, int position) {
        String href = bean.getHref();
        if (TextUtils.isEmpty(href)) {
            if (bean.getId()==999) {//退出登录
                logout();
            } else if (bean.getId() ==5) {//修改密码
                forwardModifyPwd();
            }  else if (bean.getId() == Constants.SETTING_TRANSCATION_PWD) {//设置交易密码
                forwardModifyTranscationPwd();
            } else if (bean.getId() == 9999) {//检查更新
                startActivity(new Intent(mContext,VersionActivity.class));
//                checkVersion();
            } else if (bean.getId() == 8) {//清除缓存
                clearCache(position);
            }else if (bean.getId() == 6) {//清除缓存
                startActivity(new Intent(mContext, HeiMingDanActivity.class));
            }
        } else {
            if (bean.getId() == 7) {//意见反馈要在url上加版本号和设备号
//                href += "&version=" + android.os.Build.VERSION.RELEASE + "&model=" + android.os.Build.MODEL;
                WebViewActivity.forward(mContext, href);
            }else{
                WebViewActivity.forward(mContext, href);
            }
//
        }
    }

    /**
     * 检查更新
     */
    private void checkVersion() {
        AppConfig.getInstance().getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean configBean) {
                if (configBean != null) {

                    if (VersionUtil.isLatest(configBean.getVersion())) {
                        ToastUtil.show(R.string.version_latest);
                    } else {
                        VersionUtil.showDialog(mContext, configBean.getUpdateDes(), configBean.getDownloadApkUrl());
                    }
                }
            }
        });

    }

    /**
     * 退出登录
     */
    private void logout() {
        AppConfig.getInstance().clearLoginInfo();
        //友盟统计登出
        MobclickAgent.onProfileSignOff();
        LoginActivity.forward();
    }

    /**
     * 修改密码
     */
    private void forwardModifyPwd() {
//        startActivity(new Intent(mContext, ModifyPwdActivity.class));
        startActivity(new Intent(mContext, ZhangHaoAqActivity.class));
    }

    /**
     * 设置交易密码
     */
    private void forwardModifyTranscationPwd() {
        startActivity(new Intent(mContext, SetTransactionPwdActivity.class));
    }

    /**
     * 获取缓存
     */
    private String getCacheSize() {
        return GlideCatchUtil.getInstance().getCacheSize();
    }

    /**
     * 清除缓存
     */
    private void clearCache(final int position) {
        final Dialog dialog = DialogUitl.loadingDialog(mContext, getString(R.string.setting_clear_cache_ing));
        dialog.show();
        GlideCatchUtil.getInstance().clearImageAllCache();
        File gifGiftDir = new File(AppConfig.GIF_PATH);
        if (gifGiftDir.exists() && gifGiftDir.length() > 0) {
            gifGiftDir.delete();
        }
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (mAdapter != null) {
                    mAdapter.setCacheString(getCacheSize());
                    mAdapter.notifyItemChanged(position);
                }
                ToastUtil.show(R.string.setting_clear_cache);
            }
        }, 2000);
    }


    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        HttpUtil.cancel(HttpConsts.GET_SETTING_LIST);
        HttpUtil.cancel(HttpConsts.GET_CONFIG);
        super.onDestroy();
    }
}
