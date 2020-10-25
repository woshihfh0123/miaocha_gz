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
import com.bs.xyplibs.utils.Utils;
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
public abstract class MyStringCallback extends StringCallback {
    private Context mContext;
    public AlertDialog.Builder builder;
    public MyStringCallback(Activity activity) {
        mContext=activity;
        builder = new AlertDialog.Builder(activity);
    }
    @Override
    public void onStart(Request<String, ? extends Request> request) {
        boolean isnet= NetUtils.isNetworkAvailable(BaseApp.getInstance());
        if(isnet){

        }else{
            Toast.makeText(mContext,"当前网络不可用",Toast.LENGTH_LONG).show();
//            noNet();
//            new AlertView(mContext.getResources().getString(R.string.net_no_use_login),null,null,new String[]{"确定"},null,mContext,AlertView.Style.Alert,null).show();
            return;
        }
    }

    public abstract void Success(String response);

    @Override
    public void onSuccess(Response<String> response) {
        if(response.code()==200){
            Logger.e(""+mContext.getPackageResourcePath()+response.body());
            if(response.body().contains("code")){
                final BaseVO baseVO= com.alibaba.fastjson.JSONObject.parseObject(response.body(),BaseVO.class);
                if(Utils.isNotEmpty(baseVO)){
                    if(baseVO.code.equals("200")){
                        if(Utils.isNotEmpty(baseVO.getData())){
                            Success(response.body());
                        }
                    }else if(baseVO.code.equals("201")){
                        if(Utils.isNotEmpty(baseVO.getDesc())){
                            Toast.makeText(mContext,baseVO.getDesc(),Toast.LENGTH_LONG).show();
                        }
                        Success(baseVO.getData().toString());
                    }else if(baseVO.code.equals("202")){
                        if(Utils.isNotEmpty(baseVO.getDesc())){
                            new AlertView(baseVO.getDesc(), null, null, new String[]{"确定"}, null, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    Success(baseVO.getData().toString());
                                }
                            }).show();
                        }
                    }else if(baseVO.code.equals("300")){//暂无更多数据

                    }else{
                        if(Utils.isNotEmpty(baseVO.getDesc())){
                            Toast.makeText(mContext,baseVO.getDesc(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onFinish() {
        super.onFinish();
    }
}
