package com.cokus.fangdouyu.ui.activity.player;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cokus.fangdouyu.DYApplication;
import com.cokus.fangdouyu.R;
import com.cokus.fangdouyu.db.HistoryRoom;
import com.cokus.fangdouyu.event.RoomEvent;
import com.cokus.fangdouyu.gen.HistoryRoomDao;
import com.cokus.fangdouyu.modle.GsonDouyuRoom;
import com.cokus.fangdouyu.mvp.base.BaseMvpActivity;
import com.cokus.fangdouyu.mvp.base.BaseMvpEventbusActivity;
import com.cokus.fangdouyu.ui.activity.player.danmu.DanmuProcess;
import com.cokus.fangdouyu.ui.activity.player.fragment.ChatFragment;
import com.cokus.fangdouyu.ui.activity.player.mediacontroller.MediaControllHortical;
import com.cokus.fangdouyu.ui.activity.player.mediacontroller.MediaControllPhone;
import com.cokus.fangdouyu.ui.activity.player.mediacontroller.MediaControllVertical;
import com.cokus.fangdouyu.ui.fragment.home.HomeFragment;
import com.cokus.fangdouyu.ui.fragment.home.game.GameFragment;
import com.cokus.fangdouyu.ui.fragment.home.recommend.RecommendFragment;
import com.cokus.fangdouyu.util.DensityUtil;
import com.cokus.fangdouyu.util.LogUtils;
import com.cokus.fangdouyu.util.NetworkUtils;
import com.cokus.fangdouyu.util.RecyclerViewUtil;
import com.cokus.fangdouyu.util.ScreenUtils;
import com.cokus.fangdouyu.util.ToastUtils;
import com.cokus.fangdouyu.widget.viewpagerindicator.view.indicator.FixedIndicatorView;
import com.cokus.fangdouyu.widget.viewpagerindicator.view.indicator.IndicatorViewPager;
import com.cokus.fangdouyu.widget.viewpagerindicator.view.indicator.slidebar.ColorBar;
import com.cokus.fangdouyu.widget.viewpagerindicator.view.indicator.slidebar.LayoutBar;
import com.cokus.fangdouyu.widget.viewpagerindicator.view.indicator.slidebar.ScrollBar;
import com.cokus.fangdouyu.widget.viewpagerindicator.view.indicator.transition.OnTransitionTextListener;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import master.flame.danmaku.controller.IDanmakuView;
import qiu.niorgai.StatusBarCompat;


/**
 *  This is a demo activity of PLVideoTextureView
 */
public class LivePlayerActivity extends BaseMvpEventbusActivity<LivePlayerPresenter,LivePlayerModel> implements LivePlayerContract.View {

    private static final int MESSAGE_ID_RECONNECTING = 0x01;

    private MediaControllVertical mediaControllVertical;
    private MediaControllHortical mediaControllHortical;
    private MediaControllPhone mediaControllPhone;
    private PLVideoTextureView mVideoView;
    private String mVideoPath = null;
    private View mLoadingView;
    private View mCoverView = null;
    private boolean mIsActivityPaused = true;
    @BindView(R.id.load_img)
    ImageView loadImg;
    @BindView(R.id.player_layout_vertical)
    FrameLayout playerLayoutVertical;
    @BindView(R.id.danmakuView)
    IDanmakuView mDanmakuView;


    @BindView(R.id.fragment_livechat_indicator)
    FixedIndicatorView livechatIndicator;
    @BindView(R.id.fragment_livechat_viewPager)
    ViewPager viewPager;
    private int index;
    private String tabs[] = {"聊天", "主播"};
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;



    private  boolean  isFull = false;
    private DanmuProcess mDanmuProcess;
    private ChatFragment chatFragment = new ChatFragment();
    private HistoryRoom historyRoom;



    private void setOptions() {
        int codec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        AVOptions mAVOptions = new AVOptions();
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        mAVOptions.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        // whether start play automatically after prepared, default value is 1
        mAVOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
        mVideoView.setAVOptions(mAVOptions);
    }


    @Subscribe(sticky = true)
    public void onRoomInfoMessage(RoomEvent roomEvent){
        historyRoom =  roomEvent.room;
        mPresenter.getDate(historyRoom.getRoomId());
        playDanmu();
        addPlayRecord();
    }


    public void addPlayRecord(){
         long test= DYApplication.getInstances().getDaoSession().getHistoryRoomDao().insert(historyRoom);
         List<HistoryRoom> historyRooms = DYApplication.getInstances().getDaoSession().getHistoryRoomDao().loadAll();
        ToastUtils.showLongToast(this,historyRooms.size()+"ssssssssssssss");
    }



