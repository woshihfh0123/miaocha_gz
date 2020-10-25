package com.picture.android.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.picture.android.PictureActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Environment.DIRECTORY_DCIM;
import static com.alibaba.fastjson.util.IOUtils.UTF8;

/**
 * created by WWL on 2019/5/31 0031:15
 */
public class HttpUtils {

    public static String HOST="http://www.221w.com/";

    public static void DOWNLOAD_FILE(final Context context, final String url){
         OkHttpClient.Builder builder= new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
//                .connectTimeout(1, TimeUnit.MILLISECONDS)
                .addInterceptor(new MyLogInterceptor())
                .addInterceptor(new UserAgentInterceptor())
                .build();
        final Retrofit instance = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(HttpUtils.HOST)
                    .build();

        final ApiService apiService = instance.create(ApiService.class);

        final Observable<ResponseBody> request = apiService.getRequest(url);

        request.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Api.INSTANCE.addDisposable(url, d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                        String[] split = url.split("\\.");
                        String filename = MD5Utils.encrypt(url)+"."+split[split.length-1];
//                        String filepath=Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath()+"/"+filename;
                        Log.i("logger",filename);
                        String filepath= CommentUtil.getAppRootPath(context).getPath()+"/"+filename;
                        Log.i("logger",filepath);
                        writeFile(context,responseBody,filepath);

                    }

                    @Override
                    public void onError(Throwable e) {

                        Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
                        Api.INSTANCE.removeDisposable(url);
                    }
                });

    }
    /**
     * 将输入流写入文件
     *
     * @param responseBody
     * @param filePath
     */
    private static void writeFile(Context context,ResponseBody responseBody, String filePath) {

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
            Log.i("logger","success");
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));


            Toast.makeText(context, "已保存在" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            inputString.close();
            fos.close();
        } catch (Exception e) {
            Toast.makeText(context, "图片格式不正确", Toast.LENGTH_SHORT).show();
//            if (callback!=null){
//                callback.onFailed(e);
//            }
        }finally {
            Log.i("logger","end");
//            if (callback!=null){
//                callback.onFinish();
//            }
        }

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
            Log.i("logger","请求地址:" + httpUrl + "\n" +
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
            Log.e("logger",e.toString());
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
