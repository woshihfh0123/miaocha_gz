package com.bs.xyplibs.mycallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.Toast;

import com.bs.xyplibs.alertview.AlertView;
import com.bs.xyplibs.base.BaseApp;
import com.bs.xyplibs.base.BaseVO;
import com.bs.xyplibs.bean.EventBean;
import com.bs.xyplibs.utils.EventBusUtil;
import com.bs.xyplibs.utils.NetUtils;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/21.
 */
public abstract class DialogCallback extends StringCallback {
    private ProgressDialog dialog;
    private Context mContext;
    public AlertDialog.Builder builder;
    public DialogCallback(Activity activity) {
        mContext=activity;
        builder = new AlertDialog.Builder(activity);
        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中...");
    }
    public abstract void noNet();
    @Override
    public void onStart(Request<String, ? extends Request> request) {
        boolean isnet= NetUtils.isNetworkAvailable(BaseApp.getInstance());
        if(isnet){
        }else{
            Logger.e("网络不可用");
//            Toast.makeText(MyApplication.getInstance(),"当前网络不可用",Toast.LENGTH_SHORT).show();
//            builder.setMessage(mContext.getResources().getString(R.string.net_no_use_login));
//            builder.setPositiveButton(mContext.getResources().getString(R.string.que_ren_fu_kuan_que_ren_only),null);
//            builder.show();
//            new AlertView(mContext.getResources().getString(R.string.net_no_use_login),null,null,new String[]{"确定"},null,mContext,AlertView.Style.Alert,null).show();
            noNet();
//            EventBusUtil.postEvent(new EventBean("GLOABLE_NO_NET"));
            return;
        }
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }
    public abstract void Success(Response<String> response);
    @Override
    public void onSuccess(Response<String> response) {
        if(response.code()==200){
            Logger.e(""+response.body());
            Gson gson=new Gson();
            BaseVO baseVO=gson.fromJson(response.body(),BaseVO.class);
            if(response.body().contains("code")){
                try {
                    JSONObject jsonObject=new JSONObject(response.body());
                    String code=jsonObject.getString("code");
                    String msg=jsonObject.getString("desc");
                    if(baseVO.code.equals("200")){
                        Success(response);
                    }else if(baseVO.code.equals("201")){
                        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
                        Success(response);
                    }else if(baseVO.code.equals("202")){
                        new AlertView(msg,null,null,new String[]{"确定"},null,mContext,AlertView.Style.Alert,null).show();
                        Success(response);
//                        builder.setMessage(msg);
//                        builder.setPositiveButton(mContext.getResources().getString(R.string.que_ren_fu_kuan_que_ren_only),null);
//                        builder.show();
                    }else if(baseVO.code.equals("300")){//暂无更多数据

                    }else{
//                        new AlertView(msg,null,null,new String[]{"确定"},null,BaseApp.getInstance().getApplicationContext(),AlertView.Style.Alert,null).show();
                        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
//                        builder.setMessage(msg);
//                        builder.setPositiveButton(mContext.getResources().getString(R.string.que_ren_fu_kuan_que_ren_only),null);
//                        builder.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
