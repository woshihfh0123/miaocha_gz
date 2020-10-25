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
import android.widget.TextView;

import com.bs.xyplibs.R;
import com.bs.xyplibs.bean.EventBean;
import com.bs.xyplibs.utils.DateUtils;
import com.bs.xyplibs.utils.EventBusUtil;
import com.bs.xyplibs.utils.Utils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

public class UpDialog extends Dialog {


    private Context context;
    private Button bt_left,bt_right;
    private String ewmCode;
    private String shopName;
    private String title;
    private String time;

    public UpDialog(@NonNull Context context, String ewmCode, String shopName, String title, String time) {
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
        setContentView(R.layout.alertext_up);

        initDialoParameter();
        initView();
    }

    @Override
    public void onBackPressed() {
    }

    private void initView() {
        TextView tv_submit=findViewById(R.id.tv_submit);
        TextView tv_cancle=findViewById(R.id.tv_cancle);
        TextView tv_info=findViewById(R.id.tv_info);
        setCanceledOnTouchOutside(false);
        tv_info.setText(shopName);
        if(Utils.isNotEmpty(ewmCode)){
            if(ewmCode.equals("1")){
                tv_cancle.setTextColor(context.getResources().getColor(R.color.colorGray));
            }else{
                tv_cancle.setTextColor(context.getResources().getColor(R.color.colorBlack));
                tv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        }
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                EventBusUtil.postEvent(new EventBean("up_version_from_check"));
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
