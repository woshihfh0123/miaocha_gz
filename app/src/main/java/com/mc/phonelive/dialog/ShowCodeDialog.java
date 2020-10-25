package com.mc.phonelive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ImageView;

import com.mc.phonelive.R;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.DataSafeUtils;

/**
 * 显示信息的dialog
 *
 * @author wwl
 */
public class ShowCodeDialog extends Dialog {

    private LayoutInflater factory;
    private Context mContext;
    private ImageView code_img;
    private String mImgs;

    public ShowCodeDialog(Context context, String imgsSr) {
        super(context);
        this.mImgs = imgsSr;
        this.mContext = context;
        factory = LayoutInflater.from(context);
    }

    public ShowCodeDialog(Context context, String imgsSr, int theme) {
        super(context, theme);
        this.mImgs = imgsSr;
        this.mContext = context;
        factory = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题
        this.setContentView(factory.inflate(R.layout.show_code_dialog, null));
        code_img = (ImageView) this.findViewById(R.id.code_img);
        if (!DataSafeUtils.isEmpty(mImgs)) {
            ImgLoader.display(mImgs, code_img, R.drawable.default_icon);
        }
    }

}
