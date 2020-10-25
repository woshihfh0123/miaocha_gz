package com.bs.xyplibs.newbase;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.xyplibs.R;
import com.bs.xyplibs.base.BaseApp;
import com.bs.xyplibs.bean.EventBean;
import com.bs.xyplibs.bean.MessageEvent;
import com.bs.xyplibs.callback.MyUiCallback;
import com.bs.xyplibs.utils.ActivityCollector;
import com.bs.xyplibs.utils.EventBusUtil;
import com.bs.xyplibs.utils.KeyboardUtil;
import com.bs.xyplibs.utils.ToastUtils;
import com.bs.xyplibs.view.MyTitleBarView;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.CheckBox;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import pub.devrel.easypermissions.AppSettingsDialog;


/**
 * Created by Administrator on 2018/7/31.
 */

public abstract class XBaseActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate, MyUiCallback,View.OnClickListener {
    protected BGASwipeBackHelper mSwipeBackHelper;
    public XBaseActivity mActivity;
    public LinearLayout mBase_content_layout;
    public TextView mNo_content_tv;
    public TextView mNo_net_tv;
    private LinearLayout rootLayout;
    private Bundle sis;
    private boolean isBack=true;
    public   MyTitleBarView my_titlebar;
    private boolean isMainActivity = false;//是否是主界面
    private long exitTime = 0;//退出时间
    protected View v_actionbar;
    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码
    private String strPhone="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 在 super.onCreate(savedInstanceState) 之前调用该方法

        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mActivity = this;

        sis=savedInstanceState;
        super.setContentView(R.layout.xbase_activity);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        baseInitView();
        setBarModel(true);
        initView(savedInstanceState);
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
//        ActivityCollector.addActivity(this);
//        setStatusBar();
        setListener();
        processLogic(savedInstanceState);

