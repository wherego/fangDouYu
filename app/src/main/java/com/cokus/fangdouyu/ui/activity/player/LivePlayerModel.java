package com.cokus.fangdouyu.ui.activity.player;

import com.cokus.fangdouyu.domain.LiveUrlClient;
import com.cokus.fangdouyu.domain.RestClient;
import com.cokus.fangdouyu.modle.GsonDouyuRoom;
import com.cokus.fangdouyu.util.douyu.BuildUrl;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by chenzhuo on 2017/4/20.
 */

public class LivePlayerModel implements LivePlayerContract.Model {
    @Override
    public Observable<GsonDouyuRoom> getData(String RoomId) {
        String path = BuildUrl.getDouyuRoomUrl(RoomId);
        HashMap<String,String> params = BuildUrl.getDouyuRoomParams(RoomId);
        return LiveUrlClient.getInstance().getLiveStreamService().getLiveUrl(path,params);
    }
}
