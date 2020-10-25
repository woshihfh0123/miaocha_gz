package com.tencent.qcloud.ugckit.module.effect.bgm;

import android.support.annotation.Nullable;

public class TCMusicInfo {
    public String name;
    public String url;
    public int    status = STATE_UNDOWNLOAD;
    public int    progress;
    @Nullable
    public String localPath;

    private String title;
    private String file_url;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(@Nullable String localPath) {
        this.localPath = localPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public static final int STATE_UNDOWNLOAD = 1;
    public static final int STATE_DOWNLOADING = 2;
    public static final int STATE_DOWNLOADED = 3;
    public static final int STATE_USED = 4;
}
