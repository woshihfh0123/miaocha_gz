package com.mc.phonelive.jmrtc;

import android.util.LongSparseArray;
import android.view.SurfaceView;

import cn.jiguang.jmrtc.api.JMRtcSession;

public class JMRTCUtils {
    private JMRtcSession session;//通话数据元信息对象
    LongSparseArray<SurfaceView> surfaceViewCache = new LongSparseArray<SurfaceView>();
    boolean requestPermissionSended = false;


}
