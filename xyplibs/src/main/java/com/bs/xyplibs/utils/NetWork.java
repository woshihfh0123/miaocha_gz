package com.bs.xyplibs.utils;

import android.app.Application;

import com.bs.xyplibs.base.BaseApp;
import com.hjq.toast.*;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2018/11/29.
 */

public class NetWork {
    public static void internal() {
        int networkType = NetUtils.getNetworkType(BaseApp.getInstance());
        String networkTypeName = NetUtils.getNetworkTypeName(BaseApp.getInstance());
        Logger.d("-----网络名字-----", networkTypeName);
        Logger.d("----网络类型-----", networkType + "");
        if (networkTypeName.equals(NetUtils.NETWORK_TYPE_WIFI)) {
            com.hjq.toast.ToastUtils.show("你目前处于wifi网络");
        } else if (networkTypeName.equals(NetUtils.NETWORK_TYPE_DISCONNECT)) {
            com.hjq.toast.ToastUtils.show("你目前处于断网状态");
        } else if (networkTypeName.equals(NetUtils.NETWORK_TYPE_3G)) {
            com.hjq.toast.ToastUtils.show("你目前处于3G状态");
        } else if (networkTypeName.equals(NetUtils.NETWORK_TYPE_2G)) {
            com.hjq.toast.ToastUtils.show("你目前处于2G网络");
        } else if (networkTypeName.equals(NetUtils.NETWORK_TYPE_WAP)) {
            com.hjq.toast.ToastUtils.show("你目前处于企业网");
        } else if (networkTypeName.equals(NetUtils.NETWORK_TYPE_UNKNOWN)) {
            com.hjq.toast.ToastUtils.show("你目前网络类型不知道");
        }
    }
}
