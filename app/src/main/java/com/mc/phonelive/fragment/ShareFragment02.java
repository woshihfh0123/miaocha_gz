package com.mc.phonelive.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.ShareBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 任务大厅
 */
@RuntimePermissions
public class ShareFragment02 extends BaseFragment {

    View layoutView;
    LinearLayout ll_main_share;
    ImageView shareErweima;
    LinearLayout saveLayout;

    public static ShareFragment02 newInstance() {
        ShareFragment02 fragment = new ShareFragment02();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.share_fragment02, container, false);
        return layoutView;
    }


    @Override
    protected void initView(View view) {
        ll_main_share =view.findViewById(R.id.share_layout);
         shareErweima=view.findViewById(R.id.share_erweima);
         saveLayout=view.findViewById(R.id.save_layout);

    }

    @Override
    protected void initHttpData() {
        HttpUtil.getTeamInvite(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<ShareBean.InfoBean> data = JSON.parseArray(Arrays.toString(info),ShareBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(data) && data.size()>0){
                    if (!DataSafeUtils.isEmpty(data.get(0).getInvite_ewm())){
                        Log.v("tags",data.get(0).getInvite_ewm()+"---ewm");
                        ImgLoader.display(data.get(0).getInvite_ewm(),shareErweima);
                    }
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.save_layout)
    public void onViewClicked() {
        ShareFragment02PermissionsDispatcher.getphotourlWithPermissionCheck(this);
    }


    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void getphotourl() {

        String path = CommentUtil.screenShotWholeScreen(ll_main_share);
        if (!TextUtils.isEmpty(path)) {
//            Glide.with(getActivity()).load(path).into(iv_screen);
            ToastUtil.show("截图成功，以保存至:" + path);
            //发广播告诉相册有图片需要更新，这样可以在图册下看到保存的图片了
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(path));
            intent.setData(uri);
            getActivity().sendBroadcast(intent);
//            getShareNumber();
        } else {
            ToastUtil.show("截图失败");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ShareFragment02PermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
