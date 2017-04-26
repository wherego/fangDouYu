package com.cokus.fangdouyu.domain.service;

import com.cokus.fangdouyu.modle.GsonDouyuRoom;

import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by chenzhuo on 2017/4/20.
 */

public interface LiveStreamService {
    @GET
    Observable<GsonDouyuRoom>  getLiveUrl(@Url String  path, @HeaderMap HashMap<String,String> Auths);
}
