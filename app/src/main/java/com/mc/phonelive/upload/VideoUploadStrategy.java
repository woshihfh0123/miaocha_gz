package com.mc.phonelive.upload;

/**
 * Created by cxf on 2018/5/21.
 */

public interface VideoUploadStrategy {
    void upload(VideoUploadBean bean, VideoUploadCallback callback);

    void cancel();
}
