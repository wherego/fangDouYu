package com.cokus.fangdouyu.mvp.base;

import android.os.Bundle;

import com.cokus.fangdouyu.base.BaseFragment;
import com.cokus.fangdouyu.mvp.BaseModel;
import com.cokus.fangdouyu.mvp.BasePresenter;
import com.cokus.fangdouyu.mvp.BaseView;
import com.cokus.fangdouyu.mvp.util.TUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;


/**
 *
 * @param <P>
 * @param <M>
 */

public abstract  class BaseMvpEventbusFragment<P extends BasePresenter, M extends BaseModel> extends BaseFragment implements BaseView {

    public P mPresenter;

    public M mModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof BaseView) {
            mPresenter.setVM(this, mModel);
        }
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestEnd() {

    }

    @Override
    public void onInternetError() {
    }

    @Override
    public void onRequestError(String msg) {
        Logger.e("REQUEST_ERROR ==== ", msg);
    }

}
