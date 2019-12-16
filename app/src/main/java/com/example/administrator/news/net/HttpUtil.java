package com.example.administrator.news.net;

import com.example.administrator.news.bean.News;
import com.example.administrator.news.bean.Video;
import com.example.administrator.news.net.entity.ResponseLogin;
import com.example.administrator.news.net.entity.ResponseNews;
import com.example.administrator.news.net.function.HttpResultFunction;
import com.example.administrator.news.net.interceptor.LoggingInterceptor;
import com.example.administrator.news.util.Const;

import org.reactivestreams.Subscriber;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018\4\20 0020.
 */

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    private ApiService mApiService;
    private static HttpUtil mHttpUtil = null;
    private HttpUtil() {
        LoggingInterceptor interceptor=new LoggingInterceptor();
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApiService = retrofit.create(ApiService.class);
    }

    public static HttpUtil getInstance() {
        if (mHttpUtil == null) {
            synchronized (HttpUtil.class) {
                mHttpUtil = new HttpUtil();
            }
        }
        return mHttpUtil;
    }

    public void getNews(Subscriber<ResponseNews> subscriber, Map<String,String> params) {
        Flowable<HttpResult<ResponseNews>> flowable = mApiService.getNews(params);
        flowable.map(new HttpResultFunction<ResponseNews>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public void getVideo(Subscriber<List<Video>> subscriber,Map<String,String> params) {
        Flowable<HttpResult<List<Video>>> flowable = mApiService.getVideo(params);
        flowable.map(new HttpResultFunction<List<Video>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }
    public void login(Subscriber<ResponseLogin> subscriber, Map<String,String> params){
        Flowable<HttpResult<ResponseLogin>> flowable=mApiService.login(params);
        flowable.map(new HttpResultFunction<ResponseLogin>())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(subscriber);
    }

    public void handlePraise(Subscriber<Integer> subscriber ,Map<String,Object> params){
        Flowable<HttpResult<Integer>> flowable=mApiService.handlePraise(params);
        flowable.map(new HttpResultFunction<Integer>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }
    public void getPraiseByUser(Subscriber<List<News>> subscriber,Map<String,Object> params){
        Flowable<HttpResult<List<News>>> flowable=mApiService.getPraiseByUser(params);
        flowable.map(new HttpResultFunction<List<News>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }
    public void uploadAvater(Subscriber<String>subscriber,List<MultipartBody.Part> partList){
        Flowable<HttpResult<String>> flowable=mApiService.uploadAvater(partList);
        flowable.map(new HttpResultFunction<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }
}
