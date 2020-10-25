package com.mc.phonelive.activity.foxtone;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.BaseVO;
import com.mc.phonelive.httpnet.Callback;
import com.mc.phonelive.httpnet.HttpUtils;
import com.mc.phonelive.httpnet.JsonUtils;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 创建俱乐部
 */
@RuntimePermissions
public class CreateClubActivity extends AbsActivity {
    @BindView(R.id.name_edit)
    EditText nameEdit;
    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.summary_edit)
    EditText summaryEdit;
    @BindView(R.id.club_add_img)
    ImageView clubAddImg;
    @BindView(R.id.club_submit)
    TextView clubSubmit;

    private ArrayList<String> mPicList = new ArrayList<>();
    private String mImgs = "";
    public static int SELECTIMGCOUNT = 1; // 可以上传图片的数量
    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;
    public static final String SDCARD_CACHE = "com.zhiboshow/files/"; // 文件sdk缓存

    @Override
    protected int getLayoutId() {
        return R.layout.club_create_layout;
    }

    @Override
    protected void main() {
        setTitle("创建俱乐部");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.club_add_img, R.id.club_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.club_add_img:
                CreateClubActivityPermissionsDispatcher.addThumbDataWithPermissionCheck(this);
                break;
            case R.id.club_submit:
                if (DataSafeUtils.isEmpty(nameEdit.getText().toString())) {
                    ToastUtil.show("资料不全");
                    return;
                }

                if (DataSafeUtils.isEmpty(mImgs)) {
                    ToastUtil.show("资料不全");
                    return;
                }
                SubmitHttpData();
                break;
        }
    }

    /**
     * 创建俱乐部
     */
    private void SubmitHttpData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", AppConfig.getInstance().getUid());
        map.put("token", AppConfig.getInstance().getToken());
        map.put("name", nameEdit.getText().toString());
        map.put("phone", phoneEdit.getText().toString() + "");
        map.put("info", summaryEdit.getText().toString());
        Map<String, List<String>> filepath = new HashMap<>();

        Map<String, String> singleFilePath = new HashMap<>();
        singleFilePath.put("file", mImgs);

        String mUrls = AppConfig.HOST + "/api/public/?service=";
        HttpUtils.POST_WHITH_UPLOAD(mUrls + "Club.addClub", map, singleFilePath, filepath, true, BaseVO.class, new Callback<BaseVO>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSucceed(String json, String code, BaseVO model) {
                if (!DataSafeUtils.isEmpty(model)) {

                    if (model.getRet() == 200) {
                        ToastUtil.show( "添加成功");
                        EventBus.getDefault().post(new EventBusModel("club_refresh"));
                        CreateClubActivity.this.finish();
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
                Log.v("tags", "---错误---");
            }

            @Override
            public void onFinish() {
                Log.v("tags", "---结束---");
            }
        });

    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void addThumbData() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), SDCARD_CACHE);
        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(TextUtils.isEmpty(SDCARD_CACHE) ? null : takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                .maxChooseCount(1) // 图片选择张数的最大值
                .selectedPhotos(null) // 当前已选中的图片路径集合
                .pauseOnScroll(false)
                // 滚动列表时是否暂停加载图片
                .build();
        startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            //是否单选，单选走true 语句，多选走false语句，这么默认false
            mPicList = BGAPhotoPickerActivity.getSelectedPhotos(data);

        } else if (requestCode == RC_PHOTO_PREVIEW) {
            // 在预览界面按返回也会回传预览界面已选择的图片集合
            mPicList = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data);
        }
        if (!DataSafeUtils.isEmpty(mPicList) && mPicList.size() > 0) {
            Log.v("tags", mPicList.get(0));
            mImgs = mPicList.get(0);
            Glide.with(mContext).load(mPicList.get(0)).into(clubAddImg);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CreateClubActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
