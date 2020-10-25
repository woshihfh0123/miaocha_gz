package com.bs.xyplibs.callback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
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
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by Administrator on 2017/12/21.
 */

public abstract class JsonDialogCallback<T> extends AbsCallback<T> {


    private Context context;
    private ProgressDialog dialog;
    public JsonDialogCallback(Activity activity){
        context=activity;
        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中...");
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        boolean isnet= NetUtils.isNetworkAvailable(BaseApp.getInstance());
        if(isnet){
            Logger.e("网络连接正常");
        }else{
            Logger.e("网络不可用");
            Toast.makeText(BaseApp.getInstance(),"当前网络不可用",Toast.LENGTH_SHORT).show();
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
    @Override
    public T convertResponse(Response response) throws Throwable {
        Type genType=getClass().getGenericSuperclass();
        Type[]params=((ParameterizedType)genType).getActualTypeArguments();
        Type type=params[0];
        if(!(type instanceof  ParameterizedType))throw new IllegalStateException("没有填写泛型参数");
        Type rawType=((ParameterizedType) type).getRawType();
        Type typeArgument=((ParameterizedType) type).getActualTypeArguments()[0];
        ResponseBody body=response.body();
        if(body==null)return null;
        if(rawType!=BaseVO.class){
            T data= JSONObject.parseObject(body.string(),type);
            response.close();
            return  data;
        }else{
            if(typeArgument==Void.class){
                SimpleResponse simpleResponse= JSONObject.parseObject(body.string(),SimpleResponse.class);
                response.close();
                return (T) simpleResponse.toLzyResponse();
            }else{
                //有数据类型，表示有data
//                BaseVO vo=gson.fromJson(jsonReader,type);
//              BaseVO vo=  JSONObject.parseObject(String.valueOf(jsonReader),type);
              BaseVO vo=  JSONObject.parseObject(body.string(),type);
//                com.alibaba.fastjson.JSONObject basejs= com.alibaba.fastjson.JSONObject.parseObject(jsonReader.toString());
//                BaseVO vo1= JSON.toJavaObject(jsonReader.toString(),BaseVO.class);
                Logger.e("data:"+vo.toString());
                response.close();
                return (T) vo;
            }
        }
        /**
         * 以下三行是优化写法
         */
    }
    public abstract void Success(T response);
    @Override
    public void onSuccess(final com.lzy.okgo.model.Response<T> response) {
        if(response.code()==200){//此为网络可以正常访问，有返回值，此处200并非服务器返回值200，
            final BaseVO<T> vo = (BaseVO) response.body();
           Object object=JSONObject.parseObject(response.body().toString(),vo.getClass());
//           T t= response.body();
            if(vo!=null){
                String code= vo.getCode();
                if(code.equals("200")){//200成功无任何提示，，
                    Success((T)vo.getData());
                }else if(code.equals("201")){//201成功并提示信息3秒内自动关闭，
                    Toast.makeText(BaseApp.getInstance(),vo.getDesc(),Toast.LENGTH_LONG).show();
                    Success((T)vo.getData());
                }else if(code.equals("202")){//202成功并提示信息须用户点击确认后关闭，
//                    new AlertView(vo.getDesc(), null, null, new String[]{"确定"}, null, mContext, AlertView.Style.Alert, new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(Object o, int position) {
//                            Success((T)vo.getData());
//                        }
//                    }).show();

                }else if(code.equals("300")){//300暂无数据，

                }else if(code.equals("400")){//400错误或失败，
                    Success1((T) vo.getData());
                }else if(code.equals("500")){//500强制退出登录

                }else{
                    Toast.makeText(BaseApp.getInstance(),vo.getDesc(),Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void Success1(T object) {
    }


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
}
