package com.mc.phonelive.httpnet;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.mc.phonelive.AppContext;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.alibaba.fastjson.util.IOUtils.UTF8;


/**
 * @author LYK
 * <p>
 * 网络请求工具类
 */

public class HttpUtils {


    private Callback callback;

    /**
     * GET请求，默认允许重复请求，统一处理其他状态
     *
     * @param url           请求地址
     * @param param         请求参数
     * @param isHandleError 是否统一处理其他状态
     * @param cls           解析类
     * @param callback      解析回调，需要泛型写出解析类
     */
    public static void GET(final String url, Map<String, String> param, final boolean isHandleError, final Class<?> cls, final Callback callback) {

        /**
         * 判断请求地址是否为空
         */
        if (TextUtils.isEmpty(url)) {
            Logger.e("请求地址不能为空");
            return;
        }

        if (param == null) {
            param = new HashMap<>();
        }

        final String key = url + param.toString();

        ApiService apiService = Api.INSTANCE.getInstance().create(ApiService.class);
        final Observable<ResponseBody> request = apiService.getRequest(url, param);
        request.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (callback != null) {
                            callback.onStart();
                        }
                        Api.INSTANCE.addDisposable(key, d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {


                        String json = "";

                        if (callback != null) {
                            try {
                                json = responseBody.string();

                                new org.json.JSONObject(json);
//                                Logger.json(json);


                                String code = JsonUtils.getSinglePara(json, "code");

                                if (!TextUtils.isEmpty(code)){

                                    if (isHandleError) {

                                        if ("0".equals(code)||"201".equals(code)||"202".equals(code)) {
                                            /**
                                             * 状态正确
                                             */
                                            handRequestResult(code, json, cls, callback);

                                        } else {
                                            /**
                                             /**
                                             * 其他状态统一处理
                                             */
                                            handleOtherStatus(json,callback);

                                        }


                                    } else {
                                        /**
                                         * 不统一处理，要再每个请求方法回调里自己处理
                                         */


                                        if ("0".equals(code)||"201".equals(code)||"202".equals(code)) {
                                            /**
                                             * 状态正确
                                             */
                                            handRequestResult(code, json, cls, callback);

                                        } else {
                                            /**
                                             /**
                                             * 其他状态自己处理
                                             */

                                            handleOtherStatus(code,json,callback);


                                        }

                                    }


                                }else {



                                    handRequestResult(code, json, cls, callback);


                                }



                            } catch (IOException e) {

                                if (callback != null) {
                                    callback.onFailed(e);

                                }
                            } catch (JSONException e) {

                                Logger.e("不合法的json：" + json);

                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        if (callback != null) {
                            callback.onFailed(e);
                        }

                    }

                    @Override
                    public void onComplete() {
                        if (callback != null) {
                            callback.onFinish();
                        }

                        Api.INSTANCE.removeDisposable(key);
                    }
                });


    }


    /**
     * GET请求，默认允许重复请求
     *
     * @param url      请求地址
     * @param param    请求参数
     * @param cls      解析类
     * @param callback 解析回调，需要泛型写出解析类
     */
    public static void GET(final String url, Map<String, String> param, final Class<?> cls, final Callback callback) {
        GET(url, param, true, cls, callback);
    }


    /**
     * 可以控制是否重复请求的GET请求
     *
     * @param url      请求地址
     * @param param    请求参数
     * @param cls      解析类
     * @param isRepeat 是否允许重复请求
     * @param callback 解析回调，需要泛型写出解析类
     */
    public static void GET(final String url, final Map<String, String> param, final Class<?> cls, boolean isRepeat, final Callback callback) {


        final String key = url + param.toString();
        if (isRepeat) {
            /**
             * 允许重复请求
             */

            GET(url, param, cls, callback);


        } else {
            /**
             * 禁止重复请求
             */
            boolean exitRequest = Api.INSTANCE.isExitRequest(key);

            if (exitRequest) {
                /**
                 * 请求池里有请求，禁止请求
                 */

            } else {
                /**
                 * 没有请求，开始请求
                 */

                GET(url, param, cls, callback);

            }


        }

    }


    /**
     * 无参数GET请求，默认请求参数为空，默认允许重复的GET请求
     *
     * @param url      请求地址
     * @param cls      解析类
     * @param callback 解析回调，需要泛型写出解析类
     */
    public static void GET(final String url, final Class<?> cls, final Callback callback) {

        GET(url, null, cls, callback);

    }


    /**
     * 无参数、无解析类，默认请求参数为空，解析类默认为String，默认允许重复的GET请求
     *
     * @param url      请求地址
     * @param callback 解析类回调，泛型是String 类型的json
     */
    public static void GET(final String url, final Callback callback) {
        GET(url, null, null, callback);
    }

    /**
     * 无解析类,解析类默认为String，默认允许重复的GET请求
     *
     * @param url      请求地址
     * @param param    请求参数
     * @param callback 解析类回调，泛型是String 类型的json
     */
    public static void GET(final String url, final Map<String, String> param, final Callback callback) {
        GET(url, param, null, callback);
    }


//    public static void DOWNLOAD_FILE(final String url, final Callback callback){
//
//        ApiService apiService = Api.INSTANCE.getInstance().create(ApiService.class);
//
//        final Observable<ResponseBody> request = apiService.getRequest(url);
//        request.subscribeOn(Schedulers.io()).
//                observeOn(AndroidSchedulers.mainThread()).
//                subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                        if (callback != null) {
//                            callback.onStart();
//                        }
//
//                        Api.INSTANCE.addDisposable(url, d);
//
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody responseBody) {
//
//                        String[] split = url.split("\\.");
//                        String filename = MD5Utils.encrypt(url)+"."+split[split.length-1];
////                        String filepath=Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath()+"/"+filename;
//                        String filepath= CommentUtil.getAppRootPath(App.instance).getPath()+"/Camera/"+filename;
//                        Log.i("logger",filepath);
//                        writeFile(responseBody,filepath,callback);
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        if (callback != null) {
//                            callback.onFailed(e);
//                        }
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        if (callback != null) {
//                            callback.onFinish();
//                        }
//
//                        Api.INSTANCE.removeDisposable(url);
//                    }
//                });
//
//
//
//
//    }


    /**
     * ******************************************************************************************************
     */


    /**
     * POST请求，默认允许重复请求,默认统一处理其他状态
     *
     * @param url           请求地址
     * @param param         请求参数,需要写成model传入进去
     * @param isHandleError 是否统一处理其他状态
     * @param cls           解析类
     * @param callback      解析回调，需要泛型写出解析类
     */
    public static void POST(final String url, Map<String, String> param, final boolean isHandleError, final Class<?> cls, final Callback callback) {

        /**
         * 判断请求地址是否为空
         */
        if (TextUtils.isEmpty(url)) {
            Logger.e("请求地址不能为空");
            return;
        }

        if (param == null) {
            param = new HashMap<>();
        }

//        param.put("imei", MD5Utils.encrypt(CommentUtil.getDeviceId()));
        param.put("equipment_num", Settings.Secure.getString(AppContext.sInstance.getContentResolver(), Settings.Secure.ANDROID_ID) );

//        UserDataModel userDataModel = SPUtil.getUserDataModel();
//        if(userDataModel!=null) {
//            String login_type = userDataModel.getData().getLogin_type();
////            String login_type = SPUtil.getString("login");
////            param.put("login_type",login_type);
//        }


        /**
         * 加密处理
         */

        encryption(param);


        final String key = url + param.toString();

        ApiService apiService = Api.INSTANCE.getInstance().create(ApiService.class);

        final Observable<ResponseBody> request = apiService.postRequest(url, param);
        request.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        if (callback != null) {
                            callback.onStart();
                        }

                        Api.INSTANCE.addDisposable(key, d);


                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {


                        String json = "";

                        if (callback != null) {
                            try {



                                json = responseBody.string();

                                new org.json.JSONObject(json);
//                                new org.json.JSONObject(json);
                                //                                Logger.json(json);


                                String code = JsonUtils.getSinglePara(json, "code");

                                if (isHandleError) {

                                    if ("0".equals(code)||"201".equals(code)||"202".equals(code)) {
                                        /**
                                         * 状态正确
                                         */
                                        handRequestResult(code, json, cls, callback);

                                    } else {
                                        /**
                                         /**
                                         * 其他状态统一处理
                                         */
                                        handleOtherStatus(json, callback);

                                    }


                                } else {
                                    /**
                                     * 不统一处理，要再每个请求方法回调里自己处理
                                     */

                                    if ("0".equals(code)||"201".equals(code)||"202".equals(code)) {
                                        /**
                                         * 状态正确
                                         */
                                        handRequestResult(code, json, cls, callback);

                                    } else {
                                        /**
                                         /**
                                         * 其他状态自己处理
                                         */

                                        handleOtherStatus(code,json,callback);


                                    }

                                }

                            } catch (IOException e) {

                                if (callback != null) {
                                    callback.onFailed(e);

                                }
                            } catch (JSONException e) {

                                Logger.e("不合法的json：" + json);

                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        if (callback != null) {
                            callback.onFailed(e);
                        }

                    }

                    @Override
                    public void onComplete() {
                        if (callback != null) {
                            callback.onFinish();
                        }

                        Api.INSTANCE.removeDisposable(key);
                    }
                });


    }


    /**
     * POST请求，默认允许重复请求
     *
     * @param url      请求地址
     * @param param    请求参数,需要写成model传入进去
     * @param cls      解析类
     * @param callback 解析回调，需要泛型写出解析类
     */
    public static void POST(final String url, Map<String, String> param, final Class<?> cls, final Callback callback) {
        POST(url, param, true, cls, callback);
    }


    /**
     * 可以控制是否重复请求的POST请求
     *
     * @param url      请求地址
     * @param param    请求参数
     * @param cls      解析类
     * @param isRepeat 是否允许重复请求
     * @param callback 解析回调，需要泛型写出解析类
     */
    public static void POST(final String url, final Map<String, String> param, final Class<?> cls, boolean isRepeat, final Callback callback) {


        final String key = url + param.toString();
        if (isRepeat) {
            /**
             * 允许重复请求
             */

            POST(url, param, cls, callback);


        } else {
            /**
             * 禁止重复请求
             */
            boolean exitRequest = Api.INSTANCE.isExitRequest(key);

            if (exitRequest) {
                /**
                 * 请求池里有请求，禁止请求
                 */

            } else {
                /**
                 * 没有请求，开始请求
                 */

                POST(url, param, cls, callback);

            }


        }

    }


    /**
     * 无参数POST请求，默认请求参数为空，默认允许重复请求
     *
     * @param url      请求地址
     * @param cls      解析类
     * @param callback 解析回调，需要泛型写出解析类
     */
    public static void POST(final String url, final Class<?> cls, final Callback callback) {

        POST(url, null, cls, callback);

    }


    /**
     * 无参数、无解析类，默认请求参数为空，解析类默认为String，默认允许重复的POST请求
     *
     * @param url      请求地址
     * @param callback 解析类回调，泛型是String 类型的json
     */
    public static void POST(final String url, final Callback callback) {
        POST(url, null, null, callback);
    }

    /**
     * 无解析类,解析类默认为String，默认允许重复的POST请求
     *
     * @param url      请求地址
     * @param param    请求参数
     * @param callback 解析类回调，泛型是String 类型的json
     */
    public static void POST(final String url, final Map<String, String> param, final Callback callback) {
        POST(url, param, null, callback);
    }


    public synchronized static void POST_WHITH_UPLOAD(final String url, Map<String, String> param, Map<String, String> singleFilePath, Map<String, List<String>> filePathMaps, boolean isRepeat, final boolean isHandleError, final Class<?> cls, final Callback callback) {

        final String key = url;
        if (isRepeat) {
            /**
             * 允许重复请求
             */

            POST_WHITH_UPLOAD(url, param, singleFilePath, filePathMaps, isHandleError, cls, callback);

        } else {
            /**
             * 禁止重复请求
             */
            boolean exitRequest = Api.INSTANCE.isExitRequest(key);

            if (exitRequest) {
                /**
                 * 请求池里有请求，禁止请求
                 */

            } else {
                /**
                 * 没有请求，开始请求
                 */

                POST_WHITH_UPLOAD(url, param, singleFilePath, filePathMaps, isHandleError, cls, callback);

            }


        }

    }

    /**
     * POST请求，默认允许重复请求,默认统一处理其他状态
     *
     * @param url           请求地址
     * @param param         请求参数,需要写成model传入进去
     * @param isHandleError 是否统一处理其他状态
     * @param cls           解析类
     * @param callback      解析回调，需要泛型写出解析类
     */
    public synchronized static void POST_WHITH_UPLOAD(final String url, Map<String, String> param, Map<String, String> singleFilePath, Map<String, List<String>> filePathMaps, final boolean isHandleError, final Class<?> cls, final Callback callback) {

        /**
         * 判断请求地址是否为空
         */
        if (TextUtils.isEmpty(url)) {
            Logger.e("请求地址不能为空");
            return;
        }
    Log.v("tags","url= "+url);
        if (param == null) {
            param = new HashMap<>();
        }


        /**
         * 加密处理
         */

        Logger.i("加密前数据:"+ com.alibaba.fastjson.JSONObject.toJSONString(param));
        encryption(param);

        final String key =url;

        ApiService apiService = Api.INSTANCE.getInstance().create(ApiService.class);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        /**
         * 添加文本参数
         */
        for (String mapkey:param.keySet()){
            builder.addFormDataPart(mapkey,param.get(mapkey));
        }

        /**
         * 添加单个文件
         */
        for (String mapkey:singleFilePath.keySet()){
            String filePath= singleFilePath.get(mapkey);
            File file=new File(filePath);
            Log.i("tags",file.getPath());
            builder.addFormDataPart(mapkey,file.getName(),RequestBody.create(MediaType.parse("image/*"), file));
        }

        /**
         * 添加多个文件
         */
        for (String mapkey:filePathMaps.keySet()){

            List<String> filesPath = filePathMaps.get(mapkey);

            for (int i=0;i<filesPath.size();i++){

                String filePath = filesPath.get(i);
                if (!TextUtils.isEmpty(filePath)){
                    /**
                     * 文件路径不为空
                     */
                    File file=new File(filePath);
                    Log.i("tags",file.getPath());
                    builder.addFormDataPart(mapkey+"["+i+"]",file.getName(),RequestBody.create(MediaType.parse("image/*"), file));
                }else {
                    /**
                     * 文件路径为空
                     */
                    File file = new File(AppContext.sInstance.getFilesDir().getPath()+"emptyfilelyk");
                    if (file.exists()){
                        Log.i("tags",file.getPath());
                        builder.addFormDataPart(mapkey+"["+i+"]",file.getName(),RequestBody.create(MediaType.parse("image/*"), file));
                    }else {
                        try {
                            file.createNewFile();
                            Log.i("tags",file.getPath());
                            builder.addFormDataPart(mapkey+"["+i+"]",file.getName(),RequestBody.create(MediaType.parse("image/*"), file));
                        } catch (IOException e) {
                            Logger.i("上传文件创建空文件失败");
                        }
                    }

                }

            }

        }

        RequestBody  requestBody = builder.build();

        final Observable<ResponseBody> request = apiService.postRequestWithUpload(url, requestBody);
        request.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        if (callback != null) {
                            callback.onStart();
                        }

                        Api.INSTANCE.addDisposable(key, d);


                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {


                        String json = "";

                        if (callback != null) {
                            try {
                                json = responseBody.string();
                                Log.v("tags",json);

                                new org.json.JSONObject(json);
                                                                Logger.json(json);
                                Log.v("tags",json+"");

                                handRequestResult("0", json, cls, callback);

                            } catch (IOException e) {

                                if (callback != null) {
                                    callback.onFailed(e);

                                }
                            } catch (JSONException e) {

                                Logger.e("不合法的json：" + json);

                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        if (callback != null) {
                            callback.onFailed(e);
                        }

                    }

                    @Override
                    public void onComplete() {
                        if (callback != null) {
                            callback.onFinish();
                        }

                        Api.INSTANCE.removeDisposable(key);
                    }
                });


    }


    /**
     * ******************************************************************************************************
     */


    /**
     * 统一处理其他状态
     *
     * @param json
     */
    public static void handleOtherStatus(String json, Callback callback) {

        String msg = JsonUtils.getSinglePara(json, "msg");
        String code = JsonUtils.getSinglePara(json, "code");
        if (!code.equals("500")) {
//            CommentUtil.showSingleToast(msg);
        }else{
//            EventBus.getDefault().post(new EventBusModel("unLogin"));
        }

    }

    /**
     * 统一处理其他状态
     *
     * @param json
     */
    public static void handleOtherStatus(String code, String json, Callback callback) {

        String msg = JsonUtils.getSinglePara(json, "msg");
        if (!code.equals("500")) {
            callback.onOtherStatus(json, code);
        }else{
//            callback.onNoLogin(json, code);
//            EventBus.getDefault().post(new EventBusModel("unLogin"));
        }




    }


    /**
     * 请求服务器成功的处理
     *
     * @param code   状态码
     * @param json     请求的结果json
     * @param cls      要解析的model类
     * @param callback 请求回调
     */
    private static void handRequestResult(String code, String json, Class<?> cls, Callback callback) {

        if (cls != null) {

            Object object = com.alibaba.fastjson.JSONObject.parseObject(json, cls);
            callback.onSucceed(json, code, object);

        } else {

            callback.onSucceed(json, code, json);

        }

    }


    /**
     * ******************************************************************************************************
     */



    /**
     * 将输入流写入文件
     *
     * @param responseBody
     * @param filePath
     */
    private static void writeFile(ResponseBody responseBody, String filePath, Callback callback) {

        InputStream inputString= responseBody.byteStream();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b,0,len);
            }

            if (callback!=null){
                callback.onSucceed(file.getPath(),"0","");
            }
            inputString.close();
            fos.close();
        } catch (Exception e) {
            if (callback!=null){
                callback.onFailed(e);
            }
        }finally {
            if (callback!=null){
                callback.onFinish();
            }
        }

    }

