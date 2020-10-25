package com.mc.phonelive.constant;

import android.os.Environment;

import java.io.File;

public class Constant {
    /**
     * 选择文件
     */
    public static final String SDCARD_CACHE = "phonelive/files/"; // 文件sdk缓存
    public static final String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "phonelive"
            + File.separator;
    //get 最后加 &

}
