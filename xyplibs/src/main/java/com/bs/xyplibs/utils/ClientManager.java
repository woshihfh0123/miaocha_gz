package com.bs.xyplibs.utils;

import com.bs.xyplibs.base.BaseApp;
import com.inuker.bluetooth.library.BluetoothClient;

/**
 * Created by dingjikerbo on 2016/8/27.
 */
public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(BaseApp.getInstance());
                }
            }
        }
        return mClient;
    }
}
