package com.cokus.fangdouyu.event;

import com.cokus.fangdouyu.db.HistoryRoom;

/**
 * Created by chenzhuo on 2017/4/27.
 */

public class RoomEvent {
    public final HistoryRoom room;

    public RoomEvent(HistoryRoom room) {
        this.room = room;
    }
}
