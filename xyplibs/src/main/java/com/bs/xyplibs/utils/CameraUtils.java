package com.bs.xyplibs.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Administrator on 2018/11/29.
 */

public class CameraUtils {
    public static final int CAMERA_REQ_CODE = 0x0011;

    /**
     * 打开相机
     *
     * @param activity
     * @param path
     */
    public void openCamera(Activity activity, String path) {
        openCamera(activity, path, "IMG_" + System.currentTimeMillis() + ".jpg");
    }

    /**
     * 打开相机
     *
     * @param activity
     * @param path
     * @param fileName
     */
    public void openCamera(Activity activity, String path, String fileName) {
//        FileUtils.makeDirs(path);
//        File cameraFile = new File(path, fileName);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
//        activity.startActivityForResult(intent, CAMERA_REQ_CODE);
    }
}
