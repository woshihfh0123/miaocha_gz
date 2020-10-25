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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.xyplibs.R;
import com.bs.xyplibs.utils.DateUtils;
import com.bs.xyplibs.utils.DisplayUtils;
import com.bs.xyplibs.utils.Utils;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class LoginJlDialog extends Dialog {


    private Context context;
    private Button bt_left,bt_right;
    private String ewmCode;
    private String shopName;
    private String title;
    private String time;

    public LoginJlDialog(@NonNull Context context, String ewmCode,String shopName,String title,String time) {
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
        setContentView(R.layout.alertext_login_jl);

        initDialoParameter();
        initView();
    }
    private void initView() {
        Button bt_submit=findViewById(R.id.bt_submit);
        ImageView iv_cancel=findViewById(R.id.iv_cancel);
        TextView tv_shop_name=findViewById(R.id.tv_shop_name);
        TextView tv_score=findViewById(R.id.tv_score);
        TextView tv_time=findViewById(R.id.tv_time);
        ImageView iv=findViewById(R.id.iv);
        tv_shop_name.setText(shopName);
        tv_score.setText(title);
        tv_time.setText("购买时间："+ DateUtils.getYMDHMS(DateUtils.serviceText2Date(time)));
//        int width = (DisplayUtils.getScreenWidth(context) - DensityUtil.dp2px(70));
//        int heigh=width*428/875;
//        iv.setLayoutParams(new LinearLayout.LayoutParams(width, heigh));
        if(Utils.isNotEmpty(ewmCode)){
            iv.setVisibility(View.VISIBLE);
           Bitmap bitmap= CodeUtils.createImage(ewmCode,200,200,null);
            iv.setImageBitmap(bitmap);
        }else{
            iv.setVisibility(View.GONE);
        }

iv_cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dismiss();
    }
});
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dismiss();
            }
        });
//        bt_left=findViewById(R.id.bt_left);
//        bt_right=findViewById(R.id.bt_right);
//        bt_left.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                GetCoupon();
////                Toast.makeText(context,"ldd",Toast.LENGTH_SHORT).show();
////                EventBus.getDefault().post(new EventBusModel("up_video_from_yhqDialog"));
//                dismiss();
//            }
//        });
//        bt_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(context,"dsdsf",Toast.LENGTH_SHORT).show();
//                EventBus.getDefault().post(new EventBusModel("go_person_from_yhqDialog"));
//                dismiss();
//            }
//        });

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
