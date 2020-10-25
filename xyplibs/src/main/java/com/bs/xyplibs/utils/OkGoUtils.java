package com.bs.xyplibs.utils;

import android.content.Context;
import android.provider.Settings;

import com.bs.xyplibs.base.BaseApp;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.PostRequest;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;

/**
 * Created by Administrator on 2018/4/11.
 */

public class OkGoUtils {
    public  static  String androidKey="a0fdcd06cd244dcf8d3db31c6e6c0f0c";
    public static <T> void getRequets(Context context, String url, Object tag, Map<String, String> map, AbsCallback<T> callback) {
        // TODO: 2018/5/3 加密 时间戳等 请求日志打印
        String tm=System.currentTimeMillis()+"";
        String temptime=tm.substring(0,10);
        String mySign= MD5Utils.Md5(temptime+androidKey);
        HttpHeaders headers=new HttpHeaders();
        if(SUtils.getInstance().isLogin()){
            headers.put("sid", SUtils.getInstance().getToken());
            headers.put("os", "android");
            headers.put("version",AppUtils.getAppVersionCode(context)+"");
            headers.put("timestamp",temptime);
            headers.put("sec",mySign.toLowerCase());
        }
        OkGo.<T>get(url)
                .tag(tag)
                .headers(headers)
                .params(map)
                .execute(callback);
    }
    public static <T> void postRequest(Context context,String url, Object tag, TreeMap<String, String> map, AbsCallback<T> callback) {
        // TODO: 2017/10/13  加密 时间戳等 请求日志打印
        String tm=System.currentTimeMillis()+"";
        String temptime=tm.substring(0,10);
        String mySign= MD5Utils.Md5(temptime+androidKey);
        HttpHeaders headers=new HttpHeaders();
        if(SUtils.getInstance().isLogin()){
            headers.put("sid", SUtils.getInstance().getToken());
            headers.put("os", "android");
            headers.put("version",AppUtils.getAppVersionCode(context)+"");
            headers.put("timestamp",temptime);
            headers.put("sec",mySign.toLowerCase());
        }
        Logger.e("请求地址："+url);
        Logger.e("请求参数："+JsonUtils.toJson(map));
        OkGo.<T>post(url)
                .tag(tag)
                .headers(headers)
                .upJson(JsonUtils.toJson(map))
                .execute(callback);
    }
    public static <T> void postRequest(Context context,String url, Object tag,Map<Object,Object>map, AbsCallback<T> callback) {
        // TODO: 2017/10/13  加密 时间戳等 请求日志打印
        String tm=System.currentTimeMillis()+"";
        String temptime=tm.substring(0,10);
        String mySign= MD5Utils.Md5(temptime+androidKey);
        HttpHeaders headers=new HttpHeaders();
        if(SUtils.getInstance().isLogin()){
            headers.put("sid", SUtils.getInstance().getToken());
            headers.put("os", "android");
            headers.put("version",AppUtils.getAppVersionCode(context)+"");
            headers.put("timestamp",temptime);
            headers.put("sec",mySign.toLowerCase());
        }
        Logger.e("请求地址："+url);
        Logger.e("请求参数："+JsonUtils.toJson(map));
        OkGo.<T>post(url)
                .tag(tag)
                .headers(headers)
                .upJson(JsonUtils.toJson(map))
                .execute(callback);
    }
    public static <T> void postJsObjct(Context context, String url, Object tag, TreeMap<String, String> map, List<List<String>> svalue, List<List<File>> kvalue,List<String>kname, AbsCallback<T> callback) {

        // TODO: 2017/10/13  加密 时间戳等 请求日志打印
        String tm=System.currentTimeMillis()+"";
        String temptime=tm.substring(0,10);
        if(SUtils.getInstance().isLogin()){
            map.put("uid", SUtils.getInstance().getUserid());
            map.put("token", SUtils.getInstance().getToken());
            map.put("username",SUtils.getInstance().getLogin_name());
        }
//        map.put("lang", SUtils.getInstance().getLang());
        map.put("timestamp",temptime);
//        map.put("equipment_num", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) );//设备信息
        String ss="";
        String []stra=new String[map.size()];
        int is=0;
        for(String key:map.keySet()){
            stra[is]=key+"="+map.get(key);
            is++;
        }
        Arrays.sort(stra);
        for(int i=stra.length-1;i>-1;i--){
            ss=ss+stra[i]+"&";
        }
        String stsign=ss+Constant.MD5_TITLE;
        String mySign= MD5Utils.Md5(stsign);
        map.put("sign",mySign);

        Logger.e("加密后:"+new Gson().toJson(map));
        PostRequest<T> og = OkGo.<T>post(url)
                .tag(tag)
                .params(map);
        for(int i=0;i<kname.size();i++){
            ArrayList<File> vls = (ArrayList<File>) kvalue.get(i);
            for(int j=0;j<vls.size();j++){
                og.params(kname.get(i)+"["+j+"]",vls.get(j));
            }
//            og.addFileParams(kname.get(i)+"["+i+"]",vls);
        }
        Logger.e("请求链接："+url+"?"+stsign);
//        for(int i=0;i<kname.size();i++){
//            if(kvalue!=null&&kvalue.size()>0){
////                og.params(kname.get(i),svalue.get(i).toString());
//                String name=kname.get(i);
//                List<File> vl = kvalue.get(i);
//                for(int j=0;j<vl.size();j++){
//                    og.addFileParams(name,vl);
////                    og.params(name+j,vl.get(j));
//                    Logger.e("key:"+name+"------"+"file 文件size:"+vl.get(j).length()+"--"+vl.get(j).getName());
//
//                }
////                og.params(kname.get(i),kvalue.get(i))
//            }
//        }
        Logger.e("ccc:"+new Gson().toJson(og));
        og.execute(callback);
    }

