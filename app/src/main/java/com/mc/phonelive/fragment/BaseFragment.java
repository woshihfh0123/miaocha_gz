package com.mc.phonelive.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mc.phonelive.AppContext;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DialogUtil;
import com.mc.phonelive.utils.EventBusUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by WWL on 2020/4/6 0006:16
 */
public abstract class BaseFragment extends Fragment {

    private Context context;

    private Unbinder unbinder;

    private Dialog mDialog;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        initHttpData();

    }


    /**
     * 初始化控件
     */
    protected abstract void initView(View view);


    /**
     * 联网请求
     */
    protected abstract void initHttpData();


    /** 如果是与ViewPager一起使用，调用的是setUserVisibleHint */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
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
            onVisible();
        } else {
            onInvisible();
        }

    }

    protected void onInvisible() {

    }

    protected void onVisible() {
    }

    /**
     * @param cls 要启动的activity
     */
    protected void startActivity(Class<?> cls){
        CommentUtil.startActivity(context,cls);
    }


    //     显示dialog
    public void showDialog() {
        try {
            if (mDialog == null) {
                mDialog = DialogUtil.createLoadingDialog(AppContext.sInstance);
            }
            mDialog.show();
        } catch (Exception e) {
        }
    }

    // 隐藏dialog
    public void dismissDialog() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }
    protected boolean isRegisterEventBus() {
        return false;
    }
    @Override
    public void onDestroy() {
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
        if (unbinder!=null){
            unbinder.unbind();
        }
        super.onDestroy();
    }

}
