package com.picture.android.util;

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
 * @author WWL
 * 抽出来的请求接口
 */

public interface ApiService {

    @Streaming
    @GET
    Observable<ResponseBody> getRequest(@Url String url);


}
