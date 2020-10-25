package com.mc.phonelive.httpnet;


import com.orhanobut.logger.Logger;
import com.mc.phonelive.AppConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author LYK
 * 单例实现Retrofit对象
 */

public enum Api {

    INSTANCE;

    private Retrofit instance;

    private Map<String,Disposable> disposableMap;
    private OkHttpClient.Builder builder;

    Api() {
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(false)
//                .connectTimeout(1, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpUtils.MyLogInterceptor())
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new HttpUtils.UserAgentInterceptor())
                //https
                //        HttpsSSL httpsSSL = SSLUtils.getSSL();
                //        .sslSocketFactory(httpsSSL.getSslSocketFactory(),httpsSSL.getTrustManager())
                .build();

       initHttpConfig();

        disposableMap=new HashMap<>();

    }



    public void initHttpConfig(){

        if (builder!=null) {
            instance = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(AppConfig.HOST)
                    .build();
        }

    }
    public Retrofit getInstance() {
        return instance;
    }

    /**
     * 取消网络请求
     * @param key
     */
    public  void cancelRequest(String key){
        Disposable disposable = disposableMap.get(key);
            try {
                disposable.dispose();
            } catch (Exception e) {
               Logger.e("disposable对象为空，请检查添加和获取的key是否一致");
            }
    }


    public synchronized void addDisposable(String key, Disposable disposable){
        disposableMap.put(key,disposable);
    }



    public synchronized void removeDisposable(String key){
        if (disposableMap.containsKey(key)) {
            disposableMap.remove(key);
        }else {
//            Logger.e("移除Disposable失败，请检查key是否存在");
        }
    }


    /**
     * 判断是否存在请求对象
     * @param key
     * @return
     */
    public synchronized boolean isExitRequest(String key){

        if (disposableMap.containsKey(key)){
            return true;
        }else {
            return false;
        }

    }



}
