package com.bs.xyplibs.dialog;

import android.app.Dialog;
import android.content.Context;

public class DownloadDialog extends Dialog {

    private int res;

    public DownloadDialog(Context context, int theme, int res) {
        super(context, theme);
        // TODO 自动生成的构造函数存根
        setContentView(res);
        this.res = res;
        setCanceledOnTouchOutside(false);
    }
}
