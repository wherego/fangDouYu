package com.cokus.fangdouyu.ui.activity.player;

import com.cokus.fangdouyu.modle.GsonDouyuRoom;
import com.cokus.fangdouyu.modle.concern.RecommendLiveRoom;
import com.cokus.fangdouyu.mvp.BaseModel;
import com.cokus.fangdouyu.mvp.BasePresenter;
import com.cokus.fangdouyu.mvp.BaseView;
import com.cokus.fangdouyu.ui.fragment.concern.UnloginConcernContract;

import rx.Observable;

/**
 * Created by chenzhuo on 2017/4/20.
 */

public interface LivePlayerContract {
    interface  Model extends BaseModel {
        Observable<GsonDouyuRoom> getData(String roomId);
    }

    interface View extends BaseView {
        void getData(GsonDouyuRoom data);

    }

    abstract class Presenter extends BasePresenter<LivePlayerContract.Model,LivePlayerContract.View> {
        public abstract void getDate(String roomId);
    }

}
