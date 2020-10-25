package com.mc.phonelive;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.mc.phonelive.constant.Constant;
import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.qcloud.ugckit.UGCKit;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.ugc.TXUGCBase;
import com.umeng.commonsdk.UMConfigure;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.ImMessageUtil;
import com.mc.phonelive.im.ImPushUtil;
import com.mc.phonelive.utils.L;
import cn.tillusory.sdk.TiSDK;


/**
 * Created by cxf on 2017/8/3.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class AppContext extends MultiDexApplication {

    public static AppContext sInstance;
    public static boolean sDeBug;
    private int mCount;
    private boolean mFront;//是否前台
    //public static RefWatcher sRefWatcher;
    private boolean mBeautyInited;
    //KKKKKKKK直播录制Key
//    String licenceUrl = "http://license.vod2.myqcloud.com/license/v1/c45bd26aba16a8b916aa6b2c854024a1/TXUgcSDK.licence";
//    String licenseKey = "b4ee29b265d181feabfb309c0478cffe";
//    String licenceUrl = "http://license.vod2.myqcloud.com/license/v1/e7c915bba661a93b8a26b3cad547ba89/TXUgcSDK.licence";
//    String licenseKey = "870b01c2b8c75d0add730ea3e69ac145";
    String licenceUrl = "http://license.vod2.myqcloud.com/license/v1/f5aeaa85a38c4bd5ca925871b9927b12/TXUgcSDK.licence";
    String licenseKey = "ae2a39d6d26d56dd68eeb81a5295df0b";


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sDeBug = BuildConfig.DEBUG;
//        if (sDeBug) {
//            sRefWatcher = LeakCanary.install(this);
//        }
        //初始化腾讯bugly
        CrashReport.initCrashReport(this);
        CrashReport.setAppVersion(this, AppConfig.getInstance().getVersion());
        //初始化Http
        HttpUtil.init();
        //初始化ShareSdk
        MobSDK.init(this);
        //初始化极光推送
        ImPushUtil.getInstance().init(this);
        //初始化极光IM
        ImMessageUtil.getInstance().init();
        //初始化友盟统计
        UMConfigure.setLogEnabled(true);
//        PlatformConfig.setWeixin("wx22cf2c26cb824afc", "dcdf032cc64b0b454d8220a9cbd49bb7");
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5f1a9a5ab4fa6023ce198ebf");
        registerActivityLifecycleCallbacks();


        TXLiveBase.setConsoleEnabled(true);
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(TXLiveBase.getSDKVersionStr());
        CrashReport.initCrashReport(getApplicationContext(), strategy);

        UGCKit.init(this);
        TXLiveBase.getInstance().setLicence(sInstance, licenceUrl, licenseKey);

        // 短视频licence设置
        TXUGCBase.getInstance().setLicence(sInstance, licenceUrl, licenseKey);

        // 短视频licence设置
        TXUGCBase.getInstance().setLicence(this, licenceUrl, licenseKey);
        UGCKit.init(this);
    }

    /**
     * 初始化萌颜
     */
    public void initBeautySdk(String beautyKey) {
        if (AppConfig.TI_BEAUTY_ENABLE) {
            if (!mBeautyInited) {
                mBeautyInited = true;
                TiSDK.init(beautyKey, this);
                L.e("萌颜初始化------->");
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mCount++;
                if (!mFront) {
                    mFront = true;
                    L.e("AppContext------->处于前台");
                    AppConfig.getInstance().setFrontGround(true);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mCount--;
                if (mCount == 0) {
                    mFront = false;
                    L.e("AppContext------->处于后台");
                    AppConfig.getInstance().setFrontGround(false);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