    public static <T> void postJsObjct(Context context, String url, Object tag, TreeMap<String, String> map, List<File> files, AbsCallback<T> callback) {
        // TODO: 2017/10/13  加密 时间戳等 请求日志打印
        String tm=System.currentTimeMillis()+"";
        String temptime=tm.substring(0,10);
        String mySign= MD5Utils.Md5(temptime+androidKey);
        HttpHeaders headers=new HttpHeaders();
        if(SUtils.getInstance().isLogin()){
            headers.put("sid", SUtils.getInstance().getToken());
            headers.put("os", "android");
            headers.put("version",AppUtils.getAppVersionCode(context)+"");
            headers.put("timestamp",temptime);
            headers.put("sec",mySign.toLowerCase());
        }
        PostRequest<T> og = OkGo.<T>post(url)
                .tag(tag)
                .cacheTime(120000)
                .retryCount(5)
                .headers(headers)
                .upJson(JsonUtils.toJson(map))
               ;
        if(Utils.isNotEmpty(files)){
            for(int i=0;i<files.size();i++){
                if(files.get(i)!=null){
                    og.params("file"+i,files.get(i));
                }else{
                    og.params("goods_image",null);
                }
            }
        }

        og.execute(callback);
    }
    public static <T> void postJsObjctForYz(Context context, String url, Object tag, TreeMap<String, String> map, List<File> files, AbsCallback<T> callback) {
        // TODO: 2017/10/13  加密 时间戳等 请求日志打印
        String tm=System.currentTimeMillis()+"";
        String temptime=tm.substring(0,10);
        String mySign= MD5Utils.Md5(temptime+androidKey);
        HttpHeaders headers=new HttpHeaders();
        if(SUtils.getInstance().isLogin()){
            headers.put("sid", SUtils.getInstance().getToken());
            headers.put("os", "android");
            headers.put("version",AppUtils.getAppVersionCode(context)+"");
            headers.put("timestamp",temptime);
            headers.put("sec",mySign.toLowerCase());
        }
        Logger.e("header:"+headers.toJSONString());
        Logger.e("params:"+JsonUtils.toJson(map));
        Logger.e("files:"+files.size());
        PostRequest<T> og = OkGo.<T>post(url)
                .tag(tag)
                .cacheTime(120000)
                .retryCount(5)
                .headers(headers)
                .params(map)
                .isMultipart(true)
                .addFileParams("cover",files)
               ;
        og.execute(callback);
    }
    public static <T> void postJsObjctForYzPl(Context context, String url, Object tag, TreeMap<String, String> map, List<File> files, AbsCallback<T> callback) {
        // TODO: 2017/10/13  加密 时间戳等 请求日志打印
        String tm=System.currentTimeMillis()+"";
        String temptime=tm.substring(0,10);
        String mySign= MD5Utils.Md5(temptime+androidKey);
        HttpHeaders headers=new HttpHeaders();
        if(SUtils.getInstance().isLogin()){
            headers.put("sid", SUtils.getInstance().getToken());
            headers.put("os", "android");
            headers.put("version",AppUtils.getAppVersionCode(context)+"");
            headers.put("timestamp",temptime);
            headers.put("sec",mySign.toLowerCase());
        }
        Logger.e("header:"+headers.toJSONString());
        Logger.e("params:"+JsonUtils.toJson(map));
        Logger.e("files:"+files.size());
        PostRequest<T> og = OkGo.<T>post(url)
                .tag(tag)
                .cacheTime(120000)
                .retryCount(5)
                .headers(headers)
                .params(map)
                .isMultipart(true);
//                .addFileParams("pic",files);
        if(Utils.isNotEmpty(files)){
            for(int i=0;i<files.size();i++){
                og.params("pic"+(i+1),files.get(i));
            }
        }
        og.execute(callback);
    }
    public static <T> void postJsObjctForYzDt(Context context, String url, Object tag, TreeMap<String, String> map, List<File> files, AbsCallback<T> callback) {
        // TODO: 2017/10/13  加密 时间戳等 请求日志打印
        String tm=System.currentTimeMillis()+"";
        String temptime=tm.substring(0,10);
        String mySign= MD5Utils.Md5(temptime+androidKey);
        HttpHeaders headers=new HttpHeaders();
        if(SUtils.getInstance().isLogin()){
            headers.put("sid", SUtils.getInstance().getToken());
            headers.put("os", "android");
            headers.put("version",AppUtils.getAppVersionCode(context)+"");
            headers.put("timestamp",temptime);
            headers.put("sec",mySign.toLowerCase());
        }
        Logger.e("header:"+headers.toJSONString());
        Logger.e("params:"+JsonUtils.toJson(map));
        Logger.e("files:"+files.size());
        PostRequest<T> og = OkGo.<T>post(url)
                .tag(tag)
                .cacheTime(120000)
                .retryCount(5)
                .headers(headers)
                .params(map)
                .isMultipart(true)
                .addFileParams("pic1",files)
               ;
        og.execute(callback);
    }


}
