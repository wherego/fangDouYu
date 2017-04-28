package com.cokus.fangdouyu.ui.fragment.concern.adapter;

import android.view.View;
import android.widget.ImageView;

import com.cokus.fangdouyu.DYApplication;
import com.cokus.fangdouyu.R;
import com.cokus.fangdouyu.base.adapter.BaseQuickAdapter;
import com.cokus.fangdouyu.base.adapter.BaseViewHolder;
import com.cokus.fangdouyu.listener.OnItemClick;
import com.cokus.fangdouyu.modle.concern.RecommendLiveRoom;
import com.cokus.fangdouyu.modle.live.LiveRoom;

import java.util.List;

/**
 * Created by coku on 16/11/12.
 */
public class RecommendLiveRoomAdapter extends BaseQuickAdapter<RecommendLiveRoom.DataBean.NewBieBean> {
    private OnItemClick onItemClick;
    public RecommendLiveRoomAdapter(List<RecommendLiveRoom.DataBean.NewBieBean> data, OnItemClick onItemClick) {
        super(R.layout.item_recommend_room, data);
        this.onItemClick = onItemClick;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RecommendLiveRoom.DataBean.NewBieBean item) {
        helper.setText(R.id.room_title,item.getRoom_name())
                .setText(R.id.onlinetv,item.getOnline()+"")
                .setText(R.id.authortv,item.getNickname());;
        DYApplication.glideBitmap.displayRound((ImageView) helper.getView(R.id.room_img),item.getRoom_src());
        helper.getView(R.id.room_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClick(helper.getAdapterPosition(),item);
            }
        });

    }
}
