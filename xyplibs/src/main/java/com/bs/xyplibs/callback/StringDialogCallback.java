package com.bs.xyplibs.callback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.bs.xyplibs.R;
import com.bs.xyplibs.alertview.AlertView;
import com.bs.xyplibs.alertview.OnItemClickListener;
import com.bs.xyplibs.base.BaseApp;
import com.bs.xyplibs.base.BaseVO;
import com.bs.xyplibs.bean.EventBean;
import com.bs.xyplibs.utils.EventBusUtil;
import com.bs.xyplibs.utils.NetUtils;
import com.bs.xyplibs.utils.Utils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/12/21.
 */
public abstract class StringDialogCallback extends StringCallback {
    private ProgressDialog dialog;
    private Context mContext;
    public AlertDialog.Builder builder;
    private boolean mIsDialogShow = true;
    public StringDialogCallback(Activity activity, boolean isDialogShow) {
        mContext=activity;
        this.mIsDialogShow=isDialogShow;
        builder = new AlertDialog.Builder(activity);
//        dialog = new ProgressDialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage("加载中...");
    }

    private void initDialogShow() {
        initViews();
        this.dialog.show();
        this.dialog.setContentView(R.layout.progress_bar_dialog);
    }

    private void initViews() {
        this.dialog=new ProgressDialog(this.mContext,R.style.Dialog);
        this.dialog.setCancelable(false);
        this.dialog.setCanceledOnTouchOutside(false);
    }
    protected void dismissprogressDialog(){
        if(dialog!=null&&this.dialog.isShowing()){
            this.dialog.dismiss();
        }
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        boolean isnet= NetUtils.isNetworkAvailable(BaseApp.getInstance());
        if(isnet){
            if(mIsDialogShow){

                initDialogShow();
            }
        }else{
            Toast.makeText(mContext,"当前网络不可用",Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onFinish() {
        if (this.mIsDialogShow) {
           dismissprogressDialog();
        }

    }
    public abstract void Success(String response);
    public void IsNoData(){};
    public void IsErrorData(String desc){
        if(Utils.isNotEmpty(desc)){
            Toast.makeText(mContext,desc,Toast.LENGTH_SHORT).show();
        }

    };
    @Override
    public void onSuccess(final Response<String> response) {
        Logger.e("返回结果："+response.code()+"="+response.body());
        if(response.code()==200){
            if(response.body().contains("code")){
                final BaseVO baseVO= com.alibaba.fastjson.JSONObject.parseObject(response.body(),BaseVO.class);
                if(Utils.isNotEmpty(baseVO)){
                    if(baseVO.code.equals("0000")){
                            Success(response.body());
                    }else if(baseVO.code.equals("201")){
                        if(Utils.isNotEmpty(baseVO.getDesc())){
                            Toast.makeText(mContext,baseVO.getDesc(),Toast.LENGTH_LONG).show();
                        }
                        Success(response.body());
                    }else if(baseVO.code.equals("202")){
                        if(Utils.isNotEmpty(baseVO.getDesc())){
                            new AlertView(baseVO.getDesc(), null, null, new String[]{"确定"}, null, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    Success(response.body());
                                }
                            }).show();
                        }
                    }else if(baseVO.code.equals("300")){//暂无更多数据
                        IsNoData();
                    }else if(baseVO.code.equals("400")){
                        IsErrorData(baseVO.getDesc());
                    }else if(baseVO.code.equals("888888")){
                        EventBusUtil.postEvent(new EventBean("go_get_vip_from_server_code"));
                    }else if(baseVO.code.equals("777777")){
                        EventBusUtil.postEvent(new EventBean("go_sm_rz_server_code"));
                    }else if(baseVO.code.equals("99999")){
                        EventBusUtil.postEvent(new EventBean("go_login_from_server_code"));
                    }else{
                        if(Utils.isNotEmpty(baseVO.getMessage())){
//                            Toast.makeText(mContext,baseVO.getMessage(),Toast.LENGTH_SHORT).show();
                            IsErrorData(baseVO.getMessage());
                        }
                    }
                }
            }
        }else{
            Toast.makeText(mContext,response.code()+"--"+response.body(),Toast.LENGTH_SHORT).show();
        }
    }
}
