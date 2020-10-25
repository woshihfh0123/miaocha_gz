package com.picture.android.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

import static android.os.Environment.DIRECTORY_DCIM;

/**
 * created by WWL on 2019/5/31 0031:16
 */
public class CommentUtil {

    public static File getAppRootPath(Context context) {
        if (sdCardIsAvailable()) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM);
        } else {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String path = GetPathFromUri.getPath(context,uri);
            File file = new File(path);
            return file;
        }
    }

    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().canWrite();
        } else
            return false;
    }

}
