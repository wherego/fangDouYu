package com.cokus.fangdouyu.ui.activity.player;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cokus.fangdouyu.R;
import com.cokus.fangdouyu.base.BaseActivity;
import com.cokus.fangdouyu.modle.GsonDouyuRoom;
import com.cokus.fangdouyu.mvp.base.BaseMvpActivity;
import com.cokus.fangdouyu.util.LogUtils;
import com.cokus.fangdouyu.util.ToastUtils;

/**
 * Created by chenzhuo on 2017/3/28.
 * 播放器类
 */

public class PlayerActivity extends BaseMvpActivity<LivePlayerPresenter,LivePlayerModel> implements LivePlayerContract.View {
    private String roomId;



    @Override
    protected void loadData() {
        LogUtils.e("test",""+roomId);
        roomId = getIntent().getStringExtra("roomId");
        LogUtils.e("test","---"+roomId);
        mPresenter.getDate(roomId);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {


    }

    @Override
    public void onRequestStart() {
//        ToastUtils.showLongToast(this,"start");
        LogUtils.e("test","start");
    }

    @Override
    public void onRequestEnd() {
//        ToastUtils.showLongToast(this,"end");
        LogUtils.e("test","end");
    }

    @Override
    public void getData(GsonDouyuRoom data) {
//        ToastUtils.showLongToast(this,data.getData().getLive_url()+"");
        LogUtils.e("test",data.getData().getLive_url());
        Intent intent = new Intent(PlayerActivity.this,PLVideoTextureActivity.class);
        intent.putExtra("path",data.getData().getLive_url());
        startActivity(intent);
    }
}
