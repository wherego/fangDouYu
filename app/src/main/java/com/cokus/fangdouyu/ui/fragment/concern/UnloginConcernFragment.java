package com.cokus.fangdouyu.ui.fragment.concern;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.cokus.fangdouyu.R;
import com.cokus.fangdouyu.db.HistoryRoom;
import com.cokus.fangdouyu.event.RoomEvent;
import com.cokus.fangdouyu.listener.OnItemClick;
import com.cokus.fangdouyu.modle.concern.RecommendLiveRoom;
import com.cokus.fangdouyu.modle.live.LiveRoom;
import com.cokus.fangdouyu.mvp.base.BaseMvpFragment;
import com.cokus.fangdouyu.ui.activity.player.LivePlayerActivity;
import com.cokus.fangdouyu.ui.fragment.concern.adapter.RecommendLiveRoomAdapter;
import com.cokus.fangdouyu.util.IntentUtils;
import com.cokus.fangdouyu.widget.MultiStateView;
import com.cokus.fangdouyu.widget.refresh.DouYuRefreshEmptyBottem;
import com.cokus.fangdouyu.widget.refresh.DouYuRefreshHeader;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by coku on 16/11/13.
 */
public class UnloginConcernFragment extends BaseMvpFragment<UnloginConcernPresenter,UnloginConcernModel> implements UnloginConcernContract.View{
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.multistateview)
    MultiStateView multiStateView;



    private RecommendLiveRoomAdapter adapter;
    private View headView;

    @Inject
    public UnloginConcernFragment() {
    }

    @Override
    protected void initActionBar(int actionbarID) {
        super.initActionBar(actionbarID);
        actionBar.setBarCenter("关注",0,null);
    }

    @Override
    protected void initView() {
        initActionBar(R.id.action_bar);
        initHead();
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_unlogin_concern;
    }

    @Override
    protected void loadData() {
        mPresenter.getDate();

    }

    @Override
    public void getData(RecommendLiveRoom data) {
        adapter = new RecommendLiveRoomAdapter(data.getData().getNew_bie(), new OnItemClick() {
            @Override
            public <T> void OnItemClick(int position, T t) {
                RecommendLiveRoom.DataBean.NewBieBean bean = (RecommendLiveRoom.DataBean.NewBieBean) t;
                Intent intent =  IntentUtils.getIntent(getActivity(), LivePlayerActivity.class,null);
                startActivity(intent);
                HistoryRoom historyRoom = new HistoryRoom();
                historyRoom.setAvatar(bean.getNickname());
//                historyRoom.setAvatar_small(bean.get);
                historyRoom.setRoomId(bean.getRoom_id());
                historyRoom.setOnline(bean.getOnline());
                EventBus.getDefault().postSticky(new RoomEvent(historyRoom));
            }
        });
        adapter.addHeaderView(headView);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onRequestStart() {
        if(adapter == null || adapter.getItemCount() == 0)
            multiStateView.setViewState(MultiStateView.STATE_LOADING);
    }

    @Override
    public void onRequestEnd() {
        if(adapter.getItemCount() == 0){
            multiStateView.setViewState(MultiStateView.STATE_EMPTY);
        }else {
            multiStateView.setViewState(MultiStateView.STATE_CONTENT);
        }
    }


    private  void initHead(){
        LayoutInflater inflate = LayoutInflater.from(getActivity());
        headView = inflate.inflate(R.layout.head_unlogin_concern, null, false);
    }
}
