package com.bs.xyplibs.callback;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.bs.xyplibs.alertview.AlertView;
import com.bs.xyplibs.alertview.OnItemClickListener;
import com.bs.xyplibs.base.BaseApp;
import com.bs.xyplibs.base.BaseVO;
import com.bs.xyplibs.utils.NetUtils;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/1/12.
 */
@Deprecated
public abstract class OldMyStringCallback extends StringCallback {
    private Context mContext;
    public AlertDialog.Builder builder;
    public OldMyStringCallback(Activity activity) {
        mContext=activity;
        builder = new AlertDialog.Builder(activity);
    }
    @Override
    public void onStart(Request<String, ? extends Request> request) {
        boolean isnet= NetUtils.isNetworkAvailable(BaseApp.getInstance());
        if(isnet){
        }else{
            noNet();
//            new AlertView(mContext.getResources().getString(R.string.net_no_use_login),null,null,new String[]{"确定"},null,mContext,AlertView.Style.Alert,null).show();
            return;
        }
    }

    public abstract void Success(Response<String> response);
    public abstract void noNet();

    @Override
    public void onSuccess(Response<String> response) {
        if(response.code()==200){
            Logger.e(""+mContext.getPackageResourcePath()+response.body());

            if(response.body().contains("code")){
                try {
                    Gson gson=new Gson();
                    BaseVO baseVO=gson.fromJson(response.body(),BaseVO.class);
                    JSONObject jsonObject=new JSONObject(response.body());
                    String code=jsonObject.getString("code");
                    String msg=jsonObject.getString("desc");

                    if(baseVO.code.equals("200")){
                        Success(response);
                    }else if(baseVO.code.equals("201")){
                        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
                        Success(response);
                    }else if(baseVO.code.equals("202")){
                        new AlertView(msg, null, null, new String[]{"确定"}, null, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {

                            }
                        }).show();
                        Success(response);
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

    @Override
    public void onFinish() {
        super.onFinish();
    }
}
