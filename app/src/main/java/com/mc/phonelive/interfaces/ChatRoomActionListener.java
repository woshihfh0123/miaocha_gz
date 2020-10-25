package com.mc.phonelive.interfaces;

import android.view.ViewGroup;

/**
 * Created by cxf on 2018/11/8.
 */

public interface ChatRoomActionListener {
    void onCloseClick();

    void onPopupWindowChanged(int height);

    /**
     * 点击选择图片
     */
    void onChooseImageClick();

    /**
     * 点击拍照
     */
    void onCameraClick();

    /**
     * 点击语音输入
     */
    void onVoiceInputClick();

    /**
     * 点击位置
     */
    void onLocationClick();

    /**
     * 点击语音
     */
    void onVoiceClick();

    /**
     * 获取视频
     * @return
     */
    void onVideoClick();

    /**
     * 点击音视频对话
     */
    void onVideoVoiceClick();
    /**
     * 红包
     * @return
     */
    void onRedPackClick();

    ViewGroup getImageParentView();

    /**
     * 语音通话
     */
    void onVideoAutoClick();

    //void onImageClick();
}
