package com.mc.phonelive.tecent.common;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.ZbAndPsActivity;
import com.mc.phonelive.tecent.videochoose.TCPicturePickerActivity;
import com.mc.phonelive.tecent.videochoose.TCVideoPickerActivity;
import com.mc.phonelive.tecent.videorecord.TCVideoFollowRecordActivity;
import com.mc.phonelive.tecent.videorecord.TCVideoRecordActivity;
import com.mc.phonelive.tecent.videorecord.TCVideoTripleScreenActivity;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.mc.phonelive.utils.SpUtil;
import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.qcloud.ugckit.UGCKitConstants;
import com.tencent.qcloud.ugckit.utils.BackgroundTasks;
import com.tencent.qcloud.ugckit.utils.DownloadUtil;
import com.tencent.qcloud.ugckit.utils.ToastUtil;


import java.io.File;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * 短视频选择界面
 */
public class ShortVideoDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "ShortVideoDialog";
    private RelativeLayout mTVVideo;
    private ImageView mIVClose;
    private RelativeLayout mTVEditer;
    private RelativeLayout mTVPicture;
    private RelativeLayout mTVChorus;
    private RelativeLayout mTVTriple;
    private RelativeLayout rl_zb_picture;

    private ProgressDialog mDownloadProgressDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_short_video);
        dialog.setCanceledOnTouchOutside(true);

        mTVVideo = (RelativeLayout) dialog.findViewById(R.id.tv_record);
        mTVEditer = (RelativeLayout) dialog.findViewById(R.id.tv_editer);
        mTVPicture = (RelativeLayout) dialog.findViewById(R.id.tv_picture);
        mTVChorus = (RelativeLayout) dialog.findViewById(R.id.tv_chorus);
        mIVClose = (ImageView) dialog.findViewById(R.id.iv_close);
        mTVTriple = (RelativeLayout) dialog.findViewById(R.id.tv_triple_chorus);
        rl_zb_picture = dialog.findViewById(R.id.rl_zb_picture);

        mTVVideo.setOnClickListener(this);
        mTVEditer.setOnClickListener(this);
        mTVPicture.setOnClickListener(this);
        mTVChorus.setOnClickListener(this);
        mIVClose.setOnClickListener(this);
        mTVTriple.setOnClickListener(this);
        rl_zb_picture.setOnClickListener(this);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        mDownloadProgressDialog = new ProgressDialog(getActivity());
        mDownloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置进度条的形式为圆形转动的进度条
        mDownloadProgressDialog.setCancelable(false);                           // 设置是否可以通过点击Back键取消
        mDownloadProgressDialog.setCanceledOnTouchOutside(false);               // 设置在点击Dialog外是否取消Dialog进度条

        return dialog;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_zb_picture:
                dismissDialog();
                SpUtil.getInstance().setBooleanValue("setMe",false);
//                PermissionRequest();
                EventBusUtil.postEvent(new EventBean("tozb"));
                break;
            case R.id.tv_record:
                dismissDialog();
                startActivity(new Intent(getActivity(), TCVideoRecordActivity.class));
                break;
            case R.id.tv_chorus:
                dismissDialog();
                prepareToDownload(false);
                break;
            case R.id.tv_triple_chorus:
                dismissDialog();
                prepareToDownload(true);
                break;
            case R.id.tv_editer:
                dismissDialog();

                Intent intent = new Intent(getActivity(), TCVideoPickerActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_picture:
                dismissDialog();

                Intent intent2 = new Intent(getActivity(), TCPicturePickerActivity.class);
                startActivity(intent2);
                break;

            case R.id.iv_close:
                dismissDialog();
                break;

        }
    }

    private void prepareToDownload(final boolean isTriple) {
        if (getActivity() == null) {
            return;
        }
        File sdcardDir = getActivity().getExternalFilesDir(null);
        if (sdcardDir == null) {
            TXCLog.e(TAG, "prepareToDownload sdcardDir is null");
            return;
        }
        File downloadFileFolder = new File(sdcardDir, UGCKitConstants.OUTPUT_DIR_NAME);
        File downloadFile = new File(downloadFileFolder, DownloadUtil.getNameFromUrl(UGCKitConstants.CHORUS_URL));
        if (downloadFile.exists()) {
            mDownloadProgressDialog.dismiss();
            if (isTriple) {
                startTripleActivity(downloadFile.getAbsolutePath());
            } else {
                startRecordActivity(downloadFile.getAbsolutePath());
            }
            return;
        }
        if (mDownloadProgressDialog != null) {
            mDownloadProgressDialog.show();
        }
        DownloadUtil.get(getActivity()).download(UGCKitConstants.CHORUS_URL, UGCKitConstants.OUTPUT_DIR_NAME, new DownloadUtil.DownloadListener() {
            @Override
            public void onDownloadSuccess(final String path) {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadProgressDialog.dismiss();
                        if (isTriple) {
                            startTripleActivity(path);
                        } else {
                            startRecordActivity(path);
                        }
                    }
                });
            }

            @Override
            public void onDownloading(final int progress) {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadProgressDialog.setMessage("正在下载..." + progress + "%");
                    }
                });
            }

            @Override
            public void onDownloadFailed() {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadProgressDialog.dismiss();

                        ToastUtil.toastShortMessage("下载失败");
                    }
                });
            }
        });
    }

    private void startRecordActivity(String path) {
        Intent intent = new Intent(getActivity(), TCVideoFollowRecordActivity.class);
        intent.putExtra(UGCKitConstants.VIDEO_PATH, path);
        startActivity(intent);
    }

    private void startTripleActivity(String path) {
        Intent intent = new Intent(getActivity(), TCVideoTripleScreenActivity.class);//TCVideoPreviewActivity
        intent.putExtra(UGCKitConstants.VIDEO_PATH, path);
        startActivity(intent);

    }

    private void dismissDialog() {
        if (ShortVideoDialog.this.isAdded()) {
            ShortVideoDialog.this.dismiss();
        }
    }
}
