package com.mc.phonelive.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mc.phonelive.R;


public class AvZhiBoDialog extends Dialog {


    private Context context;
    private Button bt_left,bt_right;
    private String url;
    private String pic;
    private WebView webView;
    private LinearLayout ll_all;
    private ImageView iv_pic;

    public AvZhiBoDialog(@NonNull Context context, String url, String pic) {
        super(context, R.style.custom_options_dialog);
        this.context=context;
        this.url=url;
        this.pic=pic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zb_pop_dd);

        initDialoParameter();
        initView();

    }
    private void initView() {
        LinearLayout ll_all=findViewById(R.id.ll_all);
        webView =findViewById(R.id.webView);
        iv_pic =findViewById(R.id.iv_pic);
        webView.loadUrl(url);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return false;
            }
        });
        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }




    @Override
    public void show() {
        super.show();
    }




    /**
     * 初始化弹窗参数
     */
    private void initDialoParameter() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent); //设置对话框背景透明 ，对于AlertDialog 就不管用了
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        lp.x = 0; // 新位置X坐标
//        lp.y = -20; // 新位置Y坐标
//        lp.width= (int) CommentUtil.dpToPx(280);
//        lp.height =(int) CommentUtil.dpToPx(200);
//        lp.alpha = 9f; // 透明度
//        dialogWindow.setAttributes(lp);

    }


}
