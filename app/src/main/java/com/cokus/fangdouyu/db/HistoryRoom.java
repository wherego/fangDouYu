package com.cokus.fangdouyu.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by chenzhuo on 2017/4/27.
 */

@Entity
public class HistoryRoom {
    private String  roomId;
    private String  avatar;
    private String  avatar_small;
    private int online;
    @Generated(hash = 878663133)
    public HistoryRoom(String roomId, String avatar, String avatar_small,
            int online) {
        this.roomId = roomId;
        this.avatar = avatar;
        this.avatar_small = avatar_small;
        this.online = online;
    }
    @Generated(hash = 952898555)
    public HistoryRoom() {
    }
    public String getRoomId() {
        return this.roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar_small() {
        return this.avatar_small;
    }
    public void setAvatar_small(String avatar_small) {
        this.avatar_small = avatar_small;
    }
    public int getOnline() {
        return this.online;
    }
    public void setOnline(int online) {
        this.online = online;
    }

}
