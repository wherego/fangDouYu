package com.cokus.fangdouyu.domain;

import com.cokus.fangdouyu.domain.service.ConcernService;
import com.cokus.fangdouyu.domain.service.HomeService;
import com.cokus.fangdouyu.domain.service.LiveService;
import com.cokus.fangdouyu.domain.service.LiveStreamService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

;

/**
 * Created by coku on 16/10/31.
 */
public class LiveUrlClient {
    private  static String HOST_NAME="http://coapi.douyucdn.cn";

    private static final LiveUrlClient instance = new LiveUrlClient();



    private LiveStreamService liveStreamService;



    public LiveStreamService getLiveStreamService() {
        return liveStreamService == null ?  create(LiveStreamService.class) :liveStreamService;
    }



    public static LiveUrlClient getInstance() {
        return instance;
    }


    private Retrofit retrofit;

    public LiveUrlClient() {
        createRetrofit();
    }

    private void createRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(HOST_NAME)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    public <T> T create(Class<T> clazz) {
        return (T) retrofit.create(clazz);
    }


}
