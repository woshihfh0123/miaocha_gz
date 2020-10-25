package com.mc.phonelive.httpnet;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author LYK
 * 抽出来的请求接口
 */

public interface ApiService {


    @GET
    Observable<ResponseBody> getRequest(@Url String url, @QueryMap Map<String, String> param);

    @Streaming
    @GET
    Observable<ResponseBody> getRequest(@Url String url);

    @POST
    Observable<ResponseBody> postRequestBody(@Url String url, @Body Object object);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> postRequest(@Url String url, @FieldMap Map<String, String> param);


    @POST
    Observable<ResponseBody> postRequestWithUpload(@Url String url, @Body RequestBody Body);

}
