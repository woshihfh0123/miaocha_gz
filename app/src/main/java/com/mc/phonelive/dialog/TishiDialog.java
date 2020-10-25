package com.mc.phonelive.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.shop.ZbServiceActivity;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.mc.phonelive.utils.GlideUtils;


public class TishiDialog extends Dialog {


    private Context context;
    private String url;
    private String pic;

    public TishiDialog(@NonNull Context context, String url, String pic) {
        super(context, R.style.custom_options_dialog);
        this.context=context;
        this.url=url;
        this.pic=pic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ts_pop);

        initDialoParameter();
        initView();
    }
    private void initView() {
        ImageView iv=findViewById(R.id.iv);
        TextView tv_content=findViewById(R.id.tv_content);
        TextView tv_submit=findViewById(R.id.tv_submit);
//        tv_content.setText(url);
        GlideUtils.loadImage(context,pic,iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusUtil.postEvent(new EventBean("close_act_ps_zb_view"));
                Intent intent=new Intent(context,ZbServiceActivity.class);
                intent.putExtra("gozb","1");
                context.startActivity(intent);
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
