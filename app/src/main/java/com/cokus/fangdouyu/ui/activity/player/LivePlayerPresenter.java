package com.cokus.fangdouyu.ui.activity.player;

import com.cokus.fangdouyu.modle.GsonDouyuRoom;
import com.cokus.fangdouyu.modle.concern.RecommendLiveRoom;
import com.google.gson.Gson;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenzhuo on 2017/4/20.
 */

public class LivePlayerPresenter extends LivePlayerContract.Presenter {
    @Override
    public void getDate(String roomID) {
        mRxManager.add(mModel.getData(roomID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GsonDouyuRoom>() {
                    @Override
                    public void onCompleted() {
                        mView.onRequestEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onRequestError(e.toString());
                        mView.onInternetError();
                    }

                    @Override
                    public void onNext(GsonDouyuRoom gsonDouyuRoom) {
                        mView.getData(gsonDouyuRoom);
                    }
                }));
    }

}
