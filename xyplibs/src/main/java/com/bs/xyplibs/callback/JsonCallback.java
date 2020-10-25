package com.bs.xyplibs.callback;


import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bs.xyplibs.alertview.AlertView;
import com.bs.xyplibs.alertview.OnItemClickListener;
import com.bs.xyplibs.base.BaseApp;
import com.bs.xyplibs.base.BaseVO;
import com.bs.xyplibs.bean.EventBean;
import com.bs.xyplibs.utils.EventBusUtil;
import com.bs.xyplibs.utils.NetUtils;
import com.bs.xyplibs.utils.Utils;
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

/**
 *
 * @param <T>泛型必须写生成实体类
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

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
        Gson gson=new Gson();
        JsonReader jsonReader=new JsonReader(body.charStream());
        if(rawType!=BaseVO.class){
            T data=gson.fromJson(jsonReader,type);
            response.close();
            return  data;
        }else{
            if(typeArgument==Void.class){
                SimpleResponse simpleResponse=gson.fromJson(jsonReader,SimpleResponse.class);
                response.close();
                return (T) simpleResponse.toLzyResponse();
            }else{
                //有数据类型，表示有data
                BaseVO vo=gson.fromJson(jsonReader,type);
                response.close();
                String code=vo.code;
               if(code.equals("200")){//列表请求成功只加载数据
                    return (T) vo.getData();
               }else if(code.equals("201")){//成功需要提示的弹窗点击确定
                   return (T) vo.getData();
               }else if(code.equals("202")){
                   return (T) vo.getData();
               }else if(code.equals("300")){
                    throw new IllegalStateException("无更多数据"+code);
               }else if(code.equals("400")){
                   Success((T) vo.getData());
                   throw new IllegalStateException("服务端返回错误或者失败"+code);
               }else if(code.equals("500")){//强制退出
                   throw new IllegalStateException("强制退出"+code);
               }else{
                   throw new IllegalStateException("其他未知错误"+code);
               }

            }
        }
        /**
         * 以下三行是优化写法
         */
    }
    public abstract void Success(T t);
    public  void NoMoreData(){

    };
    @Override
    public void onSuccess(final com.lzy.okgo.model.Response<T> response) {
        if(response.code()==200){//此为网络可以正常访问，有返回值，此处200并非服务器返回值200，
            BaseVO vo = (BaseVO) response.body();
            if(vo!=null){
               String code= vo.getCode();
               if(code.equals("200")){//200成功无任何提示，，
                   Success(response.body());
               }else if(code.equals("201")){//201成功并提示信息3秒内自动关闭，
                   Toast.makeText(BaseApp.getInstance(),vo.getDesc(),Toast.LENGTH_LONG).show();
                   Success(response.body());
               }else if(code.equals("202")){//202成功并提示信息须用户点击确认后关闭，
//                   new AlertView(vo.getDesc(), null, null, new String[]{"确定"}, null, mContext, AlertView.Style.Alert, new OnItemClickListener() {
//                       @Override
//                       public void onItemClick(Object o, int position) {
//                           Success(response.body());
//                       }
//                   }).show();

               }
            }
        }
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
            String message=exception.getMessage();
            if(message.indexOf("300")!=-1){
                NoMoreData();
            }else if(message.indexOf("400")!=-1){
                Toast.makeText(BaseApp.getInstance(),exception.getMessage(),Toast.LENGTH_SHORT).show();
            }else if(message.indexOf("500")!=-1){
                EventBusUtil.postEvent(new EventBean("JsonCallback_LogOut"));
            }else{
                Toast.makeText(BaseApp.getInstance(),exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
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