    /**
     * 加密数据
     * @param param
     */
    public static void encryption(Map<String, String> param){

        String[]str=new String[param.size()];
        int index=0;
        for (String key:param.keySet()){
            str[index]=key+"="+param.get(key);
            index++;
        }
//        String timestamp= MD5Utils.getString(str)[0];
//        String sign= MD5Utils.getString(str)[1];

//        param.put("timestamp",timestamp);
//        param.put("sign",sign);

    }



    /**
     * 拦截器,可以修改header,可以通过拦截器打印日志
     */
    public static class MyLogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    //                    .addHeader("Content-Type", "application/json")
                    .build();

            /**
             * url
             */
            HttpUrl url = request.url();
            String httpUrl = url.url().toString();

            /**
             * 请求方法
             */
            String method = request.method();

            /**
             * 请求头
             */
            Map<String, List<String>> headers = request.headers().toMultimap();

            /**
             * 请求体
             */
            RequestBody requestBody = request.body();
            String resquestBodyStr = getRequestBody(requestBody);

            Response response = chain.proceed(request);
//            Response logResponse = chain.proceed(request);
//            String json = logResponse.body().string();
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(9223372036854775807L);
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            String json = buffer.clone().readString(charset);
            Logger.i("请求地址:" + httpUrl + "\n" +
                    "请求方法:" + method + "\n" +
                    "请求头:" + headers + "" + "\n" +
                    "请求体:" + resquestBodyStr + "\n" +
                    "请求结果:" + json);


            return response;
        }

    }


    /**
     * 请求体的内容解析
     *
     * @param requestBody
     * @return
     */
    public static String getRequestBody(RequestBody requestBody) {

        try {
            Buffer buffer = new Buffer();
            if (requestBody != null) {
                requestBody.writeTo(buffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                String paramsStr = buffer.readString(charset);
                return paramsStr;
            } else {
                return "请求体为空";
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }

        return "";
    }


    /**
     * user-agent的配置
     */
    public static class UserAgentInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", "kkcast")
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }


}
