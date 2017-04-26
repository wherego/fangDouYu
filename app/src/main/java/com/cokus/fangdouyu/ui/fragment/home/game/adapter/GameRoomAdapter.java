package com.cokus.fangdouyu.ui.fragment.home.game.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.cokus.fangdouyu.DYApplication;
import com.cokus.fangdouyu.R;
import com.cokus.fangdouyu.base.adapter.BaseQuickAdapter;
import com.cokus.fangdouyu.base.adapter.BaseViewHolder;
import com.cokus.fangdouyu.modle.game.Game;
import com.cokus.fangdouyu.ui.activity.player.PlayerActivity;
import com.cokus.fangdouyu.util.IntentUtils;
import com.cokus.fangdouyu.util.ToastUtils;

import java.util.List;

/**
 * Created by coku on 16/11/2.
 */
public class GameRoomAdapter extends BaseQuickAdapter<Game.DataBean.RoomListBean>{


    public GameRoomAdapter(List<Game.DataBean.RoomListBean> data) {
        super(R.layout.item_recommend_room, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Game.DataBean.RoomListBean item) {
        helper.setText(R.id.room_title,item.getRoom_name())
                .setText(R.id.onlinetv,item.getOnline()+"")
                .setText(R.id.authortv,item.getNickname());;
        DYApplication.glideBitmap.displayRound((ImageView) helper.getView(R.id.room_img),item.getRoom_src());
        helper.getView(R.id.room_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLongToast(DYApplication.getAppContext(),"test");
//                Intent intent = IntentUtils.getIntent(DYApplication.getAppContext(), PlayerActivity.class,null);
//                DYApplication.getAppContext().startActivity(intent);
            }
        });

    }


}
