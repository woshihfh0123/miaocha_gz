package com.mc.phonelive.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.AppContext;
import com.mc.phonelive.HtmlConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.LoginTypeAdapter;
import com.mc.phonelive.bean.ConfigBean;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.event.RegSuccessEvent;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.mob.LoginData;
import com.mc.phonelive.mob.MobBean;
import com.mc.phonelive.mob.MobCallback;
import com.mc.phonelive.mob.MobLoginUtil;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.ValidatePhoneUtil;
import com.mc.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


/**
 * Created by cxf on 2018/9/17.
 * 登录
 */

public class LoginActivity extends AbsActivity implements OnItemClickListener<MobBean>, View.OnClickListener {

    private EditText mEditPhone;
    private EditText mEditPwd;
    private View mBtnLogin;
    private ImageView back_img;
    private RecyclerView mRecyclerView;
    private MobLoginUtil mLoginUtil;
    private boolean mFirstLogin;//是否是第一次登录
    private boolean mShowInvite;//显示邀请码弹窗
    private String mLoginType = "phone";//登录方式
    private ImageView iv_sjk;  //是否同意用户协议选择按钮

    boolean isAgreeXY = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void main() {
        iv_sjk = findViewById(R.id.iv_sjk);
        mEditPhone = (EditText) findViewById(R.id.edit_phone);
        mEditPwd = (EditText) findViewById(R.id.edit_pwd);
        mBtnLogin = findViewById(R.id.btn_login);
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = mEditPhone.getText().toString();
                String pwd = mEditPwd.getText().toString();
                mBtnLogin.setEnabled(!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pwd));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mEditPhone.addTextChangedListener(textWatcher);
        mEditPwd.addTextChangedListener(textWatcher);
        boolean otherLoginType = false;
        ConfigBean configBean = AppConfig.getInstance().getConfig();
        if (configBean != null) {
            List<MobBean> list = MobBean.getLoginTypeList(configBean.getLoginType());
            if (list != null && list.size() > 0) {
                mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                LoginTypeAdapter adapter = new LoginTypeAdapter(mContext, list);
                adapter.setOnItemClickListener(this);
                mRecyclerView.setAdapter(adapter);
                mLoginUtil = new MobLoginUtil();
                otherLoginType = true;
            }
        }
        if (!otherLoginType) {
            findViewById(R.id.other_login_tip).setVisibility(View.INVISIBLE);
        }
        EventBus.getDefault().register(this);


        iv_sjk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAgreeXY){
                    isAgreeXY = false;
                    iv_sjk.setImageResource(R.drawable.check_u);
                }else{
                    isAgreeXY = true;
                    iv_sjk.setImageResource(R.drawable.check_y);
                }
            }
        });
    }


    public static void forward() {
        Intent intent = new Intent(AppContext.sInstance, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        AppContext.sInstance.startActivity(intent);
    }


    public void loginClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.btn_forget_pwd:
                forgetPwd();
                break;
            case R.id.btn_tip:
                forwardTip();
                break;
        }
    }

    //注册
    private void register() {
        startActivity(new Intent(mContext, RegisterActivity.class));
    }

    //忘记密码
    private void forgetPwd() {
        startActivity(new Intent(mContext, FindPwdActivity.class));
    }


    //手机号密码登录
    private void login() {
        String phoneNum = mEditPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            mEditPhone.setError(WordUtil.getString(R.string.login_input_phone));
            mEditPhone.requestFocus();
            return;
        }
        if (!ValidatePhoneUtil.validateMobileNumber(phoneNum)) {
            mEditPhone.setError(WordUtil.getString(R.string.login_phone_error));
            mEditPhone.requestFocus();
            return;
        }
        String pwd = mEditPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            mEditPwd.setError(WordUtil.getString(R.string.login_input_pwd));
            mEditPwd.requestFocus();
            return;
        }
        if (!isAgreeXY) {
            ToastUtil.show("请先同意服务协议和信息保护政策");
            return;
        }

        mLoginType = "phone";
        HttpUtil.login(phoneNum, pwd, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                onLoginSuccess(code, msg, info);
            }
        });
    }

    //登录即代表同意服务和隐私条款
    private void forwardTip() {
        WebViewActivity.forward(mContext, HtmlConfig.LOGIN_PRIVCAY);
    }

    //登录成功！
    private void onLoginSuccess(int code, String msg, String[] info) {
        if (code == 0 && info.length > 0) {
            JSONObject obj = JSON.parseObject(info[0]);
            String uid = obj.getString("id");
            String token = obj.getString("token");
            String is_store = obj.getString("is_store");
            mFirstLogin = obj.getIntValue("isreg") == 1;
            mShowInvite = obj.getIntValue("isagent") == 1;
            AppConfig.getInstance().setLoginInfo(uid, token, true);
            AppConfig.getInstance().setisStore(is_store, true);
            getBaseUserInfo();
            //友盟统计登录
            MobclickAgent.onProfileSignIn(mLoginType, uid);
        } else {
            ToastUtil.show(msg);
        }
    }

    /**
     * 获取用户信息
     */
    private void getBaseUserInfo() {
        HttpUtil.getBaseInfo(new CommonCallback<UserBean>() {
            @Override
            public void callback(UserBean bean) {
                Log.v("log",mShowInvite+"-------------------------------------------mShowInvite");
                MainActivity.forward(mContext, mShowInvite);
//                if (mFirstLogin) {
//                    RecommendActivity.forward(mContext, mShowInvite);
//                } else {
//                    MainActivity.forward(mContext, mShowInvite);
//                }
                finish();
            }
        });
    }

    /**
     * 三方登录
     */
    private void loginBuyThird(LoginData data) {
        mLoginType = data.getType();
        Log.e("dddddd:",data.getOpenID()+"");
        HttpUtil.loginByThird(data.getOpenID(), data.getNickName(), data.getAvatar(), data.getType(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                onLoginSuccess(code, msg, info);
            }
        });
    }

    @Override
    public void onItemClick(MobBean bean, int position) {
        if (mLoginUtil == null) {
            return;
        }
        final Dialog dialog = DialogUitl.loginAuthDialog(mContext);
        dialog.show();
        Log.e("ddddddsss:",bean.getType()+"");
        mLoginUtil.execute(bean.getType(), new MobCallback() {
            @Override
            public void onSuccess(Object data) {
                Log.e("ddddddsaaass:",((LoginData) data).getOpenID()+"");
                if (data != null) {
                    loginBuyThird((LoginData) data);
                }
            }

            @Override
            public void onError() {
                Log.e("ddddddsdeeeeeeee:","eeeeee");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish() {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegSuccessEvent(RegSuccessEvent e) {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        HttpUtil.cancel(HttpConsts.LOGIN);
        HttpUtil.cancel(HttpConsts.GET_QQ_LOGIN_UNION_ID);
        HttpUtil.cancel(HttpConsts.LOGIN_BY_THIRD);
        HttpUtil.cancel(HttpConsts.GET_BASE_INFO);
        if (mLoginUtil != null) {
            mLoginUtil.release();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
       LoginActivity.this.finish();
    }
}
