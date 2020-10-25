package com.bs.xyplibs.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.xyplibs.R;
import com.bs.xyplibs.bean.EventBean;
import com.bs.xyplibs.utils.DateUtils;
import com.bs.xyplibs.utils.DisplayUtils;
import com.bs.xyplibs.utils.EventBusUtil;
import com.bs.xyplibs.utils.Utils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class GetVidDialog extends Dialog {


    private Context context;
    private Button bt_left,bt_right;
    private String ewmCode;
    private String shopName;
    private String title;
    private String time;

    public GetVidDialog(@NonNull Context context) {
        super(context, R.style.custom_options_dialog);
        this.context=context;
        this.ewmCode=ewmCode;
        this.shopName=shopName;
        this.title=title;
        this.time=time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_vip_pop);

        initDialoParameter();
        initView();
    }
    private void initView() {
        Button bt_submit=findViewById(R.id.bt_submit);
        ImageView iv_cancel=findViewById(R.id.iv_cancel);

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusUtil.postEvent(new EventBean("ling_qu_vip_from_home"));
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
