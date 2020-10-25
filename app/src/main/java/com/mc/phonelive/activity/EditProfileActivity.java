package com.mc.phonelive.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.foxtone.CityChoiseActivity;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.ActivityResultCallback;
import com.mc.phonelive.interfaces.ImageResultCallback;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.ProcessImageUtil;
import com.mc.phonelive.utils.SpUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;

/**
 * Created by cxf on 2018/9/29.
 * 我的 编辑资料999999
 */

public class EditProfileActivity extends AbsActivity {

    private ImageView mAvatar;
    private TextView mName;
    private TextView mSign;
    private TextView mBirthday;
    private TextView mCityName;
    private TextView mSex;
    private ProcessImageUtil mImageUtil;
    private TextView id_num;
    private UserBean mUserBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.edit_profile));
        SpUtil.getInstance().setBooleanValue("setMe",false);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        mSign = (TextView)findViewById(R.id.sign);
        id_num = (TextView)findViewById(R.id.id_num);
        mBirthday = (TextView)findViewById(R.id.birthday);
        mSex = (TextView) findViewById(R.id.sex);
        mCityName = (TextView) findViewById(R.id.city);
        mImageUtil = new ProcessImageUtil(this);
        Log.e("ssssss:",AppConfig.getInstance().getUserBean().getSex());
        mSex.setText(AppConfig.getInstance().getUserBean().getSex().equals("1")?"男":"女");
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {

            }

            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    ImgLoader.display(file, mAvatar);
                    HttpUtil.updateAvatar(file, new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0 && info.length > 0) {
                                ToastUtil.show(R.string.edit_profile_update_avatar_success);
                                UserBean bean = AppConfig.getInstance().getUserBean();
                                if (bean != null) {
                                    JSONObject obj = JSON.parseObject(info[0]);
                                    bean.setAvatar(obj.getString("avatar"));
                                    bean.setAvatarThumb(obj.getString("avatarThumb"));
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
            }
        });
        mUserBean = AppConfig.getInstance().getUserBean();
        if (mUserBean != null) {
            showData(mUserBean);
        } else {
//            HttpUtil.getBaseInfo(new CommonCallback<UserBean>() {
//                @Override
//                public void callback(UserBean u) {
//                    mUserBean = u;
//                    showData(u);
//                }
//            });
        }
    }


    public void editProfileClick(View v) {
        if (!canClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_avatar:
                editAvatar();
                break;
            case R.id.btn_name:
                forwardName();
                break;
            case R.id.btn_sign:
                forwardSign();
                break;
            case R.id.btn_birthday:
                editBirthday();
                break;
            case R.id.btn_city:
               choiseCity();
                break;
            case R.id.btn_sex:
                forwardSex();
                break;
            case R.id.btn_impression:
                forwardImpress();
                break;
        }
    }

    private void choiseCity() {
        if (mUserBean == null) {
            return;
        }
        Intent intent = new Intent(mContext, CityChoiseActivity.class);
        intent.putExtra(Constants.CITYNAME, mUserBean.getCity());
        mImageUtil.startActivityForResult(intent, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    String name = intent.getStringExtra(Constants.CITYNAME);
                    mUserBean.setArea(name);
                    mCityName.setText(name);
                }
            }
        });
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

    private void forwardName() {
        if (mUserBean == null) {
            return;
        }
        Intent intent = new Intent(mContext, EditNameActivity.class);
        intent.putExtra(Constants.NICK_NAME, mUserBean.getUserNiceName());
        mImageUtil.startActivityForResult(intent, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    String name = intent.getStringExtra(Constants.NICK_NAME);
                    mUserBean.setUserNiceName(name);
                    mName.setText(name);
                }
            }
        });
    }


    private void forwardSign() {
        if (mUserBean == null) {
            return;
        }
        Intent intent = new Intent(mContext, EditSignActivity.class);
        intent.putExtra(Constants.SIGN, mUserBean.getSignature());
        mImageUtil.startActivityForResult(intent, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    String sign = intent.getStringExtra(Constants.SIGN);
                    mUserBean.setSignature(sign);
                    mSign.setText(sign);
                }
            }

        });
    }

    private void editBirthday() {
        if (mUserBean == null) {
            return;
        }
        DialogUitl.showDatePickerDialog(mContext, new DialogUitl.DataPickerCallback() {
            @Override
            public void onConfirmClick(final String date) {
                HttpUtil.updateFields("{\"birthday\":\"" + date + "\"}", new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            if (info.length > 0) {
                                ToastUtil.show(JSON.parseObject(info[0]).getString("msg"));
                                mUserBean.setBirthday(date);
                                mBirthday.setText(date);
                            }
                        } else {
                            ToastUtil.show(msg);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    @Subscribe
    public void getEvent(EventBean event){
        switch(event.getCode()){
            case "send_sex_from_ck":
            String str= (String) event.getFirstObject();
            mSex.setText(str);

                break;
            default:

                break;
        }
    };
    private void forwardSex() {
        if (mUserBean == null) {
            return;
        }
        Intent intent = new Intent(mContext, EditSexActivity.class);
        intent.putExtra(Constants.SEX, mUserBean.getSex());
        mImageUtil.startActivityForResult(intent, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    int sex = intent.getIntExtra(Constants.SEX, 0);
                    if (sex == 1) {
                        mSex.setText(R.string.sex_male);
                        mUserBean.setSex(sex+"");
                    } else if (sex == 2) {
                        mSex.setText(R.string.sex_female);
                        mUserBean.setSex(sex+"");
                    }
                }
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    /**
     * 我的印象
     */
    private void forwardImpress() {
        startActivity(new Intent(mContext, MyImpressActivity.class));
    }

    @Override
    protected void onDestroy() {
        if (mImageUtil != null) {
            mImageUtil.release();
        }
        HttpUtil.cancel(HttpConsts.UPDATE_AVATAR);
        HttpUtil.cancel(HttpConsts.UPDATE_FIELDS);
        SpUtil.getInstance().setBooleanValue("setMe",true);
        super.onDestroy();
    }

    private void showData(UserBean u) {
        ImgLoader.displayAvatar(u.getAvatar(), mAvatar);
        mName.setText(u.getUserNiceName());
        mSign.setText(u.getSignature());
        mBirthday.setText(u.getBirthday());
        id_num.setText("渺茶"+u.getLiangNameTip());
        mCityName.setText(u.getCity());
        mSex.setText(u.getSex().equals("1") ? R.string.sex_male : R.string.sex_female);

    }

}