        BaseApp.addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
//        initSystemBarTint();
    }
    public void goH5(Class<?> activity,String name,String url,String title){
        Intent i1=new Intent(mActivity, activity);
        i1.putExtra("name",name);
        i1.putExtra("url",url);
        i1.putExtra("title",title);
        startActivity(i1);

    }
    /**
     * 把状态栏设成透明
     */
    /*
    private void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = this.getWindow().getDecorView();
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                return defaultInsets.replaceSystemWindowInsets(
                        defaultInsets.getSystemWindowInsetLeft(),
                        0,
                        defaultInsets.getSystemWindowInsetRight(),
                        defaultInsets.getSystemWindowInsetBottom());
            });
            ViewCompat.requestApplyInsets(decorView);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }
    */
    public void goH5(Class<?> activity,String name,String url,String title,String type,String id){
        Intent i1=new Intent(mActivity, activity);
        i1.putExtra("name",name);
        i1.putExtra("url",url);
        i1.putExtra("title",title);
        i1.putExtra("type",type);
        i1.putExtra("id",id);
        startActivity(i1);

    }

    /**
     * 设置通知栏颜色
     *
     * @param color
     */
    protected void setStatueBarColor(String color) {
        if (my_titlebar != null) {
            my_titlebar.setStatueBarColor(Color.parseColor(color));
        }

    }
    protected void setNoTitle(){
        v_actionbar.setVisibility(View.GONE);
        my_titlebar.setVisibility(View.GONE);
    }
    public void  back(View view){
        KeyboardUtil.closeKeyboard(this);
        finish();
    }
    /**
     * 判断是否有某项权限
     * @param string_permission 权限
     * @param request_code 请求码
     * @return
     */
    public boolean checkReadPermission(String string_permission,int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(this, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(this, new String[]{string_permission}, request_code);
        }
        return flag;
    }
    /**
     * 检查权限后的回调
     * @param requestCode 请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                    Toast.makeText(this,"请允许拨号权限后再试",Toast.LENGTH_SHORT).show();
                    new AppSettingsDialog.Builder(this).setTitle("权限访问受限")
                            .setRationale("打开系统设置界面，开启电话权限")
                            .build().show();
                } else {//成功
//                    call("tel:"+"10086");
                    call(strPhone);
                }
                break;
        }
    }
    /**
     * 拨打电话（直接拨打）
     * @param telPhone 电话
     */
    public void call(String telPhone){
        try {
            strPhone=telPhone;
            if(checkReadPermission(Manifest.permission.CALL_PHONE,REQUEST_CALL_PERMISSION)){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telPhone.toString().trim()));
                mActivity.startActivity(intent);
            }
        }catch (Exception e){
            e.getMessage();
        }

    }
    @Override
    public void onClick(View v) {

    }

    public void setBarModel(boolean isBlack){
        try{
            if(isBlack){
                int color = getResources().getColor(R.color.white);
                StatusBarUtil.setColor(mActivity, color, 0);
                StatusBarUtil.setLightMode(mActivity);
            }else{
                int color3 = getResources().getColor(R.color.YzcolorBlue);
                StatusBarUtil.setColor(mActivity, color3, 0);
                StatusBarUtil.setDarkMode(mActivity);
            }
        }catch (Exception e){
            Logger.e("StatusBarUtil异常");
        }


    }
    /**
     * 接收到分发的事件
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventBean event) {

    }
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.YzcolorBlue));
    }
    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveStickyEvent(EventBean event) {
    }

    protected void setlayoutview(int id) {
        show();
        View.inflate(this, id, mBase_content_layout);
    }
    public void baseInitView() {
        mBase_content_layout = (LinearLayout) findViewById(R.id.base_content_layout);
        mNo_content_tv = (TextView) findViewById(R.id.no_content_tv);
        mNo_net_tv = (TextView) findViewById(R.id.no_net_tv);
        my_titlebar =findViewById(R.id.my_titlebar);
        v_actionbar =findViewById(R.id.v_actionbar);
    }



    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this,layoutResID,null));
    }


    /** 获取主题色 */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }
    /** 子类可以重写改变状态栏颜色 */
    protected int setStatusBarColor() {
        return getColorPrimary();
    }
    /** 子类可以重写决定是否使用透明状态栏 */
    protected boolean translucentStatusBar() {
        return false;
    }

    private void initSystemBarTint() {
        Window window = getWindow();
        if (translucentStatusBar()) {
            // 设置状态栏全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            return;
        }
        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上使用原生方法
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(setStatusBarColor());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4-5.0使用三方工具类，有些4.4的手机有问题，这里为演示方便，不使用沉浸式
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintColor(setStatusBarColor());
        }
    }
    /** 获取深主题色 */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }
    /** 初始化 Toolbar */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
    @Override
    public void baseHasData() {
        mNo_net_tv.setVisibility(View.GONE);
        mNo_content_tv.setVisibility(View.GONE);
        mBase_content_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void show() {
        mBase_content_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void baseNoData() {
        mBase_content_layout.setVisibility(View.GONE);
        mNo_content_tv.setVisibility(View.VISIBLE);
    }
    @Override
    public void baseNoNet() {
        mBase_content_layout.setVisibility(View.GONE);
        mNo_net_tv.setVisibility(View.VISIBLE);
        mNo_net_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogic(sis);
            }
        });
    }
    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }
    public void showToast(String msg) {
//        com.hjq.toast.ToastUtils.show(msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(@ColorInt int color) {
        setStatusBarColor(color, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }
    protected void jumpActivity(Class<?> activity){
        Intent intent=new Intent(mActivity,activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mActivity.startActivity(intent);
    }
    protected void jumpActivity(Class<?> activity,String info){
        Intent intent=new Intent(mActivity,activity);
        intent.putExtra("info",info);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mActivity.startActivity(intent);
    }
    /**
     * 设置状态栏颜色
     *
     * @param color
     * @param statusBarAlpha 透明度
     */
    public void setStatusBarColor(@ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        StatusBarUtil.setColorForSwipeBack(this, color, statusBarAlpha);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化布局以及View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected  void setListener(){

    };

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);

//    /**
//     * 需要处理点击事件时，重写该方法
//     *
//     * @param v
//     */
//    public void onClick(View v) {
//    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
        ToastUtils.cancelToast();
//        ActivityCollector.removeActivity(this);
//        ToastUtils.cancelToast();
//        Log.i(this.getClass().getSimpleName(), "onDestroy");
    }
    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }
    protected String getInfoFromIntent(){
      return getIntent().getStringExtra("info");
    };
    @Override
    protected void onStop() {
        super.onStop();
//        Log.i(this.getClass().getSimpleName(), "onStop");
    }
    public  void hideSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * 设置是否是MainActivity
     *
     * @param isMainActivity
     */
    protected void setMainActivity(boolean isMainActivity) {
        this.isMainActivity = isMainActivity;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isMainActivity) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    showToast("再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                } else {
                    KeyboardUtil.closeKeyboard(this);
                    removeActivity(this);//将该Activity从集合中移除
                    //System.exit(0);//彻底关闭应用
                }
                return true;
            } else {
                removeActivity(this);
            }
        }
        return super.onKeyDown(keyCode, event);

    }
    /**
     * 对TitleBar进行复杂的操作可以拿到对象进行操作
     * 获取MyTititleBarView
     *
     * @return
     */
    protected MyTitleBarView getMyTitleBarView() {
        return my_titlebar;
    }
    /**
     * 隐藏titleBar
     */
    protected void hideTitleBar() {

        if (my_titlebar != null) {
            my_titlebar.setVisibility(View.GONE);
        }
    }
    /**
     * 是否显示返回键
     *
     * @param isVisiable
     */
    protected void setIsVisiableBack(boolean isVisiable) {
        if(my_titlebar!=null){
            my_titlebar.isVisiableBack(isVisiable);
        }
    }


    /**
     * 移除activity
     *
     * @param activity
     */
    protected void removeActivity(Activity activity) {
        BaseApp.getInstance().removeActivity(activity);
    }
}
