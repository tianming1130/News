package com.example.administrator.news.net;

import com.example.administrator.news.bean.News;
import com.example.administrator.news.bean.Video;
import com.example.administrator.news.net.entity.ResponseLogin;
import com.example.administrator.news.net.entity.ResponseNews;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2018\4\20 0020.
 */

public interface ApiService {
    @GET("getnews.php")
    Flowable<HttpResult<ResponseNews>> getNews(@QueryMap Map<String,String> params);

    @GET("getvideo.php")
    Flowable<HttpResult<List<Video>>> getVideo(@QueryMap Map<String,String> params);

    @POST("handleuser.php")
    @FormUrlEncoded
    Flowable<HttpResult<ResponseLogin>> login(@FieldMap Map<String,String> params);

    @POST("handlepraise.php")
    @FormUrlEncoded
    Flowable<HttpResult<Integer>> handlePraise(@FieldMap Map<String,Object> params);

    @GET("getpraisebyuser.php")
    Flowable<HttpResult<List<News>>> getPraiseByUser(@QueryMap Map<String,Object> params);

//    @POST("handleuser.php")
//    @Multipart
//    Flowable<HttpResult<String>> uploadAvater(@FieldMap Map<String,String> params,
//                                              @Part("avater") RequestBody avater);
    @POST("handleuser.php")
    @Multipart
    Flowable<HttpResult<String>> uploadAvater(@Part List<MultipartBody.Part> partList);
}
