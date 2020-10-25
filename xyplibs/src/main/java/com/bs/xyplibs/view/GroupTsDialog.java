package com.bs.xyplibs.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bs.xyplibs.R;
import com.bs.xyplibs.bean.EventBean;
import com.bs.xyplibs.utils.EventBusUtil;
import com.bs.xyplibs.utils.Utils;


/**
 * 单按钮提示框，携带文字
 */
public class GroupTsDialog extends Dialog {


    private Context context;
    private Button bt_left,bt_right;
    private String info;
    private boolean isClick=false;
    private boolean isLeft=false;
    private String buttonName="我知道了";
    private String sendMsg="click_from_click_dialog";
    private TextView tv_title;
    private String title="";

    public GroupTsDialog(@NonNull Context context, String info) {
        super(context, R.style.custom_options_dialog);
        this.context=context;
        this.info=info;
    }
    public GroupTsDialog(@NonNull Context context, String info, boolean isClick) {
        super(context, R.style.custom_options_dialog);
        this.context=context;
        this.info=info;
        this.isClick=isClick;
    }
    public GroupTsDialog(@NonNull Context context, String buttonName, String info, boolean isClick) {
        super(context, R.style.custom_options_dialog);
        this.context=context;
        this.info=info;
        this.buttonName=buttonName;
        this.isClick=isClick;
    }
    public GroupTsDialog(@NonNull Context context, String stitle, String buttonName, String info, boolean isClick) {
        super(context, R.style.custom_options_dialog);
        this.context=context;
        this.title=stitle;
        this.info=info;
        this.buttonName=buttonName;
        this.isClick=isClick;
    }
    public GroupTsDialog(@NonNull Context context, String stitle, String buttonName, String info, boolean isClick,boolean isLeft) {
        super(context, R.style.custom_options_dialog);
        this.context=context;
        this.title=stitle;
        this.info=info;
        this.buttonName=buttonName;
        this.isClick=isClick;
        this.isLeft=isLeft;
    }
    public GroupTsDialog(@NonNull Context context, String buttonName, String info, boolean isClick, String eventMsg) {
        super(context, R.style.custom_options_dialog);
        this.context=context;
        this.info=info;
        this.buttonName=buttonName;
        this.isClick=isClick;
        this.sendMsg=eventMsg;
    }
    public GroupTsDialog(@NonNull Context context, String stitle, String buttonName, String info, boolean isClick, String eventMsg) {
        super(context, R.style.custom_options_dialog);
        this.context=context;
        this.title=stitle;
        this.info=info;
        this.buttonName=buttonName;
        this.isClick=isClick;
        this.sendMsg=eventMsg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_ts_dialog);
        setCanceledOnTouchOutside(true);
        initDialoParameter();
        initView();

    }
    private void initView() {
        Button bt_submit=findViewById(R.id.bt_submit);
        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_info = findViewById(R.id.tv_info);
        if(Utils.isNotEmpty(title)){
            tv_title.setText(title);
            tv_title.setVisibility(View.VISIBLE);
        }else{
            tv_title.setVisibility(View.GONE);
        }
        if(isLeft){
            tv_info.setGravity(Gravity.CENTER_VERTICAL);
        }
        tv_info.setText(info);
        bt_submit.setText(buttonName);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClick){
                    if(Utils.isNotEmpty(sendMsg)){
                        EventBusUtil.postEvent(new EventBean(sendMsg));
                    }
                }
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
        dialogWindow.setWindowAnimations(R.style.custom_options_dialog);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        lp.x = 0; // 新位置X坐标
//        lp.y = -20; // 新位置Y坐标
//        lp.width= (int) CommentUtil.dpToPx(280);
//        lp.height =(int) CommentUtil.dpToPx(200);
//        lp.alpha = 9f; // 透明度
//        dialogWindow.setAttributes(lp);

    }


}
