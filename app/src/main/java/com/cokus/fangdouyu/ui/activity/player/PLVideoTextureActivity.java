package com.cokus.fangdouyu.ui.activity.player;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.cokus.fangdouyu.R;
import com.cokus.fangdouyu.modle.GsonDouyuRoom;
import com.cokus.fangdouyu.mvp.base.BaseMvpActivity;
import com.cokus.fangdouyu.util.RecyclerViewUtil;
import com.cokus.fangdouyu.util.ScreenUtils;
import com.cokus.fangdouyu.util.ToastUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.ui.widget.DanmakuView;
import qiu.niorgai.StatusBarCompat;
import rx.internal.util.PlatformDependent;


/**
 *  This is a demo activity of PLVideoTextureView
 */
public class PLVideoTextureActivity extends BaseMvpActivity<LivePlayerPresenter,LivePlayerModel> implements LivePlayerContract.View {

    private static final int MESSAGE_ID_RECONNECTING = 0x01;

    private MediaControllVertical mediaControllVertical;
    private MediaControllHortical mediaControllHortical;
    private PLVideoTextureView mVideoView;
    private Toast mToast = null;
    private String mVideoPath = null;
    private int mRotation = 0;
    private int mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_FIT_PARENT; //default
    private View mLoadingView;
    private View mCoverView = null;
    private boolean mIsActivityPaused = true;
    private int mIsLiveStreaming = 1;
    @BindView(R.id.load_img)
    ImageView loadImg;
    @BindView(R.id.player_layout_vertical)
    FrameLayout playerLayoutVertical;
    @BindView(R.id.danmakuView)
    IDanmakuView mDanmakuView;
    @BindView(R.id.chatRecyclerView)
    RecyclerView chatRecyclerView;

    private List<String> chatmsg = new ArrayList<>();
    private ChatMsgAdapter chatMsgAdapter;

    private String mRoomId;

    private  boolean  isFull = false;
    private DanmuProcess mDanmuProcess;

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

    Handler danmuHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            chatmsg.add((String) msg.obj);
            chatMsgAdapter.notifyDataSetChanged();
            if(RecyclerViewUtil.isSlideToBottom(chatRecyclerView)){
                chatRecyclerView.scrollToPosition(chatMsgAdapter.getItemCount()-1);
            }
        }
    };


    private void playDanmu() {
        mDanmuProcess = new DanmuProcess(this, mDanmakuView, Integer.parseInt(mRoomId), new DanmuProcess.CallbackDanmu() {
            @Override
            public void callbackDanmu(String chat) {
                Message msg =new Message();
                msg.obj = chat;
                danmuHandler.sendMessage(msg);
            }
        });
        mDanmakuView.hide();
        mDanmuProcess.start();
    }


    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(PLVideoTextureActivity.this, Color.parseColor("#cc000000"));
        mVideoView = (PLVideoTextureView) findViewById(R.id.VideoView);
        mVideoView.getLayoutParams().height = ScreenUtils.getScreenWidth(this)*720/1280;
        playerLayoutVertical.getLayoutParams().height  =ScreenUtils.getScreenWidth(this)*720/1280;
        mLoadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(mLoadingView);
        mLoadingView.setVisibility(View.VISIBLE);
        mCoverView = (ImageView) findViewById(R.id.CoverView);
        mCoverView.getLayoutParams().height  =ScreenUtils.getScreenWidth(this)*720/1280;
        mVideoView.setCoverView(mCoverView);
        mRoomId = getIntent().getStringExtra("roomId");
        loadImg.setImageResource(R.drawable.load_content_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadImg.getDrawable();
        animationDrawable.start();
        chatMsgAdapter = new ChatMsgAdapter(chatmsg);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatMsgAdapter);
        playDanmu();
    }


    private void initPlayerVer(){
        mediaControllVertical = new MediaControllVertical(this, new MediaControllVertical.MediaControllerVer() {
            @Override
            public void back() {
                finish();
            }
            @Override
            public void full() {
                    isFull = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mVideoView.getLayoutParams().height = ScreenUtils.getScreenHeight(PLVideoTextureActivity.this)*1280/720;
                    playerLayoutVertical.getLayoutParams().height  =ScreenUtils.getScreenHeight(PLVideoTextureActivity.this)*1280/720;
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
                StatusBarCompat.setStatusBarColor(PLVideoTextureActivity.this, Color.parseColor("#cc000000"));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mVideoView.getLayoutParams().height = ScreenUtils.getScreenWidth(PLVideoTextureActivity.this)*720/1280;
                    playerLayoutVertical.getLayoutParams().height  =ScreenUtils.getScreenWidth(PLVideoTextureActivity.this)*720/1280;
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
        mPresenter.getDate(mRoomId);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_pl_video_texture;
    }





    @Override
    protected void onPause() {
        super.onPause();
        mToast = null;
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
        mVideoView.stopPlayback();
        mDanmuProcess.finish();
    }

    public void onClickRotate(View v) {
        mRotation = (mRotation + 90) % 360;
        mVideoView.setDisplayOrientation(mRotation);
    }



    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            boolean isNeedReconnect = false;
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    showToastTips("Invalid URL !");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    showToastTips("404 resource not found !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    showToastTips("Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    showToastTips("Connection timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    showToastTips("Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    showToastTips("Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    showToastTips("Network IO Error !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    showToastTips("Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    showToastTips("Prepare timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    showToastTips("Read frame timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_HW_DECODE_FAILURE:
                    setOptions();
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    showToastTips("unknown error !");
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
            showToastTips("Play Completed !");
            finish();
        }
    };

    private void showToastTips(final String tips) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(PLVideoTextureActivity.this, tips, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    private void sendReconnectMessage() {
        showToastTips("正在重连...");
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
            if (mIsActivityPaused || !Utils.isLiveStreamingAvailable()) {
                finish();
                return;
            }
            if (!Utils.isNetworkAvailable(PLVideoTextureActivity.this)) {
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
