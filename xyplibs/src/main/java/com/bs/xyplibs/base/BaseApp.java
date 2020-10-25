package com.bs.xyplibs.base;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.SSLCertificateSocketFactory;
import android.os.Build;
import android.os.Parcel;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import com.bs.xyplibs.R;
import com.bs.xyplibs.bean.LoginBean;
import com.bs.xyplibs.bean.UserBean;
import com.bs.xyplibs.bean.YzLoginBean;
import com.bs.xyplibs.utils.Constant;
import com.bs.xyplibs.utils.SUtils;
import com.bs.xyplibs.view.LoadingAndRetryManager;
import com.bs.xyplibs.view.MyToastStyle;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.utils.OkLogger;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2018/2/23.
 */
public abstract class BaseApp extends MultiDexApplication {
    private static BaseApp instance = null;
    public static int mainColor = R.color.colorPrimary;
    public static BaseApp getInstance() {
        return instance;
    }
    public static List<Activity> activityList = new ArrayList<Activity>();//所有activity的集合
    public abstract void initInfo();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLogger();
        LitePal.initialize(this);
        LitePal.limit(9999999);
        CrashReport.initCrashReport(getApplicationContext(), "183beb0971", true);//bugly
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(3, 200)//设置连接时重连次数和重连间隔（毫秒）
                .setSplitWriteNum(20)//每一包的数据长度，默认20个字节
                .setConnectOverTime(10000)//设置连接超时时间（毫秒），默认10秒
                .setOperateTimeout(5000)//配置操作超时
                .init(this);
        initOkGo();
//        initUM();
        changeAppLanguage();
        initInfo();
        //第三方ToastUtils框架
//        ToastUtils.init(this);
        //原生ToastUtils工具类
        com.bs.xyplibs.utils.ToastUtils.init(false);
//        ToastUtils.initStyle(new MyToastStyle());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
//        List<Class<? extends View>> clist=new ArrayList<>();
//        BGASwipeBackHelper.init(this,clist);
        BGASwipeBackHelper.init(this, null);
        initNetView();
    }
    private void initNetView() {
        LoadingAndRetryManager.BASE_RETRY_LAYOUT_ID = R.layout.base_retry;
        LoadingAndRetryManager.BASE_LOADING_LAYOUT_ID = R.layout.base_loading;
        LoadingAndRetryManager.BASE_EMPTY_LAYOUT_ID = R.layout.base_empty;
    }
    /**
     * 添加activity集合
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    /**
     * 移除所有的activity
     */
    public static void removeAllActivity() {

        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();

    }

    /**
     * 移除activity
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        if (activityList.contains(activity)) {
            activityList.remove(activity);
            activity.finish();
        }
    }

    private void initOkGo() {
//        OkLogger.debug(Constant.isDebug);

//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        //全局的读取超时时间
//        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
////全局的写入超时时间
//        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
////全局的连接超时时间
//        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//        //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
//        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
//        builder.sslSocketFactory(sslParams1.sSLSocketFactory,sslParams1.trustManager);
////        builder.hostnameVerifier(new SafeHostnameVerifier());
//        OkGo.getInstance().init(this)
//                .setOkHttpClient(builder.build())
//                .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        OkGo.getInstance().init(this);
    }


   /* private void initUM() {
        *//**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:友盟 app key
         * 参数3:友盟 channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         * 参数1:上下文，必须的参数，不能为空

         参数2:友盟 app key，非必须参数，如果Manifest文件中已配置app key，该参数可以传空，则使用Manifest中配置的app key，否则该参数必须传入

         参数3:友盟 channel，非必须参数，如果Manifest文件中已配置channel，该参数可以传空，则使用Manifest中配置的channel，否则该参数必须传入，channel命名请详见channel渠道命名规范

         参数4:设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机

         参数5:Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空
         *//*
//        UMConfigure.init(this, "5a1a2804f29d9818d400010f",
//                                        "BkShell",
//                          UMConfigure.DEVICE_TYPE_PHONE,
//                                        null);
//        UMConfigure.setEncryptEnabled(true);
        *//*
        开放平台
        *79bc29b5a7d0db606e852805c5c02875----------老
        *
        * 5662821cf8c4f09c86ebbc0053113b86------新
        * *//*
//        UMShareAPI.get(this);
        UMConfigure.setLogEnabled(Constant.isDebug);
        PlatformConfig.setWeixin("wx793c00ff8e3afa43", "5f5ec255b0e2bc6ad97c7de1faeb2f56");
        PlatformConfig.setQQZone("1106830294", "ZY0vHEo4Mb0DVNoi");
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5acb5de4f43e4843c8000090");
    }*/

    public void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(1)         // (Optional) How many method line to show. Default 2
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeAppLanguage() {
        Resources resources = this.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            configuration.setLocale(Locale.forLanguageTag(SUtils.getInstance().getLang()));

        } else {

//			configuration.locale = locale;
            return;

        }
        resources.updateConfiguration(configuration, metrics);
    }

    static {
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "全部加载完成";//"全部加载完成";
        //启用矢量图兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //设置全局的Header构建器
        //启用矢量图兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
//                layout.setReboundDuration(500);//回弹动画时长（毫秒）
//                layout.setFooterHeight(100);
//                layout.setDisableContentWhenLoading(true);
                layout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
                layout.setEnableNestedScroll(true);//是否启用嵌套滚动
                layout.setEnableOverScrollBounce(true);//是否启用越界回弹
                layout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
                layout.setEnableScrollContentWhenLoaded(true);//是否在加载完成时滚动列表显示新的内容
                layout.setEnableAutoLoadMore(false);
                layout.setEnableHeaderTranslationContent(true);//是否下拉Header的时候向下平移列表或者内容
                layout.setEnableFooterTranslationContent(true);//是否上拉Footer的时候向上平移列表或者内容
                layout.setEnableLoadMoreWhenContentNotFull(false);//是否在列表不满一页时候开启上拉加载功能
                layout.setEnableScrollContentWhenRefreshed(true);//是否在刷新完成时滚动列表显示新的内容 1.0.5
//                layout.setPrimaryColorsId(R.color.colorViewBg, android.R.color.darker_gray);//全局设置主题颜色
                layout.setEnableFooterFollowWhenLoadFinished(false);//是否在全部加载结束之后Footer跟随内容1.0.4
                layout.setEnableClipFooterWhenFixedBehind(true);//是否剪裁Footer当时样式为FixedBehind时1.0.5
            }
        });
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {

            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                new  MaterialHeader(context,null,5);
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                layout.setEnableFooterFollowWhenLoadFinished(true);
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    public LoginBean getUserBean() {
        try {
            FileInputStream stream = this.openFileInput("data.UserInfo");
            ObjectInputStream ois = new ObjectInputStream(stream);
            LoginBean userBean = (LoginBean) ois.readObject();
            return userBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUserBean(LoginBean userBean) {
        try {
            FileOutputStream stream = this.openFileOutput("data.UserInfo", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(userBean);// td is an Instance of TableData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public YzLoginBean getYzUserBean() {
        try {
            FileInputStream stream = this.openFileInput("yzdata.UserInfo");
            ObjectInputStream ois = new ObjectInputStream(stream);
            YzLoginBean userBean = (YzLoginBean) ois.readObject();
            return userBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setYzUserBean(YzLoginBean userBean) {
        try {
            FileOutputStream stream = this.openFileOutput("yzdata.UserInfo", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(userBean);// td is an Instance of TableData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    public BleDevice getBleDevice() {
//        try {
//            FileInputStream stream = this.openFileInput("data.bleinfo");
//            ObjectInputStream ois = new ObjectInputStream(stream);
//            BleDevice userBean = (BleDevice) ois.readObject();
//            return userBean;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void setBleDevice(BleDevice userBean) {
//        try {
//            FileOutputStream stream = this.openFileOutput("data.bleinfo", MODE_PRIVATE);
//            ObjectOutputStream oos = new ObjectOutputStream(stream);
//            oos.writeObject(userBean);// td is an Instance of TableData;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void setBleDevice(BleDevice bleDevice) {
        FileOutputStream fos;
        try {
            fos = getApplicationContext().openFileOutput("data.bleinfo",
                    Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            Parcel parcel = Parcel.obtain();

            parcel.writeParcelable(bleDevice, 0);

            bos.write(parcel.marshall());
            bos.flush();
            bos.close();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BleDevice getBleDevice() {
        FileInputStream fis;
        try {
            fis = getApplicationContext().openFileInput("data.bleinfo");
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(bytes, 0, bytes.length);
            parcel.setDataPosition(0);

            BleDevice data = parcel.readParcelable(BleDevice.class.getClassLoader());

            fis.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