    private void playDanmu() {

        mDanmuProcess = new DanmuProcess(this, mDanmakuView, Integer.parseInt(historyRoom.getRoomId()), chatFragment.getCallbackDanmu());
        mDanmakuView.hide();
        mDanmuProcess.start();
    }


    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(LivePlayerActivity.this, Color.parseColor("#cc000000"));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mVideoView = (PLVideoTextureView) findViewById(R.id.VideoView);
        mVideoView.getLayoutParams().height = ScreenUtils.getScreenWidth(this)*720/1280;
        playerLayoutVertical.getLayoutParams().height  =ScreenUtils.getScreenWidth(this)*720/1280;
        mLoadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(mLoadingView);
        mLoadingView.setVisibility(View.VISIBLE);
        mCoverView = (ImageView) findViewById(R.id.CoverView);
        mCoverView.getLayoutParams().height  =ScreenUtils.getScreenWidth(this)*720/1280;
        mVideoView.setCoverView(mCoverView);
        loadImg.setImageResource(R.drawable.load_content_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadImg.getDrawable();
        animationDrawable.start();


        mVideoView.setOnVideoSizeChangedListener(new PLMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1, int i2, int i3) {
                if(i1>i){
                    playerLayoutVertical.getLayoutParams().height = ScreenUtils.getScreenHeight(LivePlayerActivity.this);
                    mVideoView.getLayoutParams().height = ScreenUtils.getScreenHeight(LivePlayerActivity.this);
                    mediaControllPhone = new MediaControllPhone(LivePlayerActivity.this, new MediaControllPhone.MediaControllerPhone() {
                        @Override
                        public void back() {
                            myFinish();
                        }
                    });
                    mVideoView.setMediaController(mediaControllPhone);
                }
            }
        });
        inittabs();

    }

    void inittabs(){
        index = 0;
        switch (index) {
            case 0:
                ColorBar line1 = new ColorBar(getApplicationContext(), Color.parseColor("#ffff921b"), 5);
                line1.setWidth(DensityUtil.dip2px(this, 85));
                line1.setHeight(DensityUtil.dip2px(this, 3));
                livechatIndicator.setScrollBar(line1);
                break;
            case 1:
                livechatIndicator.setScrollBar(new ColorBar(getApplicationContext(), Color.parseColor("#ffff921b"), 0, ScrollBar.Gravity.CENTENT_BACKGROUND));
                break;
            case 2:
                livechatIndicator.setScrollBar(new ColorBar(getApplicationContext(), Color.parseColor("#ffff921b"), 5, ScrollBar.Gravity.TOP));
                break;
            case 3:
                livechatIndicator.setScrollBar(new LayoutBar(getApplicationContext(), R.layout.layout_slidebar, ScrollBar.Gravity.CENTENT_BACKGROUND));
                break;
        }
        float unSelectSize = 15;
        float selectSize = 15;
        int selectColor = Color.parseColor("#ffff921b");
        int unSelectColor = Color.BLACK;
        livechatIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        viewPager.setOffscreenPageLimit(5);
        indicatorViewPager = new IndicatorViewPager(livechatIndicator, viewPager);
        inflate = LayoutInflater.from(getApplicationContext());
        // 注意这里 的FragmentManager 是 getChildFragmentManager(); 因为是在Fragment里面
        // 而在activity里面用FragmentManager 是 getSupportFragmentManager()
        indicatorViewPager.setAdapter(new LivePlayerActivity.MyAdapter(getSupportFragmentManager()));
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_textview, container, false);
                fragments.add(chatFragment);
                fragments.add(new RecommendFragment());
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabs[position]);
            return convertView;
        }


        @Override
        public Fragment getFragmentForPage(int position) {
            return fragments.get(position);
        }
    }


    private void initPlayerVer(){
        mediaControllVertical = new MediaControllVertical(this, new MediaControllVertical.MediaControllerVer() {
            @Override
            public void back() {
                myFinish();
            }
            @Override
            public void full() {
                    isFull = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mVideoView.getLayoutParams().height = ScreenUtils.getScreenHeight(LivePlayerActivity.this)*1280/720;
                    playerLayoutVertical.getLayoutParams().height  =ScreenUtils.getScreenHeight(LivePlayerActivity.this)*1280/720;
                    initPlayerHor();
                    mDanmakuView.show();
            }
        });
        mVideoView.setMediaController(mediaControllVertical);
    }

    /**
     *
     */
    private void initPlayerHor(){
        mediaControllHortical = new MediaControllHortical(this, new MediaControllHortical.MediaControllerHor() {
            @Override
            public void back() {
                finish();
            }
            @Override
            public void full() {
                    isFull = false;
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                StatusBarCompat.setStatusBarColor(LivePlayerActivity.this, Color.parseColor("#cc000000"));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mVideoView.getLayoutParams().height = ScreenUtils.getScreenWidth(LivePlayerActivity.this)*720/1280;
                    playerLayoutVertical.getLayoutParams().height  =ScreenUtils.getScreenWidth(LivePlayerActivity.this)*720/1280;
                initPlayerVer();
                mDanmakuView.hide();
            }
        });
        mVideoView.setMediaController(mediaControllHortical);
    }


    private void playInit(){
        setOptions();
        initPlayerVer();
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setVideoPath(mVideoPath);
        mVideoView.start();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_pl_video_texture;
    }


    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
        mIsActivityPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsActivityPaused = false;
        mVideoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mVideoView!=null){ mVideoView.stopPlayback();}
        if(mDanmuProcess!=null){mDanmuProcess.finish();}
    }


    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            boolean isNeedReconnect = false;
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"Invalid URL !");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"404 resource not found !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"Connection timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"Network IO Error !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"Prepare timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"Read frame timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_HW_DECODE_FAILURE:
                    setOptions();
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    ToastUtils.showLongToast(LivePlayerActivity.this,"unknown error !");
                    break;
            }
            // Todo pls handle the error status here, reconnect or call finish()
            if (isNeedReconnect) {
                sendReconnectMessage();
            } else {
                finish();
            }
            return true;
        }
    };

    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            ToastUtils.showLongToast(LivePlayerActivity.this,"Play Completed !");
            finish();
        }
    };


    private void sendReconnectMessage() {
        ToastUtils.showLongToast(this,"正在重连...");
        mLoadingView.setVisibility(View.VISIBLE);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 500);
    }

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING) {
                return;
            }
            if (mIsActivityPaused ) {
                finish();
                return;
            }
            if (!NetworkUtils.isConnected(LivePlayerActivity.this)) {
                sendReconnectMessage();
                return;
            }
            mVideoView.setVideoPath(mVideoPath);
            mVideoView.start();
        }
    };

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestEnd() {

    }

    @Override
    public void getData(GsonDouyuRoom data) {
        mVideoPath = data.getData().getHls_url();
        playInit();
    }
}
