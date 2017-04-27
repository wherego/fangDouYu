package com.cokus.fangdouyu.ui.activity.player.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cokus.fangdouyu.R;
import com.cokus.fangdouyu.base.BaseFragment;
import com.cokus.fangdouyu.ui.activity.player.ChatMsgAdapter;
import com.cokus.fangdouyu.ui.activity.player.danmu.DanmuProcess;
import com.cokus.fangdouyu.util.LogUtils;
import com.cokus.fangdouyu.util.RecyclerViewUtil;
import com.cokus.fangdouyu.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by chenzhuo on 2017/4/27.
 */

public class ChatFragment extends BaseFragment{
    @BindView(R.id.chatRecyclerView)
    RecyclerView chatRecyclerView;


    private  DanmuProcess.CallbackDanmu  callbackDanmu = new DanmuProcess.CallbackDanmu(){
        @Override
        public void callbackDanmu(String msg) {
            Message message = new Message();
            message.obj = msg;
            danmuHandler.sendMessage(message);
        }
    };

    public DanmuProcess.CallbackDanmu  getCallbackDanmu() {
        return callbackDanmu;
    }

    private List<String> chatmsg = new ArrayList<>();
    private ChatMsgAdapter chatMsgAdapter= new ChatMsgAdapter(chatmsg);

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


    @Override
    protected void initView() {
        chatmsg.add("<font color=\"#EE0000\">欢迎来到本直播间！！！</font>");
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatRecyclerView.setAdapter(chatMsgAdapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void loadData() {


    }
}
