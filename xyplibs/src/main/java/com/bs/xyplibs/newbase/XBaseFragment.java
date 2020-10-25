package com.bs.xyplibs.newbase;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bs.xyplibs.R;
import com.bs.xyplibs.base.BaseActivity;
import com.bs.xyplibs.base.BaseApp;
import com.bs.xyplibs.bean.MessageEvent;
import com.bs.xyplibs.utils.EventBusUtil;
import com.bs.xyplibs.utils.ToastUtils;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import pub.devrel.easypermissions.AppSettingsDialog;


/**
 * Created by Administrator on 2017/12/30.
 */

/**
 * 若把初始化内容放到initData实现,就是采用Lazy方式加载的Fragment
 * 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可
 * -
 * -注1: 如果是与ViewPager一起使用，调用的是setUserVisibleHint。
 * ------可以调用mViewPager.setOffscreenPageLimit(size),若设置了该属性 则viewpager会缓存指定数量的Fragment
 * -注2: 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
 * -注3: 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show
 */
public abstract class XBaseFragment extends Fragment {

//    protected int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
//            0xFFE6B800, 0xFF7CFC00};

    protected float pxTodp(float value){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float valueDP= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
        return valueDP;
    }
    protected Activity mActivity;
    /**
     * 初始化布局, 子类必须实现
     */
    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化数据, 子类可以不实现
     */
    public abstract void initData();

    public boolean isVisible;                  //是否可见状态
    private boolean isPrepared;                 //标志位，View已经初始化完成。
    private boolean isFirstLoad = true;         //是否第一次加载
    protected LayoutInflater inflater;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
        ToastUtils.cancelToast();
    }
//    public void setBarModel(boolean isBlack){
//        if(isBlack){
//            int color = getResources().getColor(R.color.white);
//            StatusBarUtil.setColor(mActivity, color, 0);
//            StatusBarUtil.setLightMode(mActivity);
//        }else{
//            int color3 = getResources().getColor(R.color.colorPrimary);
//            StatusBarUtil.setColor(mActivity, color3, 0);
//            StatusBarUtil.setDarkMode(mActivity);
//        }
//
//    }
    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(MessageEvent event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(MessageEvent event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }
    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(MessageEvent event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(MessageEvent event) {

    }
    /**
     * 判断是否有某项权限
     * @param string_permission 权限
     * @param request_code 请求码
     * @return
     */
    public boolean checkReadPermission(Activity activity,String string_permission,int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(activity, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(activity, new String[]{string_permission}, request_code);
        }
        return flag;
    }
    /**
     * 检查权限后的回调
     * @param requestCode 请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码
    private String strPhone="";
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                    Toast.makeText(mActivity,"请允许拨号权限后再试",Toast.LENGTH_SHORT).show();
                    new AppSettingsDialog.Builder(this).setTitle("权限访问受限")
                            .setRationale("打开系统设置界面，开启电话权限")
                            .build().show();
                } else {//成功
//                    call("tel:"+"10086");
                    call(mActivity,strPhone);
                }
                break;
        }
    }
    /**
     * 拨打电话（直接拨打）
     * @param telPhone 电话
     */
    public void call(Activity activity,String telPhone){
            strPhone=telPhone;
            if(checkReadPermission(activity,Manifest.permission.CALL_PHONE,REQUEST_CALL_PERMISSION)){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telPhone));
                activity.startActivity(intent);
            }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater=inflater;
        isFirstLoad=true;

        View view=initView(inflater,container,savedInstanceState);
        isPrepared=true;
        lazyLoad();
        return view;
    }
    public  void hideSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /** 如果是与ViewPager一起使用，调用的是setUserVisibleHint */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }

    }

    protected void onInvisible() {

    }

    protected void onVisible() {
        lazyLoad();
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
//    protected void jumpActivity(Class<?> activity,boolean isfinish){
//        Intent intent=new Intent(mActivity,activity);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        mActivity.startActivity(intent);
//        if(isfinish){
//            mActivity.finish();
//        }
//    }
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initData();
    }
    public void showToast(String msg) {
        Toast.makeText(BaseApp.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
    public void goH5(Class<?> activity,String name,String url,String title){
        Intent i1=new Intent(mActivity, activity);
        i1.putExtra("name",name);
        i1.putExtra("url",url);
        i1.putExtra("title",title);
        startActivity(i1);

    }
    public void goH5(Class<?> activity,String name,String url,String title,String type,String id){
        Intent i1=new Intent(mActivity, activity);
        i1.putExtra("name",name);
        i1.putExtra("url",url);
        i1.putExtra("title",title);
        i1.putExtra("type",type);
        i1.putExtra("id",id);
        startActivity(i1);

    }

    public  void setRequestedOrientation(int screenOrientationLandscape){};
}
