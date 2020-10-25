package com.bs.xyplibs.callback;


import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.bs.xyplibs.alertview.AlertView;
import com.bs.xyplibs.alertview.OnItemClickListener;
import com.bs.xyplibs.base.BaseApp;
import com.bs.xyplibs.base.BaseVO;
import com.bs.xyplibs.utils.NetUtils;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.exception.StorageException;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by Administrator on 2017/12/21.
 */

public abstract class JsCallback<T> extends AbsCallback<T> {
    private Type type;
    private Class<T>clazz;

    public JsCallback() {
    }

    public JsCallback(Type type) {
        this.type = type;
    }

    public JsCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        ResponseBody body=response.body();
        if(body==null)return null;
        T data=null;
        Gson gson=new Gson();
        JsonReader jsonReader=new JsonReader(body.charStream());
        if(type!=null){
            data=gson.fromJson(jsonReader,type);
        }else if(clazz!=null){
            data=gson.fromJson(jsonReader,clazz);
        }else{
            Type genType=getClass().getGenericSuperclass();
            Type type=((ParameterizedType)genType).getActualTypeArguments()[0];
            data=gson.fromJson(jsonReader,type);
        }
        return data;
    }
    public static <T> T getObject(String jsonString,Type listType){
        Gson gson=new Gson();
        T t=null;
        try {
            t=gson.fromJson(jsonString,listType);
        }catch (Exception e){
            e.printStackTrace();
        }

        return t;
    }
    @Override
    public void onSuccess(final com.lzy.okgo.model.Response<T> response) {
        if(response.code()==200){//此为网络可以正常访问，有返回值，此处200并非服务器返回值200，
            final BaseVO vo = (BaseVO) response.body();
            if(vo!=null){
                String code= vo.getCode();
                if(code.equals("200")){//200成功无任何提示，，
                    Success(vo);
                }else if(code.equals("201")){//201成功并提示信息3秒内自动关闭，
                    Toast.makeText(BaseApp.getInstance(),vo.getDesc(),Toast.LENGTH_LONG).show();
                    Success(vo);
                }else if(code.equals("202")){//202成功并提示信息须用户点击确认后关闭，
//                    new AlertView(vo.getDesc(), null, null, new String[]{"确定"}, null, mContext, AlertView.Style.Alert, new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(Object o, int position) {
//                            Success(vo);
//                        }
//                    }).show();

                }else if(code.equals("300")){//300暂无数据，

                }else if(code.equals("400")){//400错误或失败，

                }else if(code.equals("500")){//500强制退出登录

                }else{
                    Toast.makeText(BaseApp.getInstance(),vo.getDesc(),Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    protected abstract void Success(BaseVO<T> ss);


    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
//        super.onError(response);
        Throwable exception=response.getException();
        if(exception!=null)exception.printStackTrace();
        if(exception instanceof UnknownHostException ||exception instanceof ConnectException){
            Toast.makeText(BaseApp.getInstance(),"网络连接失败，请连接网络后重试！",Toast.LENGTH_SHORT).show();
        }else if(exception instanceof SocketTimeoutException){
            Toast.makeText(BaseApp.getInstance(),"网络请求超时",Toast.LENGTH_SHORT).show();
        }else if(exception instanceof HttpException){
            Toast.makeText(BaseApp.getInstance(),"服务器相应异常",Toast.LENGTH_SHORT).show();
        }else if(exception instanceof StorageException){
            Toast.makeText(BaseApp.getInstance(),"SD卡不存在或者没有权限",Toast.LENGTH_SHORT).show();
        }else if(exception instanceof  IllegalStateException){//自己抛出的异常自定义
            Toast.makeText(BaseApp.getInstance(),exception.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        boolean isnet= NetUtils.isNetworkAvailable(BaseApp.getInstance());
        if(isnet){
            Logger.e("网络连接正常----------------");
        }else{
            Logger.e("网络不可用");
            Toast.makeText(BaseApp.getInstance(),"当前网络不可用",Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
