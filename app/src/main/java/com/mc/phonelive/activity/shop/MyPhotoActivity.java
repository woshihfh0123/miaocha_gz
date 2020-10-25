package com.mc.phonelive.activity.shop;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.utils.ToastUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 *我的名片
 * 新888888
 *
 */

public class MyPhotoActivity extends AbsActivity implements EasyPermissions.PermissionCallbacks{
private ImageView iv_ewm;
private String pic="";
private TextView tv_save;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_photo;
    }
    @Override
    protected void main() {
        pic=getIntent().getStringExtra("pic");
        iv_ewm=findViewById(R.id.iv_ewm);
        tv_save=findViewById(R.id.tv_save);
        iv_ewm.setImageURI(Uri.fromFile(new File(pic)));
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        iv_ewm.setImageURI(Uri.fromFile(new File("/sdcard/DCIM/huyin/video/pic_11628_20200706_104256_8059051.jpg")));
    }
    @AfterPermissionGranted(1323)
    public  void PermissionRequest(){
        String[] perms = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 已经申请过权限，做想做的事
//            Utils.viewSaveToImage(rl_save,mContext);
        } else {
            // 没有申请过权限，现在去申请
//            EasyPermissions.requestPermissions(this, "系统访问受限，点击开启相关权限",1323, perms);
            ActivityCompat.requestPermissions(this, perms,
                    1323);
        }

    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 112) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    getInfo(result);
//                    ToastUtil.show(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtil.show("无法识别");
                }
            }
        }
    }

    private void getInfo(String result) {
        HttpUtil.getUserHome(result, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    UserHomeActivity.forward(mContext, result);
                        finish();
                }else{
                    ToastUtil.show("无法识别："+result);
                }
            }
        });
    }

    private void release(){
//        if(mVideoHomeViewHolder!=null){
//            mVideoHomeViewHolder.release();
//        }
//        mVideoHomeViewHolder=null;
    }

    private void getList() {
        HttpUtil.getCard(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String invite_code=obj.getString("invite_code");
                    String invite_url=obj.getString("invite_url");
                    String invite_ewm=obj.getString("invite_ewm");
                    if(Utils.isNotEmpty(invite_ewm)){
                        GlideUtils.loadImage(mContext,invite_ewm,iv_ewm);
                    }
//                    ArrayList<FamilyLeftBean> nowsBean = (ArrayList<FamilyLeftBean>) JSON.parseArray(obj.getString("list"), FamilyLeftBean.class);
//                    if(Utils.isNotEmpty(nowsBean)){
//                        if(page==1){
//                            mAdapter.setNewData(nowsBean);
//                        }else{
//                            mAdapter.addData(nowsBean);
//                        }
//                    }else{
//                        mRefreshLayout.setEnableLoadMore(false);
//
//                    }

                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        release();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }
}
