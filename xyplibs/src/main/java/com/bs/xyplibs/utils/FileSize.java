package com.bs.xyplibs.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/11/29.
 */

public class FileSize {

    public static final long SIZE_BT = 1024L;

    public static final long SIZE_KB = SIZE_BT * 1024L;

    public static final long SIZE_MB = SIZE_KB * 1024L;

    public static final long SIZE_GB = SIZE_MB * 1024L;

    public static final long SIZE_TB = SIZE_GB * 1024L;

    public static final int SACLE = 2;

    private File file;

    private long longSize;

    public FileSize(File file) {
        this.file = file;
    }

    private void getFileSize() throws RuntimeException, IOException {
        getFileSize(file);
    }

    private void getFileSize(File file) {

        if (file == null || !file.exists()) {
            return;
        }

        if (file.isFile()) {
            this.longSize += file.length();
            return;
        }

        File[] childArray = file.listFiles();
        if (childArray == null || childArray.length == 0) {
            return;
        }

        for (File child : childArray) {
            getFileSize(child);
        }

    }

    public String toString() throws RuntimeException {
        try {
            try {
                getFileSize();
            } catch (RuntimeException e) {
                return "";
            }

            return convertSizeToString(this.longSize);

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 格式化输出文件大小
     * @param fileSize
     * @return
     */
    public static String convertSizeToString(long fileSize) {
        if (fileSize >= 0 && fileSize < SIZE_BT) {
            return fileSize + "B";
        } else if (fileSize >= SIZE_BT && fileSize < SIZE_KB) {
            return fileSize / SIZE_BT + "KB";
        } else if (fileSize >= SIZE_KB && fileSize < SIZE_MB) {
            return fileSize / SIZE_KB + "MB";
        } else if (fileSize >= SIZE_MB && fileSize < SIZE_GB) {
            BigDecimal longs = new BigDecimal(Double.valueOf(fileSize + "").toString());
            BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_MB + "").toString());
            String result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString();
            // double result=this.longSize/(double)SIZE_MB;
            return result + "GB";
        } else {
            BigDecimal longs = new BigDecimal(Double.valueOf(fileSize + "").toString());
            BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_GB + "").toString());
            String result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString();
            return result + "TB";
        }
    }

    /**
     * MB转KB
     * @param fileSize
     * @return
     */
    public static double getKBSize(String fileSize) {
        if (TextUtils.isEmpty(fileSize)) {
            return 0;
        }
        fileSize = fileSize.toUpperCase();
        int index;
        if ((index = fileSize.indexOf("MB")) > 0) {
            String str = fileSize.substring(0, index).trim();
            return string2double(str) * 1024;
        }
        if ((index = fileSize.indexOf("KB")) > 0) {
            String str = fileSize.substring(0, index).trim();
            return string2double(str);
        }
        return 0;
    }

    public static double string2double(String doubleStr) {
        try {
            return Double.valueOf(doubleStr);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     *  获取文件的大小单位是byte
     * @return 返回文件的大小
     * @throws RuntimeException
     */
    public long getLongSize() throws RuntimeException {
        try {

            getFileSize();
            return longSize;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

}
