package com.mc.phonelive.activity;

import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

/**
 * Created by cxf on 2018/9/29.
 * 设置性别
 */

public class EditSexActivity extends AbsActivity implements View.OnClickListener {

    private RadioButton mBtnMale;
    private RadioButton mBtnFeMale;
    private int mSex;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_sex;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.edit_profile_sex));
        mBtnMale = (RadioButton)findViewById(R.id.btn_male);
        mBtnFeMale = (RadioButton)findViewById(R.id.btn_female);
        mBtnMale.setOnClickListener(this);
        mBtnFeMale.setOnClickListener(this);
        mSex = getIntent().getIntExtra(Constants.SEX, Constants.SEX_MALE);
        Log.e("SSSSS:",mSex+"");
//        if (mSex ==2) {
//            mBtnMale.setChecked(true);
//            mBtnFeMale.setChecked(false);
//        }else if(mSex==1){
//            mBtnFeMale.setChecked(true);
//            mBtnMale.setChecked(false);
//        }
    }

    @Override
    public void onClick(View v) {
        if(!canClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.btn_male:
                EventBusUtil.postEvent(new EventBean("send_sex_from_ck","男"));
                setSex(Constants.SEX_MALE);
                break;
            case R.id.btn_female:
                EventBusUtil.postEvent(new EventBean("send_sex_from_ck","女"));
                setSex(Constants.SEX_FEMALE);
                break;
        }
    }

    private void setSex(int sex) {
//        if (mSex == sex) {
//            return;
//        }
//        mSex = sex;
        HttpUtil.updateFields("{\"sex\":\"" + sex + "\"}", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ToastUtil.show(obj.getString("msg"));
//                    if (mSex == Constants.SEX_MALE) {
//                        mBtnMale.setChecked(true);
//                    } else {
//                        mBtnFeMale.setChecked(true);
//                    }
//                    UserBean u = AppConfig.getInstance().getUserBean();
//                    if (u != null) {
//                        u.setSex(mSex+"");
//                    }
//                    Intent intent = getIntent();
//                    intent.putExtra(Constants.SEX, mSex);
//                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpConsts.UPDATE_FIELDS);
        super.onDestroy();
    }
}
