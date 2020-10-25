package com.mc.phonelive.activity.shop;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.SecondActivity;
import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.mob.MobCallback;
import com.mc.phonelive.mob.MobShareUtil;
import com.mc.phonelive.mob.ShareData;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.utils.SpUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 *我的名片
 * 新888888
 *
 */

public class MyCardActivity extends AbsActivity implements EasyPermissions.PermissionCallbacks{
private ImageView iv_ewm;
private TextView tv_name,tv_title,tv_info;
private LinearLayout ll_sys;
private LinearLayout ll_save;
private RelativeLayout rl_save;
private ImageView btn_share;
private ImageView miv;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_card;
    }
    @Override
    protected void main() {
        setTitle("我的名片");
        mMobShareUtil = new MobShareUtil();
        iv_ewm=findViewById(R.id.iv_ewm);
        miv=findViewById(R.id.miv);
        tv_name=findViewById(R.id.tv_name);
        tv_title = findViewById(R.id.tv_title);
        tv_info = findViewById(R.id.tv_info);
        btn_share=findViewById(R.id.btn_share);
        ll_sys=findViewById(R.id.ll_sys);
        ll_save=findViewById(R.id.ll_save);
        rl_save=findViewById(R.id.rl_save);
        tv_info.setText(""+AppConfig.getInstance().getUserBean().getSignature());
        tv_title.setText("使用渺茶科技扫码，关注@"+AppConfig.getInstance().getUserBean().getUserNiceName());
        tv_name.setText(AppConfig.getInstance().getUserBean().getUserNiceName());
        getList();
        if(Utils.isNotEmpty(AppConfig.getInstance().getUserBean().getAvatar())){
            miv.setVisibility(View.VISIBLE);
            GlideUtils.loadImage(mContext,AppConfig.getInstance().getUserBean().getAvatar(),miv);
        }else{
            miv.setVisibility(View.GONE);
        }
        ll_sys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SecondActivity.class);
                startActivityForResult(intent, 112);
            }
        });
        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionRequest();

            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNotEmpty(SpUtil.getInstance().getStringValue("ewmPath"))){
                    share();
                }else{
                    ToastUtil.show("先保存相册再分享");
                }
            }
        });
    }
    @AfterPermissionGranted(1323)
    public  void PermissionRequest(){
        String[] perms = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 已经申请过权限，做想做的事
            Utils.viewSaveToImage(rl_save,mContext);
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
    private void share() {
//        if (mVideoBean == null) {
//            return;
//        }
//        ToastUtil.show("分享");
//        CardShareDialogFragment fragment = new CardShareDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(Constants.VIDEO_BEAN, null);
//        fragment.setArguments(bundle);
//        fragment.show(getSupportFragmentManager(), "CardShareDialogFragment");
//        LiveShareDialogFragment fragment = new LiveShareDialogFragment();
//        fragment.setActionListener(this);
//        fragment.show(((AbsActivity) mContext).getSupportFragmentManager(), "LiveShareDialogFragment");
        String url= SpUtil.getInstance().getStringValue("ewmPath");
        sharePic("",url);
    }
    private void sharePic(String platform,String url){

        OnekeyShare oks = new OnekeyShare();
//        oks.setPlatform(platform);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.addHiddenPlatform(QZone.NAME);
        oks.addHiddenPlatform(QQ.NAME);
//        oks.addHiddenPlatform(WechatFavorite.NAME);
        oks.addHiddenPlatform(Facebook.NAME);
        oks.addHiddenPlatform(Twitter.NAME);
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("渺茶");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl(url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(url);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                    paramsToShare.setImagePath(url);
                } else if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                    paramsToShare.setImagePath(url);
                }

            }
        });
//        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("test");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

// 启动分享GUI
        oks.show(mContext);
    }
    private MobShareUtil mMobShareUtil;
    /**
     * 分享页面链接
     */
    public void shareVideoPage(String type, String path) {
        ShareData data = new ShareData();
        data.setTitle("ttttt");
        data.setDes("ssssss");
        data.setImg(path);
//        data.setImgUrl(videoBean.getThumbs());
//        String webUrl = HtmlConfig.SHARE_VIDEO + videoBean.getId();
//        data.setWebUrl("www.baidu.com");
        if (mMobShareUtil == null) {
            mMobShareUtil = new MobShareUtil();
        }
        mMobShareUtil.executeImg(type, data, mMobCallback);
    }

    private MobCallback mMobCallback = new MobCallback() {

        @Override
        public void onSuccess(Object data) {

        }

        @Override
        public void onError() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onFinish() {

        }
    };
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
