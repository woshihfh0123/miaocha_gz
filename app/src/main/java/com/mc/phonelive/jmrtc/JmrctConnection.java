package com.mc.phonelive.jmrtc;

import android.view.SurfaceView;

import cn.jiguang.jmrtc.api.JMRtcClient;
import cn.jiguang.jmrtc.api.JMRtcSession;
import cn.jpush.im.android.api.model.UserInfo;


public interface JmrctConnection {
    void onConnected(JMRtcSession jmRtcSession, SurfaceView localSurfaceView);
    void onMemberJoin(UserInfo userInfo, SurfaceView surfaceView);
    void onMemberOffline(UserInfo userInfo, JMRtcClient.DisconnectReason reason);
    void onDisconnected(JMRtcClient.DisconnectReason disconnectReason);
}
