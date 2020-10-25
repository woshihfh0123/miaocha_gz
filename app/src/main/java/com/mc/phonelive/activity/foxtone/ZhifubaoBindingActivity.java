package com.mc.phonelive.activity.foxtone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.BaseVO;
import com.mc.phonelive.bean.foxtone.TradeAccountBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.httpnet.Callback;
import com.mc.phonelive.httpnet.HttpUtils;
import com.mc.phonelive.httpnet.JsonUtils;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.interfaces.ImageResultCallback;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ProcessImageUtil;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支付宝收款绑定
 */
public class ZhifubaoBindingActivity extends AbsActivity {
    @BindView(R.id.name_edit)
    EditText nameEdit;
    @BindView(R.id.account_edit)
    EditText accountEdit;
    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.erwema_img)
    ImageView erwemaImg;
    @BindView(R.id.binding_submit)
    TextView bindingSubmit;

    TradeAccountBean.InfoBean.ListInfoBean mData = null;

    private String mEwmImg = "";
    private ProcessImageUtil mImageUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.zfb_binding_layout;
    }

    @Override
    protected void main() {
        setTitle("收款绑定");
        TradeAccountBean.InfoBean.ListInfoBean data = (TradeAccountBean.InfoBean.ListInfoBean) this.getIntent().getSerializableExtra("data");
        if (!DataSafeUtils.isEmpty(data)) {
            mData = data;
            nameEdit.setText(data.getName());
            accountEdit.setText(data.getAccount());
            phoneEdit.setText(data.getMobile());
            if (!DataSafeUtils.isEmpty(data.getApi_ewm())) {
                mEwmImg =data.getApi_ewm();
                RequestOptions options = new RequestOptions().error(R.drawable.default_img);
                Glide.with(mContext).load(data.getApi_ewm()).apply(options).into(erwemaImg);
            }
        }

        imageCallBackData();
    }

    private void imageCallBackData() {
        mImageUtil = new ProcessImageUtil(this);
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {
            }

            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    ImgLoader.display(file, erwemaImg);
                    mEwmImg = file.toString();
                }
            }

            @Override
            public void onFailure() {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.erwema_img, R.id.binding_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.erwema_img:
                editAvatar();
                break;
            case R.id.binding_submit:
                String mName = nameEdit.getText().toString();
                String mAccount = accountEdit.getText().toString();
                String mPhone = phoneEdit.getText().toString();
                if (DataSafeUtils.isEmpty(mName)) {
                    ToastUtil.show("请填写您要绑定的账号姓名");
                    return;
                }
                if (DataSafeUtils.isEmpty(mAccount)) {
                    ToastUtil.show("请填写您要绑定的账号");
                    return;
                }
                if (DataSafeUtils.isEmpty(mPhone)) {
                    ToastUtil.show("请填写您要绑定的手机号");
                    return;
                }
                if (DataSafeUtils.isEmpty(mEwmImg)) {
                    ToastUtil.show("请上传支付宝收款二维码");
                    return;
                }
                if (!mEwmImg.startsWith(AppConfig.HOST)) {
                    submitData(mName, mAccount, mPhone, mEwmImg);
                }else {
                    submitData01(mName, mAccount, mPhone, mEwmImg);
                }
                break;
        }
    }

    private void editAvatar() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{
                R.string.camera, R.string.alumb}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    mImageUtil.getImageByCamera();
                } else {
                    mImageUtil.getImageByAlumb();
                }
            }
        });
    }

    /**
     * 绑定支付宝
     *如果是修改信息，但是 在没有选择图片的情况下
     * @param mName
     * @param mAccount
     * @param mPhone
     * @param mEwmImg
     */
    private void submitData01(String mName, String mAccount, String mPhone, String mEwmImg) {
        HttpUtil.setTradeAccount("1", mName,mAccount,  mPhone, mEwmImg, "", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (code==0){
                    EventBus.getDefault().post(new EventBusModel("refresh_data"));
                    ZhifubaoBindingActivity.this.finish();
                }
            }
        });
    }
    /**
     * 绑定支付宝
     *
     * @param mName
     * @param mAccount
     * @param mPhone
     * @param mEwmImg
     */
    private void submitData(String mName, String mAccount, String mPhone, String mEwmImg) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", AppConfig.getInstance().getUid());
        map.put("token", AppConfig.getInstance().getToken());
        map.put("type", "1");
        map.put("name", mName);
        map.put("account",mAccount + "");
        map.put("mobile", mPhone);
        Map<String, List<String>> filepath = new HashMap<>();

        Map<String, String> singleFilePath = new HashMap<>();
        singleFilePath.put("file", mEwmImg);

        String mUrls = AppConfig.HOST + "/api/public/?service=";
        HttpUtils.POST_WHITH_UPLOAD(mUrls + "Trade.setTradeAccount", map, singleFilePath, filepath, true, BaseVO.class, new Callback<BaseVO>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSucceed(String json, String code, BaseVO model) {
                if (!DataSafeUtils.isEmpty(model)) {

                    if (model.getRet() == 200) {
                        ToastUtil.show( "添加成功");
                        EventBus.getDefault().post(new EventBusModel("refresh_data"));
                        ZhifubaoBindingActivity.this.finish();
                    }else{
                        if (!DataSafeUtils.isEmpty(model.getMsg())) {
                            ToastUtil.show(model.getMsg() + "");
                        }
                    }
                }
            }

            @Override
            public void onOtherStatus(String json, String code) {
                String msg = JsonUtils.getSinglePara(json, "msg");
                Log.v("tags", msg);
            }

            @Override
            public void onFailed(Throwable e) {
                Log.v("tags", "---错误---"+e.getMessage());
            }

            @Override
            public void onFinish() {
                Log.v("tags", "---结束---");
            }
        });

//        HttpUtil.setTradeAccount("1", mName, mAccount, mPhone, mEwmImg, "", new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//                ToastUtil.show(msg);
//                if (code == 0) {
//                    EventBus.getDefault().post(new EventBusModel("refresh_data"));
//                    ZhifubaoBindingActivity.this.finish();
//                }
//            }
//        });
    }

}
