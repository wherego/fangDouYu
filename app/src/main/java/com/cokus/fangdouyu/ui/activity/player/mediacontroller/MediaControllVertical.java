package com.cokus.fangdouyu.ui.activity.player.mediacontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.cokus.fangdouyu.R;
import com.cokus.fangdouyu.util.ScreenUtils;
import com.cokus.fangdouyu.util.ToastUtils;
import com.pili.pldroid.player.IMediaController;

/**
 * Created by chenzhuo on 2017/4/25.
 */

public class MediaControllVertical extends FrameLayout implements IMediaController,View.OnClickListener {
    private PopupWindow mWindow;
    private Context mContext;
    private View mRoot;
    private View mAnchor;

    private View backView;
    private View fullView;
    private final static int HIDE = 1000;
    private final static int SHOWTIME = 3000;

    private MediaControllerVer mediaControllerVer;


    public MediaControllVertical(@NonNull Context context,MediaControllerVer mediaControllerVer) {
        super(context);
        this.mContext = context;
        initFloatingWindow();
        this.mediaControllerVer = mediaControllerVer;
    }

    public MediaControllVertical(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaControllVertical(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case HIDE:
                    hide();
                    break;
            }
        }
    };

    private void initFloatingWindow() {
        mWindow = new PopupWindow(mContext);
        mWindow.setFocusable(false);
        mWindow.setBackgroundDrawable(null);
        mWindow.setOutsideTouchable(false);
    }

    private  void initMediaControll(View mRoot){
        mRoot.findViewById(R.id.verMediaControll).getLayoutParams().height =ScreenUtils.getScreenWidth(mContext)*720/1280;
        backView = mRoot.findViewById(R.id.player_back);
        fullView = mRoot.findViewById(R.id.player_full);

        backView.setOnClickListener(this);
        fullView.setOnClickListener(this);
    }

    protected View makeControllerView() {
        return ((LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mediacontroller_vertical, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show(SHOWTIME);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(SHOWTIME);
        return false;
    }


    @Override
    public void setMediaPlayer(MediaPlayerControl mediaPlayerControl) {

    }

    @Override
    public void show() {
        show(2000);
    }

    @Override
    public void show(int i) {
        if(isShowing()){
            hide();
            return;
        }
        int[] location = new int[2];
        if (mAnchor != null) {
            mAnchor.getLocationOnScreen(location);
            Rect anchorRect = new Rect(location[0], location[1],
                    location[0] + mAnchor.getWidth(), location[1]
                    + mAnchor.getHeight());

//            mWindow.setAnimationStyle(mAnimStyle);
            mWindow.showAtLocation(mAnchor, Gravity.TOP,
                    anchorRect.left, 0);
        } else {
            Rect anchorRect = new Rect(location[0], location[1],
                    location[0] + mRoot.getWidth(), location[1]
                    + mRoot.getHeight());

//            mWindow.setAnimationStyle(mAnimStyle);
            mWindow.showAtLocation(mRoot, Gravity.BOTTOM,
                    anchorRect.left, 0);
        }
        mHandler.removeMessages(HIDE);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE),
                i);
    }

    @Override
    public void hide() {
      mWindow.dismiss();
    }

    @Override
    public boolean isShowing() {
        return mWindow.isShowing();
    }

    @Override
    public void setAnchorView(View view) {
        mAnchor = view;
        mRoot = makeControllerView();
        mWindow.setContentView(mRoot);
        mWindow.setWidth(LayoutParams.MATCH_PARENT);
        mWindow.setHeight(ScreenUtils.getScreenWidth(mContext)*720/1280);
        initMediaControll(mRoot);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.player_back:
                mediaControllerVer.back();
                break;
            case R.id.player_full:
                mediaControllerVer.full();
                break;
        }
    }

   public interface MediaControllerVer{
        void back();
        void full();
    }
}
