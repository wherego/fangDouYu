package com.cokus.fangdouyu.ui.activity.player;

import android.text.Html;
import android.widget.TextView;

import com.cokus.fangdouyu.R;
import com.cokus.fangdouyu.base.adapter.BaseQuickAdapter;
import com.cokus.fangdouyu.base.adapter.BaseViewHolder;
import com.cokus.fangdouyu.modle.concern.RecommendLiveRoom;

import java.util.List;

/**
 * Created by chenzhuo on 2017/4/26.
 */

public class ChatMsgAdapter extends BaseQuickAdapter<String> {
    public ChatMsgAdapter(List<String> data) {
        super(R.layout.item_chatmsg, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {
         helper.setText(R.id.chat, Html.fromHtml(item));
    }
}
