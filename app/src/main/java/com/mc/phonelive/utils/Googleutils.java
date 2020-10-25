package com.mc.phonelive.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.mc.phonelive.AppContext;
import com.mc.phonelive.R;

public class Googleutils {

    /**
     * APP(内/外)打开地图
     */
    public static void openGoogleMap(Context context, String title, String mLon, String mLat) {
//如果已安装,
        Log.i("tags", mLon + "," + mLat);
        try {
            Uri mUri = Uri.parse("geo:" + mLat + "," + mLon + "?q=" + title);
            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            context.startActivity(mIntent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.show(AppContext.sInstance.getResources().getString(R.string.install_map));

        }
    }
}
