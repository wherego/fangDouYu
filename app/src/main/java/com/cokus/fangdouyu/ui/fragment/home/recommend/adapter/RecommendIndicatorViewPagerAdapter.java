package com.cokus.fangdouyu.ui.fragment.home.recommend.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cokus.fangdouyu.DYApplication;
import com.cokus.fangdouyu.R;
import com.cokus.fangdouyu.db.HistoryRoom;
import com.cokus.fangdouyu.event.RoomEvent;
import com.cokus.fangdouyu.modle.recommend.RecomendChannel;
import com.cokus.fangdouyu.modle.recommend.RecommendData;
import com.cokus.fangdouyu.ui.activity.player.LivePlayerActivity;
import com.cokus.fangdouyu.util.IntentUtils;
import com.cokus.fangdouyu.widget.viewpagerindicator.view.indicator.IndicatorViewPager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by coku on 16/11/3.
 */
public class RecommendIndicatorViewPagerAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter{
    private  Context context;
    private RecommendData recommendData;
    public RecommendIndicatorViewPagerAdapter(Context context,RecommendData recommendData){
        this.context = context;
        this.recommendData = recommendData;
    }

    @Override
    public int getCount() {
        return recommendData.getRecomendChannel().getData().size();
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            LayoutInflater inflate = LayoutInflater.from(context);
            convertView = inflate.inflate(R.layout.tab_circle, container, false);
        }
        return convertView;
    }

    @Override
    public View getViewForPage(final int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = new ImageView(context);
            convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
        }
        ImageView imageView = (ImageView) convertView;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        DYApplication.glideBitmap.display(imageView, recommendData.getRecomendChannel().getData().get(position).getPic_url());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecomendChannel.DataBean.RoomBean bean = recommendData.getRecomendChannel().getData().get(position).getRoom();
                HistoryRoom historyRoom = new HistoryRoom();
                historyRoom.setAvatar(bean.getNickname());
//                historyRoom.setAvatar_small(bean.getAvatar_small());
                historyRoom.setRoomId(bean.getRoom_id());
                historyRoom.setOnline(bean.getOnline());
                Intent intent =  IntentUtils.getIntent(context, LivePlayerActivity.class,null);
                context.startActivity(intent);
                EventBus.getDefault().postSticky(new RoomEvent(historyRoom));

            }
        });
        return convertView;
    }
}
